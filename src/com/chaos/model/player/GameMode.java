package com.chaos.model.player;

import com.chaos.util.Misc;

public enum GameMode {

	/**
	 * DO NOT TOUCH THIS OR YOU WILL TRASH THE HISCORES!!!!!!!!!!!!!!
	 */
	SIR(125, 5, 1, 20, 0),
	LORD(80, 8, .75, 15, 0),
	LEGEND(35, 10, .60 , 10, 0),
	EXTREME(15, 12, .50, 8, 0),
	REALISM(5, 15, .40, 5, 0),
	_IRONMAN(25, 12, .55, 8, 12),
	IRONMAN(220, 4, 1, 20, 12),
	HARDCORE_IRONMAN(25, 12, .55, 8, 12);

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
