package com.ikov.world.content.pos;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a group of offers in the pos.
 * @author Blake
 */
public class PosOffers {

	private String owner_name;
	private String caption;
	private int shopItems;
	private int coins_to_collect;
	private List<PosOffer> offers = new ArrayList<PosOffer>();
	
	public PosOffers(String owner, String caption, int shopItems, int coins_to_collect, PosOffer[] offers) {
		this.owner_name = owner;
		this.caption = caption;
		this.shopItems = shopItems;
		this.coins_to_collect = coins_to_collect;
		for (int i = 0; i<offers.length; i++)
			this.offers.add(i, offers[i]);
	}	
	
	public String getOwner() {
		return owner_name;
	}	
	
	public String getCaption() {
		return caption;
	}
	
	public int getAmountInShop() {
		return shopItems;
	}

	public int getCoinsToCollect() {
		return coins_to_collect;
	}
	
	public List<PosOffer> getOffers() {
		return offers;
	}
	
	public PosOffer forId(int id) {
		for (PosOffer e : offers) {
			if(e == null) {
				continue;
			}
			if (e.getItemId() == id)
				return e;
		}
		return null;
	}
	
	public boolean addOffer(PosOffer o) {
		return offers.add(o);
	}
	
	public boolean removeOffer(PosOffer o) {
		return offers.remove(o);
	}
	
	public void save(DataOutputStream out) throws IOException {
		out.writeUTF(getOwner());
		out.writeUTF(getCaption());
		out.writeInt(getOffers().size());
		out.writeInt(getCoinsToCollect());
		for(int i2 = 0; i2 < getOffers().size(); i2++) {
			if(getOffers().get(i2) == null) {
				out.writeInt(-1);
				out.writeInt(-1);
				out.writeInt(-1);
				out.writeLong(-1);
			} else {
				out.writeInt(getOffers().get(i2).getItemId());
				out.writeInt(getOffers().get(i2).getAmount());
				out.writeInt(getOffers().get(i2).getSoldAmount());
				out.writeLong(getOffers().get(i2).getPrice());
			}
		}
	}
	
}