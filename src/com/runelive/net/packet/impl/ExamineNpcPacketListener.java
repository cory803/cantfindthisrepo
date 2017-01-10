package com.runelive.net.packet.impl;

import com.runelive.model.definitions.NpcDefinition;
import com.runelive.net.packet.Packet;
import com.runelive.net.packet.PacketListener;
import com.runelive.world.entity.impl.player.Player;

public class ExamineNpcPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int npc = packet.readShort();
		if (npc <= 0) {
			return;
		}
		NpcDefinition npcDef = NpcDefinition.forId(npc);
		if (npcDef != null) {
			player.getPacketSender().sendMessage(npcDef.getExamine());
		}
	}

}
