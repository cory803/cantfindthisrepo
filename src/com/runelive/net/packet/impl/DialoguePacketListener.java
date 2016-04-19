package com.runelive.net.packet.impl;

import com.runelive.net.packet.Packet;
import com.runelive.net.packet.PacketListener;
import com.runelive.world.content.dialogue.DialogueManager;
import com.runelive.world.entity.impl.player.Player;

/**
 * This packet listener handles player's mouse click on the "Click here to continue" option, etc.
 * 
 * @author relex lawl
 */

public class DialoguePacketListener implements PacketListener {

  @Override
  public void handleMessage(Player player, Packet packet) {
    switch (packet.getOpcode()) {
      case DIALOGUE_OPCODE:
        DialogueManager.next(player);
        break;
    }
  }

  public static final int DIALOGUE_OPCODE = 40;
}
