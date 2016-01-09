package com.ikov.world.content.minigames.impl;

import com.ikov.world.entity.impl.player.Player;

public class ClanWars {
	public enum Rules {
		//combat rules
		COMBAT_MELEE(false),
		COMBAT_MAGE(false),
		COMBAT_RANGE(false),
		COMBAT_PRAYER(false),
		COMBAT_SUMMONING(false),
		COMBAT_FOOD(false),
		COMBAT_POTS(false),
		
		//time limit
		NO_LIMIT(1000),
		FIVE_MINUTES(5),
		TEN_MINUTES(10),
		THIRTY_MINUTES(30),
		
		//kills
		FIRST_25(25, false),
		FIRST_50(50, false),
		
		//Arenas
		CLASSIC(3333, 3333, 0),
		PLATEAU(3333, 3333, 0),
		FOREST(3333, 3333, 0),
		TURRETS(3333, 3333, 0);
		
		
		private int x,y,z;
		private Rules(int a, int b, int c) {
			x=a;
			y=b;
			z=c;
		}
		public int getX() {
			return x;
		}
		public int getY() {
			return y;
		}
		public int getZ() {
			return z;
		}
		
		private int kills;
		private boolean respawns;
		private Rules(int kill, boolean res) {
			kills = kill;
			respawns = res;
		}
		public int getKills() {
			return kills;
		}
		public boolean getRespawn() {
			return respawns;
		}
		
		private int time;
		private Rules(int t) {
			time = t;
		}
		public int getTime() {
			return time;
		}
		
		private boolean toggle;
		
		private Rules(boolean tog) {
			toggle = tog;
		}
		public boolean getToggle() {
			return toggle;
		}
		
	}
	/* Rules */
	void checkRules(Player player) {
		if(Rules.COMBAT_FOOD.getToggle() == true) {
			//player can eat
		} else {
			//player cannot eat
		}
		
	}
	
}
