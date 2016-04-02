package com.ikov.world.content.pos;

import java.io.DataOutputStream;
import java.io.IOException;

import com.ikov.model.Item;

public class PosOffers {

	private String owner_name;
	private String caption;
	private int index;
	private int amount_in_shop = 0;
	private int coins_to_collect;
	private int[][] sell_offers = new int[40][4];
	private long[] price = new long[40];
	
	public PosOffers(String owner_name, String caption, int index, int amount_in_shop, int coins_to_collect, int[][] sell_offers, long[] price) {
		this.owner_name = owner_name;
		this.caption = caption;
		this.index = index;
		this.amount_in_shop = amount_in_shop;
		this.coins_to_collect = coins_to_collect;
		this.sell_offers = sell_offers;
		this.price = price;
	}	
	
	public String getOwner() {
		return owner_name;
	}	
	
	public String getCaption() {
		return caption;
	}
	
	public int getIndex() {
		return index;
	}	
	
	public int getAmountInShop() {
		return amount_in_shop;
	}

	public int getCoinsToCollect() {
		return coins_to_collect;
	}
	
	public int[][] getSellOffers() {
		return sell_offers;
	}	
	
	public long[] getPrice() {
		return price;
	}
	
	public void addToSellOffers(int item_id, int amount, long price) {
		this.sell_offers[getAmountInShop()][0] = item_id;
		this.sell_offers[getAmountInShop()][1] = amount;
		this.price[getAmountInShop()] = price; //Price complete at a later date
		this.amount_in_shop++;
		
	}
	
	public void save(DataOutputStream out) throws IOException {
		out.writeUTF(getOwner());
		out.writeUTF(getCaption());
		out.writeInt(getIndex());
		out.writeInt(getAmountInShop());
		out.writeInt(getCoinsToCollect());
		for(int i2 = 0; i2 < getAmountInShop(); i2++) {
			out.writeInt(getSellOffers()[i2][0]);
			out.writeInt(getSellOffers()[i2][1]);
			out.writeInt(getSellOffers()[i2][2]);
			out.writeLong(getPrice()[i2]);
		}
	}
	
}
