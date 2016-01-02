package com.ikov.net.packet.impl;

import com.ikov.model.ChatMessage.Message;
import com.ikov.model.Flag;
import com.ikov.net.packet.Packet;
import com.ikov.net.packet.PacketListener;
import com.ikov.util.Misc;
import com.ikov.world.content.PlayerPunishment;
import com.ikov.world.content.dialogue.DialogueManager;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.util.Logs;

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
			Logs.write_data(player.getUsername()+ ".txt", "advertisers", "Player was caught saying in global chat: "+str+"");
			DialogueManager.sendStatement(player, "A word was blocked in your sentence. Please do not repeat it!");
			return;
		}
		Logs.write_data(player.getUsername()+ ".txt", "global_chats", ""+str+"");
		player.getChatMessages().set(new Message(color, effects, text));
		player.getUpdateFlag().flag(Flag.CHAT);
	}

}
