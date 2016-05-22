package com.runelive.model.input.impl;

import com.runelive.model.input.EnterAmount;
import com.runelive.world.entity.impl.player.Player;

public class EnterAmountToSellToPos extends EnterAmount {

  public EnterAmountToSellToPos(int item, int slot) {
    super(item, slot);
  }

  @Override
  public void handleAmount(Player player, long value) {
	int amount = (int) value;
	if(value > Integer.MAX_VALUE) {
		amount = Integer.MAX_VALUE;
	}
    if (player.isPlayerOwnedShopping())
      player.getPlayerOwnedShop().sellItem(player, getSlot(), amount, 0);
  }

}
