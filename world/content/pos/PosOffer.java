package com.ikov.world.content.pos;

/**
 * Represents a single pos offer.
 * @author Blake
 */
public class PosOffer implements Comparable<PosOffer> {

	private int itemId;
	private int amount;
	private int soldAmount;
	private long price;

	public PosOffer(int itemId, int amount, int soldAmount, long price) {
		this.itemId = itemId;
		this.amount= amount;
		this.soldAmount = soldAmount;
		this.price = price;
	}

	/**
	 * Gets the item id.
	 * @return the itemId
	 */
	public int getItemId() {
		return itemId;
	}

	/**
	 * Gets the amount
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}
	
	/**
	 * Gets the sold amount
	 * @return the sold amount
	 */
	public int getSoldAmount() {
		return soldAmount;
	}

	/**
	 * Gets the price
	 * @return the price
	 */
	public long getPrice() {
		return price;
	}

	@Override
	public int compareTo(PosOffer offer) {
		return getPrice() > offer.getPrice() ? 1 : -1;
	}

}
