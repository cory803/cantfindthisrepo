package com.runelive.model.input.impl;

import com.runelive.model.input.Input;
import com.runelive.world.content.skill.impl.thieving.Stalls;
import com.runelive.world.entity.impl.player.Player;

public class ThievBots extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		player.getPacketSender().sendInterfaceRemoval();
		if (Stalls.getBotStop() == 1) {
			if (syntax.equalsIgnoreCase("ikov2")) {
				player.getPacketSender().sendMessage("You have successfully completed the random.");
				player.setPassedRandom(true);
			} else {
				player.getPacketSender().sendMessage("That was not the right answer, please try again.");
				player.setPassedRandom(false);
				// player.getPacketSender().sendLogout();
			}
		} else if (Stalls.getBotStop() == 2) {
			if (syntax.equalsIgnoreCase("15")) {
				player.getPacketSender().sendMessage("You have successfully completed the random.");
				player.setPassedRandom(true);
			} else {
				player.getPacketSender().sendMessage("That was not the right answer, please try again.");
				player.setPassedRandom(false);
				// player.getPacketSender().sendLogout();
			}
		} else if (Stalls.getBotStop() == 3) {
			if (syntax.equalsIgnoreCase("no")) {
				player.getPacketSender().sendMessage("You have successfully completed the random.");
				player.setPassedRandom(true);
			} else {
				player.getPacketSender().sendMessage("That was not the right answer, please try again.");
				player.setPassedRandom(false);
				// player.getPacketSender().sendLogout();
			}
		}
	}
}