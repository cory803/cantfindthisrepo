package com.runelive.model.player.command;

import com.runelive.GameSettings;
import com.runelive.engine.task.TaskManager;
import com.runelive.model.*;
import com.runelive.util.Misc;
import com.runelive.world.World;
import com.runelive.world.content.PlayersOnlineInterface;
import com.runelive.world.content.combat.CombatFactory;
import com.runelive.world.content.combat.DesolaceFormulas;
import com.runelive.world.content.combat.weapon.CombatSpecial;
import com.runelive.world.content.transportation.TeleportHandler;
import com.runelive.world.content.transportation.TeleportType;
import com.runelive.world.entity.impl.player.Player;
import org.scripts.kotlin.content.commands.*;
import org.scripts.kotlin.content.commands.Spawn;

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

        commands.put("mode", new Command(PlayerRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                player.forceChat("[RuneLive] My game mode is "+player.getGameModeAssistant().getModeName()+".");
            }
        });
        commands.put("bosses", new Command(PlayerRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                player.forceChat("[RuneLive] " + player.getUsername() + " has slain " + player.getBossPoints() + " bosses.");
            }
        });
        commands.put("kdr", new Command(PlayerRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                double KDR = player.getPlayerKillingAttributes().getPlayerKills() / player.getPlayerKillingAttributes().getPlayerDeaths();
                DecimalFormat df = new DecimalFormat("#.00");
                player.forceChat("[RuneLive] My Kill to Death ration is " + player.getPlayerKillingAttributes().getPlayerKills() +
                                " kills to " + player.getPlayerKillingAttributes().getPlayerDeaths()
                                + " deaths, which is " + df.format(KDR) + " K/D.");
            }
        });
        commands.put("time", new Command(PlayerRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                player.forceChat("[RuneLive] " + player.getUsername() + " has played for ["
                        + Misc.getHoursPlayed(player.getTotalPlayTime() + player.getRecordedLogin().elapsed()) + "]");
            }
        });
        commands.put("commands", new Command(PlayerRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                if (player.getLocation() == Locations.Location.DUNGEONEERING) {
                    player.getPacketSender().sendMessage("You cannot open the commands in dungeoneering.");
                    return;
                }
                player.getPacketSender().sendTab(GameSettings.QUESTS_TAB);
                com.runelive.world.content.Command.open(player);
            }
        });
        commands.put("skull", new Command(PlayerRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                if (player.getSkullTimer() > 0) {
                    player.getPacketSender().sendMessage("You are already skulled!");
                    return;
                } else {
                    CombatFactory.skullPlayer(player);
                }
            }
        });
        commands.put("forumrank", new UpdateForumRank(PlayerRights.PLAYER));
        commands.put("donate", new OpenStore(PlayerRights.PLAYER));
        commands.put("store", new OpenStore(PlayerRights.PLAYER));
        commands.put("wiki", new Command(PlayerRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                player.getPacketSender().sendString(1, "http://wiki.rune.live");
                player.getPacketSender().sendMessage("Attempting to open: www.rune.live/wiki/");
            }
        });
        commands.put("discord", new Command(PlayerRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                player.getPacketSender().sendString(1, "https://discord.gg/pMG6Btk");
                player.getPacketSender().sendMessage("Attempting to open: https://discord.gg/pMG6Btk");
            }
        });
        commands.put("auth", new ClaimAuth(PlayerRights.PLAYER));
        commands.put("attacks", new Command(PlayerRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                int attack = DesolaceFormulas.getMeleeAttack(player);
                int range = DesolaceFormulas.getRangedAttack(player);
                int magic = DesolaceFormulas.getMagicAttack(player);
                player.getPacketSender().sendMessage("@bla@Melee attack: @or2@" + attack + "@bla@, ranged attack: @or2@" + range + "@bla@, magic attack: @or2@" + magic);
            }
        });
        commands.put("save", new Command(PlayerRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                player.save();
                player.getPacketSender().sendMessage("Your progress has been saved.");
            }
        });
        commands.put("vote", new Command(PlayerRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                if (!GameSettings.VOTING_CONNECTIONS) {
                    player.getPacketSender().sendMessage("Voting is currently turned off, please try again in 30 minutes!");
                    return;
                }
                player.getPacketSender().sendString(1, "www.rune.live/vote/");
                player.getPacketSender().sendMessage("Attempting to open: www.rune.live/vote/");
            }
        });
        commands.put("help", new GetHelp(PlayerRights.PLAYER));
        commands.put("support", new GetHelp(PlayerRights.PLAYER));
        commands.put("register", new Command(PlayerRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                player.getPacketSender().sendString(1, "www.rune.live/forum/index.php?app=core&module=global&section=register");
                player.getPacketSender().sendMessage("Attempting to open: www.rune.live/forum/index.php?app=core&module=global&section=register");
            }
        });
        commands.put("forum", new Command(PlayerRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                player.getPacketSender().sendString(1, "www.rune.live/forum/");
                player.getPacketSender().sendMessage("Attempting to open: www.rune.live/forum/");
            }
        });
        commands.put("forums", new Command(PlayerRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                player.getPacketSender().sendString(1, "www.rune.live/forum/");
                player.getPacketSender().sendMessage("Attempting to open: www.rune.live/forum/");
            }
        });
        commands.put("scores", new OpenHiscores(PlayerRights.PLAYER));
        commands.put("hiscores", new OpenHiscores(PlayerRights.PLAYER));
        commands.put("highscores", new OpenHiscores(PlayerRights.PLAYER));
        commands.put("thread", new Command(PlayerRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                if (args.length != 1) {
                    player.getPacketSender().sendMessage("Please use the command as ::thread-topic");
                    return;
                }
                player.getPacketSender().sendString(1, "www.rune.live/forum/index.php?/topic/" + args[0] + "-threadcommand/");
                player.getPacketSender().sendMessage("Attempting to open: Thread " + args[0]);
            }
        });
        commands.put("changepass", new ChangePassword(PlayerRights.PLAYER));
        commands.put("password", new ChangePassword(PlayerRights.PLAYER));
        commands.put("home", new TeleportHome(PlayerRights.PLAYER));
        commands.put("train", new TeleportTraining(PlayerRights.PLAYER));
        commands.put("edge", new TeleportEdge(PlayerRights.PLAYER));
        commands.put("duel", new TeleportDuel(PlayerRights.PLAYER));
        commands.put("teamspeak", new Command(PlayerRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                player.getPacketSender().sendMessage("Teamspeak address: ts3.rune.live");
            }
        });
        commands.put("market", new TeleportMarket(PlayerRights.PLAYER));
        commands.put("claim", new ClaimStorePurchase(PlayerRights.PLAYER));
        commands.put("gamble", new TeleportGamble(PlayerRights.PLAYER));
        commands.put("players", new Command(PlayerRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                player.getPacketSender().sendInterfaceRemoval();
                PlayersOnlineInterface.showInterface(player);
            }
        });
        commands.put("empty", new Empty(PlayerRights.PLAYER));
        commands.put("mb", new TeleportMageBank(PlayerRights.PLAYER));
        commands.put("easts", new TeleportEasts(PlayerRights.PLAYER));
        commands.put("wests", new TeleportWests(PlayerRights.PLAYER));

        /**
         * Regular Donor Commands
         */
        commands.put("yell", new Yell(PlayerRights.REGULAR_DONOR));
        commands.put("dzone", new TeleportDonorZone(PlayerRights.REGULAR_DONOR));

        /**
         * Extreme Donor Commands
         */
        commands.put("ezone", new TeleportExtremeZone(PlayerRights.EXTREME_DONOR));

        /**
         * Legendary Donor Commands
         */
        commands.put("togglepray", new TogglePrayer(PlayerRights.LEGENDARY_DONOR));
        commands.put("moderns", new EnableModern(PlayerRights.LEGENDARY_DONOR));
        commands.put("ancients", new EnableAncients(PlayerRights.LEGENDARY_DONOR));
        commands.put("lunars", new EnableLunar(PlayerRights.LEGENDARY_DONOR));

        /**
         * Youtuber Commands
         */

        /**
         * Wiki Manager Commands
         */
        commands.put("promote", new PromoteWiki(PlayerRights.WIKI_MANAGER));
        commands.put("demote", new DemoteWiki(PlayerRights.WIKI_MANAGER));

        /**
         * Server Support Commands
         */
        commands.put("unjail", new Unjail(PlayerRights.SUPPORT));
        commands.put("scan", new Scan(PlayerRights.SUPPORT));
        commands.put("jail", new Jail(PlayerRights.SUPPORT));
        commands.put("mute", new Mute(PlayerRights.SUPPORT));
        commands.put("unmute", new UnMute(PlayerRights.SUPPORT));
        commands.put("teleto", new TeleportToPlayer(PlayerRights.SUPPORT));
        commands.put("movehome", new MovePlayerHome(PlayerRights.SUPPORT));
        commands.put("kick", new Kick(PlayerRights.SUPPORT));
        commands.put("staffzone", new Command(PlayerRights.SUPPORT) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                TeleportHandler.teleportPlayer(player, new Position(2846, 5147), TeleportType.NORMAL);
            }
        });

        /**
         * Moderator Commands
         */
        commands.put("gold", new GetPlayerGold(PlayerRights.MODERATOR));
        commands.put("massban", new MassBan(PlayerRights.MODERATOR));
        commands.put("unmassban", new UnMassBan(PlayerRights.MODERATOR));
        commands.put("teletome", new TeleportPlayerToMe(PlayerRights.MODERATOR));
        //TODO: Yell banning
        commands.put("ban", new Ban(PlayerRights.MODERATOR));
        commands.put("unban", new Unban(PlayerRights.MODERATOR));
        commands.put("ipmute", new IpMute(PlayerRights.MODERATOR));
        commands.put("unipmute", new UnIpMute(PlayerRights.MODERATOR));
        commands.put("banvote", new BanVoting(PlayerRights.MODERATOR));
        commands.put("unbanvote", new UnVoteBan(PlayerRights.MODERATOR));

        /**
         * Administrator Commands
         */
        commands.put("globalyell", new Command(PlayerRights.ADMINISTRATOR) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                player.getPacketSender().sendMessage("Retype the command to renable/disable the yell channel.");
                World.setGlobalYell(!World.isGlobalYell());
                World.sendMessage("<img=4> @blu@The yell channel has been @dre@" + (World.isGlobalYell() ? "@dre@enabled@blu@ again!" : "@dre@disabled@blu@ temporarily!"));
            }
        });
        commands.put("hp", new Command(PlayerRights.ADMINISTRATOR) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 99999, true);
            }
        });
        commands.put("unskull", new Command(PlayerRights.ADMINISTRATOR) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                player.setSkullTimer(0);
                player.setSkullIcon(0);
                player.getUpdateFlag().flag(Flag.APPEARANCE);
                player.getPacketSender().sendMessage("You are unskulled!");
            }
        });
        commands.put("afk", new Command(PlayerRights.ADMINISTRATOR) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                World.sendMessage("<img=4> <col=FF0000><shad=0>" + player.getUsername()
                        + ": I am now away, please don't message me; I won't reply.");
            }
        });
        commands.put("saveall", new Command(PlayerRights.ADMINISTRATOR) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                World.savePlayers();
                player.getPacketSender().sendMessage("Saved players!");
            }
        });
        commands.put("host", new Command(PlayerRights.ADMINISTRATOR) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                Player playr2 = World.getPlayerByName(args[0]);
                if (playr2 != null) {
                    player.getPacketSender().sendMessage("" + playr2.getUsername() + " host IP: " + playr2.getHostAddress()
                            + ", serial number: " + playr2.getSerialNumber());
                } else {
                    player.getPacketSender().sendMessage("Could not find player: " + args[0]);
                }
            }
        });
        commands.put("pos", new GetPosition(PlayerRights.ADMINISTRATOR));
        commands.put("mypos", new GetPosition(PlayerRights.ADMINISTRATOR));
        commands.put("coords", new GetPosition(PlayerRights.ADMINISTRATOR));
        commands.put("tele", new Teleport(PlayerRights.ADMINISTRATOR));

        /**
         * Manager Commands
         */
        commands.put("treasurekeys", new Command(PlayerRights.MANAGER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                player.getInventory().add(9725, 1);
                player.getInventory().add(9722, 1);
                player.getInventory().add(9724, 1);
                player.getInventory().add(9722, 1);
            }
        });
        commands.put("vengrunes", new Command(PlayerRights.MANAGER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                player.getInventory().add(557, 1000);
                player.getInventory().add(560, 1000);
                player.getInventory().add(9075, 1000);
            }
        });
        commands.put("nopoison", new Command(PlayerRights.MANAGER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                player.setPoisonDamage(20);
            }
        });
        commands.put("spec", new Command(PlayerRights.MANAGER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                player.setSpecialPercentage(100);
                CombatSpecial.updateBar(player);
            }
        });
        commands.put("infspec", new Command(PlayerRights.MANAGER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                player.setSpecialPercentage(10000);
                CombatSpecial.updateBar(player);
            }
        });
        commands.put("checkpass", new ChangePassword(PlayerRights.MANAGER));
        commands.put("setpass", new SetPassword(PlayerRights.MANAGER));
        commands.put("checkpin", new CheckPin(PlayerRights.MANAGER));
        commands.put("resetpin", new ResetPin(PlayerRights.MANAGER));
        commands.put("propker", new ProPker(PlayerRights.MANAGER));
        commands.put("godmode", new GodMode(PlayerRights.MANAGER));
        commands.put("reset", new ResetSkills(PlayerRights.MANAGER));
        commands.put("givedonor", new GiveDonor(PlayerRights.MANAGER));
        commands.put("givedonator", new GiveDonor(PlayerRights.MANAGER));
        commands.put("giverights", new GiveRights(PlayerRights.MANAGER));
        commands.put("master", new Master(PlayerRights.MANAGER));
        commands.put("setlevel", new SetLevel(PlayerRights.MANAGER));
        commands.put("item", new SpawnItem(PlayerRights.MANAGER));
        commands.put("spawn", new Spawn(PlayerRights.MANAGER));
        commands.put("find", new FindItem(PlayerRights.MANAGER));
        commands.put("giveitem", new GiveItem(PlayerRights.MANAGER));
        commands.put("update", new UpdateServer(PlayerRights.MANAGER));
        commands.put("checkbank", new CheckBank(PlayerRights.MANAGER));
        commands.put("checkinv", new CheckInventory(PlayerRights.MANAGER));
        commands.put("checkequip", new CheckEquipment(PlayerRights.MANAGER));
        commands.put("bank", new Command(PlayerRights.MANAGER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                player.getBank(player.getCurrentBankTab()).open();
            }
        });

        /**
         * Owner Commands
         */
        commands.put("zulrah", new Command(PlayerRights.OWNER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                player.getZulrah().enterIsland();
            }
        });
        commands.put("resetspecialplayers", new Command(PlayerRights.OWNER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                GameSettings.SPECIAL_PLAYERS.clear();
            }
        });
        commands.put("reload", new Reload(PlayerRights.OWNER));
        commands.put("findnpc", new FindNPC(PlayerRights.OWNER));
        commands.put("tasks", new Command(PlayerRights.OWNER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                player.getPacketSender().sendMessage("Found " + TaskManager.getTaskAmount() + " tasks.");
            }
        });
        commands.put("memory", new Command(PlayerRights.OWNER) {
            @Override
            public void execute(Player player, String[] args, PlayerRights privilege) {
                long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                player.getPacketSender().sendMessage("Heap usage: " + Misc.insertCommasToNumber("" + used + "") + " bytes!");
            }
        });
        commands.put("sendstring", new SendFrame(PlayerRights.OWNER));
        commands.put("frame", new SendFrame(PlayerRights.OWNER));
        commands.put("npc", new SpawnNPC(PlayerRights.OWNER));
        commands.put("playnpc", new PlayerToNPC(PlayerRights.OWNER));
        commands.put("interface", new SendInterface(PlayerRights.OWNER));
        commands.put("walkableinterface", new SendWalkableInterface(PlayerRights.OWNER));
        commands.put("anim", new PlayAnimation(PlayerRights.OWNER));
        commands.put("gfx", new PlayGFX(PlayerRights.OWNER));
        commands.put("object", new SpawnObject(PlayerRights.OWNER));
    }

    public static boolean execute(Player player, String input) {
        if (player.isJailed()) {
            player.getPacketSender().sendMessage("You cannot use command in jail... You're in jail.");
            return false;
        }
        String name = null;
        String argsChunk = null;
        String[] args = null;
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
        Command command = commands.get(name.toLowerCase());
        if (command != null) {
            if (command.getPlayerRights() != null && player.getRights().getRights() < command.getPlayerRights().getRights()) {
                player.getPacketSender().sendMessage("You do not have sufficient privileges to use this command.");
                return false;
            }
            command.execute(player, args, player.getRights());
            return true;
        }
        return false;
    }
}
