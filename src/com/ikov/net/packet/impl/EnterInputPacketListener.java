package com.ikov.net.packet.impl;

import com.ikov.GameSettings;
import com.ikov.net.packet.Packet;
import com.ikov.net.packet.PacketListener;
import com.ikov.util.Misc;
import com.ikov.world.content.PlayerLogs;
import com.ikov.world.entity.impl.player.Player;


/**
 * This packet manages the input taken from chat box interfaces that allow input, such as withdraw
 * x, bank x, enter name of friend, etc.
 * 
 * @author Gabriel Hannason
 */

public class EnterInputPacketListener implements PacketListener {


  @Override
  public void handleMessage(Player player, Packet packet) {
    switch (packet.getOpcode()) {
      case ENTER_SYNTAX_OPCODE:
        String name = Misc.readString(packet.getBuffer());
        if (name == null)
          return;
        if (player.getInputHandling() != null)
          player.getInputHandling().handleSyntax(player, name);

        if (GameSettings.DEBUG_MODE) {
          PlayerLogs.log(player.getUsername(),
              "" + player.getUsername() + " in EnterInputPacketListener: " + name + "");
        }
        player.setInputHandling(null);
        break;
      case ENTER_AMOUNT_OPCODE:
        int amount = packet.readInt();
        if (amount <= 0)
          return;
        if (player.getInputHandling() != null)
          player.getInputHandling().handleAmount(player, amount);

        if (GameSettings.DEBUG_MODE) {
          PlayerLogs.log(player.getUsername(),
              "" + player.getUsername() + " in EnterInputPacketListener: " + amount + "");
        }
        if (!player.hasNext()) {
          player.setInputHandling(null);
        } else {
          player.setHasNext(false);
        }
        break;
    }
  }

  public static final int ENTER_AMOUNT_OPCODE = 208, ENTER_SYNTAX_OPCODE = 60;
}
