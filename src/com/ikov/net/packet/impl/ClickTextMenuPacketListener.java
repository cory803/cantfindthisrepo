package com.ikov.net.packet.impl;

import com.ikov.model.PlayerRights;
import com.ikov.net.packet.Packet;
import com.ikov.net.packet.PacketListener;
import com.ikov.world.content.clan.ClanChatManager;
import com.ikov.world.entity.impl.player.Player;

import com.ikov.GameSettings;
import com.ikov.world.content.PlayerLogs;


public class ClickTextMenuPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {

		int interfaceId = packet.readShort();
		int menuId = packet.readByte();

		if(player.getRights() == PlayerRights.OWNER) {
			player.getPacketSender().sendConsoleMessage("Clicked text menu: "+interfaceId+", menuId: "+menuId);
		}
		if(GameSettings.DEBUG_MODE) {
			PlayerLogs.log(player.getUsername(), ""+player.getUsername()+" has changed their private status in ChangeRelationStatusPacketListener "+interfaceId+", "+menuId+"");
		}
		if(interfaceId >= 29344 && interfaceId <= 29443) { // Clan chat list
			int index = interfaceId - 29344;
			ClanChatManager.handleMemberOption(player, index, menuId);
		}
		
	}

	public static final int OPCODE = 222;
}