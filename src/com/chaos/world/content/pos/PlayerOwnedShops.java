package com.chaos.world.content.pos;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.chaos.model.Item;
import com.chaos.model.container.impl.PlayerOwnedShopContainer;
import com.chaos.model.container.impl.PlayerOwnedShopContainer.PlayerOwnedShopManager;
import com.chaos.model.definitions.ItemDefinition;
import com.chaos.model.input.impl.PosItemSearch;
import com.chaos.model.input.impl.PosSearchShop;
import com.chaos.world.content.PlayerLogs;
import com.chaos.world.entity.impl.player.Player;

public class PlayerOwnedShops {

	public static ArrayList<PosOffers> SHOPS_ARRAYLIST = new ArrayList<>();
	public static ArrayList<String> SHOPS_TO_SEARCH = new ArrayList<>();

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
					int shopItems = in.readInt();
					long coins_to_collect = in.readLong();
					PosOffer[] sell_offers = new PosOffer[shopItems];
					for (int i2 = 0; i2 < shopItems; i2++) {
						sell_offers[i2] = new PosOffer(in.readInt(), in.readInt(), in.readInt(), in.readLong());
						PosOffer off = sell_offers[i2];
					}
					SHOPS_ARRAYLIST
							.add(new PosOffers(owner_name, store_caption, shopItems, coins_to_collect, sell_offers));
					SHOPS_TO_SEARCH.add(owner_name.toLowerCase());
				}
			}
			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static SortedMap<String, Object> getByPreffix(
			NavigableMap<String, Object> myMap,
			String preffix ) {
		return myMap.subMap( preffix, preffix + Character.MAX_VALUE );
	}

	public static void displayFeaturedShops(Player player) {

		/*
		 * int[] featuredShopNameIds = {41422, 41426, 41430, 41434, 41438,
		 * 41442, 41446, 41450, 41454, 41458}; int[] featuredShopLastsIds =
		 * {41423, 41427, 41431, 41435, 41439, 41443, 41447, 41451, 41455,
		 * 41459}; int totalShops = 0; ArrayList<Integer> shop_ids = new
		 * ArrayList<Integer>(); for (int i = 0; i < SHOPS.length; i++) {
		 * PosOffers p = SHOPS[i]; if (p != null) { shop_ids.add(i); } } for(int
		 * i2 = 0; i2 < 9; i2++) { int shops = Misc.getRandom(shop_ids.size());
		 * player.getPacketSender().sendString(featuredShopNameIds[i2],
		 * SHOPS[shop_ids.get(shops)].getOwner());
		 * player.getPacketSender().sendString(featuredShopLastsIds[i2],
		 * "Unlimited"); }
		 */
	}

	private static final Executor saveWorker = Executors.newSingleThreadExecutor();

	private static final Runnable saveRunnable = () -> saveShops();

	public static void saveShops() {
		File pos = new File("./data/saves/pos");
		if (!pos.exists()) {
			pos.mkdirs();
		}
		RandomAccessFile file = null;
		try {
			file = new RandomAccessFile(pos + "/shops.dat", "rw");
			file.writeInt(getCount());
			Iterator<PosOffers> it = SHOPS_ARRAYLIST.iterator();
			while (it.hasNext()) {
				PosOffers offer = it.next();
				if (offer == null) {
					continue;
				}
				offer.saveRAF(file);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void save() {
		saveWorker.execute(saveRunnable);
		/*
		 * try { File pos = new File("./data/saves/pos"); if (!pos.exists())
		 * pos.mkdirs(); DataOutputStream out = new DataOutputStream(new
		 * FileOutputStream(pos + "/shops.dat", false));
		 * out.writeInt(getCount()); for (PosOffers l : SHOPS) { if (l == null)
		 * { continue; } l.save(out); } out.close(); System.out.println(
		 * "Player owned shops have been saved..."); } catch (Exception ex) {
		 * ex.printStackTrace(); }
		 */
	}

	private static int getCount() {
		return SHOPS_ARRAYLIST.size();
	}

	public static boolean posButtons(Player player, int buttonId) {
		switch (buttonId) {
		case -24062:

			break;
		case -24114: // Search by Name
			if (player.getGameModeAssistant().isIronMan()) {
				player.getPacketSender().sendMessage("Ironmen can't use the player owned shops!");
				return false;
			}
			player.setInputHandling(new PosSearchShop());
			player.getPacketSender().sendEnterInputPrompt("Enter the name of a player's shop:");
			return true;
		case -24113: // Search by Item
			if (player.getGameModeAssistant().isIronMan()) {
				player.getPacketSender().sendMessage("Ironmen can't use the player owned shops!");
				return false;
			}
			player.setInputHandling(new PosItemSearch());
			player.getPacketSender().sendEnterInputPrompt("Enter the name of the item you wish to buy:");
			return true;
		}
		return false;
	}

	public static void soldItem(Player player, int index, int item_id, int item_amount, long price) {
		PosOffers o = SHOPS_ARRAYLIST.get(index);
		if (o != null) {
			PosOffer offer = o.forId(item_id);
			if (offer != null) {
				offer.increaseAmount(item_amount);
			} else {
				if (o.addOffer(new PosOffer(item_id, item_amount, 0, price)))
					player.getPacketSender()
							.sendMessage("You have successfully placed your <col=CA024B>"
									+ ItemDefinition.forId(item_id).getName() + "</col> for sale for <col=CA024B>"
									+ formatAmount(price) + "</col>");
				else
					player.getPacketSender().sendMessage("Shop full!");
			}
		} else {
			System.out.println("Error: Shop null");
		}
	}

	public static int getIndex(String name) {
		return SHOPS_TO_SEARCH.indexOf(name.toLowerCase());
	}

	public static int getFreeIndex() {
		for (int i = 0; i < SHOPS_ARRAYLIST.size(); i++) {
			PosOffers p = SHOPS_ARRAYLIST.get(i);
			if (p == null) {
				return i;
			}
		}
		return -1;
	}

	public static void openShop(String username, Player player) {
		if (player.getGameModeAssistant().isIronMan()) {
			player.getPacketSender().sendMessage("Ironmen can't use the player owned shops!");
			return;
		}
		int[] stock = new int[40];
		int[] stockAmount = new int[40];
		for (int i = 0; i < stock.length; i++) {
			stock[i] = -1;
			stockAmount[i] = 1;
		}
		int i = getIndex(username.toLowerCase());
		if (i >= 0) {
			String name = SHOPS_TO_SEARCH.get(i);
			if (name.equals(username.toLowerCase())) {
				player.setPlayerOwnedShopping(true);
				// player.getPacketSender().sendString(3903, "Shop caption");
				PlayerOwnedShopManager.getShops().get(i).open(player, username.toLowerCase(), i);
				PlayerLogs.other(player, "Opened the player owned shop: " + username + "");
				if (i == SHOPS_ARRAYLIST.size())
					player.getPacketSender().sendMessage("This shop does not exist!");
			}
		} else {
			if (player.getUsername().equalsIgnoreCase(username)) {
				PosOffer[] offers = new PosOffer[40];
				SHOPS_ARRAYLIST.add(new PosOffers(player.getUsername(), player.getUsername() + "'s store",
						offers.length, 0, offers));
				SHOPS_TO_SEARCH.add(player.getUsername().toLowerCase());
				Item[] default_items = new Item[0];
				PlayerOwnedShopManager.getShops().put(SHOPS_ARRAYLIST.size() - 1,
						new PlayerOwnedShopContainer(null, player.getUsername(), default_items));
				player.getPacketSender().sendString(41900, "");
				openShop(player.getUsername(), player);
			} else {
				player.getPacketSender().sendMessage("This shop does not exist!");
			}
		}
	}

	public static void collectCoinsOnLogin(Player player) {
		for (PosOffers o : SHOPS_ARRAYLIST) {
			if (o == null)
				continue;
			if (o.getOwner().toLowerCase().equals(player.getUsername().toLowerCase())) {
				if(o.getCoinsToCollect() == 0) {
					return;
				}
				if (o.getCoinsToCollect() >= 1 && player.getBankPinAttributes().hasEnteredBankPin()) {
					player.setMoneyInPouch((player.getMoneyInPouch() + (o.getCoinsToCollect())));
					player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
					player.getPacketSender().sendString(1, ":moneypouchearning:" + o.getCoinsToCollect());
					player.getPacketSender().sendMessage(
							"Your items have sold for <col=CA024B>" + formatAmount(o.getCoinsToCollect()) + "</col>");
					PlayerLogs.other(player,
							"Player owned shop items sold for: " + formatAmount(o.getCoinsToCollect()) + "");
					o.resetCoinsCollect();
					player.save();
					save();
				} else {
					player.setMoneyInPouch((player.getMoneyInPouch() + (o.getCoinsToCollect())));
					player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
					player.getPacketSender().sendString(1, ":moneypouchearning:" + o.getCoinsToCollect());
					player.getPacketSender()
							.sendMessage("<col=CA024B>Some of your Player Owned Shop items have been sold</col>");
					PlayerLogs.other(player,
							"Player owned shop items sold for: " + formatAmount(o.getCoinsToCollect()) + "");
					o.resetCoinsCollect();
					player.save();
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

	public static void openItemSearch(Player player, boolean wipe) {
		if (wipe) {
			PosItemSearch.reset(player);
			player.getPacketSender().sendString(1, "[WIPEPOS]");
		}
		player.getPacketSender().sendInterface(41409);
		displayFeaturedShops(player);
	}

}