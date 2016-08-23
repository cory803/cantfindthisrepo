package com.chaos.model.input.impl;

import com.chaos.model.input.Input;
import com.chaos.world.content.PlayerPunishment;
import com.chaos.world.content.pos.PlayerOwnedShops;
import com.chaos.world.entity.impl.player.Player;

public class PosSearchShop extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		if (syntax.length() <= 1) {
			player.getPacketSender().sendMessage("Invalid syntax entered.");
			return;
		}
		if (PlayerPunishment.isPlayerBanned(syntax)) {
			player.getPacketSender().sendMessage("This player is banned!");
			return;
		}
		player.getPacketSender().sendString(41900, "");
		PlayerOwnedShops.openShop(syntax, player);
	}
}
