package com.runelive.world.entity.impl.player;

import java.util.concurrent.TimeUnit;

import com.runelive.GameServer;
import com.runelive.GameSettings;
import com.runelive.engine.task.TaskManager;
import com.runelive.engine.task.impl.BonusExperienceTask;
import com.runelive.engine.task.impl.CombatSkullEffect;
import com.runelive.engine.task.impl.FireImmunityTask;
import com.runelive.engine.task.impl.OverloadPotionTask;
import com.runelive.engine.task.impl.PlayerSkillsTask;
import com.runelive.engine.task.impl.PlayerSpecialAmountTask;
import com.runelive.engine.task.impl.PrayerRenewalPotionTask;
import com.runelive.engine.task.impl.StaffOfLightSpecialAttackTask;
import com.runelive.model.Flag;
import com.runelive.model.Locations;
import com.runelive.model.Locations.Location;
import com.runelive.model.PlayerRights;
import com.runelive.model.Position;
import com.runelive.model.Skill;
import com.runelive.model.container.impl.Bank;
import com.runelive.model.container.impl.Equipment;
import com.runelive.model.definitions.WeaponAnimations;
import com.runelive.model.definitions.WeaponInterfaces;
import com.runelive.net.PlayerSession;
import com.runelive.net.SessionState;
import com.runelive.net.security.ConnectionHandler;
import com.runelive.util.Misc;
import com.runelive.world.World;
import com.runelive.world.content.Achievements;
import com.runelive.world.content.BankPin;
import com.runelive.world.content.BonusManager;
import com.runelive.world.content.Lottery;
import com.runelive.world.content.PlayerLogs;
import com.runelive.world.content.PlayerPanel;
import com.runelive.world.content.PlayersOnlineInterface;
import com.runelive.world.content.WellOfGoodwill;
import com.runelive.world.content.clan.ClanChatManager;
import com.runelive.world.content.combat.effect.CombatPoisonEffect;
import com.runelive.world.content.combat.effect.CombatTeleblockEffect;
import com.runelive.world.content.combat.effect.CombatVenomEffect;
import com.runelive.world.content.combat.magic.Autocasting;
import com.runelive.world.content.combat.prayer.CurseHandler;
import com.runelive.world.content.combat.prayer.PrayerHandler;
import com.runelive.world.content.combat.pvp.BountyHunter;
import com.runelive.world.content.combat.range.DwarfMultiCannon;
import com.runelive.world.content.combat.weapon.CombatSpecial;
import com.runelive.world.content.dialogue.DialogueManager;
import com.runelive.world.content.grandexchange.GrandExchange;
import com.runelive.world.content.grandexchange.GrandExchangeOffer;
import com.runelive.world.content.grandexchange.GrandExchangeOffers;
import com.runelive.world.content.grandexchange.GrandExchangeSlot;
import com.runelive.world.content.grandexchange.GrandExchangeSlotState;
import com.runelive.world.content.minigames.impl.Barrows;
import com.runelive.world.content.pos.PlayerOwnedShops;
import com.runelive.world.content.skill.impl.hunter.Hunter;
import com.runelive.world.content.skill.impl.slayer.Slayer;
import com.runelive.world.entity.impl.npc.NPC;

public class PlayerHandler {

    public static void handleLogin(Player player) {
        player.setLoginQue(false);
        // Register the player
        System.out.println("[World] Registering player - [username, host] : [" + player.getUsername()
                + ", " + player.getHostAddress() + "]");
        ConnectionHandler.add(player.getHostAddress());
        World.getPlayers().add(player);
        World.updatePlayersOnline();
        PlayersOnlineInterface.add(player);
        player.getSession().setState(SessionState.LOGGED_IN);

        // Packets
        player.getPacketSender().sendMapRegion().sendDetails();

        player.getRecordedLogin().reset();
        boolean maxed_out = true;
        for (int i = 0; i < Skill.values().length; i++) {
            if (i == 21)
                continue;
            if (player.getSkillManager().getMaxLevel(i) < (i == 3 || i == 5 ? 990 : 99)) {
                maxed_out = false;
            }
        }
        if (maxed_out) {
            player.setAnnounceMax(true);
        }
        // Tabs
        player.getPacketSender().sendTabs();
        // Setting up the player's item containers..
        for (int i = 0; i < player.getBanks().length; i++) {
            if (player.getBank(i) == null) {
                player.setBank(i, new Bank(player));
            }
        }
        player.getInventory().refreshItems();
        player.getEquipment().refreshItems();

        // Weapons and equipment..
        WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
        WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
        CombatSpecial.updateBar(player);
        BonusManager.update(player);

        // Here we are checking if a player is at coords 2602, 5713 (Old tormented demon spawn, and will
        // be moving them to home)
        if (player.getPosition().equals(new Position(2602, 5713))) {
            player.moveTo(new Position(3087, 3502));
            System.out.println("Moved player " + player.getUsername() + " for being in a bad area.");
        }

        // Skills
        player.getSummoning().login();
        player.getFarming().load();
        Slayer.checkDuoSlayer(player, true);
        for (Skill skill : Skill.values()) {
            player.getSkillManager().updateSkill(skill);
        }

        // Relations
        player.getRelations().setPrivateMessageId(1).onLogin(player).updateLists(true, 1);

        // Client configurations
        player.getPacketSender().sendConfig(172, player.isAutoRetaliate() ? 1 : 0)
                .sendTotalXp(player.getSkillManager().getTotalGainedExp())
                .sendConfig(player.getFightType().getParentId(), player.getFightType().getChildId())
                .sendRunStatus().sendRunEnergy(player.getRunEnergy())
                .sendConstitutionOrbPoison(player.isPoisoned()).sendConstitutionOrbVenom(player.isVenomed())
                .sendString(8135, "" + player.getMoneyInPouch()).sendInteractionOption("Follow", 3, false)
                .sendInteractionOption("Trade With", 4, false).sendInterfaceRemoval().sendString(39161,
                "@or2@Server time: @or2@[ @yel@" + Misc.getCurrentServerTime() + "@or2@ ]");

        Autocasting.onLogin(player);
        PrayerHandler.deactivateAll(player);
        CurseHandler.deactivateAll(player);
        BonusManager.sendCurseBonuses(player);
        Achievements.updateInterface(player);
        Barrows.updateInterface(player);

        // Spellbook Teleports
        player.getPacketSender().sendString(13037, "Training Teleports");
        player.getPacketSender().sendString(13038, "Teleport to easy monsters.");
        player.getPacketSender().sendString(13047, "Skilling Areas");
        player.getPacketSender().sendString(13048, "Teleport to skillable locations.");
        player.getPacketSender().sendString(13055, "Boss Teleports");
        player.getPacketSender().sendString(13056, "Teleport to Bosses on RuneLive.");
        player.getPacketSender().sendString(13063, "Quests");
        player.getPacketSender().sendString(13064, "Teleport to start quests.");
        player.getPacketSender().sendString(13071, "Dungeons");
        player.getPacketSender().sendString(13072, "Teleport to dungeons.");
        player.getPacketSender().sendString(13081, "City Teleports");
        player.getPacketSender().sendString(13082, "Teleport to various citys.");
        player.getPacketSender().sendString(13089, "Minigames");
        player.getPacketSender().sendString(13090, "Teleport to minigames.");
        player.getPacketSender().sendString(13097, "Wilderness Areas");
        player.getPacketSender().sendString(13098, "Teleport to the wilderness.");

        player.getPacketSender().sendString(1300, "Training Teleports");
        player.getPacketSender().sendString(1301, "Teleport to easy monsters.");
        player.getPacketSender().sendString(1325, "Minigames");
        player.getPacketSender().sendString(1326, "Teleport to minigames.");
        player.getPacketSender().sendString(1350, "Wilderness Areas");
        player.getPacketSender().sendString(1351, "Teleport to the wilderness.");
        player.getPacketSender().sendString(1382, "City Teleports");
        player.getPacketSender().sendString(1383, "Teleport to various citys.");
        player.getPacketSender().sendString(1415, "Quests");
        player.getPacketSender().sendString(1416, "Teleport to start quests.");
        player.getPacketSender().sendString(1454, "Dungeons");
        player.getPacketSender().sendString(1455, "Teleport to dungeons.");
        player.getPacketSender().sendString(7457, "Boss Teleports");
        player.getPacketSender().sendString(7458, "Teleport to Bosses on RuneLive.");
        player.getPacketSender().sendString(18472, "Ape Atoll");

        // Tasks
        TaskManager.submit(new PlayerSkillsTask(player));
        if (player.isPoisoned()) {
            TaskManager.submit(new CombatPoisonEffect(player));
        }
        if (player.isVenomed()) {
            TaskManager.submit(new CombatVenomEffect(player));
        }
        if (player.getPrayerRenewalPotionTimer() > 0) {
            TaskManager.submit(new PrayerRenewalPotionTask(player));
        }
        if (player.getOverloadPotionTimer() > 0) {
            TaskManager.submit(new OverloadPotionTask(player));
        }
        if (player.getTeleblockTimer() > 0) {
            TaskManager.submit(new CombatTeleblockEffect(player));
        }
        if (player.getSkullTimer() > 0) {
            player.setSkullIcon(1);
            TaskManager.submit(new CombatSkullEffect(player));
        }
        if (player.getFireImmunity() > 0) {
            FireImmunityTask.makeImmune(player, player.getFireImmunity(), player.getFireDamageModifier());
        }
        if (player.getSpecialPercentage() < 100) {
            TaskManager.submit(new PlayerSpecialAmountTask(player));
        }
        if (player.hasStaffOfLightEffect()) {
            TaskManager.submit(new StaffOfLightSpecialAttackTask(player));
        }
        if (player.getMinutesBonusExp() >= 0) {
            TaskManager.submit(new BonusExperienceTask(player));
        }
        if (player.getPointsHandler().getPkPoints() < 0) {
            player.getPointsHandler().setPkPoints(0, false);
            System.out.println("The user " + player.getUsername()
                    + " logged in with negative PK Points, resetting to 0.");
        }

        // Update appearance
        player.getUpdateFlag().flag(Flag.APPEARANCE);

        // Others
        Lottery.onLogin(player);
        Locations.login(player);
        if (GameSettings.DOUBLE_EXP) {
            player.getPacketSender().sendMessage(
                    "@bla@Welcome to RuneLive! We're currently in Double EXP mode! (@red@X2.0@bla@)");
        } else if (GameSettings.INSANE_EXP) {
            player.getPacketSender().sendMessage(
                    "@bla@Welcome to RuneLive! We're currently in Insane EXP mode! (@red@X8.0@bla@)");
        } else {
            player.getPacketSender().sendMessage(
                    "@bla@Welcome to RuneLive! We're currently in Normal EXP mode! (@red@X1.0@bla@)");
        }

        long days = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - player.getLastLogin());

        if (player.getLastIpAddress() != null && player.getLastLogin() != -1 && player.showIpAddress()) {
            if (days > 0) {
                player.getPacketSender().sendMessage("You last logged in @blu@" + days + "@bla@ "
                        + (days > 1 ? "days" : "day") + " ago from @blu@" + player.getLastIpAddress());
            } else {
                player.getPacketSender()
                        .sendMessage("You last logged in earlier today from @blu@" + player.getLastIpAddress());
            }
        } else if ((player.getLastIpAddress() != null && player.getLastLogin() != -1 && !player.showIpAddress())) {
            if (days > 0) {
                player.getPacketSender().sendMessage("You last logged in @blu@" + days + "@bla@ "
                        + (days > 1 ? "days" : "day") + " ago.");
            } else {
                player.getPacketSender().sendMessage("You last logged in earlier today.");
            }
        }

        if (player.experienceLocked())
            player.getPacketSender().sendMessage("@red@Warning: your experience is currently locked.");

        player.getPacketSender().sendString(1, "[CLEAR]");
        ClanChatManager.handleLogin(player);

        if (GameSettings.DOUBLE_POINTS) {
            player.getPacketSender().sendMessage(
                    "<img=4> <col=008FB2>RuneLive currently has a double points event going on, make sure to use it!");
        }

        if (GameSettings.DOUBLE_VOTE_TOKENS) {
            player.getPacketSender().sendMessage(
                    "<img=4> <col=008FB2>RuneLive currently has a double vote rewards event going on, make sure to use it!");
        }

        if (GameSettings.TRIPLE_VOTE_TOKENS) {
            player.getPacketSender().sendMessage(
                    "<img=4> <col=008FB2>RuneLive currently has a triple vote rewards event going on, make sure to use it!");
        }

        if (WellOfGoodwill.isActive()) {
            if (player.getDonorRights() > 0) {
                player.getPacketSender().sendMessage(
                        "<img=4> <col=008FB2>The Well of Goodwill is granting 50% bonus experience for another "
                                + WellOfGoodwill.getMinutesRemaining() + " minutes.");
            } else {
                player.getPacketSender().sendMessage(
                        "<img=4> <col=008FB2>The Well of Goodwill is granting 30% bonus experience for another "
                                + WellOfGoodwill.getMinutesRemaining() + " minutes.");
            }
        }

        PlayerPanel.refreshPanel(player);


        // New player
        if (player.newPlayer()) {
            // player.setClanChatName("runelive");
            player.setPlayerLocked(true).setDialogueActionId(45);
            DialogueManager.start(player, 81);
        }

        player.getPacketSender().updateSpecialAttackOrb()
                .sendIronmanMode(player.getGameMode().ordinal());

        GrandExchange.onLogin(player);
        GrandExchange.updateSlotStates(player);
        if (!player.hasDoneGrandExchangeReturn()) {
            for (int i = 0; i < 6; i++) {
                GrandExchangeOffer offer = player.getGrandExchangeSlots()[i].getOffer();
                if (offer != null) {
                    player.getPacketSender().sendMessage(
                            "Hi, The Grand Exchange has been disabled while we work on Player Owned Shops.");
                    player.getPacketSender().sendMessage(
                            "An offer that you had in The Grand Exchange has been added to your bank.");
                    GrandExchangeSlot slot = player.getGrandExchangeSlots()[i];
                    if (slot.getState() == GrandExchangeSlotState.FINISHED_SALE) {
                        player.getBank(0).add(995, offer.getCoinsCollect());
                    } else if (slot.getState() == GrandExchangeSlotState.PENDING_PURCHASE) {
                        player.getBank(0).add(995, offer.getPricePerItem() * offer.getAmount());
                    } else {
                        player.getBank(0).add(offer.getId(), offer.getAmount());
                    }
                    if (offer.getId() == 995) {
                        offer.setCoinsCollect(0);
                    } else {
                        offer.setItemCollect(0);
                    }
                    GrandExchangeOffers.setOffer(offer.getIndex(), null);
                    player.getGrandExchangeSlots()[i].setOffer(null);
                    player.getGrandExchangeSlots()[i].setState(GrandExchangeSlotState.EMPTY);
                }
            }
            player.setDoneGrandExchangeReturn(true);
            player.save();
        }
        if (player.getPointsHandler().getAchievementPoints() == 0) {
            Achievements.setPoints(player);
        }
        if (player.getRights() == PlayerRights.OWNER
                || player.getRights() == PlayerRights.COMMUNITY_MANAGER) {
            player.setDonorRights(5);
        } else if (player.getRights() == PlayerRights.ADMINISTRATOR) {
            player.setDonorRights(5);
        } else if ((player.getRights() == PlayerRights.MODERATOR
                || player.getRights() == PlayerRights.WIKI_MANAGER) && player.getDonorRights() < 3) {
            player.setDonorRights(3);
        } else if ((player.getRights() == PlayerRights.SUPPORT
                || player.getRights() == PlayerRights.WIKI_EDITOR) && player.getDonorRights() < 1) {
            player.setDonorRights(1);
        }
        if (player.getRights().isStaff()) {
            if (player.getRights() == PlayerRights.OWNER) {
                player.setLoyaltyRank(43);
            } else if (player.getRights() == PlayerRights.COMMUNITY_MANAGER) {
                player.setLoyaltyRank(50);
            } else if (player.getRights() == PlayerRights.ADMINISTRATOR) {
                player.setLoyaltyRank(42);
            } else if (player.getRights() == PlayerRights.WIKI_MANAGER) {
                player.setLoyaltyRank(49);
            } else if (player.getRights() == PlayerRights.WIKI_EDITOR) {
                player.setLoyaltyRank(48);
            } else if (player.getRights() == PlayerRights.GLOBAL_MOD) {
                player.setLoyaltyRank(47);
            } else if (player.getRights() == PlayerRights.SUPPORT) {
                player.setLoyaltyRank(44);
            } else if (player.getRights() == PlayerRights.MODERATOR) {
                player.setLoyaltyRank(41);
            } else if (player.getRights() == PlayerRights.STAFF_MANAGER) {
                player.setLoyaltyRank(52);
            }
        } else if (player.getDonorRights() != 0 && !player.getUsername().equalsIgnoreCase("dc blitz")
                && !player.getUsername().equalsIgnoreCase("hero")) {
            if (player.getDonorRights() == 1) {
                player.setLoyaltyRank(37);
            } else if (player.getDonorRights() == 2) {
                player.setLoyaltyRank(38);
            } else if (player.getDonorRights() == 3) {
                player.setLoyaltyRank(39);
            } else if (player.getDonorRights() == 4) {
                player.setLoyaltyRank(40);
            } else if (player.getDonorRights() == 5) {
                player.setLoyaltyRank(46);
            }
        }

        if (player.getUsername().equalsIgnoreCase("dc blitz")
                || player.getUsername().equalsIgnoreCase("hero")) {
            player.setLoyaltyRank(51);
        }

        if (player.getBankPinAttributes().hasBankPin()
                && !player.getBankPinAttributes().hasEnteredBankPin()
                && player.getBankPinAttributes().onDifferent(player)) {
            BankPin.init(player, false);
        }
        if (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) == 0) {
            player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
                    player.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
        }
        PlayerOwnedShops.collectCoinsOnLogin(player);
        if (!player.getBankPinAttributes().hasBankPin()) {
            if (player.getLocation() != Location.WILDERNESS) {
                if (!player.newPlayer() && !player.getRights().equals(PlayerRights.OWNER)) {
                    player.setDialogueActionId(181);
                    DialogueManager.start(player, 181);
                }
            }
        }
        // PlayerOwnedShops.collectCoinsOnLogin(player);
        PlayerLogs.log(player.getUsername(), "Login from host " + player.getHostAddress()
                + ", Computer Address: " + player.getComputerAddress());
    }

    public static boolean handleLogout(Player player) {
        try {
            PlayerSession session = player.getSession();

            if (session.getChannel().isOpen()) {
                session.getChannel().close();
            }

            if (!player.isRegistered()) {
                return true;
            }
            if (player.spawnedCerberus) {
                NPC n = new NPC(5866, new Position(1240, 1253, player.getPosition().getZ())).setSpawnedFor(player);
                World.deregister(n);
            }
            boolean exception = GameServer.isUpdating()
                    || World.getLogoutQueue().contains(player) && player.getLogoutTimer().elapsed(90000);
            if (player.logout() || exception) {
                System.out.println("[World] Deregistering player - [username, host] : ["
                        + player.getUsername() + ", " + player.getHostAddress() + "]");
                // if(player.getRights() != PlayerRights.OWNER && player.getRights() !=
                // PlayerRights.ADMINISTRATOR) {
                // new Thread(new Highscores(player)).start();
                // }
                // Sets last account information available
                player.setLastLogin(System.currentTimeMillis());
                player.setLastIpAddress(player.getHostAddress());
                player.setLastSerialAddress(player.getSerialNumber());
                player.setLastMacAddress(player.getMacAddress());
                player.setLastComputerAddress(player.getComputerAddress());
                player.getSession().setState(SessionState.LOGGING_OUT);
                ConnectionHandler.remove(player.getHostAddress());
                player.setTotalPlayTime(player.getTotalPlayTime() + player.getRecordedLogin().elapsed());
                player.getPacketSender().sendInterfaceRemoval();
                if (player.getCannon() != null) {
                    DwarfMultiCannon.pickupCannon(player, player.getCannon(), true);
                }
                if (exception && player.getResetPosition() != null) {
                    player.moveTo(player.getResetPosition());
                    player.setResetPosition(null);
                }
                if (player.getRegionInstance() != null) {
                    player.getRegionInstance().destruct();
                }
                Hunter.handleLogout(player);
                Locations.logout(player);
                player.getSummoning().unsummon(false, false);
                player.getFarming().save();
                BountyHunter.handleLogout(player);
                ClanChatManager.leave(player, false);
                player.getRelations().updateLists(false, 0);
                PlayersOnlineInterface.remove(player);
                TaskManager.cancelTasks(player.getCombatBuilder());
                TaskManager.cancelTasks(player);
                player.save();
                World.getPlayers().remove(player);
                session.setState(SessionState.LOGGED_OUT);
                World.updatePlayersOnline();
                player.setForumConnections(0);
                PlayerLogs.log(player.getUsername(), "Logout from host " + player.getHostAddress()
                        + ", Computer Address: " + player.getComputerAddress() + "");
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
