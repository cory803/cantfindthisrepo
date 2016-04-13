package com.ikov.model.input.impl;

import com.ikov.model.input.EnterAmount;
import com.ikov.world.content.grandexchange.GrandExchange;
import com.ikov.world.entity.impl.player.Player;

public class EnterGePricePerItem extends EnterAmount {

  @Override
  public void handleAmount(Player player, int amount) {
    GrandExchange.setPricePerItem(player, amount);
  }

}
