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
import com.ikov.model.input.impl.PosItemToBuy;
import com.ikov.model.input.impl.PosSearchShop;
import com.ikov.world.entity.impl.player.Player;

public class PlayerOwnedShops {
	
	public static final PosOffers[] SHOPS = new PosOffers[5000];

	public static void init() {
		try {
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
					int index = in.readInt();
					int amount_in_shop = in.readInt();
					int coins_to_collect = in.readInt();
					int[][] sell_offers = new int[amount_in_shop][4];
					long[] price = new long[amount_in_shop];
					for(int i2 = 0; i2 < amount_in_shop; i2++) {
						sell_offers[i2][0] = in.readInt(); //Item id
						sell_offers[i2][1] = in.readInt(); //Item amount
						sell_offers[i2][3] = in.readInt(); //Amount Sold
						price[i2] = in.readLong();
					}
					SHOPS[index] = new PosOffers(owner_name, store_caption, index, amount_in_shop, coins_to_collect, sell_offers, price);
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
	
	public static void openShop(String username, Player player) {
		int[] stock = new int[40];
		int[] stockAmount = new int[40];
		for(int i = 0; i < stock.length; i++) {
			stock[i] = -1;
			stockAmount[i] = 1;
		}
		int length_of_shops = 0;
		boolean opened_shop = false;
		for(PosOffers o : SHOPS) {
			length_of_shops++;
			if(o == null) {
				if(!opened_shop) {
					if(player.getUsername().equalsIgnoreCase(username)) {
						int[][] sell_offers = new int[0][4];
						long[] price = new long[0];
						SHOPS[length_of_shops - 1] = new PosOffers(player.getUsername(), player.getUsername()+"'s store", length_of_shops - 1, 0, 0, sell_offers, price);
						openShop(player.getUsername(), player);
						break;
					} else {
						player.getPacketSender().sendMessage("This shop does not exist!");
					}
				}
				break;
			}
			if(o.getOwner().toLowerCase().equals(username.toLowerCase())) {
				Item[] stockItems = new Item[stock.length];
				for(int i = 0; i < stock.length; i++) {
					if(i > o.getAmountInShop() - 1) {
						stockItems[i] = new Item(-1, 1);
					} else {
						stockItems[i] = new Item(o.getSellOffers()[i][0], o.getSellOffers()[i][1]);
					}
				}
				Shop shop = new Shop(player, Shop.POS, o.getOwner()+"'s shop", new Item(995), stockItems);
				stock = stockAmount = null;
				stockItems = null;
				shop.setPlayer(player);
				player.getPacketSender().sendItemContainer(player.getInventory(), Shop.INVENTORY_INTERFACE_ID);
				player.getPacketSender().sendItemContainer(shop, Shop.ITEM_CHILD_ID);
				player.getPacketSender().sendString(Shop.NAME_INTERFACE_CHILD_ID, o.getOwner()+"'s shop");
				if(player.getInputHandling() == null || !(player.getInputHandling() instanceof EnterAmountToSellToShop || player.getInputHandling() instanceof EnterAmountToBuyFromShop))
					player.getPacketSender().sendInterfaceSet(Shop.INTERFACE_ID, Shop.INVENTORY_INTERFACE_ID - 1);
				
				player.setShop(shop).setInterfaceId(Shop.INTERFACE_ID).setShopping(true);
				opened_shop = true;
			}
			if(length_of_shops == SHOPS.length && !opened_shop) {
				player.getPacketSender().sendMessage("This shop does not exist!");
			}
		}
	}
}
