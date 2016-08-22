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
            case 4707: // bolt enchanter
                Enchanting.update_interface(player);
                break;
            case 2127:
                if (!GameSettings.POS_ENABLED) {
                    player.getPacketSender().sendMessage("Player owned shops have been disabled.");
                    return;
                }
                player.setDialogueActionId(214);
                DialogueManager.start(player, 214);
                break;
            case 8710:
            case 8707:
            case 8706:
            case 8705:
                EnergyHandler.rest(player);
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
            case 8591:
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
            case 3789:
                player.getPacketSender().sendInterface(18730);
                player.getPacketSender().sendString(18729,
                        "Commendations: " + Integer.toString(player.getPointsHandler().getCommendations()));
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
            case 2676:
                player.getPacketSender().sendInterface(3559);
                player.getAppearance().setCanChangeAppearance(true);
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

}
