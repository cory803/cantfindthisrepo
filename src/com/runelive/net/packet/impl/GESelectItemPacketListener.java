package com.runelive.net.packet.impl;

import com.runelive.GameSettings;
import com.runelive.model.Item;
import com.runelive.model.definitions.ItemDefinition;
import com.runelive.net.packet.Packet;
import com.runelive.net.packet.PacketListener;
import com.runelive.world.content.PlayerLogs;
import com.runelive.world.content.grandexchange.GrandExchange;
import com.runelive.world.entity.impl.player.Player;


public class GESelectItemPacketListener implements PacketListener {

  @Override
  public void handleMessage(Player player, Packet packet) {
    int item = packet.readShort();
    if (item <= 0 || item >= ItemDefinition.getMaxAmountOfItems())
      return;
    ItemDefinition def = ItemDefinition.forId(item);
    if (def != null) {
      if (def.getValue() <= 0 || !Item.tradeable(item) || item == 995) {
        player.getPacketSender()
            .sendMessage("This item can currently not be purchased or sold in the Grand Exchange.");
        return;
      }
      if (GameSettings.DEBUG_MODE) {
        PlayerLogs.log(player.getUsername(),
            "" + player.getUsername() + " in GESelectItemPacketListener: " + item + "");
      }
      GrandExchange.setSelectedItem(player, item);
    }
  }

}
