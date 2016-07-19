package com.runelive.model.player;

import com.runelive.util.Misc;
import com.runelive.world.content.PlayerPanel;
import com.runelive.world.content.dialogue.DialogueManager;
import com.runelive.world.entity.impl.player.Player;

public enum GameMode {

	SIR(125, 5, 1, 20),
	LORD(80, 8, .75, 15),
	LEGEND(35, 10, .60 , 10),
	EXTREME(15, 12, .50, 8),
	REALISM(5, 15, .40, 5),
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

	public static void set(Player player, GameMode mode) {
		if (!player.getClickDelay().elapsed(1000))
			return;
		player.getClickDelay().reset();
		player.getPacketSender().sendInterfaceRemoval();
		player.getGameModeAssistant().setGameMode(mode);
		PlayerPanel.refreshPanel(player);
		player.getPacketSender().sendIronmanMode();
		player.getPacketSender().sendMessage("").sendMessage("You've set your gamemode to " + mode.name().toLowerCase().replaceAll("_", " ") + ".");
		player.getPacketSender().sendMessage("If you wish to change it, please talk to the town crier in Edgeville.");
		player.setPlayerLocked(true);
		DialogueManager.start(player, 253);
		player.setDialogueActionId(253);
	}

}
