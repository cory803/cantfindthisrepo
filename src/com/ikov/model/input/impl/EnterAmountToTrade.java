package com.ikov.model.input.impl;

import com.ikov.model.input.EnterAmount;
import com.ikov.world.entity.impl.player.Player;

public class EnterAmountToTrade extends EnterAmount {

	public EnterAmountToTrade(int item, int slot) {
		super(item, slot);
	}
	
	@Override
	public void handleAmount(Player player, int amount) {
		if(player.getTrading().inTrade() && getItem() > 0 && getSlot() >= 0 && getSlot() < 28)
			player.getTrading().tradeItem(getItem(), amount, getSlot());
		else
			player.getPacketSender().sendInterfaceRemoval();
	}

}