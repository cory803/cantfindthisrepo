package com.strattus.model.input.impl;

import com.strattus.model.input.EnterAmount;
import com.strattus.world.content.skill.impl.crafting.Tanning;
import com.strattus.world.entity.impl.player.Player;

public class EnterAmountOfHidesToTan extends EnterAmount {

	private int button;
	public EnterAmountOfHidesToTan(int button) {
		this.button = button;
	}
	
	@Override
	public void handleAmount(Player player, int amount) {
		Tanning.tanHide(player, button, amount);
	}

}
