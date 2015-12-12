package com.ikov.model.input.impl;

import com.ikov.model.input.EnterAmount;
import com.ikov.world.content.skill.impl.crafting.Tanning;
import com.ikov.world.entity.impl.player.Player;

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
