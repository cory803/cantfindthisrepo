package com.strattus.model.input.impl;

import java.sql.PreparedStatement;

import com.strattus.GameSettings;
import com.strattus.model.input.Input;
import com.strattus.world.content.Achievements;
import com.strattus.world.content.Achievements.AchievementData;
import com.strattus.world.content.PlayerPanel;
import com.strattus.world.entity.impl.player.Player;

public class SetEmail extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		player.getPacketSender().sendInterfaceRemoval();
		if(!GameSettings.MYSQL_ENABLED) {
			player.getPacketSender().sendMessage("This service is currently unavailable.");
			return;
		}
		if(syntax.length() <= 3 || !syntax.contains("@") || syntax.endsWith("@")) {
			player.getPacketSender().sendMessage("Invalid syntax, please enter a valid one.");
			return;
		}
		if(player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()) {
			player.getPacketSender().sendMessage("Please visit the nearest bank and enter your pin before doing this.");
			return;
		}
		if(player.getEmailAddress() != null && syntax.equalsIgnoreCase(player.getEmailAddress())) {
			player.getPacketSender().sendMessage("This is already your email-address!");
			return;
		}
		syntax = syntax.toLowerCase();
		syntax = syntax.substring(0, 1).toUpperCase() + syntax.substring(1);
		boolean success = false;
		try {

		} catch(Exception e) {
			e.printStackTrace();
			success = false;
		}
		if(success) {
			player.setEmailAddress(syntax);
			player.getPacketSender().sendMessage("Your account's email-adress is now: "+syntax);
		//	Achievements.finishAchievement(player, AchievementData.SET_AN_EMAIL_ADDRESS);
			PlayerPanel.refreshPanel(player);
		} else {
			player.getPacketSender().sendMessage("An error occured. Please try again.");
		}
	}
}
