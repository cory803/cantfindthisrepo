package com.ikov.model.input.impl;

import com.ikov.model.input.EnterAmount;
import com.ikov.world.content.skill.impl.crafting.Flax;
import com.ikov.world.entity.impl.player.Player;

public class EnterAmountToSpin extends EnterAmount {

  @Override
  public void handleAmount(Player player, int amount) {
    Flax.spinFlax(player, amount);
  }

}
