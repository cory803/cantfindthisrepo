package com.strattus.net.packet.impl;

import com.strattus.net.packet.Packet;
import com.strattus.net.packet.PacketListener;
import com.strattus.util.Misc;
import com.strattus.world.content.PlayerPunishment;
import com.strattus.world.content.clan.ClanChatManager;
import com.strattus.world.content.dialogue.DialogueManager;
import com.strattus.world.entity.impl.player.Player;

public class SendClanChatMessagePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		String clanMessage = Misc.readString(packet.getBuffer());
		if(clanMessage == null || clanMessage.length() < 1)
			return;
		if(PlayerPunishment.muted(player.getUsername()) || PlayerPunishment.IPMuted(player.getHostAddress())) {
			player.getPacketSender().sendMessage("You are muted and cannot chat.");
			return;
		}
		if(Misc.blockedWord(clanMessage)) {
			DialogueManager.sendStatement(player, "A word was blocked in your sentence. Please do not repeat it!");
			return;
		}
		ClanChatManager.sendMessage(player, clanMessage);
	}

}
