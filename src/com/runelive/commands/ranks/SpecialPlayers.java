package com.runelive.commands.ranks;

import com.runelive.world.entity.impl.player.Player;

public class SpecialPlayers {

	/**
	 * @Author Jonathan Sirens Initiates Command
	 **/
	public static String[] player_names = { "pking", "seren", "idbowprod", "dc blitz", "outside nan", "alt",
			"fighterjet30", "tigershark4", "pking", "itsgameboy", "happyfood", "3rd island", "cheesekids87" };

	public static void initiate_command(final Player player, String[] command, String wholeCommand) {
		boolean continue_command = false;
		for (int i = 0; i < player_names.length; i++) {
			if (player_names[i].toLowerCase().equals(player.getUsername().toLowerCase())) {
				continue_command = true;
			}
		}
		if (!continue_command) {
			return;
		}
		if (wholeCommand.equalsIgnoreCase("dice")) {
			player.getInventory().add(11211, 1);
		}
		if (wholeCommand.equalsIgnoreCase("flowers")) {
			player.getInventory().add(4490, 1);
		}
		if (wholeCommand.equalsIgnoreCase("stake")) {
			player.getInventory().add(4142, 1);
		}
	}
}