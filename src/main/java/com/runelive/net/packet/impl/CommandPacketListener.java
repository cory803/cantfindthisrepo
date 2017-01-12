package com.runelive.net.packet.impl;

import com.runelive.model.player.command.CommandManager;
import com.runelive.net.packet.Packet;
import com.runelive.net.packet.PacketListener;
import com.runelive.util.Misc;
import com.runelive.world.entity.impl.player.Player;

/**
 * This packet listener manages commands a player uses by using the command
 * console prompted by using the "`" char.
 *
 * @author Gabriel Hannason
 */

public class CommandPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (packet.getSize() == 0) {
			return;
		}
		String command = Misc.readString(packet.getBuffer());
		if (command == null) {
			return;
		}
		if (CommandManager.execute(player, command)) {
			return;
		}
	}
}