package com.ikov.model.input.impl;

import com.ikov.model.definitions.ItemDefinition;
import com.ikov.model.input.EnterAmount;
import com.ikov.util.Misc;
import com.ikov.world.entity.impl.player.Player;

public class BuyShards extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		boolean use_pouch = false;
		if(amount > 85880000) {
			use_pouch = true;
		}
		if(amount > 2147000000) {
			amount = 2147000000;
		}
		if(use_pouch) {
			player.getPacketSender().sendInterfaceRemoval();
			long cost = ItemDefinition.forId(18016).getValue() * amount;
			long moneyAmount = player.getMoneyInPouch();
			long canBeBought = moneyAmount / (ItemDefinition.forId(18016).getValue());
			long totalShards = player.getInventory().getAmount(18016) + canBeBought;
			if(totalShards > 2147000000) {
				player.getPacketSender().sendMessage("You already have the max amount of spirit shards!");
				return;
			}
			if(canBeBought >= amount)
				canBeBought = amount;
			if(canBeBought == 0) {
				player.getPacketSender().sendMessage("You do not have enough money in your @red@money pouch@bla@ to buy that amount.");
				return;
			}
			for(int i = 0; i < 25; i++) {
				player.setMoneyInPouch((player.getMoneyInPouch() - canBeBought));
			}
			player.getPacketSender().sendString(8135, ""+player.getMoneyInPouch());
			player.getInventory().add(18016, (int) canBeBought);
			player.getPacketSender().sendMessage("You've bought "+canBeBought+" Spirit Shards for coins from your money pouch.");
		} else {
			player.getPacketSender().sendInterfaceRemoval();
			int cost = ItemDefinition.forId(18016).getValue() * amount;
			long moneyAmount = player.getInventory().getAmount(995);
			long canBeBought = moneyAmount / (ItemDefinition.forId(18016).getValue());
			if(canBeBought >= amount)
				canBeBought = amount;
			if(canBeBought == 0) {
				player.getPacketSender().sendMessage("You do not have enough money in your @red@inventory@bla@ to buy that amount.");
				return;
			}
			cost = ItemDefinition.forId(18016).getValue() * (int) canBeBought;
			if(player.getInventory().getAmount(995) < cost) {
				player.getPacketSender().sendMessage("You do not have enough money in your @red@inventory@bla@ to buy that amount.");
				return;
			}
			player.getInventory().delete(995, (int) cost);
			player.getInventory().add(18016, (int) canBeBought);
			player.getPacketSender().sendMessage("You've bought "+canBeBought+" Spirit Shards for "+Misc.insertCommasToNumber(""+(int)cost)+" coins.");
		}
	}

}
