package com.strattus.net.packet.impl;

import com.strattus.net.packet.Packet;
import com.strattus.net.packet.PacketListener;
import com.strattus.world.content.MoneyPouch;
import com.strattus.world.entity.impl.player.Player;

public class WithdrawMoneyFromPouchPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int amount = packet.readInt();
		MoneyPouch.withdrawMoney(player, amount);
	}

}
