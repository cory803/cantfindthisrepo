package com.runelive.net.packet.impl;

import com.runelive.net.packet.Packet;
import com.runelive.net.packet.PacketListener;
import com.runelive.util.Misc;
import com.runelive.world.content.PlayerPunishment;
import com.runelive.world.content.clan.ClanChatManager;
import com.runelive.world.entity.impl.player.Player;

public class SendClanChatMessagePacketListener implements PacketListener {

  @Override
  public void handleMessage(Player player, Packet packet) {
    String clanMessage = Misc.readString(packet.getBuffer());
    if (clanMessage == null || clanMessage.length() < 1)
      return;
    if (PlayerPunishment.isMuted(player.getUsername())
        || PlayerPunishment.isIpMuted(player.getHostAddress()) || Misc.blockedWord(clanMessage)) {
      player.getPacketSender().sendMessage("You either said a bad word, or currently muted!");
      return;
    }
    ClanChatManager.sendMessage(player, clanMessage);
  }

}
