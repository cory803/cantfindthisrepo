package com.chaos.model.player;

import com.chaos.util.Misc;

public enum GameMode {

	KNIGHT(500, 5, 1, 20, 0),
	REALISM(10, 15, .50, 10, 0),
	IRONMAN(100, 10, .75, 15, 0);

	GameMode(int modeExpRate, int monsterDropRate, double prayerDrainRate, int specialRecoveryRate, int crown) {
		this.modeExpRate = modeExpRate;
		this.monsterDropRate = monsterDropRate;
		this.prayerDrainRate = prayerDrainRate;
		this.specialRecoveryRate = specialRecoveryRate;
		this.crown = crown;
	}

	private int modeExpRate;
	private int monsterDropRate;
	private double prayerDrainRate;
	private int specialRecoveryRate;
	private int crown;

	/**
	 * Gets the xp rate for the mode.
	 * @return
	 */
	public int getModeExpRate() {
		return modeExpRate;
	}

	/**
	 * Gets the drop rate for the mode.
	 * @return
	 */
	public int getMonsterDropRate() {
		return monsterDropRate;
	}

	/**
	 * Gets how faster the players prayer will drain.
	 * @return
	 */
	public double getPrayerDrainRate() {
		return prayerDrainRate;
	}

	/**
	 * Gets how fast the player will recover their special attack.
	 * @return
	 */
	public int getSpecialRecoveryRate() {
		return specialRecoveryRate;
	}

	/**
	 * Returns the formatted name of the gamemode.
	 * @return
	 */
	public String getModeName() {
		return Misc.formatText(this.toString().toLowerCase().replace("_", ""));
	}

	/**
	 * Gets the crown for a certain mode
	 *
	 * @return
	 */
	public int getCrown() {
		return this.crown;
	}

}
