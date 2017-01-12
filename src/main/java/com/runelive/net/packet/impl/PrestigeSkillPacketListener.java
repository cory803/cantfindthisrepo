package com.runelive.net.packet.impl;

import com.runelive.net.packet.Packet;
import com.runelive.net.packet.PacketListener;
import com.runelive.world.entity.impl.player.Player;

public class PrestigeSkillPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int prestigeId = packet.readShort();

	}

}