package com.ikov.model.input.impl;

import com.ikov.model.input.EnterAmount;
import com.ikov.world.content.skill.impl.prayer.BonesOnAltar;
import com.ikov.world.entity.impl.player.Player;

public class EnterAmountOfBonesToSacrifice extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		BonesOnAltar.offerBones(player, amount);
	}

}
