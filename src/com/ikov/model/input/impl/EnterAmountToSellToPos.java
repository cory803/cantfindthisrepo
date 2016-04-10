package com.ikov.model.input.impl;

import com.ikov.model.input.EnterAmount;
import com.ikov.world.content.minigames.impl.Dueling;
import com.ikov.world.entity.impl.player.Player;

public class EnterAmountToSellToPos extends EnterAmount {
	
	public EnterAmountToSellToPos(int item, int slot) {
		super(item, slot);
	}
	
	@Override
	public void handleAmount(Player player, int amount) {
		if (player.isPlayerOwnedShopping())
			player.getPlayerOwnedShop().sellItem(player, getSlot(), amount, 0);
	}

}
