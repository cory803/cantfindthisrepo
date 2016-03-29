package com.ikov;

import java.net.*;
import java.io.*;
import java.lang.Runtime;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.rspserver.mvh.*;

import com.ikov.engine.task.impl.ServerTimeUpdateTask;
import com.ikov.util.ShutdownHook;

/**
 * The starting point of strattus.
 * @author Gabriel
 * @author Samy
 */
public class GameServer {

	private static final GameLoader loader = new GameLoader(GameSettings.GAME_PORT);
	private static final Logger logger = Logger.getLogger("Ikov");
	private static boolean updating;

	public static void main(String[] params) {
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
		try {

			logger.info("Initializing the loader...");
			System.out.println("Fetching client version...");
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://dl.dropboxusercontent.com/u/344464529/IKov/update.txt").openStream()));
				for (int i = 0; i < 1; i++) {
					GameSettings.client_version = reader.readLine();
				}
				System.out.println("Client version has been set to: "+GameSettings.client_version+"");
			} catch (Exception e) {
				//GameSettings.client_version = "invalid_connection";
				e.printStackTrace();
			}
			loader.init();
			loader.finish();
			logger.info("Starting configuration settings...");
			ServerTimeUpdateTask.start_configuration_process();
			logger.info("Starting voting...");
			AuthService.setProvider(new Motivote("qypFds9A0q2TAccrVXeAJvA8SGusjyxm"));
			logger.info("Finished starting configuration settings...");
			logger.info("The loader has finished loading utility tasks.");
			logger.info("Ikov is now online on port "+GameSettings.GAME_PORT+"!");
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Could not start Ikov! Program terminated.", ex);
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