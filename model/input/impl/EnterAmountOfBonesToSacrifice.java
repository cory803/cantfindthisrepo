package com.strattus.model.input.impl;

import com.strattus.model.input.EnterAmount;
import com.strattus.world.content.skill.impl.prayer.BonesOnAltar;
import com.strattus.world.entity.impl.player.Player;

public class EnterAmountOfBonesToSacrifice extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		BonesOnAltar.offerBones(player, amount);
	}

}
