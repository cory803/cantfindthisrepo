package com.ikov.world.content.pos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.ikov.model.Item;
import com.ikov.model.container.impl.PlayerOwnedShopContainer;
import com.ikov.model.container.impl.PlayerOwnedShopContainer.PlayerOwnedShopManager;
import com.ikov.model.input.impl.PosItemSearch;
import com.ikov.model.input.impl.PosSearchShop;
import com.ikov.world.entity.impl.player.Player;

public class PlayerOwnedShops {
	
	public static final PosOffers[] SHOPS = new PosOffers[5000];

	public static void init() {
		try {
			//SHOPS[0] = new PosOffers("Jonny", "Jonny's Shop", 2, 0, new PosOffer[] {new PosOffer(4151, 1, 0, 110000), new PosOffer(1050, 1, 0, 1170000)});
			//SHOPS[1] = new PosOffers("Blake", "Blake's Shop", 2, 0, new PosOffer[] {new PosOffer(4151, 1, 0, 123000), new PosOffer(1050, 1, 0, 121000)});
			File file = new File("./data/saves/pos/shops.dat");
			if (!file.exists()) {
				return;
			}
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			int count = in.readInt();
			if (count > 0) {
				for (int i = 0; i < count; i++) {
					String owner_name = in.readUTF();
					String store_caption = in.readUTF();
					int shopItems = in.readInt();
					int coins_to_collect = in.readInt();
					PosOffer[] sell_offers = new PosOffer[shopItems];
					for(int i2 = 0; i2 < shopItems; i2++) {
						sell_offers[i2] = new PosOffer(in.readInt(), in.readInt(), in.readInt(), in.readLong());
					}
					SHOPS[i] = new PosOffers(owner_name, store_caption, shopItems, coins_to_collect, sell_offers);
				}
			}
			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void save() {
		try {
			File pos = new File("./data/saves/pos");
			if (!pos.exists())
				pos.mkdirs();
			DataOutputStream out = new DataOutputStream(new FileOutputStream(pos + "/shops.dat", false));
			out.writeInt(getCount());
			for (PosOffers l : SHOPS) {
				if (l == null) {
					continue;
				}
				l.save(out);
			}
			out.close();
			System.out.println("[POS] Saved!");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static int getCount() {
		int count = 0;
		for(int i = 0; i < SHOPS.length; i++) {
			if(SHOPS[i] == null) {
				continue;
			}
			count++;
		}
		return count;
	}
	
	public static boolean posButtons(Player player, int buttonId) {
		switch(buttonId) {
			case -24062:
				
				break;
			case -24074: //Search by Name
				player.setInputHandling(new PosSearchShop());
				player.getPacketSender().sendEnterInputPrompt("Enter the name of a player's shop:");
				return true;
			case -24073: //Search by Item
				player.setInputHandling(new PosItemSearch());
				player.getPacketSender().sendEnterInputPrompt("Enter the name of the item you wish to buy:");
				return true;
		}
		return false;
	}
	
	public static void soldItem(Player player, int index, int item_id, int item_amount, long price) {
		PosOffers o = SHOPS[index];
		if (o != null) {
			PosOffer offer = o.forId(item_id);
			if (offer != null) {
				offer.increaseAmount(1);
			} else {
				if (!o.addOffer(new PosOffer(item_id, 1, 0, price)))
					player.getPacketSender().sendMessage("Shop full!");
			}
		} else {
			System.out.println("Error: Shop null");
		}		
	}
	
	public static int getIndex(String name) {
		for (int i = 0; i < SHOPS.length; i++) {
			PosOffers p = SHOPS[i];
			if (p != null) {
				if (p.getOwner().equalsIgnoreCase(name))
					return i;
			}
		}
		return -1;
	}
	
	public static int getFreeIndex() {
		for (int i = 0; i < SHOPS.length; i++) {
			PosOffers p = SHOPS[i];
			if (p == null) {
				return i;
			}
		}
		return -1;
	}
	
	public static void openShop(String username, Player player) {
		int[] stock = new int[40];
		int[] stockAmount = new int[40];
		for(int i = 0; i < stock.length; i++) {
			stock[i] = -1;
			stockAmount[i] = 1;
		}
		
		for (int i = 0; i < SHOPS.length; i++) {
			PosOffers o = SHOPS[i];
			if (o != null) {
				if(o.getOwner().toLowerCase().equals(username.toLowerCase())) {
					PlayerOwnedShopManager.getShops().get(PlayerOwnedShopContainer.getIndex(o.getOwner())).open(player, username.toLowerCase());
					if(i == SHOPS.length)
						player.getPacketSender().sendMessage("This shop does not exist!");
					break;
				}
			} else {
				if (player.getUsername().equalsIgnoreCase(username)) {
					PosOffer[] offers = new PosOffer[40];
					SHOPS[i] = new PosOffers(player.getUsername(), player.getUsername() + "'s store", offers.length, 0, offers);
					Item[] default_items = new Item[0];
					PlayerOwnedShopManager.getShops().put(i, new PlayerOwnedShopContainer(null, player.getUsername(), default_items));
					openShop(player.getUsername(), player);
					break;
				} else {
					player.getPacketSender().sendMessage("This shop does not exist!");
				}
				break;
			}
		}
	}
	
	public static void openItemSearch(Player player) {
		PosItemSearch.reset();
		for(int caption_index = 41869; caption_index > 41469; caption_index -= 4) {
			player.getPacketSender().sendString(caption_index, "");
		}
		for(int owner_name_index = 41868; owner_name_index > 41468; owner_name_index -= 4) {
			player.getPacketSender().sendString(owner_name_index, "");
		}
		player.getPacketSender().sendInterface(41409);
	}
	
}
