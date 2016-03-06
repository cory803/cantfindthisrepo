package com.ikov.world.entity.impl.player;

import com.ikov.util.PlayersOnline;
import com.ikov.model.Locations.Location;
import com.ikov.GameSettings;
import com.ikov.GameServer;
import com.ikov.engine.task.TaskManager;
import com.ikov.world.content.BankPin;
import com.ikov.util.ForumDatabase;
import java.sql.*;
import java.util.Properties;
import com.ikov.engine.task.impl.BonusExperienceTask;
import com.ikov.engine.task.impl.CombatSkullEffect;
import com.ikov.engine.task.impl.FireImmunityTask;
import com.ikov.engine.task.impl.OverloadPotionTask;
import com.ikov.util.Logs;
import com.ikov.engine.task.impl.PlayerSkillsTask;
import com.ikov.engine.task.impl.PlayerSpecialAmountTask;
import com.ikov.engine.task.impl.PrayerRenewalPotionTask;
import com.ikov.engine.task.impl.StaffOfLightSpecialAttackTask;
import com.ikov.model.Flag;
import com.ikov.model.Locations;
import com.ikov.model.PlayerRights;
import com.ikov.model.Skill;
import com.ikov.model.container.impl.Bank;
import com.ikov.model.container.impl.Equipment;
import com.ikov.model.definitions.WeaponAnimations;
import com.ikov.model.definitions.WeaponInterfaces;
import com.ikov.model.Highscores;
import com.ikov.net.PlayerSession;
import com.ikov.net.SessionState;
import com.ikov.net.security.ConnectionHandler;
import com.ikov.util.Misc;
import com.ikov.world.World;
import com.ikov.world.content.Achievements;
import com.ikov.world.content.BonusManager;
import com.ikov.world.content.WellOfGoodwill;
import com.ikov.world.content.Lottery;
import com.ikov.world.content.PlayerPanel;
import com.ikov.world.content.PlayersOnlineInterface;
import com.ikov.world.content.clan.ClanChatManager;
import com.ikov.world.content.combat.effect.CombatPoisonEffect;
import com.ikov.world.content.combat.effect.CombatVenomEffect;
import com.ikov.world.content.combat.effect.CombatTeleblockEffect;
import com.ikov.world.content.combat.magic.Autocasting;
import com.ikov.world.content.combat.prayer.CurseHandler;
import com.ikov.world.content.combat.prayer.PrayerHandler;
import com.ikov.world.content.combat.pvp.BountyHunter;
import com.ikov.world.content.combat.range.DwarfMultiCannon;
import com.ikov.world.content.combat.weapon.CombatSpecial;
import com.ikov.world.content.dialogue.DialogueManager;
import com.ikov.world.content.grandexchange.GrandExchange;
import com.ikov.world.content.minigames.impl.Barrows;
import com.ikov.world.content.skill.impl.hunter.Hunter;
import com.ikov.world.content.skill.impl.slayer.Slayer;

public class PlayerHandler {

	public static void handleLogin(Player player) {
		//Register the player
		System.out.println("[World] Registering player - [username, host] : [" + player.getUsername() + ", " + player.getHostAddress() + "]");
		ConnectionHandler.add(player.getHostAddress());
		World.getPlayers().add(player);
		World.updatePlayersOnline();
		PlayersOnlineInterface.add(player);
		player.getSession().setState(SessionState.LOGGED_IN);

		//Packets
		player.getPacketSender().sendMapRegion().sendDetails();

		player.getRecordedLogin().reset();
		boolean maxed_out = true;
		for(int i = 0; i < Skill.values().length; i++) {
			if(i == 21)
				continue;
			if(player.getSkillManager().getMaxLevel(i) < (i == 3 || i == 5 ? 990 : 99)) {
				maxed_out = false;
			}
		}
		if(maxed_out) {
			player.setAnnounceMax(true);
		}
		//Tabs
		player.getPacketSender().sendTabs();
		//Setting up the player's item containers..
		for(int i = 0; i < player.getBanks().length; i++) {
			if(player.getBank(i) == null) {
				player.setBank(i, new Bank(player));
			}
		}
		player.getInventory().refreshItems();
		player.getEquipment().refreshItems();

		//Weapons and equipment..
		WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
		WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
		CombatSpecial.updateBar(player);
		BonusManager.update(player);

		//Skills
		player.getSummoning().login();
		player.getFarming().load();
		Slayer.checkDuoSlayer(player, true);
		for (Skill skill : Skill.values()) {
			player.getSkillManager().updateSkill(skill);
		}

		//Relations
		player.getRelations().setPrivateMessageId(1).onLogin(player).updateLists(true);

		//Client configurations
		player.getPacketSender().sendConfig(172, player.isAutoRetaliate() ? 1 : 0)
		.sendTotalXp(player.getSkillManager().getTotalGainedExp())
		.sendConfig(player.getFightType().getParentId(), player.getFightType().getChildId())
		.sendRunStatus()
		.sendRunEnergy(player.getRunEnergy())
		.sendConstitutionOrbPoison(player.isPoisoned())
		.sendConstitutionOrbVenom(player.isVenomed())
		.sendString(8135, ""+player.getMoneyInPouch())
		.sendInteractionOption("Follow", 3, false)
		.sendInteractionOption("Trade With", 4, false)
		.sendInterfaceRemoval().sendString(39161, "@or2@Server time: @or2@[ @yel@"+Misc.getCurrentServerTime()+"@or2@ ]");

		Autocasting.onLogin(player);
		PrayerHandler.deactivateAll(player);
		CurseHandler.deactivateAll(player);
		BonusManager.sendCurseBonuses(player);
		Achievements.updateInterface(player);
		Barrows.updateInterface(player);


		//Tasks
		TaskManager.submit(new PlayerSkillsTask(player));
		if (player.isPoisoned()) {
			TaskManager.submit(new CombatPoisonEffect(player));
		}	
		if (player.isVenomed()) {
			TaskManager.submit(new CombatVenomEffect(player));
		}
		if(player.getPrayerRenewalPotionTimer() > 0) {
			TaskManager.submit(new PrayerRenewalPotionTask(player));
		}
		if(player.getOverloadPotionTimer() > 0) {
			TaskManager.submit(new OverloadPotionTask(player));
		}
		if (player.getTeleblockTimer() > 0) {
			TaskManager.submit(new CombatTeleblockEffect(player));
		}
		if (player.getSkullTimer() > 0) {
			player.setSkullIcon(1);
			TaskManager.submit(new CombatSkullEffect(player));
		}
		if(player.getFireImmunity() > 0) {
			FireImmunityTask.makeImmune(player, player.getFireImmunity(), player.getFireDamageModifier());
		}
		if(player.getSpecialPercentage() < 100) {
			TaskManager.submit(new PlayerSpecialAmountTask(player));
		}
		if(player.hasStaffOfLightEffect()) {
			TaskManager.submit(new StaffOfLightSpecialAttackTask(player));
		}
		if(player.getMinutesBonusExp() >= 0) {
			TaskManager.submit(new BonusExperienceTask(player));
		}

		//Update appearance
		player.getUpdateFlag().flag(Flag.APPEARANCE);

		//Others
		Lottery.onLogin(player);
		Locations.login(player);
		if(GameSettings.DOUBLE_EXP) {
			player.getPacketSender().sendMessage("@bla@Welcome to Ikov! We're currently in Double EXP mode! (@red@X2.0@bla@)");
		} else {
			player.getPacketSender().sendMessage("@bla@Welcome to Ikov! We're currently in Normal EXP mode! (@red@X1.0@bla@)");
		}
		
		if(GameSettings.INSANE_EXP) {
			player.getPacketSender().sendMessage("@bla@Welcome to Ikov! We're currently in Insane EXP mode! (@red@X8.0@bla@)");
		}
		
		if(player.experienceLocked())
			player.getPacketSender().sendMessage("@red@Warning: your experience is currently locked.");
		ClanChatManager.handleLogin(player);

		if(GameSettings.DOUBLE_POINTS) {
			player.getPacketSender().sendMessage("<img=10> <col=008FB2>Ikov currently has a double points event going on, make sure to use it!");
		}	

		if(GameSettings.DOUBLE_VOTE_TOKENS) {
			player.getPacketSender().sendMessage("<img=10> <col=008FB2>Ikov currently has a double vote rewards event going on, make sure to use it!");
		}
		
		if(GameSettings.TRIPLE_VOTE_TOKENS) {
			player.getPacketSender().sendMessage("<img=10> <col=008FB2>Ikov currently has a triple vote rewards event going on, make sure to use it!");
		}
		
		if(WellOfGoodwill.isActive()) {
			if(player.getDonorRights() > 0) {
				player.getPacketSender().sendMessage("<img=10> <col=008FB2>The Well of Goodwill is granting 50% bonus experience for another "+WellOfGoodwill.getMinutesRemaining()+" minutes.");
			} else {
				player.getPacketSender().sendMessage("<img=10> <col=008FB2>The Well of Goodwill is granting 30% bonus experience for another "+WellOfGoodwill.getMinutesRemaining()+" minutes.");		
			}
		}

		PlayerPanel.refreshPanel(player);


		//New player
		if(player.newPlayer()) {
			//player.setClanChatName("ikov");
			player.setPlayerLocked(true).setDialogueActionId(45);
			DialogueManager.start(player, 81);
		}

		player.getPacketSender().updateSpecialAttackOrb().sendIronmanMode(player.getGameMode().ordinal());

		if(player.getRights() == PlayerRights.SUPPORT || player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR)
			World.sendMessage("<img=10><col=6600CC> "+Misc.formatText(player.getRights().toString().toLowerCase())+" "+player.getUsername()+" has just logged in, feel free to message them for support.");
		
		GrandExchange.onLogin(player);
		
		if(player.getPointsHandler().getAchievementPoints() == 0) {
			Achievements.setPoints(player);
		}
		if(player.getRights() == PlayerRights.OWNER) {
			player.setDonorRights(5);
		} else if(player.getRights() == PlayerRights.ADMINISTRATOR) {
			player.setDonorRights(5);
		} else if(player.getRights() == PlayerRights.MODERATOR && player.getDonorRights() < 3) {
			player.setDonorRights(3);
		} else if(player.getRights() == PlayerRights.SUPPORT && player.getDonorRights() < 1) {
			player.setDonorRights(1);
		}
		if(player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin() && player.getBankPinAttributes().onDifferent(player)) {
			BankPin.init(player, false);
		}
		if(player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) == 0) {
			player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, player.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
		}
		if(!player.getBankPinAttributes().hasBankPin()) {
			if(player.getLocation() != Location.WILDERNESS) {
				if(player.newPlayer()) {
					
				} else {
					player.setDialogueActionId(181);
					DialogueManager.start(player, 181);
				}
			}
		}
		Logs.write_data(player.getUsername()+ ".txt", "account_logins", "Login from host "+player.getHostAddress()+", Computer Address: "+player.getComputerAddress()+"");
	}

	public static boolean handleLogout(Player player) {
		try {

			PlayerSession session = player.getSession();
			
			if(session.getChannel().isOpen()) {
				session.getChannel().close();
			}

			if(!player.isRegistered()) {
				return true;
			}

			boolean exception = GameServer.isUpdating() || World.getLogoutQueue().contains(player) && player.getLogoutTimer().elapsed(90000);
			if(player.logout() || exception) {
				System.out.println("[World] Deregistering player - [username, host] : [" + player.getUsername() + ", " + player.getHostAddress() + "]");
				//if(player.getRights() != PlayerRights.OWNER && player.getRights() != PlayerRights.ADMINISTRATOR) {
				//	new Thread(new Highscores(player)).start();
				//}
				//Sets last account information available
				player.setLastIpAddress(player.getHostAddress());
				player.setLastSerialAddress(player.getSerialNumber());
				player.setLastMacAddress(player.getMacAddress());
				player.setLastComputerAddress(player.getComputerAddress());
				
				player.getSession().setState(SessionState.LOGGING_OUT);
				ConnectionHandler.remove(player.getHostAddress());
				player.setTotalPlayTime(player.getTotalPlayTime() + player.getRecordedLogin().elapsed());
				player.getPacketSender().sendInterfaceRemoval();
				if(player.getCannon() != null) {
					DwarfMultiCannon.pickupCannon(player, player.getCannon(), true);
				}
				if(exception && player.getResetPosition() != null) {
					player.moveTo(player.getResetPosition());
					player.setResetPosition(null);
				}
				if(player.getRegionInstance() != null) {
					player.getRegionInstance().destruct();
				}
				Hunter.handleLogout(player);
				Locations.logout(player);
				player.getSummoning().unsummon(false, false);
				player.getFarming().save();
				BountyHunter.handleLogout(player);
				ClanChatManager.leave(player, false);
				player.getRelations().updateLists(false);
				PlayersOnlineInterface.remove(player);
				TaskManager.cancelTasks(player.getCombatBuilder());
				TaskManager.cancelTasks(player);
				player.save();
				World.getPlayers().remove(player);
				session.setState(SessionState.LOGGED_OUT);
				World.updatePlayersOnline();
				Logs.write_data(player.getUsername()+ ".txt", "account_logins", "Logout from host "+player.getHostAddress()+", Computer Address: "+player.getComputerAddress()+"");
				return true;
			} else {
				return false;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
