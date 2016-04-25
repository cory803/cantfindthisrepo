package com.runelive.net.packet.impl;

import com.runelive.commands.Commands;
import com.runelive.model.actions.Action;
import com.runelive.model.actions.ActionHandler;
import com.runelive.net.packet.Packet;
import com.runelive.net.packet.PacketListener;
import com.runelive.util.Misc;
import com.runelive.world.entity.impl.player.Player;

/**
 * This packet listener manages commands a player uses by using the
 * command console prompted by using the "`" char.
 *
 * @author Gabriel Hannason
 */

public class CommandPacketListener implements PacketListener {

  @Override
  public void handleMessage(Player player, Packet packet) {
    String command = Misc.readString(packet.getBuffer());
    String[] parts = command.toLowerCase().split(" ");
    if(command.contains("\r") || command.contains("\n")) {
      return;
    }
    try {
      Action action = ActionHandler.getActionHandler().getAction(command);
      if (action != null) {
        action.handle(player);
        return;
      }
      Commands.initiate_commands(player, parts, command);
    } catch (Exception exception) {
      player.getPacketSender().sendMessage("Error executing that command.");
    }
  }
}