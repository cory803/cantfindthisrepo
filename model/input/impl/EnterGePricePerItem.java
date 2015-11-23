package com.strattus.model.input.impl;

import com.strattus.model.input.EnterAmount;
import com.strattus.world.content.grandexchange.GrandExchange;
import com.strattus.world.entity.impl.player.Player;

public class EnterGePricePerItem extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		GrandExchange.setPricePerItem(player, amount);
	}

}
