package com.ikov.model.input.impl;

import com.ikov.model.input.EnterAmount;
import com.ikov.world.content.WellOfGoodwill;
import com.ikov.world.entity.impl.player.Player;

public class DonateToWell extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		WellOfGoodwill.donate(player, amount);
	}

}
