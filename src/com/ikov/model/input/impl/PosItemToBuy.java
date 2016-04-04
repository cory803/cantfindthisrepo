package com.ikov.model.input.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import com.ikov.model.definitions.ItemDefinition;
import com.ikov.model.input.Input;
import com.ikov.world.content.pos.PlayerOwnedShops;
import com.ikov.world.content.pos.PosDetails;
import com.ikov.world.content.pos.PosOffer;
import com.ikov.world.content.pos.PosOffers;
import com.ikov.world.entity.impl.player.Player;

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
		int searchedItem = ItemDefinition.getIdContaining(item_searched);
		if (searchedItem == -1)
			return;
		
		Map<PosDetails, PosOffer> foundOffers = new HashMap<PosDetails, PosOffer>();
		
		for (PosOffers o : PlayerOwnedShops.SHOPS) {
			if (o == null)
				continue;
			
			for (int q = 0; q<o.getOffers().size(); q++) {
				if (o.getOffers().get(q).getItemId() == searchedItem) {
					foundOffers.put(new PosDetails(o.getOwner(), o.getCaption()), new PosOffer(o.getOffers().get(q).getItemId(), o.getOffers().get(q).getAmount(), o.getOffers().get(q).getSoldAmount(), o.getOffers().get(q).getPrice()));
				}
			}
		}
		
		if (foundOffers.size() < 1) {
			player.getPacketSender().sendMessage("Your search did not return any offers.");
			return;
		}

		foundOffers = sortByValue(foundOffers);

		for (Map.Entry<PosDetails, PosOffer> entry : foundOffers.entrySet()) {
			if(index >= 100) {
				break;
			}
			PosDetails pd = entry.getKey();
			PosOffer p = entry.getValue();

			String item_name = ItemDefinition.forId(p.getItemId()).name;
			start_button += 4;
			start_caption += 4;
			start_owner_name += 4;
			search_results_values[index][0] = start_button;
			search_results_values[index][1] = start_owner_name;
			search_results_values[index][2] = start_caption;
			search_results_strings[index][0] = pd.getOwner();
			search_results_strings[index][1] = "Found: " + item_name + " for " + formatAmount(p.getPrice()) + "";
			player.getPacketSender().sendString(search_results_values[index][1], search_results_strings[index][0]);
			player.getPacketSender().sendString(search_results_values[index][2], search_results_strings[index][1]);
			index++;
		}
		
		player.getPacketSender().sendMessage("Does nothing yet");
	}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		Map<K, V> result = new LinkedHashMap<>();
		Stream<Entry<K, V>> st = map.entrySet().stream();

		st.sorted(Comparator.comparing(e -> e.getValue())).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));

		return result;
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
