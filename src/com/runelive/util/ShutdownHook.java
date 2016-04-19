package com.runelive.util;

import java.util.logging.Logger;

import com.runelive.GameServer;
import com.runelive.world.World;
import com.runelive.world.content.WellOfGoodwill;
import com.runelive.world.content.clan.ClanChatManager;
import com.runelive.world.content.grandexchange.GrandExchangeOffers;
import com.runelive.world.content.pos.PlayerOwnedShops;
import com.runelive.world.entity.impl.player.Player;
import com.runelive.world.entity.impl.player.PlayerHandler;

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
        // World.deregister(player);
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
