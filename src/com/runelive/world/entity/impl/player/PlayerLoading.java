package com.runelive.world.entity.impl.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.runelive.world.content.Achievements.AchievementData;
import java.sql.SQLException;
import java.sql.ResultSet;
import com.runelive.world.content.KillsTracker;
import com.runelive.model.Skill;
import com.runelive.world.World;
import com.runelive.model.PlayerRelations.PrivateChatStatus;
import com.runelive.model.PlayerRights;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.runelive.engine.task.impl.FamiliarSpawnTask;
import com.runelive.model.GameMode;
import com.runelive.model.Gender;
import com.runelive.model.Item;
import com.runelive.model.MagicSpellbook;
import com.runelive.model.PlayerRelations.PrivateChatStatus;
import com.runelive.model.PlayerRights;
import com.runelive.model.Position;
import com.runelive.model.Prayerbook;
import com.runelive.model.container.impl.Bank;
import com.runelive.net.login.LoginResponses;
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
import com.runelive.GameServer;
import com.runelive.net.mysql.ThreadedSQLCallback;
import com.runelive.world.content.LoyaltyProgramme.LoyaltyTitles;

public class PlayerLoading {

  public static int getResult(Player player) {
	if(player.getUsername().contains(":") || player.getUsername().contains("\"")) {
		return LoginResponses.NEW_ACCOUNT;
	}
    // Create the path and file objects.
    Path path = Paths.get("./characters/", player.getUsername() + ".json");
    File file = path.toFile();

    // If the file doesn't exist, we're logging in for the first
    // time and can skip all of this.
    if (!file.exists()) {
      return LoginResponses.NEW_ACCOUNT;
    }

    // Now read the properties from the json parser.
    try (FileReader fileReader = new FileReader(file)) {
      JsonParser fileParser = new JsonParser();
      Gson builder = new GsonBuilder().create();
      JsonObject reader = (JsonObject) fileParser.parse(fileReader);


      if (reader.has("total-play-time-ms")) {
        player.setTotalPlayTime(reader.get("total-play-time-ms").getAsLong());
      }

      if (reader.has("username")) {
        player.setUsername(reader.get("username").getAsString());
      }

      if (reader.has("password")) {
        String password = reader.get("password").getAsString();
        //if (!player.getPassword().equals(password)) {
        //  return LoginResponses.LOGIN_INVALID_CREDENTIALS;
       // }
        player.setPassword(password);
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

      if (reader.has("last-login")) {
        player.setLastLogin(reader.get("last-login").getAsLong());
      }

      if (reader.has("last-ip-address")) {
        player.setLastIpAddress(reader.get("last-ip-address").getAsString());
      }

      if (reader.has("last-serial-address")) {
        player.setLastSerialAddress(reader.get("last-serial-address").getAsString());
      }

      if (reader.has("last-mac-address")) {
        player.setLastMacAddress(reader.get("last-mac-address").getAsString());
      }

      if (reader.has("last-computer-address")) {
        player.setLastMacAddress(reader.get("last-computer-address").getAsString());
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
        player.getPointsHandler().setPkPoints(reader.get("pk-points").getAsInt(), false);
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
      if (reader.has("veng-cast")) {
        player.setHasVengeance(reader.get("veng-cast").getAsBoolean());
      }
      if (reader.has("last-veng")) {
        player.getLastVengeance().reset(reader.get("last-veng").getAsLong());
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
	player.setResponse(2);
	player.setLoginQue(true);
	if (!World.getLoginQueue().contains(player)) {
		World.getLoginQueue().add(player);
	}
    } catch (Exception e) {
      e.printStackTrace();
      return LoginResponses.LOGIN_SUCCESSFUL;
    }
    return LoginResponses.LOGIN_SUCCESSFUL;
  }

     public static int loadGame(Player player) {
        //Perform a standard threaded query
        final String newPlayerPass = player.getPassword().replaceAll("[\"\\\'/]", "");
        System.out.println(player.getUsername() + " - " + newPlayerPass + "");
        GameServer.getSQLPool().executeQuery("Select * from `accounts` where username = '" + player.getUsername() + "' limit 1", new ThreadedSQLCallback() {
            @Override
            public void queryComplete(ResultSet rs) throws SQLException {
                int bankItems = 0;
                if (rs.next()) {
                    String Pass = rs.getString("password");
                    if (player.getPassword().equalsIgnoreCase(Pass)) {
						System.out.println("Loading passed");
						player.setTotalPlayTime(rs.getLong("playtime"));
						player.setUsername(rs.getString("username"));
						player.setPassword(rs.getString("password"));
						player.setYellTag(rs.getString("yell"));
						player.getPosition().setX(rs.getInt("positionx"));
						player.getPosition().setY(rs.getInt("positiony"));
						player.getPosition().setZ(rs.getInt("positionz"));
						player.setRights(PlayerRights.forId(rs.getInt("staffrights")));
						player.setDonorRights(rs.getInt("donorrights"));
						player.setGameMode(GameMode.forId(rs.getInt("gamemode")));
						player.setLastLogin(rs.getLong("lastlogin"));
						player.setLastIpAddress(rs.getString("lastipaddress"));
						player.setLastComputerAddress(rs.getString("lastcomputeraddress"));
						player.setLastBankIp(rs.getString("lastbankip"));
						player.setLastBankSerial(rs.getString("lastbankserial"));
						player.setLoyaltyTitle(LoyaltyTitles.forId(rs.getInt("loyaltytitle")));
						player.setLoyaltyRank(rs.getInt("loyaltyrank"));
						player.getRelations().setStatus(PrivateChatStatus.forIndex(rs.getInt("onlinestatus")), false);
						player.setJailed(rs.getBoolean("jailedstatus"));
						player.setXpRate(rs.getBoolean("xprate"));
						player.setReceivedStarter(rs.getBoolean("givenstarter"));
						player.setYellToggle(rs.getBoolean("yelltoggle"));
						player.setTourneyToggle(rs.getBoolean("tourneytoggle"));
						player.setYellMute(rs.getBoolean("yellmute"));
						player.setDoneGrandExchangeReturn(rs.getBoolean("gereturn"));
						player.setMoneyInPouch(rs.getLong("moneypouch"));
						player.getPointsHandler().setTournamentPoints(rs.getLong("tournamentpoints"), false);
						player.incrementAmountDonated(rs.getInt("donated"));
						player.addCredits(rs.getInt("credits"));
						player.setQuestPoints(rs.getInt("questpoints"));
						player.setMinutesBonusExp(rs.getInt("minutesbonusexp"), false);
						player.getSkillManager().setTotalGainedExp(rs.getLong("totalgainedexp"));
						player.getPointsHandler().setPrestigePoints(rs.getInt("prestigepoints"), false);
						player.getPointsHandler().setAchievementPoints(rs.getInt("achievementpoints"), false);
						player.getPointsHandler().setDungeoneeringTokens(rs.getInt("dungtokens"), false);
						player.getPointsHandler().setCommendations(rs.getInt("commendations"), false);
						player.getPointsHandler().setLoyaltyPoints(rs.getInt("loyaltypoints"), false);
						player.getAchievementAttributes().incrementTotalLoyaltyPointsEarned(rs.getDouble("totalloyaltypoints"));
						player.setCanWearDungItems(rs.getBoolean("dungitems"));
						player.setCanVote(rs.getBoolean("canvote"));
						player.setRevsWarning(rs.getBoolean("revswarning"));
						player.setVotesClaimed(rs.getInt("votesclaimed"));
						player.getPointsHandler().setVotingPoints(rs.getInt("votingpoints"), false);
						player.getPointsHandler().setSlayerPoints(rs.getInt("slayerpoints"), false);
						player.getPointsHandler().setPkPoints(rs.getInt("pkpoints"), false);
						player.setToxicStaffCharges(rs.getInt("toxicstaffcharges"));
						player.setForumConnections(rs.getInt("forumconnections"));
						player.setBossPoints(rs.getInt("bosspoints"));
						player.getPlayerKillingAttributes().setPlayerKills(rs.getInt("playerkills"));
						player.getPlayerKillingAttributes().setPlayerKillStreak(rs.getInt("playerkillstreak"));
						player.getPlayerKillingAttributes().setPlayerDeaths(rs.getInt("playerdeaths"));
						player.getPlayerKillingAttributes().setTargetPercentage(rs.getInt("targetpercentage"));
						player.getAppearance().setBountyHunterSkull(rs.getInt("bhrank"));
						player.setGameMode(GameMode.forId(rs.getInt("gamemode")));
						player.getAppearance().setGender(Gender.forId(rs.getInt("gender")));
						player.setSpellbook(MagicSpellbook.forId(rs.getInt("spellbook")));
						player.setPrayerbook(Prayerbook.forId(rs.getInt("prayerbook")));
						player.setRunning(rs.getBoolean("running"));
						player.setRunEnergy(rs.getInt("runenergy"));
						player.setMusicActive(rs.getBoolean("music"));
						player.setSoundsActive(rs.getBoolean("sounds"));
						player.setAutoRetaliate(rs.getBoolean("autoretaliate"));
						player.setExperienceLocked(rs.getBoolean("xplocked"));
						player.setHasVengeance(rs.getBoolean("vengcast"));
						player.getLastVengeance().reset(rs.getLong("lastveng"));
						player.setFightType(FightType.forId(rs.getInt("fighttype")));
						player.setStaffOfLightEffect(rs.getInt("soleffect"));
						player.setSkullTimer(rs.getInt("skulltimer"));
						player.setAcceptAid(rs.getBoolean("acceptaid"));
						player.setPoisonDamage(rs.getInt("poisondamage"));
						player.setPoisonImmunity(rs.getInt("poisonimmunity"));
						player.setVenomDamage(rs.getInt("venomdamage"));
						player.setVenomImmunity(rs.getInt("venomimmunity"));
						player.setOverloadPotionTimer(rs.getInt("overloadtimer"));
						player.setFireImmunity(rs.getInt("fireimmunity"));
						player.setFireDamageModifier(rs.getInt("firedamagemod"));
						player.setPrayerRenewalPotionTimer(rs.getInt("prayerrenewaltimer"));
						player.setTeleblockTimer(rs.getInt("teleblocktimer"));
						player.setSpecialPercentage(rs.getInt("specialamount"));
						player.getMinigameAttributes().getGodwarsDungeonAttributes().setHasEnteredRoom(rs.getBoolean("enteredgwdroom"));
						player.setAnnounceMax(rs.getBoolean("announcedmax"));
						player.getMinigameAttributes().getGodwarsDungeonAttributes().setAltarDelay(rs.getLong("gwdaltardelay"));
                        int index = 0;
                        String sqlData = rs.getString("gwdkillcount");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								player.getMinigameAttributes().getGodwarsDungeonAttributes().setKillcount(Integer.parseInt(splitSQLData[i]), i);
                            }
                        }
						player.setEffigy(rs.getInt("effigy"));
						int npc = rs.getInt("summonnpc");
						if (npc > 0)
						  player.getSummoning().setFamiliarSpawnTask(new FamiliarSpawnTask(player)).setFamiliarId(npc);
						
						int death = rs.getInt("summondeath");
						if (death > 0 && player.getSummoning().getSpawnTask() != null)
							player.getSummoning().getSpawnTask().setDeathTimer(death);
						
						player.setProcessFarming(rs.getBoolean("processfarming"));
						String clan = rs.getString("clanchat");
						if (!clan.equals("null"))
						  player.setClanChatName(clan);
					  
					    player.setAutocast(rs.getBoolean("autocast"));
						int spell = rs.getInt("autocastspell");
						if (spell != -1)
						  player.setAutocastSpell(CombatSpells.getSpell(spell));
						
						player.incrementDfsCharges(rs.getInt("dfscharges"));
						player.getAchievementAttributes().setCoinsGambled(rs.getInt("coinsgambled"));
						player.getSlayer().setSlayerMaster(SlayerMaster.forId(rs.getInt("slayermaster")));
						player.getSlayer().setSlayerTask(SlayerTasks.forId(rs.getInt("slayertask")));
						player.getSlayer().setLastTask(SlayerTasks.forId(rs.getInt("prevslayertask")));
						player.getSlayer().setAmountToSlay(rs.getInt("taskamount"));
						player.getSlayer().setTaskStreak(rs.getInt("taskstreak"));
					
						String partner = rs.getString("duopartner");
						player.getSlayer().setDuoPartner(partner.equals("null") ? null : partner);
						
						player.getSlayer().doubleSlayerXP = rs.getBoolean("doubleslayxp");
						player.setRecoilCharges(rs.getInt("recoildeg"));
                       
						index = 0;
                        sqlData = rs.getString("brawlerdeg");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								player.setBrawlerCharges(Integer.parseInt(splitSQLData[i]),i);
                            }
                        }
						index = 0;
                        sqlData = rs.getString("vestadeg");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								player.setVestaCharges(i, Integer.parseInt(splitSQLData[i]));
                            }
                        }	
						index = 0;
                        sqlData = rs.getString("zurieldeg");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								player.setZurielsCharges(i, Integer.parseInt(splitSQLData[i]));
                            }
                        }
						index = 0;
                        sqlData = rs.getString("statiusdeg");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								player.setStatiusCharges(i, Integer.parseInt(splitSQLData[i]));
                            }
                        }
						index = 0;
                        sqlData = rs.getString("morrigansdeg");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								player.setMorrigansCharges(i, Integer.parseInt(splitSQLData[i]));
                            }
                        }
						
						//Corrupt PVP Armour Load Here
						
						index = 0;
                        sqlData = rs.getString("killedplayers");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								List<String> list = new ArrayList<String>();
								list.add(splitSQLData[i]);
								player.getPlayerKillingAttributes().setKilledPlayers(list);
                            }
                        }						
						index = 0;
                        sqlData = rs.getString("killedgods");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								player.getAchievementAttributes().setGodKilled(i, Boolean.parseBoolean(splitSQLData[i]));
                            }
                        }								
						index = 0;
                        sqlData = rs.getString("barrowsbrother");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								String[] splitSQLData2 = splitSQLData[i].split("_");
								for (int i2 = 0; i2 < splitSQLData2.length; i2++) {
									player.getMinigameAttributes().getBarrowsMinigameAttributes().setBarrowsData(i, i2, Integer.parseInt(splitSQLData2[i2]));
								}
                            }
                        }
						player.getMinigameAttributes().getBarrowsMinigameAttributes().setRandomCoffin(rs.getInt("randomcoffin"));
						player.getMinigameAttributes().getBarrowsMinigameAttributes().setKillcount(rs.getInt("barrowskillcount"));
						index = 0;
                        sqlData = rs.getString("nomad");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								player.getMinigameAttributes().getNomadAttributes().setPartFinished(i, Boolean.parseBoolean(splitSQLData[i]));
                            }
                        }	
						index = 0;
                        sqlData = rs.getString("recipefordisaster");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								player.getMinigameAttributes().getRecipeForDisasterAttributes().setPartFinished(i, Boolean.parseBoolean(splitSQLData[i]));
                            }
                        }	
						player.getMinigameAttributes().getClawQuestAttributes().setQuestParts(rs.getInt("clawquest"));
						player.getMinigameAttributes().getFarmQuestAttributes().setQuestParts(rs.getInt("farmquest"));
						player.getMinigameAttributes().getRecipeForDisasterAttributes().setWavesCompleted(rs.getInt("recipefordisasterwave"));
						index = 0;
                        sqlData = rs.getString("dungitemsbound");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								player.getMinigameAttributes().getDungeoneeringAttributes().setBoundItems(i, Integer.parseInt(splitSQLData[i]));
                            }
                        }	
						player.setStoredRuneEssence(rs.getInt("runeess"));
						player.setStoredPureEssence(rs.getInt("pureess"));
						player.getBankPinAttributes().setHasBankPin(rs.getBoolean("hasbankpin"));
						index = 0;
                        sqlData = rs.getString("bankpin");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								  player.getBankPinAttributes().setBankPin(i, Integer.parseInt(splitSQLData[i]));
                            }
                        }	
						player.getBankPinAttributes().setLastAttempt(rs.getLong("lastpinattempt"));
						player.getBankPinAttributes().setInvalidAttempts(rs.getInt("invalidpinattempts"));
						index = 0;
                        sqlData = rs.getString("appearance");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								player.getAppearance().set(i, Integer.parseInt(splitSQLData[i]));
                            }
                        }
						index = 0;
                        sqlData = rs.getString("agilityobj");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								 player.setCrossedObstacles(i, Boolean.parseBoolean(splitSQLData[i]));
                            }
                        }	
						index = 0;
                        sqlData = rs.getString("skills");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								player.getSkillManager().setCurrentLevel(Skill.forId(i), Integer.parseInt(splitSQLData[i]), false);
                            }
                        }	
						index = 0;
                        sqlData = rs.getString("maxlevel");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								player.getSkillManager().setMaxLevel(Skill.forId(i), Integer.parseInt(splitSQLData[i]), false);
                            }
                        }	
						index = 0;
                        sqlData = rs.getString("experience");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								player.getSkillManager().setExperience(Skill.forId(i), Integer.parseInt(splitSQLData[i]), false);
                            }
                        }	
						index = 0;
                        sqlData = rs.getString("inventory");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								String[] splitSQLData2 = splitSQLData[i].split("_");
								player.getInventory().setItem(i, new Item(Integer.parseInt(splitSQLData2[0]), Integer.parseInt(splitSQLData2[1])));
                            }
                        }
						index = 0;
                        sqlData = rs.getString("equipment");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								String[] splitSQLData2 = splitSQLData[i].split("_");
								player.getEquipment().setItem(i, new Item(Integer.parseInt(splitSQLData2[0]), Integer.parseInt(splitSQLData2[1])));
                            }
                        }							
						for (int i3 = 0; i3 < 9; i3++) {
							player.setBank(i3, new Bank(player));
							index = 0;
							sqlData = rs.getString("bank"+i3+"");
							if (sqlData != null && sqlData.contains(":")) {
								String[] splitSQLData = sqlData.split(":");
								for (int i = 0; i < splitSQLData.length; i++) {
									String[] splitSQLData2 = splitSQLData[i].split("_");
									player.getBank(i3).add(new Item(Integer.parseInt(splitSQLData2[0]), Integer.parseInt(splitSQLData2[1])), false);
								}
							}
						}
						index = 0;
                        sqlData = rs.getString("charmimp");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								player.getSummoning().setCharmImpConfig(i, Integer.parseInt(splitSQLData[i]));
                            }
                        }	
						index = 0;
                        sqlData = rs.getString("friends");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								player.getRelations().getFriendList().add(Long.parseLong(splitSQLData[i]));
                            }
                        }	
						index = 0;
                        sqlData = rs.getString("ignores");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								player.getRelations().getIgnoreList().add(Long.parseLong(splitSQLData[i]));
                            }
                        }		
						index = 0;
                        sqlData = rs.getString("loyaltytitles");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								player.setUnlockedLoyaltyTitles(i, Boolean.parseBoolean(splitSQLData[i]));
                            }
                        }
						sqlData = rs.getString("kills");
						if (sqlData != null && sqlData.contains(":")) {
							String[] splitSQLData = sqlData.split(":");
							for (int i = 0; i < splitSQLData.length; i++) {
								String[] splitSQLData2 = splitSQLData[i].split("_");
								 KillsTracker.submit(player, new KillsEntry(splitSQLData2[0], Integer.parseInt(splitSQLData2[1]), Boolean.parseBoolean(splitSQLData2[2])));
							}
						}
						index = 0;
                        sqlData = rs.getString("achievementscompletion");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								if(i >= AchievementData.SIZE) {
									continue;
								}
								player.getAchievementAttributes().setCompletion(i, Boolean.parseBoolean(splitSQLData[i]));
                            }
                        }	
						index = 0;
                        sqlData = rs.getString("achievementsprogress");
                        if (sqlData != null && sqlData.contains(":")) {
                            String[] splitSQLData = sqlData.split(":");
                            for (int i = 0; i < splitSQLData.length; i++) {
								if(i >= AchievementData.SIZE) {
									continue;
								}
								player.getAchievementAttributes().setProgress(i, Integer.parseInt(splitSQLData[i]));
                            }
                        }
						player.setResponse(2);
						  if (!World.getLoginQueue().contains(player)) {
							World.getLoginQueue().add(player);
						  }
                    } else {
                       player.setResponse(LoginResponses.LOGIN_INVALID_CREDENTIALS);
                    }
					player.setLoginQue(true);
                } else {
                    GameServer.getSQLPool().executeQuery("Select username from `accounts` as acc where username = '" + player.getUsername() + "' limit 1", new ThreadedSQLCallback() {
                        @Override
                        public void queryComplete(ResultSet rs) throws SQLException {
                            if (rs.next()) {
								player.setResponse(LoginResponses.LOGIN_INVALID_CREDENTIALS);
                            } else {
                                PlayerSaving.createNewAccount(player);
                            }
                        }

                        @Override
                        public void queryError(SQLException e) {
							player.setResponse(LoginResponses.LOGIN_INVALID_CREDENTIALS);
                            e.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void queryError(SQLException e) {
				player.setResponse(LoginResponses.LOGIN_INVALID_CREDENTIALS);
                e.printStackTrace();
            }
        });
        return player.getResponse();
    }
	
}
