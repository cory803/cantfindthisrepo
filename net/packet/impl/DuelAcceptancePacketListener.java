package com.strattus.net.packet.impl;

import com.strattus.net.packet.Packet;
import com.strattus.net.packet.PacketListener;
import com.strattus.world.World;
import com.strattus.world.entity.impl.player.Player;

public class DuelAcceptancePacketListener implements PacketListener {
	
	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0)
			return;
		player.getSkillManager().stopSkilling();
		int index = packet.getOpcode() == OPCODE ?  (packet.readShort() & 0xFF) : packet.readLEShort();
		if(index > World.getPlayers().capacity())
			return;
		Player target = World.getPlayers().get(index);
		if (target == null) 
			return;
		if(target.getIndex() != player.getIndex())
			player.getDueling().challengePlayer(target);
	}
	
	public static final int OPCODE = 128;
}
