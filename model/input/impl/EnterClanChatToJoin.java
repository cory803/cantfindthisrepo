package com.strattus.model.input.impl;

import com.strattus.model.input.Input;
import com.strattus.world.content.clan.ClanChatManager;
import com.strattus.world.entity.impl.player.Player;

public class EnterClanChatToJoin extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		if(syntax.length() <= 1) {
			player.getPacketSender().sendMessage("Invalid syntax entered.");
			return;
		}
		ClanChatManager.join(player, syntax);
	}
}
