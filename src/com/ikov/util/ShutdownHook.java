package com.ikov.util;

import java.util.logging.Logger;

import com.ikov.GameServer;
import com.ikov.world.World;
import com.ikov.world.content.WellOfGoodwill;
import com.ikov.world.content.clan.ClanChatManager;
import com.ikov.world.content.grandexchange.GrandExchangeOffers;
import com.ikov.world.content.pos.PlayerOwnedShops;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.world.entity.impl.player.PlayerHandler;

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
			//	World.deregister(player);
				PlayerHandler.handleLogout(player);
			}
		}
		WellOfGoodwill.save();
		GrandExchangeOffers.save();
		PlayerOwnedShops.save();
		ClanChatManager.save();
		logger.info("The shudown hook actions have been completed, shutting the server down...");
	}
}
