package com.chaos.net.packet.impl;

import com.chaos.GameSettings;
import com.chaos.net.packet.Packet;
import com.chaos.net.packet.PacketListener;
import com.chaos.world.content.BankPin;
import com.chaos.world.content.MoneyPouch;
import com.chaos.world.entity.impl.player.Player;

public class WithdrawMoneyFromPouchPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
				&& player.getBankPinAttributes().onDifferent(player)) {
			BankPin.init(player, false);
			return;
		}
		int amount = packet.readInt();
		MoneyPouch.withdrawMoney(player, amount);
		if (GameSettings.DEBUG_MODE) {
			// PlayerLogs.log(player,
			// "" + player.getUsername() + " in
			// WithdrawMoneyFromPouchPacketListener " + amount + "");
		}
	}

}
