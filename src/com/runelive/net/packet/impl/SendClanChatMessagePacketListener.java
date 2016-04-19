package com.runelive.net.packet.impl;

import com.runelive.net.packet.Packet;
import com.runelive.net.packet.PacketListener;
import com.runelive.util.Misc;
import com.runelive.world.content.PlayerPunishment;
import com.runelive.world.content.clan.ClanChatManager;
import com.runelive.world.content.dialogue.DialogueManager;
import com.runelive.world.entity.impl.player.Player;

public class SendClanChatMessagePacketListener implements PacketListener {

  @Override
  public void handleMessage(Player player, Packet packet) {
    String clanMessage = Misc.readString(packet.getBuffer());
    if (clanMessage == null || clanMessage.length() < 1)
      return;
    if (PlayerPunishment.isMuted(player.getUsername())
        || PlayerPunishment.isIpMuted(player.getHostAddress())) {
      player.getPacketSender().sendMessage("You are muted and cannot chat.");
      return;
    }
    if (Misc.blockedWord(clanMessage)) {
      DialogueManager.sendStatement(player,
          "A word was blocked in your sentence. Please do not repeat it!");
      return;
    }
    // Logs.write_data(player.getUsername()+ ".txt", "clan_chats", "["+player.getUsername()+"]:
    // "+clanMessage+"");
    ClanChatManager.sendMessage(player, clanMessage);
  }

}
