package com.runelive.net.packet.impl;

import com.runelive.GameSettings;
import com.runelive.net.packet.Packet;
import com.runelive.net.packet.PacketListener;
import com.runelive.world.entity.impl.player.Player;

public class CloseInterfacePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		player.getPacketSender().sendClientRightClickRemoval();
		player.getPacketSender().sendInterfaceRemoval();
		// player.getPacketSender().sendTabInterface(Constants.CLAN_CHAT_TAB,
		// 29328); //Clan chat tab
		// player.getAttributes().setSkillGuideInterfaceData(null);
		if (GameSettings.DEBUG_MODE) {
			// PlayerLogs.log(player, "" + player.getUsername()
			// + " has changed close interface in
			// CloseInterfacePacketListener");
		}
	}
}
