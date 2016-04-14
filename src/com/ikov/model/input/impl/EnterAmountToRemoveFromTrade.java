package com.ikov.model.input.impl;

import com.ikov.model.input.EnterAmount;
import com.ikov.world.entity.impl.player.Player;

public class EnterAmountToRemoveFromTrade extends EnterAmount {

  public EnterAmountToRemoveFromTrade(int item) {
    super(item);
  }

  @Override
  public void handleAmount(Player player, int amount) {
    if (player.getTrading().inTrade() && getItem() > 0)
      player.getTrading().removeTradedItem(getItem(), amount);
    else
      player.getPacketSender().sendInterfaceRemoval();
  }


}
