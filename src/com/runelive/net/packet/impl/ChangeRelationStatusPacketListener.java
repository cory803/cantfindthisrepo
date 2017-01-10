package com.runelive.net.packet.impl;

import com.runelive.GameSettings;
import com.runelive.model.PlayerRelations.PrivateChatStatus;
import com.runelive.net.packet.Packet;
import com.runelive.net.packet.PacketListener;
import com.runelive.world.entity.impl.player.Player;

public class ChangeRelationStatusPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int actionId = packet.readInt();
		if (GameSettings.DEBUG_MODE) {
			// PlayerLogs.log(player, "" + player.getUsername()
			// + " has changed their private status in
			// ChangeRelationStatusPacketListener");
		}
		player.getRelations().setStatus(PrivateChatStatus.forActionId(actionId), true);
	}

}
