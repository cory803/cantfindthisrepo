package com.runelive.model.input.impl;

import com.runelive.model.Skill;
import com.runelive.model.input.EnterAmount;
import com.runelive.world.entity.impl.player.Player;

public class BuyAgilityExperience extends EnterAmount {

	  @Override
	  public void handleAmount(Player player, long value) {
		int amount = (int) value;
		if(value > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE;
		}
    player.getPacketSender().sendInterfaceRemoval();
    int ticketAmount = player.getInventory().getAmount(2996);
    if (ticketAmount == 0) {
      player.getPacketSender().sendMessage("You do not have any tickets.");
      return;
    }
    if (ticketAmount > amount) {
      ticketAmount = amount;
    }

    if (player.getInventory().getAmount(2996) < ticketAmount) {
      return;
    }

    int exp = ticketAmount * 7680;
    player.getInventory().delete(2996, ticketAmount);
    player.getSkillManager().addExperience(Skill.AGILITY, exp);
    player.getPacketSender().sendMessage("You've bought " + exp + " Agility experience for "
        + ticketAmount + " Agility ticket" + (ticketAmount == 1 ? "" : "s") + ".");
  }

}
