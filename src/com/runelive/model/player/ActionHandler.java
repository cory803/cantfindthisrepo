package com.runelive.model.player;

import com.runelive.GameSettings;
import com.runelive.engine.task.Task;
import com.runelive.engine.task.TaskManager;
import com.runelive.engine.task.impl.WalkToTask;
import com.runelive.model.*;
import com.runelive.model.container.impl.Equipment;
import com.runelive.model.container.impl.Shop;
import com.runelive.model.definitions.ItemDefinition;
import com.runelive.model.definitions.NpcDefinition;
import com.runelive.model.input.impl.DonateToWell;
import com.runelive.model.input.impl.EnterAmountOfLogsToAdd;
import com.runelive.model.input.impl.PosSearchShop;
import com.runelive.net.packet.Packet;
import com.runelive.util.Misc;
import com.runelive.world.ChaosTunnelHandler;
import com.runelive.world.World;
import com.runelive.world.content.*;
import com.runelive.world.content.combat.CombatFactory;
import com.runelive.world.content.combat.instanced.InstancedCerberus;
import com.runelive.world.content.combat.magic.Autocasting;
import com.runelive.world.content.combat.magic.CombatSpells;
import com.runelive.world.content.combat.prayer.CurseHandler;
import com.runelive.world.content.combat.prayer.PrayerHandler;
import com.runelive.world.content.combat.range.DwarfMultiCannon;
import com.runelive.world.content.combat.weapon.CombatSpecial;
import com.runelive.world.content.dialogue.DialogueManager;
import com.runelive.world.content.dialogue.impl.Denath;
import com.runelive.world.content.dialogue.impl.ExplorerJack;
import com.runelive.world.content.grandexchange.GrandExchange;
import com.runelive.world.content.minigames.TreasureIslandChest;
import com.runelive.world.content.minigames.impl.*;
import com.runelive.world.content.pos.PlayerOwnedShops;
import com.runelive.world.content.skill.Enchanting;
import com.runelive.world.content.skill.impl.agility.Agility;
import com.runelive.world.content.skill.impl.construction.Construction;
import com.runelive.world.content.skill.impl.crafting.Flax;
import com.runelive.world.content.skill.impl.crafting.Tanning;
import com.runelive.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.runelive.world.content.skill.impl.fishing.Fishing;
import com.runelive.world.content.skill.impl.hunter.Hunter;
import com.runelive.world.content.skill.impl.hunter.PuroPuro;
import com.runelive.world.content.skill.impl.mining.Mining;
import com.runelive.world.content.skill.impl.mining.MiningData;
import com.runelive.world.content.skill.impl.mining.Prospecting;
import com.runelive.world.content.skill.impl.runecrafting.DesoSpan;
import com.runelive.world.content.skill.impl.runecrafting.Runecrafting;
import com.runelive.world.content.skill.impl.runecrafting.RunecraftingData;
import com.runelive.world.content.skill.impl.slayer.SlayerDialogues;
import com.runelive.world.content.skill.impl.slayer.SlayerTasks;
import com.runelive.world.content.skill.impl.smithing.EquipmentMaking;
import com.runelive.world.content.skill.impl.smithing.Smelting;
import com.runelive.world.content.skill.impl.summoning.BossPets;
import com.runelive.world.content.skill.impl.summoning.Summoning;
import com.runelive.world.content.skill.impl.summoning.SummoningData;
import com.runelive.world.content.skill.impl.thieving.ThievingManager;
import com.runelive.world.content.skill.impl.thieving.ThievingStall;
import com.runelive.world.content.skill.impl.woodcutting.Woodcutting;
import com.runelive.world.content.skill.impl.woodcutting.WoodcuttingData;
import com.runelive.world.content.transportation.TeleportHandler;
import com.runelive.world.content.transportation.TeleportType;
import com.runelive.world.doors.DoorManager;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;
import org.scripts.kotlin.content.dialog.TownCrier;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/4/2016.
 *
 * @author Seba
 */
public final class ActionHandler {

    private final Player player;

    public ActionHandler(Player player) {
        this.player = player;
    }

    public void firstClickNpc(NPC npc) {
        if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
                && player.getBankPinAttributes().onDifferent(player)) {
            BankPin.init(player, false);
            return;
        }
        player.setEntityInteraction(npc);
        player.setNpcClickId(npc.getId());
        if (player.getRights() == PlayerRights.DEVELOPER)
            player.getPacketSender().sendMessage("First click npc id: " + npc.getId());
        if (BossPets.pickup(player, npc)) {
            player.getWalkingQueue().clear();
            return;
        }
        if (GameSettings.DEBUG_MODE) {
            // PlayerLogs.log(player, "" + player.getUsername()
            // + " in NPCOptionPacketListener: " + npc.getId() + " -
            // FIRST_CLICK_OPCODE");
        }

        if (SummoningData.beastOfBurden(npc.getId())) {
            Summoning summoning = player.getSummoning();
            if (summoning.getBeastOfBurden() != null && summoning.getFamiliar() != null
                    && summoning.getFamiliar().getSummonNpc() != null
                    && summoning.getFamiliar().getSummonNpc().getIndex() == npc.getIndex()) {
                summoning.store();
                player.getWalkingQueue().clear();
            } else {
                player.getPacketSender().sendMessage("That familiar is not yours!");
            }
            return;
        }
        switch (npc.getId()) {
            case 1385:
                DialogueManager.start(player, 249);
                player.setDialogueActionId(250);
                break;
            case 241:
                // if
                // (player.getMinigameAttributes().getShrek1Attributes().getQuestParts()
                // == 0) {
                DialogueManager.start(player, 230); // quest begins
					/*
					 * player.getMinigameAttributes().getShrek1Attributes().
					 * setQuestParts(1); PlayerPanel.refreshPanel(player); }
					 * else if
					 * (player.getMinigameAttributes().getShrek1Attributes().
					 * getQuestParts() == 1) { DialogueManager.start(player,
					 * 241); } else { DialogueManager.start(player, 240); }
					 */
                break;
            case 501:
                DialogueManager.start(player, 224);
                player.setDialogueActionId(224);
                for (Item item : player.getInventory().getItems()) {
                    ItemDefinition def = ItemDefinition.forId(item.getId() + 1);
                    if (def.getName().toLowerCase().startsWith("raw")) {
                        if (def.isNoted()) {
                            int notedId = item.getId() + 1;
                            player.getInventory().delete(item.getId(), 1);
                            player.getInventory().add(notedId, 1);
                        }
                    }
                }
                break;
            /** Construction **/
            case 4247:
                if (player.getHouseRooms()[0][0][0] == null) {
                    DialogueManager.start(player, 187);
                    player.setDialogueActionId(188);
                } else {
                    DialogueManager.start(player, 201);
                    player.setDialogueActionId(189);
                }
                break;
            case 590:
                if (player.getDonorRights() >= 3) {
                    Shop.ShopManager.getShops().get(113).open(player);
                } else {
                    player.getPacketSender()
                            .sendMessage("You need to be an Extreme donator or higher, to access this shop!");
                }
                break;
            case 308:
                Shop.ShopManager.getShops().get(18).open(player);
                break;
            case 2290: // ironman npc
                if (!player.getGameModeAssistant().isIronMan()) {
                    DialogueManager.sendStatement(player, "I am not an iron man so I cannot change my mode.");
                    return;
                }
                DialogueManager.start(player, 216);
                player.setDialogueActionId(217);
                break;
            case 1285:
                DialogueManager.start(player, 210);
                player.setDialogueActionId(210);
                break;
            case 4707: // bolt enchanter
                Enchanting.update_interface(player);
                break;
            case 5030:
                if (player.getTeleblockTimer() > 0) {
                    player.getPacketSender()
                            .sendMessage("You cannot sail with Captain Cain while teleport blocked!");
                    return;
                }
                player.setDialogueActionId(228);
                DialogueManager.start(player, 228);
                break;
            case 1093: // unnoter
					/*if (player.getGameMode() != GameMode.HARDCORE_IRONMAN) {
						DialogueManager.sendStatement(player, "B-A-A-H, you're not a hardcore ironman!");
						return;
					}
					DialogueManager.start(player, 207);*/
                break;
            case 2305:
                if (player.getMinigameAttributes().getFarmQuestAttributes().getQuestParts() == 0) {
                    if (player.getSkillManager().getMaxLevel(17) < 70
                            || player.getSkillManager().getMaxLevel(8) < 60
                            || player.getSkillManager().getMaxLevel(19) < 60) {
                        player.getPacketSender()
                                .sendMessage("You do not have the skills required to start this quest.");
                        return;
                    }
                    DialogueManager.start(player, 189);
                    player.getMinigameAttributes().getFarmQuestAttributes().setQuestParts(1);
                    PlayerPanel.refreshPanel(player);
                } else if (player.getMinigameAttributes().getFarmQuestAttributes().getQuestParts() == 1) {
                    if (player.getInventory().contains(5329) && player.getInventory().contains(771)) {
                        player.getMinigameAttributes().getFarmQuestAttributes().setQuestParts(2);
                        player.getInventory().delete(new Item(5329, 1));
                        player.getInventory().delete(new Item(771, 1));
                        DialogueManager.start(player, 200);
                    } else {
                        DialogueManager.start(player, 195);
                    }
                } else if (player.getMinigameAttributes().getFarmQuestAttributes().getQuestParts() == 2) {
                    DialogueManager.start(player, 197);
                } else if (player.getMinigameAttributes().getFarmQuestAttributes().getQuestParts() == 3) {
                    player.getMinigameAttributes().getFarmQuestAttributes().setQuestParts(4);
                    FarmingQuest.giveReward(player);
                    PlayerPanel.refreshPanel(player);
                } else {
                    DialogueManager.sendStatement(player,
                            "Vanessa does not seem interested in talking to you right now.");
                }
                break;
            case 536:
                DialogueManager.start(player, 187);
                player.setDialogueActionId(187);
                break;
            case 4663:
                if (player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 3) {
                    DialogueManager.start(player, 159);
                    player.setDialogueActionId(159);
                } else if (player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 4) {
                    if (player.getInventory().getAmount(15273) >= 100) {
                        player.getInventory().delete(15273, 100);
                        player.getMinigameAttributes().getClawQuestAttributes().setQuestParts(5);
                        DialogueManager.start(player, 166);
                    } else {
                        DialogueManager.start(player, 164);
                    }
                } else if (player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() >= 6) {
                    if (player.getMinigameAttributes().getClawQuestAttributes().getSamples() >= player
                            .getMinigameAttributes().getClawQuestAttributes().SAMPLES_NEEDED) {
                        if (!player.getInventory().isFull()) {
                            player.getMinigameAttributes().getClawQuestAttributes().minusSamples(
                                    player.getMinigameAttributes().getClawQuestAttributes().SAMPLES_NEEDED);
                            player.getPacketSender()
                                    .sendMessage("Denath takes the samples and converts it into a bravery potion.");
                            player.getPacketSender().sendMessage(
                                    "If you lose this potion you can bring denath more samples to get another one.");
                            player.getInventory().add(739, 1);
                            player.getMinigameAttributes().getClawQuestAttributes().setQuestParts(7);
                            DialogueManager.start(player, 169);
                        } else {
                            player.getPacketSender()
                                    .sendMessage("You need one free slot to have your samples converted");
                        }
                    } else {
                        DialogueManager.start(player, Denath.getDialogue(player));
                    }
                } else {
                    DialogueManager.sendStatement(player,
                            "He doesn't seem interested in speaking to me right now.");
                }
                break;
            case 2127:
                if (!GameSettings.POS_ENABLED) {
                    player.getPacketSender().sendMessage("Player owned shops have been disabled.");
                    return;
                }
                player.setDialogueActionId(214);
                DialogueManager.start(player, 214);
                break;
            case 291:
                if (player.getMinigameAttributes().getFarmQuestAttributes().getQuestParts() == 1) {
                    if (player.getSkillManager().getMaxLevel(17) < 70) {
                        player.getPacketSender()
                                .sendMessage("You need a thieving level of 70 to pickpocket this farmer.");
                        return;
                    }
                    if (player.getInventory().contains(1843)) {
                        player.getPacketSender().sendMessage(
                                "You have already stolen the key from the farmer. You don't need another.");
                        return;
                    }
                    if (!player.getInventory().isFull()) {
                        player.performAnimation(new Animation(881));
                        TaskManager.submit(new Task(1, player, false) {
                            int tick = 0;

                            @Override
                            public void execute() {
                                switch (tick) {
                                    case 2:
                                        int random = Misc.getRandom(15);
                                        if (random == 10) {
                                            player.getInventory().add(1843, 1);
                                            player.getMinigameAttributes().getFarmQuestAttributes().setQuestParts(2);
                                        } else {
                                            player.setTeleblockTimer(30);
                                            player.moveTo(new Position(2757, 3504, 0));
                                            player.getPacketSender().sendMessage(
                                                    "You were caught thieving by the Farmer. He has used his powers to move you and ");
                                            player.getPacketSender().sendMessage("teleblock you for 30 seconds.");
                                        }
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
                    player.getPacketSender().sendMessage("I don't need to pickpocket this farmer right now.");
                }
                break;
            case 2947:
                if (player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 1
                        || !player.getInventory().contains(6040)) {
                    if (player.getSkillManager().getMaxLevel(17) < 50) {
                        player.getPacketSender()
                                .sendMessage("You need a thieving level of 50 to pickpocket this woman.");
                        return;
                    }
                    if (!player.getInventory().isFull()) {
                        player.performAnimation(new Animation(881));
                        TaskManager.submit(new Task(1, player, false) {
                            int tick = 0;

                            @Override
                            public void execute() {
                                switch (tick) {
                                    case 2:
                                        player.getInventory().add(6040, 1);
                                        DialogueManager.sendStatement(player,
                                                "I think this is what the king wanted me to steal for him...");
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
                if (player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 0) {
                    if (player.getSkillManager().getMaxLevel(17) < 50
                            && player.getSkillManager().getMaxLevel(10) < 91) {
                        player.getPacketSender()
                                .sendMessage("You do not meet all the requirements for this quest.");
                        return;
                    }
                    player.setDialogueActionId(152);
                    DialogueManager.start(player, 152);
                } else if (player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 2) {
                    if (player.getInventory().contains(6040)) {
                        DialogueManager.start(player, 156);
                        player.getMinigameAttributes().getClawQuestAttributes().setQuestParts(3);
                    } else {
                        DialogueManager.start(player, 155);
                    }

                } else if (player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 5) {
                    player.getMinigameAttributes().getClawQuestAttributes().setQuestParts(6);
                    PlayerPanel.refreshPanel(player);
                    DialogueManager.start(player, 167);
                } else if (player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 7) {
                    player.getMinigameAttributes().getClawQuestAttributes().setQuestParts(8);
                    DialogueManager.start(player, 170);
                } else if (player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 9) {
                    if (player.getInventory().contains(691)) {
                        if (player.getInventory().getFreeSlots() > 2) {
                            player.getMinigameAttributes().getClawQuestAttributes().setQuestParts(10);
                            player.getInventory().delete(691, 1);
                            ClawQuest.giveReward(player);
                            // DialogueManager.start(player, 178);
                        } else {
                            player.getPacketSender()
                                    .sendMessage("You need 3 free slots before you can claim your reward.");
                        }
                    } else {
                        DialogueManager.sendStatement(player, "Show me the proof when you've killed the beast.");
                    }
                } else {
                    DialogueManager.sendStatement(player,
                            "The king does not seem interested in talking to you right now.");
                }
                break;
            case 5093:
                Shop.ShopManager.getShops().get(84).open(player);
                break;
            case 2286:
                player.setDialogueActionId(139);
                DialogueManager.start(player, 139);
                break;
            case 4002:
                player.gambler_id = 1;
                if (GameSettings.gambler_1) {
                    player.setDialogueActionId(151);
                    DialogueManager.start(player, 151);
                } else {
                    player.setDialogueActionId(144);
                    DialogueManager.start(player, 144);
                }
                break;
            case 2633:
                player.gambler_id = 2;
                if (GameSettings.gambler_2) {
                    player.setDialogueActionId(151);
                    DialogueManager.start(player, 151);
                } else {
                    player.setDialogueActionId(144);
                    DialogueManager.start(player, 144);
                }
                break;
            case 198:
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
                DialogueManager.start(player, 220);
                player.setDialogueActionId(221);
                break;
            case 653:
                if (player.getGameModeAssistant().isIronMan()) {
                    Shop.ShopManager.getShops().get(82).open(player);
                } else {
                    Shop.ShopManager.getShops().get(27).open(player);
                }
                break;
            case 947:
                if (player.getPosition().getX() >= 3092) {
                    player.getWalkingQueue().clear();
                    GrandExchange.open(player);
                }
                break;
            case 11226:
                if (Dungeoneering.doingDungeoneering(player)) {
                    Shop.ShopManager.getShops().get(45).open(player);
                }
                break;
            case 9713:
                DialogueManager.start(player, 107);
                player.setDialogueActionId(69);
                break;
            case 2622:
                Shop.ShopManager.getShops().get(43).open(player);
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
                if (npc.getId() != player.getSlayer().getSlayerMaster().getNpcId()) {
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
                Shop.ShopManager.getShops().get(38).open(player);
                break;
            case 8591:
                // player.nomadQuest[0] = player.nomadQuest[1] =
                // player.nomadQuest[2] = false;
                if (!player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(0)) {
                    DialogueManager.start(player, 48);
                    player.setDialogueActionId(23);
                } else if (player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(0)
                        && !player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(1)) {
                    DialogueManager.start(player, 50);
                    player.setDialogueActionId(24);
                } else if (player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(1))
                    DialogueManager.start(player, 53);
                break;
            case 3385:
                if (player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0) && player
                        .getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() < 6) {
                    DialogueManager.start(player, 39);
                    return;
                }
                if (player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6) {
                    DialogueManager.start(player, 46);
                    return;
                }
                DialogueManager.start(player, 38);
                player.setDialogueActionId(20);
                break;
            case 6139:
                player.getDialog().sendDialog(new TownCrier(player));
                break;
            case 3789:
                player.getPacketSender().sendInterface(18730);
                player.getPacketSender().sendString(18729,
                        "Commendations: " + Integer.toString(player.getPointsHandler().getCommendations()));
                break;
            case 2948:
                DialogueManager.start(player, WarriorsGuild.warriorsGuildDialogue(player));
                break;
            case 650:
                Shop.ShopManager.getShops().get(35).open(player);
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
                if (player.getSummoning().getFamiliar() == null
                        || player.getSummoning().getFamiliar().getSummonNpc() == null
                        || player.getSummoning().getFamiliar().getSummonNpc().getIndex() != npc.getIndex()) {
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
            case 309:
            case 2724:
            case 3019:
            case 2722:
            case 2859:
                player.setEntityInteraction(npc);
                Fishing.setupFishing(player, Fishing.forSpot(npc.getId(), false));
                break;
            case 2253:
                Shop.ShopManager.getShops().get(9).open(player);
                break;
            case 2733:
                Shop.ShopManager.getShops().get(60).open(player);
                break;
            case 805:
                Shop.ShopManager.getShops().get(34).open(player);
                break;
            case 462:
                Shop.ShopManager.getShops().get(33).open(player);
                break;
            case 461:
                Shop.ShopManager.getShops().get(32).open(player);
                break;
            case 8444:
                if (player.getDonorRights() == 0) {
                    player.getPacketSender().sendMessage("You are not a donator... Get out of here!");
                    player.moveTo(new Position(3087, 3502, 0));
                    return;
                }
                Shop.ShopManager.getShops().get(31).open(player);
                break;
            case 8459:
                Shop.ShopManager.getShops().get(30).open(player);
                break;
            case 3299:
                Shop.ShopManager.getShops().get(21).open(player);
                break;
            case 548:
                Shop.ShopManager.getShops().get(20).open(player);
                break;
            case 1685:
                Shop.ShopManager.getShops().get(19).open(player);
                break;
            case 558:
                Shop.ShopManager.getShops().get(18).open(player);
                break;
            case 802:
                Shop.ShopManager.getShops().get(17).open(player);
                break;
            case 278:
                Shop.ShopManager.getShops().get(16).open(player);
                break;
            case 4946:
                Shop.ShopManager.getShops().get(15).open(player);
                break;
            case 948:
                Shop.ShopManager.getShops().get(13).open(player);
                break;
            case 4906:
                Shop.ShopManager.getShops().get(14).open(player);
                break;
            case 520:
            case 521:
                player.setDialogueActionId(128);
                DialogueManager.start(player, 128);
                break;
            case 2292:
                Shop.ShopManager.getShops().get(11).open(player);
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
        if (!(npc.getId() >= 8705 && npc.getId() <= 8710)) {
            npc.setPositionToFace(player.getPosition());
        }
        player.setPositionToFace(npc.getPosition());
    }

    public void secondClickNpc(NPC npc) {
        if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
                && player.getBankPinAttributes().onDifferent(player)) {
            BankPin.init(player, false);
            return;
        }
        player.setEntityInteraction(npc);
        player.setNpcClickId(npc.getId());
        if (player.getRights() == PlayerRights.DEVELOPER)
            player.getPacketSender().sendConsoleMessage("Second click npc id: " + npc.getId());

        if (GameSettings.DEBUG_MODE) {
            // PlayerLogs.log(player, "" + player.getUsername()
            // + " in NPCOptionPacketListener: " + npc.getId() + " -
            // SECOND_CLICK_OPCODE");
        }
        switch (npc.getId()) {
            case 1:
            case 2:
            case 3:
            case 7:
            case 4:
            case 9:
            case 1714:
            case 1715:
            case 18:
            case 23:
            case 32:
            case 26:
            case 20:
            case 2234:
            case 21:
            case 34:
            case 1307:
            case 1305:
            case 1306:
            case 1311:
            case 1310:
            case 1308:
            case 1314:
                ThievingManager.initMobData(player, ThievingManager.forMobData(npc.getId()));
                break;

            case 6537:
                player.setDialogueActionId(10);
                DialogueManager.start(player, 19);
                break;
            case 6139:
                DialogueManager.start(player, 250);
                player.setDialogueActionId(250);
                break;
            case 590:
                if (player.getDonorRights() >= 3) {
                    Shop.ShopManager.getShops().get(113).open(player);
                } else {
                    player.getPacketSender()
                            .sendMessage("You need to be an Extreme donator or higher, to access this shop!");
                }
                break;
            case 4646:
                if (player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 8) {
                    if (!player.getInventory().isFull()) {
                        player.getInventory().add(993, 1);
                        player.moveTo(new Position(2660, 3306, 0));
                        DialogueManager.start(player, 176);
                    } else {
                        player.getPacketSender().sendMessage("You need 1 slot available for this teleport");
                    }
                } else if (player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 10) {
                    player.moveTo(new Position(2660, 3306, 0));
                } else {
                    player.getPacketSender().sendMessage("The king has no reason to drain his energy on you.");
                }
                break;
            case 198:
                if (player.getLastBoss() == 0) {
                    player.getPacketSender().sendMessage("You have not spawned a boss since your last login!");
                } else if (player.getClanChatName() == null
                        || player.getClanChatName().equalsIgnoreCase("RuneLive") && !player.isBossSolo()) {
                    player.getPacketSender()
                            .sendMessage("Your last boss spawn was in a clan, you currently are not in one!");
                    player.getPacketSender().sendInterfaceRemoval();
                    return;
                } else {
                    BossSystem.startInstance(player, player.getLastBoss(), player.isBossSolo());
                }
                break;
            case 5030:
                if (player.getTeleblockTimer() > 0) {
                    player.getPacketSender()
                            .sendMessage("You cannot sail with Captain Cain while teleport blocked!");
                    return;
                }
                player.moveTo(new Position(2525, 4765, 0));
                DialogueManager.sendStatement(player, "You all of a sudden appear somewhere skulled...");
                if (player.getSkullTimer() > 0) {

                } else {
                    CombatFactory.skullPlayer(player);
                }
                break;
            case 536:
                Shop.ShopManager.getShops().get(59).open(player);
                break;
            case 2579:
                Shop.ShopManager.getShops().get(46).open(player);
                player.getPacketSender().sendMessage("<col=255>You currently have "
                        + player.getPointsHandler().getPrestigePoints() + " Prestige points!");
                break;
            case 457:
                player.getPacketSender().sendMessage("The ghost teleports you away.");
                player.getPacketSender().sendInterfaceRemoval();
                player.moveTo(new Position(3651, 3486));
                break;
            case 6874:
            case 6873:
                if (player.getSummoning().getFamiliar() == null
                        || player.getSummoning().getFamiliar().getSummonNpc() == null
                        || player.getSummoning().getFamiliar().getSummonNpc().getIndex() != npc.getIndex()) {
                    player.getPacketSender().sendMessage("That is not your familiar.");
                    return;
                }
                player.getSummoning().store();
                break;
            case 2622:
                Shop.ShopManager.getShops().get(43).open(player);
                break;
            case 462:
                npc.performAnimation(CombatSpells.CONFUSE.getSpell().castAnimation().get());
                npc.forceChat("Off you go!");
                TeleportHandler.teleportPlayer(player, new Position(2911, 4832),
                        player.getSpellbook().getTeleportType());
                break;
            case 2127:
                if (!GameSettings.POS_ENABLED) {
                    player.getPacketSender().sendMessage("Player owned shops have been disabled.");
                    return;
                }
                if (player.getGameModeAssistant().isIronMan()) {
                    player.getPacketSender().sendMessage("Ironmen can't use the player owned shops!");
                    return;
                }
                PlayerOwnedShops.openItemSearch(player, true);
                player.setPlayerOwnedShopping(true);
                break;
            case 3101:
                DialogueManager.start(player, 95);
                player.setDialogueActionId(57);
                break;
            case 7969:
                Shop.ShopManager.getShops().get(28).open(player);
                break;
            case 2253:
                Shop.ShopManager.getShops().get(10).open(player);
                break;
            case 605:
                player.getPacketSender().sendMessage("")
                        .sendMessage("You currently have " + player.getPointsHandler().getVotingPoints()
                                + " Voting points.")
                        .sendMessage(
                                "You can earn points and coins by voting. To do so, simply use the ::vote command.");
                Shop.ShopManager.getShops().get(27).open(player);
                break;
            case 4657:

                break;
            case 1597:
            case 9085:
            case 7780:
                if (npc.getId() != player.getSlayer().getSlayerMaster().getNpcId()) {
                    player.getPacketSender().sendMessage("This is not your current Slayer master.");
                    return;
                }
                if (player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK)
                    player.getSlayer().assignTask();
                else
                    DialogueManager.start(player, SlayerDialogues.findAssignment(player));
                break;
            case 8591:
                if (!player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(1)) {
                    player.getPacketSender()
                            .sendMessage("You must complete Nomad's quest before being able to use this shop.");
                    return;
                }
                Shop.ShopManager.getShops().get(37).open(player);
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
                Shop.ShopManager.getShops().get(15).open(player);
                break;
            case 946:
                Shop.ShopManager.getShops().get(1).open(player);
                break;
            case 961:
                Shop.ShopManager.getShops().get(6).open(player);
                break;
            case 1861:
                Shop.ShopManager.getShops().get(3).open(player);
                break;
            case 705:
                Shop.ShopManager.getShops().get(4).open(player);
                break;
            case 6970:
                player.setDialogueActionId(35);
                DialogueManager.start(player, 63);
                break;
        }
        npc.setPositionToFace(player.getPosition());
        player.setPositionToFace(npc.getPosition());
    }

    public void thirdClickNpc(NPC npc) {
        if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
                && player.getBankPinAttributes().onDifferent(player)) {
            BankPin.init(player, false);
            return;
        }
        player.setEntityInteraction(npc).setPositionToFace(npc.getPosition().copy());
        npc.setPositionToFace(player.getPosition());
        player.setNpcClickId(npc.getId());
        if (player.getRights() == PlayerRights.DEVELOPER)
            player.getPacketSender().sendMessage("Third click npc id: " + npc.getId());

        if (GameSettings.DEBUG_MODE) {
            // PlayerLogs.log(player, "" + player.getUsername()
            // + " in NPCOptionPacketListener: " + npc.getId() + " -
            // THIRD_CLICK_OPCODE");
        }
        switch (npc.getId()) {
            case 6537:
                Artifacts.sellArtifacts(player);
                break;
            case 3101:
                Shop.ShopManager.getShops().get(42).open(player);
                break;
            case 1597:
            case 8275:
            case 9085:
            case 7780:
                Shop.ShopManager.getShops().get(40).open(player);
                break;
            case 2127:
                if (!GameSettings.POS_ENABLED) {
                    player.getPacketSender().sendMessage("Player owned shops have been disabled.");
                    return;
                }
                if (player.getGameModeAssistant().isIronMan()) {
                    player.getPacketSender().sendMessage("Ironmen can't use the player owned shops!");
                    return;
                }
                player.getPacketSender().sendString(41900, "");
                PlayerOwnedShops.openShop(player.getUsername(), player);
                player.setPlayerOwnedShopping(true);
                break;
            case 605:
                // LoyaltyProgramme.open(player);
                break;
            case 4657:
                DialogueManager.start(player, MemberScrolls.getTotalFunds(player));
                break;
            case 946:
                Shop.ShopManager.getShops().get(0).open(player);
                break;
            case 1861:
                Shop.ShopManager.getShops().get(2).open(player);
                break;
            case 2253:
                Shop.ShopManager.getShops().get(83).open(player);
                break;
            case 961:
                if (player.getDonorRights() == 0) {
                    player.getPacketSender().sendMessage("This feature is currently only available for members.");
                    return;
                }
                boolean restore = player.getSpecialPercentage() < 100;
                // if(restore) {
                // player.setSpecialPercentage(100);
                // CombatSpecial.updateBar(player);
                // player.getPacketSender().sendMessage("Your special attack
                // energy has been
                // restored.");
                // }
                for (Skill skill : Skill.values()) {
                    if (player.getSkillManager().getCurrentLevel(skill) < player.getSkillManager()
                            .getMaxLevel(skill)) {
                        player.getSkillManager().setCurrentLevel(skill,
                                player.getSkillManager().getMaxLevel(skill));
                        restore = true;
                    }
                }
                if (restore) {
                    player.performGraphic(new Graphic(1302));
                    player.getPacketSender().sendMessage("Your stats have been restored.");
                } else
                    player.getPacketSender().sendMessage("Your stats do not need to be restored at the moment.");
                break;
            case 705:
                Shop.ShopManager.getShops().get(5).open(player);
                break;
        }
        npc.setPositionToFace(player.getPosition());
        player.setPositionToFace(npc.getPosition());
    }

    public void fourthClickNpc(NPC npc) {
        if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
                && player.getBankPinAttributes().onDifferent(player)) {
            BankPin.init(player, false);
            return;
        }
        player.setEntityInteraction(npc);
        player.setNpcClickId(npc.getId());
        if (player.getRights() == PlayerRights.DEVELOPER)
            player.getPacketSender().sendMessage("Fourth click npc id: " + npc.getId());

        if (GameSettings.DEBUG_MODE) {
            // PlayerLogs.log(player, "" + player.getUsername()
            // + " in NPCOptionPacketListener: " + npc.getId() + " -
            // FOURTH_CLICK_OPCODE");
        }
        switch (npc.getId()) {
            case 946:
                if (player.getGameModeAssistant().getGameMode() == GameMode.IRONMAN || player.getGameModeAssistant().getGameMode() == GameMode._IRONMAN || player.getGameModeAssistant().getGameMode() == GameMode.HARDCORE_IRONMAN) {
                    player.getPacketSender().sendMessage("You're an ironman you can't do this");
                } else {
                    DialogueManager.start(player, 252);
                    player.setDialogueActionId(252);
                }
                break;
            case 2217:
                player.getPacketSender().sendEnterInputPrompt("Enter the name of a player's shop:");
                break;
            case 4657:
                if (player.getDonorRights() == 0) {
                    player.getPacketSender().sendMessage("You need to be a member to teleport to this zone.")
                            .sendMessage("To become a member, visit rune.live and purchase a scroll.");
                    return;
                }
                TeleportHandler.teleportPlayer(player, new Position(3424, 2919),
                        player.getSpellbook().getTeleportType());
                break;
            case 705:
                Shop.ShopManager.getShops().get(7).open(player);
                break;
            case 2253:
                Shop.ShopManager.getShops().get(9).open(player);
                break;
            case 2127:
                if (!GameSettings.POS_ENABLED) {
                    player.getPacketSender().sendMessage("Player owned shops have been disabled.");
                    return;
                }
                player.getPacketSender().sendEnterInputPrompt("Enter the name of a player's shop:");
                player.setInputHandling(new PosSearchShop());
                player.setPlayerOwnedShopping(true);
                break;
            case 1597:
            case 9085:
            case 8275:
            case 7780:
                player.setDialogueActionId(188);
                DialogueManager.start(player, 188);
                break;
        }
        npc.setPositionToFace(player.getPosition());
        player.setPositionToFace(npc.getPosition());
    }

    public void firstClickObject(GameObject gameObject) {
        int id = gameObject.getId();
        int x = gameObject.getPosition().getX();
        int y = gameObject.getPosition().getY();
        if (GameSettings.DEBUG_MODE) {
            // PlayerLogs.log(player, "" + player.getUsername()
            // + " in ObjectActionPacketListener: " + id + " -
            // FIRST_CLICK_OPCODE");
        }
        if (player.getRights() == PlayerRights.DEVELOPER)
            player.getPacketSender().sendConsoleMessage(
                    "First click object id; [id, position] : [" + id + ", " + gameObject.getPosition().toString() + "]");

        if (!player.getDragonSpear().elapsed(3000)) {
            player.getPacketSender().sendMessage("You are stunned!");
            return;
        }
        player.setPositionToFace(gameObject.getPosition());
        if (WoodcuttingData.Trees.forId(id) != null) {
            Woodcutting.cutWood(player, gameObject, false);
            return;
        }
        if (Construction.handleFirstObjectClick(x, y, id, player))
            return;
        if (MiningData.forRock(gameObject.getId()) != null) {
            Mining.startMining(player, gameObject);
            return;
        }
        if (player.getFarming().click(player, x, y, 1))
            return;
        if (Runecrafting.runecraftingAltar(player, gameObject.getId())) {
            RunecraftingData.RuneData rune = RunecraftingData.RuneData.forId(gameObject.getId());
            if (rune == null)
                return;
            Runecrafting.craftRunes(player, rune);
            return;
        }
        if (Agility.handleObject(player, gameObject)) {
            return;
        }
        if (Barrows.handleObject(player, gameObject)) {
            return;
        }
        if (ChaosTunnelHandler.handleObjects(player, gameObject)) {
            return;
        }
        if (player.getLocation() == Locations.Location.WILDERNESS
                && WildernessObelisks.handleObelisk(gameObject.getId())) {
            return;
        }
        if (!player.getDragonSpear().elapsed(3000)) {
            player.getPacketSender().sendMessage("You are stunned!");
            return;
        }
        switch (id) {
            case 2406:
                if (!player.getClickDelay().elapsed(3000))
                    return;
                player.getPacketSender().sendMessage(
                        "kicks: " + player.getMinigameAttributes().getShrek1Attributes().getDoorKicks());
                if (player.getMinigameAttributes().getShrek1Attributes().getQuestParts() == 1) {
                    if (player.getMinigameAttributes().getShrek1Attributes().getDoorKicks() == 0) {
                        player.setDirection(Direction.EAST);
                        player.performAnimation(new Animation(2555));
                        DialogueManager.start(player, 239);
                        player.getPacketSender()
                                .sendMessage("You hear a very intimidating voice from inside yelling.");
                        player.getMinigameAttributes().getShrek1Attributes().setDoorKicks(1);
                    } else if (player.getMinigameAttributes().getShrek1Attributes().getDoorKicks() == 1) {
                        player.setDirection(Direction.EAST);
                        player.performAnimation(new Animation(2555));
                        // spawn shrek
                        DialogueManager.sendStatement(player, "You upset the ogre and he came out!");
                        TaskManager.submit(new Task(2, player, false) {
                            @Override
                            public void execute() {
                                NPC n = new NPC(5872, new Position(3201, 3169, player.getPosition().getZ()))
                                        .setSpawnedFor(player);
                                World.register(n);
                                n.getCombatBuilder().attack(player);
                                stop();
                            }
                        });
                        // player.getMinigameAttributes().getShrek1Attributes().setDoorKicks(2);
                    }
                } else {
                    player.getPacketSender().sendMessage("Nothing interesting happens.");
                }
                // ok so player kicks door once - shrek says leave
                // dont come back or else...
                // if player kicks door again he spawns - they fight
                // - 100% drop ogres head
                player.getClickDelay().reset();
                break;
            case 9299:
                if (player.getSkillManager().getMaxLevel(Skill.AGILITY) < 50) {
                    player.getPacketSender()
                            .sendMessage("You need an agility of 50 or higher to use this shortcut.");
                    return;
                }
                if (player.getPosition().getY() > 3190) {
                    player.setDirection(Direction.SOUTH);
                    player.performAnimation(new Animation(2240));
                    TaskManager.submit(new Task(1, player, true) {
                        int tick = 1;

                        @Override
                        public void execute() {
                            tick++;
                            if (tick == 4) {
                                stop();
                            }
                        }

                        @Override
                        public void stop() {
                            setEventRunning(false);
                            player.moveTo(new Position(3240, 3190, 0));
                            player.getPacketSender().sendMessage("You squeeze through the fence.");
                        }
                    });
                } else {
                    player.setDirection(Direction.NORTH);
                    player.performAnimation(new Animation(2240));
                    TaskManager.submit(new Task(1, player, true) {
                        int tick = 1;

                        @Override
                        public void execute() {
                            tick++;
                            if (tick == 4) {
                                stop();
                            }
                        }

                        @Override
                        public void stop() {
                            setEventRunning(false);
                            player.moveTo(new Position(3240, 3191, 0));
                            player.getPacketSender().sendMessage("You squeeze through the fence.");
                        }
                    });
                }
                break;
            case 21772:
                player.moveTo(new Position(3236, 3458, 0));
                break;
            case 881:
                // player.moveTo(new Position(1240, 1226, 0));
                // InstancedCerberus.enterDungeon(player);
                InstancedCerberus.enterDungeon(player);
                break;
            case 2995:
                player.openBank();
                break;
            case 10309:
                player.performAnimation(new Animation(828));
                TaskManager.submit(new Task(1, player, true) {
                    int tick = 1;

                    @Override
                    public void execute() {
                        tick++;
                        if (tick == 4) {
                            stop();
                        }
                    }

                    @Override
                    public void stop() {
                        setEventRunning(false);
                        player.moveTo(new Position(2658, 3492, 0));
                    }
                });
                break;
            case 1754:
                player.performAnimation(new Animation(827));
                TaskManager.submit(new Task(1, player, true) {
                    int tick = 1;

                    @Override
                    public void execute() {
                        tick++;
                        if (tick == 4) {
                            stop();
                        }
                    }

                    @Override
                    public void stop() {
                        setEventRunning(false);
                        player.moveTo(new Position(2962, 9650, 0));
                    }
                });
                break;
            case 11339:
                TreasureIslandChest.openChest(player);
                break;
            case 1734:
                if (player.getPosition().getX() == 3045 && player.getPosition().getY() == 10323) {
                    player.moveTo(new Position(3045, 3927, 0));
                } else if (player.getPosition().getX() == 3044 && player.getPosition().getY() == 10323) {
                    player.moveTo(new Position(3044, 3927, 0));
                } else {
                    player.getPacketSender().sendMessage("I can't climb them from here.");

                }
                break;
            case 1733:
                if (player.getPosition().getX() == 3045 || player.getPosition().getY() == 3927) {
                    player.moveTo(new Position(3045, 10323, 0));
                } else if (player.getPosition().getX() == 3044 || player.getPosition().getY() == 3927) {
                    player.moveTo(new Position(3044, 10323, 0));
                } else {
                    player.getPacketSender().sendMessage("I can't climb them from here.");
                }
                break;
            case 51:
                player.getPacketSender().sendMessage("There is no way I could squeeze through that...");
                break;
            case 52:
            case 53:
                if (player.getPosition().getY() < 3470) {
                    player.moveTo(
                            new Position(player.getPosition().getX(), player.getPosition().getY() + 1));
                } else if (player.getPosition().getY() > 3469) {
                    player.moveTo(
                            new Position(player.getPosition().getX(), player.getPosition().getY() - 1));
                }
                break;
            case 99:
                if (player.getInventory().contains(1843)) {
                    player.moveTo(
                            new Position(player.getPosition().getX(), player.getPosition().getY() - 1, 0));
                } else {
                    player.getPacketSender()
                            .sendMessage("The door is locked. I must need some sort of key to get in.");
                }
                break;
            case 2932:
                player.moveTo(new Position(2600, 3157, 0));
                player.performAnimation(new Animation(2306));
                TaskManager.submit(new Task(1, player, true) {
                    int tick = 1;

                    @Override
                    public void execute() {
                        tick++;
                        if (tick == 6) {
                        } else if (tick >= 10) {
                            stop();
                        }
                    }

                    @Override
                    public void stop() {
                        setEventRunning(false);
                        player.moveTo(new Position(player.getPosition().getX() - 1,
                                player.getPosition().getY(), player.getPosition().getZ()));
                        player.getPacketSender().sendMessage(
                                "You smash open the barrel by jumping on it, a lion appeared!");
                        NPC n = new NPC(1172, new Position(player.getPosition().getX(),
                                player.getPosition().getY() + 2, player.getPosition().getZ()))
                                .setSpawnedFor(player);
                        World.register(n);
                    }
                });
                break;
            case 21764:
                if (!player.getSpecTimer().elapsed(120000)) {
                    player.getPacketSender()
                            .sendMessage("You can only restore your special attack every 2 minutes.");
                    return;
                }
                player.performAnimation(new Animation(1327));
                player.setSpecialPercentage(100);
                CombatSpecial.updateBar(player);
                int max = player.getSkillManager().getMaxLevel(Skill.CONSTITUTION);
                player.setConstitution(max);
                player.getSkillManager().setCurrentLevel(Skill.PRAYER,
                        player.getSkillManager().getMaxLevel(Skill.PRAYER));
                player.setPoisonDamage(0);
                player.setVenomDamage(0);
                player.getPacketSender().sendConstitutionOrbPoison(false);
                player.getPacketSender().sendConstitutionOrbVenom(false);
                player.getPacketSender().sendMessage(
                        "<col=00ff00><shad=0><img=9> You take a drink from the fountain... and feel revived!");
                player.getSpecTimer().reset();
                break;
            case 81:
                if (player.getPosition().getX() == 2584) {
                    if (player.getInventory().contains(993)) {
                        player.performAnimation(new Animation(1820));
                        TaskManager.submit(new Task(1, player, true) {
                            int tick = 1;

                            @Override
                            public void execute() {
                                tick++;
                                if (tick == 2) {
                                } else if (tick >= 5) {
                                    player.moveTo(new Position(player.getPosition().getX() + 1,
                                            player.getPosition().getY(), 0));
                                    stop();
                                }
                            }

                            @Override
                            public void stop() {
                                setEventRunning(false);
                                player.getPacketSender()
                                        .sendMessage("You use the key to get through the door.");
                            }
                        });
                    }
                }
                break;
            case 82:
                if (player.getPosition().getX() == 2606 || player.getPosition().getX() == 2607) {
                    if (player.getInventory().contains(993)) {
                        player.performAnimation(new Animation(1820));
                        TaskManager.submit(new Task(1, player, true) {
                            int tick = 1;

                            @Override
                            public void execute() {
                                tick++;
                                if (tick == 2) {
                                } else if (tick >= 5) {
                                    player.moveTo(new Position(player.getPosition().getX(),
                                            player.getPosition().getY() + 2, 0));
                                    stop();
                                }
                            }

                            @Override
                            public void stop() {
                                setEventRunning(false);
                                player.getPacketSender()
                                        .sendMessage("You use the key to get through the door.");
                            }
                        });
                    }
                }
                break;
            case 4754:
            case 4749:
                if (player.getMinigameAttributes().getClawQuestAttributes().getSamples() <= 50) {
                    if (player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() >= 6) {
                        player.performAnimation(new Animation(2290));
                        TaskManager.submit(new Task(1, player, true) {
                            int tick = 1;

                            @Override
                            public void execute() {
                                tick++;
                                if (tick == 2) {
                                    player.getMinigameAttributes().getClawQuestAttributes().addSamples(1);
                                } else if (tick >= 6) {
                                    stop();
                                }
                            }

                            @Override
                            public void stop() {
                                setEventRunning(false);
                                player.getPacketSender()
                                        .sendMessage("You have collected "
                                                + player.getMinigameAttributes().getClawQuestAttributes()
                                                .getSamples()
                                                + " of "
                                                + player.getMinigameAttributes()
                                                .getClawQuestAttributes().SAMPLES_NEEDED
                                                + " samples needed.");
                            }
                        });
                    } else {
                        player.getPacketSender().sendMessage("Nothing interesting happened.");
                    }
                }
                break;
            case 12987:
                player.getPacketSender()
                        .sendMessage("There is another way out of this stable. The gate is broken!");
                break;
            case 12982:
                if (player.getPosition().getY() == 3275) {
                    TaskManager.submit(new Task(1, player, true) {
                        int tick = 1;

                        @Override
                        public void execute() {
                            tick++;
                            player.performAnimation(new Animation(828));
                            if (tick == 3) {
                                player.moveTo(new Position(player.getPosition().getX(),
                                        player.getPosition().getY() + 2));
                            } else if (tick >= 4) {
                                stop();
                            }
                        }

                        @Override
                        public void stop() {
                            setEventRunning(false);
                            player.getPacketSender().sendMessage("You jump over the stile.");
                        }
                    });
                } else {
                    player.getPacketSender().sendMessage("You failed to climb over, please try again.");
                }
                break;
            case 3565:
                if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) >= 50) {

                    if (player.getLocation() == Locations.Location.BORK && player.getPosition().getY() < 2973) {
                        TaskManager.submit(new Task(1, player, true) {
                            int tick = 1;

                            @Override
                            public void execute() {
                                tick++;
                                player.performAnimation(new Animation(769));
                                if (tick == 3) {
                                    player.moveTo(new Position(player.getPosition().getX(), player.getPosition().getY() + 3));
                                } else if (tick >= 4) {
                                    stop();
                                }
                            }

                            @Override
                            public void stop() {
                                setEventRunning(false);
                                Agility.addExperience(player, 100);
                                player.getPacketSender().sendMessage("You jump over the wall.");
                            }
                        });
                    } else if (player.getLocation() == Locations.Location.BORK && player.getPosition().getY() >= 2973) {
                        TaskManager.submit(new Task(1, player, true) {
                            int tick = 1;

                            @Override
                            public void execute() {
                                tick++;
                                player.performAnimation(new Animation(769));
                                if (tick == 3) {
                                    player.moveTo(new Position(player.getPosition().getX(), player.getPosition().getY() - 3));
                                } else if (tick >= 4) {
                                    stop();
                                }
                            }

                            @Override
                            public void stop() {
                                setEventRunning(false);
                                Agility.addExperience(player, 100);
                                player.getPacketSender().sendMessage("You jump over the wall.");
                            }
                        });
                    } else if (player.getPosition().getX() <= 3349) {
                        TaskManager.submit(new Task(1, player, true) {
                            int tick = 1;

                            @Override
                            public void execute() {
                                tick++;
                                player.performAnimation(new Animation(769));
                                if (tick == 3) {
                                    player.moveTo(new Position(3352, player.getPosition().getY()));
                                } else if (tick >= 4) {
                                    stop();
                                }
                            }

                            @Override
                            public void stop() {
                                setEventRunning(false);
                                Agility.addExperience(player, 100);
                                player.getPacketSender().sendMessage("You jump over the wall.");
                            }
                        });
                    } else if (player.getPosition().getX() > 3350) {
                        DialogueManager.start(player, 142);
                        player.setDialogueActionId(142);
                    } else {
                        player.getPacketSender().sendMessage(
                                "You need an Agility level of at least 50 to get past this obstacle.");
                        player.getPacketSender().sendMessage("or an wilderness key!");
                    }
                } else {
                    player.getPacketSender().sendMessage("You need 50 agility in order to cross this...");
                }
                break;
            case 38660:
                if (ShootingStar.CRASHED_STAR != null) {

                }
                break;
            case 5259:
                if (player.getTeleblockTimer() > 0) {
                    player.getPacketSender().sendMessage("You are teleblocked");
                    return;
                }
                if (player.getPosition().getX() >= 3653) { // :)
                    player.moveTo(new Position(3652, player.getPosition().getY()));
                } else {
                    if (player.getRevsWarning()) {
                        player.setDialogueActionId(73);
                        DialogueManager.start(player, 115);
                    } else {
                        player.getPacketSender().sendInterfaceRemoval();
                        player.moveTo(new Position(3653, player.getPosition().getY()));
                    }
                }
                break;
            case 10805:
            case 10806:
            case 28089:
                // GrandExchange.open(player);
                break;
            case 38700:
                player.moveTo(new Position(3085, 3512));
                break;
            case 1765:// ladder down to posion spider KBD
                player.performAnimation(new Animation(827));
                TaskManager.submit(new Task(1, player, true) {
                    int tick = 1;

                    @Override
                    public void execute() {
                        tick++;
                        if (tick == 4) {
                            stop();
                        }
                    }

                    @Override
                    public void stop() {
                        setEventRunning(false);
                        player.moveTo(new Position(3055, 10271, 0));
                    }
                });
                break;
            case 2795: // lever KBD
                if (player.getTeleblockTimer() > 0) {
                    player.getPacketSender()
                            .sendMessage("A magical spell is blocking you from teleporting.");
                    return;
                }
                if (player.getPosition().getY() >= 10252) {
                    TaskManager.submit(new Task(1, player, true) {
                        int tick = 1;

                        @Override
                        public void execute() {
                            tick++;
                            player.performAnimation(new Animation(2140));
                            if (tick >= 2) {
                                stop();
                            }
                        }

                        @Override
                        public void stop() {
                            setEventRunning(false);
                            TeleportHandler.teleportPlayer(player, new Position(2273, 4681, 0),
                                    TeleportType.LEVER);
                        }
                    });
                } else {
                    TaskManager.submit(new Task(1, player, true) {
                        int tick = 1;

                        @Override
                        public void execute() {
                            tick++;
                            player.performAnimation(new Animation(2140));
                            if (tick >= 2) {
                                stop();
                            }
                        }

                        @Override
                        public void stop() {
                            setEventRunning(false);
                            TeleportHandler.teleportPlayer(player, new Position(3066, 10254),
                                    TeleportType.LEVER);
                        }
                    });
                }
                break;
            case 398:
                DialogueManager.start(player, 227);
                player.setDialogueActionId(227);
                break;
            case 1766:// poison spider ladder KBD
                if (player.getTeleblockTimer() > 0) {
                    player.getPacketSender()
                            .sendMessage("A magical spell is blocking you from teleporting.");
                    return;
                }
                player.performAnimation(new Animation(828));
                TaskManager.submit(new Task(1, player, true) {
                    int tick = 1;

                    @Override
                    public void execute() {
                        tick++;
                        if (tick == 4) {
                            stop();
                        }
                    }

                    @Override
                    public void stop() {
                        setEventRunning(false);
                        player.moveTo(new Position(3017, 3850, 0));
                    }
                });
                break;
            case 9312: // Grand Exchange Underwall Tunnel
                Position position = new Position(3164, 3484, 0);
                break;
            case 2465:
                if (player.getLocation() == Locations.Location.EDGEVILLE) {
                    player.getPacketSender().sendMessage(
                            "<img=4> @blu@Welcome to the free-for-all arena! You will not lose any items on death here.");
                    player.moveTo(new Position(2815, 5511));
                } else {
                    player.getPacketSender()
                            .sendMessage("The portal does not seem to be functioning properly.");
                }
                break;
            case 45803:
            case 1767:
                DialogueManager.start(player, 114);
                player.setDialogueActionId(72);
                break;
            case 7352:
                if (Dungeoneering.doingDungeoneering(player) && player.getMinigameAttributes()
                        .getDungeoneeringAttributes().getParty().getGatestonePosition() != null) {
                    player.moveTo(player.getMinigameAttributes().getDungeoneeringAttributes().getParty()
                            .getGatestonePosition());
                    player.setEntityInteraction(null);
                    player.getPacketSender().sendMessage("You are teleported to your party's gatestone.");
                    player.performGraphic(new Graphic(1310));
                } else
                    player.getPacketSender().sendMessage(
                            "Your party must drop a Gatestone somewhere in the dungeon to use this portal.");
                break;
            case 7353:
                player.moveTo(new Position(2439, 4956, player.getPosition().getZ()));
                break;
            case 7321:
                player.moveTo(new Position(2452, 4944, player.getPosition().getZ()));
                break;
            case 7322:
                player.moveTo(new Position(2455, 4964, player.getPosition().getZ()));
                break;
            case 7315:
                player.moveTo(new Position(2447, 4956, player.getPosition().getZ()));
                break;
            case 7316:
                player.moveTo(new Position(2471, 4956, player.getPosition().getZ()));
                break;
            case 7318:
                player.moveTo(new Position(2464, 4963, player.getPosition().getZ()));
                break;
            // case 7319:
            // player.moveTo(new Position(2467, 4940,
            // player.getPosition().getZ()));
            // break;
            case 7324:
                player.moveTo(new Position(2481, 4956, player.getPosition().getZ()));
                break;

            case 7319:
                if (gameObject.getPosition().getX() == 2481 && gameObject.getPosition().getY() == 4956)
                    player.moveTo(new Position(2467, 4940, player.getPosition().getZ()));
                break;

            case 11356:
                if (player.getDonorRights() == 0) {
                    player.getPacketSender().sendMessage("You are not a donator... Get out of here!");
                    player.moveTo(new Position(3087, 3502, 0));
                    return;
                }
                player.moveTo(new Position(2860, 9741));
                player.getPacketSender().sendMessage("You step through the portal..");
                break;
            case 47180:
                if (player.getDonorRights() >= 3) {
                    player.getPacketSender().sendMessage("You activate the device..");
                    player.moveTo(new Position(2793, 3794));
                } else {
                    player.getPacketSender().sendMessage("You need to be an Extreme Donator to use this.");
                }
                break;
            case 10091:
            case 8702:
                if (gameObject.getId() == 8702) {
                    if (player.getDonorRights() < 2) {
                        player.getPacketSender()
                                .sendMessage("You must be at least a Super Donator to use this.");
                        return;
                    }
                }
                Fishing.setupFishing(player, Fishing.Spot.ROCKTAIL);
                break;
            case 9319:
                if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 61) {
                    player.getPacketSender().sendMessage(
                            "You need an Agility level of at least 61 or higher to climb this");
                    return;
                }
                if (player.getPosition().getZ() == 0)
                    player.moveTo(new Position(3422, 3549, 1));
                else if (player.getPosition().getZ() == 1) {
                    if (gameObject.getPosition().getX() == 3447)
                        player.moveTo(new Position(3447, 3575, 2));
                    else
                        player.moveTo(new Position(3447, 3575, 0));
                }
                break;

            case 9320:
                if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 61) {
                    player.getPacketSender().sendMessage(
                            "You need an Agility level of at least 61 or higher to climb this");
                    return;
                }
                if (player.getPosition().getZ() == 1)
                    player.moveTo(new Position(3422, 3549, 0));
                else if (player.getPosition().getZ() == 0)
                    player.moveTo(new Position(3447, 3575, 1));
                else if (player.getPosition().getZ() == 2)
                    player.moveTo(new Position(3447, 3575, 1));
                player.performAnimation(new Animation(828));
                break;
            case 2274:
                if (player.getTeleblockTimer() > 0) {
                    player.getPacketSender().sendMessage("You are teleblocked, don't die, noob.");
                    return;
                }
                if (gameObject.getPosition().getX() == 2912 && gameObject.getPosition().getY() == 5300) {
                    player.moveTo(new Position(2914, 5300, 1));
                } else if (gameObject.getPosition().getX() == 2914
                        && gameObject.getPosition().getY() == 5300) {
                    player.moveTo(new Position(2912, 5300, 2));
                } else if (gameObject.getPosition().getX() == 2919
                        && gameObject.getPosition().getY() == 5276) {
                    player.moveTo(new Position(2918, 5274));
                } else if (gameObject.getPosition().getX() == 2918
                        && gameObject.getPosition().getY() == 5274) {
                    player.moveTo(new Position(2919, 5276, 1));
                } else if (gameObject.getPosition().getX() == 3001
                        && gameObject.getPosition().getY() == 3931
                        || gameObject.getPosition().getX() == 3652
                        && gameObject.getPosition().getY() == 3488) {
                    player.moveTo(GameSettings.DEFAULT_POSITION_EDGEVILLE.copy());
                    player.getPacketSender().sendMessage("The portal teleports you to Edgeville.");
                }
                break;
            case 7836:
            case 7837:
            case 7808:
                int amt = player.getInventory().getAmount(6055);
                if (amt > 0) {
                    player.getInventory().delete(6055, amt);
                    player.getPacketSender().sendMessage("You put the weed in the compost bin.");
                    player.getSkillManager().addSkillExperience(Skill.FARMING, 20 * amt);
                    if (player.getMinigameAttributes().getFarmQuestAttributes().getQuestParts() == 2) {
                        player.getMinigameAttributes().getFarmQuestAttributes().addProduce(amt);
                        player.getPacketSender()
                                .sendMessage("You now have added " + player.getMinigameAttributes()
                                        .getFarmQuestAttributes().getProduce()
                                        + "/100 weeds to the compost bin.");
                        if (player.getMinigameAttributes().getFarmQuestAttributes().getProduce() > 99) {
                            player.getMinigameAttributes().getFarmQuestAttributes().setQuestParts(3);
                            DialogueManager.sendStatement(player,
                                    "You have put 100 weeds in the bin. Vanessa should be informed.");
                        }
                    }
                } else {
                    player.getPacketSender().sendMessage("You do not have any weeds in your inventory.");
                }
                break;
            case 9706:
                if (player.getTeleblockTimer() > 0) {
                    player.getPacketSender()
                            .sendMessage("A magical spell is blocking you from teleporting.");
                    return;
                }
                if (gameObject.getPosition().getX() == 3104 && gameObject.getPosition().getY() == 3956) {
                    player.setDirection(Direction.WEST);
                    TeleportHandler.teleportPlayer(player, new Position(3105, 3951), TeleportType.LEVER);
                }
                break;
            case 9707:
                if (player.getTeleblockTimer() > 0) {
                    player.getPacketSender()
                            .sendMessage("A magical spell is blocking you from teleporting.");
                    return;
                }
                if (gameObject.getPosition().getX() == 3105 && gameObject.getPosition().getY() == 3952) {
                    player.setDirection(Direction.NORTH);
                    TeleportHandler.teleportPlayer(player, new Position(3105, 3956), TeleportType.LEVER);
                }
                break;
            case 5960: // Levers
                if (player.getTeleblockTimer() > 0) {
                    player.getPacketSender()
                            .sendMessage("A magical spell is blocking you from teleporting.");
                    return;
                }
                if (gameObject.getPosition().getX() == 2539 && gameObject.getPosition().getY() == 4712) {
                    player.setDirection(Direction.SOUTH);
                    TeleportHandler.teleportPlayer(player, new Position(3090, 3956), TeleportType.LEVER);
                } else if (gameObject.getPosition().getX() == 3067
                        && gameObject.getPosition().getY() == 10253) {
                    TaskManager.submit(new Task(1, player, true) {
                        int tick = 1;

                        @Override
                        public void execute() {
                            tick++;
                            player.performAnimation(new Animation(2140));
                            if (tick >= 2) {
                                stop();
                            }
                        }

                        @Override
                        public void stop() {
                            setEventRunning(false);
                            TeleportHandler.teleportPlayer(player, new Position(2272, 4680, 0),
                                    TeleportType.LEVER);
                        }
                    });
                } else if (gameObject.getPosition().getX() == 2272
                        && gameObject.getPosition().getY() == 4680) {
                    TaskManager.submit(new Task(1, player, true) {
                        int tick = 1;

                        @Override
                        public void execute() {
                            tick++;
                            player.performAnimation(new Animation(2140));
                            if (tick >= 2) {
                                stop();
                            }
                        }

                        @Override
                        public void stop() {
                            setEventRunning(false);
                            TeleportHandler.teleportPlayer(player, new Position(3067, 10253),
                                    TeleportType.LEVER);
                        }
                    });
                }
                break;
            case 5959:
                if (player.getTeleblockTimer() > 0) {
                    player.getPacketSender().sendMessage("A magical spell is blocking you from teleporting.");
                    return;
                }
                if (gameObject.getPosition().getX() == 3090 && gameObject.getPosition().getY() == 3956) {
                    player.setDirection(Direction.WEST);
                    TeleportHandler.teleportPlayer(player, new Position(2539, 4712), TeleportType.LEVER);
                } else if (player.getPosition().getX() == 3090 && player.getPosition().getY() >= 3957) {
                    player.setDirection(Direction.SOUTH);
                    TeleportHandler.teleportPlayer(player, new Position(2539, 4712), TeleportType.LEVER);
                } else if (player.getPosition().getX() == 3090 && player.getPosition().getY() <= 3955) {
                    player.setDirection(Direction.NORTH);
                    TeleportHandler.teleportPlayer(player, new Position(2539, 4712), TeleportType.LEVER);
                } else if (player.getPosition().getX() == 3153 && player.getPosition().getY() <= 3923) {
                    player.setDirection(Direction.WEST);
                    TeleportHandler.teleportPlayer(player, new Position(2561, 3311), TeleportType.LEVER);
                } else if (player.getPosition().getX() == 2561 && player.getPosition().getY() <= 3311) {
                    player.setDirection(Direction.WEST);
                    TeleportHandler.teleportPlayer(player, new Position(3153, 3923), TeleportType.LEVER);
                }
                break;
            case 5096:
                if (gameObject.getPosition().getX() == 2644 && gameObject.getPosition().getY() == 9593)
                    player.moveTo(new Position(2649, 9591));
                break;

            case 5094:
                if (gameObject.getPosition().getX() == 2648 && gameObject.getPosition().getY() == 9592)
                    player.moveTo(new Position(2643, 9594, 2));
                break;

            case 5098:
                if (gameObject.getPosition().getX() == 2635 && gameObject.getPosition().getY() == 9511)
                    player.moveTo(new Position(2637, 9517));
                break;

            case 5097:
                if (gameObject.getPosition().getX() == 2635 && gameObject.getPosition().getY() == 9514)
                    player.moveTo(new Position(2636, 9510, 2));
                break;
            case 26428:
            case 26426:
            case 26425:
            case 26427:
                String bossRoom = "Armadyl";
                boolean leaveRoom = player.getPosition().getY() > 5295;
                int index = 0;
                Position movePos = new Position(2839, !leaveRoom ? 5296 : 5295, 2);
                if (id == 26425) {
                    bossRoom = "Bandos";
                    leaveRoom = player.getPosition().getX() > 2863;
                    index = 1;
                    movePos = new Position(!leaveRoom ? 2864 : 2863, 5354, 2);
                } else if (id == 26427) {
                    bossRoom = "Saradomin";
                    leaveRoom = player.getPosition().getX() < 2908;
                    index = 2;
                    movePos = new Position(leaveRoom ? 2908 : 2907, 5265, 0);
                } else if (id == 26428) {
                    bossRoom = "Zamorak";
                    leaveRoom = player.getPosition().getY() <= 5331;
                    index = 3;
                    movePos = new Position(2925, leaveRoom ? 5332 : 5331, 2);
                }
                int killcount_amount = 20;
                if (player.getDonorRights() == 1) {
                    killcount_amount = 15;
                } else if (player.getDonorRights() == 2) {
                    killcount_amount = 10;
                } else if (player.getDonorRights() == 3) {
                    killcount_amount = 5;
                } else if (player.getDonorRights() == 4) {
                    killcount_amount = 2;
                } else if (player.getDonorRights() == 5) {
                    killcount_amount = 0;
                }
                if (!leaveRoom && (player.getRights() != PlayerRights.ADMINISTRATOR
                        && player.getRights() != PlayerRights.DEVELOPER && player.getRights() != PlayerRights.DEVELOPER
                        && player.getRights() != PlayerRights.MANAGER && player.getMinigameAttributes()
                        .getGodwarsDungeonAttributes().getKillcount()[index] < killcount_amount)) {
                    player.getPacketSender().sendMessage("You need " + Misc.anOrA(bossRoom) + " " + bossRoom
                            + " killcount of at least " + killcount_amount + " to enter this room.");
                    return;
                }
                player.moveTo(movePos);
                player.getMinigameAttributes().getGodwarsDungeonAttributes()
                        .setHasEnteredRoom(leaveRoom ? false : true);
                player.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[index] = 0;
                player.getPacketSender().sendString(16216 + index, "0");
                break;
            case 26289:
            case 26286:
            case 26288:
            case 26287:
                if (System.currentTimeMillis() - player.getMinigameAttributes()
                        .getGodwarsDungeonAttributes().getAltarDelay() < 600000) {
                    player.getPacketSender().sendMessage("");
                    player.getPacketSender()
                            .sendMessage("You can only pray at a God's altar once every 10 minutes.");
                    player.getPacketSender().sendMessage("You must wait another "
                            + (int) ((600 - (System.currentTimeMillis() - player.getMinigameAttributes()
                            .getGodwarsDungeonAttributes().getAltarDelay()) * 0.001))
                            + " seconds before being able to do this again.");
                    return;
                }
                int itemCount = id == 26289 ? Equipment.getItemCount(player, "Bandos", false)
                        : id == 26286 ? Equipment.getItemCount(player, "Zamorak", false)
                        : id == 26288 ? Equipment.getItemCount(player, "Armadyl", false)
                        : id == 26287 ? Equipment.getItemCount(player, "Saradomin", false)
                        : 0;
                int toRestore = player.getSkillManager().getMaxLevel(Skill.PRAYER) + (itemCount * 10);
                if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) >= toRestore) {
                    player.getPacketSender()
                            .sendMessage("You do not need to recharge your Prayer points at the moment.");
                    return;
                }
                player.performAnimation(new Animation(645));
                player.getSkillManager().setCurrentLevel(Skill.PRAYER, toRestore);
                player.getMinigameAttributes().getGodwarsDungeonAttributes()
                        .setAltarDelay(System.currentTimeMillis());
                break;
            case 2873:
                player.performAnimation(new Animation(645));
                player.getPacketSender().sendMessage("You pray to Saradomin and recieve a holy cape...");
                player.getInventory().add(new Item(2412, 1));
                break;
            case 2875:
                player.performAnimation(new Animation(645));
                player.getPacketSender().sendMessage("You pray to Guthix and recieve a holy cape...");
                player.getInventory().add(new Item(2413, 1));
                break;
            case 2874:
                player.performAnimation(new Animation(645));
                player.getPacketSender().sendMessage("You pray to Zamorak and recieve a holy cape...");
                player.getInventory().add(new Item(2414, 1));
                break;
            case 16044:
                if (player.getPosition().getY() < 3875) {
                    TaskManager.submit(new Task(1, player, true) {
                        int tick = 1;

                        @Override
                        public void execute() {
                            tick++;
                            player.performAnimation(new Animation(804));
                            if (tick == 4) {
                                stop();
                            } else if (tick >= 6) {
                                stop();
                            }
                        }

                        @Override
                        public void stop() {
                            setEventRunning(false);
                            player.moveTo(new Position(player.getPosition().getX(),
                                    player.getPosition().getY() + 2));
                            player.getPacketSender().sendMessage("You teleport through the portal.");
                            player.getPacketSender()
                                    .sendMessage("You can only leave this zone by talking to Sir Tinley.");
                        }
                    });
                } else {
                    player.getPacketSender()
                            .sendMessage("You cannot leave through this portal, talk to Sir Tinley.");
                }
                break;
            case 23093:
                if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 70) {
                    player.getPacketSender().sendMessage(
                            "You need an Agility level of at least 70 to go through this portal.");
                    return;
                }
                if (!player.getClickDelay().elapsed(2000))
                    return;
                int plrHeight = player.getPosition().getZ();
                if (plrHeight == 2)
                    player.moveTo(new Position(2914, 5300, 1));
                else if (plrHeight == 1) {
                    x = gameObject.getPosition().getX();
                    y = gameObject.getPosition().getY();
                    if (x == 2914 && y == 5300)
                        player.moveTo(new Position(2912, 5299, 2));
                    else if (x == 2920 && y == 5276)
                        player.moveTo(new Position(2920, 5274, 0));
                } else if (plrHeight == 0)
                    player.moveTo(new Position(2920, 5276, 1));
                player.getClickDelay().reset();
                break;
            case 26439:
                if (player.getSkillManager().getMaxLevel(Skill.CONSTITUTION) <= 700) {
                    player.getPacketSender()
                            .sendMessage("You need a Constitution level of at least 70 to swim across.");
                    return;
                }
                if (!player.getClickDelay().elapsed(1000))
                    return;
                if (player.isCrossingObstacle())
                    return;
                final String startMessage = "You jump into the icy cold water..";
                final String endMessage = "You climb out of the water safely.";
                final int jumpGFX = 68;
                final int jumpAnimation = 772;
                player.setSkillAnimation(773);
                player.setCrossingObstacle(true);
                player.getUpdateFlag().flag(Flag.APPEARANCE);
                player.performAnimation(new Animation(3067));
                final boolean goBack2 = player.getPosition().getY() >= 5344;
                player.getPacketSender().sendMessage(startMessage);
                player.moveTo(new Position(2885, !goBack2 ? 5335 : 5342, 2));
                player.setDirection(goBack2 ? Direction.SOUTH : Direction.NORTH);
                player.performGraphic(new Graphic(jumpGFX));
                player.performAnimation(new Animation(jumpAnimation));
                TaskManager.submit(new Task(1, player, false) {
                    int ticks = 0;

                    @Override
                    public void execute() {
                        ticks++;
                        player.getWalkingQueue().walkStep(0, goBack2 ? -1 : 1);
                        if (ticks >= 10)
                            stop();
                    }

                    @Override
                    public void stop() {
                        player.setSkillAnimation(-1);
                        player.setCrossingObstacle(false);
                        player.getUpdateFlag().flag(Flag.APPEARANCE);
                        player.getPacketSender().sendMessage(endMessage);
                        player.moveTo(
                                new Position(2885, player.getPosition().getY() < 5340 ? 5333 : 5345, 2));
                        setEventRunning(false);
                    }
                });
                player.getClickDelay().reset((System.currentTimeMillis() + 9000));
                break;
            case 26384:
                if (player.isCrossingObstacle())
                    return;
                if (!player.getInventory().contains(2347)) {
                    player.getPacketSender()
                            .sendMessage("You need to have a hammer to bang on the door with.");
                    return;
                }
                player.setCrossingObstacle(true);
                final boolean goBack = player.getPosition().getX() <= 2850;
                player.performAnimation(new Animation(377));
                TaskManager.submit(new Task(2, player, false) {
                    @Override
                    public void execute() {
                        player.moveTo(new Position(goBack ? 2851 : 2850, 5333, 2));
                        player.setCrossingObstacle(false);
                        stop();
                    }
                });
                break;
            case 26303:
                if (!player.getClickDelay().elapsed(1200))
                    return;
                if (player.getSkillManager().getCurrentLevel(Skill.RANGED) < 70)
                    player.getPacketSender()
                            .sendMessage("You need a Ranged level of at least 70 to swing across here.");
                else if (!player.getInventory().contains(9419)) {
                    player.getPacketSender()
                            .sendMessage("You need a Mithril grapple to swing across here.");
                    return;
                } else {
                    player.performAnimation(new Animation(789));
                    TaskManager.submit(new Task(2, player, false) {
                        @Override
                        public void execute() {
                            player.getPacketSender().sendMessage(
                                    "You throw your Mithril grapple over the pillar and move across.");
                            player.moveTo(new Position(2871,
                                    player.getPosition().getY() <= 5270 ? 5279 : 5269, 2));
                            stop();
                        }
                    });
                    player.getClickDelay().reset();
                }
                break;
            case 4493:
                if (player.getPosition().getX() >= 3432) {
                    player.moveTo(new Position(3433, 3538, 1));
                }
                break;
            case 4494:
                player.moveTo(new Position(3438, 3538, 0));
                break;
            case 4495:
                player.moveTo(new Position(3417, 3541, 2));
                break;
            case 4496:
                player.moveTo(new Position(3412, 3541, 1));
                break;
            case 2491:
                player.setDialogueActionId(48);
                DialogueManager.start(player, 87);
                break;
            case 25339:
            case 25340:
                player.moveTo(new Position(1778, 5346, player.getPosition().getZ() == 0 ? 1 : 0));
                break;
            case 10229:
            case 10230:
                boolean up = id == 10229;
                player.performAnimation(new Animation(up ? 828 : 827));
                player.getPacketSender().sendMessage("You climb " + (up ? "up" : "down") + " the ladder..");
                TaskManager.submit(new Task(1, player, false) {
                    @Override
                    protected void execute() {
                        player.moveTo(up ? new Position(1912, 4367) : new Position(2900, 4449));
                        stop();
                    }
                });
                break;
            case 1568:
                player.moveTo(new Position(3097, 9868));
                break;
            case 5103: // Brimhaven vines
            case 5104:
            case 5105:
            case 5106:
            case 5107:
                if (!player.getClickDelay().elapsed(4000))
                    return;
                if (player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) < 30) {
                    player.getPacketSender()
                            .sendMessage("You need a Woodcutting level of at least 30 to do this.");
                    return;
                }
                if (WoodcuttingData.getHatchet(player) < 0) {
                    player.getPacketSender().sendMessage(
                            "You do not have a hatchet which you have the required Woodcutting level to use.");
                    return;
                }
                final WoodcuttingData.Hatchet axe = WoodcuttingData.Hatchet.forId(WoodcuttingData.getHatchet(player));
                player.performAnimation(new Animation(axe.getAnim()));
                gameObject.setRotation(-1);
                TaskManager.submit(new Task(3 + Misc.getRandom(4), player, false) {
                    @Override
                    protected void execute() {
                        if (player.moving) {
                            stop();
                            return;
                        }
                        int x = 0;
                        int y = 0;
                        if (player.getPosition().getX() == 2689 && player.getPosition().getY() == 9564) {
                            x = 2;
                            y = 0;
                        } else if (player.getPosition().getX() == 2691
                                && player.getPosition().getY() == 9564) {
                            x = -2;
                            y = 0;
                        } else if (player.getPosition().getX() == 2683
                                && player.getPosition().getY() == 9568) {
                            x = 0;
                            y = 2;
                        } else if (player.getPosition().getX() == 2683
                                && player.getPosition().getY() == 9570) {
                            x = 0;
                            y = -2;
                        } else if (player.getPosition().getX() == 2674
                                && player.getPosition().getY() == 9479) {
                            x = 2;
                            y = 0;
                        } else if (player.getPosition().getX() == 2676
                                && player.getPosition().getY() == 9479) {
                            x = -2;
                            y = 0;
                        } else if (player.getPosition().getX() == 2693
                                && player.getPosition().getY() == 9482) {
                            x = 2;
                            y = 0;
                        } else if (player.getPosition().getX() == 2672
                                && player.getPosition().getY() == 9499) {
                            x = 2;
                            y = 0;
                        } else if (player.getPosition().getX() == 2674
                                && player.getPosition().getY() == 9499) {
                            x = -2;
                            y = 0;
                        }
                        CustomObjects.objectRespawnTask(player,
                                new GameObject(-1, gameObject.getPosition().copy()), gameObject, 10);
                        player.getPacketSender().sendMessage("You chop down the vines..");
                        player.getSkillManager().addSkillExperience(Skill.WOODCUTTING, 45);
                        player.performAnimation(new Animation(65535));
                        player.getWalkingQueue().walkStep(x, y);
                        stop();
                    }
                });
                player.getClickDelay().reset();
                break;

            case 29942:
                if (player.getSkillManager().getCurrentLevel(Skill.SUMMONING) == player.getSkillManager()
                        .getMaxLevel(Skill.SUMMONING)) {
                    player.getPacketSender()
                            .sendMessage("You do not need to recharge your Summoning points right now.");
                    return;
                }
                player.performGraphic(new Graphic(1517));
                player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
                        player.getSkillManager().getMaxLevel(Skill.SUMMONING), true);
                player.getPacketSender().sendString(18045,
                        " " + player.getSkillManager().getCurrentLevel(Skill.SUMMONING) + "/"
                                + player.getSkillManager().getMaxLevel(Skill.SUMMONING));
                player.getPacketSender().sendMessage("You recharge your Summoning points.");
                break;
            case 57225:
                if (!player.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom()) {
                    player.setDialogueActionId(44);
                    DialogueManager.start(player, 79);
                } else {
                    player.moveTo(new Position(2906, 5204));
                    player.getMinigameAttributes().getGodwarsDungeonAttributes().setHasEnteredRoom(false);
                }
                break;
            case 884:
            case 26945:
                player.setDialogueActionId(41);
                DialogueManager.start(player, 75);
                break;
            case 9294:
                if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 80) {
                    player.getPacketSender()
                            .sendMessage("You need an Agility level of at least 80 to use this shortcut.");
                    return;
                }
                player.performAnimation(new Animation(769));
                TaskManager.submit(new Task(1, player, false) {
                    @Override
                    protected void execute() {
                        player.moveTo(
                                new Position(player.getPosition().getX() >= 2880 ? 2878 : 2880, 9813));
                        stop();
                    }
                });
                break;
            case 9293:
                boolean back = player.getPosition().getX() > 2888;
                player.moveTo(back ? new Position(2886, 9799) : new Position(2891, 9799));
                break;
            case 2320:
                back = player.getPosition().getY() == 9969 || player.getPosition().getY() == 9970;
                player.moveTo(back ? new Position(3120, 9963) : new Position(3120, 9969));
                break;
            case 1755:
                player.performAnimation(new Animation(828));
                player.getPacketSender().sendMessage("You climb the stairs..");
                TaskManager.submit(new Task(1, player, false) {
                    @Override
                    protected void execute() {
                        if (gameObject.getPosition().getX() == 2547
                                && gameObject.getPosition().getY() == 9951) {
                            player.moveTo(new Position(2548, 3551));
                        } else if (gameObject.getPosition().getX() == 3005
                                && gameObject.getPosition().getY() == 10363) {
                            player.moveTo(new Position(3005, 3962));
                        } else if (gameObject.getPosition().getX() == 3084
                                && gameObject.getPosition().getY() == 9672) {
                            player.moveTo(new Position(3117, 3244));
                        } else if (gameObject.getPosition().getX() == 3097
                                && gameObject.getPosition().getY() == 9867) {
                            player.moveTo(new Position(3096, 3468));
                        }
                        stop();
                    }
                });
                break;
            case 5110:
                player.moveTo(new Position(2647, 9557));
                player.getPacketSender().sendMessage("You pass the stones..");
                break;
            case 5111:
                player.moveTo(new Position(2649, 9562));
                player.getPacketSender().sendMessage("You pass the stones..");
                break;
            case 6434:
                player.performAnimation(new Animation(827));
                player.getPacketSender().sendMessage("You enter the trapdoor..");
                TaskManager.submit(new Task(1, player, false) {
                    @Override
                    protected void execute() {
                        player.moveTo(new Position(3085, 9672));
                        stop();
                    }
                });
                break;
            case 19187:
            case 19175:
                Hunter.dismantle(player, gameObject);
                break;
            case 25029:
                PuroPuro.goThroughWheat(player, gameObject);
                break;
            case 47976:
                Nomad.endFight(player, false);
                break;
            case 2182:
                if (!player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0)) {
                    player.getPacketSender()
                            .sendMessage("You have no business with this chest. Talk to the Gypsy first!");
                    return;
                }
                RecipeForDisaster.openRFDShop(player);
                break;
            case 12356:
                if (!player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0)) {
                    player.getPacketSender()
                            .sendMessage("You have no business with this portal. Talk to the Gypsy first!");
                    return;
                }
                if (player.getPosition().getZ() > 0) {
                    RecipeForDisaster.leave(player);
                } else {
                    player.getMinigameAttributes().getRecipeForDisasterAttributes().setPartFinished(1,
                            true);
                    RecipeForDisaster.enter(player);
                }
                break;
            case 9369:
                if (player.getPosition().getY() > 5175) {
                    FightPit.addPlayer(player);
                } else {
                    FightPit.removePlayer(player, "leave room");
                }
                break;
            case 9368:
                if (player.getPosition().getY() < 5169) {
                    FightPit.removePlayer(player, "leave game");
                }
                break;
            case 9357:
                FightCave.leaveCave(player, false);
                break;
            case 9356:
                FightCave.enterCave(player);
                break;
            case 6704:
                player.moveTo(new Position(3577, 3282, 0));
                break;
            case 5013:
                player.moveTo(new Position(2838, 10124, 0));
                break;
            case 5998:
                player.moveTo(new Position(2799, 10134, 0));
                break;
            case 6706:
                player.moveTo(new Position(3554, 3283, 0));
                break;
            case 6705:
                player.moveTo(new Position(3566, 3275, 0));
                break;
            case 6702:
                player.moveTo(new Position(3564, 3289, 0));
                break;
            case 6703:
                player.moveTo(new Position(3574, 3298, 0));
                break;
            case 6707:
                player.moveTo(new Position(3556, 3298, 0));
                break;
            case 3203:
                if (player.getLocation() == Locations.Location.DUEL_ARENA && player.getDueling().duelingStatus == 5) {
                    if (player.getDueling().timer >= 0) {
                        player.getPacketSender()
                                .sendMessage("You cannot forfeit before the duel has started.");
                        return;
                    }
                    if (Dueling.checkRule(player, Dueling.DuelRule.NO_FORFEIT)) {
                        player.getPacketSender().sendMessage("Forfeiting has been disabled in this duel.");
                        return;
                    }
                    player.getCombatBuilder().reset(true);
                    if (player.getDueling().duelingWith > -1) {
                        Player duelEnemy = World.getPlayers().get(player.getDueling().duelingWith);
                        if (duelEnemy == null)
                            return;
                        duelEnemy.getCombatBuilder().reset(true);
                        duelEnemy.getWalkingQueue().clear();
                        duelEnemy.getDueling().duelVictory();
                    }
                    player.moveTo(new Position(3368 + Misc.getRandom(5), 3267 + Misc.getRandom(3), 0));
                    player.getDueling().reset();
                    player.getCombatBuilder().reset(true);
                    player.restart();
                }
                break;
            case 14315:
                PestControl.boardBoat(player);
                break;
            case 14314:
                if (player.getLocation() == Locations.Location.PEST_CONTROL_BOAT) {
                    player.getLocation().leave(player);
                }
                break;
            case 1738:
                if (player.getLocation() == Locations.Location.LUMBRIDGE && player.getPosition().getZ() == 0) {
                    player.moveTo(
                            new Position(player.getPosition().getX(), player.getPosition().getY(), 1));
                } else if (player.getLocation() != Locations.Location.WARRIORS_GUILD
                        && player.getPosition().getZ() == 0) {
                    player.moveTo(new Position(2729, 3462, 1));
                } else {
                    player.moveTo(new Position(2840, 3539, 2));
                }
                break;
            case 1740:
                player.moveTo(new Position(2729, 3462, 0));
                break;
            case 15638:
                player.moveTo(new Position(2840, 3539, 0));
                break;
            case 15644:
            case 15641:
                switch (player.getPosition().getZ()) {
                    case 0:
                        player.moveTo(new Position(2855, player.getPosition().getY() >= 3546 ? 3545 : 3546));
                        break;
                    case 2:
                        if (player.getPosition().getX() == 2846) {
                            if (player.getInventory().getAmount(8851) < 70) {
                                player.getPacketSender()
                                        .sendMessage("You need at least 70 tokens to enter this area.");
                                return;
                            }
                            DialogueManager.start(player, WarriorsGuild.warriorsGuildDialogue(player));
                            player.moveTo(new Position(2847, player.getPosition().getY(), 2));
                            WarriorsGuild.handleTokenRemoval(player);
                        } else if (player.getPosition().getX() == 2847) {
                            WarriorsGuild.resetCyclopsCombat(player);
                            player.moveTo(new Position(2846, player.getPosition().getY(), 2));
                            player.getMinigameAttributes().getWarriorsGuildAttributes()
                                    .setEnteredTokenRoom(false);
                        }
                        break;
                }
                break;
            case 28714:
                player.performAnimation(new Animation(828));
                player.delayedMoveTo(new Position(3216, 3437), 2);
                break;
            case 1746:
                player.performAnimation(new Animation(827));
                player.delayedMoveTo(new Position(2209, 5348), 2);
                break;
            case 1756:
                player.performAnimation(new Animation(827));
                player.delayedMoveTo(new Position(2777, 10161), 2);
                break;
            case 2268:
                player.performAnimation(new Animation(828));
                player.delayedMoveTo(new Position(3229, 3610), 2);
                break;

            case 19191:
            case 19189:
            case 19180:
            case 19184:
            case 19182:
            case 19178:
                Hunter.lootTrap(player, gameObject);
                break;
            case 30205:
                player.setDialogueActionId(11);
                DialogueManager.start(player, 20);
                break;
            case 28716:
                if (!player.busy()) {
                    player.getSkillManager().updateSkill(Skill.SUMMONING);
                    player.getPacketSender().sendInterface(63471);
                } else
                    player.getPacketSender()
                            .sendMessage("Please finish what you're doing before opening this.");
                break;
            case 6:
                DwarfCannon cannon = player.getCannon();
                if (cannon == null || cannon.getOwnerIndex() != player.getIndex()) {
                    player.getPacketSender().sendMessage("This is not your cannon!");
                } else {
                    DwarfMultiCannon.startFiringCannon(player, cannon);
                }
                break;
            case 2:
                player.moveTo(new Position(player.getPosition().getX() > 2690 ? 2687 : 2694, 3714));
                player.getPacketSender().sendMessage("You walk through the entrance..");
                break;
            case 2026:
            case 2028:
            case 2029:
            case 2030:
            case 2031:
                player.setEntityInteraction(gameObject);
                Fishing.setupFishing(player, Fishing.forSpot(gameObject.getId(), false));
                return;
            case 12692:
            case 2783:
            case 4306:
                player.setInteractingObject(gameObject);
                EquipmentMaking.handleAnvil(player);
                break;
            case 2732:
                EnterAmountOfLogsToAdd.openInterface(player);
                break;
            case 409:
            case 27661:
            case 2640:
            case 36972:
                player.performAnimation(new Animation(645));
                if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) < player.getSkillManager()
                        .getMaxLevel(Skill.PRAYER)) {
                    player.getSkillManager().setCurrentLevel(Skill.PRAYER,
                            player.getSkillManager().getMaxLevel(Skill.PRAYER), true);
                    player.getPacketSender().sendMessage("You recharge your Prayer points.");
                }
                break;
            case 8749:
                boolean restore = player.getSpecialPercentage() < 100;
                if (restore) {
                    player.setSpecialPercentage(100);
                    CombatSpecial.updateBar(player);
                    player.getPacketSender().sendMessage("Your special attack energy has been restored.");
                }
                for (Skill skill : Skill.values()) {
                    int increase = skill != Skill.PRAYER && skill != Skill.CONSTITUTION
                            && skill != Skill.SUMMONING ? 19 : 0;
                    if (player.getSkillManager().getCurrentLevel(
                            skill) < (player.getSkillManager().getMaxLevel(skill) + increase))
                        player.getSkillManager().setCurrentLevel(skill,
                                (player.getSkillManager().getMaxLevel(skill) + increase));
                }
                player.performGraphic(new Graphic(1302));
                player.getPacketSender().sendMessage("Your stats have received a major buff.");
                break;
            case 4859:
                player.performAnimation(new Animation(645));
                if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) < player.getSkillManager()
                        .getMaxLevel(Skill.PRAYER)) {
                    player.getSkillManager().setCurrentLevel(Skill.PRAYER,
                            player.getSkillManager().getMaxLevel(Skill.PRAYER), true);
                    player.getPacketSender().sendMessage("You recharge your Prayer points.");
                }
                break;
            case 411:
                if (player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 30) {
                    player.getPacketSender()
                            .sendMessage("You need a Defence level of at least 30 to use this altar.");
                    return;
                }
                player.performAnimation(new Animation(645));
                if (player.getPrayerbook() == Prayerbook.NORMAL) {
                    player.getPacketSender()
                            .sendMessage("You sense a surge of power flow through your body!");
                    player.setPrayerbook(Prayerbook.CURSES);
                } else {
                    player.getPacketSender()
                            .sendMessage("You sense a surge of purity flow through your body!");
                    player.setPrayerbook(Prayerbook.NORMAL);
                }
                player.getPacketSender().sendTabInterface(GameSettings.PRAYER_TAB,
                        player.getPrayerbook().getInterfaceId());
                PrayerHandler.deactivateAll(player);
                CurseHandler.deactivateAll(player);
                break;
            case 2515:
                if (player.getLocation() == Locations.Location.ROCK_CRABS) {
                    player.performAnimation(new Animation(828));
                    player.getPacketSender().sendString(1, "ZULRAHFADE");
                    TaskManager.submit(new Task(1, player, true) {
                        int tick = 1;

                        @Override
                        public void execute() {
                            if (tick == 2) {
                                player.moveTo(new Position(2690, 3706, 0));
                            }
                            if (tick == 5) {
                                player.moveTo(new Position(2691, 3771, 0));
                            }
                            if (tick == 8) {
                                stop();
                            }
                            tick++;
                        }

                        @Override
                        public void stop() {
                            player.moveTo(new Position(3102, 2959, 0));
                        }
                    });
                } else {
                    player.performAnimation(new Animation(828));
                    player.getPacketSender().sendString(1, "ZULRAHFADE");
                    TaskManager.submit(new Task(1, player, true) {
                        int tick = 1;

                        @Override
                        public void execute() {
                            if (tick == 2) {
                                player.moveTo(new Position(3102, 2956, 0));
                            }
                            if (tick == 5) {
                                player.moveTo(new Position(2691, 3771, 0));
                            }
                            if (tick == 8) {
                                stop();
                            }
                            tick++;
                        }

                        @Override
                        public void stop() {
                            player.moveTo(new Position(2688, 3706, 0));
                        }
                    });
                }
                break;
            case 6552:
                player.performAnimation(new Animation(645));
                player.setSpellbook(player.getSpellbook() == MagicSpellbook.ANCIENT ? MagicSpellbook.NORMAL
                        : MagicSpellbook.ANCIENT);
                player.getPacketSender()
                        .sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId())
                        .sendMessage("Your magic spellbook is changed..");
                Autocasting.resetAutocast(player, true);
                break;
            case 410:
                if (player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 40) {
                    player.getPacketSender()
                            .sendMessage("You need a Defence level of at least 40 to use this altar.");
                    return;
                }
                player.performAnimation(new Animation(645));
                player.setSpellbook(player.getSpellbook() == MagicSpellbook.LUNAR ? MagicSpellbook.NORMAL
                        : MagicSpellbook.LUNAR);
                player.getPacketSender()
                        .sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId())
                        .sendMessage("Your magic spellbook is changed..");
                ;
                Autocasting.resetAutocast(player, true);
                break;
            case 2878:
                player.moveTo(new Position(2509, 4689));
                break;
            case 2879:
                player.moveTo(new Position(2498, 4718));
                break;
            case 172:
                CrystalChest.handleChest(player, gameObject);
                break;
            case 6910:
            case 4483:
            case 3193:
            case 2213:
            case 11758:
            case 6084:
            case 10517:
            case 14367:
            case 42192:
            case 26972:
            case 11402:
            case 26969:
            case 75:
                player.getBank(player.getCurrentBankTab()).open();
                break;
        }
    }

    public void secondClickObject(GameObject gameObject) {
        int id = gameObject.getId();
        int x = gameObject.getPosition().getX();
        int y = gameObject.getPosition().getY();
        if (MiningData.forRock(gameObject.getId()) != null) {
            Prospecting.prospectOre(player, id);
            return;
        }
        if (player.getFarming().click(player, x, y, 1))
            return;
        if (player.getThieving().stealFromStall(ThievingStall.forId(id)))
            return;
        switch (gameObject.getId()) {
            case 2274:
                player.setRevsWarning(true);
                player.getPacketSender().sendMessage("You have re-enabled the revs warning toggle.");
                break;
            case 6910:
            case 4483:
            case 3193:
            case 2213:
            case 6084:
            case 10517:
            case 11758:
            case 14367:
            case 42192:
            case 26972:
            case 11402:
            case 26969:
            case 75:
                player.getBank(player.getCurrentBankTab()).open();
                break;
            case 884:
            case 26945:
                player.setDialogueActionId(41);
                player.setInputHandling(new DonateToWell());
                player.getPacketSender().sendInterfaceRemoval()
                        .sendEnterAmountPrompt("How much money would you like to contribute with?");
                break;
            case 28716:
                if (player.getSkillManager().getCurrentLevel(Skill.SUMMONING) == player.getSkillManager()
                        .getMaxLevel(Skill.SUMMONING)) {
                    player.getPacketSender()
                            .sendMessage("You do not need to recharge your Summoning points right now.");
                    return;
                }
                player.performGraphic(new Graphic(1517));
                player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
                        player.getSkillManager().getMaxLevel(Skill.SUMMONING), true);
                player.getPacketSender().sendString(18045,
                        " " + player.getSkillManager().getCurrentLevel(Skill.SUMMONING) + "/"
                                + player.getSkillManager().getMaxLevel(Skill.SUMMONING));
                player.getPacketSender().sendMessage("You recharge your Summoning points.");
                break;
            case 2646:
            case 312:
                if (!player.getClickDelay().elapsed(1200))
                    return;
                if (player.getInventory().isFull()) {
                    player.getPacketSender().sendMessage("You don't have enough free inventory space.");
                    return;
                }
                String type = gameObject.getId() == 312 ? "Potato" : "Flax";
                player.performAnimation(new Animation(827));
                player.getInventory().add(gameObject.getId() == 312 ? 1942 : 1779, 1);
                player.getPacketSender().sendMessage("You pick some " + type + "..");
                gameObject.setPickAmount(gameObject.getPickAmount() + 1);
                if (Misc.getRandom(3) == 1 && gameObject.getPickAmount() >= 1
                        || gameObject.getPickAmount() >= 6) {
                    player.getPacketSender().sendClientRightClickRemoval();
                    gameObject.setPickAmount(0);
                    CustomObjects.globalObjectRespawnTask(new GameObject(-1, gameObject.getPosition()),
                            gameObject, 10);
                }
                player.getClickDelay().reset();
                break;
            case 2644:
                Flax.showSpinInterface(player);
                break;
            case 6:
                DwarfCannon cannon = player.getCannon();
                if (cannon == null || cannon.getOwnerIndex() != player.getIndex()) {
                    player.getPacketSender().sendMessage("This is not your cannon!");
                } else {
                    DwarfMultiCannon.pickupCannon(player, cannon, false);
                }
                break;
            case 6189:
            case 26814:
            case 11666:
                Smelting.openInterface(player);
                break;
            case 2152:
                player.performAnimation(new Animation(8502));
                player.performGraphic(new Graphic(1308));
                player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
                        player.getSkillManager().getMaxLevel(Skill.SUMMONING));
                player.getPacketSender().sendMessage("You renew your Summoning points.");
                break;
        }
    }

    public void thirdClickObject(GameObject gameObject) {
        int id = gameObject.getId();
        int x = gameObject.getPosition().getX();
        int y = gameObject.getPosition().getY();

    }

    public void fourthClickObject(GameObject gameObject) {
        int id = gameObject.getId();
        int x = gameObject.getPosition().getX();
        int y = gameObject.getPosition().getY();

    }

    public void fifthClickObject(GameObject gameObject) {
        int id = gameObject.getId();
        int x = gameObject.getPosition().getX();
        int y = gameObject.getPosition().getY();
        Construction.handleFifthObjectClick(x, y, id, player);
    }

}
