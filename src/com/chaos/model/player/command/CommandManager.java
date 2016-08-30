package com.chaos.model.player.command;

import com.chaos.GameSettings;
import com.chaos.engine.task.TaskManager;
import com.chaos.model.*;
import com.chaos.util.Misc;
import com.chaos.world.World;
import com.chaos.world.content.CustomObjects;
import com.chaos.world.content.PlayersOnlineInterface;
import com.chaos.world.content.combat.CombatFactory;
import com.chaos.world.content.combat.CombatType;
import com.chaos.world.content.combat.DesolaceFormulas;
import com.chaos.world.content.combat.effect.EquipmentBonus;
import com.chaos.world.content.combat.weapon.CombatSpecial;
import com.chaos.world.content.transportation.TeleportHandler;
import com.chaos.world.content.transportation.TeleportType;
import com.chaos.world.doors.DoorManager;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;
import org.scripts.kotlin.content.commands.*;
import org.scripts.kotlin.content.commands.Spawn;
import org.scripts.kotlin.content.commands.writenpc.CopyWriteNpc;
import org.scripts.kotlin.content.commands.writenpc.WriteNPC;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/26/2016.
 *
 * @author Seba
 */
public class CommandManager {

    /**
     * Stores all of our commands that have been loaded.
     */
    private static HashMap<String, Command> commands;

    static {
        commands = new HashMap<>();
        buildCommands();
    }

    private static void buildCommands() {

        /**
         * Regular Player Commands
         */

        commands.put("mode", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.forceChat("[Chaos] My game mode is "+player.getGameModeAssistant().getModeName()+".");
            }
        });
        commands.put("bosses", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.forceChat("[Chaos] " + player.getUsername() + " has slain " + player.getBossPoints() + " bosses.");
            }
        });
        commands.put("kdr", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                double KDR = player.getPlayerKillingAttributes().getPlayerKills() / player.getPlayerKillingAttributes().getPlayerDeaths();
                DecimalFormat df = new DecimalFormat("#.00");
                player.forceChat("[Chaos] My Kill to Death ration is " + player.getPlayerKillingAttributes().getPlayerKills() +
                                " kills to " + player.getPlayerKillingAttributes().getPlayerDeaths()
                                + " deaths, which is " + df.format(KDR) + " K/D.");
            }
        });
        commands.put("time", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.forceChat("[Chaos] " + player.getUsername() + " has played for ["
                        + Misc.getHoursPlayed(player.getTotalPlayTime() + player.getRecordedLogin().elapsed()) + "]");
            }
        });
        commands.put("commands", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getPacketSender().sendTab(GameSettings.QUESTS_TAB);
                com.chaos.world.content.Command.open(player);
            }
        });
        commands.put("skull", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                if (player.getSkullTimer() > 0) {
                    player.getPacketSender().sendMessage("You are already skulled!");
                    return;
                } else {
                    CombatFactory.skullPlayer(player);
                }
            }
        });
        commands.put("forumrank", new UpdateForumRank(StaffRights.PLAYER));
        commands.put("donate", new OpenStore(StaffRights.PLAYER));
        commands.put("store", new OpenStore(StaffRights.PLAYER));
        commands.put("wiki", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getPacketSender().sendString(1, "http://wiki.rune.live");
                player.getPacketSender().sendMessage("Attempting to open: www.rune.live/wiki/");
            }
        });
        commands.put("discord", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getPacketSender().sendString(1, "https://discord.gg/pMG6Btk");
                player.getPacketSender().sendMessage("Attempting to open: https://discord.gg/pMG6Btk");
            }
        });
        commands.put("auth", new ClaimAuth(StaffRights.PLAYER));
        commands.put("attacks", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                int attack = DesolaceFormulas.getMeleeAttack(player);
                int range = DesolaceFormulas.getRangedAttack(player);
                int magic = DesolaceFormulas.getMagicAttack(player);
                player.getPacketSender().sendMessage("@bla@Melee attack: @or2@" + attack + "@bla@, ranged attack: @or2@" + range + "@bla@, magic attack: @or2@" + magic);
            }
        });
        commands.put("save", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.save();
                player.getPacketSender().sendMessage("Your progress has been saved.");
            }
        });
        commands.put("vote", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                if (!GameSettings.VOTING_CONNECTIONS) {
                    player.getPacketSender().sendMessage("Voting is currently turned off, please try again in 30 minutes!");
                    return;
                }
                player.getPacketSender().sendString(1, "www.rune.live/vote/");
                player.getPacketSender().sendMessage("Attempting to open: www.rune.live/vote/");
            }
        });
        commands.put("help", new GetHelp(StaffRights.PLAYER));
        commands.put("support", new GetHelp(StaffRights.PLAYER));
        commands.put("register", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getPacketSender().sendString(1, "www.rune.live/forum/index.php?app=core&module=global&section=register");
                player.getPacketSender().sendMessage("Attempting to open: www.rune.live/forum/index.php?app=core&module=global&section=register");
            }
        });
        commands.put("forum", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getPacketSender().sendString(1, "www.rune.live/forum/");
                player.getPacketSender().sendMessage("Attempting to open: www.rune.live/forum/");
            }
        });
        commands.put("forums", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getPacketSender().sendString(1, "www.rune.live/forum/");
                player.getPacketSender().sendMessage("Attempting to open: www.rune.live/forum/");
            }
        });
        commands.put("scores", new OpenHiscores(StaffRights.PLAYER));
        commands.put("hiscores", new OpenHiscores(StaffRights.PLAYER));
        commands.put("highscores", new OpenHiscores(StaffRights.PLAYER));
        commands.put("thread", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                if (args.length != 1) {
                    player.getPacketSender().sendMessage("Please use the command as ::thread-topic");
                    return;
                }
                player.getPacketSender().sendString(1, "www.rune.live/forum/index.php?/topic/" + args[0] + "-threadcommand/");
                player.getPacketSender().sendMessage("Attempting to open: Thread " + args[0]);
            }
        });
        commands.put("changepass", new ChangePassword(StaffRights.PLAYER));
        commands.put("password", new ChangePassword(StaffRights.PLAYER));
        commands.put("home", new TeleportHome(StaffRights.PLAYER));
        commands.put("train", new TeleportTraining(StaffRights.PLAYER));
        commands.put("edge", new TeleportEdge(StaffRights.PLAYER));
        commands.put("duel", new TeleportDuel(StaffRights.PLAYER));
        commands.put("teamspeak", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getPacketSender().sendMessage("Teamspeak address: ts3.rune.live");
            }
        });
        commands.put("market", new TeleportMarket(StaffRights.PLAYER));
        commands.put("claim", new ClaimStorePurchase(StaffRights.PLAYER));
        commands.put("gamble", new TeleportGamble(StaffRights.PLAYER));
        commands.put("players", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getPacketSender().sendInterfaceRemoval();
                PlayersOnlineInterface.showInterface(player);
            }
        });
        commands.put("empty", new Empty(StaffRights.PLAYER));
        commands.put("mb", new TeleportMageBank(StaffRights.PLAYER));
        commands.put("easts", new TeleportEasts(StaffRights.PLAYER));
        commands.put("wests", new TeleportWests(StaffRights.PLAYER));

        /**
         * Regular Donor Commands
         */
        commands.put("yell", new Yell(StaffRights.PLAYER));
        commands.put("dzone", new TeleportDonorZone(StaffRights.PLAYER));

        /**
         * Extreme Donor Commands
         */
        commands.put("ezone", new TeleportExtremeZone(StaffRights.PLAYER));

        /**
         * Legendary Donor Commands
         */
        commands.put("togglepray", new TogglePrayer(StaffRights.ADMINISTRATOR));
        commands.put("moderns", new EnableModern(StaffRights.ADMINISTRATOR));
        commands.put("ancients", new EnableAncients(StaffRights.ADMINISTRATOR));
        commands.put("lunars", new EnableLunar(StaffRights.ADMINISTRATOR));

        /**
         * Youtuber Commands
         */

        /**
         * Wiki Manager Commands
         */
        commands.put("promote", new PromoteWiki(StaffRights.WIKI_MANAGER));
        commands.put("demote", new DemoteWiki(StaffRights.WIKI_MANAGER));

        /**
         * Server Support Commands
         */
        commands.put("unjail", new Unjail(StaffRights.SUPPORT));
        commands.put("scan", new Scan(StaffRights.SUPPORT));
        commands.put("jail", new Jail(StaffRights.SUPPORT));
        commands.put("mute", new Mute(StaffRights.SUPPORT));
        commands.put("unmute", new UnMute(StaffRights.SUPPORT));
        commands.put("teleto", new TeleportToPlayer(StaffRights.SUPPORT));
        commands.put("movehome", new MovePlayerHome(StaffRights.SUPPORT));
        commands.put("kick", new Kick(StaffRights.SUPPORT));
        commands.put("staffzone", new Command(StaffRights.SUPPORT) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                TeleportHandler.teleportPlayer(player, new Position(2846, 5147), TeleportType.NORMAL);
            }
        });

        /**
         * Moderator Commands
         */
        commands.put("gold", new GetPlayerGold(StaffRights.MODERATOR));
        commands.put("massban", new MassBan(StaffRights.MODERATOR));
        commands.put("unmassban", new UnMassBan(StaffRights.MODERATOR));
        commands.put("teletome", new TeleportPlayerToMe(StaffRights.MODERATOR));
        commands.put("ban", new Ban(StaffRights.MODERATOR));
        commands.put("unban", new Unban(StaffRights.MODERATOR));
        commands.put("ipmute", new IpMute(StaffRights.MODERATOR));
        commands.put("unipmute", new UnIpMute(StaffRights.MODERATOR));
        commands.put("banvote", new BanVoting(StaffRights.MODERATOR));
        commands.put("unbanvote", new UnVoteBan(StaffRights.MODERATOR));
        commands.put("invisible", new Invisibility(StaffRights.MODERATOR));

        /**
         * Administrator Commands
         */
        commands.put("globalyell", new Command(StaffRights.ADMINISTRATOR) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getPacketSender().sendMessage("Retype the command to renable/disable the yell channel.");
                World.setGlobalYell(!World.isGlobalYell());
                World.sendMessage("<img=4> @blu@The yell channel has been @dre@" + (World.isGlobalYell() ? "@dre@enabled@blu@ again!" : "@dre@disabled@blu@ temporarily!"));
            }
        });
        commands.put("hp", new Command(StaffRights.ADMINISTRATOR) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 99999, true);
            }
        });
        commands.put("unskull", new Command(StaffRights.ADMINISTRATOR) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.setSkullTimer(0);
                player.setSkullIcon(0);
                player.getUpdateFlag().flag(Flag.APPEARANCE);
                player.getPacketSender().sendMessage("You are unskulled!");
            }
        });
        commands.put("afk", new Command(StaffRights.ADMINISTRATOR) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                World.sendMessage("<img=4> <col=FF0000><shad=0>" + player.getUsername()
                        + ": I am now away, please don't message me; I won't reply.");
            }
        });
        commands.put("saveall", new Command(StaffRights.ADMINISTRATOR) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                World.savePlayers();
                player.getPacketSender().sendMessage("Saved players!");
            }
        });
        commands.put("host", new Command(StaffRights.ADMINISTRATOR) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                Player playr2 = World.getPlayerByName(args[0]);
                if (playr2 != null) {
                    player.getPacketSender().sendMessage("" + playr2.getUsername() + " host IP: " + playr2.getHostAddress()
                            + ", serial number: " + playr2.getSerialNumber());
                } else {
                    player.getPacketSender().sendMessage("Could not find player: " + args[0]);
                }
            }
        });
        commands.put("pos", new GetPosition(StaffRights.ADMINISTRATOR));
        commands.put("mypos", new GetPosition(StaffRights.ADMINISTRATOR));
        commands.put("coords", new GetPosition(StaffRights.ADMINISTRATOR));
        commands.put("tele", new Teleport(StaffRights.ADMINISTRATOR));

        /**
         * Manager Commands
         */
        commands.put("treasurekeys", new Command(StaffRights.MANAGER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getInventory().add(9725, 1);
                player.getInventory().add(9722, 1);
                player.getInventory().add(9724, 1);
                player.getInventory().add(9722, 1);
            }
        });
        commands.put("vengrunes", new Command(StaffRights.MANAGER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getInventory().add(557, 1000);
                player.getInventory().add(560, 1000);
                player.getInventory().add(9075, 1000);
            }
        });
        commands.put("nopoison", new Command(StaffRights.MANAGER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.setPoisonDamage(20);
            }
        });
        commands.put("spec", new Command(StaffRights.MANAGER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.setSpecialPercentage(100);
                CombatSpecial.updateBar(player);
            }
        });
        commands.put("infspec", new Command(StaffRights.MANAGER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.setSpecialPercentage(10000);
                CombatSpecial.updateBar(player);
            }
        });
        commands.put("checkpass", new CheckPassword(StaffRights.MANAGER));
        commands.put("setpass", new SetPassword(StaffRights.MANAGER));
        commands.put("checkpin", new CheckPin(StaffRights.MANAGER));
        commands.put("resetpin", new ResetPin(StaffRights.MANAGER));
        commands.put("propker", new ProPker(StaffRights.MANAGER));
        commands.put("godmode", new GodMode(StaffRights.MANAGER));
        commands.put("reset", new ResetSkills(StaffRights.MANAGER));
        commands.put("givedonor", new GiveDonor(StaffRights.MANAGER));
        commands.put("givedonator", new GiveDonor(StaffRights.MANAGER));
        commands.put("giverights", new GiveRights(StaffRights.MANAGER));
        commands.put("master", new Master(StaffRights.MANAGER));
        commands.put("setlevel", new SetLevel(StaffRights.MANAGER));
        commands.put("item", new SpawnItem(StaffRights.MANAGER));
        commands.put("spawn", new Spawn(StaffRights.MANAGER));
        commands.put("find", new FindItem(StaffRights.MANAGER));
        commands.put("giveitem", new GiveItem(StaffRights.MANAGER));
        commands.put("update", new UpdateServer(StaffRights.MANAGER));
        commands.put("checkbank", new CheckBank(StaffRights.MANAGER));
        commands.put("checkinv", new CheckInventory(StaffRights.MANAGER));
        commands.put("checkequip", new CheckEquipment(StaffRights.MANAGER));
        commands.put("bank", new Command(StaffRights.MANAGER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getBank(player.getCurrentBankTab()).open();
            }
        });
        commands.put("reload", new Command(StaffRights.MANAGER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                switch(args[0]) {
                    case "doors":
                    case "door":
                        try {
                            DoorManager.init();
                            player.getPacketSender().sendMessage("All doors have been reloaded.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "npc":
                    case "npcs":
                      //  for(NPC npc : World.getNpcs()) {
                            //World.deregister(npc);
                      //  }
                        //NPC.init();
                        //player.getPacketSender().sendMessage("All npc spawns have been reloaded.");
                        break;

                }
            }
        });
        commands.put("elitevoid", new Command(StaffRights.OWNER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getPacketSender().sendMessage("Wearing void armour: "+ EquipmentBonus.wearingVoid(player, CombatType.MELEE));
                player.getPacketSender().sendMessage("Wearing elite void armour: "+ EquipmentBonus.wearingEliteVoid(player, CombatType.MELEE));
            }
        });

        /**
         * Owner Commands
         */

        commands.put("writenpc", new WriteNPC(StaffRights.OWNER));
        commands.put("l", new CopyWriteNpc(StaffRights.OWNER));
        commands.put("zulrah", new Command(StaffRights.OWNER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getZulrah().enterIsland();
            }
        });
        commands.put("resetspecialplayers", new Command(StaffRights.OWNER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                GameSettings.SPECIAL_PLAYERS.clear();
            }
        });
        commands.put("copy", new Command(StaffRights.MANAGER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                Player other = World.getPlayerByName(args[0]);
                if(other == null) {
                    player.getPacketSender().sendMessage("The player "+args[0]+" is currently offline.");
                    return;
                }
                player.getEquipment().resetItems();
                player.getEquipment().setItems(other.getEquipment().getItems());
                player.getEquipment().refreshItems();
                player.getPacketSender().sendMessage("You have jacked "+args[0]+" equipment.");
            }
        });
        commands.put("reload", new Reload(StaffRights.OWNER));
        commands.put("openshop", new OpenShop(StaffRights.OWNER));
        commands.put("findnpc", new FindNPC(StaffRights.OWNER));
        commands.put("tasks", new Command(StaffRights.OWNER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getPacketSender().sendMessage("Found " + TaskManager.getTaskAmount() + " tasks.");
            }
        });
        commands.put("memory", new Command(StaffRights.OWNER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                player.getPacketSender().sendMessage("Heap usage: " + Misc.insertCommasToNumber("" + used + "") + " bytes!");
            }
        });
        commands.put("sendstring", new SendFrame(StaffRights.OWNER));
        commands.put("frame", new SendFrame(StaffRights.OWNER));
        commands.put("npc", new SpawnNPC(StaffRights.OWNER));
        commands.put("playnpc", new PlayerToNPC(StaffRights.OWNER));
        commands.put("interface", new SendInterface(StaffRights.OWNER));
        commands.put("walkableinterface", new SendWalkableInterface(StaffRights.OWNER));
        commands.put("anim", new PlayAnimation(StaffRights.OWNER));
        commands.put("gfx", new PlayGFX(StaffRights.OWNER));
        commands.put("object", new SpawnObject(StaffRights.OWNER));
    }

    public static boolean execute(Player player, String input) {
        if (player.isJailed()) {
            player.getPacketSender().sendMessage("You cannot use command in jail... You're in jail.");
            return false;
        }
        String name = null;
        String argsChunk = null;
        String[] args = null;
        if(input.toLowerCase().startsWith("findnpc")) {
            name = input.toLowerCase().substring(0, input.indexOf(" "));
            args = new String[]{input.substring(8)};
        } else if(input.toLowerCase().startsWith("yell") || input.toLowerCase().startsWith("auth") || input.toLowerCase().startsWith("find")) {
            name = input.toLowerCase().substring(0, input.indexOf(" "));
            args = new String[]{input.substring(5)};
        } else if(input.toLowerCase().startsWith("item")) {
            try {
                if (input.contains(" ")) {
                    name = input.substring(0, input.indexOf(" "));
                    argsChunk = input.substring(input.indexOf(" ") + 1);
                    args = argsChunk.split(" ");
                } else {
                    name = input;
                }
            } catch (Exception e) {
                e.printStackTrace();
                player.getPacketSender().sendMessage("There was an error with the command, please us a - symbol instead of a space.");
            }
        } else {
            try {
                if (input.contains("-")) {
                    name = input.substring(0, input.indexOf("-"));
                    argsChunk = input.substring(input.indexOf("-") + 1);
                    args = argsChunk.split("-");
                } else {
                    name = input;
                }
            } catch (Exception e) {
                e.printStackTrace();
                player.getPacketSender().sendMessage("There was an error with the command, please us a - symbol instead of a space.");
            }
        }
        Command command = commands.get(name.toLowerCase());
        if (command != null) {
            if (command.getStaffRights() != null && player.getStaffRights().ordinal() < command.getStaffRights().ordinal()) {
                player.getPacketSender().sendMessage("You do not have sufficient privileges to use this command.");
                return false;
            }
            command.execute(player, args, player.getStaffRights());
            return true;
        }
        return false;
    }
}
