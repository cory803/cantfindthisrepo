package com.runelive.net.packet.impl;

import com.runelive.model.ChatMessage.Message;
import com.runelive.model.Flag;
import com.runelive.net.packet.Packet;
import com.runelive.net.packet.PacketListener;
import com.runelive.util.Misc;
import com.runelive.world.content.PlayerPunishment;
import com.runelive.world.content.dialogue.DialogueManager;
import com.runelive.world.entity.impl.player.Player;

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
    if (PlayerPunishment.isMuted(player.getUsername())
        || PlayerPunishment.isIpMuted(player.getHostAddress())) {
      player.getPacketSender().sendMessage("You are muted and cannot chat.");
      return;
    }
    String str = Misc.textUnpack(text, size).toLowerCase().replaceAll(";", ".");
    if (Misc.blockedWord(str)) {
      // Logs.write_data(player.getUsername()+ ".txt", "advertisers", "Player was caught saying in
      // global chat: "+str+"");
      DialogueManager.sendStatement(player,
          "A word was blocked in your sentence. Please do not repeat it!");
      return;
    }
    // Logs.write_data(player.getUsername()+ ".txt", "global_chats", ""+str+"");
    player.getChatMessages().set(new Message(color, effects, text));
    player.getUpdateFlag().flag(Flag.CHAT);
  }

}
