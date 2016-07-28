package com.runelive.world.content.skill.impl.mining;

import com.runelive.engine.task.Task;
import com.runelive.engine.task.TaskManager;
import com.runelive.model.Animation;
import com.runelive.model.GameObject;
import com.runelive.model.Locations;
import com.runelive.model.Skill;
import com.runelive.model.container.impl.Equipment;
import com.runelive.model.definitions.ItemDefinition;
import com.runelive.util.Misc;
import com.runelive.world.World;
import com.runelive.world.content.Achievements;
import com.runelive.world.content.Achievements.AchievementData;
import com.runelive.world.content.CustomObjects;
import com.runelive.world.content.Emotes.Skillcape_Data;
import com.runelive.world.content.ShootingStar;
import com.runelive.world.content.Sounds;
import com.runelive.world.content.Sounds.Sound;
import com.runelive.world.content.skill.impl.mining.MiningData.Ores;
import com.runelive.world.content.tasks.DailyTaskManager;
import com.runelive.world.entity.impl.player.Player;

public class Mining {

	public static void rollPet(Player player) {
		int PET_ID = 21250;
		int PET_CHANCE = Misc.inclusiveRandom(1, 25_000);
		if (PET_CHANCE == 1 && player.getInventory().getFreeSlots() >= 1) {
			player.getInventory().add(PET_ID, 1);
			player.getPacketSender()
					.sendMessage("You hit the rock with such a force, that a small chunk starts to move!");
			World.sendMessage("<icon=1><shad=FF8C38> " + player.getUsername() + " has just received "
					+ ItemDefinition.forId(PET_ID).getName() + " from the Mining skill!");
		} else if (PET_CHANCE == 1 && player.getInventory().isFull()) {
			player.getBank(player.getCurrentBankTab()).add(PET_ID, 1);
			player.getPacketSender()
					.sendMessage("You hit the rock with such a force, that a small chunk, it's moved to your bank!");
			World.sendMessage("<icon=1><shad=FF8C38> " + player.getUsername() + " has just received "
					+ ItemDefinition.forId(PET_ID).getName() + " from the Mining skill!");
		}
	}

	public static void startMining(final Player player, final GameObject oreObject) {
		player.getSkillManager().stopSkilling();
		player.getPacketSender().sendInterfaceRemoval();
		if (!Locations.goodDistance(player.getPosition().copy(), oreObject.getPosition(), 1)
				&& oreObject.getId() != 24444 && oreObject.getId() != 24445 && oreObject.getId() != 38660)
			return;
		if (player.busy() || player.getCombatBuilder().isBeingAttacked() || player.getCombatBuilder().isAttacking()) {
			player.getPacketSender().sendMessage("You cannot do that right now.");
			return;
		}
		if (player.getInventory().getFreeSlots() == 0) {
			player.getPacketSender().sendMessage("You do not have any free inventory space left.");
			return;
		}
		player.setInteractingObject(oreObject);
		player.setPositionToFace(oreObject.getPosition());
		final Ores o = MiningData.forRock(oreObject.getId());
		final boolean giveGem = o != Ores.Rune_essence && o != Ores.Pure_essence;
		final int reqCycle = o == Ores.Runite ? 6 + Misc.getRandom(2) : Misc.getRandom(o.getTicks() - 1);
		if (o != null) {
			final int pickaxe = MiningData.getPickaxe(player);
			final int miningLevel = player.getSkillManager().getCurrentLevel(Skill.MINING);
			if (pickaxe > 0) {
				if (miningLevel >= o.getLevelReq()) {
					final MiningData.Pickaxe p = MiningData.forPick(pickaxe);
					if (miningLevel >= p.getReq()) {
						player.performAnimation(new Animation(p.getAnim()));
						final int delay = o.getTicks() - MiningData.getReducedTimer(player, p) + 1;
						player.setCurrentTask(new Task(delay >= 2 ? delay : 1, player, false) {
							int cycle = 0;

							@Override
							public void execute() {
								if (player.getInteractingObject() == null
										|| player.getInteractingObject().getId() != oreObject.getId()) {
									player.getSkillManager().stopSkilling();
									player.performAnimation(new Animation(65535));
									stop();
									return;
								}
								if (player.getInventory().getFreeSlots() == 0) {
									player.performAnimation(new Animation(65535));
									stop();
									player.getPacketSender()
											.sendMessage("You do not have any free inventory space left.");
									return;
								}
								if (cycle != reqCycle) {
									cycle++;
									player.performAnimation(new Animation(p.getAnim()));
								}
								if (giveGem) {
									boolean onyx = (o == Ores.Runite || o == Ores.CRASHED_STAR)
											&& Misc.getRandom(o == Ores.CRASHED_STAR ? 20000 : 5000) == 1;
									if (onyx || Misc.getRandom(o == Ores.CRASHED_STAR ? 35 : 50) == 15) {
										int gemId = onyx ? 6571
												: MiningData.RANDOM_GEMS[(int) (MiningData.RANDOM_GEMS.length
												* Math.random())];
										player.getInventory().add(gemId, 1);
										player.getPacketSender().sendMessage("You've found a gem!");
										if (gemId == 6571) {
											String s = o == Ores.Runite ? "Runite ore" : "Crashed star";
											World.sendMessage("<img=4><col=009966> " + player.getUsername()
													+ " has just received an Uncut Onyx from mining a " + s + "!");
										}
									}
								}
								if (cycle == reqCycle) {
									if (player.dailyTask == 9 && !player.completedDailyTask) {
										DailyTaskManager.doTaskProgress(player);
									}
									if (o == Ores.Iron) {
										Achievements.finishAchievement(player, AchievementData.MINE_SOME_IRON);
									} else if (o == Ores.Runite) {
										Achievements.doProgress(player, AchievementData.MINE_25_RUNITE_ORES);
										Achievements.doProgress(player, AchievementData.MINE_2000_RUNITE_ORES);
									}
									int multiplier = (Skillcape_Data.MINING.isWearingCape(player)
											&& Misc.inclusiveRandom(0, 9) == 0) ? 2 : 1;
									if (o.getItemId() != -1) {
										player.getInventory().add(o.getItemId(), 1 * multiplier);
									}
									player.getSkillManager().addSkillExperience(Skill.MINING, (int) (o.getXpAmount() * 1.4));
									if (o == Ores.CRASHED_STAR) {
										if (goldenMiningArmour(player)) {
											int bonus = Misc.inclusiveRandom(1, 2);
											if (bonus == 1) {
												player.forceChat("*CLINK* What was that?");
												player.getPacketSender().sendMessage(
														"A big chunk of the star lands on your golden armour.");
												player.getInventory().add(13727, 1);
											}
										}
										player.getPacketSender().sendMessage("You mine the crashed star..");
									} else {
										player.getPacketSender().sendMessage("You mine some ore.");
									}
									rollPet(player);
									Sounds.sendSound(player, Sound.MINE_ITEM);
									cycle = 0;
									this.stop();
									if (o.getRespawn() > 0) {
										player.performAnimation(new Animation(65535));
										oreRespawn(player, oreObject, o);
									} else {
										if (oreObject.getId() == 38660) {
											if (ShootingStar.CRASHED_STAR == null
													|| ShootingStar.CRASHED_STAR.getStarObject()
													.getPickAmount() >= ShootingStar.MAXIMUM_MINING_AMOUNT) {
												player.getPacketSender().sendClientRightClickRemoval();
												player.getSkillManager().stopSkilling();
												return;
											} else {
												ShootingStar.CRASHED_STAR.getStarObject().incrementPickAmount();
											}
										} else {
											player.performAnimation(new Animation(65535));
										}
										startMining(player, oreObject);
									}
								}
							}
						});
						TaskManager.submit(player.getCurrentTask());
					} else {
						player.getPacketSender().sendMessage(
								"You need a Mining level of at least " + p.getReq() + " to use this pickaxe.");
					}
				} else {
					player.getPacketSender().sendMessage(
							"You need a Mining level of at least " + o.getLevelReq() + " to mine this rock.");
				}
			} else {
				player.getPacketSender().sendMessage("You don't have a pickaxe to mine this rock with.");
			}
		}
	}

	public static boolean goldenMiningArmour(Player player) {
		return player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 20789
				&& player.getEquipment().get(Equipment.BODY_SLOT).getId() == 20791
				&& player.getEquipment().get(Equipment.LEG_SLOT).getId() == 20790
				&& player.getEquipment().get(Equipment.FEET_SLOT).getId() == 20788
				&& player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 20787;
	}

	public static void oreRespawn(final Player player, final GameObject oldOre, Ores o) {
		if (oldOre == null || oldOre.getPickAmount() >= 1)
			return;
		oldOre.setPickAmount(1);
		for (Player players : player.getLocalPlayers()) {
			if (players == null)
				continue;
			if (players.getInteractingObject() != null && players.getInteractingObject().getPosition()
					.equals(player.getInteractingObject().getPosition().copy())) {
				players.getPacketSender().sendClientRightClickRemoval();
				players.getSkillManager().stopSkilling();
			}
		}
		player.getPacketSender().sendClientRightClickRemoval();
		player.getSkillManager().stopSkilling();
		CustomObjects.globalObjectRespawnTask(new GameObject(452, oldOre.getPosition().copy(), 10, 0), oldOre,
				o.getRespawn());
	}
}
