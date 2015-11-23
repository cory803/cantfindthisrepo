package com.strattus.model.input.impl;

import com.strattus.model.input.EnterAmount;
import com.strattus.world.content.skill.impl.fletching.Fletching;
import com.strattus.world.entity.impl.player.Player;

public class EnterAmountOfBowsToString extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		Fletching.stringBow(player, amount);
	}

}
