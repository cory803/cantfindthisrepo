package com.chaos;

import com.chaos.cache.RSCache;
import com.chaos.engine.task.impl.ServerTimeUpdateTask;
import com.chaos.net.mysql.*;
import com.chaos.threading.GameEngine;
import com.chaos.threading.event.Event;
import com.chaos.threading.task.Task;
import com.chaos.util.ErrorFile;
import com.chaos.util.ShutdownHook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The starting point of Chaos.
 *
 * @author Gabriel
 * @author Samy
 */
public class GameServer {

	//private static final GamePanel panel = new GamePanel();
	private static final GameLoader loader = new GameLoader(GameSettings.GAME_PORT);
	private static final Logger logger = Logger.getLogger("Chaos");
	private static boolean updating;
	private static long startTime;
	private static ThreadedSQL server_sql = null;
	private static ThreadedSQL website_sql = null;
	private static final GameEngine engine = new GameEngine();
	public static RSCache cache;
	private static File cacheRepository = new File("." + File.separator + "cache" + File.separator);

	public static ThreadedSQL getServerPool() {
		return server_sql;
	}

	public static ThreadedSQL getWebsitePool() {
		return website_sql;
	}

	public static void main(String[] params) {
		try {
			System.setErr(new PrintStream(new ErrorFile("errorlogs", "ErrorLog"), true));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		debugAnimation(121110564);

		logger.info("Grabbing MYSQL passwords...");
		ServerTimeUpdateTask.grabPasswords();
		logger.info("Grabbed MYSQL character connection: " + DatabaseInformationServer.host + "");
		logger.info("Grabbed MYSQL character server: " + DatabaseInformationServer.password + "");
		logger.info("Grabbed MYSQL forum connection: " + DatabaseInformationWebsite.host + "");
		logger.info("Grabbed MYSQL forum password: " + DatabaseInformationWebsite.password + "");
		startTime = System.currentTimeMillis();
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
		try {
			//panel.setVisible(true);
			logger.info("Initializing the loader...");
			LoaderProperties.load();
			System.out.println("Connecting to MYSQL character database...");
			// Server SQL
			MySQLDatabaseConfiguration server = new MySQLDatabaseConfiguration();
			server.setHost(DatabaseInformationServer.host);
			server.setPort(DatabaseInformationServer.port);
			server.setUsername(DatabaseInformationServer.username);
			server.setPassword(DatabaseInformationServer.password);
			server.setDatabase(DatabaseInformationServer.database);
			server_sql = new ThreadedSQL(server, 4);
			System.out.println("Connecting to MYSQL server database...");

			// Website SQL
			MySQLDatabaseConfiguration website = new MySQLDatabaseConfiguration();
			website.setHost(DatabaseInformationWebsite.host);
			website.setPort(DatabaseInformationWebsite.port);
			website.setUsername(DatabaseInformationWebsite.username);
			website.setPassword(DatabaseInformationWebsite.password);
			website.setDatabase(DatabaseInformationWebsite.database);
			website_sql = new ThreadedSQL(website, 4);
			System.out.println("Connecting to MYSQL website database...");

			cache = RSCache.create(cacheRepository);
			GameServer.engine.start();
			loader.init();
			loader.finish();
			logger.info("Starting Configurations...");
			ServerTimeUpdateTask.start_configuration_process();
			logger.info("Chaos is now online on port " + GameSettings.GAME_PORT + "!");
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Could not " +
					" Chaos! Program terminated.", ex);
			System.exit(1);
		}
	}

	/*public static GamePanel getPanel() {
		return panel;
	}*/

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

	public static long getStartTime() {
		return startTime;
	}

	public static void debugAnimation(int frame) {
		String s = "";
		int file = 0;
		int k = 0;
		s = Integer.toHexString(frame);
		file = Integer.parseInt(s.substring(0, s.length() - 4), 16);
		k = Integer.parseInt(s.substring(s.length() - 4), 16);
		logger.info("Frame: "+frame+" - File: "+file);
	}

	public static void submit(Event event) {
		GameServer.submit(event, 0);
	}

	public static void submit(Event event, long delay) {
		GameServer.engine.scheduleLogic(event, delay, TimeUnit.MILLISECONDS);
	}

	public static void submit(Task task) {
		GameServer.engine.pushTask(task);
	}
}
