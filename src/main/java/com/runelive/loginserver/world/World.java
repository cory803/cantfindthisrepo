package com.runelive.loginserver.world;

public final class World {
	private final int node;
	private final WorldPlayer[] players = new WorldPlayer[1000];

	public World(int node) {
		this.node = node;
	}

	public int getNode() {
		return node;
	}

	public int getClientNode() {
		return node + 9;
	}

	public void clearPlayers() {
		for (int i = 0; i < 1000; i++) {
			players[i] = null;
		}
	}

	public WorldPlayer getPlayer(long encodedName) {
		for (WorldPlayer worldPlayer : players) {
			if (worldPlayer == null) {
				continue;
			}
			if (worldPlayer.getEncodedName() != encodedName) {
				continue;
			}
			return worldPlayer;
		}
		return null;
	}


	public boolean add(long encodedName) {
		for (int i = 0; i < 1000; i++) {
            if (players[i] != null) {
                if(players[i].getEncodedName() == encodedName){
                	//Server.getLogger().log(Level.INFO, "Double encoded name when adding to world");
                    return false;
                }
				continue;
			}
			players[i] = new WorldPlayer(encodedName);
			return true;
		}
		return false;
	}

	public boolean contains(long encodedName) {
		for (WorldPlayer worldPlayer : players) {
			if (worldPlayer == null) {
				continue;
			}
			if (worldPlayer.getEncodedName() != encodedName) {
				continue;
			}
			return true;
		}
		return false;
	}

	public boolean remove(long encodedName) {
		for (int i = 0; i < 1000; i++) {
			if (players[i] == null) {
              continue;
			}
			if (players[i].getEncodedName() != encodedName) {
				continue;
			}
			players[i] = null;
			return true;
		}
		return false;
	}
}
