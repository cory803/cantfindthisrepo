package com.ikov.model.input.impl;

import com.ikov.model.definitions.ItemDefinition;
import com.ikov.model.input.EnterAmount;
import com.ikov.util.Misc;
import com.ikov.world.entity.impl.player.Player;

import java.text.NumberFormat;
import java.util.Locale;

public class SellShards extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		Locale locale = new Locale("en", "US");
		NumberFormat currencyFormatter = NumberFormat.getInstance(locale);
		boolean use_pouch = false;
		if(amount >= 85899345) {
			use_pouch = true;
		}
			
		player.getPacketSender().sendInterfaceRemoval();
		
		int shards = player.getInventory().getAmount(18016);
		if(amount > shards)
			amount = shards;
		if(amount == 0) {
			return;
		} else {
			if(use_pouch) {
				player.getInventory().delete(18016, amount);
				for(int i = 0; i < 25; i++) {
					player.setMoneyInPouch((player.getMoneyInPouch() + amount));
					player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
					player.getPacketSender().sendMessage("Your coins have been added to your coin pouch.");
				}
			} else {
				int rew = ItemDefinition.forId(18016).getValue() * amount;
				player.getInventory().delete(18016, (int) amount);
				player.getInventory().add(995, rew);
				player.getPacketSender().sendMessage("You've sold "+amount+" Spirit Shards for "+Misc.insertCommasToNumber(""+(int)rew)+" coins.");
			}
		}
	}

}
