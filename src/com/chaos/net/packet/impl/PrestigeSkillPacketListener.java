package com.chaos.net.packet.impl;

import com.chaos.GameSettings;
import com.chaos.model.Skill;
import com.chaos.net.packet.Packet;
import com.chaos.net.packet.PacketListener;
import com.chaos.world.entity.impl.player.Player;

public class PrestigeSkillPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int prestigeId = packet.readShort();

	}

}