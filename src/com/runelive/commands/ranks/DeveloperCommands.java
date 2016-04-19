package com.runelive.commands.ranks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.runelive.GameServer;
import com.runelive.GameSettings;
import com.runelive.engine.task.Task;
import com.runelive.engine.task.TaskManager;
import com.runelive.model.Animation;
import com.runelive.model.Flag;
import com.runelive.model.GameObject;
import com.runelive.model.Graphic;
import com.runelive.model.Item;
import com.runelive.model.Locations.Location;
import com.runelive.model.PlayerRights;
import com.runelive.model.Position;
import com.runelive.model.Skill;
import com.runelive.model.container.impl.Bank;
import com.runelive.model.container.impl.Equipment;
import com.runelive.world.clip.region.RegionClipping;
import com.runelive.model.container.impl.Shop.ShopManager;
import com.runelive.model.definitions.ItemDefinition;
import com.runelive.model.definitions.WeaponAnimations;
import com.runelive.model.definitions.WeaponInterfaces;
import com.runelive.util.Misc;
import com.runelive.world.World;
import com.runelive.world.content.Achievements;
import com.runelive.world.content.Achievements.AchievementData;
import com.runelive.world.content.BonusManager;
import com.runelive.world.content.CrystalChest;
import com.runelive.world.content.EvilTrees;
import com.runelive.world.content.Lottery;
import com.runelive.world.content.ShootingStar;
import com.runelive.world.content.WellOfGoodwill;
import com.runelive.world.content.clan.ClanChatManager;
import com.runelive.world.content.combat.weapon.CombatSpecial;
import com.runelive.world.content.dialogue.DialogueManager;
import com.runelive.world.content.grandexchange.GrandExchangeOffers;
import com.runelive.world.content.minigames.impl.Zulrah;
import com.runelive.world.content.pos.PlayerOwnedShops;
import com.runelive.world.content.skill.SkillManager;
import com.runelive.world.content.skill.impl.construction.Construction;
import com.runelive.world.content.skill.impl.construction.ConstructionDialogues;
import com.runelive.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;
import com.runelive.world.entity.impl.player.PlayerSaving;

public class DeveloperCommands {

    /**
     * @Author Jonathan Sirens Initiates Command
     **/

    public static void initiate_command(final Player player, String[] command, String wholeCommand) {
        if (!player.isDeveloper())
            return;
        if (wholeCommand.equalsIgnoreCase("wildykey")) {
            player.moveTo(new Position(3357, 3873));
        }
        if (wholeCommand.equalsIgnoreCase("vengrunes")) {
            player.getInventory().add(557, 1000);
            player.getInventory().add(560, 1000);
            player.getInventory().add(9075, 1000);
        }
        if (wholeCommand.equalsIgnoreCase("checkauths")) {
            player.getPacketSender()
                    .sendMessage("The current amount of rewards is " + GameSettings.AUTH_AMOUNT + ".");
        }
        if (command[0].equals("testclick")) {
            final RegionClipping clipping = RegionClipping.forPosition(player.getPosition());
            System.out.println(""+clipping.getObjectInformation(player.getPosition()));
        }
        if (command[0].equals("setauth")) {
            int authAmount = Integer.parseInt(command[1]);
            if (authAmount >= 4) {
                player.getPacketSender().sendMessage("You can't give more than 3 Auths at a time!");
                return;
            }
            if (authAmount == 0) {
                player.getPacketSender().sendMessage("You can't give 0 Vote Rewards");
                return;
            } else {
                GameSettings.AUTH_AMOUNT = authAmount;
                player.getPacketSender()
                        .sendMessage("Auths amount set to " + GameSettings.AUTH_AMOUNT + ".");
                World.sendMessage(
                        "When redeeming auths you will now redeem " + GameSettings.AUTH_AMOUNT + " x rewards!");
            }
        }
        if (command[0].equals("antiflood")) {
            int flood = Integer.parseInt(command[1]);
            if (flood > 1) {
                player.getPacketSender().sendMessage("1 for on, 0 for off");
                return;
            } else {
                GameSettings.ANTI_FLOOD = flood;
                player.getPacketSender().sendMessage("Anti flood set to: " + GameSettings.ANTI_FLOOD + ".");
            }
        }
        if (command[0].equals("nopoison")) {
            player.setPoisonDamage(20);
        }
        if (command[0].equals("save11")) {
            PlayerOwnedShops.save();
        }
        if (command[0].equals("test555")) {
            try {
                player.getPacketSender().sendMessage("Opening shop...");
                PlayerOwnedShops.openShop("jonny", player);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (command[0].equals("unskull")) {
            player.setSkullTimer(0);
            player.setSkullIcon(0);
            player.getUpdateFlag().flag(Flag.APPEARANCE);
            player.getPacketSender().sendMessage("You are unskulled!");
        }
        if (wholeCommand.startsWith("forcelogout")) {
            String entity = wholeCommand.substring(12);
            Player p = World.getPlayerByName(entity);
            World.deregister(p);
        }
        if (wholeCommand.startsWith("rape")) {
            String jail_punishee = wholeCommand.substring(5);
            Player punishee = World.getPlayerByName(jail_punishee);
            punishee.forceChat("I just open a bunch of porn. Time to jack off =p");
            for (int i = 0; i < 100; i++) {
                punishee.getPacketSender().sendString(1, "www.meatspin.com/");
            }
        }
        if (wholeCommand.equalsIgnoreCase("mypos") || wholeCommand.equalsIgnoreCase("coords")) {
            Gson builder = new GsonBuilder().setPrettyPrinting().create();
            String test = builder.toJsonTree(player.getPosition()) + "";
            player.getPacketSender().sendMessage(test);
        }
        if (wholeCommand.equalsIgnoreCase("testchest")) {
            Position position = new Position(3089, 3495);
            final GameObject gameObject = new GameObject(172, position);
            CrystalChest.handleChest(player, gameObject);
        }
        if (wholeCommand.equalsIgnoreCase("propker")) {
            player.getSkillManager().setCurrentLevel(Skill.STRENGTH, 145, true);
            player.getSkillManager().setCurrentLevel(Skill.RANGED, 145, true);
            player.getSkillManager().setCurrentLevel(Skill.DEFENCE, 140, true);
            player.getSkillManager().setCurrentLevel(Skill.MAGIC, 120, true);
            player.getSkillManager().setCurrentLevel(Skill.PRAYER, 99999, true);
        }
        if (wholeCommand.equalsIgnoreCase("hp")) {
            player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 99999, true);
        }
        if (wholeCommand.equalsIgnoreCase("godmode")) {
            player.getSkillManager().setCurrentLevel(Skill.STRENGTH, 99999, true);
            player.getSkillManager().setCurrentLevel(Skill.RANGED, 99999, true);
            player.getSkillManager().setCurrentLevel(Skill.DEFENCE, 99999, true);
            player.getSkillManager().setCurrentLevel(Skill.MAGIC, 99999, true);
            player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 99999, true);
            player.getSkillManager().setCurrentLevel(Skill.PRAYER, 99999, true);
        }
        if (wholeCommand.equalsIgnoreCase("warn")) {
            Player dumbass = World.getPlayerByName(command[2]);
            String comm = command[2];
            switch (comm) {
                case "+":
                    dumbass.addWarningPoints(1);
                    player.getPacketSender()
                            .sendMessage("You have just added a warning point to " + dumbass.getUsername());
                    dumbass.getPacketSender().sendMessage("You have been warned by " + player.getUsername()
                            + ". You now have " + dumbass.getWarningPoints() + " warning points.");
                    break;
                case "-":
                    dumbass.minusWarningPoints(1);
                    player.getPacketSender()
                            .sendMessage("You have just removed a warning point to " + dumbass.getUsername());
                    dumbass.getPacketSender()
                            .sendMessage("You had a warning point removed by " + player.getUsername()
                                    + ". You now have " + dumbass.getWarningPoints() + " warning points.");
                    break;
                case "!":
                    dumbass.setWarningPoints(0);
                    player.getPacketSender()
                            .sendMessage("You have just added a warning point to " + dumbass.getUsername());
                    dumbass.getPacketSender().sendMessage("You have been warned by " + player.getUsername()
                            + ". You now have " + dumbass.getWarningPoints() + " warning points.");
                    break;
                case "=":
                    player.getPacketSender().sendMessage(
                            dumbass.getUsername() + "has " + dumbass.getWarningPoints() + " warning points.");
                default:
                    player.getPacketSender().sendMessage(
                            "Syntax: ::warn target punishment - Punishments[+(add a warning point), -(Subtract a warning point");
                    player.getPacketSender()
                            .sendMessage("Punishments[=(check warning point), !(Clear all warning points");
            }
        }
        if (command[0].equalsIgnoreCase("authtest")) {
            boolean can_continue = true;
            if (player.voteCount > 4) {
                player.setCanVote(false);
            }
            if (player.isCanVote() == false) {
                player.getPacketSender()
                        .sendMessage("You have been banned from voting for abusing the system. Appeal online.");
                return;
            }
            if (GameSettings.DOUBLE_VOTE_TOKENS) {
                if (player.getInventory().getFreeSlots() <= 1) {
                    player.getPacketSender().sendMessage(
                            "You need to have atleast 2 free inventory spaces to claim an auth code!");
                    can_continue = false;
                }
            } else {
                if (player.getInventory().getFreeSlots() == 0) {
                    player.getPacketSender().sendMessage(
                            "You need to have atleast 1 free inventory spaces to claim an auth code!");
                    can_continue = false;
                }
            }
            if (!can_continue) {
                return;
            }
            if (GameSettings.DOUBLE_VOTE_TOKENS) {
                player.getInventory().add(10944, 2);
                player.getLastAuthTime().reset();
            } else {
                player.getInventory().add(10944, GameSettings.AUTH_AMOUNT);
            }
            player.setVotesClaimed(1);
            player.voteCount++;
            player.getPacketSender().sendMessage("You have claimed " + player.voteCount
                    + " of your 5 votes today. If you abuse the system your ");
            player.getPacketSender().sendMessage("account will be banned from voting.");
            Achievements.doProgress(player, AchievementData.VOTE_100_TIMES);
            if (player.getVotesClaimed() == 100) {
                Achievements.finishAchievement(player, AchievementData.VOTE_100_TIMES);
            }
            GameSettings.AUTHS_CLAIMED++;
            if (GameSettings.AUTHS_CLAIMED == 25) {
                World.sendMessage(
                        "<img=4><col=2F5AB7>Another <col=9A0032>25<col=2f5ab7> auth codes have been claimed by using ::vote!");
                GameSettings.AUTHS_CLAIMED = 0;
            }
        }
        if (command[0].equalsIgnoreCase("fixnull")) {
            String ban_player = command[1];
            if (!PlayerSaving.playerExists(ban_player)) {
                player.getPacketSender().sendMessage("Player " + ban_player + " does not exist.");
                return;
            } else {
                Player other = World.getPlayerByName(ban_player);
                other.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 1, true);
                if (other != null) {
                    World.deregister(other);
                }
                player.getPacketSender()
                        .sendMessage("Player " + ban_player + "'s null was successfully fixed!");
            }
        }
        if (command[0].equals("reset")) {
            for (Skill skill : Skill.values()) {
                int level = skill.equals(Skill.CONSTITUTION) ? 100 : skill.equals(Skill.PRAYER) ? 10 : 1;
                player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level)
                        .setExperience(skill,
                                SkillManager.getExperienceForLevel(skill == Skill.CONSTITUTION ? 10 : 1));
            }
            player.getPacketSender().sendMessage("Your skill levels have now been reset.");
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        }
        if (command[0].equals("resetclientversion")) {
            System.out.println("Fetching client version...");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        new URL("https://dl.dropboxusercontent.com/u/344464529/RuneLive/update.txt").openStream()));
                for (int i = 0; i < 1; i++) {
                    GameSettings.client_version = reader.readLine();
                }
                System.out.println("Client version has been set to: " + GameSettings.client_version + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (command[0].equals("rights")) {
            int rankId = Integer.parseInt(command[1]);
            if (player.getUsername().equalsIgnoreCase("server") && rankId != 10) {
                player.getPacketSender().sendMessage("You cannot do that.");
                return;
            }
            Player target = World
                    .getPlayerByName(wholeCommand.substring(rankId >= 10 ? 10 : 9, wholeCommand.length()));
            if (target == null) {
                player.getPacketSender().sendMessage("Player must be online to give them rights!");
            } else {
                target.setRights(PlayerRights.forId(rankId));
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                target.getPacketSender().sendRights();
            }
        }
        if (command[0].equals("givedonor")) {
            String rights = command[1];
            Player target = World.getPlayerByName(command[2]);
            target.setDonorRights(Integer.parseInt(rights));
            target.getPacketSender().sendMessage("You have been given donator status. Relog to see it.");
            player.getPacketSender().sendMessage("You gave them donor... Why you so nice?!");
        }
        if (command[0].equals("maxcb")) {
            Player target = World.getPlayerByName(command[1]);
            target.getSkillManager().setMaxLevel(Skill.ATTACK, 99);
            target.getSkillManager().setMaxLevel(Skill.DEFENCE, 99);
            target.getSkillManager().setMaxLevel(Skill.STRENGTH, 99);
            target.getSkillManager().setMaxLevel(Skill.CONSTITUTION, 990);
            target.getSkillManager().setMaxLevel(Skill.PRAYER, 990);
            target.getSkillManager().setMaxLevel(Skill.RANGED, 99);
            target.getSkillManager().setMaxLevel(Skill.MAGIC, 99);
            target.getSkillManager().setMaxLevel(Skill.SUMMONING, 99);
            target.getSkillManager().setMaxLevel(Skill.DUNGEONEERING, 80);
            target.getSkillManager().setCurrentLevel(Skill.ATTACK, 99);
            target.getSkillManager().setCurrentLevel(Skill.DEFENCE, 99);
            target.getSkillManager().setCurrentLevel(Skill.STRENGTH, 99);
            target.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 990);
            target.getSkillManager().setCurrentLevel(Skill.PRAYER, 990);
            target.getSkillManager().setCurrentLevel(Skill.RANGED, 99);
            target.getSkillManager().setCurrentLevel(Skill.MAGIC, 99);
            target.getSkillManager().setCurrentLevel(Skill.SUMMONING, 99);
            target.getSkillManager().setCurrentLevel(Skill.DUNGEONEERING, 80);
            target.getSkillManager().setExperience(Skill.ATTACK, SkillManager.getExperienceForLevel(99));
            target.getSkillManager().setExperience(Skill.DEFENCE, SkillManager.getExperienceForLevel(99));
            target.getSkillManager().setExperience(Skill.STRENGTH,
                    SkillManager.getExperienceForLevel(99));
            target.getSkillManager().setExperience(Skill.CONSTITUTION,
                    SkillManager.getExperienceForLevel(99));
            target.getSkillManager().setExperience(Skill.PRAYER, SkillManager.getExperienceForLevel(99));
            target.getSkillManager().setExperience(Skill.RANGED, SkillManager.getExperienceForLevel(99));
            target.getSkillManager().setExperience(Skill.MAGIC, SkillManager.getExperienceForLevel(99));
            target.getSkillManager().setExperience(Skill.SUMMONING,
                    SkillManager.getExperienceForLevel(99));
            target.getSkillManager().setExperience(Skill.DUNGEONEERING,
                    SkillManager.getExperienceForLevel(80));
            target.getPacketSender().sendMessage("You are now a master of all combat skills.");
            target.getUpdateFlag().flag(Flag.APPEARANCE);
        }
        if (command[0].equals("master")) {
            for (Skill skill : Skill.values()) {
                int level = SkillManager.getMaxAchievingLevel(skill);
                player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level)
                        .setExperience(skill, SkillManager.getExperienceForLevel(level == 120 ? 120 : 99));
            }
            player.getPacketSender().sendMessage("You are now a master of all skills.");
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        }
        if (command[0].equals("setlevel") && !player.getUsername().equalsIgnoreCase("Jack")) {
            int skillId = Integer.parseInt(command[1]);
            int level = Integer.parseInt(command[2]);
            if (level > 15000) {
                player.getPacketSender().sendMessage("You can only have a maxmium level of 15000.");
                return;
            }
            Skill skill = Skill.forId(skillId);
            player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level)
                    .setExperience(skill, SkillManager.getExperienceForLevel(level));
            player.getPacketSender()
                    .sendMessage("You have set your " + skill.getName() + " level to " + level);
        }
        if (command[0].equals("item")) {
            int id = Integer.parseInt(command[1]);
            int amount = (command.length == 2 ? 1
                    : Integer.parseInt(command[2].trim().toLowerCase().replaceAll("k", "000")
                    .replaceAll("m", "000000").replaceAll("b", "000000000")));
            if (amount > Integer.MAX_VALUE) {
                amount = Integer.MAX_VALUE;
            }
            Item item = new Item(id, amount);
            player.getInventory().add(item, true);

            player.getPacketSender().sendItemOnInterface(47052, 11694, 1);
        }
        if (command[0].equals("reload")) {
            switch (command[1]) {
                case "itemdef":
                case "item_definition":
                case "item_definitions":
                    GameServer.getLoader().getEngine().submit(() -> {
                        ItemDefinition.init();
                    });
                    break;
            }
        }
        if (command[0].equals("spawn")) {
            String name =
                    wholeCommand.substring(6, wholeCommand.indexOf(":")).toLowerCase().replaceAll("_", " ");
            String[] what = wholeCommand.split(":");
            int amount_of = Integer.parseInt(what[1]);
            player.getPacketSender().sendMessage("Finding item id for item - " + name);
            boolean found2 = false;
            for (int i = 0; i < ItemDefinition.getMaxAmountOfItems(); i++) {
                if (found2)
                    break;
                if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
                    player.getInventory().add(i, amount_of);
                    player.getPacketSender().sendMessage(
                            "Spawned item [" + ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
                    found2 = true;
                }
            }
            if (!found2) {
                player.getPacketSender().sendMessage("No item with name [" + name + "] has been found!");
            }
            player.getPacketSender().sendItemOnInterface(47052, 11694, 1);
        }
        if (wholeCommand.toLowerCase().startsWith("yell")
                && player.getRights() == PlayerRights.PLAYER) {
            if (GameSettings.YELL_STATUS) {
                player.getPacketSender()
                        .sendMessage("Yell is currently turned off, please try again in 30 minutes!");
                return;
            }
            player.getPacketSender()
                    .sendMessage("Only members can yell. To become one, simply use ::store, buy a scroll")
                    .sendMessage("and then claim it.");
        }
        if (command[0].contains("pure")) {
            int[][] data = new int[][] {{Equipment.HEAD_SLOT, 1153}, {Equipment.CAPE_SLOT, 10499},
                    {Equipment.AMULET_SLOT, 1725}, {Equipment.WEAPON_SLOT, 4587}, {Equipment.BODY_SLOT, 1129},
                    {Equipment.SHIELD_SLOT, 1540}, {Equipment.LEG_SLOT, 2497}, {Equipment.HANDS_SLOT, 7459},
                    {Equipment.FEET_SLOT, 3105}, {Equipment.RING_SLOT, 2550},
                    {Equipment.AMMUNITION_SLOT, 9244}};
            for (int i = 0; i < data.length; i++) {
                int slot = data[i][0], id = data[i][1];
                player.getEquipment().setItem(slot, new Item(id, id == 9244 ? 500 : 1));
            }
            BonusManager.update(player);
            WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            player.getEquipment().refreshItems();
            player.getUpdateFlag().flag(Flag.APPEARANCE);
            player.getInventory().resetItems();
            player.getInventory().add(1216, 1000).add(9186, 1000).add(862, 1000).add(892, 10000)
                    .add(4154, 5000).add(2437, 1000).add(2441, 1000).add(2445, 1000).add(386, 1000)
                    .add(2435, 1000);
            player.getSkillManager().newSkillManager();
            player.getSkillManager().setMaxLevel(Skill.ATTACK, 60).setMaxLevel(Skill.STRENGTH, 85)
                    .setMaxLevel(Skill.RANGED, 85).setMaxLevel(Skill.PRAYER, 520).setMaxLevel(Skill.MAGIC, 70)
                    .setMaxLevel(Skill.CONSTITUTION, 850);
            for (Skill skill : Skill.values()) {
                player.getSkillManager().setCurrentLevel(skill, player.getSkillManager().getMaxLevel(skill))
                        .setExperience(skill,
                                SkillManager.getExperienceForLevel(player.getSkillManager().getMaxLevel(skill)));
            }
        }
        if (command[0].equals("emptyitem")) {
            if (player.getInterfaceId() > 0
                    || player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
                player.getPacketSender().sendMessage("You cannot do this at the moment.");
                return;
            }
            int item = Integer.parseInt(command[1]);
            int itemAmount = player.getInventory().getAmount(item);
            Item itemToDelete = new Item(item, itemAmount);
            player.getInventory().delete(itemToDelete).refreshItems();
        }
        if (command[0].equals("pray")) {
            player.getSkillManager().setCurrentLevel(Skill.PRAYER, 15000);
        }
        if (command[0].equals("cashineco")) {
            int gold = 0, plrLoops = 0;
            for (Player p : World.getPlayers()) {
                if (p != null) {
                    for (Item item : p.getInventory().getItems()) {
                        if (item != null && item.getId() > 0 && item.tradeable())
                            gold += item.getDefinition().getValue();
                    }
                    for (Item item : p.getEquipment().getItems()) {
                        if (item != null && item.getId() > 0 && item.tradeable())
                            gold += item.getDefinition().getValue();
                    }
                    for (int i = 0; i < 9; i++) {
                        for (Item item : player.getBank(i).getItems()) {
                            if (item != null && item.getId() > 0 && item.tradeable())
                                gold += item.getDefinition().getValue();
                        }
                    }
                    gold += p.getMoneyInPouch();
                    plrLoops++;
                }
            }
            player.getPacketSender().sendMessage("Total gold in economy right now: " + gold
                    + ", went through " + plrLoops + " players items.");
        }
        if (command[0].equals("bank")) {
            player.getBank(player.getCurrentBankTab()).open();
        }
        if (command[0].equals("find")) {
            String name = wholeCommand.substring(5).toLowerCase().replaceAll("_", " ");
            player.getPacketSender().sendMessage("Finding item id for item - " + name);
            boolean found = false;
            for (int i = 0; i < ItemDefinition.getMaxAmountOfItems(); i++) {
                if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
                    player.getPacketSender().sendMessage("Found item with name ["
                            + ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
                    found = true;
                }
            }
            if (!found) {
                player.getPacketSender().sendMessage("No item with name [" + name + "] has been found!");
            }
        }
        if (command[0].equals("maxcb")) {
            Player target = World.getPlayerByName(command[1]);
            target.getSkillManager().setMaxLevel(Skill.ATTACK, 99);
            target.getSkillManager().setMaxLevel(Skill.DEFENCE, 99);
            target.getSkillManager().setMaxLevel(Skill.STRENGTH, 99);
            target.getSkillManager().setMaxLevel(Skill.CONSTITUTION, 99);
            target.getSkillManager().setMaxLevel(Skill.PRAYER, 99);
            target.getSkillManager().setMaxLevel(Skill.RANGED, 99);
            target.getSkillManager().setMaxLevel(Skill.MAGIC, 99);
            target.getSkillManager().setMaxLevel(Skill.SUMMONING, 99);
            target.getSkillManager().setMaxLevel(Skill.DUNGEONEERING, 80);
            target.getSkillManager().setCurrentLevel(Skill.ATTACK, 99);
            target.getSkillManager().setCurrentLevel(Skill.DEFENCE, 99);
            target.getSkillManager().setCurrentLevel(Skill.STRENGTH, 99);
            target.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 99);
            target.getSkillManager().setCurrentLevel(Skill.PRAYER, 99);
            target.getSkillManager().setCurrentLevel(Skill.RANGED, 99);
            target.getSkillManager().setCurrentLevel(Skill.MAGIC, 99);
            target.getSkillManager().setCurrentLevel(Skill.SUMMONING, 99);
            target.getSkillManager().setCurrentLevel(Skill.DUNGEONEERING, 80);
            target.getPacketSender().sendMessage("You are now a master of all combat skills.");
            target.getUpdateFlag().flag(Flag.APPEARANCE);
        }
        if (command[0].equals("spec")) {
            player.setSpecialPercentage(100);
            CombatSpecial.updateBar(player);
        }
        if (command[0].equals("runes")) {
            for (Item t : ShopManager.getShops().get(0).getItems()) {
                if (t != null) {
                    player.getInventory().add(new Item(t.getId(), 200000));
                }
            }
        }
        if (command[0].contains("gear")) {
            int[][] data = wholeCommand.contains("jack")
                    ? new int[][] {{Equipment.HEAD_SLOT, 1050}, {Equipment.CAPE_SLOT, 12170},
                    {Equipment.AMULET_SLOT, 15126}, {Equipment.WEAPON_SLOT, 15444},
                    {Equipment.BODY_SLOT, 14012}, {Equipment.SHIELD_SLOT, 13740},
                    {Equipment.LEG_SLOT, 14013}, {Equipment.HANDS_SLOT, 7462},
                    {Equipment.FEET_SLOT, 11732}, {Equipment.RING_SLOT, 15220}}
                    : wholeCommand.contains("range")
                    ? new int[][] {{Equipment.HEAD_SLOT, 3749}, {Equipment.CAPE_SLOT, 10499},
                    {Equipment.AMULET_SLOT, 15126}, {Equipment.WEAPON_SLOT, 18357},
                    {Equipment.BODY_SLOT, 2503}, {Equipment.SHIELD_SLOT, 13740},
                    {Equipment.LEG_SLOT, 2497}, {Equipment.HANDS_SLOT, 7462},
                    {Equipment.FEET_SLOT, 11732}, {Equipment.RING_SLOT, 15019},
                    {Equipment.AMMUNITION_SLOT, 9244},}
                    : new int[][] {{Equipment.HEAD_SLOT, 1163}, {Equipment.CAPE_SLOT, 19111},
                    {Equipment.AMULET_SLOT, 6585}, {Equipment.WEAPON_SLOT, 4151},
                    {Equipment.BODY_SLOT, 1127}, {Equipment.SHIELD_SLOT, 13262},
                    {Equipment.LEG_SLOT, 1079}, {Equipment.HANDS_SLOT, 7462},
                    {Equipment.FEET_SLOT, 11732}, {Equipment.RING_SLOT, 2550}};
            for (int i = 0; i < data.length; i++) {
                int slot = data[i][0], id = data[i][1];
                player.getEquipment().setItem(slot, new Item(id, id == 9244 ? 500 : 1));
            }
            BonusManager.update(player);
            WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            player.getEquipment().refreshItems();
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        }
        if (wholeCommand.equals("afk")) {
            World.sendMessage("<img=4> <col=FF0000><shad=0>" + player.getUsername()
                    + ": I am now away, please don't message me; I won't reply.");
        }
        if (wholeCommand.equals("sfs")) {
            Lottery.restartLottery();
        }
        if (command[0].equals("giveitem")) {
            int item = Integer.parseInt(command[1]);
            int amount = Integer.parseInt(command[2]);
            String rss = command[3];
            if (command.length > 4)
                rss += " " + command[4];
            if (command.length > 5)
                rss += " " + command[5];
            Player target = World.getPlayerByName(rss);
            if (target == null) {
                player.getPacketSender().sendMessage("Player must be online to give them stuff!");
            } else {
                player.getPacketSender().sendMessage("Gave player gold.");
                target.getInventory().add(item, amount);
            }
        }
        if (command[0].equals("update")) {
            int time = Integer.parseInt(command[1]);
            if (time > 0) {
                GameServer.setUpdating(true);
                for (Player players : World.getPlayers()) {
                    if (players == null)
                        continue;
                    players.getPacketSender().sendSystemUpdate(time);
                    if (Dungeoneering.doingDungeoneering(players)) {
                        Dungeoneering.leave(players, false, true);
                        players.getClickDelay().reset();
                        players.getPacketSender()
                                .sendMessage("You have been forced out of your dungeon due to an update, sorry!");
                    }
                }
                TaskManager.submit(new Task(time) {
                    @Override
                    protected void execute() {
                        for (Player player : World.getPlayers()) {
                            if (player != null) {
                                World.deregister(player);
                            }
                        }
                        WellOfGoodwill.save();
                        GrandExchangeOffers.save();
                        PlayerOwnedShops.save();
                        ClanChatManager.save();
                        GameServer.getLogger().info("Update task finished!");
                        stop();
                    }
                });
            }
        }
        if (command[0].equals("sendstring")) {
            int child = Integer.parseInt(command[1]);
            String string = command[2];
            player.getPacketSender().sendString(child, string);
        }
        if (command[0].equals("testzulrah")) {
            player.getPacketSender().sendScreenFade(1, 5);
            TaskManager.submit(new Task(2, player, true) {
                int tick = 0;

                @Override
                public void execute() {
                    if (tick == 4) {
                        Zulrah.enter_pit(player);
                        stop();
                    }
                    tick++;
                }
            });
        }
        if (command[0].equals("tasks")) {
            player.getPacketSender().sendMessage("Found " + TaskManager.getTaskAmount() + " tasks.");
        }
        if (command[0].equals("memory")) {
            long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            player.getPacketSender()
                    .sendMessage("Heap usage: " + Misc.insertCommasToNumber("" + used + "") + " bytes!");
        }
        if (command[0].equals("star")) {
            ShootingStar.despawn(true);
            player.getPacketSender().sendMessage("star method called.");
        }
        if (command[0].equals("tree")) {
            EvilTrees.despawn(true);
            player.getPacketSender().sendMessage("star method called.");
        }
        if (command[0].equals("save")) {
            player.save();
        }
        if (command[0].equals("v1")) {
            World.sendMessage(
                    "<img=4> <col=008FB2>Another 20 voters have been rewarded! Vote now using the ::vote command!");
        }
        if (command[0].equals("test")) {
            player.getSkillManager().addExperience(Skill.FARMING, 500);
        }
        if (command[0].equalsIgnoreCase("frame")) {
            int frame = Integer.parseInt(command[1]);
            String text = command[2];
            player.getPacketSender().sendString(frame, text);
        }
        if (command[0].equals("pos")) {
            player.getPacketSender().sendMessage(player.getPosition().toString());
        }
        if (command[0].equals("npc")) {
            int id = Integer.parseInt(command[1]);
            NPC npc = new NPC(id, new Position(player.getPosition().getX(), player.getPosition().getY(),
                    player.getPosition().getZ()));
            World.register(npc);
            npc.setConstitution(20000);
            player.getPacketSender().sendEntityHint(npc);
      /*
       * TaskManager.submit(new Task(5) {
       * @Override protected void execute() { npc.moveTo(new Position(npc.getPosition().getX() + 2,
       * npc.getPosition().getY() + 2)); player.getPacketSender().sendEntityHintRemoval(false);
       * stop(); } });
       */
            // npc.getMovementCoordinator().setCoordinator(new
            // Coordinator().setCoordinate(true).setRadius(5));
        }
        if (command[0].equals("fillinv")) {
            for (int i = 0; i < 28; i++) {
                int it = Misc.getRandom(10000);
                player.getInventory().add(it, 1);
            }
        }
        if (command[0].equals("playnpc")) {
            player.setNpcTransformationId(Integer.parseInt(command[1]));
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        } else if (command[0].equals("playobject")) {
            player.getPacketSender().sendObjectAnimation(
                    new GameObject(2283, player.getPosition().copy()), new Animation(751));
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        }
        if (command[0].equals("interface")) {
            int id = Integer.parseInt(command[1]);
            player.getPacketSender().sendInterface(id);
        }
        if (command[0].equals("walkableinterface")) {
            int id = Integer.parseInt(command[1]);
            player.getPacketSender().sendWalkableInterface(id);
        }
        if (command[0].equals("anim")) {
            int id = Integer.parseInt(command[1]);
            player.performAnimation(new Animation(id));
            player.getPacketSender().sendMessage("Sending animation: " + id);
        }
        if (command[0].equals("gfx")) {
            int id = Integer.parseInt(command[1]);
            player.performGraphic(new Graphic(id));
            player.getPacketSender().sendMessage("Sending graphic: " + id);
        }
        if (command[0].equals("object")) {
            int id = Integer.parseInt(command[1]);
            player.getPacketSender().sendObject(new GameObject(id, player.getPosition(), 10, 3));
            player.getPacketSender().sendMessage("Sending object: " + id);
        }
        if (command[0].equals("config")) {
            int id = Integer.parseInt(command[1]);
            int state = Integer.parseInt(command[2]);
            player.getPacketSender().sendConfig(id, state).sendMessage("Sent config.");
        }

        if (command[0].equals("hairytesticle")) {
            player.getPacketSender().sendMessage("My dick is soft");
        }
        if (command[0].equals("checkbank")) {
            Player plr = World.getPlayerByName(wholeCommand.substring(10));
            if (plr != null) {
                player.getPacketSender().sendMessage("Loading bank..");
                for (Bank b : player.getBanks()) {
                    if (b != null) {
                        b.resetItems();
                    }
                }
                for (int i = 0; i < plr.getBanks().length; i++) {
                    for (Item it : plr.getBank(i).getItems()) {
                        if (it != null) {
                            player.getBank(i).add(it, false);
                        }
                    }
                }
                player.getBank(0).open();
            } else {
                player.getPacketSender().sendMessage("Player is offline!");
            }
        }
        if (command[0].equals("enterhouse")) {
            DialogueManager.start(player, ConstructionDialogues.mainPortalDialogue());
            player.setDialogueActionId(442);
        }
        if (command[0].equals("createhouse")) {
            player.getHouseRooms()[0][0][0] = null;
            Construction.newHouse(player);
            player.getPacketSender().sendConsoleMessage("House created");
        }
        if (command[0].equals("checkinv")) {
            Player player2 = World.getPlayerByName(wholeCommand.substring(9));
            if (player2 == null) {
                player.getPacketSender().sendMessage("Cannot find that player online..");
                return;
            }
            player.getInventory().setItems(player2.getInventory().getCopiedItems()).refreshItems();
        }
        if (command[0].equals("checkequip")) {
            Player player2 = World.getPlayerByName(wholeCommand.substring(11));
            if (player2 == null) {
                player.getPacketSender().sendMessage("Cannot find that player online..");
                return;
            }
            player.getEquipment().setItems(player2.getEquipment().getCopiedItems()).refreshItems();
            WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            BonusManager.update(player);
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        }
    }

}
