package com.chaos.net.packet.impl;

import com.chaos.net.packet.Packet;
import com.chaos.net.packet.PacketListener;
import com.chaos.util.Misc;
import com.chaos.world.content.PlayerPunishment;
import com.chaos.world.content.clan.ClanChatManager;
import com.chaos.world.entity.impl.player.Player;

public class SendClanChatMessagePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		String clanMessage = Misc.readString(packet.getBuffer());
		if (clanMessage == null || clanMessage.length() < 1)
			return;
		if (PlayerPunishment.isMuted(player.getUsername()) || PlayerPunishment.isIpMuted(player.getHostAddress())
				|| Misc.blockedWord(clanMessage)) {
			player.getPacketSender().sendMessage("You either said a bad word, or currently muted!");
			return;
		}
		ClanChatManager.sendMessage(player, clanMessage);
	}

}
