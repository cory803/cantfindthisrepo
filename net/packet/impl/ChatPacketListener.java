package com.strattus.net.packet.impl;

import com.strattus.model.ChatMessage.Message;
import com.strattus.model.Flag;
import com.strattus.net.packet.Packet;
import com.strattus.net.packet.PacketListener;
import com.strattus.util.Misc;
import com.strattus.world.content.PlayerPunishment;
import com.strattus.world.content.dialogue.DialogueManager;
import com.strattus.world.entity.impl.player.Player;

/**
 * This packet listener manages the spoken text by a player.
 * 
 * @author relex lawl
 */

public class ChatPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int effects = packet.readUnsignedByteS();
		int color = packet.readUnsignedByteS();
		int size = packet.getSize();
		byte[] text = packet.readReversedBytesA(size);
		if(PlayerPunishment.muted(player.getUsername()) || PlayerPunishment.IPMuted(player.getHostAddress())) {
			player.getPacketSender().sendMessage("You are muted and cannot chat.");
			return;
		}
		String str = Misc.textUnpack(text, size).toLowerCase().replaceAll(";", ".");
		if(Misc.blockedWord(str)) {
			DialogueManager.sendStatement(player, "A word was blocked in your sentence. Please do not repeat it!");
			return;
		}
		player.getChatMessages().set(new Message(color, effects, text));
		player.getUpdateFlag().flag(Flag.CHAT);
	}

}
