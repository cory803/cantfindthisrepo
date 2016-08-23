package com.chaos.world.entity.impl.player;

import static com.chaos.world.content.Lottery.onLogin;

import com.chaos.GameServer;
import com.chaos.GameSettings;
import com.chaos.engine.task.TaskManager;
import com.chaos.engine.task.impl.BonusExperienceTask;
import com.chaos.engine.task.impl.CombatSkullEffect;
import com.chaos.engine.task.impl.FireImmunityTask;
import com.chaos.engine.task.impl.OverloadPotionTask;
import com.chaos.engine.task.impl.PlayerSkillsTask;
import com.chaos.engine.task.impl.PlayerSpecialAmountTask;
import com.chaos.engine.task.impl.PrayerRenewalPotionTask;
import com.chaos.engine.task.impl.StaffOfLightSpecialAttackTask;
import com.chaos.model.Flag;
import com.chaos.model.Locations;
import com.chaos.model.PlayerRights;
import com.chaos.model.Position;
import com.chaos.model.Skill;
import com.chaos.model.container.impl.Bank;
import com.chaos.model.container.impl.Equipment;
import com.chaos.model.definitions.WeaponAnimations;
import com.chaos.model.definitions.WeaponInterfaces;
import com.chaos.model.input.impl.ChangePassword;
import com.chaos.model.player.GameMode;
import com.chaos.net.PlayerSession;
import com.chaos.net.SessionState;
import com.chaos.net.login.LoginResponses;
//import org.scripts.kotlin.core.login.LoginChecksParser;
//import org.scripts.kotlin.core.login.LoginLoaderAssetts;
//import org.scripts.kotlin.core.login.LoginMessageParser;
import com.chaos.net.security.ConnectionHandler;
import com.chaos.util.Misc;
import com.chaos.world.World;
import com.chaos.world.content.*;
import com.chaos.world.content.Achievements.AchievementData;
import com.chaos.world.content.clan.ClanChatManager;
import com.chaos.world.content.combat.effect.CombatPoisonEffect;
import com.chaos.world.content.combat.effect.CombatTeleblockEffect;
import com.chaos.world.content.combat.effect.CombatVenomEffect;
import com.chaos.world.content.combat.magic.Autocasting;
import com.chaos.world.content.combat.prayer.CurseHandler;
import com.chaos.world.content.combat.prayer.PrayerHandler;
import com.chaos.world.content.combat.pvp.BountyHunter;
import com.chaos.world.content.combat.range.DwarfMultiCannon;
import com.chaos.world.content.combat.weapon.CombatSpecial;
import com.chaos.world.content.dialogue.DialogueManager;
import com.chaos.world.content.dialogue.impl.Tutorial;
import com.chaos.world.content.minigames.impl.Barrows;
import com.chaos.world.content.pos.PlayerOwnedShops;
import com.chaos.world.content.skill.impl.hunter.Hunter;
import com.chaos.world.content.skill.impl.slayer.Slayer;
import com.chaos.world.content.tasks.DailyTaskManager;
import com.chaos.world.entity.impl.npc.NPC;

public class PlayerHandler {

	public static void displayCombatLevels(Player player) {
		int low_combat = player.getSkillManager().getCombatLevel() - 15;
		int highest_combat = player.getSkillManager().getCombatLevel() + 15;
		if(low_combat >= 3 && highest_combat <= 126) {
			player.getPacketSender().sendString(199, "@or2@" + low_combat + "-" + highest_combat + "");
			return;
		} else if(low_combat < 3) {
			player.getPacketSender().sendString(199, "@or2@3-" + highest_combat + "");
			return;
		} else if(highest_combat >= 127) {
			player.getPacketSender().sendString(199, "@or2@"+low_combat+"-126");
			return;
		}
	}

	public static void handleLogin(Player player) {
		// player.setLoginQue(false);
		player.setResponse(LoginResponses.LOGIN_SUCCESSFUL);
		// Register the player
		System.out.println("[World] Registering player - [username, host] : [" + player.getUsername() + ", "
				+ player.getHostAddress() + "]");
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

		// Here we are checking if a player is at coords 2602, 5713 (Old
		// tormented demon spawn, and will
		// be moving them to home)
		if (player.getPosition().equals(new Position(2602, 5713))) {
			player.moveTo(new Position(3087, 3502));
			System.out.println("Moved player " + player.getUsername() + " for being in a bad area.");
		}

		//Don't remove this, has to be here due to a game mode bug
		if (player.reset_stats_1 == 0) {
			if (player.getGameModeAssistant().getGameMode() != GameMode.SIR && player.getGameModeAssistant().getGameMode() != GameMode.LORD) {
				player.getSkillManager().setCurrentLevel(Skill.RUNECRAFTING, 1);
				player.getSkillManager().setMaxLevel(Skill.RUNECRAFTING, 1);
				player.getSkillManager().setExperience(Skill.RUNECRAFTING, 1);
				player.getSkillManager().setCurrentLevel(Skill.FARMING, 1);
				player.getSkillManager().setMaxLevel(Skill.FARMING, 1);
				player.getSkillManager().setExperience(Skill.FARMING, 1);
				player.getSkillManager().setCurrentLevel(Skill.AGILITY, 1);
				player.getSkillManager().setMaxLevel(Skill.AGILITY, 1);
				player.getSkillManager().setExperience(Skill.AGILITY, 1);
				player.getSkillManager().setCurrentLevel(Skill.FLETCHING, 1);
				player.getSkillManager().setMaxLevel(Skill.FLETCHING, 1);
				player.getSkillManager().setExperience(Skill.FLETCHING, 1);
			}
			player.reset_stats_1 = 1;
		}
		player.getPacketSender().sendString(1, "[WITHDRAWX]-"+player.getWithdrawX());
		// Skills
		player.getSummoning().login();
		player.getFarming().load();
		Slayer.checkDuoSlayer(player, true);
		for (Skill skill : Skill.values()) {
			player.getSkillManager().updateSkill(skill);
		}
		player.getPacketSender().sendString(5067,
				"Friends List (" + player.getRelations().getFriendList().size() + "/200)");
		// Relations
		player.getRelations().setPrivateMessageId(1).onLogin(player).updateLists(true, 1);

		// Client configurations
		player.getPacketSender().sendConfig(172, player.isAutoRetaliate() ? 1 : 0)
				.sendTotalXp(player.getSkillManager().getTotalGainedExp())
				.sendConfig(player.getFightType().getParentId(), player.getFightType().getChildId()).sendRunStatus()
				.sendRunEnergy().sendConstitutionOrbPoison(player.isPoisoned())
				.sendConstitutionOrbVenom(player.isVenomed()).sendString(8135, "" + player.getMoneyInPouch())
				.sendInteractionOption("Follow", 3, false).sendInteractionOption("Trade with", 4, false)
				.sendInterfaceRemoval()
				.sendString(39161, "@or2@Server time: @or2@[ @yel@" + Misc.getCurrentServerTime() + "@or2@ ]");

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
		player.getPacketSender().sendString(13056, "Teleport to Bosses on Chaos.");
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
		player.getPacketSender().sendString(7458, "Teleport to Bosses on Chaos.");
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
			player.getPointsHandler().setPkPoints(0, false, false);
			System.out.println(
					"The user " + player.getUsername() + " logged in with negative PK Points, resetting to 0.");
		}
		if (player.getPointsHandler().getSlayerPoints() < 0) {
			player.getPointsHandler().setSlayerPoints(0, false);
			player.getPacketSender()
					.sendMessage("You have logged in with negative Slayer points, they have been set to 0.");
		}
		if (player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6) {
			Achievements.finishAchievement(player, AchievementData.DEFEAT_NOMAD);
			Achievements.finishAchievement(player, AchievementData.DEFEAT_THE_CULINAROMANCER);
		}
		if(player.getSkillManager().hasAll99s()) {
			if (!player.hasAnnouncedMax()) {
				player.setAnnounceMax(true);
			}
		}
		// Update appearance
		player.getUpdateFlag().flag(Flag.APPEARANCE);

		// Loads login messages from Kotlin
		// new org.scripts.kotlin.core.login.LoginMessageParser();
		// LoginMessageParser.LoginMessageParser.sendLogin(player);
		// Loads Assetts
		// new org.scripts.kotlin.core.login.LoginLoaderAssetts();
		// LoginLoaderAssetts.LoginLoaderAssetts.loadAssetts(player);
		if (player.getPointsHandler().getAchievementPoints() > AchievementData.values().length) {
			player.getPointsHandler().setAchievementPoints(AchievementData.values().length, false);
		}
		onLogin(player);
		Locations.login(player);
		player.getPacketSender().sendString(1, "[CLEAR]");
		ClanChatManager.handleLogin(player);
		PlayerPanel.refreshPanel(player);

		// New player
		if (player.newPlayer()) {
			player.setPasswordChange(GameSettings.PASSWORD_CHANGE);
			player.save();
			// player.setClanChatName("chaos");
			player.setPlayerLocked(true);
			player.getDialog().sendDialog(new org.scripts.kotlin.content.dialog.Tutorial(player));
		} else {
			if (player.getPasswordChange() != GameSettings.PASSWORD_CHANGE) {
				player.setPlayerLocked(true);
				player.setPasswordChanging(true);
				player.setInputHandling(new ChangePassword());
				player.getPacketSender().sendEnterInputPrompt("Please enter a new password to set for your account:");
			} else {
				if (!player.getBankPinAttributes().hasBankPin()) {
					player.setPlayerLocked(true);
					player.setLoginAccountPin(true);
					DialogueManager.start(player, Tutorial.get(player, 17));
				}
			}
		}

		player.getPacketSender().updateSpecialAttackOrb().sendIronmanMode();

		if (player.getPointsHandler().getAchievementPoints() == 0) {
			Achievements.setPoints(player);
		}
		if (player.getRights() == PlayerRights.OWNER || player.getRights() == PlayerRights.MANAGER || player.getRights() == PlayerRights.DEVELOPER) {
			player.setDonorRights(5);
		} else if (player.getRights() == PlayerRights.ADMINISTRATOR) {
			player.setDonorRights(5);
		} else if ((player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.WIKI_MANAGER)
				&& player.getDonorRights() < 3) {
			player.setDonorRights(3);
		} else if ((player.getRights() == PlayerRights.SUPPORT || player.getRights() == PlayerRights.WIKI_EDITOR)
				&& player.getDonorRights() < 1) {
			player.setDonorRights(1);
		} else if (player.getRights() == PlayerRights.YOUTUBER && player.getDonorRights() < 5) {
			player.setDonorRights(5);
		} if (player.getRights().isStaff()) {
			if (player.getRights() == PlayerRights.OWNER) {
				player.setLoyaltyRank(43);
			} else if (player.getRights() == PlayerRights.DEVELOPER) {
					player.setLoyaltyRank(14);
			} else if (player.getRights() == PlayerRights.MANAGER) {
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
		if (player.getUsername().equalsIgnoreCase("dc blitz") || player.getUsername().equalsIgnoreCase("hero")) {
			player.setLoyaltyRank(51);
		}
		if (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) == 0) {
			player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
					player.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
		}
		// LoginChecksParser.checkLogin(player);
		if (GameSettings.DOUBLE_EXP) {
			player.getPacketSender().sendMessage("Welcome to <col=3385ff>Chaos@bla@, We're currently in <col=ff1a1a>Double EXP @bla@mode!");
		} else if(GameSettings.PVP_WORLD) {
			player.getPacketSender().sendMessage("Welcome to <col=3385ff>Chaos@bla@.");
			player.getPacketSender().sendWalkableInterface(197);
			displayCombatLevels(player);
		} else {
			player.getPacketSender().sendMessage(
					"Welcome to <col=3385ff>Chaos@bla@.");
		}
		if (player.getHomeLocation() == 0 && player.showHomeOnLogin()) {
			player.getPacketSender().sendMessage("@blu@Your home location is set to: @dre@Varrock@blu@.");
		} else if (player.getHomeLocation() == 1 && player.showHomeOnLogin()) {
			player.getPacketSender().sendMessage("@blu@Your home location is set to: @dre@Edgeville@blu@.");
		}
		PlayerLogs.connections(player, "Login");
		if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
				&& player.getBankPinAttributes().onDifferent(player)) {
			BankPin.init(player, false);
		}
		DailyTaskManager.giveNewTask(player);
		PlayerOwnedShops.collectCoinsOnLogin(player);
		player.updateRank();
		NoteHandler.login(player);
		player.getPacketSender().sendRights();
		player.getPacketSender().sendMessage("@blu@Chaos now has a public discord! Join in where all the fun and action happens.");
		player.getPacketSender().sendMessage("@blu@You can talk to players either by voice or text, and also listen to music with everyone!");
		player.getPacketSender().sendMessage("@blu@Simply type ::discord to join the discord!");
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
				System.out.println("[World] Deregistering player - [username, host] : [" + player.getUsername() + ", "
						+ player.getHostAddress() + "]");

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
				PlayerLogs.connections(player, "Logout");
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
