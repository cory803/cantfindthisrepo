package com.ikov.model.input.impl;

import com.ikov.model.definitions.ItemDefinition;
import com.ikov.model.input.EnterAmount;
import com.ikov.util.Misc;
import com.ikov.world.entity.impl.player.Player;

import java.text.NumberFormat;
import java.util.Locale;

/**
* Handles selling spirit shards
* @Jonathan Sirens
**/

public class SellShards extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		//Sets the amount that you actually have in your inventory of Spirit shards
		if(player.getInventory().getAmount(18016) < amount) {
			amount = player.getInventory().getAmount(18016);
		}
		//Tells if the amount of Spirit shards in your inventory is 0 or duplication amount under negative
		if(amount <= 0) {
			player.getPacketSender().sendMessage("You do not have this amount of Spirit shards!");
			return;
		}
		if((long) player.getInventory().getAmount(995) + ((long) amount * ItemDefinition.forId(18016).getValue()) > Integer.MAX_VALUE) {
			player.getPacketSender().sendInterfaceRemoval();
			
			//If the inventory doesn't have enough room, sell shards to pouch
			player.setMoneyInPouch((player.getMoneyInPouch() + ((long) amount * ItemDefinition.forId(18016).getValue())));
			player.getInventory().delete(18016, amount);
			player.getPacketSender().sendMessage("You have sold <col=ff0000>"+Misc.formatAmount(amount)+"</col> Spirit shards for <col=ff0000>"+Misc.formatAmount((long) amount * ItemDefinition.forId(18016).getValue())+"</col> coins to your money pouch.");
			player.getPacketSender().sendString(8135, ""+player.getMoneyInPouch());
		} else {
			if(!player.getInventory().hasRoomFor(995, amount)) {
				player.getPacketSender().sendMessage("You do not have enough inventory space to hold <col=ff0000>"+Misc.formatAmount(amount)+"</col> coins.");
				return;
			}
			player.getPacketSender().sendInterfaceRemoval();
			
			//If inventory can hold the coins, sell the spirit shards for coins to inventory
			player.getInventory().delete(18016, amount);
			player.getInventory().add(995, amount * ItemDefinition.forId(18016).getValue());
			player.getPacketSender().sendMessage("You have sold <col=ff0000>"+Misc.formatAmount(amount)+"</col> Spirit shards for <col=ff0000>"+Misc.formatAmount((long) amount * ItemDefinition.forId(18016).getValue())+"</col> coins to your inventory.");
		}
	}

}
