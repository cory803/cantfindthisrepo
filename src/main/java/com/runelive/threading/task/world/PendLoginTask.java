package com.runelive.threading.task.world;

import com.runelive.loginserver.LoginProcessor;
import com.runelive.model.player.PlayerDetails;
import com.runelive.threading.GameEngine;
import com.runelive.threading.task.Task;

public final class PendLoginTask extends Task {
	private final PlayerDetails player;

	public PendLoginTask(PlayerDetails player) {
		this.player = player;
	}

	@Override
	public void execute(GameEngine context) {
		LoginProcessor.pendLogin(player);
	}
}
