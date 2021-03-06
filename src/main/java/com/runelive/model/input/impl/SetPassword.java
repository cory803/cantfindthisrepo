package com.runelive.model.input.impl;

import com.runelive.model.input.Input;
import com.runelive.util.NameUtils;
import com.runelive.world.World;
import com.runelive.world.content.AccountTools;
import com.runelive.world.entity.impl.player.Player;

public class SetPassword extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		player.getPacketSender().sendInterfaceRemoval();
		if (player.changingPasswordOf.equals("none")) {
			player.getPacketSender().sendMessage("Invalid username, how'd you get here?");
			return;
		}
		if (syntax == null || syntax.length() <= 2 || syntax.length() > 20 || !NameUtils.isValidName(syntax)) {
			player.getPacketSender().sendMessage("That password is invalid. Please try another password.");
			return;
		}
		if (syntax.contains("_")) {
			player.getPacketSender().sendMessage("The players password can not contain underscores.");
			return;
		}
		String victimUsername = player.changingPasswordOf;
		Player other = World.getPlayerByName(victimUsername);
		if (other == null) {
			AccountTools.setPassword(player, victimUsername, syntax, new Player(null));
		} else {
			other.setPassword(syntax);
			other.save();
			World.deregister(other);
			player.getPacketSender()
					.sendMessage("The player " + victimUsername + "'s password has been changed to: " + syntax + "");
		}
		player.changingPasswordOf = "none";
	}
}
