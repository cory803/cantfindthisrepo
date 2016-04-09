package com.ikov.commands;

import java.util.HashMap;

import com.ikov.GameSettings;
import com.ikov.commands.impl.ChangePasswordCommand;
import com.ikov.commands.ranks.StaffCommands;
import com.ikov.model.PlayerRights;
import com.ikov.world.World;
import com.ikov.world.content.PlayerLogs;
import com.ikov.world.content.PlayerPunishment;
import com.ikov.world.entity.impl.player.Player;

public class CommandHandler {
	
	/**
	 * Map containing all the commands.
	 */
	private static HashMap<String, Command> commands = new HashMap<String, Command>();
	
	/**
	 * Adds a new command into the map.
	 * @param command
	 */
	public static void submit(Command...cmds) {
		for (Command cmd : cmds) {
			commands.put(cmd.getName(), cmd);
		}
	}
	
	/**
	 * Checks if the command was processed.
	 * @param player
	 * @param key
	 * @param input
	 * @return
	 */
	public static boolean processed(Player player, String key, String input) {
		Command command = commands.get(key);
		if (command != null) {
			if (command instanceof StaffCommand) {
				if (!player.getRights().isStaff()) {
					player.getPacketSender().sendMessage("This is a staff only command.");
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
				if (command.execute(player, input)) {
					PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " has done command " + key + " " + input);
				}
			} catch (Exception e) {
				e.printStackTrace();
				player.getPacketSender().sendMessage("There was an error processing the command.");
			}
			return true;
		}
		return false;
	}
	
	static {
		StaffCommands.init();
		submit(new ChangePasswordCommand("changepass"), new ChangePasswordCommand("changepassword"));
	}

}
