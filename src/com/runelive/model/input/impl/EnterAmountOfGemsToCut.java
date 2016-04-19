package com.runelive.model.input.impl;

import com.runelive.model.input.EnterAmount;
import com.runelive.world.content.skill.impl.crafting.Gems;
import com.runelive.world.entity.impl.player.Player;

public class EnterAmountOfGemsToCut extends EnterAmount {

  @Override
  public void handleAmount(Player player, int amount) {
    if (player.getSelectedSkillingItem() > 0)
      Gems.cutGem(player, amount, player.getSelectedSkillingItem());
  }

}
