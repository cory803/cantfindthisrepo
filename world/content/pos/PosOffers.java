package com.ikov.world.content.pos;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Represents a group of offers in the pos.
 * @author Blake
 */
public class PosOffers {

	private String owner_name;
	private String caption;
	private int shopItems;
	private int coins_to_collect;
	private PosOffer[] offers = new PosOffer[40];
	
	public PosOffers(String owner, String caption, int shopItems, int coins_to_collect, PosOffer[] offers) {
		this.owner_name = owner;
		this.caption = caption;
		this.shopItems = shopItems;
		this.coins_to_collect = coins_to_collect;
		this.offers = offers;
	}	
	
	public String getOwner() {
		return owner_name;
	}	
	
	public String getCaption() {
		return caption;
	}
	
	public int getShopItems() {
		return shopItems;
	}

	public int getCoinsToCollect() {
		return coins_to_collect;
	}
	
	public PosOffer[] getOffers() {
		return offers;
	}
	
	public void save(DataOutputStream out) throws IOException {
		out.writeUTF(getOwner());
		out.writeUTF(getCaption());
		out.writeInt(getOffers().length);
		out.writeInt(getCoinsToCollect());
		for(int i2 = 0; i2 < getOffers().length; i2++) {
			out.writeInt(getOffers()[i2].getItemId());
			out.writeInt(getOffers()[i2].getAmount());
			out.writeInt(getOffers()[i2].getSoldAmount());
			out.writeLong(getOffers()[i2].getPrice());
		}
	}
	
}
