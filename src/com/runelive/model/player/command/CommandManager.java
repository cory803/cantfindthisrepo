package com.runelive.model.player.command;

import com.runelive.GameSettings;
import com.runelive.model.Locations;
import com.runelive.model.PlayerRights;
import com.runelive.util.Misc;
import com.runelive.world.content.PlayersOnlineInterface;
import com.runelive.world.content.combat.CombatFactory;
import com.runelive.world.content.combat.DesolaceFormulas;
import com.runelive.world.entity.impl.player.Player;
import org.scripts.kotlin.content.commands.*;

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
                player.getPacketSender().sendString(1, "www.runelive-2.wikia.com/wiki/runelive_2_Wikia");
                player.getPacketSender().sendMessage("Attempting to open: www.rune.live/wiki/");
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
