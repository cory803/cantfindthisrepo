package com.ikov.commands.impl;

import com.ikov.commands.Command;
import com.ikov.model.PlayerRights;
import com.ikov.model.input.impl.ChangePassword;
import com.ikov.world.entity.impl.player.Player;

public class ChangePasswordCommand extends Command {

	public ChangePasswordCommand(String name) {
		super(name, PlayerRights.PLAYER);
	}

	@Override
	public boolean execute(Player player, String key, String input) throws Exception {
		player.setInputHandling(new ChangePassword());
		player.getPacketSender().sendEnterInputPrompt("Enter a new password:");
		return true;
	}

	
	
}
