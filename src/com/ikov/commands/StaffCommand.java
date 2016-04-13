package com.ikov.commands;

import com.ikov.world.entity.impl.player.Player;

/**
 * Represents a command which is only available to a staff member.
 * @author Blake
 *
 */
public abstract class StaffCommand extends Command {

	public StaffCommand(String name) {
		super(name);
	}

	@Override
	public abstract boolean execute(Player player, String key, String input) throws Exception;

}
