package com.runelive.model;

import com.runelive.world.entity.impl.player.Player;

public enum ExpRates {
	DEFAULT(250),
	SIR(125),
	LORD(80),
	LEGEND(35),
	EXTREME(15),
	REALISM(5);

	private int multiplier;

	//We should set this up as multiplier, drop rate, prayer drain rate, spec recovery rate
	ExpRates(int multiplier) {
		this.multiplier = multiplier;
	}

	public int getMultiplier() {
		return multiplier;
	}

	public static void set(Player player, ExpRates expRate) {
		player.setExpRate(expRate);
		//player.getPacketSender().sendIronmanMode(expRate.ordinal());
	}


	/**
	 * Gets the mode for a certain id.
	 *
	 * @param id The id (ordinal()) of the mode.
	 * @return gamemode.
	 */
	public static ExpRates forId(int id) {
		for (ExpRates exp : ExpRates.values()) {
			if (exp.ordinal() == id) {
				return exp;
			}
		}
		return null;
	}

}
