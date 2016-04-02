package com.ikov.model.input.impl;

import com.ikov.model.input.Input;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.world.content.pos.PosOffers;
import com.ikov.world.content.pos.PlayerOwnedShops;
import com.ikov.model.definitions.ItemDefinition;

public class PosItemToBuy extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		if(syntax.length() <= 1) {
			player.getPacketSender().sendMessage("Invalid syntax entered.");
			return;
		}
		int start_button = 23666;
		int start_owner_name = 41468;
		int start_caption = 41469;
		int index = 0;
		
		int[][] search_results_values = new int[100][3];
		//0 = start_button
		//1 = start_owner_name
		//2 = start_caption

		String[][] search_results_strings = new String[100][2];
		//0 = owner_name
		//1 = owner_caption
		
		String item_searched = syntax;
		for(PosOffers o : PlayerOwnedShops.SHOPS) {
			if(o == null) {
				continue;
			}
			if(index >= 100) {
				break;
			}
			for(int i = 0; i < o.getAmountInShop(); i++) {
				if(index >= 100) {
					break;
				}
				int item_id = o.getSellOffers()[i][0];
				String item_name = ItemDefinition.forId(item_id).name;
				if(item_name.contains(item_searched)) {
					start_button += 4;
					start_caption += 4;
					start_owner_name += 4;
					search_results_values[index][0] = start_button;
					search_results_values[index][1] = start_owner_name;
					search_results_values[index][2] = start_caption;
					search_results_strings[index][0] = o.getOwner();
					search_results_strings[index][1] = "Found: "+item_name+" for "+formatAmount(o.getPrice()[i])+"";
					player.getPacketSender().sendString(search_results_values[index][1], search_results_strings[index][0]);
					player.getPacketSender().sendString(search_results_values[index][2], search_results_strings[index][1]);
					index++;
				}
			}
			
			//Buttons
			/*
			for(int index = 24062; index > 23666; index -= 4) {
				player.getPacketSender().sendString(index, "test");
			}
			*/
		}
		player.getPacketSender().sendMessage("Does nothing yet");
	}
	
	public final String formatAmount(long amount) {
		String format = "Too high!";
		if (amount >= 0 && amount < 100000) {
			format = String.valueOf(amount);
		} else if (amount >= 100000 && amount < 1000000) {
			format = amount / 1000 + "K";
		} else if (amount >= 1000000 && amount < 1000000000L) {
			format = amount / 1000000 + "M";
		} else if (amount >= 1000000000L && amount < 1000000000000L) {
			format = amount / 1000000000 + "B";
		} else if (amount >= 10000000000000L && amount < 10000000000000000L) {
			format = amount / 1000000000000L + "T";
		} else if (amount >= 10000000000000000L && amount < 1000000000000000000L) {
			format = amount / 1000000000000000L + "QD";
		} else if (amount >= 1000000000000000000L && amount < Long.MAX_VALUE) {
			format = amount / 1000000000000000000L + "QT";
		}
		return format;
	}
	
}
