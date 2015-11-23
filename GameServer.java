package com.strattus;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.strattus.world.entity.impl.player.*;
import com.strattus.util.RSAKeyGenerator;
import com.strattus.util.ShutdownHook;

/**
 * The starting point of strattus.
 * @author Gabriel
 * @author Samy
 */
public class GameServer {

	private static final GameLoader loader = new GameLoader(GameSettings.GAME_PORT);
	private static final Logger logger = Logger.getLogger("Strattus");
	private static boolean updating;

	public static void main(String[] params) {
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
		try {

			logger.info("Initializing the loader...");
			loader.init();
			loader.finish();
			logger.info("The loader has finished loading utility tasks.");
			logger.info("Strattus is now online on port "+GameSettings.GAME_PORT+"!");

		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Could not start Strattus! Program terminated.", ex);
			System.exit(1);
		}
	}

	public static GameLoader getLoader() {
		return loader;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setUpdating(boolean updating) {
		GameServer.updating = updating;
	}

	public static boolean isUpdating() {
		return GameServer.updating;
	}
}