package com.runelive.model.input.impl;

import com.runelive.model.input.EnterAmount;
import com.runelive.world.content.skill.impl.cooking.Cooking;
import com.runelive.world.entity.impl.player.Player;

public class EnterAmountToCook extends EnterAmount {

	@Override
	public void handleAmount(Player player, long value) {
		int amount = (int) value;
		if (value > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE;
		}
		if (player.getSelectedSkillingItem() > 0)
			Cooking.cook(player, player.getSelectedSkillingItem(), amount);
	}

}
