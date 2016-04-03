package com.ikov.world.content.pos;

import java.io.File;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;

import com.ikov.model.Item;
import com.ikov.model.container.impl.Shop;
import com.ikov.model.input.impl.EnterAmountToBuyFromShop;
import com.ikov.model.input.impl.EnterAmountToSellToShop;
import com.ikov.model.input.impl.EnterAmountOfPosOfferPrice;
import com.ikov.model.input.impl.PosItemToBuy;
import com.ikov.model.input.impl.PosSearchShop;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.model.container.impl.PlayerOwnedShopContainer.PlayerOwnedShopManager;
import com.ikov.model.container.impl.PlayerOwnedShopContainer;

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
			DataOutputStream out = new DataOutputStream(new FileOutputStream("./data/saves/pos/shops.dat", false));
			out.writeInt(getCount());
			for (PosOffers l : SHOPS) {
				if(l == null) {
					continue;
				}
				l.save(out);
			}
			out.close();
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
				player.setInputHandling(new PosItemToBuy());
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
				offer.increaseAmount(item_amount);
			} else {
				if (!o.addOffer(new PosOffer(item_id, item_amount, 0, price)))
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
	
	private static boolean opened_shop;
	public static void openShop(String username, Player player) {
		int[] stock = new int[40];
		int[] stockAmount = new int[40];
		for(int i = 0; i < stock.length; i++) {
			stock[i] = -1;
			stockAmount[i] = 1;
		}
		
		for (int i = 0; i < SHOPS.length; i++) {
			PosOffers o = SHOPS[i];
			if (o == null) {
				if(!opened_shop) {
					if(player.getUsername().equalsIgnoreCase(username)) {
						PosOffer[] offers = new PosOffer[40];
						SHOPS[i] = new PosOffers(player.getUsername(), player.getUsername()+"'s store", offers.length, 0, offers);
						Item[] default_items = new Item[0];
						PlayerOwnedShopManager.getShops().put(i, new PlayerOwnedShopContainer(null, player.getUsername(), default_items));
						opened_shop = true;
						openShop(player.getUsername(), player);
						break;
					} else {
						player.getPacketSender().sendMessage("This shop does not exist!");
					}
				}
				break;
			}
			if(o.getOwner().toLowerCase().equals(username.toLowerCase())) {
				System.out.println("Owner index: " + PlayerOwnedShopContainer.getIndex(o.getOwner()));
				PlayerOwnedShopManager.getShops().get(PlayerOwnedShopContainer.getIndex(o.getOwner())).open(player, username.toLowerCase());
				//opened_shop = true;
			}
			if(i == SHOPS.length && !opened_shop) {
				player.getPacketSender().sendMessage("This shop does not exist!");
			}
		}
	}
	
}
