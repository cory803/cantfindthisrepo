package com.chaos.model.input.impl;

import com.chaos.model.definitions.NpcDefinition;
import com.chaos.model.input.EnterAmount;
import com.chaos.world.entity.impl.player.Player;

public class DropGeneratorAmount extends EnterAmount {

	@Override
	public void handleAmount(Player player, long value) {
		int amount = (int) value;
		if (value > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE;
		}
		player.getDropGenerator().generate(player, amount, player.getDropGenerator().getNpcId());
	}

}
