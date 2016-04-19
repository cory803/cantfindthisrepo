package com.runelive.model.input.impl;

import com.runelive.model.input.EnterAmount;
import com.runelive.world.content.skill.impl.prayer.BonesOnAltar;
import com.runelive.world.entity.impl.player.Player;

public class EnterAmountOfBonesToSacrifice extends EnterAmount {

  @Override
  public void handleAmount(Player player, int amount) {
    BonesOnAltar.offerBones(player, amount);
  }

}
