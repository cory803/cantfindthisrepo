package com.runelive.net.packet.impl;

import com.runelive.GameSettings;
import com.runelive.net.packet.Packet;
import com.runelive.net.packet.PacketListener;
import com.runelive.world.content.BankPin;
import com.runelive.world.content.MoneyPouch;
import com.runelive.world.content.PlayerLogs;
import com.runelive.world.entity.impl.player.Player;


public class WithdrawMoneyFromPouchPacketListener implements PacketListener {

  @Override
  public void handleMessage(Player player, Packet packet) {
    if (player.getBankPinAttributes().hasBankPin()
        && !player.getBankPinAttributes().hasEnteredBankPin()
        && player.getBankPinAttributes().onDifferent(player)) {
      BankPin.init(player, false);
      return;
    }
    int amount = packet.readInt();
    MoneyPouch.withdrawMoney(player, amount);
    if (GameSettings.DEBUG_MODE) {
      PlayerLogs.log(player.getUsername(),
          "" + player.getUsername() + " in WithdrawMoneyFromPouchPacketListener " + amount + "");
    }
  }

}
