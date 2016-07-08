package com.runelive.model.input.impl;

import com.runelive.model.Item;
import com.runelive.model.container.impl.Bank;
import com.runelive.model.input.EnterAmount;
import com.runelive.world.entity.impl.player.Player;

public class EnterAmountToBank extends EnterAmount {

	public EnterAmountToBank(int item, int slot) {
		super(item, slot);
	}

	@Override
	public void handleAmount(Player player, long value) {
		int amount = (int) value;
		if (value > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE;
		}
		if (!player.isBanking())
			return;
		int item = player.getInventory().getItems()[getSlot()].getId();
		if (item != getItem())
			return;
		int invAmount = player.getInventory().getAmount(item);
		if (amount > invAmount)
			amount = invAmount;
		if (amount <= 0)
			return;
		player.getInventory().switchItem(player.getBank(Bank.getTabForItem(player, item)), new Item(item, amount),
				getSlot(), false, true);
	}
}
