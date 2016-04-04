package com.ikov.model.input.impl;

import com.ikov.model.definitions.ItemDefinition;
import com.ikov.model.input.EnterAmount;
import com.ikov.util.Misc;
import com.ikov.world.entity.impl.player.Player;

public class SellShards extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		boolean use_pouch = false;
		if(amount > 85880000) {
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
				}
				player.getPacketSender().sendMessage("You've sold "+amount+" Spirit Shards for coins into your money pouch.");
				player.getPacketSender().sendString(8135, ""+player.getMoneyInPouch());
			} else {
				int rew = ItemDefinition.forId(18016).getValue() * amount;
				player.getInventory().delete(18016, (int) amount);
				player.getInventory().add(995, rew);
				player.getPacketSender().sendMessage("You've sold "+amount+" Spirit Shards for "+Misc.insertCommasToNumber(""+(int)rew)+" coins.");
			}
		}
	}

}
