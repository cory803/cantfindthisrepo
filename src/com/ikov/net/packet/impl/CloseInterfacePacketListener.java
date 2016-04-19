package com.ikov.net.packet.impl;

import com.ikov.GameSettings;
import com.ikov.net.packet.Packet;
import com.ikov.net.packet.PacketListener;
import com.ikov.world.content.PlayerLogs;
import com.ikov.world.entity.impl.player.Player;


public class CloseInterfacePacketListener implements PacketListener {

  @Override
  public void handleMessage(Player player, Packet packet) {
    player.getPacketSender().sendClientRightClickRemoval();
    player.getPacketSender().sendInterfaceRemoval();
    // player.getPacketSender().sendTabInterface(Constants.CLAN_CHAT_TAB, 29328); //Clan chat tab
    // player.getAttributes().setSkillGuideInterfaceData(null);
    if (GameSettings.DEBUG_MODE) {
      PlayerLogs.log(player.getUsername(), "" + player.getUsername()
          + " has changed close interface in CloseInterfacePacketListener");
    }
  }
}
