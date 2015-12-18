package com.ikov;

import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.lang.Runtime;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ikov.world.entity.impl.player.*;
import com.ikov.util.RSAKeyGenerator;
import com.ikov.util.ShutdownHook;

/**
 * The starting point of strattus.
 * @author Gabriel
 * @author Samy
 */
public class GameServer {

	private static final GameLoader loader = new GameLoader(GameSettings.GAME_PORT);
	private static final Logger logger = Logger.getLogger("IKov");
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
				e.printStackTrace();
			}
			loader.init();
			loader.finish();
			logger.info("The loader has finished loading utility tasks.");
			logger.info("IKov is now online on port "+GameSettings.GAME_PORT+"!");
			final BufferedReader reader = new BufferedReader(new FileReader(new File("data/mysql_connection.txt")));
			if(reader.readLine().toLowerCase().equals("on")) {
				GameSettings.MYSQL_ENABLED = true;
				System.out.println("MYSQL Connections are enabled!");
			} else {
				GameSettings.MYSQL_ENABLED = false;
				System.out.println("MYSQL Connections are disabled.");
			}
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Could not start IKov! Program terminated.", ex);
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