package com.runelive.model.player;

import com.runelive.util.Misc;

public enum GameMode {

	/**
	 * DO NOT TOUCH THIS OR YOU WILL TRASH THE HISCORES!!!!!!!!!!!!!!
	 */
	SIR(125, 5, 1, 20),
	LORD(80, 8, .75, 15),
	LEGEND(35, 10, .60 , 10),
	EXTREME(15, 12, .50, 8),
	REALISM(5, 15, .40, 5),
	_IRONMAN(25, 12, .55, 8),
	IRONMAN(220, 4, 1, 20),
	HARDCORE_IRONMAN(25, 12, .55, 8);

	GameMode(int modeExpRate, int monsterDropRate, double prayerDrainRate, int specialRecoveryRate) {
		this.modeExpRate = modeExpRate;
		this.monsterDropRate = monsterDropRate;
		this.prayerDrainRate = prayerDrainRate;
		this.specialRecoveryRate = specialRecoveryRate;
	}

	private int modeExpRate;
	private int monsterDropRate;
	private double prayerDrainRate;
	private int specialRecoveryRate;

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

}
