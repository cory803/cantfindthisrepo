package com.runelive.model;
import com.runelive.world.entity.impl.player.Player;

public enum ExpRates {DEFAULT("Knight", 250), SIR("Sir", 125), LORD("Lord", 80), LEGEND("Legend", 35), EXTREME("Extreme", 15), REALISM("Realism", 15);

	ExpRates(String name, int multiplier) {
		this.name = name;
		this.multiplier = multiplier;
	}

	private String name;
	private int multiplier;
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
