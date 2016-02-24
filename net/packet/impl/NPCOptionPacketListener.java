package com.ikov.net.packet.impl;

import com.ikov.engine.task.Task;
import com.ikov.engine.task.TaskManager;
import com.ikov.engine.task.impl.WalkToTask;
import com.ikov.world.content.BankPin;
import com.ikov.world.content.BossSystem;
import com.ikov.engine.task.impl.WalkToTask.FinalizedMovementTask;
import com.ikov.model.Animation;
import com.ikov.model.GameMode;
import com.ikov.model.Graphic;
import com.ikov.model.PlayerRights;
import com.ikov.model.Position;
import com.ikov.GameSettings;
import com.ikov.model.Skill;
import com.ikov.model.container.impl.Shop.ShopManager;
import com.ikov.model.definitions.NpcDefinition;
import com.ikov.net.packet.Packet;
import com.ikov.world.content.minigames.impl.Zulrah;
import com.ikov.net.packet.PacketListener;
import com.ikov.world.World;
import com.ikov.world.content.EnergyHandler;
import com.ikov.world.content.LoyaltyProgramme;
import com.ikov.world.content.MemberScrolls;
import com.ikov.world.content.PlayerPanel;
import com.ikov.world.content.combat.CombatFactory;
import com.ikov.world.content.combat.magic.CombatSpell;
import com.ikov.world.content.combat.magic.CombatSpells;
import com.ikov.world.content.combat.weapon.CombatSpecial;
import com.ikov.world.content.dialogue.DialogueManager;
import com.ikov.world.content.dialogue.impl.Denath;
import com.ikov.world.content.dialogue.impl.ExplorerJack;
import com.ikov.world.content.grandexchange.GrandExchange;
import com.ikov.world.content.minigames.impl.ClawQuest;
import com.ikov.world.content.minigames.impl.WarriorsGuild;
import com.ikov.world.content.skill.impl.crafting.Tanning;
import com.ikov.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.ikov.world.content.skill.impl.fishing.Fishing;
import com.ikov.world.content.skill.impl.hunter.PuroPuro;
import com.ikov.world.content.skill.impl.runecrafting.DesoSpan;
import com.ikov.world.content.skill.impl.slayer.SlayerDialogues;
import com.ikov.world.content.skill.impl.slayer.SlayerTasks;
import com.ikov.world.content.skill.impl.summoning.BossPets;
import com.ikov.world.content.skill.impl.summoning.Summoning;
import com.ikov.world.content.skill.impl.summoning.SummoningData;
import com.ikov.world.content.transportation.TeleportHandler;
import com.ikov.world.entity.impl.npc.NPC;
import com.ikov.world.entity.impl.player.Player;

public class NPCOptionPacketListener implements PacketListener {


	private static void firstClick(Player player, Packet packet) {
		if(player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin() && player.getBankPinAttributes().onDifferent(player)) {
			BankPin.init(player, false);
			return;
		}
		int index = packet.readLEShort();
		if(index < 0 || index > World.getNpcs().capacity())
			return;
		final NPC npc = World.getNpcs().get(index);
		if (npc == null)
			return;
		player.setEntityInteraction(npc);
		if(player.getRights() == PlayerRights.OWNER)
			player.getPacketSender().sendMessage("First click npc id: "+npc.getId());
		if(BossPets.pickup(player, npc)) {
			player.getMovementQueue().reset();
			return;
		}
		player.setWalkToTask(new WalkToTask(player, npc.getPosition(), npc.getSize(), new FinalizedMovementTask() {
			@Override
			public void execute() {
				if(SummoningData.beastOfBurden(npc.getId())) {
					Summoning summoning = player.getSummoning();
					if(summoning.getBeastOfBurden() != null && summoning.getFamiliar() != null && summoning.getFamiliar().getSummonNpc() != null && summoning.getFamiliar().getSummonNpc().getIndex() == npc.getIndex()) {
						summoning.store();
						player.getMovementQueue().reset();
					} else {
						player.getPacketSender().sendMessage("That familiar is not yours!");
					}
					return;
				}
				switch(npc.getId()) {
				case 4663:
					if(player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 3) {
						DialogueManager.start(player, 159);
						player.setDialogueActionId(159);
					} else if(player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 4) {
						if(player.getInventory().getAmount(15273) >= 100) {
							player.getInventory().delete(15273, 100);
							player.getMinigameAttributes().getClawQuestAttributes().setQuestParts(5);
							DialogueManager.start(player, 166);
						} else {
							DialogueManager.start(player, 164);
						}
					} else if(player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() >= 6) {
						if(player.getMinigameAttributes().getClawQuestAttributes().getSamples() >= player.getMinigameAttributes().getClawQuestAttributes().SAMPLES_NEEDED) {
							if(!player.getInventory().isFull()) {
								player.getMinigameAttributes().getClawQuestAttributes().minusSamples(player.getMinigameAttributes().getClawQuestAttributes().SAMPLES_NEEDED);
								player.getPacketSender().sendMessage("Denath takes the samples and converts it into a bravery potion.");
								player.getPacketSender().sendMessage("If you lose this potion you can bring denath more samples to get another one.");
								player.getInventory().add(739, 1);
								player.getMinigameAttributes().getClawQuestAttributes().setQuestParts(7);
								DialogueManager.start(player, 169);
							} else {
								player.getPacketSender().sendMessage("You need one free slot to have your samples converted");
							}
						} else {
							DialogueManager.start(player, Denath.getDialogue(player));
						}
					} else {
						DialogueManager.sendStatement(player, "He doesn't seem interested in speaking to me right now.");
					}
					break;
				case 2947:
					if(player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 1 || !player.getInventory().contains(6040)) {
						if(player.getSkillManager().getMaxLevel(17) < 50) {
							player.getPacketSender().sendMessage("You need a thieving level of 50 to pickpocket this woman.");
							return;
						}
						if(!player.getInventory().isFull()) {
							player.performAnimation(new Animation(881));
							TaskManager.submit(new Task(1, player, false) {
								int tick = 0;
								@Override
								public void execute() {
									switch(tick) {
									case 2:
											player.getInventory().add(6040, 1);
											DialogueManager.sendStatement(player, "I think this is what the king wanted me to steal for him...");
											player.getMinigameAttributes().getClawQuestAttributes().setQuestParts(2);
										stop();
										break;
									}
									tick++;
								}
							});
						} else {
							player.getPacketSender().sendMessage("You need at least one free space.");
						}
					} else {
						player.getPacketSender().sendMessage("I cannot pick pocket this woman and I'm not sure why.");
					}
					break;
				case 4646:
					if(player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 0) {
						if(player.getSkillManager().getMaxLevel(17) < 50 && player.getSkillManager().getMaxLevel(10) < 91) {
							player.getPacketSender().sendMessage("You do not meet all the requirements for this quest.");
							return;
						}
						player.setDialogueActionId(152);
						DialogueManager.start(player, 152);
					} else if(player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 2) {
						if(player.getInventory().contains(6040)) { 
							DialogueManager.start(player, 156);
							player.getMinigameAttributes().getClawQuestAttributes().setQuestParts(3);
						} else {
							DialogueManager.start(player, 155);
						}

					} else if(player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 5) {
						player.getMinigameAttributes().getClawQuestAttributes().setQuestParts(6);
						PlayerPanel.refreshPanel(player);
						DialogueManager.start(player, 167);
					} else if(player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 7) {
						player.getMinigameAttributes().getClawQuestAttributes().setQuestParts(8);
						DialogueManager.start(player, 170);
					} else if(player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 9) {
						if(player.getInventory().contains(691)) {
							if(player.getInventory().getFreeSlots() > 2) {
								player.getMinigameAttributes().getClawQuestAttributes().setQuestParts(10);
								player.getInventory().delete(691, 1);
								ClawQuest.giveReward(player);
								//DialogueManager.start(player, 178);
							} else {
								player.getPacketSender().sendMessage("You need 3 free slots before you can claim your reward.");
							}
						} else {
							DialogueManager.sendStatement(player, "Show me the proof when you've killed the beast.");
						}
					} else {
						DialogueManager.sendStatement(player, "The king does not seem interested in talking to you right now.");
					}
					break;
				case 5093:
					ShopManager.getShops().get(84).open(player);
					break;
				case 2286:
					player.setDialogueActionId(139);
					DialogueManager.start(player, 139);
					break;
				case 4002:
					player.gambler_id = 1;
					if(GameSettings.gambler_1) {
						player.setDialogueActionId(151);
						DialogueManager.start(player, 151);
					} else {
						player.setDialogueActionId(144);
						DialogueManager.start(player, 144);
					}
					break;
				case 2633:
					player.gambler_id = 2;
					if(GameSettings.gambler_2) {
						player.setDialogueActionId(151);
						DialogueManager.start(player, 151);
					} else {
						player.setDialogueActionId(144);
						DialogueManager.start(player, 144);
					}
					break;
				case 4375:
					player.setDialogueActionId(133);
					DialogueManager.start(player, 133);
					break;
				case 1304:
					DialogueManager.start(player, 127);
					player.setDialogueActionId(80);
					break;
				case 457:
					DialogueManager.start(player, 117);
					player.setDialogueActionId(74);
					break;
				case 695:
					DialogueManager.start(player, 136);
					player.setDialogueActionId(136);
					break;
				case 8710:
				case 8707:
				case 8706:
				case 8705:
					EnergyHandler.rest(player);
					break;
				case 1396:
					ShopManager.getShops().get(78).open(player);
					break;
				case 653:
					if(player.getGameMode() == GameMode.IRONMAN || player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
						ShopManager.getShops().get(82).open(player);
					} else {
						ShopManager.getShops().get(27).open(player);
					}
					break;
				case 947:
					//if(player.getPosition().getX() >= 3092) {
					//	player.getMovementQueue().reset();
						GrandExchange.open(player);
					//}
					break;
				case 11226:
					if(Dungeoneering.doingDungeoneering(player)) {
						ShopManager.getShops().get(45).open(player);
					}
					break;
				case 9713:
					DialogueManager.start(player, 107);
					player.setDialogueActionId(69);
					break;
				case 2622:
					ShopManager.getShops().get(43).open(player);
					break;
				case 3101:
					DialogueManager.start(player, 90);
					player.setDialogueActionId(57);
					break;
				case 7969:
					DialogueManager.start(player, ExplorerJack.getDialogue(player));
					break;
				case 3147:
					player.setDialogueActionId(135);
					DialogueManager.start(player, 135);
					break;
				case 1597:
				case 8275:
				case 9085:
				case 7780:
					if(npc.getId() != player.getSlayer().getSlayerMaster().getNpcId()) {
						player.getPacketSender().sendMessage("This is not your current Slayer master.");
						return;
					}
					DialogueManager.start(player, SlayerDialogues.dialogue(player));
					break;
				case 437:
					DialogueManager.start(player, 99);
					player.setDialogueActionId(58);
					break;
				case 5112:
					ShopManager.getShops().get(38).open(player);
					break;
				case 8591:
					//player.nomadQuest[0] = player.nomadQuest[1] = player.nomadQuest[2] = false;
					if(!player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(0)) {
						DialogueManager.start(player, 48);
						player.setDialogueActionId(23);
					} else if(player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(0) && !player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(1)) {
						DialogueManager.start(player, 50);
						player.setDialogueActionId(24);
					} else if(player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(1))
						DialogueManager.start(player, 53);
					break;
				case 3385:
					if(player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0) && player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() < 6) {
						DialogueManager.start(player, 39);
						return;
					}
					if(player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6) {
						DialogueManager.start(player, 46);
						return;
					}
					DialogueManager.start(player, 38);
					player.setDialogueActionId(20);
					break;
				case 6139:
					DialogueManager.start(player, 29);
					player.setDialogueActionId(17);
					break;
				case 3789:
					player.getPacketSender().sendInterface(18730);
					player.getPacketSender().sendString(18729, "Commendations: "+Integer.toString(player.getPointsHandler().getCommendations()));
					break;
				case 2948:
					DialogueManager.start(player, WarriorsGuild.warriorsGuildDialogue(player));
					break;
				case 650:
					ShopManager.getShops().get(35).open(player);
					break;
				case 6055:
				case 6056:
				case 6057:
				case 6058:
				case 6059:
				case 6060:
				case 6061:
				case 6062:
				case 6063:
				case 6064:
				case 7903:
					PuroPuro.catchImpling(player, npc);
					break;
				case 8022:
				case 8028:
					DesoSpan.siphon(player, npc);
					break;
				case 2579:
					player.setDialogueActionId(13);
					DialogueManager.start(player, 24);
					break;
				case 8725:
					player.setDialogueActionId(10);
					DialogueManager.start(player, 19);
					break;
				case 4249:
					player.setDialogueActionId(9);
					DialogueManager.start(player, 64);
					break;
				case 6807:
				case 6994:
				case 6995:
				case 6867:
				case 6868:
				case 6794:
				case 6795:
				case 6815:
				case 6816:
				case 6874:
				case 6873:
				case 3594:
				case 3590:
				case 3596:
					if(player.getSummoning().getFamiliar() == null || player.getSummoning().getFamiliar().getSummonNpc() == null || player.getSummoning().getFamiliar().getSummonNpc().getIndex() != npc.getIndex()) {
						player.getPacketSender().sendMessage("That is not your familiar.");
						return;
					}
					player.getSummoning().store();
					break;
				case 605:
					player.setDialogueActionId(8);
					DialogueManager.start(player, 13);
					break;
				case 6970:
					player.setDialogueActionId(3);
					DialogueManager.start(player, 3);
					break;
				case 4657:
					player.setDialogueActionId(5);
					DialogueManager.start(player, 5);
					break;
				case 318:
				case 316:
				case 313:
				case 312:
					player.setEntityInteraction(npc);
					Fishing.setupFishing(player, Fishing.forSpot(npc.getId(), false));
					break;
				case 2253:
					ShopManager.getShops().get(9).open(player);
					break;
				case 2733:
					ShopManager.getShops().get(60).open(player);
					break;
				case 805:
					ShopManager.getShops().get(34).open(player);
					break;
				case 462:
					ShopManager.getShops().get(33).open(player);
					break;
				case 461:
					ShopManager.getShops().get(32).open(player);
					break;
				case 8444:
					if(player.getDonorRights() == 0) {
						player.getPacketSender().sendMessage("You are not a donator... Get out of here!");
						player.moveTo(new Position(3087, 3502, 0));
						return;
					}
					ShopManager.getShops().get(31).open(player);
					break;
				case 8459:
					ShopManager.getShops().get(30).open(player);
					break;
				case 3299:
					ShopManager.getShops().get(21).open(player);
					break;
				case 548:
					ShopManager.getShops().get(20).open(player);
					break;
				case 1685:
					ShopManager.getShops().get(19).open(player);
					break;
				case 308:
					ShopManager.getShops().get(18).open(player);
					break;
				case 802:
					ShopManager.getShops().get(17).open(player);
					break;
				case 278:
					ShopManager.getShops().get(16).open(player);
					break;
				case 4946:
					ShopManager.getShops().get(15).open(player);
					break;
				case 948:
					ShopManager.getShops().get(13).open(player);
					break;
				case 4906:
					ShopManager.getShops().get(14).open(player);
					break;
				case 520:
				case 521:
					player.setDialogueActionId(128);
					DialogueManager.start(player, 128);
					break;
				case 2292:
					ShopManager.getShops().get(11).open(player);
					break;
				case 2676:
					player.getPacketSender().sendInterface(3559);
					player.getAppearance().setCanChangeAppearance(true);
					break;
				case 494:
				case 1360:
					player.getBank(player.getCurrentBankTab()).open();
					break;
				}
				if(!(npc.getId() >= 8705 && npc.getId() <= 8710)) {
					npc.setPositionToFace(player.getPosition());
				}
				player.setPositionToFace(npc.getPosition());
			}
		}));
	}

	private static void attackNPC(Player player, Packet packet) {
		if(player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin() && player.getBankPinAttributes().onDifferent(player)) {
			BankPin.init(player, false);
			return;
		}
		int index = packet.readShortA();
		if(index < 0 || index > World.getNpcs().capacity())
			return;
		final NPC interact = World.getNpcs().get(index);
		if (interact == null)
			return;

		if (!NpcDefinition.getDefinitions()[interact.getId()].isAttackable()) {
			return;
		}

		if(interact.getConstitution() <= 0) {
			player.getMovementQueue().reset();
			return;
		}

		if(player.getCombatBuilder().getStrategy() == null) {
			player.getCombatBuilder().determineStrategy();
		}
		if (CombatFactory.checkAttackDistance(player, interact)) {
			player.getMovementQueue().reset();
		}

		if(player.getRights() == PlayerRights.OWNER) {
			player.getPacketSender().sendMessage("Attacking npc id: "+interact.getId());
		}
		if(player.isZulrahMoving() && (interact.getId() == Zulrah.ZULRAH_GREEN_NPC_ID || interact.getId() == Zulrah.ZULRAH_RED_NPC_ID || interact.getId() == Zulrah.ZULRAH_BLUE_NPC_ID || interact.getId() == Zulrah.ZULRAH_JAD_NPC_ID)) {
			return;
		}
		if(interact.getId() == 1172) {
			if(!player.getDrankBraverly()) {
				player.getPacketSender().sendMessage("You need to drink a braverly potion before fighting this monster.");
				return;
			} else {
				player.getCombatBuilder().attack(interact);
			}
		}
		player.getCombatBuilder().attack(interact);
	}
	public void handleSecondClick(Player player, Packet packet) {
		if(player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin() && player.getBankPinAttributes().onDifferent(player)) {
			BankPin.init(player, false);
			return;
		}
		int index = packet.readLEShortA();
		if(index < 0 || index > World.getNpcs().capacity())
			return;
		final NPC npc = World.getNpcs().get(index);
		if(npc == null)
			return;
		player.setEntityInteraction(npc);
		final int npcId = npc.getId();
		if(player.getRights() == PlayerRights.OWNER)
			player.getPacketSender().sendMessage("Second click npc id: "+npcId);
		player.setWalkToTask(new WalkToTask(player, npc.getPosition(), npc.getSize(), new FinalizedMovementTask() {
			@Override
			public void execute() {
				switch(npc.getId()) {
				case 4646:
					if(player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 8) {
						if(!player.getInventory().isFull()) {
							player.getInventory().add(993, 1);
							player.moveTo(new Position(2660, 3306, 0));
							DialogueManager.start(player, 176);
						} else {
							player.getPacketSender().sendMessage("You need 1 slot available for this teleport");
						}
					} else if(player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 10) {
						player.moveTo(new Position(2660, 3306, 0));
					} else {
						player.getPacketSender().sendMessage("The king has no reason to drain his energy on you.");
					}
					break;
				case 4375:
					if(player.getLastBoss() == 0) {
						player.getPacketSender().sendMessage("You have not spawned a boss since your last login!");
					} else if(player.getClanChatName() == null && !player.isBossSolo()) {
						 if (player.getClanChatName().equalsIgnoreCase("ikov")) { 
								player.getPacketSender().sendMessage("Your last boss spawn was in a clan, you cannot do this from the ikov cc!");
							 player.getPacketSender().sendInterfaceRemoval();
							 return;
						 }
						player.getPacketSender().sendMessage("Your last boss spawn was in a clan, you currently are not in one!");
						player.getPacketSender().sendInterfaceRemoval();
						return;
					} else {
						BossSystem.startInstance(player, player.getLastBoss(), player.isBossSolo());
					}
					break;
				case 2579:
					ShopManager.getShops().get(46).open(player);
					player.getPacketSender().sendMessage("<col=255>You currently have "+player.getPointsHandler().getPrestigePoints()+" Prestige points!");
					break;
				case 457:
					player.getPacketSender().sendMessage("The ghost teleports you away.");
					player.getPacketSender().sendInterfaceRemoval();
					player.moveTo(new Position(3651, 3486));
					break;
				case 2622:
					ShopManager.getShops().get(43).open(player);
					break;
				case 462:
					npc.performAnimation(CombatSpells.CONFUSE.getSpell().castAnimation().get());
					npc.forceChat("Off you go!");
					TeleportHandler.teleportPlayer(player,new Position(2911, 4832), player.getSpellbook().getTeleportType());
					break;
				case 3101:
					DialogueManager.start(player, 95);
					player.setDialogueActionId(57);
					break;
				case 7969:
					ShopManager.getShops().get(28).open(player);
					break;
				case 2253:
					ShopManager.getShops().get(10).open(player);
					break;
				case 605:
					player.getPacketSender().sendMessage("").sendMessage("You currently have "+player.getPointsHandler().getVotingPoints()+" Voting points.").sendMessage("You can earn points and coins by voting. To do so, simply use the ::vote command.");;
					ShopManager.getShops().get(27).open(player);
					break;
				case 4657:
				
					break;
				case 1597:
				case 9085:
				case 7780:
					if(npc.getId() != player.getSlayer().getSlayerMaster().getNpcId()) {
						player.getPacketSender().sendMessage("This is not your current Slayer master.");
						return;
					}
					if(player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK)
						player.getSlayer().assignTask();
					else
						DialogueManager.start(player, SlayerDialogues.findAssignment(player));
					break;
				case 8591:
					if(!player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(1)) {
						player.getPacketSender().sendMessage("You must complete Nomad's quest before being able to use this shop.");
						return;
					}
					ShopManager.getShops().get(37).open(player);
					break;
				case 805:
					Tanning.selectionInterface(player);
					break;
				case 318:
				case 316:
				case 313:
				case 312:
					player.setEntityInteraction(npc);
					Fishing.setupFishing(player, Fishing.forSpot(npc.getId(), true));
					break;
				case 4946:
					ShopManager.getShops().get(15).open(player);
					break;
				case 946:
					ShopManager.getShops().get(1).open(player);
					break;
				case 961:
					ShopManager.getShops().get(6).open(player);
					break;
				case 1861:
					ShopManager.getShops().get(3).open(player);
					break;
				case 705:
					ShopManager.getShops().get(4).open(player);
					break;
				case 6970:
					player.setDialogueActionId(35);
					DialogueManager.start(player, 63);
					break;
				}
				npc.setPositionToFace(player.getPosition());
				player.setPositionToFace(npc.getPosition());
			}
		}));
	}

	public void handleThirdClick(Player player, Packet packet) {
		if(player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin() && player.getBankPinAttributes().onDifferent(player)) {
			BankPin.init(player, false);
			return;
		}
		int index = packet.readShort();
		if(index < 0 || index > World.getNpcs().capacity())
			return;
		final NPC npc = World.getNpcs().get(index);
		if (npc == null)
			return;
		player.setEntityInteraction(npc).setPositionToFace(npc.getPosition().copy());
		npc.setPositionToFace(player.getPosition());
		if(player.getRights() == PlayerRights.OWNER)
			player.getPacketSender().sendMessage("Third click npc id: "+npc.getId());
		player.setWalkToTask(new WalkToTask(player, npc.getPosition(), npc.getSize(), new FinalizedMovementTask() {
			@Override
			public void execute() {
				switch(npc.getId()) {
				case 3101:
					ShopManager.getShops().get(42).open(player);
					break;
				case 1597:
				case 8275:
				case 9085:
				case 7780:
					ShopManager.getShops().get(40).open(player);
					break;
				case 605:
					LoyaltyProgramme.open(player);
					break;
				case 4657:
					DialogueManager.start(player, MemberScrolls.getTotalFunds(player));
					break;
				case 946:
					ShopManager.getShops().get(0).open(player);
					break;
				case 1861:
					ShopManager.getShops().get(2).open(player);
					break;
				case 2253:
					ShopManager.getShops().get(83).open(player);
					break;
				case 961:
					if(player.getDonorRights() == 0) {
						player.getPacketSender().sendMessage("This feature is currently only available for members.");
						return;
					}
					boolean restore = player.getSpecialPercentage() < 100;
					if(restore) {
						player.setSpecialPercentage(100);
						CombatSpecial.updateBar(player);
						player.getPacketSender().sendMessage("Your special attack energy has been restored.");
					}
					for(Skill skill : Skill.values()) {
						if(player.getSkillManager().getCurrentLevel(skill) < player.getSkillManager().getMaxLevel(skill)) {
							player.getSkillManager().setCurrentLevel(skill, player.getSkillManager().getMaxLevel(skill));
							restore = true;
						}
					}
					if(restore) {
						player.performGraphic(new Graphic(1302));
						player.getPacketSender().sendMessage("Your stats have been restored.");
					} else
						player.getPacketSender().sendMessage("Your stats do not need to be restored at the moment.");
					break;
				case 705:
					ShopManager.getShops().get(5).open(player);
					break;
				}
				npc.setPositionToFace(player.getPosition());
				player.setPositionToFace(npc.getPosition());
			}
		}));
	}

	public void handleFourthClick(Player player, Packet packet) {
		if(player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin() && player.getBankPinAttributes().onDifferent(player)) {
			BankPin.init(player, false);
			return;
		}
		int index = packet.readLEShort();
		if(index < 0 || index > World.getNpcs().capacity())
			return;
		final NPC npc = World.getNpcs().get(index);
		if (npc == null)
			return;
		player.setEntityInteraction(npc);
		if(player.getRights() == PlayerRights.OWNER)
			player.getPacketSender().sendMessage("Fourth click npc id: "+npc.getId());
		player.setWalkToTask(new WalkToTask(player, npc.getPosition(), npc.getSize(), new FinalizedMovementTask() {
			@Override
			public void execute() {
				switch(npc.getId()) {
				case 4657:
				if(player.getDonorRights() == 0) {
						player.getPacketSender().sendMessage("You need to be a member to teleport to this zone.").sendMessage("To become a member, visit ikov2.org and purchase a scroll.");
						return;
					}
					TeleportHandler.teleportPlayer(player, new Position(3424, 2919), player.getSpellbook().getTeleportType());
					break;
				case 705:
					ShopManager.getShops().get(7).open(player);
					break;
				case 2253:
					ShopManager.getShops().get(8).open(player);
					break;
				case 1597:
				case 9085:
				case 8275:
				case 7780:
					player.getPacketSender().sendString(36030, "Current Points:   "+player.getPointsHandler().getSlayerPoints());
					player.getPacketSender().sendInterface(36000);
					break;
				}
				npc.setPositionToFace(player.getPosition());
				player.setPositionToFace(npc.getPosition());
			}
		}));
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		if(player.isTeleporting() || player.isPlayerLocked() || player.getMovementQueue().isLockMovement())
			return;
		switch (packet.getOpcode()) {
		case ATTACK_NPC:
			attackNPC(player, packet);
			break;
		case FIRST_CLICK_OPCODE:
			firstClick(player, packet);
			break;
		case SECOND_CLICK_OPCODE:
			handleSecondClick(player, packet);
			break;
		case THIRD_CLICK_OPCODE:
			handleThirdClick(player, packet);
			break;
		case FOURTH_CLICK_OPCODE:
			handleFourthClick(player, packet);
			break;
		case MAGE_NPC:
			int npcIndex = packet.readLEShortA();
			int spellId = packet.readShortA();

			if (npcIndex < 0 || spellId < 0 || npcIndex > World.getNpcs().capacity()) {
				return;
			}

			NPC n = World.getNpcs().get(npcIndex);
			player.setEntityInteraction(n);

			CombatSpell spell = CombatSpells.getSpell(spellId);

			if (n == null || spell == null) {
				player.getMovementQueue().reset();
				return;
			}

			if (!NpcDefinition.getDefinitions()[n.getId()].isAttackable()) {
				player.getMovementQueue().reset();
				return;
			}

			if(n.getConstitution() <= 0) {
				player.getMovementQueue().reset();
				return;
			}

			player.setPositionToFace(n.getPosition());
			player.setCastSpell(spell);
			if(player.getCombatBuilder().getStrategy() == null) {
				player.getCombatBuilder().determineStrategy();
			}
			if (CombatFactory.checkAttackDistance(player, n)) {
				player.getMovementQueue().reset();
			}
			player.getCombatBuilder().resetCooldown();
			player.getCombatBuilder().attack(n);
			break;
		}
	}

	public static final int ATTACK_NPC = 72, FIRST_CLICK_OPCODE = 155, MAGE_NPC = 131, SECOND_CLICK_OPCODE = 17, THIRD_CLICK_OPCODE = 21, FOURTH_CLICK_OPCODE = 18;
}
