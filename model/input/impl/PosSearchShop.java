package com.ikov.model.input.impl;

import com.ikov.model.input.Input;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.world.content.pos.PlayerOwnedShops;

public class PosSearchShop extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		if(syntax.length() <= 1) {
			player.getPacketSender().sendMessage("Invalid syntax entered.");
			return;
		}
		PlayerOwnedShops.openShop(syntax, player);
	}
}
