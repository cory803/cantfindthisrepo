package com.chaos.model.player;

import com.chaos.GameSettings;
import com.chaos.model.*;
import com.chaos.model.container.impl.Shop;
import com.chaos.model.definitions.ItemDefinition;
import com.chaos.model.input.impl.PosSearchShop;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.*;
import com.chaos.world.content.combat.magic.CombatSpells;
import com.chaos.world.content.pos.PlayerOwnedShops;
import com.chaos.world.content.skill.Enchanting;
import com.chaos.world.content.skill.impl.crafting.Tanning;
import com.chaos.world.content.skill.impl.fishing.Fishing;
import com.chaos.world.content.skill.impl.hunter.PuroPuro;
import com.chaos.world.content.skill.impl.runecrafting.DesoSpan;
import com.chaos.world.content.skill.impl.summoning.BossPets;
import com.chaos.world.content.skill.impl.summoning.Summoning;
import com.chaos.world.content.skill.impl.summoning.SummoningData;
import com.chaos.world.content.skill.impl.thieving.ThievingManager;
import com.chaos.world.content.transportation.TeleportHandler;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;
import org.scripts.kotlin.content.dialog.healers.Healers;
import org.scripts.kotlin.content.dialog.healers.HealersQuickOption;
import org.scripts.kotlin.content.dialog.npcs.*;

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
        if (player.getStaffRights().isDeveloper(player))
            player.getPacketSender().sendMessage("First click npc id: " + npc.getId());
        if (BossPets.pickup(player, npc)) {
            player.getWalkingQueue().clear();
            return;
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
            //Bankers
            case 494:
                player.getBank(0).open();
                break;

            //Oziach
            case 747:
                player.getDialog().sendDialog(new Oziach(player));
                break;

            //Duel arena healers
            case 959:
            case 960:
            case 961:
            case 962:
                player.getDialog().sendDialog(new Healers(player));
                break;

            //Talk to Horvik
            case 549:
                player.getDialog().sendDialog(new Horvik(player));
                break;

            //Talk to Zeke
            case 541:
                player.getDialog().sendDialog(new Zeke(player));
                break;

            //Talk to Zaff
            case 546:
                player.getDialog().sendDialog(new Zaff(player));
                break;

            //Talk to Lowe
            case 550:
                player.getDialog().sendDialog(new Lowe(player));
                break;

            //Talk to Giles
            case 2538:
                player.getDialog().sendDialog(new Giles(player));
                break;

            //Talk to Thessalia
            case 548:
                player.getDialog().sendDialog(new Thessalia(player));
                break;

            //Talk to Shopkeeper
            case 520:
                player.getDialog().sendDialog(new Shopkeeper(player));
                break;

            //Talk to Wise Old Man
            case 2253:
                player.getDialog().sendDialog(new WiseOldMan(player));
                break;

            //Talk to Bob
            case 519:
                player.getDialog().sendDialog(new Bob(player));
                break;

            //Talk to Sailor
            case 1304:
                player.getDialog().sendDialog(new Sailor(player));
                break;

            case 501:
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
        if (player.getStaffRights().isDeveloper(player))
            player.getPacketSender().sendConsoleMessage("Second click npc id: " + npc.getId());

        if (GameSettings.DEBUG_MODE) {
            // PlayerLogs.log(player, "" + player.getUsername()
            // + " in NPCOptionPacketListener: " + npc.getId() + " -
            // SECOND_CLICK_OPCODE");
        }
        switch (npc.getId()) {
            //Oziach
            case 747:
                Artifacts.sellArtifacts(player);
                break;
            //Duel arena healers
            case 959:
            case 960:
            case 961:
            case 962:
                player.getDialog().sendDialog(new HealersQuickOption(player));
                break;

            //Horvik's armour shop
            case 549:
                Shop.ShopManager.getShops().get(1).open(player);
                break;

            //Zeke's weapon shop
            case 541:
                Shop.ShopManager.getShops().get(2).open(player);
                break;

            //Zaff's magic shop
            case 546:
                Shop.ShopManager.getShops().get(5).open(player);
                break;

            //Lowe's range shop
            case 550:
                Shop.ShopManager.getShops().get(4).open(player);
                break;

            //Giles's supply shop
            case 2538:
                Shop.ShopManager.getShops().get(3).open(player);
                break;

            //Thessalia's clothes store
            case 548:
                Shop.ShopManager.getShops().get(20).open(player);
                break;

            //Shopkeeper's general store
            case 520:
                Shop.ShopManager.getShops().get(0).open(player);
                break;

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
        if (player.getStaffRights().isDeveloper(player))
            player.getPacketSender().sendMessage("Third click npc id: " + npc.getId());

        if (GameSettings.DEBUG_MODE) {
            // PlayerLogs.log(player, "" + player.getUsername()
            // + " in NPCOptionPacketListener: " + npc.getId() + " -
            // THIRD_CLICK_OPCODE");
        }
        switch (npc.getId()) {
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
                //Stat restore npc here
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
        if (player.getStaffRights().isDeveloper(player))
            player.getPacketSender().sendMessage("Fourth click npc id: " + npc.getId());

        if (GameSettings.DEBUG_MODE) {
            // PlayerLogs.log(player, "" + player.getUsername()
            // + " in NPCOptionPacketListener: " + npc.getId() + " -
            // FOURTH_CLICK_OPCODE");
        }
        switch (npc.getId()) {
            case 2217:
                player.getPacketSender().sendEnterInputPrompt("Enter the name of a player's shop:");
                break;
            case 4657:
                if (!player.getDonatorRights().isDonator()) {
                    player.getPacketSender().sendMessage("You need to be a member to teleport to this zone.")
                            .sendMessage("To become a member, visit chaosps.com and purchase a scroll.");
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
        }
        npc.setPositionToFace(player.getPosition());
        player.setPositionToFace(npc.getPosition());
    }

}
