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
     * @param stmt The statement to execute
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
	"update `accounts` set positionx = ?, positiony = ?, positionz = ?, playtime = ?, yell = ?, staffrights = ?, donorrights = ?, gamemode = ?, lastlogin = ?, lastipaddress = ?, lastcomputeraddress = ?, lastmacaddress = ?, lastserialaddress = ?, lastbankip = ?, lastbankserial = ?, loyaltytitle = ?, loyaltyrank = ?, onlinestatus = ?, jailedstatus = ?, xprate = ?, givenstarter = ?, yelltoggle = ?, tourneytoggle = ?, yellmute = ?, gereturn = ?, moneypouch = ?, tournamentpoints = ?, donated = ?, credits = ?, questpoints = ?, warningpoints = ?, minutesbonusexp = ?, totalgainedexp = ?, prestigepoints = ?, achievementpoints = ?, dungtokens = ?, commendations = ?, loyaltypoints = ?, totalloyaltypoints = ?, dungitems = ?, canvote = ?, revswarning = ?, votesclaimed = ?, votingpoints = ?, slayerpoints = ?, pkpoints = ?, toxicstaffcharges = ?, forumconnections = ?, bosspoints = ?, playerkills = ?, playerkillstreak = ?, playerdeaths = ?, targetpercentage = ?, bhrank = ?, gender = ?, spellbook = ?, prayerbook = ?, running = ?, runenergy = ?, music = ?, sounds = ?, autoretaliate = ?, xplocked = ?, vengcast = ?, lastveng = ?, fighttype = ?, soleffect = ?, skulltimer = ?, acceptaid = ?, poisondamage = ?, poisonimmunity = ?, venomdamage = ?, venomimmunity = ?, overloadtimer = ?, fireimmunity = ?, firedamagemod = ?, prayerrenewaltimer = ?, teleblocktimer = ?, specialamount = ?, enteredgwdroom = ?, announcedmax = ?, gwdaltardelay = ?, gwdkillcount = ?, effigy = ?, summonnpc = ?, summondeath = ?, processfarming = ?, clanchat = ?, autocast = ?, autocastspell = ?, dfscharges = ?, coinsgambled = ?, slayermaster = ?, slayertask = ?, prevslayertask = ?, taskamount = ?, taskstreak = ?, duopartner = ?, doubleslayxp = ?, recoildeg = ?, brawlerdeg = ?, vestadeg = ?, zurieldeg = ?, statiusdeg = ?, morrigansdeg = ?, corruptvestadeg = ?, corruptzurieldeg = ?, corruptstatiusdeg = ?, corruptmorrigansdeg = ?, killedplayers = ?, killedgods = ?, barrowsbrother = ?, randomcoffin = ?, barrowskillcount = ?, nomad = ?, recipefordisaster = ?, clawquest = ?, farmquest = ?, recipefordisasterwave = ?, dungitemsbound = ?, runeess = ?, pureess = ?, hasbankpin = ?, bankpin = ?, lastpinattempt = ?, invalidpinattempts = ?, appearance = ?, agilityobj = ?, skills = ?, maxlevel = ?, experience = ?, inventory = ?, equipment = ?, bank0 = ?, bank1 = ?, bank2 = ?, bank3 = ?, bank4 = ?, bank5 = ?, bank6 = ?, bank7 = ?, bank8 = ?, charmimp = ?, friends = ?, ignores = ?, loyaltytitles = ?, kills = ?, achievementscompletion = ?, achievementsprogress = ? Where username = ? limit 1;";

    /**
     * Executed a PreparedStatement query.
     *
     * @param callback The callback to inform when the query is successful/fails
     */
    public void executeLogoutQuery(final Player player, final ThreadedSQLCallback callback) {
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
					int index = 1;
                    final PreparedStatement stmt = GameServer.getSQLPool().prepareStatement(UpdateQuery);
                    stmt.setInt(index++, player.getPosition().getX());
                    stmt.setInt(index++, player.getPosition().getY());
					stmt.setInt(index++, player.getPosition().getZ());
					stmt.setLong(index++, player.getTotalPlayTime());
					stmt.setString(index++, player.getYellTag());
					stmt.setInt(index++, player.getRights().ordinal());
					stmt.setInt(index++, player.getDonorRights());
					stmt.setInt(index++, player.getGameMode().ordinal());
					stmt.setLong(index++, player.getLastLogin());
					stmt.setString(index++, player.getLastIpAddress());
					stmt.setString(index++, player.getLastComputerAddress());
					stmt.setString(index++, player.getMacAddress());
					stmt.setLong(index++, player.getSerialNumber());
					stmt.setString(index++, player.getLastBankIp());
					stmt.setString(index++, player.getLastBankSerial());
					stmt.setInt(index++, player.getLoyaltyTitle().ordinal());
					stmt.setInt(index++, player.getLoyaltyRank());
					stmt.setInt(index++, player.getRelations().getStatus().ordinal());
					stmt.setBoolean(index++, player.isJailed());
					stmt.setBoolean(index++, player.getXpRate());
					stmt.setBoolean(index++, player.didReceiveStarter());
					stmt.setBoolean(index++, player.yellToggle());
					stmt.setBoolean(index++, player.tourneyToggle());
					stmt.setBoolean(index++, player.isYellMute());
					stmt.setBoolean(index++, player.hasDoneGrandExchangeReturn());
					stmt.setLong(index++, player.getMoneyInPouch());
					stmt.setLong(index++, player.getPointsHandler().getTournamentPoints());
					stmt.setInt(index++, player.getAmountDonated());
					stmt.setInt(index++, player.getCredits());
					stmt.setInt(index++, player.getQuestPoints());
					stmt.setInt(index++, player.getWarningPoints());
					stmt.setInt(index++, player.getMinutesBonusExp());
					stmt.setLong(index++, player.getSkillManager().getTotalGainedExp());
					stmt.setInt(index++, player.getPointsHandler().getPrestigePoints());
					stmt.setInt(index++, player.getPointsHandler().getAchievementPoints());
					stmt.setInt(index++, player.getPointsHandler().getDungeoneeringTokens());
					stmt.setInt(index++, player.getPointsHandler().getCommendations());
					stmt.setInt(index++, player.getPointsHandler().getLoyaltyPoints());
					stmt.setDouble(index++, player.getAchievementAttributes().getTotalLoyaltyPointsEarned());
					stmt.setBoolean(index++, player.isCanWearDungItems());
					stmt.setBoolean(index++, player.isCanVote());
					stmt.setBoolean(index++, player.getRevsWarning());
					stmt.setInt(index++, player.getVotesClaimed());
					stmt.setInt(index++, player.getPointsHandler().getVotingPoints());
					stmt.setInt(index++, player.getPointsHandler().getSlayerPoints());
					stmt.setInt(index++, player.getPointsHandler().getPkPoints());
					stmt.setInt(index++, player.getToxicStaffCharges());
					stmt.setInt(index++, player.getForumConnections());
					stmt.setInt(index++, player.getBossPoints());
					stmt.setInt(index++, player.getPlayerKillingAttributes().getPlayerKills());
					stmt.setInt(index++, player.getPlayerKillingAttributes().getPlayerKillStreak());
					stmt.setInt(index++, player.getPlayerKillingAttributes().getPlayerDeaths());
					stmt.setInt(index++, player.getPlayerKillingAttributes().getTargetPercentage());
					stmt.setInt(index++, player.getAppearance().getBountyHunterSkull());
					stmt.setInt(index++, player.getAppearance().getGender().ordinal());
					stmt.setInt(index++, player.getSpellbook().ordinal());
					stmt.setInt(index++, player.getPrayerbook().ordinal());
					stmt.setBoolean(index++, player.isRunning());
					stmt.setInt(index++, player.getRunEnergy());
					stmt.setBoolean(index++, player.musicActive());
					stmt.setBoolean(index++, player.soundsActive());
					stmt.setBoolean(index++, player.isAutoRetaliate());
					stmt.setBoolean(index++, player.experienceLocked());
					stmt.setBoolean(index++, player.hasVengeance());
					stmt.setLong(index++, player.getLastVengeance().elapsed());
					stmt.setInt(index++, player.getFightType().ordinal());
					stmt.setInt(index++, player.getStaffOfLightEffect());
					stmt.setInt(index++, player.getSkullTimer());
					stmt.setBoolean(index++, player.isAcceptAid());
					stmt.setInt(index++, player.getPoisonDamage());
					stmt.setInt(index++, player.getPoisonImmunity());
					stmt.setInt(index++, player.getVenomDamage());
					stmt.setInt(index++, player.getVenomImmunity());
					stmt.setInt(index++, player.getOverloadPotionTimer());
					stmt.setInt(index++, player.getFireImmunity());
					stmt.setInt(index++, player.getFireDamageModifier());
					stmt.setInt(index++, player.getPrayerRenewalPotionTimer());
					stmt.setInt(index++, player.getTeleblockTimer());
					stmt.setInt(index++, player.getSpecialPercentage());
					stmt.setBoolean(index++, player.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom());
					stmt.setBoolean(index++, player.hasAnnouncedMax());
					stmt.setLong(index++, player.getMinigameAttributes().getGodwarsDungeonAttributes().getAltarDelay());
					StringBuilder Temp = new StringBuilder();
                    for (int i = 0; i < player.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount().length; i++) {
                        Temp.append(player.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[i] + ":");
                    }
                    stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
					stmt.setInt(index++, player.getEffigy());
					stmt.setInt(index++, player.getSummoning().getFamiliar() != null ? player.getSummoning().getFamiliar().getSummonNpc().getId() : -1);
					stmt.setInt(index++, player.getSummoning().getFamiliar() != null ? player.getSummoning().getFamiliar().getDeathTimer() : -1);
					stmt.setBoolean(index++, player.shouldProcessFarming());
					stmt.setString(index++, player.getClanChatName() == null ? "null" : player.getClanChatName().trim());
					stmt.setBoolean(index++, player.isAutocast());
					stmt.setInt(index++, player.getAutocastSpell() != null ? player.getAutocastSpell().spellId() : -1);
					stmt.setInt(index++, player.getDfsCharges());
					stmt.setInt(index++, player.getAchievementAttributes().getCoinsGambled());
					stmt.setInt(index++, player.getSlayer().getSlayerMaster().ordinal());
					stmt.setInt(index++, player.getSlayer().getSlayerTask().ordinal());
					stmt.setInt(index++, player.getSlayer().getLastTask().ordinal());
					stmt.setInt(index++, player.getSlayer().getAmountToSlay());
					stmt.setInt(index++, player.getSlayer().getTaskStreak());
					stmt.setString(index++, player.getSlayer().getDuoPartner() == null ? "null" : player.getSlayer().getDuoPartner());
					stmt.setBoolean(index++, player.getSlayer().doubleSlayerXP);
					stmt.setInt(index++, player.getRecoilCharges());
					Temp = new StringBuilder();
					for (int i = 0; i < player.getBrawlerChargers().length; i++) {
                        Temp.append(player.getBrawlerChargers()[i] + ":");
                    }
					stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
					
					Temp = new StringBuilder();
					for (int i = 0; i < player.getVestaCharges().length; i++) {
                        Temp.append(player.getVestaCharges()[i] + ":");
                    }
					stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
					
					Temp = new StringBuilder();
					for (int i = 0; i < player.getZurielsCharges().length; i++) {
                        Temp.append(player.getZurielsCharges()[i] + ":");
                    }
					stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
					
					Temp = new StringBuilder();
					for (int i = 0; i < player.getStatiusCharges().length; i++) {
                        Temp.append(player.getStatiusCharges()[i] + ":");
                    }
					stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());

					Temp = new StringBuilder();
					for (int i = 0; i < player.getMorrigansCharges().length; i++) {
                        Temp.append(player.getMorrigansCharges()[i] + ":");
                    }
					stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
						
					Temp = new StringBuilder();
					for (int i = 0; i < player.getCorruptVestaCharges().length; i++) {
                        Temp.append(player.getCorruptVestaCharges()[i] + ":");
                    }
					stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
									
					Temp = new StringBuilder();
					for (int i = 0; i < player.getCorruptZurielsCharges().length; i++) {
                        Temp.append(player.getCorruptZurielsCharges()[i] + ":");
                    }
					stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
														
					Temp = new StringBuilder();
					for (int i = 0; i < player.getCorruptStatiusCharges().length; i++) {
                        Temp.append(player.getCorruptStatiusCharges()[i] + ":");
                    }
					stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
																			
					Temp = new StringBuilder();
					for (int i = 0; i < player.getCorruptMorrigansCharges().length; i++) {
                        Temp.append(player.getCorruptMorrigansCharges()[i] + ":");
                    }
					stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
					
					Temp = new StringBuilder();
					for (int i = 0; i < player.getPlayerKillingAttributes().getKilledPlayers().size(); i++) {
                        Temp.append(player.getPlayerKillingAttributes().getKilledPlayers().get(i) + ":");
                    }
					if(player.getPlayerKillingAttributes().getKilledPlayers().size() == 0) {
						stmt.setString(index++, "");
					} else {
						stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
					}					
																			
					Temp = new StringBuilder();
					for (int i = 0; i < player.getAchievementAttributes().getGodsKilled().length; i++) {
                        Temp.append(player.getAchievementAttributes().getGodsKilled()[i] + ":");
                    }
					stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
					
					Temp = new StringBuilder();
					for (int i = 0; i < player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData().length; i++) {
						for (int i2 = 0; i2 < player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[1].length; i2++) {
							Temp.append(player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[i][i2] + "_");
						}
						Temp.deleteCharAt(Temp.length() - 1);
						Temp.append(":");
                    }
					stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
					
					stmt.setInt(index++, player.getMinigameAttributes().getBarrowsMinigameAttributes().getRandomCoffin());
					stmt.setInt(index++, player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount());
					
					Temp = new StringBuilder();
					for (int i = 0; i < player.getMinigameAttributes().getNomadAttributes().getQuestParts().length; i++) {
                        Temp.append(player.getMinigameAttributes().getNomadAttributes().getQuestParts()[i] + ":");
                    }
					stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
										
					Temp = new StringBuilder();
					for (int i = 0; i < player.getMinigameAttributes().getRecipeForDisasterAttributes().getQuestParts().length; i++) {
                        Temp.append(player.getMinigameAttributes().getRecipeForDisasterAttributes().getQuestParts()[i] + ":");
                    }
					stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
					
					stmt.setInt(index++, player.getMinigameAttributes().getClawQuestAttributes().getQuestParts());
					stmt.setInt(index++, player.getMinigameAttributes().getFarmQuestAttributes().getQuestParts());
					stmt.setInt(index++, player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted());
										
					Temp = new StringBuilder();
					for (int i = 0; i < player.getMinigameAttributes().getDungeoneeringAttributes().getBoundItems().length; i++) {
                        Temp.append(player.getMinigameAttributes().getDungeoneeringAttributes().getBoundItems()[i] + ":");
                    }
					stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
					
					stmt.setInt(index++, player.getStoredRuneEssence());
					stmt.setInt(index++, player.getStoredPureEssence());
					stmt.setBoolean(index++, player.getBankPinAttributes().hasBankPin());
					Temp = new StringBuilder();
					for (int i = 0; i < player.getBankPinAttributes().getBankPin().length; i++) {
                        Temp.append(player.getBankPinAttributes().getBankPin()[i] + ":");
                    }	
					stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
					
					stmt.setLong(index++, player.getBankPinAttributes().getLastAttempt());
					stmt.setLong(index++, player.getBankPinAttributes().getInvalidAttempts());
					
					Temp = new StringBuilder();
					for (int i = 0; i < player.getAppearance().getLook().length; i++) {
                        Temp.append(player.getAppearance().getLook()[i] + ":");
                    }	
					stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
					
					Temp = new StringBuilder();
					for (int i = 0; i < player.getCrossedObstacles().length; i++) {
                        Temp.append(player.getCrossedObstacles()[i] + ":");
                    }	
					stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
					
					Temp = new StringBuilder();
					for (int i = 0; i < 25; i++) {
						Temp.append(player.getSkillManager().getCurrentLevel(Skill.forId(i)) + ":");
                    }
					stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
					
					Temp = new StringBuilder();
					for (int i = 0; i < 25; i++) {
						Temp.append(player.getSkillManager().getMaxLevel(Skill.forId(i)) + ":");
                    }
					stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
										
					Temp = new StringBuilder();
					for (int i = 0; i < 25; i++) {
						Temp.append(player.getSkillManager().getExperience(Skill.forId(i)) + ":");
                    }
					stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
															
					Temp = new StringBuilder();
					for (int i = 0; i < player.getInventory().capacity(); i++) {
						Temp.append(player.getInventory().getItems()[i].getId());
						Temp.append("_");
						Temp.append(player.getInventory().getItems()[i].getAmount());
						Temp.append(":");
				   }
				   stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
															
					Temp = new StringBuilder();
					for (int i = 0; i < player.getEquipment().capacity(); i++) {
						Temp.append(player.getEquipment().getItems()[i].getId());
						Temp.append("_");
						Temp.append(player.getEquipment().getItems()[i].getAmount());
						Temp.append(":");
				   }
				   stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
				  // int xx = 132;
				   for (int i3 = 0; i3 < 9; i3++) {
						if(Bank.isEmpty(player.getBank(i3))) {
							stmt.setString(index++, "");
						} else {
							Temp = new StringBuilder();
							for (int i = 0; i < player.getBank(i3).getValidItems().size(); i++) {
								Temp.append(player.getBank(i3).getValidItems().get(i).getId());
								Temp.append("_");
								Temp.append(player.getBank(i3).getValidItems().get(i).getAmount());
								Temp.append(":");
						   }
							stmt.setString(index++, Temp.toString());
						}
						//xx++;
				   }
					Temp = new StringBuilder();
					for (int i = 0; i < player.getSummoning().getCharmImpConfigs().length; i++) {
                        Temp.append(player.getSummoning().getCharmImpConfigs()[i] + ":");
                    }	
					stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
					
					Temp = new StringBuilder();
					if(player.getRelations().getFriendList().toArray().length == 0) {
						stmt.setString(index++, "");
					} else {
						for (int i = 0; i < player.getRelations().getFriendList().toArray().length; i++) {
							Temp.append(player.getRelations().getFriendList().toArray()[i] + ":");
						}	
						stmt.setString(index++, Temp.toString());
					}
					Temp = new StringBuilder();
					if(player.getRelations().getIgnoreList().toArray().length == 0) {
						stmt.setString(index++, "");
					} else {
						for (int i = 0; i < player.getRelations().getIgnoreList().toArray().length; i++) {
							Temp.append(player.getRelations().getIgnoreList().toArray()[i] + ":");
						}						
						stmt.setString(index++, Temp.toString());
					}
					
					Temp = new StringBuilder();
					for (int i = 0; i < player.getUnlockedLoyaltyTitles().length; i++) {
                        Temp.append(player.getUnlockedLoyaltyTitles()[i] + ":");
                    }	
					stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
					
					if(player.getKillsTracker().toArray().length == 0) {
						stmt.setString(index++, "");
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
						stmt.setString(index++, Temp.toString());
					}
					
					Temp = new StringBuilder();
					for (int i = 0; i < player.getAchievementAttributes().getCompletion().length; i++) {
                        Temp.append(player.getAchievementAttributes().getCompletion()[i] + ":");
                    }	
					stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
					
					Temp = new StringBuilder();
					for (int i = 0; i < player.getAchievementAttributes().getProgress().length; i++) {
                        Temp.append(player.getAchievementAttributes().getProgress()[i] + ":");
                    }	
					stmt.setString(index++, Temp.deleteCharAt(Temp.length() - 1).toString());
					
					//Final Username
					stmt.setString(index++, player.getUsername());
                    
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
