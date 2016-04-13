package com.ikov.net.packet.impl;

import com.ikov.GameSettings;
import com.ikov.model.PlayerRelations.PrivateChatStatus;
import com.ikov.net.packet.Packet;
import com.ikov.net.packet.PacketListener;
import com.ikov.world.content.PlayerLogs;
import com.ikov.world.entity.impl.player.Player;


public class ChangeRelationStatusPacketListener implements PacketListener {

  @Override
  public void handleMessage(Player player, Packet packet) {
    int actionId = packet.readInt();
    if (GameSettings.DEBUG_MODE) {
      PlayerLogs.log(player.getUsername(), "" + player.getUsername()
          + " has changed their private status in ChangeRelationStatusPacketListener");
    }
    player.getRelations().setStatus(PrivateChatStatus.forActionId(actionId), true);
  }

}
