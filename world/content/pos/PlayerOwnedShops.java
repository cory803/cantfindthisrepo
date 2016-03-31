package com.ikov.world.content.pos;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import com.ikov.model.Item;
import com.ikov.model.container.impl.Shop;
import com.ikov.model.input.impl.EnterAmountToBuyFromShop;
import com.ikov.model.input.impl.EnterAmountToSellToShop;
import com.ikov.model.input.impl.PosItemToBuy;
import com.ikov.model.input.impl.PosSearchShop;
import com.ikov.world.content.pos.PosOffers.OfferType;
import com.ikov.world.entity.impl.player.Player;

public class PlayerOwnedShops {
	private static final PosOffers[] OFFERS = new PosOffers[5000];

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
					int id = in.readInt();
					int quantity = in.readInt();
					String listedBy = in.readUTF();
					int arrayIndex = in.readInt();
					int pricePerItem = in.readInt();
					int box = in.readInt();
					int amountFinished = in.readInt();
					int coinsCollect = in.readInt();
					int itemCollect = in.readInt();
					int failedAttempts = in.readInt();
					int updateStateOrdinal = in.readInt();
					OfferType type = OfferType.valueOf(in.readUTF());
					OFFERS[arrayIndex] = new PosOffers(id, quantity, listedBy, arrayIndex, pricePerItem, box, amountFinished, coinsCollect, itemCollect, failedAttempts, type, updateStateOrdinal);
				}
			}

			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public static void posButtons(Player player, int buttonId) {
		switch(buttonId) {
			case -24062:
				
				break;
			case -24074: //Search by Name
				player.setInputHandling(new PosSearchShop());
				player.getPacketSender().sendEnterInputPrompt("Enter the name of a player's shop:");
				break;
			case -24073: //Search by Item
				player.setInputHandling(new PosItemToBuy());
				player.getPacketSender().sendEnterInputPrompt("Enter the name of the item you wish to buy:");
				break;
		}
	}
	public static void openPosShop(final Player player) {
		int[] stock = new int[40];
		int[] stockAmount = new int[40];
		for(int i = 0; i < stock.length; i++) {
			stock[i] = -1;
			stockAmount[i] = 2000000000; // temp will put 2b
		}
		
		Item[] stockItems = new Item[stock.length];
		for(int i = 0; i < stock.length; i++)
			stockItems[i] = new Item(stock[i], stockAmount[i]);
		Shop shop = new Shop(player, Shop.POS, player.getUsername()+"'s shop", new Item(995), stockItems);
		stock = stockAmount = null;
		stockItems = null;
		shop.setPlayer(player);
		player.getPacketSender().sendItemContainer(player.getInventory(), Shop.INVENTORY_INTERFACE_ID);
		player.getPacketSender().sendItemContainer(shop, Shop.ITEM_CHILD_ID);
		player.getPacketSender().sendString(Shop.NAME_INTERFACE_CHILD_ID, player.getUsername()+"'s shop");
		if(player.getInputHandling() == null || !(player.getInputHandling() instanceof EnterAmountToSellToShop || player.getInputHandling() instanceof EnterAmountToBuyFromShop))
			player.getPacketSender().sendInterfaceSet(Shop.INTERFACE_ID, Shop.INVENTORY_INTERFACE_ID - 1);
		player.setShop(shop).setInterfaceId(Shop.INTERFACE_ID).setShopping(true);
	}
}
