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
import com.ikov.model.definitions.ItemDefinition;
import com.ikov.world.content.PlayerLogs;

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
					if (shopItems > 40) {
						System.out.println(owner_name + " has more than 40 items in pos.");
						shopItems = 40;
					}
					int coins_to_collect = (int) in.readLong();
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
			System.out.println("Player owned shops have been saved...");
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
				offer.increaseAmount(item_amount);
			} else {
				if (o.addOffer(new PosOffer(item_id, item_amount, 0, price)))
					player.getPacketSender().sendMessage("You have sucessfully placed your <col=CA024B>" + ItemDefinition.forId(item_id).getName() + "</col> for sale for <col=CA024B>" + formatAmount(price) + "</col>");
				else
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
		for (int i = 0; i < SHOPS.length; i++) {
			PosOffers o = SHOPS[i];
			if (o != null) {
				if(o.getOwner().toLowerCase().equals(username.toLowerCase())) {
					//player.getPacketSender().sendString(3903, "Shop caption");
					PlayerOwnedShopManager.getShops().get(PlayerOwnedShopContainer.getIndex(o.getOwner())).open(player, username.toLowerCase());
					PlayerLogs.log(player.getUsername(), "Opened the player owned shop: "+username+"");
					if(i == SHOPS.length)
						player.getPacketSender().sendMessage("This shop does not exist!");
					break;
				}
			} else {
				if (player.getUsername().equalsIgnoreCase(username)) {
					PosOffer[] offers = new PosOffer[0];
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
	
	public static void collectCoinsOnLogin(Player player) {
		for (PosOffers o : SHOPS) {
			if (o == null)
				continue;
			if(o.getOwner().toLowerCase().equals(player.getUsername().toLowerCase())) {
				if(o.getCoinsToCollect() >= 1) {
					player.setMoneyInPouch((player.getMoneyInPouch() + (o.getCoinsToCollect())));
					player.getPacketSender().sendString(8135, ""+player.getMoneyInPouch());
					player.getPacketSender().sendString(1, ":moneypouchearning:"+o.getCoinsToCollect());
					player.getPacketSender().sendMessage("Your items have sold for <col=CA024B>"+formatAmount(o.getCoinsToCollect())+"</col>");
					PlayerLogs.log(player.getUsername(), "Player owned shop items sold for: "+formatAmount(o.getCoinsToCollect())+"");
					o.resetCoinsCollect();
					save();
				}
			}
		}
	}
	
	public static final String formatAmount(long amount) {
		String format = "Too high!";
		if (amount >= 0 && amount < 100000) {
			format = String.valueOf(amount);
		} else if (amount >= 100000 && amount < 1000000) {
			format = amount / 1000 + "K";
		} else if (amount >= 1000000 && amount < 10000000000L) {
			format = amount / 1000000 + "M";
		} else if (amount >= 10000000000L && amount < 1000000000000L) {
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
