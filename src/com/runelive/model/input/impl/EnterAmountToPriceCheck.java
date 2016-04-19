package com.runelive.model.input.impl;

import com.runelive.model.Item;
import com.runelive.model.input.EnterAmount;
import com.runelive.world.entity.impl.player.Player;

public class EnterAmountToPriceCheck extends EnterAmount {

  public EnterAmountToPriceCheck(int item, int slot) {
    super(item, slot);
  }

  @Override
  public void handleAmount(Player player, int amount) {
    if (!player.getPriceChecker().isOpen())
      return;
    int item = player.getInventory().getItems()[getSlot()].getId();
    if (item != getItem())
      return;
    int invAmount = player.getInventory().getAmount(item);
    if (amount > invAmount)
      amount = invAmount;
    if (amount <= 0)
      return;
    player.getInventory().switchItem(player.getPriceChecker(), new Item(item, amount), getSlot(),
        false, true);
  }
}