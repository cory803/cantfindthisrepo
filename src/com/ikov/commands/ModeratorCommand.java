package com.ikov.commands;

import com.ikov.world.entity.impl.player.Player;

public abstract class ModeratorCommand extends Command {

	public ModeratorCommand(String name) {
		super(name);
	}

	@Override
	public abstract boolean execute(Player player, String key, String input) throws Exception;

}
