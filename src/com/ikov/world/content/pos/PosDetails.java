package com.ikov.world.content.pos;

/**
 * Represents the details of a players pos.
 * @author Blake
 */
public class PosDetails {
	
	private String owner;
	private String caption;
	
	public PosDetails(String owner, String caption) {
		this.owner = owner;
		this.caption = caption;
	}

	/**
	 * Gets the caption.
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * Gets the owner.
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

}
