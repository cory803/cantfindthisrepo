package com.chaos.model.input.impl;

import com.chaos.model.input.EnterAmount;
import com.chaos.world.content.wells.WellOfGoodness;
import com.chaos.world.entity.impl.player.Player;

public class DonateToWell extends EnterAmount {

	String well = "";
	public DonateToWell(String well) {
		this.well = well;
	}

	@Override
	public void handleAmount(Player player, long value) {
		int amount = (int) value;
		if (value > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE;
		}
		WellOfGoodness.donate(player, amount, well);
	}

}
