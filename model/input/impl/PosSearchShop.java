package com.ikov.model.input.impl;

import com.ikov.model.input.Input;
import com.ikov.world.content.clan.ClanChatManager;
import com.ikov.world.entity.impl.player.Player;

public class PosSearchShop extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		if(syntax.length() <= 1) {
			player.getPacketSender().sendMessage("Invalid syntax entered.");
			return;
		}
		//search shop entered @param syntax
	}
}
