package com.ikov.net.packet.impl;

import com.ikov.net.packet.Packet;
import com.ikov.net.packet.PacketListener;
import com.ikov.world.content.MoneyPouch;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.world.content.BankPin;
import com.ikov.GameSettings;
import com.ikov.world.content.PlayerLogs;


public class WithdrawMoneyFromPouchPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if(player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin() && player.getBankPinAttributes().onDifferent(player)) {
			BankPin.init(player, false);
			return;
		}
		int amount = packet.readInt();
		MoneyPouch.withdrawMoney(player, amount);
		if(GameSettings.DEBUG_MODE) {
			PlayerLogs.log(player.getUsername(), ""+player.getUsername()+" in WithdrawMoneyFromPouchPacketListener "+amount+"");
		}
	}

}
