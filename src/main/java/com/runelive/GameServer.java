package com.runelive;

import com.runelive.cache.RSCache;
import com.runelive.engine.task.impl.ServerTimeUpdateTask;
import com.runelive.loginserver.LoginServer;
import com.runelive.net.mysql.*;
import com.runelive.net.mysql.impl.DatabaseInformationVoting;
import com.runelive.threading.GameEngine;
import com.runelive.threading.event.Event;
import com.runelive.threading.event.ServerLogEvent;
import com.runelive.threading.task.Task;
import com.runelive.util.ErrorFile;
import com.runelive.util.ShutdownHook;
import com.runelive.world.World;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The starting point of RuneLive.
 *
 * @author Gabriel
 * @author Samy
 */
public class GameServer {

	//private static final GamePanel panel = new GamePanel();
	private static final GameLoader loader = new GameLoader(GameSettings.GAME_PORT);
	private static final Logger logger = Logger.getLogger("RuneLive");
	private static boolean updating;
	private static long startTime;
	private static ThreadedSQL server_sql = null;
	private static ThreadedSQL website_sql = null;
	private static ThreadedSQL voting_sql = null;
	private static final GameEngine engine = new GameEngine();
	private static LoginServer loginServer;
	public static RSCache cache;
	private static File cacheRepository = new File("." + File.separator + "cache" + File.separator);

	public static LoginServer getLoginServer() {
		return GameServer.loginServer;
	}

	public static void setLoginServer(LoginServer loginServer) {
		GameServer.loginServer = loginServer;
	}

	public static ThreadedSQL getServerPool() {
		return server_sql;
	}

	public static ThreadedSQL getStorePool() {
		return website_sql;
	}

	public static ThreadedSQL getVotingPool() {
		return voting_sql;
	}

	public static void main(String[] params) {
		try {
			System.setErr(new PrintStream(new ErrorFile("errorlogs", "ErrorLog"), true));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//debugAnimation(121110564);

		logger.info("Grabbing MYSQL passwords...");
		ServerTimeUpdateTask.grabPasswords();
		logger.info("Grabbed MYSQL character connection: " + DatabaseInformationServer.host + "");
		logger.info("Grabbed MYSQL character server: " + DatabaseInformationServer.password + "");
		logger.info("Grabbed MYSQL store connection: " + DatabaseInformationStore.host + "");
		logger.info("Grabbed MYSQL store password: " + DatabaseInformationStore.password + "");
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

			// Store SQL
			MySQLDatabaseConfiguration store = new MySQLDatabaseConfiguration();
			store.setHost(DatabaseInformationStore.host);
			store.setPort(DatabaseInformationStore.port);
			store.setUsername(DatabaseInformationStore.username);
			store.setPassword(DatabaseInformationStore.password);
			store.setDatabase(DatabaseInformationStore.database);
			website_sql = new ThreadedSQL(store, 4);

			// Voting SQL
			MySQLDatabaseConfiguration voting = new MySQLDatabaseConfiguration();
			voting.setHost(DatabaseInformationVoting.host);
			voting.setPort(DatabaseInformationVoting.port);
			voting.setUsername(DatabaseInformationVoting.username);
			voting.setPassword(DatabaseInformationVoting.password);
			voting.setDatabase(DatabaseInformationVoting.database);
			voting_sql = new ThreadedSQL(voting, 4);

			System.out.println("Connecting to MYSQL website database...");

			cache = RSCache.create(cacheRepository);
			GameServer.loginServer = new LoginServer(new String[] {"gameserver.rune.live"}, 43590);
			GameServer.loginServer.start();
			GameServer.engine.start();
			loader.init();
			loader.finish();
			logger.info("Starting Configurations...");
			ServerTimeUpdateTask.start_configuration_process();
			logger.info("RuneLive is now online on port " + GameSettings.GAME_PORT + "!");

			GameServer.submit(new ServerLogEvent());
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Could not " +
					" RuneLive! Program terminated.", ex);
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
