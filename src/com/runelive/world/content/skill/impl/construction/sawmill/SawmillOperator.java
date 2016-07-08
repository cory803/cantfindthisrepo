package com.runelive.world.content.skill.impl.construction.sawmill;

import com.runelive.util.Misc;
import com.runelive.world.content.Emotes.Skillcape_Data;
import com.runelive.world.content.dialogue.DialogueManager;
import com.runelive.world.entity.impl.player.Player;

public class SawmillOperator {

	/**
	 * Exchanges logs for planks
	 * 
	 * @param player
	 * @param plank
	 * @param amount
	 */
	public static void Exchange(Player player, Plank plank, int amount) {
		int logs = player.getInventory().getAmount(plank.getLogId());
		if (amount < logs)
			logs = amount;
		int toPay = logs * plank.getCost();

		player.getPacketSender().sendInterfaceRemoval();

		if (player.getInventory().getAmount(995) < toPay && player.getMoneyInPouch() < toPay) {
			DialogueManager.start(player, 184);
			return;
		}

		if (Skillcape_Data.CONSTRUCTION.isWearingCape(player)
				|| Skillcape_Data.MASTER_CONSTRUCTION.isWearingCape(player) && Misc.exclusiveRandom(0, 10) == 1) {
			player.getPacketSender()
					.sendMessage("The sawmill operator recognises your cape and gives you the planks for free!");
		} else {
			if (player.getMoneyInPouch() >= toPay) {
				player.setMoneyInPouch(player.getMoneyInPouch() - toPay);
				player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch() + "");
			} else
				player.getInventory().delete(995, toPay);
		}

		player.getInventory().delete(plank.getLogId(), logs).add(plank.getPlankId(), logs);

		player.getPacketSender().sendMessage("You receive your planks.");
	}

	public static boolean handleButtonClick(int id, Player player) {

		return false;
	}

}
