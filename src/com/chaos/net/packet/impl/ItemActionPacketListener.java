package com.chaos.net.packet.impl;

import com.chaos.GameSettings;
import com.chaos.engine.task.Task;
import com.chaos.engine.task.TaskManager;
import com.chaos.model.*;
import com.chaos.model.input.impl.EnterAmountToDice;
import com.chaos.model.input.impl.EnterForumAccountTokens;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.net.packet.Packet;
import com.chaos.net.packet.PacketListener;
import com.chaos.util.Misc;
import com.chaos.world.content.*;
import com.chaos.world.content.combat.range.DwarfMultiCannon;
import com.chaos.world.content.skill.impl.herblore.Herblore;
import com.chaos.world.content.skill.impl.herblore.IngridientsBook;
import com.chaos.world.content.skill.impl.hunter.BoxTrap;
import com.chaos.world.content.skill.impl.hunter.Hunter;
import com.chaos.world.content.skill.impl.hunter.JarData;
import com.chaos.world.content.skill.impl.hunter.PuroPuro;
import com.chaos.world.content.skill.impl.hunter.SnareTrap;
import com.chaos.world.content.skill.impl.hunter.Trap.TrapState;
import com.chaos.world.content.skill.impl.prayer.Prayer;
import com.chaos.world.content.skill.impl.runecrafting.Runecrafting;
import com.chaos.world.content.skill.impl.runecrafting.RunecraftingPouches;
import com.chaos.world.content.skill.impl.runecrafting.RunecraftingPouches.RunecraftingPouch;
import com.chaos.world.content.skill.impl.slayer.SlayerDialog;
import com.chaos.world.content.skill.impl.slayer.SlayerTeleports;
import com.chaos.world.content.skill.impl.summoning.SummoningData;
import com.chaos.world.content.skill.impl.woodcutting.BirdNests;
import com.chaos.world.content.transportation.TeleportHandler;
import com.chaos.world.content.transportation.TeleportType;
import com.chaos.world.content.transportation.jewelry.CombatTeleporting;
import com.chaos.world.content.transportation.jewelry.GloryTeleporting;
import com.chaos.world.content.transportation.jewelry.SkillsTeleporting;
import com.chaos.world.entity.impl.player.Player;
import org.scripts.kotlin.content.dialog.KnightLamp;

public class ItemActionPacketListener implements PacketListener {

	public static void handleHasta(Player player) {
		if (player.getInventory().contains(11716) && player.getMoneyInPouch() >= 150_000_000) {
			player.setMoneyInPouch(player.getMoneyInPouch() - 150_000_000);
			player.getPacketSender().sendMessage("150,000,000 Coins have been removed from your Money Pouch");
			player.getInventory().delete(11716, 1);
			player.getInventory().add(21120, 1);
		} else {
			player.getPacketSender().sendMessage("You either do not have 150M in your pouch or a Zamorakian Spear.");
		}
	}

	private static void firstAction(final Player player, Packet packet) {
		int interfaceId = packet.readUnsignedShort();
		int slot = packet.readShort();
		int itemId = packet.readShort();
		if (GameSettings.DEBUG_MODE) {
			// PlayerLogs.log(player, "" + player.getUsername() + " in
			// ItemActionPacketListener: " + itemId + "");
		}
		if (slot < 0 || slot > player.getInventory().capacity())
			return;
		if (player.getInventory().getItems()[slot].getId() != itemId) {
			return;
		}
		player.setInteractingItem(player.getInventory().getItems()[slot]);
		if (Prayer.isBone(itemId)) {
			Prayer.buryBone(player, itemId);
			return;
		}
		if (!player.getDragonSpear().elapsed(3000)) {
			player.getPacketSender().sendMessage("You can't do that, you're stunned!");
			return;
		}
		if (Consumables.isFood(player, itemId, slot))
			return;
		if (Consumables.isPotion(itemId)) {
			Consumables.handlePotion(player, itemId, slot);
			return;
		}
		if (BirdNests.isNest(itemId)) {
			BirdNests.searchNest(player, itemId);
			return;
		}
		if (Herblore.cleanHerb(player, itemId))
			return;
		if (Effigies.isEffigy(itemId)) {
			Effigies.handleEffigy(player, itemId);
			return;
		}
		if (ExperienceLamps.handleLamp(player, itemId)) {
			return;
		}
		switch (itemId) {
			//slayer
			case 4155:
				if(player.getSlayer().getSlayerTask() != null) {
					player.setNpcClickId(player.getSlayer().getSlayerMaster().getNpcId());
					player.getDialog().sendDialog(new SlayerDialog(player, 5));
					return;
				} else {
					player.getPacketSender().sendInterfaceRemoval();
					player.getPacketSender().sendMessage("Your Enchanted gem will only work if you have a Slayer task.");
				}
				break;
			case 10586:
				player.getDialog().sendDialog(new KnightLamp(player));
				break;
			case 21776:
				if (player.getInventory().contains(21776) && player.getInventory().getAmount(21776) >= 100) {
					if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) >= 80) {
						player.getInventory().delete(21776, 100);
						player.getInventory().add(21775, 1);
						player.getSkillManager().addExactExperience(Skill.CRAFTING, 1000);
						return;
					} else {
						player.getPacketSender().sendMessage("You need a Crafting level of 80 to make an Orb of Armadyl");
						return;
					}
				}
				player.getPacketSender().sendMessage("You do not have 100 Shards of Armadyl");
				break;
			case 8013:
				if (player.isJailed()) {
					player.getPacketSender().sendMessage("You can't teleport out of jail.");
					return;
				}
				if (!player.getClickDelay().elapsed(4500) || player.getWalkingQueue().isLockMovement())
					return;
				if (!TeleportHandler.checkReqs(player, new Position(3087, 3502, 0), false)) {
					return;
				}
				player.getInventory().delete(itemId, 1);
				if(player.getTabTimer().elapsed(1500)) {
					Sounds.sendSound(player, Sounds.Sound.TELEPORT);
					player.currentDialog = null;
					player.setTeleporting(true).getWalkingQueue().setLockMovement(true).clear();
					TeleportHandler.cancelCurrentActions(player);
					TaskManager.submit(new Task(1, player, true) {
						int tick = 0;
						@Override
						public void execute() {
							if (tick == 0) {
								player.performAnimation(new Animation(9597));
							} else if (tick == 4) {
								player.moveTo(new Position(3087, 3502, 0));
								player.performAnimation(new Animation(9598));
								player.performGraphic(new Graphic(678));
								player.getWalkingQueue().setLockMovement(false).clear();
								stop();
							}
							tick++;
						}

						@Override
						public void stop() {
							setEventRunning(false);
							player.setTeleporting(false);
							player.getClickDelay().reset(0);
						}
					});
				}
				player.getTabTimer().reset();
				break;
		case 12437:
			if (player.getSummoning().getFamiliar() != null) {
				if (player.getSummoning().getFamiliar().getSummonNpc().getId() == 6869) {
					if (!player.getSummoningTimer().elapsed(15000)) {
						player.getPacketSender()
								.sendMessage("You must wait 15 seconds in order to use the magic focus scroll effect.");
						return;
					}
					player.getSummoningTimer().reset();
					player.performAnimation(new Animation(7660));
					player.performGraphic(new Graphic(1303));
					player.getSkillManager().setCurrentLevel(Skill.forId(6),
							player.getSkillManager().getCurrentLevel(Skill.forId(6))
									+ Consumables.getBoostedStat(player, 500, false, false),
							true);
					player.getPacketSender().sendMessage("Your wolpertinger has boosted your magic to "
							+ player.getSkillManager().getCurrentLevel(Skill.forId(6)) + ".");

					player.getInventory().delete(12437, 1);
				} else {
					player.getPacketSender()
							.sendMessage("You must have a wolpertinger summoned in order to use this scroll.");
				}
			} else {
				player.getPacketSender()
						.sendMessage("You must have a wolpertinger summoned in order to use this scroll.");
			}
			break;
		case 10943:
			player.currentScroll = 10943;
			player.setInputHandling(new EnterForumAccountTokens());
			player.getPacketSender().sendEnterInputPrompt("Enter a forum account to add 10 tokens to:");
			break;
		case 10934:
			player.currentScroll = 10934;
			player.setInputHandling(new EnterForumAccountTokens());
			player.getPacketSender().sendEnterInputPrompt("Enter a forum account to add 25 tokens to:");
			break;
		case 10935:
			player.currentScroll = 10935;
			player.setInputHandling(new EnterForumAccountTokens());
			player.getPacketSender().sendEnterInputPrompt("Enter a forum account to add 50 tokens to:");
			break;
		case 7629:
			player.currentScroll = 7629;
			player.setInputHandling(new EnterForumAccountTokens());
			player.getPacketSender().sendEnterInputPrompt("Enter a forum account to add 125 tokens to:");
			break;
		case 739:
			if (player.getInventory().contains(739)) {
				Consumables.handlePotion(player, 739, slot);
			}
			break;
		case 6040:
			Position location = new Position(2036, 4535, 0);
			TeleportHandler.teleportPlayer(player, location, TeleportType.PURO_PURO);
			break;
		case 11211:
			if(!player.isSpecialPlayer()) {
				return;
			}
			player.setInputHandling(new EnterAmountToDice(1, 1));
			player.getPacketSender().sendEnterAmountPrompt("What would you like to roll?");
			break;
		case 4142:
			if(!player.isSpecialPlayer()) {
				return;
			}
			if (!player.boost_stats) {
				player.getPacketSender().sendMessage("<col=ff0000><shad=0>You have boosted to 1,000 defence.");
				player.getSkillManager().setCurrentLevel(Skill.DEFENCE, 1000, true);
				player.boost_stats = true;
			} else {
				player.getPacketSender().sendMessage("<col=ff0000><shad=0>You have restored your defence.");
				player.boost_stats = false;
				player.getSkillManager().setCurrentLevel(Skill.DEFENCE, 99, true);
			}
			break;
			case 2677:
			player.getPacketSender().sendInterface(17537);
			break;
		case 10944:
			if (player.getInventory().isFull()) {
				player.getPacketSender().sendMessage("You need to have atleast 1 free inventory space.");
				return;
			}
			VoteTokens.open_token(player, 10944);
			break;
		case 13663:
			if (player.getInterfaceId() > 0) {
				player.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
				return;
			}
			player.setUsableObject(new Object[2]).setUsableObject(0, "reset");
			player.getPacketSender().sendString(38006, "Choose stat to reset!")
					.sendMessage("@red@Please select a skill you wish to reset and then click on the 'Confim' button.")
					.sendString(38090, "Which skill would you like to reset?");
			player.getPacketSender().sendInterface(38000);
			break;
		case 19670:
			if (player.busy()) {
				player.getPacketSender().sendMessage("You can not do this right now.");
				return;
			}
			break;
		case 7956:
			player.getInventory().delete(7956, 1);
			int[] rewards = { 200, 202, 204, 206, 208, 210, 212, 214, 216, 218, 220, 2486, 3052, 1624, 1622, 1620, 1618,
					1632, 1516, 1514, 454, 448, 450, 452, 378, 372, 7945, 384, 390, 15271, 533, 535, 537, 18831, 556,
					558, 555, 554, 557, 559, 564, 562, 566, 9075, 563, 561, 560, 565, 888, 890, 892, 11212, 9142, 9143,
					9144, 9341, 9244, 866, 867, 868, 2, 10589, 10564, 6809, 4131, 15126, 4153, 1704, 1149 };
			int[] rewardsAmount = { 50, 50, 50, 30, 20, 30, 30, 30, 30, 20, 10, 5, 4, 70, 40, 25, 10, 10, 100, 50, 100,
					80, 25, 25, 250, 200, 125, 50, 30, 25, 50, 20, 20, 5, 500, 500, 500, 500, 500, 500, 500, 500, 200,
					200, 200, 200, 200, 200, 1000, 750, 200, 100, 1200, 1200, 120, 50, 20, 1000, 500, 100, 100, 1, 1, 1,
					1, 1, 1, 1, 1 };
			int rewardPos = Misc.getRandom(rewards.length - 1);
			player.getInventory().add(rewards[rewardPos],
					(int) ((rewardsAmount[rewardPos] * 0.5) + (Misc.getRandom(rewardsAmount[rewardPos]))));
			break;
		case 15387:
			player.getInventory().delete(15387, 1);
			rewards = new int[] { 1377, 1149, 7158, 3000, 219, 5016, 6293, 6889, 2205, 3051, 269, 329, 3779, 6371, 2442,
					347, 247 };
			player.getInventory().add(rewards[Misc.getRandom(rewards.length - 1)], 1);
			break;
		case 407:
			player.getInventory().delete(407, 1);
			if (Misc.getRandom(3) < 3) {
				player.getInventory().add(409, 1);
			} else if (Misc.getRandom(4) < 4) {
				player.getInventory().add(411, 1);
			} else
				player.getInventory().add(413, 1);
			break;
		case 15084:
			if (player.getClanChatName() == null) {
				player.getPacketSender().sendMessage("You need to be in a clanchat channel to roll a dice.");
				return;
			}
			Gambling.rollDice(player);
			break;
		case 299:
			Gambling.plantSeed(player);
			break;
		case 15104:
			player.getPacketSender().sendMessage("Combine this with the three other missing parts...");
			return;
		case 15103:
			player.getPacketSender().sendMessage("Combine this with the three other missing parts...");
			return;
		case 15105:
			player.getPacketSender().sendMessage("Combine this with the three other missing parts...");
			return;
		case 15106:
			player.getPacketSender().sendMessage("Combine this with the three other missing parts...");
			return;
		case 11858:
		case 11860:
		case 11862:
		case 11848:
		case 11856:
		case 11850:
		case 11854:
		case 11852:
		case 11846:
			if (!player.getClickDelay().elapsed(2000) || !player.getInventory().contains(itemId))
				return;
			if (player.busy()) {
				player.getPacketSender().sendMessage("You cannot open this right now.");
				return;
			}

			int[] items = itemId == 11858 ? new int[] { 10350, 10348, 10346, 10352 }
					: itemId == 11860 ? new int[] { 10334, 10330, 10332, 10336 }
							: itemId == 11862 ? new int[] { 10342, 10338, 10340, 10344 }
									: itemId == 11848 ? new int[] { 4716, 4720, 4722, 4718 }
											: itemId == 11856 ? new int[] { 4753, 4757, 4759, 4755 }
													: itemId == 11850 ? new int[] { 4724, 4728, 4730, 4726 }
															: itemId == 11854 ? new int[] { 4745, 4749, 4751, 4747 }
																	: itemId == 11852
																			? new int[] { 4732, 4734, 4736, 4738 }
																			: itemId == 11846
																					? new int[] { 4708, 4712, 4714,
																							4710 }
																					: new int[] { itemId };

			if (player.getInventory().getFreeSlots() < items.length) {
				player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
				return;
			}
			player.getInventory().delete(itemId, 1);
			for (int i : items) {
				player.getInventory().add(i, 1);
			}
			player.getPacketSender().sendMessage("You open the set and find items inside.");
			player.getClickDelay().reset();
			break;
		case 952:
			Digging.dig(player);
			break;
		case 10006:
			// Hunter.getInstance().laySnare(client);
			Hunter.layTrap(player, new SnareTrap(new GameObject(19175, new Position(player.getPosition().getX(),
					player.getPosition().getY(), player.getPosition().getZ())), TrapState.SET, 200, player));
			break;
		case 10008:
			Hunter.layTrap(player, new BoxTrap(new GameObject(19187, new Position(player.getPosition().getX(),
					player.getPosition().getY(), player.getPosition().getZ())), TrapState.SET, 200, player));
			break;
		case 5509:
		case 5510:
		case 5512:
			RunecraftingPouches.fill(player, RunecraftingPouch.forId(itemId));
			break;
		case 292:
			IngridientsBook.readBook(player, 0, false);
			break;
		case 21117:
			int AchievementRewards[][] = {
					{ 15501, 15272, 2503, 10499, 6326, 861, 1163, 1201, 6111, 544, 542, 5574, 5575, 5576, 1215, 3105,
							13734, 7400, 2572, 11118 }, // Common, 0
					{ 15501, 11133, 15126, 10828, 3751, 3753, 10589, 10564, 6809, 4587, 1249, 3204, 1305, 1377, 1434,
							6528, 7158, 4153, 6, 8, 10, 12, 4675, 6914, 6889 }, // Uncommon,
																				// 1
					{ 6739, 15259, 15332, 2579, 6920, 6922, 13879, 13883, 15241, 15243 } // Rare,
																							// 2
			};
			double achievementGen = Math.random();
			/**
			 * Chances 50% chance of Common Items - cheap gear, high-end
			 * consumables 40% chance of Uncommon Items - various high-end
			 * coin-bought gear 10% chance of Rare Items - Highest-end
			 * coin-bought gear, some voting-point/pk-point equipment
			 */
			int rewardChance = achievementGen >= 0.5 ? 0 : achievementGen >= 0.20 ? 1 : 2;
			rewardPos = Misc.getRandom(AchievementRewards[rewardChance].length - 1);
			player.getInventory().delete(21117, 1);
			player.getInventory().add(AchievementRewards[rewardChance][rewardPos], 1).refreshItems();
			break;
		case 6199:
			int rewards2[][] = {
					{ 592, 592, 592, 1079, 1093, 1127, 1201, 10828, 1163, 1201, 4131, 4587, 5698, 1305, 2491, 2503,
							2497, 861, 4675, 3849, 6524, 13734, 11118, 1712, 2550, 1079, 1093, 1127, 1201, 10828, 1163,
							1201, 4131, 4587, 5698, 1305, 2491, 2503, 2497, 861, 4675, 3849, 6524, 13734, 11118, 1712,
							2550 }, // Common, 0
					{ 592, 6889, 8849, 6570, 6570, 6570, 6570, 6570, 6570, 4151, 15486, 11235, 11732, 10548, 10551,
							10547, 10549, 2581, 2577, 6585, 15126, 592, 6889, 8849, 6570, 6570, 6570, 6570, 6570, 6570,
							4151, 15486, 11235, 11732, 10548, 10551, 10547, 10549, 2581, 2577, 6585, 15126, 11846,
							11848, 11850, 11852, 11854, 11856 }, // Uncommon, 1
					{ 592, 592, 592, 6, 8, 10, 12, 6570, 6570, 6570, 6570, 6570, 6570, 6739, 6731, 6733, 6735, 6737,
							6922, 6916, 6918, 6920, 19111, 7968 } // Rare, 2
			};
			double numGen = Math.random();
			/**
			 * Chances 50% chance of Common Items - cheap gear, high-end
			 * consumables 40% chance of Uncommon Items - various high-end
			 * coin-bought gear 10% chance of Rare Items - Highest-end
			 * coin-bought gear, some voting-point/pk-point equipment
			 */
			int rewardGrade = numGen >= 0.5 ? 0 : numGen >= 0.20 ? 1 : 2;
			rewardPos = Misc.getRandom(rewards2[rewardGrade].length - 1);
			player.getInventory().delete(6199, 1);
			player.getInventory().add(rewards2[rewardGrade][rewardPos], 1).refreshItems();
			break;
		/*
		 * case 15501: int superiorRewards[][] = { {11133, 15126, 10828, 3751,
		 * 3753, 10589, 10564, 6809, 4587, 1249, 3204, 1305, 1377, 1434, 6528,
		 * 7158, 4153, 6, 8, 10, 12, 4675, 6914, 6889}, // Uncommon, 0 {6739,
		 * 15259, 15332, 2579, 6920, 6922, 15241, 11882, 11884, 11906, 20084},
		 * // Rare, 1 {6570, 15018, 15019, 15020, 15220, 11730, 18349, 18353,
		 * 13896, 18357, 13899, 10551, 4151, 2577,}, // Epic, 2 {11235, 17273,
		 * 14484, 11696, 11698, 11700, 13262, 15486, 19336, 19337, 19338, 19339,
		 * 19340, 14009, 14010, 14008, 14011, 14012, 14013, 14014, 14015, 14016}
		 * // Legendary, // 3 }; double superiorNumGen = Math.random(); int
		 * superiorRewardGrade = superiorNumGen >= 0.46 ? 0 : superiorNumGen >=
		 * 0.16 ? 1 : superiorNumGen >= 0.05 ? 2 : 3; int superiorRewardPos =
		 * Misc.getRandom(superiorRewards[superiorRewardGrade].length - 1);
		 * player.getInventory().delete(15501, 1);
		 * player.getInventory().add(superiorRewards[superiorRewardGrade][
		 * superiorRewardPos], 1) .refreshItems(); break;
		 */
		case 15501:
			int superiorRewards[][] = {
					{ 11133, 15126, 10828, 3751, 3753, 10589, 10564, 6809, 4587, 1249, 3204, 1305, 1377, 1434, 6528,
							7158, 4153, 6, 8, 10, 12, 4675, 6914, 6889 }, // Uncommon,
																			// 0
					{ 6739, 15259, 15332, 2579, 6920, 6922, 15241, 11882, 11884, 20084 }, // Rare,
																									// 1
					{ 6570, 15018, 15019, 15020, 15220, 11730, 18349, 18353, 13896, 18357, 13899, 10551, 4151, 2577, }, // Epic,
																														// 2
					{ 11235, 17273, 14484, 11696, 11698, 11700, 12954, 20072, 15486, 19336, 19337, 19338, 19339, 19340} // Legendary,
																						// 3
			};
			double superiorNumGen = Math.random();
			int superiorRewardGrade = superiorNumGen >= 0.46 ? 0
					: superiorNumGen >= 0.16 ? 1 : superiorNumGen >= 0.05 ? 2 : 3;
			int superiorRewardPos = Misc.getRandom(superiorRewards[superiorRewardGrade].length - 1);
			player.getInventory().delete(15501, 1);
			player.getInventory().add(superiorRewards[superiorRewardGrade][superiorRewardPos], 1).refreshItems();
			break;
		case 11882:
			player.getInventory().delete(11882, 1);
			player.getInventory().add(2595, 1).refreshItems();
			player.getInventory().add(2591, 1).refreshItems();
			player.getInventory().add(3473, 1).refreshItems();
			player.getInventory().add(2597, 1).refreshItems();
			break;
		case 11884:
			player.getInventory().delete(11884, 1);
			player.getInventory().add(2595, 1).refreshItems();
			player.getInventory().add(2591, 1).refreshItems();
			player.getInventory().add(2593, 1).refreshItems();
			player.getInventory().add(2597, 1).refreshItems();
			break;
		//Wizard set
//		case 11906:
//			player.getInventory().delete(11906, 1);
//			player.getInventory().add(7394, 1).refreshItems();
//			player.getInventory().add(7390, 1).refreshItems();
//			player.getInventory().add(7386, 1).refreshItems();
//			break;
		case 15262:
			if (!player.getClickDelay().elapsed(1000))
				return;
			if (player.getInventory().getAmount(18016) >= Integer.MAX_VALUE - 10000) {
				player.getPacketSender().sendMessage("You have too many spirit shards in your inventory!");
				return;
			}
			player.getInventory().delete(15262, 1);
			player.getInventory().add(18016, 10000).refreshItems();
			player.getClickDelay().reset();
			break;
		case 6:
			DwarfMultiCannon.setupCannon(player);
			break;
		}
	}

	public static void secondAction(Player player, Packet packet) {
		int interfaceId = packet.readLEShortA();
		int slot = packet.readLEShort();
		int itemId = packet.readShortA();
		if (slot < 0 || slot > player.getInventory().capacity())
			return;
		if (player.getInventory().getItems()[slot].getId() != itemId)
			return;
		if (SummoningData.isPouch(player, itemId, 2))
			return;
		if(player.getDegrading().isCheckCharges(player, itemId, slot)) {
			return;
		}
		switch (itemId) {
		case 14022:
		case 21085:
		case 21086:
		case 21087:
		case 21093:
		case 21094:
		case 21095:
		case 21096:
		case 21097:
		case 21098:
		case 21099:
			CompletionistCapes.openInterface(player, itemId);
			break;
		case 6500:
			player.getPacketSender().sendMessage("You're already gaining EXP and picking up charms! don't be greedy");
			break;
		case 1712:
		case 1710:
		case 1708:
		case 1706:
			GloryTeleporting.rub(player, itemId);
			break;

			case 11105:
			case 11107:
			case 11109:
			case 11111:
				SkillsTeleporting.rub(player, itemId);
			break;
			case 11113:
				player.getPacketSender().sendMessage("Your necklace has run out of charges.");
				break;

			case 11118:
			case 11120:
			case 11122:
			case 11124:
				CombatTeleporting.rub(player, itemId);
				break;
		case 1704:
			player.getPacketSender().sendMessage("Your amulet has run out of charges.");
			break;
		case 11126:
			player.getPacketSender().sendMessage("Your bracelet has run out of charges.");
			break;
		case 5509:
		case 5510:
		case 5512:
			RunecraftingPouches.check(player, RunecraftingPouch.forId(itemId));
			break;
		case 995:
			MoneyPouch.depositMoney(player, player.getInventory().getAmount(995));
			break;
		case 1438:
		case 1448:
		case 1440:
		case 1442:
		case 1444:
		case 1446:
		case 1454:
		case 1452:
		case 1462:
		case 1458:
		case 1456:
		case 1450:
		case 21106:
			Runecrafting.handleTalisman(player, itemId);
			break;
		}
	}

	public void thirdClickAction(Player player, Packet packet) {
		int itemId = packet.readShortA();
		int slot = packet.readLEShortA();
		int interfaceId = packet.readLEShortA();
		if (slot < 0 || slot > player.getInventory().capacity())
			return;
		if (player.getInventory().getItems()[slot].getId() != itemId)
			return;
		if (JarData.forJar(itemId) != null) {
			PuroPuro.lootJar(player, new Item(itemId, 1), JarData.forJar(itemId));
			return;
		}
		if (SummoningData.isPouch(player, itemId, 3)) {
			return;
		}
		if (SummoningData.isPouch(player, itemId, 3)) {
			return;
		}
		switch (itemId) {
		case 11716:
			handleHasta(player);
			break;
		case 11602:
			if (player.getInventory().contains(11602)) {
				player.getInventory().delete(11602, 1);
				player.getInventory().add(11601, 1);
				player.getPacketSender().sendMessage("You remove the upgrade kit, your armour is now useless.");
				player.performAnimation(new Animation(9943));
				player.getPacketSender().sendMessage("So you throw it away...");
			} else {
				player.getPacketSender().sendMessage("You don't have this item.");
			}
			break;
		case 11603:
			if (player.getInventory().contains(11603)) {
				player.getInventory().delete(11603, 1);
				player.getInventory().add(11601, 1);
				player.getPacketSender().sendMessage("You remove the upgrade kit, your armour is now useless.");
				player.performAnimation(new Animation(9943));
				player.getPacketSender().sendMessage("So you throw it away...");
			} else {
				player.getPacketSender().sendMessage("You don't have this item.");
			}
			break;
		case 11604:
			if (player.getInventory().contains(11604)) {
				player.getInventory().delete(11604, 1);
				player.getInventory().add(11601, 1);
				player.getPacketSender().sendMessage("You remove the upgrade kit, your armour is now useless.");
				player.performAnimation(new Animation(9943));
				player.getPacketSender().sendMessage("So you throw it away...");
			} else {
				player.getPacketSender().sendMessage("You don't have this item.");
			}
			break;
		case 11605:
			if (player.getInventory().contains(11605)) {
				player.getInventory().delete(11605, 1);
				player.getInventory().add(11601, 1);
				player.getPacketSender().sendMessage("You remove the upgrade kit, your armour is now useless.");
				player.performAnimation(new Animation(9943));
				player.getPacketSender().sendMessage("So you throw it away...");
			} else {
				player.getPacketSender().sendMessage("You don't have this item.");
			}
			break;
		case 11606:
			if (player.getInventory().contains(11606)) {
				player.getInventory().delete(11606, 1);
				player.getInventory().add(11601, 1);
				player.getPacketSender().sendMessage("You remove the upgrade kit, your armour is now useless.");
				player.performAnimation(new Animation(9943));
				player.getPacketSender().sendMessage("So you throw it away...");
			} else {
				player.getPacketSender().sendMessage("You don't have this item.");
			}
			break;
		case 11607:
			if (player.getInventory().contains(11607)) {
				player.getInventory().delete(11607, 1);
				player.getInventory().add(11601, 1);
				player.getPacketSender().sendMessage("You remove the upgrade kit, your armour is now useless.");
				player.performAnimation(new Animation(9943));
				player.getPacketSender().sendMessage("So you throw it away...");
			} else {
				player.getPacketSender().sendMessage("You don't have this item.");
			}
			break;
		case 11608:
			if (player.getInventory().contains(11608)) {
				player.getInventory().delete(11608, 1);
				player.getInventory().add(11601, 1);
				player.getPacketSender().sendMessage("You remove the upgrade kit, your armour is now useless.");
				player.performAnimation(new Animation(9943));
				player.getPacketSender().sendMessage("So you throw it away...");
			} else {
				player.getPacketSender().sendMessage("You don't have this item.");
			}
			break;
		case 11609:
			if (player.getInventory().contains(11609)) {
				player.getInventory().delete(11609, 1);
				player.getInventory().add(11601, 1);
				player.getPacketSender().sendMessage("You remove the upgrade kit, your armour is now useless.");
				player.performAnimation(new Animation(9943));
				player.getPacketSender().sendMessage("So you throw it away...");
			} else {
				player.getPacketSender().sendMessage("You don't have this item.");
			}
			break;
		case 11610:
			if (player.getInventory().contains(11610)) {
				player.getInventory().delete(11610, 1);
				player.getInventory().add(11601, 1);
				player.getPacketSender().sendMessage("You remove the upgrade kit, your armour is now useless.");
				player.performAnimation(new Animation(9943));
				player.getPacketSender().sendMessage("So you throw it away...");
			} else {
				player.getPacketSender().sendMessage("You don't have this item.");
			}
			break;
		case 11611:
			if (player.getInventory().contains(11611)) {
				player.getInventory().delete(11611, 1);
				player.getInventory().add(11601, 1);
				player.getPacketSender().sendMessage("You remove the upgrade kit, your armour is now useless.");
				player.performAnimation(new Animation(9943));
				player.getPacketSender().sendMessage("So you throw it away...");
			} else {
				player.getPacketSender().sendMessage("You don't have this item.");
			}
			break;
		case 11612:
			if (player.getInventory().contains(11612)) {
				player.getInventory().delete(11612, 1);
				player.getInventory().add(11601, 1);
				player.getPacketSender().sendMessage("You remove the upgrade kit, your armour is now useless.");
				player.performAnimation(new Animation(9943));
				player.getPacketSender().sendMessage("So you throw it away...");
			} else {
				player.getPacketSender().sendMessage("You don't have this item.");
			}
			break;
		case 11614:
			if (player.getInventory().contains(11614)) {
				player.getInventory().delete(11614, 1);
				player.getInventory().add(11601, 1);
				player.getPacketSender().sendMessage("You remove the upgrade kit, your armour is now useless.");
				player.performAnimation(new Animation(9943));
				player.getPacketSender().sendMessage("So you throw it away...");
			} else {
				player.getPacketSender().sendMessage("You don't have this item.");
			}
			break;
		case 11615:
			if (player.getInventory().contains(11615)) {
				player.getInventory().delete(11615, 1);
				player.getInventory().add(11601, 1);
				player.getPacketSender().sendMessage("You remove the upgrade kit, your armour is now useless.");
				player.performAnimation(new Animation(9943));
				player.getPacketSender().sendMessage("So you throw it away...");
			} else {
				player.getPacketSender().sendMessage("You don't have this item.");
			}
			break;
		case 11616:
			if (player.getInventory().contains(11616)) {
				player.getInventory().delete(11616, 1);
				player.getInventory().add(11601, 1);
				player.getPacketSender().sendMessage("You remove the upgrade kit, your armour is now useless.");
				player.performAnimation(new Animation(9943));
				player.getPacketSender().sendMessage("So you throw it away...");
			} else {
				player.getPacketSender().sendMessage("You don't have this item.");
			}
			break;
		case 11617:
			if (player.getInventory().contains(11617)) {
				player.getInventory().delete(11617, 1);
				player.getInventory().add(11601, 1);
				player.getPacketSender().sendMessage("You remove the upgrade kit, your armour is now useless.");
				player.performAnimation(new Animation(9943));
				player.getPacketSender().sendMessage("So you throw it away...");
			} else {
				player.getPacketSender().sendMessage("You don't have this item.");
			}
			break;
		case 11618:
			if (player.getInventory().contains(11618)) {
				player.getInventory().delete(11618, 1);
				player.getInventory().add(11601, 1);
				player.getPacketSender().sendMessage("You remove the upgrade kit, your armour is now useless.");
				player.performAnimation(new Animation(9943));
				player.getPacketSender().sendMessage("So you throw it away...");
			} else {
				player.getPacketSender().sendMessage("You don't have this item.");
			}
			break;
		case 7500:
			player.forceChat("Omg I am a nub!!! Jonny RULES!");
			break;
		case 19670:
			if (player.busy()) {
				player.getPacketSender().sendMessage("You can not do this right now.");
				return;
			}
			break;
		case 6500:
			player.getPacketSender().sendMessage("You're currently gain experience and picking up charms.");
			break;
		case 4155:
				player.getDialog().sendDialog(new SlayerTeleports(player));
			break;
		case 6570:
			if (player.getInventory().contains(6570) && player.getInventory().getAmount(6529) >= 50000) {
				player.getInventory().delete(6570, 1).delete(6529, 50000).add(19111, 1);
				player.getPacketSender().sendMessage("You have upgraded your Fire cape into a TokHaar-Kal cape!");
			} else {
				player.getPacketSender().sendMessage(
						"You need at least 50.000 Tokkul to upgrade your Fire Cape into a TokHaar-Kal cape.");
			}
			break;
		case 21077:
			/*
			 * double charges = (double) player.getToxicStaffCharges() / 110;
			 * DecimalFormat double_decimal_format = new DecimalFormat("#.00");
			 * player.getPacketSender().sendMessage(
			 * "Your Toxic staff of the dead has " +
			 * double_decimal_format.format(charges) + "% of it's charges left."
			 * );
			 */
			break;
		case 15262:
			if (!player.getClickDelay().elapsed(1300))
				return;
			int boxAmount = player.getInventory().getAmount(15262);
			int shardAmount = player.getInventory().getAmount(18016);

			long finalAmount = (long) boxAmount * 10000 + shardAmount;
			int canAdd = Integer.MAX_VALUE - shardAmount;

			if (finalAmount >= Integer.MAX_VALUE) {
				boxAmount = canAdd / 10000;

			}

			if (boxAmount > 0)
				player.getInventory().delete(15262, boxAmount).add(18016, 10000 * boxAmount);
			player.getClickDelay().reset();
			break;
		case 5509:
		case 5510:
		case 5512:
			RunecraftingPouches.empty(player, RunecraftingPouch.forId(itemId));
			break;
		case 11283: // DFS
			player.getPacketSender()
					.sendMessage("Your Dragonfire shield has " + player.getDfsCharges() + "/50 dragon-fire charges.");
			break;
		}
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0 && player.isDying()) {
			return;
		}
		switch (packet.getOpcode()) {
			case FIRST_ITEM_ACTION_OPCODE: {
				firstAction(player, packet);
				break;
			}
			case SECOND_ITEM_ACTION_OPCODE:
				secondAction(player, packet);
				break;
			case THIRD_ITEM_ACTION_OPCODE:
				thirdClickAction(player, packet);
				break;
		}
	}

	public static final int SECOND_ITEM_ACTION_OPCODE = 75;

	public static final int FIRST_ITEM_ACTION_OPCODE = 122;

	public static final int THIRD_ITEM_ACTION_OPCODE = 16;

}
