package com.runelive.net.packet.impl;

import com.runelive.GameSettings;
import com.runelive.model.PlayerRights;
import com.runelive.net.packet.Packet;
import com.runelive.net.packet.PacketListener;
import com.runelive.world.content.clan.ClanChatManager;
import com.runelive.world.entity.impl.player.Player;

public class ClickTextMenuPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {

		int interfaceId = packet.readShort();
		int menuId = packet.readByte();

		if (player.getRights() == PlayerRights.DEVELOPER) {
			player.getPacketSender().sendConsoleMessage("Clicked text menu: " + interfaceId + ", menuId: " + menuId);
		}
		if (GameSettings.DEBUG_MODE) {
			// PlayerLogs.log(player,
			// "" + player.getUsername()
			// + " has changed their private status in
			// ChangeRelationStatusPacketListener "
			// + interfaceId + ", " + menuId + "");
		}
		if (interfaceId >= 29344 && interfaceId <= 29443) { // Clan chat list
			int index = interfaceId - 29344;
			ClanChatManager.handleMemberOption(player, index, menuId);
		}

	}

	public static final int OPCODE = 222;
}
