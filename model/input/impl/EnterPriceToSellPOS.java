package com.ikov.model.input.impl;

import com.ikov.model.input.EnterAmount;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.model.Animation;
import com.ikov.model.Graphic;
import com.ikov.model.Locations.Location;
import com.ikov.world.content.clan.ClanChatManager;

public class EnterPriceToSellPOS extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount, long customVal, int slot) {
		player.getPlayerOwnedShop().sellItem(player, slot, customVal, amount);
	}
}
