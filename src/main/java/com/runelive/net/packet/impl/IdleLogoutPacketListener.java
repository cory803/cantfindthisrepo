package com.runelive.net.packet.impl;

import com.runelive.net.packet.Packet;
import com.runelive.net.packet.PacketListener;
import com.runelive.world.entity.impl.player.Player;

// CALLED EVERY 3 MINUTES OF INACTIVITY

public class IdleLogoutPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		player.setInactive(true);
	}
}
