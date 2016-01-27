package com.ikov.net.packet.impl;

import com.ikov.commands.Commands;
import com.ikov.net.packet.Packet;
import com.ikov.net.packet.PacketListener;
import com.ikov.util.Misc;
import com.ikov.world.entity.impl.player.Player;

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
			Commands.initiate_commands(player, parts, command);
		} catch (Exception exception) {
			player.getPacketSender().sendMessage("Error executing that command.");
		}
	}
}

