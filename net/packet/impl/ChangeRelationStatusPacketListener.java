package com.strattus.net.packet.impl;

import com.strattus.model.PlayerRelations.PrivateChatStatus;
import com.strattus.net.packet.Packet;
import com.strattus.net.packet.PacketListener;
import com.strattus.world.entity.impl.player.Player;

public class ChangeRelationStatusPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int actionId = packet.readInt();
		player.getRelations().setStatus(PrivateChatStatus.forActionId(actionId), true);
	}

}
