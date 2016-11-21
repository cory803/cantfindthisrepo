package com.chaos.model.input.impl;

import com.chaos.model.input.Input;
import com.chaos.world.content.pos.SearchItemSql;
import com.chaos.world.entity.impl.player.Player;

public class SearchByItemPOS extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		if (syntax == null || syntax.length() <= 2 || syntax.length() > 20) {
			player.getPacketSender().sendMessage("You must type a minimum of 2 characters and a maximum of 20 characters.");
			return;
		}
		player.getPlayerOwnedShops().clearSearch();
		SearchItemSql.search(player, syntax);
	}
}
