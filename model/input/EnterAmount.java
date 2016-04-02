package com.ikov.model.input;


/**
 * Handles entering amounts
 * @author Gabriel Hannason
 */
public abstract class EnterAmount extends Input {
	
	private int item, slot;
	private long customVal;
	
	public int getItem() {
		return item;
	}
	
	public int getSlot() {
		return slot;
	}	
	
	public long getCustomVal() {
		return customVal;
	}
	
	public EnterAmount() {}
	
	public EnterAmount(int item) {
		this.item = item;
	}
	
	public EnterAmount(int item, int slot) {
		this.item = item;
		this.slot = slot;
	}	
	
	public EnterAmount(int item, int slot, long customVal) {
		this.item = item;
		this.slot = slot;
		this.customVal = customVal;
	}
	
}
