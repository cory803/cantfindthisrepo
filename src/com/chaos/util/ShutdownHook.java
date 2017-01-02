package com.chaos.util;

import java.io.IOException;
import java.util.logging.Logger;

import com.chaos.GameServer;
import com.chaos.world.World;
import com.chaos.world.content.Scoreboard;
import com.chaos.world.content.pos.PlayerOwnedShops;
import com.chaos.world.content.wells.WellOfGoodness;
import com.chaos.world.content.clan.ClanChatManager;
import com.chaos.world.entity.impl.player.Player;
import com.chaos.world.entity.impl.player.PlayerHandler;

public class ShutdownHook extends Thread {

	/**
	 * The ShutdownHook logger to print out information.
	 */
	private static final Logger logger = Logger.getLogger(ShutdownHook.class.getName());

	@Override
	public void run() {
		logger.info("The shutdown hook is processing all required actions...");
		World.savePlayers();
		GameServer.setUpdating(true);
		for (Player player : World.getPlayers()) {
			if (player != null) {
				PlayerHandler.handleLogout(player);
			}
		}
		try {
			Scoreboard.save();
		} catch (IOException e) {

		}
		WellOfGoodness.save();
		ClanChatManager.save();
		PlayerOwnedShops.save();
		logger.info("The shudown hook actions have been completed, shutting the server down...");
	}
}
