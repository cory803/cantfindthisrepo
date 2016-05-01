package com.runelive.net.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.runelive.model.Skill;
import java.sql.SQLException;
import java.sql.Statement;
import com.runelive.model.container.impl.Bank;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.runelive.GameServer;
import com.runelive.world.entity.impl.player.Player;

/**
 * The main class which is used for all of the central operations
 *
 * @author Nikki
 *
 */
public class ThreadedSQL {

    /**
     * The service used to execute queries
     */
    public ExecutorService service;

    /**
     * The SQL Connection Pool
     */
    private ConnectionPool<DatabaseConnection> pool;

    /**
     * Create a new threaded sql instance from the specified configuraton
     *
     * @param configuration The configuration to use
     */
    public ThreadedSQL(DatabaseConfiguration configuration) {
        service = Executors.newCachedThreadPool();
        pool = new ConnectionPool<DatabaseConnection>(configuration, 10);
    }

    /**
     * Create a new threaded sql instance with the specified configuration and
     * number of threads
     *
     * @param configuration The configuration to use
     * @param threads The max number of threads
     */
    public ThreadedSQL(DatabaseConfiguration configuration, int threads) {
        this(configuration, threads, threads);
    }

    /**
     * Create a new threaded sql instance with the specified configuration,
     * number of threads and number of connections
     *
     * @param configuration The configuration to use
     * @param threads The max number of threads
     * @param connections The max number of connections
     */
    public ThreadedSQL(DatabaseConfiguration configuration, int threads, int connections) {
        service = Executors.newFixedThreadPool(threads);
        pool = new ConnectionPool<DatabaseConnection>(configuration, threads);
    }

    /**
     * Executed a PreparedStatement query.
     *
     * @param statement The statement to execute
     * @param callback The callback to inform when the query is successful/fails
     */
    public void executeQuery(final PreparedStatement statement, final ThreadedSQLCallback callback) {
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    query(statement, callback);
                } catch (SQLException e) {
                    if (callback != null) {
                        callback.queryError(e);
                    }
                }
            }
        });
    }

    /**
     * Executed a PreparedStatement query.
     *
     * @param statement The statement to execute
     * @param callback The callback to inform when the query is successful/fails
     */
    public void executeLoginQuery(final PreparedStatement stmt, final ThreadedSQLCallback callback, final Player p) {
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //Execute the PreparedStatement
                    query(stmt, callback);
                } catch (SQLException e) {
                    if (callback != null) {
                        callback.queryError(e);
                    }
                }
            }
        });
    }
	
    public static String UpdateQuery = 
	"update `accounts` set positionx = ?, positiony = ?, positionz = ?, playtime = ?, yell = ?, staffrights = ?, donorrights = ?, gamemode = ?, lastlogin = ?, lastipaddress = ?, lastcomputeraddress = ?, lastbankip = ?, lastbankserial = ?, loyaltytitle = ?, loyaltyrank = ?, onlinestatus = ?, jailedstatus = ?, xprate = ?, givenstarter = ?, yelltoggle = ?, tourneytoggle = ?, yellmute = ?, gereturn = ?, moneypouch = ?, tournamentpoints = ?, donated = ?, credits = ?, questpoints = ?, warningpoints = ?, minutesbonusexp = ?, totalgainedexp = ?, prestigepoints = ?, achievementpoints = ?, dungtokens = ?, commendations = ?, loyaltypoints = ?, totalloyaltypoints = ?, dungitems = ?, canvote = ?, revswarning = ?, votesclaimed = ?, votingpoints = ?, slayerpoints = ?, pkpoints = ?, toxicstaffcharges = ?, forumconnections = ?, bosspoints = ?, playerkills = ?, playerkillstreak = ?, playerdeaths = ?, targetpercentage = ?, bhrank = ?, gender = ?, spellbook = ?, prayerbook = ?, running = ?, runenergy = ?, music = ?, sounds = ?, autoretaliate = ?, xplocked = ?, vengcast = ?, lastveng = ?, fighttype = ?, soleffect = ?, skulltimer = ?, acceptaid = ?, poisondamage = ?, poisonimmunity = ?, venomdamage = ?, venomimmunity = ?, overloadtimer = ?, fireimmunity = ?, firedamagemod = ?, prayerrenewaltimer = ?, teleblocktimer = ?, specialamount = ?, enteredgwdroom = ?, announcedmax = ?, gwdaltardelay = ?, gwdkillcount = ?, effigy = ?, summonnpc = ?, summondeath = ?, processfarming = ?, clanchat = ?, autocast = ?, autocastspell = ?, dfscharges = ?, coinsgambled = ?, slayermaster = ?, slayertask = ?, prevslayertask = ?, taskamount = ?, taskstreak = ?, duopartner = ?, doubleslayxp = ?, recoildeg = ?, brawlerdeg = ?, vestadeg = ?, zurieldeg = ?, statiusdeg = ?, morrigansdeg = ?, corruptvestadeg = ?, corruptzurieldeg = ?, corruptstatiusdeg = ?, corruptmorrigansdeg = ?, killedplayers = ?, killedgods = ?, barrowsbrother = ?, randomcoffin = ?, barrowskillcount = ?, nomad = ?, recipefordisaster = ?, clawquest = ?, farmquest = ?, recipefordisasterwave = ?, dungitemsbound = ?, runeess = ?, pureess = ?, hasbankpin = ?, bankpin = ?, lastpinattempt = ?, invalidpinattempts = ?, appearance = ?, agilityobj = ?, skills = ?, maxlevel = ?, experience = ?, inventory = ?, equipment = ?, bank0 = ?, bank1 = ?, bank2 = ?, bank3 = ?, bank4 = ?, bank5 = ?, bank6 = ?, bank7 = ?, bank8 = ?, charmimp = ?, friends = ?, ignores = ?, loyaltytitles = ?, kills = ?, achievementscompletion = ?, achievementsprogress = ? Where username = ? limit 1;";

    /**
     * Executed a PreparedStatement query.
     *
     * @param statement The statement to execute
     * @param callback The callback to inform when the query is successful/fails
     */
    public void executeLogoutQuery(final Player player, final ThreadedSQLCallback callback) {
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final PreparedStatement stmt = GameServer.getSQLPool().prepareStatement(UpdateQuery);
                    stmt.setInt(1, player.getPosition().getX());
                    stmt.setInt(2, player.getPosition().getY());
					stmt.setInt(3, player.getPosition().getZ());
					stmt.setLong(4, player.getTotalPlayTime());
					stmt.setString(5, player.getYellTag());
					stmt.setInt(6, player.getRights().ordinal());
					stmt.setInt(7, player.getDonorRights());
					stmt.setInt(8, player.getGameMode().ordinal());
					stmt.setLong(9, player.getLastLogin());
					stmt.setString(10, player.getLastIpAddress());
					stmt.setString(11, player.getLastComputerAddress());
					stmt.setString(12, player.getLastBankIp());
					stmt.setString(13, player.getLastBankSerial());
					stmt.setInt(14, player.getLoyaltyTitle().ordinal());
					stmt.setInt(15, player.getLoyaltyRank());
					stmt.setInt(16, player.getRelations().getStatus().ordinal());
					stmt.setBoolean(17, player.isJailed());
					stmt.setBoolean(18, player.getXpRate());
					stmt.setBoolean(19, player.didReceiveStarter());
					stmt.setBoolean(20, player.yellToggle());
					stmt.setBoolean(21, player.tourneyToggle());
					stmt.setBoolean(22, player.isYellMute());
					stmt.setBoolean(23, player.hasDoneGrandExchangeReturn());
					stmt.setLong(24, player.getMoneyInPouch());
					stmt.setLong(25, player.getPointsHandler().getTournamentPoints());
					stmt.setInt(26, player.getAmountDonated());
					stmt.setInt(27, player.getCredits());
					stmt.setInt(28, player.getQuestPoints());
					stmt.setInt(29, player.getWarningPoints());
					stmt.setInt(30, player.getMinutesBonusExp());
					stmt.setLong(31, player.getSkillManager().getTotalGainedExp());
					stmt.setInt(32, player.getPointsHandler().getPrestigePoints());
					stmt.setInt(33, player.getPointsHandler().getAchievementPoints());
					stmt.setInt(34, player.getPointsHandler().getDungeoneeringTokens());
					stmt.setInt(35, player.getPointsHandler().getCommendations());
					stmt.setInt(36, player.getPointsHandler().getLoyaltyPoints());
					stmt.setDouble(37, player.getAchievementAttributes().getTotalLoyaltyPointsEarned());
					stmt.setBoolean(38, player.isCanWearDungItems());
					stmt.setBoolean(39, player.isCanVote());
					stmt.setBoolean(40, player.getRevsWarning());
					stmt.setInt(41, player.getVotesClaimed());
					stmt.setInt(42, player.getPointsHandler().getVotingPoints());
					stmt.setInt(43, player.getPointsHandler().getSlayerPoints());
					stmt.setInt(44, player.getPointsHandler().getPkPoints());
					stmt.setInt(45, player.getToxicStaffCharges());
					stmt.setInt(46, player.getForumConnections());
					stmt.setInt(47, player.getBossPoints());
					stmt.setInt(48, player.getPlayerKillingAttributes().getPlayerKills());
					stmt.setInt(49, player.getPlayerKillingAttributes().getPlayerKillStreak());
					stmt.setInt(50, player.getPlayerKillingAttributes().getPlayerDeaths());
					stmt.setInt(51, player.getPlayerKillingAttributes().getTargetPercentage());
					stmt.setInt(52, player.getAppearance().getBountyHunterSkull());
					stmt.setInt(53, player.getAppearance().getGender().ordinal());
					stmt.setInt(54, player.getSpellbook().ordinal());
					stmt.setInt(55, player.getPrayerbook().ordinal());
					stmt.setBoolean(56, player.isRunning());
					stmt.setInt(57, player.getRunEnergy());
					stmt.setBoolean(58, player.musicActive());
					stmt.setBoolean(59, player.soundsActive());
					stmt.setBoolean(60, player.isAutoRetaliate());
					stmt.setBoolean(61, player.experienceLocked());
					stmt.setBoolean(62, player.hasVengeance());
					stmt.setLong(63, player.getLastVengeance().elapsed());
					stmt.setInt(64, player.getFightType().ordinal());
					stmt.setInt(65, player.getStaffOfLightEffect());
					stmt.setInt(66, player.getSkullTimer());
					stmt.setBoolean(67, player.isAcceptAid());
					stmt.setInt(68, player.getPoisonDamage());
					stmt.setInt(69, player.getPoisonImmunity());
					stmt.setInt(70, player.getVenomDamage());
					stmt.setInt(71, player.getVenomImmunity());
					stmt.setInt(72, player.getOverloadPotionTimer());
					stmt.setInt(73, player.getFireImmunity());
					stmt.setInt(74, player.getFireDamageModifier());
					stmt.setInt(75, player.getPrayerRenewalPotionTimer());
					stmt.setInt(76, player.getTeleblockTimer());
					stmt.setInt(77, player.getSpecialPercentage());
					stmt.setBoolean(78, player.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom());
					stmt.setBoolean(79, player.hasAnnouncedMax());
					stmt.setLong(80, player.getMinigameAttributes().getGodwarsDungeonAttributes().getAltarDelay());
					StringBuilder Temp = new StringBuilder();
                    for (int i = 0; i < player.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount().length; i++) {
                        Temp.append(player.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[i] + ":");
                    }
                    stmt.setString(81, Temp.deleteCharAt(Temp.length() - 1).toString());
					stmt.setInt(82, player.getEffigy());
					stmt.setInt(83, player.getSummoning().getFamiliar() != null ? player.getSummoning().getFamiliar().getSummonNpc().getId() : -1);
					stmt.setInt(84, player.getSummoning().getFamiliar() != null ? player.getSummoning().getFamiliar().getDeathTimer() : -1);
					stmt.setBoolean(85, player.shouldProcessFarming());
					stmt.setString(86, player.getClanChatName() == null ? "null" : player.getClanChatName().trim());
					stmt.setBoolean(87, player.isAutocast());
					stmt.setInt(88, player.getAutocastSpell() != null ? player.getAutocastSpell().spellId() : -1);
					stmt.setInt(89, player.getDfsCharges());
					stmt.setInt(90, player.getAchievementAttributes().getCoinsGambled());
					stmt.setInt(91, player.getSlayer().getSlayerMaster().ordinal());
					stmt.setInt(92, player.getSlayer().getSlayerTask().ordinal());
					stmt.setInt(93, player.getSlayer().getLastTask().ordinal());
					stmt.setInt(94, player.getSlayer().getAmountToSlay());
					stmt.setInt(95, player.getSlayer().getTaskStreak());
					stmt.setString(96, player.getSlayer().getDuoPartner() == null ? "null" : player.getSlayer().getDuoPartner());
					stmt.setBoolean(97, player.getSlayer().doubleSlayerXP);
					stmt.setInt(98, player.getRecoilCharges());
					Temp = new StringBuilder();
					for (int i = 0; i < player.getBrawlerChargers().length; i++) {
                        Temp.append(player.getBrawlerChargers()[i] + ":");
                    }
					stmt.setString(99, Temp.deleteCharAt(Temp.length() - 1).toString());
					
					Temp = new StringBuilder();
					for (int i = 0; i < player.getVestaCharges().length; i++) {
                        Temp.append(player.getVestaCharges()[i] + ":");
                    }
					stmt.setString(100, Temp.deleteCharAt(Temp.length() - 1).toString());	
					
					Temp = new StringBuilder();
					for (int i = 0; i < player.getZurielsCharges().length; i++) {
                        Temp.append(player.getZurielsCharges()[i] + ":");
                    }
					stmt.setString(101, Temp.deleteCharAt(Temp.length() - 1).toString());		
					
					Temp = new StringBuilder();
					for (int i = 0; i < player.getStatiusCharges().length; i++) {
                        Temp.append(player.getStatiusCharges()[i] + ":");
                    }
					stmt.setString(102, Temp.deleteCharAt(Temp.length() - 1).toString());	
					
					Temp = new StringBuilder();
					for (int i = 0; i < player.getMorrigansCharges().length; i++) {
                        Temp.append(player.getMorrigansCharges()[i] + ":");
                    }
					stmt.setString(103, Temp.deleteCharAt(Temp.length() - 1).toString());
						
					Temp = new StringBuilder();
					for (int i = 0; i < player.getCorruptVestaCharges().length; i++) {
                        Temp.append(player.getCorruptVestaCharges()[i] + ":");
                    }
					stmt.setString(104, Temp.deleteCharAt(Temp.length() - 1).toString());
									
					Temp = new StringBuilder();
					for (int i = 0; i < player.getCorruptZurielsCharges().length; i++) {
                        Temp.append(player.getCorruptZurielsCharges()[i] + ":");
                    }
					stmt.setString(105, Temp.deleteCharAt(Temp.length() - 1).toString());
														
					Temp = new StringBuilder();
					for (int i = 0; i < player.getCorruptStatiusCharges().length; i++) {
                        Temp.append(player.getCorruptStatiusCharges()[i] + ":");
                    }
					stmt.setString(106, Temp.deleteCharAt(Temp.length() - 1).toString());
																			
					Temp = new StringBuilder();
					for (int i = 0; i < player.getCorruptMorrigansCharges().length; i++) {
                        Temp.append(player.getCorruptMorrigansCharges()[i] + ":");
                    }
					stmt.setString(107, Temp.deleteCharAt(Temp.length() - 1).toString());	
					
					Temp = new StringBuilder();
					for (int i = 0; i < player.getPlayerKillingAttributes().getKilledPlayers().size(); i++) {
                        Temp.append(player.getPlayerKillingAttributes().getKilledPlayers().get(i) + ":");
                    }
					if(player.getPlayerKillingAttributes().getKilledPlayers().size() == 0) {
						stmt.setString(108, "");
					} else {
						stmt.setString(108, Temp.deleteCharAt(Temp.length() - 1).toString());
					}					
																			
					Temp = new StringBuilder();
					for (int i = 0; i < player.getAchievementAttributes().getGodsKilled().length; i++) {
                        Temp.append(player.getAchievementAttributes().getGodsKilled()[i] + ":");
                    }
					stmt.setString(109, Temp.deleteCharAt(Temp.length() - 1).toString());		
					
					Temp = new StringBuilder();
					for (int i = 0; i < player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData().length; i++) {
						for (int i2 = 0; i2 < player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[1].length; i2++) {
							Temp.append(player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[i][i2] + "_");
						}
						Temp.deleteCharAt(Temp.length() - 1);
						Temp.append(":");
                    }
					stmt.setString(110, Temp.deleteCharAt(Temp.length() - 1).toString());
					
					stmt.setInt(111, player.getMinigameAttributes().getBarrowsMinigameAttributes().getRandomCoffin());	
					stmt.setInt(112, player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount());	
					
					Temp = new StringBuilder();
					for (int i = 0; i < player.getMinigameAttributes().getNomadAttributes().getQuestParts().length; i++) {
                        Temp.append(player.getMinigameAttributes().getNomadAttributes().getQuestParts()[i] + ":");
                    }
					stmt.setString(113, Temp.deleteCharAt(Temp.length() - 1).toString());	
										
					Temp = new StringBuilder();
					for (int i = 0; i < player.getMinigameAttributes().getRecipeForDisasterAttributes().getQuestParts().length; i++) {
                        Temp.append(player.getMinigameAttributes().getRecipeForDisasterAttributes().getQuestParts()[i] + ":");
                    }
					stmt.setString(114, Temp.deleteCharAt(Temp.length() - 1).toString());	
					
					stmt.setInt(115, player.getMinigameAttributes().getClawQuestAttributes().getQuestParts());	
					stmt.setInt(116, player.getMinigameAttributes().getFarmQuestAttributes().getQuestParts());	
					stmt.setInt(117, player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted());	
										
					Temp = new StringBuilder();
					for (int i = 0; i < player.getMinigameAttributes().getDungeoneeringAttributes().getBoundItems().length; i++) {
                        Temp.append(player.getMinigameAttributes().getDungeoneeringAttributes().getBoundItems()[i] + ":");
                    }
					stmt.setString(118, Temp.deleteCharAt(Temp.length() - 1).toString());	
					
					stmt.setInt(119, player.getStoredRuneEssence());	
					stmt.setInt(120, player.getStoredPureEssence());	
					stmt.setBoolean(121, player.getBankPinAttributes().hasBankPin());	
					Temp = new StringBuilder();
					for (int i = 0; i < player.getBankPinAttributes().getBankPin().length; i++) {
                        Temp.append(player.getBankPinAttributes().getBankPin()[i] + ":");
                    }	
					stmt.setString(122, Temp.deleteCharAt(Temp.length() - 1).toString());
					
					stmt.setLong(123, player.getBankPinAttributes().getLastAttempt());
					stmt.setLong(124, player.getBankPinAttributes().getInvalidAttempts());
					
					Temp = new StringBuilder();
					for (int i = 0; i < player.getAppearance().getLook().length; i++) {
                        Temp.append(player.getAppearance().getLook()[i] + ":");
                    }	
					stmt.setString(125, Temp.deleteCharAt(Temp.length() - 1).toString());	
					
					Temp = new StringBuilder();
					for (int i = 0; i < player.getCrossedObstacles().length; i++) {
                        Temp.append(player.getCrossedObstacles()[i] + ":");
                    }	
					stmt.setString(126, Temp.deleteCharAt(Temp.length() - 1).toString());
					
					Temp = new StringBuilder();
					for (int i = 0; i < 25; i++) {
						Temp.append(player.getSkillManager().getCurrentLevel(Skill.forId(i)) + ":");
                    }
					stmt.setString(127, Temp.deleteCharAt(Temp.length() - 1).toString());	
					
					Temp = new StringBuilder();
					for (int i = 0; i < 25; i++) {
						Temp.append(player.getSkillManager().getMaxLevel(Skill.forId(i)) + ":");
                    }
					stmt.setString(128, Temp.deleteCharAt(Temp.length() - 1).toString());
										
					Temp = new StringBuilder();
					for (int i = 0; i < 25; i++) {
						Temp.append(player.getSkillManager().getExperience(Skill.forId(i)) + ":");
                    }
					stmt.setString(129, Temp.deleteCharAt(Temp.length() - 1).toString());
															
					Temp = new StringBuilder();
					for (int i = 0; i < player.getInventory().capacity(); i++) {
						Temp.append(player.getInventory().getItems()[i].getId());
						Temp.append("_");
						Temp.append(player.getInventory().getItems()[i].getAmount());
						Temp.append(":");
				   }
				   stmt.setString(130, Temp.deleteCharAt(Temp.length() - 1).toString());
															
					Temp = new StringBuilder();
					for (int i = 0; i < player.getEquipment().capacity(); i++) {
						Temp.append(player.getEquipment().getItems()[i].getId());
						Temp.append("_");
						Temp.append(player.getEquipment().getItems()[i].getAmount());
						Temp.append(":");
				   }
				   stmt.setString(131, Temp.deleteCharAt(Temp.length() - 1).toString());
				   int xx = 132;
				   for (int i3 = 0; i3 < 9; i3++) {
						if(Bank.isEmpty(player.getBank(i3))) {
							stmt.setString(xx, "");
						} else {
							Temp = new StringBuilder();
							for (int i = 0; i < player.getBank(i3).getValidItems().size(); i++) {
								Temp.append(player.getBank(i3).getValidItems().get(i).getId());
								Temp.append("_");
								Temp.append(player.getBank(i3).getValidItems().get(i).getAmount());
								Temp.append(":");
						   }
							stmt.setString(xx, Temp.toString());
						}
						xx++;
				   }
					Temp = new StringBuilder();
					for (int i = 0; i < player.getSummoning().getCharmImpConfigs().length; i++) {
                        Temp.append(player.getSummoning().getCharmImpConfigs()[i] + ":");
                    }	
					stmt.setString(141, Temp.deleteCharAt(Temp.length() - 1).toString());
					
					Temp = new StringBuilder();
					if(player.getRelations().getFriendList().toArray().length == 0) {
						stmt.setString(142, "");
					} else {
						for (int i = 0; i < player.getRelations().getFriendList().toArray().length; i++) {
							Temp.append(player.getRelations().getFriendList().toArray()[i] + ":");
						}	
						stmt.setString(142, Temp.toString());	
					}
					Temp = new StringBuilder();
					if(player.getRelations().getIgnoreList().toArray().length == 0) {
						stmt.setString(143, "");
					} else {
						for (int i = 0; i < player.getRelations().getIgnoreList().toArray().length; i++) {
							Temp.append(player.getRelations().getIgnoreList().toArray()[i] + ":");
						}						
						stmt.setString(143, Temp.toString());
					}
					
					Temp = new StringBuilder();
					for (int i = 0; i < player.getUnlockedLoyaltyTitles().length; i++) {
                        Temp.append(player.getUnlockedLoyaltyTitles()[i] + ":");
                    }	
					stmt.setString(144, Temp.deleteCharAt(Temp.length() - 1).toString());
					
					if(player.getKillsTracker().toArray().length == 0) {
						stmt.setString(145, "");
					} else {
						Temp = new StringBuilder();
						for (int i = 0; i < player.getKillsTracker().toArray().length; i++) {
							Temp.append(player.getKillsTracker().get(i).npcName);
							Temp.append("_");
							Temp.append(player.getKillsTracker().get(i).amount);
							Temp.append("_");
							Temp.append(player.getKillsTracker().get(i).boss);
							Temp.append(":");
						}
						stmt.setString(145, Temp.toString());
					}
					
					Temp = new StringBuilder();
					for (int i = 0; i < player.getAchievementAttributes().getCompletion().length; i++) {
                        Temp.append(player.getAchievementAttributes().getCompletion()[i] + ":");
                    }	
					stmt.setString(146, Temp.deleteCharAt(Temp.length() - 1).toString());	
					
					Temp = new StringBuilder();
					for (int i = 0; i < player.getAchievementAttributes().getProgress().length; i++) {
                        Temp.append(player.getAchievementAttributes().getProgress()[i] + ":");
                    }	
					stmt.setString(147, Temp.deleteCharAt(Temp.length() - 1).toString());
					
					//Final Username
					stmt.setString(148, player.getUsername());
                    
                    query(stmt, callback);
                } catch (SQLException e) {
                    if (callback != null) {
                        callback.queryError(e);
                    }
                }
            }
        });
    }

    /**
     * Executed a standard sql query.
     *
     * @param query The statement to execute
     * @param callback The callback to inform when the query is successful/fails
     */
    public void executeQuery(final String query, final ThreadedSQLCallback callback) {
        service.execute(new Runnable() {
            @Override
            public void run() {
                DatabaseConnection conn = null;
                try {
                    conn = pool.nextFree();
                    query(query, callback, conn);
                } catch (SQLException e) {
                    e.printStackTrace();
                    if (callback != null) {
                        callback.queryError(e);
                    }
                } finally {
                    if (conn != null)
                       conn.returnConnection();
                }
            }
        });
    }

    /**
     * Create a PreparedStatement from a random pool connection
     *
     * @param string The statement to prepare
     * @return The initialized PreparedStatement
     * @throws SQLException If an error occurred while preparing
     */
    public PreparedStatement prepareStatement(String string) throws SQLException {
        DatabaseConnection conn = pool.nextFree();
        if (conn == null) {
            System.out.println("[BURAK] WTFFF MAN CONNECTIE IS NULL" + string);
            return null;
        }
        Connection c = conn.getConnection();

        try {
            return c.prepareStatement(string);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.returnConnection();
        }
        return null;
    }

    /**
     * Internal method to handle sql calls for PreparedStatements Note: You HAVE
     *
     * @param statement The statement to execute
     * @param callback The callback to inform
     * @throws SQLException If an error occurs while executing, this is passed
     * to callback.queryError(SQLException e)
     */
    private void query(PreparedStatement statement, ThreadedSQLCallback callback) throws SQLException {
        statement.execute();

        //Prepared statements don't hold a connection, they simply use it
        ResultSet result = statement.getResultSet();
        try {
            if (callback != null) {
                callback.queryComplete(result);
            }
        } finally {
            //Close the result set
            if (result != null) {
                result.close();
            }
            if (statement != null) {
                statement.close();
            }
            
        }
    }

    /**
     * Internal method to handle sql calls for standard queries
     *
     * @param statement The statement to execute
     * @param callback The callback to inform
     * @throws SQLException If an error occurs while executing, this is passed
     * to callback.queryError(SQLException e)
     */
    private void query(String query, ThreadedSQLCallback callback, DatabaseConnection conn) throws SQLException {
        if (conn == null) {
            System.out.println("[BURAK] WTFFF MAN CONNECTIE IS NULL" + query);
            return;
        }
        Connection c = conn.getConnection();

        Statement statement = c.createStatement();
        statement.execute(query);

        ResultSet result = statement.getResultSet();
        try {
            if (callback != null) {
                callback.queryComplete(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //Close the result set
            if (result != null) {
                result.close();
            }
            if (statement != null) {
                statement.close();
            }
            //Return the used connection
           // conn.returnConnection();
        }
    }

    /**
     * Get the connection pool, for use with standard queries :D
     */
    public ConnectionPool<DatabaseConnection> getConnectionPool() {
        return pool;
    }
}
