package com.ikov.net.packet.impl;

import com.ikov.model.definitions.NpcDefinition;
import com.ikov.net.packet.Packet;
import com.ikov.net.packet.PacketListener;
import com.ikov.world.entity.impl.player.Player;

public class ExamineNpcPacketListener implements PacketListener {

  @Override
  public void handleMessage(Player player, Packet packet) {
    int npc = packet.readShort();
    if (npc <= 0) {
      return;
    }
    NpcDefinition npcDef = NpcDefinition.forId(npc);
    if (npcDef != null) {
      player.getPacketSender().sendMessage(npcDef.getExamine());
    }
  }

}
