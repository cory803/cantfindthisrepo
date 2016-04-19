package com.runelive.model.input.impl;

import com.runelive.model.input.EnterAmount;
import com.runelive.world.content.skill.impl.summoning.PouchMaking;
import com.runelive.world.entity.impl.player.Player;

public class EnterAmountToInfuse extends EnterAmount {

  @Override
  public void handleAmount(Player player, int amount) {
    if (player.getInterfaceId() != 63471) {
      player.getPacketSender().sendInterfaceRemoval();
      return;
    }
    PouchMaking.infusePouches(player, amount);
  }

}
