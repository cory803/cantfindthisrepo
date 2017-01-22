package com.runelive.loginserver.world;

public final class WorldPlayer {
	private final long encodedName;
	
	public WorldPlayer(long encodedName) {
		this.encodedName = encodedName;
	}

	public long getEncodedName() {
		return encodedName;
	}
}
