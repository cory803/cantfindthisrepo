package com.ikov.model.input.impl;

import com.ikov.model.Animation;
import com.ikov.model.Graphic;
import com.ikov.model.Locations.Location;
import com.ikov.model.input.EnterAmount;
import com.ikov.world.content.clan.ClanChatManager;
import com.ikov.world.entity.impl.player.Player;

public class EnterAmountToDice extends EnterAmount {


  public EnterAmountToDice(int item, int slot) {
    super(item, slot);
  }

  @Override
  public void handleAmount(Player player, int amount) {
    if (amount > 100) {
      player.getPacketSender().sendMessage("You can't roll over 100.");
      return;
    }
    if (amount < 0) {
      player.getPacketSender().sendMessage("You can't roll under 0.");
      return;
    }
    if (player.getDonorRights() == 0) {
      player.getPacketSender().sendMessage("You need to be a member to use this item.");
      return;
    }
    if (player.getLocation() != Location.GAMBLE) {
      player.getPacketSender().sendMessage("")
          .sendMessage("This dice can only be used in the gambling area!")
          .sendMessage("To get there, talk to the gambler.");
      return;
    }
    if (player.getClanChatName() == null) {
      player.getPacketSender().sendMessage("You need to be in a clanchat channel to roll a dice.");
      return;
    } else if (player.getClanChatName().equalsIgnoreCase("ikov")) {
      player.getPacketSender().sendMessage("You can't roll a dice in this clanchat channel!");
      return;
    }
    if (!player.getClickDelay().elapsed(5000)) {
      player.getPacketSender().sendMessage("You must wait 5 seconds between each dice cast.");
      return;
    }
    player.getMovementQueue().reset();
    player.performAnimation(new Animation(11900));
    player.performGraphic(new Graphic(2075));
    ClanChatManager.sendMessage(player.getCurrentClanChat(), "@bla@[ClanChat] @whi@"
        + player.getUsername() + " just rolled @bla@" + amount + "@whi@ on the percentile dice.");
    player.forceChat(
        "[HOST] " + player.getUsername() + " just ROLLED " + amount + " on the percentile dice.");
    player.getClickDelay().reset();
  }
}
