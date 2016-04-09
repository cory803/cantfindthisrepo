package com.ikov.net.packet.impl;

import com.ikov.model.Item;
import com.ikov.model.definitions.ItemDefinition;
import com.ikov.net.packet.Packet;
import com.ikov.net.packet.PacketListener;
import com.ikov.world.content.grandexchange.GrandExchange;
import com.ikov.world.entity.impl.player.Player;

import com.ikov.GameSettings;
import com.ikov.world.content.PlayerLogs;


public class GESelectItemPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int item = packet.readShort();
		if(item <= 0 || item >= ItemDefinition.getMaxAmountOfItems())
			return;
		ItemDefinition def = ItemDefinition.forId(item);
		if(def != null) {
			if(def.getValue() <= 0 || !Item.tradeable(item) || item == 995) {
				player.getPacketSender().sendMessage("This item can currently not be purchased or sold in the Grand Exchange.");
				return;
			}
			if(GameSettings.DEBUG_MODE) {
				PlayerLogs.log(player.getUsername(), ""+player.getUsername()+" in GESelectItemPacketListener: "+item+"");
			}
			GrandExchange.setSelectedItem(player, item);
		}
	}

}