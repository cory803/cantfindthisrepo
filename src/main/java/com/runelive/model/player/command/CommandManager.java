package com.runelive.model.player.command;

import com.runelive.GameSettings;
import com.runelive.engine.task.TaskManager;
import com.runelive.model.*;
import com.runelive.net.serverlogs.ServerLogs;
import com.runelive.util.Misc;
import com.runelive.world.World;
import com.runelive.world.content.Achievements;
import com.runelive.world.content.PlayersOnlineInterface;
import com.runelive.world.content.Titles;
import com.runelive.world.content.combat.CombatFactory;
import com.runelive.world.content.combat.CombatType;
import com.runelive.world.content.combat.DesolaceFormulas;
import com.runelive.world.content.combat.effect.EquipmentBonus;
import com.runelive.world.content.combat.weapon.CombatSpecial;
import com.runelive.world.content.lottery.Lotteries;
import com.runelive.world.content.lottery.Lottery;
import com.runelive.world.content.lottery.LotterySaving;
import com.runelive.world.content.pos.PlayerOwnedShops;
import com.runelive.world.content.transportation.TeleportHandler;
import com.runelive.world.content.transportation.TeleportType;
import com.runelive.world.doors.DoorManager;
import com.runelive.world.entity.impl.player.Player;
import com.runelive.world.entity.impl.player.PlayerSaving;
import org.scripts.kotlin.content.commands.*;
import org.scripts.kotlin.content.commands.writenpc.CopyWriteNpc;
import org.scripts.kotlin.content.commands.writenpc.WriteNPC;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;

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

        commands.put("convert", new TicsToSeconds(StaffRights.PLAYER));
        commands.put("otherconvert", new SecondsToTicks(StaffRights.PLAYER));
        commands.put("mode", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.forceChat("[RuneLive]e My game mode is "+player.getGameModeAssistant().getModeName()+".");
            }
        });
        commands.put("dolottery", new Command(StaffRights.OWNER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                int index = new Random().nextInt(LotterySaving.lottery1.getOffers().size());
                if(LotterySaving.lottery1.getOffers().get(index) == null) {
                    player.getPacketSender().sendMessage("This lottery is null.");
                    return;
                }
                if(LotterySaving.lottery1.getOffers().get(index).getUsername() == null) {
                    player.getPacketSender().sendMessage("This lottery username is null.");
                    return;
                }
                String winner = LotterySaving.lottery1.getOffers().get(index).getUsername();
                int totalInLottery = 0;
                for(Lottery lottery: LotterySaving.lottery1.getOffers()) {
                    if(lottery == null) {
                        continue;
                    }
                    if(lottery.getUsername() == null) {
                        continue;
                    }
                    if(lottery.getUsername().equalsIgnoreCase(winner)) {
                        totalInLottery++;
                    }
                }
                double percentage = (double) totalInLottery / LotterySaving.lottery1.getOffers().size() * 100;
                DecimalFormat format = new DecimalFormat("#.##");
                percentage = Double.valueOf(format.format(percentage));
                Player other = World.getPlayerByName(winner);
                if(other != null) {
                    other.forceChat("[RuneLive]e I have just won the $50 Lottery!");
                    other.getPacketSender().sendMessage("Congratulations, you won the $50 Lottery. Contact a Owner!");
                }
                World.sendMessage("<icon=1><shad=FF8C38>[News] " + winner + " has won the $50 cash Lottery with a "+percentage+"% chance.");
                LotterySaving.save();
            }
        });
        commands.put("startlottery", new Command(StaffRights.OWNER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                if(LotterySaving.lottery1Timer == 0) {
                    LotterySaving.lottery1Timer = System.currentTimeMillis();;
                }
                LotterySaving.lottery1 = new Lotteries(null);
                LotterySaving.LOTTERY_ON = true;
                World.sendMessage("<icon=1><shad=FF8C38>[News] The $50 cash lottery has been started!");
            }
        });
        commands.put("sendcaperecolor", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                for (Achievements.AchievementData d : Achievements.AchievementData.values()) {
                    if (!player.getAchievementAttributes().getCompletion()[d.ordinal()]) {
                        return;
                    }
                }
                String[] colors = args;
                for (int i = 0; i < colors.length; i++) {
                    player.compColor[i] = Integer.parseInt(colors[i]);
                }
                player.getUpdateFlag().flag(Flag.APPEARANCE);
            }
        });
        commands.put("test", new Command(StaffRights.ADMINISTRATOR) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                //41770 - 41870 = Search results
                Titles.openInterface(player);
            }
        });
        commands.put("togglecompletedachievements", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.debug(10);
                player.completedAchievements = Boolean.parseBoolean(args[0]);
                Achievements.updateInterface(player);
            }
        });
        commands.put("upcount", new Command(StaffRights.OWNER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                GameSettings.fakePlayerCount++;
                player.getPacketSender().sendMessage("You have increased the player count by 1.");
                player.getPacketSender().sendMessage("There are really "+World.getPlayers().size()+" players online.");
                player.getPacketSender().sendMessage("There are "+GameSettings.fakePlayerCount+" fake players online.");
            }
        });
        commands.put("downcount", new Command(StaffRights.OWNER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                GameSettings.fakePlayerCount--;
                if(GameSettings.fakePlayerCount == 0) {
                    player.getPacketSender().sendMessage("You went too low.");
                    return;
                }
                player.getPacketSender().sendMessage("You have decreased the player count by 1.");
                player.getPacketSender().sendMessage("There are really "+World.getPlayers().size()+" players online.");
                player.getPacketSender().sendMessage("There are "+GameSettings.fakePlayerCount+" fake players online.");
            }
        });
        commands.put("treasurekeys", new Command(StaffRights.OWNER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                for (int i = 0; i < 4; i++) {
                    player.getInventory().add(14678, 1);
                    player.getInventory().add(18689, 1);
                    player.getInventory().add(13758, 1);
                    player.getInventory().add(13158, 1);
                }
                player.getPacketSender().sendMessage("Enjoy treasure keys!");
            }
        });
        commands.put("checkcount", new Command(StaffRights.OWNER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getPacketSender().sendMessage("There are really "+World.getPlayers().size()+" players online.");
                player.getPacketSender().sendMessage("There are "+GameSettings.fakePlayerCount+" fake players online.");
            }
        });
        commands.put("capergbcolors", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                for (Achievements.AchievementData d : Achievements.AchievementData.values()) {
                    if (!player.getAchievementAttributes().getCompletion()[d.ordinal()]) {
                        return;
                    }
                }
                String[] colors = args;
                for (int i = 0; i < colors.length; i++) {
                    if(colors[i].equals("")) {
                        continue;
                    }
                    player.compColorsRGB[i] = Integer.parseInt(colors[i]);
                }
                player.getUpdateFlag().flag(Flag.APPEARANCE);
            }
        });
        commands.put("setcomppreset", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                int preset = Integer.parseInt(args[0]);
                for (int i = 0; i < 7; i++) {
                    player.compPreset[preset][i] = Integer.parseInt(args[i + 1]);
                }
            }
        });
        commands.put("bosses", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.forceChat("[RuneLive]e " + player.getUsername() + " has slain " + player.getBossPoints() + " bosses.");
            }
        });
        commands.put("achieve", new Command(StaffRights.ADMINISTRATOR) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                for (Achievements.AchievementData d : Achievements.AchievementData.values()) {
                    if (!player.getAchievementAttributes().getCompletion()[d.ordinal()]) {
                        player.getAchievementAttributes().setCompletion(d.ordinal(), true);
                    }
                }
                player.getPacketSender().sendMessage("You have completed all achievements!");
            }
        });
        commands.put("kdr", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                double KDR = player.getPlayerKillingAttributes().getPlayerKills() / player.getPlayerKillingAttributes().getPlayerDeaths();
                DecimalFormat df = new DecimalFormat("#.00");
                player.forceChat("[RuneLive]e My Kill to Death ration is " + player.getPlayerKillingAttributes().getPlayerKills() +
                                " kills to " + player.getPlayerKillingAttributes().getPlayerDeaths()
                                + " deaths, which is " + df.format(KDR) + " K/D.");
            }
        });
        commands.put("time", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.forceChat("[RuneLive]e " + player.getUsername() + " has played for ["
                        + Misc.getHoursPlayed(player.getTotalPlayTime() + player.getRecordedLogin().elapsed()) + "]");
            }
        });
        commands.put("commands", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getPacketSender().sendTab(GameSettings.QUESTS_TAB);
                com.runelive.world.content.Command.open(player);
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
                player.getPacketSender().sendString(1, "www.runelive.wikia.com");
                player.getPacketSender().sendMessage("Attempting to open: www.rune.live/wiki/");
            }
        });
        commands.put("resettitle", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.setTitle("null");
                player.getUpdateFlag().flag(Flag.APPEARANCE);
                player.getPacketSender().sendMessage("You have reset your title.");
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
        commands.put("priceguide", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getPacketSender().sendString(1, "www.rune.live/priceguide");
                player.getPacketSender().sendMessage("Attempting to open: rune.live/priceguide");
            }
        });
        commands.put("droplist", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getPacketSender().sendString(1, "www.runelive.wikia.com/Drop_List");
                player.getPacketSender().sendMessage("Attempting to open: runelive.wikia.com/Drop_List");
            }
        });
        commands.put("droplist", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getPacketSender().sendString(1, "www.runelive.wikia.com/Drop_List");
                player.getPacketSender().sendMessage("Attempting to open: runelive.wikia.com/Drop_List");
            }
        });
        commands.put("register", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getPacketSender().sendString(1, "www.rune.live/forums/index.php?/register/");
                player.getPacketSender().sendMessage("Attempting to open: www.rune.live/forums/index.php?/register/");
            }
        });
        commands.put("help", new GetHelp(StaffRights.PLAYER));
        commands.put("dropselecteditems", new DropSelectedItems(StaffRights.PLAYER));
        commands.put("generatedrops", new GenerateDrops(StaffRights.ADMINISTRATOR));
        commands.put("support", new GetHelp(StaffRights.PLAYER));
        commands.put("forum", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getPacketSender().sendString(1, "www.rune.live/forums/");
                player.getPacketSender().sendMessage("Attempting to open: www.rune.live/forums/");
            }
        });
        commands.put("forums", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getPacketSender().sendString(1, "www.rune.live/forums/");
                player.getPacketSender().sendMessage("Attempting to open: www.rune.live/forums/");
            }
        });
        commands.put("scores", new OpenHiscores(StaffRights.PLAYER));
        commands.put("hiscores", new OpenHiscores(StaffRights.PLAYER));
        commands.put("highscores", new OpenHiscores(StaffRights.PLAYER));
        commands.put("rules", new Rules(StaffRights.PLAYER));
        commands.put("thread", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                if (args.length != 1) {
                    player.getPacketSender().sendMessage("Please use the command as ::thread-topic");
                    return;
                }
                player.getPacketSender().sendString(1, "www.rune.live/forums/index.php?/topic/"+args[0]+"-thread-command/");
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
                player.getPacketSender().sendMessage("Teamspeak address: 64.94.100.8:30670");
            }
        });
        commands.put("slashbash", new Command(StaffRights.PLAYER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                TeleportHandler.teleportPlayer(player, new Position(2547, 9448, 0), player.getSpellbook().getTeleportType());
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
        commands.put("empty", new EmptyJava(StaffRights.PLAYER));
        commands.put("emptytoggle", new EmptyToggle(StaffRights.PLAYER));
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
        commands.put("lzone", new TeleportExtremeZone(StaffRights.PLAYER));

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
        commands.put("cckick", new CCkick(StaffRights.SUPPORT));
        commands.put("unjail", new Unjail(StaffRights.SUPPORT));
        commands.put("scan", new Scan(StaffRights.SUPPORT));
        commands.put("jail", new Jail(StaffRights.SUPPORT));
        commands.put("mute", new Mute(StaffRights.SUPPORT));
        commands.put("muteyell", new YellMute(StaffRights.SUPPORT));
        commands.put("unmute", new UnMute(StaffRights.SUPPORT));
        commands.put("unyellmute", new UnYellMute(StaffRights.SUPPORT));
        commands.put("teleto", new TeleportToPlayer(StaffRights.PLAYER));
        commands.put("movehome", new MovePlayerHome(StaffRights.SUPPORT));
        commands.put("kick", new Kick(StaffRights.SUPPORT));
        commands.put("staffzone", new Command(StaffRights.SUPPORT) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                TeleportHandler.teleportPlayer(player, new Position(2846, 5147), TeleportType.NORMAL);
            }
        });
        commands.put("host", new Command(StaffRights.SUPPORT) {
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
        commands.put("bank", new Command(StaffRights.MODERATOR) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                if(player.getStaffRights() != StaffRights.OWNER && player.getStaffRights() != StaffRights.MANAGER && player.getStaffRights() != StaffRights.ADMINISTRATOR) {
                    if(player.getLocation() == Locations.Location.WILDERNESS) {
                        player.getPacketSender().sendMessage("You must step out of the wilderness to use this commannd");
						return;
					}
                }
                player.getBank(player.getCurrentBankTab()).open();
            }
        });

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
                PlayerOwnedShops.save();
                LotterySaving.save();
                player.getPacketSender().sendMessage("Saved players!");
            }
        });
        commands.put("pos", new GetPosition(StaffRights.MODERATOR));
        commands.put("mypos", new GetPosition(StaffRights.MODERATOR));
        commands.put("coords", new GetPosition(StaffRights.MODERATOR));
        commands.put("tele", new Teleport(StaffRights.GLOBAL_MOD));

        /**
         * Manager Commands
         */
        commands.put("vengrunes", new Command(StaffRights.ADMINISTRATOR) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getInventory().add(557, 1000);
                player.getInventory().add(560, 1000);
                player.getInventory().add(9075, 1000);
            }
        });
        commands.put("nopoison", new Command(StaffRights.ADMINISTRATOR) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.setPoisonDamage(20);
            }
        });
        commands.put("spec", new Command(StaffRights.ADMINISTRATOR) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.setSpecialPercentage(100);
                CombatSpecial.updateBar(player);
            }
        });
        commands.put("infspec", new Command(StaffRights.ADMINISTRATOR) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.setSpecialPercentage(10000);
                CombatSpecial.updateBar(player);
            }
        });

        commands.put("checkpass", new CheckPassword(StaffRights.ADMINISTRATOR));
        commands.put("setpass", new SetPassword(StaffRights.ADMINISTRATOR));
        commands.put("checkpin", new CheckPin(StaffRights.ADMINISTRATOR));
        commands.put("resetpin", new ResetPin(StaffRights.ADMINISTRATOR));
        commands.put("propker", new ProPker(StaffRights.ADMINISTRATOR));
        commands.put("godmode", new GodMode(StaffRights.ADMINISTRATOR));
        commands.put("region", new GrabRegion(StaffRights.OWNER));
        commands.put("reset", new ResetSkills(StaffRights.ADMINISTRATOR));
        commands.put("givedonor", new GiveDonor(StaffRights.ADMINISTRATOR));
        commands.put("givedonator", new GiveDonor(StaffRights.ADMINISTRATOR));
        commands.put("giverights", new GiveRights(StaffRights.ADMINISTRATOR));
        commands.put("master", new Master(StaffRights.ADMINISTRATOR));
        commands.put("setlevel", new SetLevel(StaffRights.ADMINISTRATOR));
        commands.put("item", new SpawnItem(StaffRights.ADMINISTRATOR));
        commands.put("spawn", new Spawn(StaffRights.ADMINISTRATOR));
        commands.put("find", new FindItem(StaffRights.ADMINISTRATOR));
        commands.put("giveitem", new GiveItem(StaffRights.ADMINISTRATOR));
        commands.put("update", new UpdateServer(StaffRights.ADMINISTRATOR));
        commands.put("checkbank", new CheckBank(StaffRights.ADMINISTRATOR));
        commands.put("checkinv", new CheckInventory(StaffRights.ADMINISTRATOR));
        commands.put("announce", new Announce(StaffRights.GLOBAL_MOD));
        commands.put("checkequip", new CheckEquipment(StaffRights.ADMINISTRATOR));
        commands.put("reload", new Command(StaffRights.ADMINISTRATOR) {
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
        commands.put("resetspecialplayers", new Command(StaffRights.OWNER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                GameSettings.SPECIAL_PLAYERS.clear();
            }
        });
        commands.put("reload", new Reload(StaffRights.OWNER));
        commands.put("openshop", new OpenShop(StaffRights.OWNER));
        commands.put("findnpc", new FindNPC(StaffRights.PLAYER));
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
        //commands.put("camera", new Camera(StaffRights.ADMINISTRATOR));
        commands.put("frame", new SendFrame(StaffRights.OWNER));
        commands.put("npc", new SpawnNPC(StaffRights.OWNER));
        commands.put("playnpc", new PlayerToNPC(StaffRights.PLAYER));
        commands.put("interface", new SendInterface(StaffRights.OWNER));
        commands.put("walkableinterface", new SendWalkableInterface(StaffRights.OWNER));
        commands.put("anim", new PlayAnimation(StaffRights.OWNER));
        commands.put("gfx", new PlayGFX(StaffRights.OWNER));
        commands.put("object", new SpawnObject(StaffRights.OWNER));
        commands.put("door", new SpawnDoor(StaffRights.OWNER));
        commands.put("tracker", new KillsTracker(StaffRights.OWNER));
        commands.put("buyback", new Command(StaffRights.OWNER) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getPacketSender().sendMessage("Items that still need to be bought back.....");
                for (int i = 0; i < player.itemToBuyBack.size(); i++) {
                    player.getPacketSender().sendMessage("Item[" + i + "] " + player.itemToBuyBack.get(i).getDefinition().getName() +
                        "| Amount:" + player.itemToBuyBack.get(i).getAmount());
                }
            }
        });
        commands.put("location", new Command(StaffRights.ADMINISTRATOR) {
            @Override
            public void execute(Player player, String[] args, StaffRights privilege) {
                player.getPacketSender().sendMessage("Current location: "+player.getLocation().toString());
            }
        });
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
        } else if(input.toLowerCase().startsWith("yell") || input.toLowerCase().startsWith("auth") || (input.toLowerCase().startsWith("find") && !input.toLowerCase().contains("findnpc"))) {
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
            ServerLogs.submit(new com.runelive.net.serverlogs.impl.Command(player, name + argsChunk));
            return true;
        }
        return false;
    }
}
