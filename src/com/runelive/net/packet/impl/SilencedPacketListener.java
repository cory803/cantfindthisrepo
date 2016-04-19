package com.runelive.net.packet.impl;

import com.runelive.net.packet.Packet;
import com.runelive.net.packet.PacketListener;
import com.runelive.world.entity.impl.player.Player;

/**
 * This packet listener is called when a packet should not execute any particular action or event,
 * but will also not print out any debug information.
 * 
 * @author relex lawl
 */

public class SilencedPacketListener implements PacketListener {

  @Override
  public void handleMessage(Player player, Packet packet) {

  }

}
