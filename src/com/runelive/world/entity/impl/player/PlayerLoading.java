package com.runelive.world.entity.impl.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.runelive.GameServer;
import com.runelive.GameSettings;
import com.runelive.engine.task.impl.FamiliarSpawnTask;
import com.runelive.model.*;
import com.runelive.model.PlayerRelations.PrivateChatStatus;
import com.runelive.model.container.impl.Bank;
import com.runelive.net.login.LoginManager;
import com.runelive.net.login.LoginResponses;
import com.runelive.net.mysql.SQLCallback;
import com.runelive.world.content.DropLog;
import com.runelive.world.content.DropLog.DropLogEntry;
import com.runelive.world.content.KillsTracker;
import com.runelive.world.content.KillsTracker.KillsEntry;
import com.runelive.world.content.combat.magic.CombatSpells;
import com.runelive.world.content.combat.weapon.FightType;
import com.runelive.world.content.grandexchange.GrandExchangeSlot;
import com.runelive.world.content.skill.SkillManager.Skills;
import com.runelive.world.content.skill.impl.construction.HouseFurniture;
import com.runelive.world.content.skill.impl.construction.Portal;
import com.runelive.world.content.skill.impl.construction.Room;
import com.runelive.world.content.skill.impl.slayer.SlayerMaster;
import com.runelive.world.content.skill.impl.slayer.SlayerTasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerLoading {

    public static int loadJSON(Player player) {
        if (player.getUsername().contains(":") || player.getUsername().contains("\"")) {
            //player.setLoginQue(true);
            return LoginResponses.LOGIN_REJECT_SESSION;
        }
        // Create the path and file objects.
        Path path = Paths.get("./characters/", player.getUsername() + ".json");
        File file = path.toFile();

        // If the file doesn't exist, we're logging in for the first
        // time and can skip all of this.
        if (!file.exists()) {
            player.setNewPlayer(true);
			player.setResponse(2);
			//player.setLoginQue(true);
            /*if (!World.getLoginQueue().contains(player)) {
                World.getLoginQueue().add(player);
            }*/
            return LoginResponses.LOGIN_SUCCESSFUL;
        }

        // Now read the properties from the json parser.
        try (FileReader fileReader = new FileReader(file)) {
            final String pass = player.getPassword();

            decodeJson(player, new String(Files.readAllBytes(path)));

            if (GameSettings.HASH_PASSWORDS) {
                String salt = player.getSalt();
                String password = player.getPassword();
                String hashedPassword = player.getHashedPassword();
                if (salt.isEmpty() || hashedPassword.isEmpty()) {
                    if (!password.equals(pass)) {
                        //player.setLoginQue(true);
                        return LoginResponses.LOGIN_INVALID_CREDENTIALS;
                    }
                } else {
                    String hashPass = PlayerSaving.hashPassword(salt, pass);
                    if (!hashPass.equals(hashedPassword)) {
                        //player.setLoginQue(true);
                        return LoginResponses.LOGIN_INVALID_CREDENTIALS;
                    }
                }
            } else {
                if (!player.getPassword().equals(pass)) {
                    //player.setLoginQue(true);
					/*if (World.getLoginQueue().contains(player)) {
						World.getLoginQueue().remove(player);
					}*/
                    return LoginResponses.LOGIN_INVALID_CREDENTIALS;
                }
            }

            File rooms = new File("./housing/rooms/" + player.getUsername() + ".ser");
            if (rooms.exists()) {
                FileInputStream fileIn = new FileInputStream(rooms);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                player.setHouseRooms((Room[][][]) in.readObject());
                in.close();
                fileIn.close();
            }

            File portals = new File("./housing/portals/" + player.getUsername() + ".ser");
            if (portals.exists()) {
                FileInputStream fileIn = new FileInputStream(portals);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                player.setHousePortals((ArrayList<Portal>) in.readObject());
                in.close();
                fileIn.close();
            }

            File furniture = new File("./housing/furniture/" + player.getUsername() + ".ser");
            if (furniture.exists()) {
                FileInputStream fileIn = new FileInputStream(furniture);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                player.setHouseFurniture((ArrayList<HouseFurniture>) in.readObject());
                in.close();
                fileIn.close();
            }
            player.setResponse(LoginResponses.LOGIN_SUCCESSFUL);
            //player.setLoginQue(true);
            /*if (!World.getLoginQueue().contains(player)) {
                World.getLoginQueue().add(player);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            return LoginResponses.LOGIN_SUCCESSFUL;
        }
        return LoginResponses.LOGIN_SUCCESSFUL;
    }

    public static void performSqlRequest(Player player) {
        //Perform a standard threaded query
        GameServer.getCharacterPool().executeQuery("SELECT * FROM `accounts` WHERE username = '" + player.getUsername() + "' LIMIT 1", new SQLCallback() {
            @Override
            public void queryComplete(ResultSet rs) throws SQLException {
                if (rs.next()) {
                    boolean matches = false;
                    String password = rs.getString("password");
                    String salt = "not-set";
                    if (GameSettings.HASH_PASSWORDS) {
                        salt = rs.getString("salt");
                        if (salt == null || salt.isEmpty() || salt.equalsIgnoreCase("not-set")) {//check old unhashed password.
                            matches = player.getPassword().equals(password);//see if passwords match.
                            player.setSalt(PlayerSaving.generateSalt());//set player salt
                            PlayerSaving.updatePassword(player, PlayerSaving.hashPassword(player.getSalt(), player.getPassword()), player.getSalt());//update password in db to hashed version w/ new salt
                        } else {
                            String hashedPlayerPassword = PlayerSaving.hashPassword(salt, player.getPassword());//hash players input password w/ salt from db
                            matches = password.equals(hashedPlayerPassword);//check if hashed pass in db matches player's input password
                        }
                    } else {
                        matches = player.getPassword().equals(password);
                    }
                    if (matches) {
                        player.setUsername(rs.getString("username"));
                        player.setRights(PlayerRights.forId(rs.getInt("staffrights")));
                        player.setDonorRights(rs.getInt("donorrights"));

                        final String jsonString = rs.getString("json");
                        if (jsonString != null) {
                            decodeJson(player, jsonString);
                        } else {
                            System.err.println("Json is NULL for existing account: " + player.getUsername());
                        }

                        if (GameSettings.HASH_PASSWORDS) {
                            player.setSalt(salt);
                            player.setHashedPassword(PlayerSaving.hashPassword(salt, player.getPassword()));
                        }
						player.setResponse(LoginResponses.LOGIN_SUCCESSFUL);
                    } else {
						player.setResponse(LoginResponses.LOGIN_INVALID_CREDENTIALS);
                    }
                    LoginManager.finalizeLogin(player);
                } else {
                    GameServer.getCharacterPool().executeQuery("SELECT username FROM `accounts` as acc WHERE username = '" + player.getUsername() + "' LIMIT 1", new SQLCallback() {
                        @Override
                        public void queryComplete(ResultSet rs) throws SQLException {
                            if (rs.next()) {
                                player.setResponse(LoginResponses.LOGIN_INVALID_CREDENTIALS);
                                LoginManager.finalizeLogin(player);
                            } else {
                                PlayerSaving.createNewAccount(player);
                            }
                        }

                        @Override
                        public void queryError(SQLException e) {
                            player.setResponse(LoginResponses.LOGIN_INVALID_CREDENTIALS);
                            LoginManager.finalizeLogin(player);
                            e.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void queryError(SQLException e) {
                player.setResponse(LoginResponses.LOGIN_INVALID_CREDENTIALS);
                LoginManager.finalizeLogin(player);
                e.printStackTrace();
            }
        });
        //return player.getResponse();
    }


    public static JsonObject decodeJson(Player player, String jsonString) {
        Gson builder = new GsonBuilder().create();
        JsonObject reader = new JsonParser().parse(jsonString).getAsJsonObject();

        if (reader.has("total-play-time-ms")) {
            player.setTotalPlayTime(reader.get("total-play-time-ms").getAsLong());
        }

        if (reader.has("username")) {
            player.setUsername(reader.get("username").getAsString());
        }

        if (reader.has("password")) {
            String password = reader.get("password").getAsString();
            player.setPassword(password);
        }

        if (reader.has("password_change")) {
        	player.setPasswordChange(reader.get("password_change").getAsInt());
        }
        
        if (reader.has("player-salt")) {
            String salt = reader.get("player-salt").getAsString();
            player.setSalt(salt);
        }

        if (reader.has("password-hash")) {
            String pass = reader.get("password-hash").getAsString();
            player.setHashedPassword(pass);
        }

        if (reader.has("email")) {
            player.setEmailAddress(reader.get("email").getAsString());
        }

        if (reader.has("yell-tag")) {
            player.setYellTag(reader.get("yell-tag").getAsString());
        }

        if (reader.has("toxic-staff-charges")) {
            player.setToxicStaffCharges(reader.get("toxic-staff-charges").getAsInt());
        }

        if (reader.has("forum-connections")) {
            player.setForumConnections(reader.get("forum-connections").getAsInt());
        }

        if (reader.has("staff-rights")) {
            player.setRights(PlayerRights.valueOf(reader.get("staff-rights").getAsString()));
        }

        if (reader.has("donor-rights")) {
            player.setDonorRights(reader.get("donor-rights").getAsInt());
        }

        if (reader.has("game-mode")) {
            player.setGameMode(GameMode.valueOf(reader.get("game-mode").getAsString()));
        }

        if (reader.has("exp-rate")) {
            player.setExpRate(ExpRates.valueOf(reader.get("exp-rate").getAsString()));
        }

        if (reader.has("last-login")) {
            player.setLastLogin(reader.get("last-login").getAsLong());
        }
		
        if (reader.has("arena-victories")) {
            player.getDueling().arenaStats[0] = reader.get("arena-victories").getAsInt();
        }	
		
        if (reader.has("arena-losses")) {
            player.getDueling().arenaStats[1] = reader.get("arena-losses").getAsInt();
        }

        if (reader.has("last-ip-address")) {
            player.setLastIpAddress(reader.get("last-ip-address").getAsString());
        }

        if (reader.has("last-serial-address")) {
            String str = reader.get("last-serial-address").getAsString();
            long serial = -1;
            try {
                serial = Long.parseLong(str);
            } catch (NumberFormatException e) {
                
            }
            player.setLastSerialAddress(serial);
        }

        if (reader.has("last-mac-address")) {
            player.setLastMacAddress(reader.get("last-mac-address").getAsString());
        }

        if (reader.has("last-computer-address")) {
            player.setLastComputerAddress(reader.get("last-computer-address").getAsString());
        }

        if (reader.has("last-bank-ip")) {
            player.setLastBankIp(reader.get("last-bank-ip").getAsString());
        }

        if (reader.has("last-bank-serial")) {
            player.setLastBankSerial(reader.get("last-bank-serial").getAsString());
        }

        if (reader.has("position")) {
            player.getPosition().setAs(builder.fromJson(reader.get("position"), Position.class));
        }

        if (reader.has("loyalty-rank")) {
            player.setLoyaltyRank(reader.get("loyalty-rank").getAsInt());
        }
        
        if (reader.has("daily-task-date")) {
            player.dailyTaskDate = reader.get("daily-task-date").getAsInt();
        }

        if (reader.has("daily-task")) {
            player.dailyTask = reader.get("daily-task").getAsInt();
        }
        
        if (reader.has("daily-task-progress")) {
            player.dailyTaskProgress = reader.get("daily-task-progress").getAsInt();
        }   
        
        if (reader.has("completed-daily-task")) {
            player.completedDailyTask = reader.get("completed-daily-task").getAsBoolean();
        }
        
        if (reader.has("online-status")) {
            player.getRelations()
                    .setStatus(PrivateChatStatus.valueOf(reader.get("online-status").getAsString()), false);
        }

        if (reader.has("jailed-status")) {
            player.setJailed(reader.get("jailed-status").getAsBoolean());
        }

        if (reader.has("money-pouch")) {
            player.setMoneyInPouch(reader.get("money-pouch").getAsLong());
        }

        if (reader.has("tournament-points")) {
            player.getPointsHandler().setTournamentPoints(reader.get("tournament-points").getAsLong(), false);
        }

        if (reader.has("xp-rate")) {
            player.setXpRate(reader.get("xp-rate").getAsBoolean());
        }

        if (reader.has("given-starter")) {
            player.setReceivedStarter(reader.get("given-starter").getAsBoolean());
        }

        if (reader.has("yell-toggle")) {
            player.setYellToggle(reader.get("yell-toggle").getAsBoolean());
        }

        if (reader.has("tourney-toggle")) {
            player.setTourneyToggle(reader.get("tourney-toggle").getAsBoolean());
        }

        if (reader.has("yell-mute")) {
            player.setYellMute(reader.get("yell-mute").getAsBoolean());
        }

        if (reader.has("ge-return")) {
            player.setDoneGrandExchangeReturn(reader.get("ge-return").getAsBoolean());
        }

        if (reader.has("donated")) {
            player.incrementAmountDonated(reader.get("donated").getAsInt());
        }

        if (reader.has("credits")) {
            player.addCredits(reader.get("credits").getAsInt());
        }
        if (reader.has("quest-points")) {
            player.setQuestPoints(reader.get("quest-points").getAsInt());
        }

        if (reader.has("minutes-bonus-exp")) {
            player.setMinutesBonusExp(reader.get("minutes-bonus-exp").getAsInt(), false);
        }

        if (reader.has("total-gained-exp")) {
            player.getSkillManager().setTotalGainedExp(reader.get("total-gained-exp").getAsInt());
        }

        if (reader.has("dung-tokens")) {
            player.getPointsHandler().setDungeoneeringTokens(reader.get("dung-tokens").getAsInt(),
                    false);
        }

        if (reader.has("prestige-points")) {
            player.getPointsHandler().setPrestigePoints(reader.get("prestige-points").getAsInt(),
                    false);
        }

        if (reader.has("achievement-points")) {
            player.getPointsHandler().setAchievementPoints(reader.get("achievement-points").getAsInt(),
                    false);
        }

        if (reader.has("commendations")) {
            player.getPointsHandler().setCommendations(reader.get("commendations").getAsInt(), false);
        }

        if (reader.has("loyalty-points")) {
            player.getPointsHandler().setLoyaltyPoints(reader.get("loyalty-points").getAsInt(), false);
        }

        if (reader.has("total-loyalty-points")) {
            player.getAchievementAttributes()
                    .incrementTotalLoyaltyPointsEarned(reader.get("total-loyalty-points").getAsDouble());
        }

        if (reader.has("dung-items")) {
            player.setCanWearDungItems(reader.get("dung-items").getAsBoolean());
        }

        if (reader.has("Can-Vote")) {
            player.setCanVote(reader.get("Can-Vote").getAsBoolean());
        }

        if (reader.has("revs-warning")) {
            player.setRevsWarning(reader.get("revs-warning").getAsBoolean());
        }

        if (reader.has("votes-claimed")) {
            player.setVotesClaimed(reader.get("votes-claimed").getAsInt());
        }

        if (reader.has("voting-points")) {
            player.getPointsHandler().setVotingPoints(reader.get("voting-points").getAsInt(), false);
        }

        if (reader.has("slayer-points")) {
            player.getPointsHandler().setSlayerPoints(reader.get("slayer-points").getAsInt(), false);
        }

        if (reader.has("pk-points")) {
            player.getPointsHandler().setPkPoints(reader.get("pk-points").getAsInt(), false, false);
        }

        if (reader.has("boss-points")) {
            player.setBossPoints(reader.get("boss-points").getAsInt());
        }

        if (reader.has("player-kills")) {
            player.getPlayerKillingAttributes().setPlayerKills(reader.get("player-kills").getAsInt());
        }

        if (reader.has("player-killstreak")) {
            player.getPlayerKillingAttributes()
                    .setPlayerKillStreak(reader.get("player-killstreak").getAsInt());
        }

        if (reader.has("player-deaths")) {
            player.getPlayerKillingAttributes().setPlayerDeaths(reader.get("player-deaths").getAsInt());
        }

        if (reader.has("target-percentage")) {
            player.getPlayerKillingAttributes()
                    .setTargetPercentage(reader.get("target-percentage").getAsInt());
        }

        if (reader.has("bh-rank")) {
            player.getAppearance().setBountyHunterSkull(reader.get("bh-rank").getAsInt());
        }

        if (reader.has("gender")) {
            player.getAppearance().setGender(Gender.valueOf(reader.get("gender").getAsString()));
        }

        if (reader.has("spell-book")) {
            player.setSpellbook(MagicSpellbook.valueOf(reader.get("spell-book").getAsString()));
        }

        if (reader.has("prayer-book")) {
            player.setPrayerbook(Prayerbook.valueOf(reader.get("prayer-book").getAsString()));
        }
        if (reader.has("running")) {
            player.setRunning(reader.get("running").getAsBoolean());
        }
        if (reader.has("run-energy")) {
            player.setRunEnergy(reader.get("run-energy").getAsInt());
        }
        if (reader.has("music")) {
            player.setMusicActive(reader.get("music").getAsBoolean());
        }
        if (reader.has("sounds")) {
            player.setSoundsActive(reader.get("sounds").getAsBoolean());
        }
        if (reader.has("auto-retaliate")) {
            player.setAutoRetaliate(reader.get("auto-retaliate").getAsBoolean());
        }
        if (reader.has("xp-locked")) {
            player.setExperienceLocked(reader.get("xp-locked").getAsBoolean());
        }
        if (reader.has("show-ip")) {
            player.setShowIpAddressOnLogin(reader.get("show-ip").getAsBoolean());
        }
        if (reader.has("show-home")) {
            player.setShowHomeOnLogin(reader.get("show-home").getAsBoolean());
        }
        if (reader.has("veng-cast")) {
            player.setHasVengeance(reader.get("veng-cast").getAsBoolean());
        }
        if (reader.has("last-veng")) {
            player.getLastVengeance().reset(reader.get("last-veng").getAsLong());
        }
        if (reader.has("last-summon")) {
            player.getSummoningTimer().reset(reader.get("last-summon").getAsLong());
        }
        if (reader.has("fight-type")) {
            player.setFightType(FightType.valueOf(reader.get("fight-type").getAsString()));
        }
        if (reader.has("sol-effect")) {
            player.setStaffOfLightEffect(Integer.valueOf(reader.get("sol-effect").getAsInt()));
        }
        if (reader.has("skull-timer")) {
            player.setSkullTimer(reader.get("skull-timer").getAsInt());
        }
        if (reader.has("accept-aid")) {
            player.setAcceptAid(reader.get("accept-aid").getAsBoolean());
        }
        if (reader.has("poison-damage")) {
            player.setPoisonDamage(reader.get("poison-damage").getAsInt());
        }
        if (reader.has("poison-immunity")) {
            player.setPoisonImmunity(reader.get("poison-immunity").getAsInt());
        }
        if (reader.has("venom-damage")) {
            player.setVenomDamage(reader.get("venom-damage").getAsInt());
        }
        if (reader.has("venom-immunity")) {
            player.setVenomImmunity(reader.get("venom-immunity").getAsInt());
        }
        if (reader.has("overload-timer")) {
            player.setOverloadPotionTimer(reader.get("overload-timer").getAsInt());
        }
        if (reader.has("fire-immunity")) {
            player.setFireImmunity(reader.get("fire-immunity").getAsInt());
        }
        if (reader.has("fire-damage-mod")) {
            player.setFireDamageModifier(reader.get("fire-damage-mod").getAsInt());
        }
        if (reader.has("overload-timer")) {
            player.setOverloadPotionTimer(reader.get("overload-timer").getAsInt());
        }
        if (reader.has("prayer-renewal-timer")) {
            player.setPrayerRenewalPotionTimer(reader.get("prayer-renewal-timer").getAsInt());
        }
        if (reader.has("teleblock-timer")) {
            player.setTeleblockTimer(reader.get("teleblock-timer").getAsInt());
        }
        if (reader.has("special-amount")) {
            player.setSpecialPercentage(reader.get("special-amount").getAsInt());
        }

        if (reader.has("entered-gwd-room")) {
            player.getMinigameAttributes().getGodwarsDungeonAttributes()
                    .setHasEnteredRoom(reader.get("entered-gwd-room").getAsBoolean());
        }

        if (reader.has("announced-max")) {
            player.setAnnounceMax(reader.get("announced-max").getAsBoolean());
        }

        if (reader.has("gwd-altar-delay")) {
            player.getMinigameAttributes().getGodwarsDungeonAttributes()
                    .setAltarDelay(reader.get("gwd-altar-delay").getAsLong());
        }

        if (reader.has("gwd-killcount")) {
            player.getMinigameAttributes().getGodwarsDungeonAttributes()
                    .setKillcount(builder.fromJson(reader.get("gwd-killcount"), int[].class));
        }

        if (reader.has("effigy")) {
            player.setEffigy(reader.get("effigy").getAsInt());
        }

        if (reader.has("summon-npc")) {
            int npc = reader.get("summon-npc").getAsInt();
            if (npc > 0)
                player.getSummoning().setFamiliarSpawnTask(new FamiliarSpawnTask(player))
                        .setFamiliarId(npc);
        }
        if (reader.has("summon-death")) {
            int death = reader.get("summon-death").getAsInt();
            if (death > 0 && player.getSummoning().getSpawnTask() != null)
                player.getSummoning().getSpawnTask().setDeathTimer(death);
        }
        if (reader.has("process-farming")) {
            player.setProcessFarming(reader.get("process-farming").getAsBoolean());
        }

        if (reader.has("clanchat")) {
            String clan = reader.get("clanchat").getAsString();
            if (!clan.equals("null"))
                player.setClanChatName(clan);
        }
        if (reader.has("autocast")) {
            player.setAutocast(reader.get("autocast").getAsBoolean());
        }
        if (reader.has("autocast-spell")) {
            int spell = reader.get("autocast-spell").getAsInt();
            if (spell != -1)
                player.setAutocastSpell(CombatSpells.getSpell(spell));
        }

        if (reader.has("dfs-charges")) {
            player.incrementDfsCharges(reader.get("dfs-charges").getAsInt());
        }
        if (reader.has("kills")) {
            KillsTracker.submit(player,
                    builder.fromJson(reader.get("kills").getAsJsonArray(), KillsEntry[].class));
        }

        if (reader.has("drops")) {
            DropLog.submit(player,
                    builder.fromJson(reader.get("drops").getAsJsonArray(), DropLogEntry[].class));
        }

        if (reader.has("coins-gambled")) {
            player.getAchievementAttributes().setCoinsGambled(reader.get("coins-gambled").getAsInt());
        }

        if (reader.has("slayer-master")) {
            player.getSlayer()
                    .setSlayerMaster(SlayerMaster.valueOf(reader.get("slayer-master").getAsString()));
        }

        if (reader.has("slayer-task")) {
            player.getSlayer()
                    .setSlayerTask(SlayerTasks.valueOf(reader.get("slayer-task").getAsString()));
        }

        if (reader.has("prev-slayer-task")) {
            player.getSlayer()
                    .setLastTask(SlayerTasks.valueOf(reader.get("prev-slayer-task").getAsString()));
        }

        if (reader.has("task-amount")) {
            player.getSlayer().setAmountToSlay(reader.get("task-amount").getAsInt());
        }

        if (reader.has("task-streak")) {
            player.getSlayer().setTaskStreak(reader.get("task-streak").getAsInt());
        }

        if (reader.has("duo-partner")) {
            String partner = reader.get("duo-partner").getAsString();
            player.getSlayer().setDuoPartner(partner.equals("null") ? null : partner);
        }

        if (reader.has("double-slay-xp")) {
            player.getSlayer().doubleSlayerXP = reader.get("double-slay-xp").getAsBoolean();
        }

        if (reader.has("recoil-deg")) {
            player.setRecoilCharges(reader.get("recoil-deg").getAsInt());
        }

        if (reader.has("home-loc")) {
            player.setHomeLocation(reader.get("home-loc").getAsInt());
        }

        if (reader.has("brawler-deg")) {
            player.setBrawlerCharges(
                    builder.fromJson(reader.get("brawler-deg").getAsJsonArray(), int[].class));
        }

        if (reader.has("vesta-deg")) {
            player.setVestaCharges(builder.fromJson(reader.get("vesta-deg").getAsJsonArray(), int[].class));
        }

        if (reader.has("zuriel-deg")) {
            player.setZurielsCharges(builder.fromJson(reader.get("zuriel-deg").getAsJsonArray(), int[].class));
        }

        if (reader.has("statius-deg")) {
            player.setStatiusCharges(builder.fromJson(reader.get("statius-deg").getAsJsonArray(), int[].class));
        }

        if (reader.has("morrigans-deg")) {
            player.setMorrigansCharges(builder.fromJson(reader.get("morrigans-deg").getAsJsonArray(), int[].class));
        }
        if (reader.has("corrupt-vesta-deg")) {
            player.setVestaCharges(builder.fromJson(reader.get("vesta-deg").getAsJsonArray(), int[].class));
        }

        if (reader.has("corrupt-zuriel-deg")) {
            player.setZurielsCharges(builder.fromJson(reader.get("zuriel-deg").getAsJsonArray(), int[].class));
        }

        if (reader.has("corrupt-statius-deg")) {
            player.setStatiusCharges(builder.fromJson(reader.get("statius-deg").getAsJsonArray(), int[].class));
        }

        if (reader.has("corrupt-morrigans-deg")) {
            player.setMorrigansCharges(builder.fromJson(reader.get("morrigans-deg").getAsJsonArray(), int[].class));
        }

        if (reader.has("killed-players")) {
            List<String> list = new ArrayList<String>();
            String[] killed_players =
                    builder.fromJson(reader.get("killed-players").getAsJsonArray(), String[].class);
            for (String s : killed_players)
                list.add(s);
            player.getPlayerKillingAttributes().setKilledPlayers(list);
        }

        if (reader.has("killed-gods")) {
            player.getAchievementAttributes().setGodsKilled(
                    builder.fromJson(reader.get("killed-gods").getAsJsonArray(), boolean[].class));
        }

        if (reader.has("barrows-brother")) {
            player.getMinigameAttributes().getBarrowsMinigameAttributes().setBarrowsData(
                    builder.fromJson(reader.get("barrows-brother").getAsJsonArray(), int[][].class));
        }

        if (reader.has("random-coffin")) {
            player.getMinigameAttributes().getBarrowsMinigameAttributes()
                    .setRandomCoffin((reader.get("random-coffin").getAsInt()));
        }

        if (reader.has("barrows-killcount")) {
            player.getMinigameAttributes().getBarrowsMinigameAttributes()
                    .setKillcount((reader.get("barrows-killcount").getAsInt()));
        }

        if (reader.has("nomad")) {
            player.getMinigameAttributes().getNomadAttributes()
                    .setQuestParts(builder.fromJson(reader.get("nomad").getAsJsonArray(), boolean[].class));
        }

        if (reader.has("claw-quest")) {
            player.getMinigameAttributes().getClawQuestAttributes()
                    .setQuestParts(reader.get("claw-quest").getAsInt());
        }

        if (reader.has("shrek1-quest")) {
            player.getMinigameAttributes().getShrek1Attributes()
                    .setQuestParts(reader.get("shrek1-quest").getAsInt());
        }

        if (reader.has("farm-quest")) {
            player.getMinigameAttributes().getFarmQuestAttributes()
                    .setQuestParts(reader.get("farm-quest").getAsInt());
        }

        if (reader.has("recipe-for-disaster")) {
            player.getMinigameAttributes().getRecipeForDisasterAttributes().setQuestParts(
                    builder.fromJson(reader.get("recipe-for-disaster").getAsJsonArray(), boolean[].class));
        }

        if (reader.has("recipe-for-disaster-wave")) {
            player.getMinigameAttributes().getRecipeForDisasterAttributes()
                    .setWavesCompleted((reader.get("recipe-for-disaster-wave").getAsInt()));
        }

        if (reader.has("dung-items-bound")) {
            player.getMinigameAttributes().getDungeoneeringAttributes().setBoundItems(
                    builder.fromJson(reader.get("dung-items-bound").getAsJsonArray(), int[].class));
        }

        if (reader.has("rune-ess")) {
            player.setStoredRuneEssence((reader.get("rune-ess").getAsInt()));
        }

        if (reader.has("pure-ess")) {
            player.setStoredPureEssence((reader.get("pure-ess").getAsInt()));
        }

        if (reader.has("bank-pin")) {
            player.getBankPinAttributes()
                    .setBankPin(builder.fromJson(reader.get("bank-pin").getAsJsonArray(), int[].class));
        }

        if (reader.has("has-bank-pin")) {
            player.getBankPinAttributes().setHasBankPin(reader.get("has-bank-pin").getAsBoolean());
        }
        if (reader.has("last-pin-attempt")) {
            player.getBankPinAttributes().setLastAttempt(reader.get("last-pin-attempt").getAsLong());
        }
        if (reader.has("invalid-pin-attempts")) {
            player.getBankPinAttributes()
                    .setInvalidAttempts(reader.get("invalid-pin-attempts").getAsInt());
        }

        if (reader.has("bank-pin")) {
            player.getBankPinAttributes()
                    .setBankPin(builder.fromJson(reader.get("bank-pin").getAsJsonArray(), int[].class));
        }

        if (reader.has("appearance")) {
            player.getAppearance()
                    .set(builder.fromJson(reader.get("appearance").getAsJsonArray(), int[].class));
        }

        if (reader.has("agility-obj")) {
            player.setCrossedObstacles(
                    builder.fromJson(reader.get("agility-obj").getAsJsonArray(), boolean[].class));
        }

        if (reader.has("skills")) {
            player.getSkillManager().setSkills(builder.fromJson(reader.get("skills"), Skills.class));
        }
        if (reader.has("inventory")) {
            player.getInventory()
                    .setItems(builder.fromJson(reader.get("inventory").getAsJsonArray(), Item[].class));
        }
        if (reader.has("equipment")) {
            player.getEquipment()
                    .setItems(builder.fromJson(reader.get("equipment").getAsJsonArray(), Item[].class));
        }

        for (int i = 0; i < 9; i++) {
            if (reader.has("bank-" + i + ""))
                player.setBank(i, new Bank(player)).getBank(i).addItems(
                        builder.fromJson(reader.get("bank-" + i + "").getAsJsonArray(), Item[].class), false);
        }

        if (reader.has("bank-0")) {
            player.setBank(0, new Bank(player)).getBank(0)
                    .addItems(builder.fromJson(reader.get("bank-0").getAsJsonArray(), Item[].class), false);
        }
        if (reader.has("bank-1")) {
            player.setBank(1, new Bank(player)).getBank(1)
                    .addItems(builder.fromJson(reader.get("bank-1").getAsJsonArray(), Item[].class), false);
        }
        if (reader.has("bank-2")) {
            player.setBank(2, new Bank(player)).getBank(2)
                    .addItems(builder.fromJson(reader.get("bank-2").getAsJsonArray(), Item[].class), false);
        }
        if (reader.has("bank-3")) {
            player.setBank(3, new Bank(player)).getBank(3)
                    .addItems(builder.fromJson(reader.get("bank-3").getAsJsonArray(), Item[].class), false);
        }
        if (reader.has("bank-4")) {
            player.setBank(4, new Bank(player)).getBank(4)
                    .addItems(builder.fromJson(reader.get("bank-4").getAsJsonArray(), Item[].class), false);
        }
        if (reader.has("bank-5")) {
            player.setBank(5, new Bank(player)).getBank(5)
                    .addItems(builder.fromJson(reader.get("bank-5").getAsJsonArray(), Item[].class), false);
        }
        if (reader.has("bank-6")) {
            player.setBank(6, new Bank(player)).getBank(6)
                    .addItems(builder.fromJson(reader.get("bank-6").getAsJsonArray(), Item[].class), false);
        }
        if (reader.has("bank-7")) {
            player.setBank(7, new Bank(player)).getBank(7)
                    .addItems(builder.fromJson(reader.get("bank-7").getAsJsonArray(), Item[].class), false);
        }
        if (reader.has("bank-8")) {
            player.setBank(8, new Bank(player)).getBank(8)
                    .addItems(builder.fromJson(reader.get("bank-8").getAsJsonArray(), Item[].class), false);
        }

        if (reader.has("ge-slots")) {
            GrandExchangeSlot[] slots =
                    builder.fromJson(reader.get("ge-slots").getAsJsonArray(), GrandExchangeSlot[].class);
            player.setGrandExchangeSlots(slots);
        }

        if (reader.has("store")) {
            Item[] validStoredItems =
                    builder.fromJson(reader.get("store").getAsJsonArray(), Item[].class);
            if (player.getSummoning().getSpawnTask() != null)
                player.getSummoning().getSpawnTask().setValidItems(validStoredItems);
        }

        if (reader.has("charm-imp")) {
            int[] charmImpConfig =
                    builder.fromJson(reader.get("charm-imp").getAsJsonArray(), int[].class);
            player.getSummoning().setCharmimpConfig(charmImpConfig);
        }

        if (reader.has("friends")) {
            long[] friends = builder.fromJson(reader.get("friends").getAsJsonArray(), long[].class);

            for (long l : friends) {
                player.getRelations().getFriendList().add(l);
            }
        }
        if (reader.has("ignores")) {
            long[] ignores = builder.fromJson(reader.get("ignores").getAsJsonArray(), long[].class);

            for (long l : ignores) {
                player.getRelations().getIgnoreList().add(l);
            }
        }

        if (reader.has("loyalty-titles")) {
            player.setUnlockedLoyaltyTitles(
                    builder.fromJson(reader.get("loyalty-titles").getAsJsonArray(), boolean[].class));
        }

        if (reader.has("achievements-completion")) {
            player.getAchievementAttributes().setCompletion(builder
                    .fromJson(reader.get("achievements-completion").getAsJsonArray(), boolean[].class));
        }

        if (reader.has("achievements-progress")) {
            player.getAchievementAttributes().setProgress(
                    builder.fromJson(reader.get("achievements-progress").getAsJsonArray(), int[].class));
        }

        if (reader.has("object")) {
           player.lastDuelRules = builder
                    .fromJson(reader.get("object").getAsJsonArray(), boolean[].class);
        }
        return reader;
    }

}
