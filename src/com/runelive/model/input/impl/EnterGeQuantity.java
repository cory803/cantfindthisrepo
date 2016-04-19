package com.runelive.model.input.impl;

import com.runelive.model.input.EnterAmount;
import com.runelive.world.content.grandexchange.GrandExchange;
import com.runelive.world.entity.impl.player.Player;

public class EnterGeQuantity extends EnterAmount {

  @Override
  public void handleAmount(Player player, int amount) {
    GrandExchange.setQuantity(player, amount);
  }

}
