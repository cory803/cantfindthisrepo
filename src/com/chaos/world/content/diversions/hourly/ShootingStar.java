package com.chaos.world.content.diversions.hourly;

import com.chaos.GameSettings;
import com.chaos.engine.task.Task;
import com.chaos.engine.task.TaskManager;
import com.chaos.model.*;
import com.chaos.model.container.impl.Equipment;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.util.MathUtil;
import com.chaos.util.Misc;
import com.chaos.util.NameUtils;
import com.chaos.world.World;
import com.chaos.world.content.CustomObjects;
import com.chaos.world.content.diversions.Diversion;
import com.chaos.world.content.skill.impl.mining.Mining;
import com.chaos.world.content.skill.impl.mining.MiningData;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;
import org.scripts.kotlin.content.dialog.npcs.ShootingStarAlien;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author relex lawl
 */
public final class ShootingStar extends GameObject implements HourlyDiversion, Diversion {

	public ShootingStar(int id, int direction, int type, Position position) {
		super(id, direction, type, position);
	}
	
	private Stage stage;

	private Location location = getRandomLocation();
	
	private int minerals;
	
	private int ticks;
	
	public Location getLocation() {
		return location;
	}
	
	public String getTimeToCrash() {
		int ticksLeft = SPAWN_TIMER - ticks;
		if (ticksLeft <= 0)
			return "The shooting star has already crashed!";
		
		return "The shooting star will crash in " + MathUtil.getTimeForTicks(ticksLeft) + ".";
	}
	
	@Override
	public void diversionPulse() {
		if (stage == null) {
			ticks++;
			if (ticks >= SPAWN_TIMER || (!GameSettings.STARTED_SERVER && ticks >= 50)) {
				GameSettings.STARTED_SERVER = true;
				stage = Stage.values()[0];
				setPosition(location.getPosition());
				
				edgeMeteora = new NPC(4515, new Position(3102, 3492), Direction.NORTH) {

				};
				
				varrockMeteora = new NPC(4515, new Position(3220, 3436), Direction.SOUTH) {

				};
				CustomObjects.spawnGlobalObject(this);
				World.register(varrockMeteora);
				World.sendMessage("<icon=0><shad=FF8C38>A shooting star has just crashed " + location.getClue() + "!");
			}
		}
	}

	@Override
	public void forcedEnd() {
		stage = null;
		location = getRandomLocation();
		ticks = 0;
		minerals = 0;
		setId(Stage.values()[0].getObjectId());
		World.deregister(edgeMeteora);
		World.deregister(varrockMeteora);
		CustomObjects.deleteGlobalObject(this);
		HourlyDiversionManager.chooseNextDiversion();
	}

	@Override
	public void setTimer(int timer) {
		this.ticks = (SPAWN_TIMER - timer);
	}

	@Override
	public boolean hasMenuAction(final Player player, int option) {
		if (stage == null)
			return true;
		if (option == 1) {
			if (player.getDiversion() == this)
				return true;
			if (player.getSkillManager().getCurrentLevel(Skill.MINING) < stage.getLevelRequirement(player)) {
				player.getPacketSender().sendMessage("You need a mining level of " + stage.getLevelRequirement(player) + " to mine this layer of the crashed star!");
				return true;
			}
			final MiningData.Pickaxe pickaxe = MiningData.forPick(MiningData.getPickaxe(player));
			if (pickaxe == null) {
				player.getPacketSender().sendMessage("You do not have a pickaxe which you have the required level to use.");
				return true;
			}			
			if (player.getInventory().isFull()
					&& !player.getInventory().contains(STARDUST)) {
				player.getPacketSender().sendMessage("Not enough space in your inventory.");
				return true;
			}
			if (player.getDiversionTask() != null) {
				player.getDiversionTask().stop();
			}
			player.setDiversion(this);
			player.performAnimation(new Animation(pickaxe.getAnim()));
						
			final Task task = new Task() {
				int stardustPossesed = player.getInventory().getAmount(STARDUST);
				int animationTick = 4;
				int ticksForReward = pickaxe.ordinal() + MathUtil.random(stage.getMiningTimerModifier());
				@Override
				public void execute() {
					if (stage == null || player.getDiversion() != ShootingStar.this) {
						player.performAnimation(new Animation(65535));
						stop();
						return;
					}
					if (player.getSkillManager().getCurrentLevel(Skill.MINING) < stage.getLevelRequirement(player)) {
						player.getPacketSender().sendMessage("You need a mining level of " + stage.getLevelRequirement(player) + " to mine this layer of the crashed star!");
						stop();
						return;
					}
					animationTick--;
					ticksForReward--;
					
					if (animationTick <= 0) {
						player.performAnimation(new Animation(pickaxe.getAnim()));
						animationTick = 4;
					}
					if (ticksForReward <= 0) {
						ticksForReward = pickaxe.ordinal() + MathUtil.random(stage.getMiningTimerModifier());
						if (stardustPossesed + 1 <= STARDUST_LIMIT) {
							stardustPossesed++;
							player.getInventory().add(new Item(STARDUST, 1));
							player.getPacketSender().sendMessage("You manage to collect some minerals.");
							if(player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 20786 || player.getInventory().contains(20786)) {
								player.getInventory().add(new Item(STARDUST, 1));
								stardustPossesed++;
								minerals++;
							}
						}
						player.getSkillManager().addSkillExperience(Skill.MINING, stage.getExperience());
						minerals++;
						
						if (minerals >= stage.getMinerals()) {
							if (stage.ordinal() + 1 < Stage.values().length) {
								final Stage newStage = Stage.values()[stage.ordinal() + 1];
								CustomObjects.deleteGlobalObject(ShootingStar.this);
								setId(newStage.getObjectId());
								stage = newStage;
								minerals = 0;
								CustomObjects.spawnGlobalObject(ShootingStar.this);
							} else {
								forcedEnd();
								spawnAlien();
							}
						}
					}
				}
			};
			player.setDiversionTask(task);
			TaskManager.submit(task);
		} else if (option == 2) {
			int progress = (minerals  * 100) / stage.getMinerals();
			player.getPacketSender().sendMessage("This star is currently in its " + stage.getName() + " stage. This layer is " + progress + "% destroyed.");
		}
		return true;
	}
		
	private static Location getRandomLocation() {
		final Location[] locations = Location.values();
		return locations[MathUtil.random(locations.length - 1)];
	}

	private void spawnAlien() {
		final Alien alien = new Alien(8091, getPosition(), Direction.NORTH, -1);
		World.register(alien);
		
		TaskManager.submit(new Task(180) {
			int ticks = 0;
			@Override
			public void execute() {		
				ticks++;
				if (ticks == 1) {
					alien.setForcedChat("Good bye and thank you, humans!");

					alien.performAnimation(new Animation(10354));
					
					
					for (int x = -1; x < 2; x++) {
						for (int y = -1; y < 2; y++) {
							World.sendStillGraphic(1621, 0, getPosition().copy().add(x, y));
						}
					}
				} else if (ticks == 2) {
					World.deregister(alien);
					stop();
				}
			}
		});
	}
	
	public static List<Item> getRewards(Player player) {
		int stardust = player.getInventory().getAmount(STARDUST);
		if (stardust <= 0)
			return null;
		
		if (stardust > STARDUST_LIMIT)
			stardust = STARDUST_LIMIT;
		
		final List<Item> rewards = new ArrayList<>();
		if (stardust >= 600 && MathUtil.random(40) == 0) {
			rewards.add(new Item(13661, 1));
			rewards.add(new Item(COSMIC_RUNES, 1820));
			rewards.add(new Item(ASTRAL_RUNES, 520));
			rewards.add(new Item(GOLD_ORES, 300));
			rewards.add(new Item(995, 300_000));
		} else if (stardust >= 550) {
			rewards.add(new Item(COSMIC_RUNES, 1520));
			rewards.add(new Item(ASTRAL_RUNES, 520));
			rewards.add(new Item(GOLD_ORES, 200));
			rewards.add(new Item(995, 500_000));
		} else if (stardust >= 500) {
			rewards.add(new Item(COSMIC_RUNES, 1380));
			rewards.add(new Item(ASTRAL_RUNES, 450));
			rewards.add(new Item(GOLD_ORES, 165));
			rewards.add(new Item(995, 584_200));
		} else if (stardust >= 450) {
			rewards.add(new Item(COSMIC_RUNES, 1180));
			rewards.add(new Item(ASTRAL_RUNES, 430));
			rewards.add(new Item(GOLD_ORES, 140));
			rewards.add(new Item(995, 435_000));
		} else if (stardust >= 400) {
			rewards.add(new Item(COSMIC_RUNES, 1020));
			rewards.add(new Item(ASTRAL_RUNES, 380));
			rewards.add(new Item(GOLD_ORES, 125));
			rewards.add(new Item(995, 400_000));
		} else if (stardust >= 300) {
			rewards.add(new Item(COSMIC_RUNES, 860));
			rewards.add(new Item(ASTRAL_RUNES, 340));
			rewards.add(new Item(GOLD_ORES, 112));
			rewards.add(new Item(995, 300_000));
		} else if (stardust >= 100) {
			rewards.add(new Item(COSMIC_RUNES, 800));
			rewards.add(new Item(ASTRAL_RUNES, 300));
			rewards.add(new Item(GOLD_ORES, 95));
			rewards.add(new Item(995, 200_000));
		} else if (stardust >= 50) {
			rewards.add(new Item(COSMIC_RUNES, 600));
			rewards.add(new Item(ASTRAL_RUNES, 150));
			rewards.add(new Item(GOLD_ORES, 50));
			rewards.add(new Item(995, 100_000));
		}
		
		//if (lamp != null)
			//rewards.add(lamp);

		if (MathUtil.random(3) == 0) {
			rewards.add(new Item(990, 2));
		} else if (MathUtil.random(3) == 0) {
			rewards.add(new Item(986, 2));
		} else if (MathUtil.random(3) == 0) {
			rewards.add(new Item(988, 2));
		}
		return rewards;
	}

	public static ShootingStar getInstance() {
		return INSTANCE;
	}
	
	private NPC edgeMeteora;
	
	private NPC varrockMeteora;
	
	private static final ShootingStar INSTANCE = new ShootingStar(38660, 0, 10, new Position(0, 0));
	
	public static final int STARDUST = 13727;
	
	public static final int STARDUST_LIMIT = 20000;
	
	private static final int SPAWN_TIMER = 2400;
	
	private static final int COSMIC_RUNES = 564;
	
	private static final int ASTRAL_RUNES = 9075;
	
	private static final int GOLD_ORES = 445;
	
	private enum Stage {	
		NINTH(38660, 10, 500 / 2, 10, 10),
		
		EIGTH(38661, 20, 400 / 2, 15, 9),
		
		SEVENTH(38662, 30, 300 / 2, 20, 8),
		
		SIXTH(38663, 40, 250 / 2, 30, 7),
		
		FIFTH(38664, 50, 200 / 2, 50, 6),
		
		FOURTH(38665, 60, 150 / 2, 70, 5),

		THIRD(38666, 70, 100 / 2, 110, 4),
		
		SECOND(38667, 80, 75 / 2, 150, 3),
		
		FIRST(38668, 90, 50 / 2, 250, 2);
		
		private Stage(int objectId, int levelRequirement, int minerals,
				int experience, int miningTimerModifier) {
			this.objectId = objectId;
			this.levelRequirement = levelRequirement;
			this.minerals = minerals;
			this.experience = experience;
			this.miningTimerModifier = miningTimerModifier;
			this.name = toString().toLowerCase();
		}
		
		private final int objectId;
		
		private final int levelRequirement;
		
		private final int minerals;
		
		private final int experience;
		
		private final int miningTimerModifier;
		
		private final String name;
		
		public int getObjectId() {
			return objectId;
		}
		
		public int getLevelRequirement(Player player) {
			return levelRequirement;
		}
		
		public int getMinerals() {
			return minerals;
		}
		
		public int getExperience() {
			return experience;
		}
		
		public int getMiningTimerModifier() {
			return miningTimerModifier;
		}
		
		public String getName() {
			return name;
		}
	}
	
	public enum Location {
		FARMING(new Position(3053, 3301), "south of the Falador Farming patches", "Farming"),
		EDGEVILLE(new Position(3094, 3484), "south of the Edgeville bank", "Edgeville"),
		GNOME_COURSE(new Position(2480, 3433), "at the Gnome Agility Course", "Gnome Course"),
		FLAX_FIELD(new Position(2745, 3445), "in the middle of the Flax field", "Flax Field"),
		DUEL_ARENA(new Position(3363, 3270), "in the Duel Arena", "Duel Arena"),
		PURO_PURO(new Position(2594, 4326), "in Puro Puro", "Puro Puro"),
		STRYKEWYRMS(new Position(2731, 5092), "in the Strykewyrm cavern", "Strykewyrms"),
		BOUNTY_HUNTER(new Position(3148, 3718), "in the bounty hunter crater", "Bounty Hunter"),
		TAVERLY_DUNG(new Position(2882, 9800), "in the Taverly dungeon", "Taverly Dung."),
		PEST_CONTROL(new Position(2666, 2648), "at the Void knight island", "Pest Control"),
		BARROWS(new Position(3566, 3297), "on the Barrows hills", "Barrows"),
		WEST_DRAGONS(new Position(2986, 3599), "in the Wilderness (near the western dragons)", "West Dragons"),
		RESOURCE_AREA(new Position(3202, 3944), "near the Resource Area", "Resource Area"),
		WILD_COURSE(new Position(2995, 3911), "outside the Wilderness Agility Course", "Wild. Course");
		
		private Location(Position position, String clue, String name) {
			this.position = position;
			this.name = name;
			this.clue = clue;
		}
		
		private Location(Position position, String clue) {
			this.position = position;
			this.name = NameUtils.capitalizeWords(toString().toLowerCase().replaceAll("_", " "));
			this.clue = clue;
		}
		
		private final Position position;
		
		private final String name;

		private String clue;
		
		public Position getPosition() {
			return position;
		}
		
		public String getName() {
			return name;
		}

		public String getClue() { return clue; }
	}
	
	private static final class Alien extends NPC {
		
		public Alien(int id, Position position, Direction direction,
				int respawnTimer) {
			super(id, position, direction);

		}

		@Override
		public Dialog getDialogue(Player player) {
			return new ShootingStarAlien(player);
		}
	}
}