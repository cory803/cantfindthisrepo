package com.chaos.world.content.skill.impl.fishing;

import com.chaos.engine.task.Task;
import com.chaos.engine.task.TaskManager;
import com.chaos.model.Animation;
import com.chaos.model.Locations;
import com.chaos.model.Skill;
import com.chaos.model.definitions.ItemDefinition;
import com.chaos.util.Misc;
import com.chaos.world.World;
import com.chaos.world.content.Achievements;
import com.chaos.world.content.Achievements.AchievementData;
import com.chaos.world.content.Emotes.Skillcape_Data;
import com.chaos.world.content.tasks.DailyTaskManager;
import com.chaos.world.entity.impl.player.Player;

public class Fishing {

	public static void rollPet(Player player) {
		int PET_ID = 7887;
		int PET_CHANCE = Misc.inclusiveRandom(1, 25_000);
		if (PET_CHANCE == 1 && player.getInventory().getFreeSlots() >= 1) {
			player.getInventory().add(PET_ID, 1);
			player.getPacketSender()
					.sendMessage("You find a weird looking item floating in the sea and quickly pick it up.");
			World.sendMessage("<icon=1><shad=FF8C38> " + player.getUsername() + " has just received "
					+ ItemDefinition.forId(PET_ID).getName() + " from the Fishing skill!");
		} else if (PET_CHANCE == 1 && player.getInventory().isFull()) {
			player.getBank(player.getCurrentBankTab()).add(PET_ID, 1);
			player.getPacketSender()
					.sendMessage("You find a weird looking item, if by magic it's been sent to your bank!");
			World.sendMessage("<icon=1><shad=FF8C38> " + player.getUsername() + " has just received "
					+ ItemDefinition.forId(PET_ID).getName() + " from the Fishing skill!");
		}
	}

	public enum Spot {

		LURE(318, new int[] { 335, 331 }, 309, 314, new int[] { 20, 30 }, true, new int[] { 55, 70 }, 623),

		CAGE(312, new int[] { 377 }, 301, -1, new int[] { 40 }, false, new int[] { 90 }, 619),

		BIGNET(313, new int[] { 353, 341, 363 }, 305, -1, new int[] { 16, 23, 46 }, false,
				new int[] { 40, 50, 100 }, 621),

		SMALLNET(316, new int[] { 317, 321 }, 303, -1, new int[] { 1, 15 }, false, new int[] { 20, 40}, 621),

		MONK_FISH(318, new int[] { 7944, 3142, 389 }, 305, -1, new int[] { 62, 65, 81 }, false,
				new int[] { 120, 105, 130}, 621),

		HARPOON(312, new int[] { 359, 371 }, 311, -1, new int[] { 35, 50 }, true, new int[] { 100, 100}, 618),

		HARPOON2(313, new int[] { 383 }, 311, -1, new int[] { 76 }, true, new int[] { 110 }, 618),

		BAIT(316, new int[] { 327, 345 }, 307, 313, new int[] { 5, 10 }, true, new int[] { 20, 30 }, 623),

		ROCKTAIL(309, new int[] { 15270 }, 309, 25, new int[] { 91 }, false, new int[] { 380 }, 623),

		KARAM(2859, new int[] { 3142 }, 305, -1, new int[] { 65 }, false, new int[] { 105 }, 621),

		WILDERNESS_ROCKTAIL(2724, new int[] { 15270 }, 309, 25, new int[] { 89 }, false, new int[] { 380 }, 623),

		WILDERNESS_MONKFISH(3019, new int[] { 7944, 3142, 389 }, 305, -1, new int[] { 60, 63, 79 }, false,
				new int[] { 120, 105, 130 }, 621),

		WILDERNESS_KARAM(2722, new int[] { 3142 }, 305, -1, new int[] { 65 }, false, new int[] { 105 }, 621);

		int npcId, equipment, bait, anim;
		int[] rawFish, fishingReqs, xp;
		boolean second;

		private Spot(int npcId, int[] rawFish, int equipment, int bait, int[] fishingReqs, boolean second, int[] xp,
				int anim) {
			this.npcId = npcId;
			this.rawFish = rawFish;
			this.equipment = equipment;
			this.bait = bait;
			this.fishingReqs = fishingReqs;
			this.second = second;
			this.xp = xp;
			this.anim = anim;
		}

		public int getNPCId() {
			return npcId;
		}

		public int[] getRawFish() {
			return rawFish;
		}

		public int getEquipment() {
			return equipment;
		}

		public int getBait() {
			return bait;
		}

		public int[] getLevelReq() {
			return fishingReqs;
		}

		public boolean getSecond() {
			return second;
		}

		public int[] getXp() {
			return xp;
		}

		public int getAnim() {
			return anim;
		}
	}

	public static Spot forSpot(int npcId, boolean secondClick) {
		for (Spot s : Spot.values()) {
			if (secondClick) {
				if (s.getSecond()) {
					if (s.getNPCId() == npcId) {
						if (s != null) {
							return s;
						}
					}
				}
			} else {
				if (s.getNPCId() == npcId && !s.getSecond()) {
					if (s != null) {
						return s;
					}
				}
			}
		}
		return null;
	}

	public static void setupFishing(Player p, Spot s) {
		if (s == null)
			return;
		if (p.getInventory().getFreeSlots() <= 0) {
			p.getPacketSender().sendMessage("You do not have any free inventory space.");
			p.getSkillManager().stopSkilling();
			return;
		}
		if (p.getSkillManager().getCurrentLevel(Skill.FISHING) >= s.getLevelReq()[0]) {
			if (p.getInventory().contains(s.getEquipment()) || Skillcape_Data.STRENGTH.isWearingCape(p)
					|| Skillcape_Data.MASTER_STRENGTH.isWearingCape(p)) {
				if (s.getBait() != -1) {
					if (p.getInventory().contains(s.getBait())) {
						startFishing(p, s);
					} else {
						String baitName = ItemDefinition.forId(s.getBait()).getName();
						if (baitName.contains("Feather") || baitName.contains("worm"))
							baitName += "s";
						p.getPacketSender().sendMessage("You need some " + baitName + " to fish here.");
						p.performAnimation(new Animation(65535));
					}
				} else {
					startFishing(p, s);
				}
			} else {
				// String def =
				// ItemDefinition.forId(s.getEquipment()).getName().toLowerCase();
				String def = ItemDefinition.forId(s.getEquipment()).getName().toLowerCase();
				p.getPacketSender().sendMessage("You need " + Misc.anOrA(def) + " " + def + " to fish here.");
			}
		} else {
			p.getPacketSender()
					.sendMessage("You need a fishing level of at least " + s.getLevelReq()[0] + " to fish here.");
		}
	}

	public static void startFishing(final Player p, final Spot s) {
		p.getSkillManager().stopSkilling();
		final int fishIndex = Misc.getRandom(100) >= 70 ? getMax(p, s.fishingReqs)
				: (getMax(p, s.fishingReqs) != 0 ? getMax(p, s.fishingReqs) - 1 : 0);
		if (Skillcape_Data.STRENGTH.isWearingCape(p))
			p.performAnimation(new Animation(9980));
		else
			p.performAnimation(new Animation(s.getAnim()));
		p.setCurrentTask(new Task(2, p, false) {
			int cycle = 0, reqCycle = Fishing.getDelay(s.getLevelReq()[fishIndex]);

			@Override
			public void execute() {
				if (p.getInventory().getFreeSlots() == 0) {
					p.getPacketSender().sendMessage("You have run out of inventory space.");
					stop();
					return;
				}
				if (!p.getInventory().contains(s.getBait())) {
					stop();
					return;
				}
				cycle++;
				if (Skillcape_Data.STRENGTH.isWearingCape(p))
					p.performAnimation(new Animation(9980));
				else
					p.performAnimation(new Animation(s.getAnim()));
				if (cycle >= Misc.getRandom(1) + reqCycle) {
					String def = ItemDefinition.forId(s.getRawFish()[fishIndex]).getName();
					if (def.endsWith("s"))
						def = def.substring(0, def.length() - 1);
					p.getPacketSender().sendMessage(
							"You catch " + Misc.anOrA(def) + " " + def.toLowerCase().replace("_", " ") + ".");
					rollPet(p);
					if (s.getBait() != -1)
						p.getInventory().delete(s.getBait(), 1);
					if (p.getLocation() == Locations.Location.WILDERNESS) {
						p.getInventory().add(s.getRawFish()[fishIndex] + 1, 1);
					}
					p.getInventory().add(s.getRawFish()[fishIndex], 1);
					if (s.getRawFish()[fishIndex] == 331) {
						Achievements.finishAchievement(p, AchievementData.FISH_A_SALMON);
					} else if (s.getRawFish()[fishIndex] == 15270) {
						Achievements.doProgress(p, AchievementData.FISH_25_ROCKTAILS);
						Achievements.doProgress(p, AchievementData.FISH_2000_ROCKTAILS);
					} else if(s.getRawFish()[fishIndex] == 15271) {
						Achievements.doProgress(p, AchievementData.FISH_25_ROCKTAILS);
						Achievements.doProgress(p, AchievementData.FISH_2000_ROCKTAILS);
					}
					if (p.dailyTask == 0 && !p.completedDailyTask) {
						DailyTaskManager.doTaskProgress(p);
					}
					p.getSkillManager().addSkillExperience(Skill.FISHING, s.getXp()[fishIndex]);
					setupFishing(p, s);
					setEventRunning(false);
				}
			}

			@Override
			public void stop() {
				setEventRunning(false);
				p.performAnimation(new Animation(s.getAnim()));
			}
		});

		TaskManager.submit(p.getCurrentTask());
	}

	public static int getMax(Player p, int[] reqs) {
		int tempInt = -1;
		for (int i : reqs) {
			if (p.getSkillManager().getCurrentLevel(Skill.FISHING) >= i) {
				tempInt++;
			}
		}
		return tempInt;
	}

	private static int getDelay(int req) {
		int timer = 1;
		timer += req * 0.08;
		return timer;
	}

}