package com.ikov.commands;

import java.util.HashMap;

import com.ikov.commands.impl.ChangePasswordCommand;
import com.ikov.commands.impl.YellCommand;
import com.ikov.commands.ranks.AdministratorCommands;
import com.ikov.commands.ranks.DonatorCommands;
import com.ikov.commands.ranks.ModeratorCommands;
import com.ikov.commands.ranks.StaffCommands;
import com.ikov.model.PlayerRights;
import com.ikov.world.content.PlayerLogs;
import com.ikov.world.entity.impl.player.Player;

public class CommandHandler {

	/**
	 * Map containing all the commands.
	 */
	private static HashMap<String, Command> commands = new HashMap<String, Command>();

	/**
	 * Adds a new command into the map.
	 * 
	 * @param command
	 */
	public static void submit(Command... cmds) {
		for (Command cmd : cmds) {
			commands.put(cmd.getName(), cmd);
		}
	}

	/**
	 * Checks if the command was processed.
	 * 
	 * @param player
	 * @param key
	 * @param input
	 * @return
	 */
	public static boolean processed(Player player, String key, String input) {
		Command command = commands.get(key.toLowerCase());
		if (command != null) {
			if (command instanceof StaffCommand) {
				if (!player.getRights().isStaff() && !player.isSpecialPlayer()) {
					player.getPacketSender().sendMessage("This is a staff only command.");
					return false;
				} else {
					if (!command.meetsRequirements(player)) {
						player.getPacketSender().sendMessage("You don't meet the requirements of this command.");
						return false;
					}
				}
			} else if (command instanceof ModeratorCommand) {
				if (!player.getRights().inherits(PlayerRights.MODERATOR) && !player.isSpecialPlayer()) {
					player.getPacketSender().sendMessage("This command requires moderator rights.");
					return false;
				}
			} else if (command instanceof DonatorCommand) {
				if (player.getDonorRights() < ((DonatorCommand) command).getDonorRights()) {
					player.getPacketSender().sendMessage("This command requires " + player.getDonorRight().toLowerCase() + " rights.");
					return false;
				}
			} else {
				PlayerRights rights = command.getRights();
				if (!player.getRights().inherits(rights)) {
					player.getPacketSender().sendMessage("This command requires " + command.getRights().toString().toLowerCase() + " rights.");
					return false;
				}
			}
			try {
				if (command.execute(player, key, input)) {
					PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " has done command " + key + " " + input);
				}
			} catch (Exception e) {
				player.getPacketSender().sendMessage("There was an error processing the command.");
			}
			return true;
		}
		return false;
	}

	static {
		DonatorCommands.init();
		StaffCommands.init();
		ModeratorCommands.init();
		AdministratorCommands.init();
		submit(new ChangePasswordCommand("changepass"), new ChangePasswordCommand("changepassword"));
		submit(new YellCommand("y"), new YellCommand("yell"));
	}

}
