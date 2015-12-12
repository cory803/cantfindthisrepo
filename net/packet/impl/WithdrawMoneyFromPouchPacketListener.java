package com.ikov.net.packet.impl;

import com.ikov.net.packet.Packet;
import com.ikov.net.packet.PacketListener;
import com.ikov.world.content.MoneyPouch;
import com.ikov.world.entity.impl.player.Player;

public class WithdrawMoneyFromPouchPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int amount = packet.readInt();
		MoneyPouch.withdrawMoney(player, amount);
	}

}
