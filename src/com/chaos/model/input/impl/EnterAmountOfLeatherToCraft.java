package com.chaos.model.input.impl;

import com.chaos.model.input.EnterAmount;
import com.chaos.world.content.skill.impl.crafting.LeatherMaking;
import com.chaos.world.content.skill.impl.crafting.leatherData;
import com.chaos.world.entity.impl.player.Player;

public class EnterAmountOfLeatherToCraft extends EnterAmount {

	@Override
	public void handleAmount(Player player, long value) {
		int amount = (int) value;
		if (value > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE;
		}
		for (final leatherData l : leatherData.values()) {
			if (player.getSelectedSkillingItem() == l.getLeather()) {
				LeatherMaking.craftLeather(player, l, amount);
				break;
			}
		}
	}
}
