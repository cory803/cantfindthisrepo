package com.ikov.commands;

import com.ikov.world.entity.impl.player.Player;

/**
 * Represents a command which is available to every staff member.
 * @author Blake
 *
 */
public abstract class StaffCommand extends Command {

	public StaffCommand(String name) {
		super(name);
	}

	@Override
	public abstract boolean execute(Player player, String input) throws Exception;

}
