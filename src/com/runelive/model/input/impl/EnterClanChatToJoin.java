package com.runelive.model.input.impl;

import com.runelive.model.input.Input;
import com.runelive.world.content.clan.ClanChatManager;
import com.runelive.world.entity.impl.player.Player;

public class EnterClanChatToJoin extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		if (syntax.length() <= 1) {
			player.getPacketSender().sendMessage("Invalid syntax entered.");
			return;
		}
		ClanChatManager.join(player, syntax);
	}
}
