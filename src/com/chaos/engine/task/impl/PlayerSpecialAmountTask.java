package com.chaos.engine.task.impl;

import com.chaos.engine.task.Task;
import com.chaos.world.content.combat.weapon.CombatSpecial;
import com.chaos.world.entity.impl.player.Player;

public class PlayerSpecialAmountTask extends Task {

	public PlayerSpecialAmountTask(Player player) {
		super(player.getGameModeAssistant().getSpecialRecoveryRate(), player, false);
		this.player = player;
		player.setRecoveringSpecialAttack(true);
	}

	private final Player player;

	@Override
	public void execute() {
		if (player == null || !player.isRegistered() || player.getSpecialPercentage() >= 100
				|| !player.isRecoveringSpecialAttack()) {
			player.setRecoveringSpecialAttack(false);
			stop();
			return;
		}
		int amount = player.getSpecialPercentage() + 5;
		if (amount >= 100) {
			amount = 100;
			player.setRecoveringSpecialAttack(false);
			stop();
		}
		player.setSpecialPercentage(amount);
		CombatSpecial.updateBar(player);
		if (player.getSpecialPercentage() % 50 == 0)
			player.getPacketSender()
					.sendMessage("Your special attack energy is now " + player.getSpecialPercentage() + "%.");
	}
}
