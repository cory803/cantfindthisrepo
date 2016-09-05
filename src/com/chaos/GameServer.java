package com.chaos;

import com.chaos.cache.RSCache;
import com.chaos.ect.dropwriting.Drop;
import com.chaos.ect.dropwriting.DropManager;
import com.chaos.ect.dropwriting.DropTable;
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
	private static ThreadedSQL characters_sql = null;
	private static ThreadedSQL forums_sql = null;
	private static ThreadedSQL voting_sql = null;
	private static ThreadedSQL hiscores_sql = null;
	private static final GameEngine engine = new GameEngine();
	public static RSCache cache;
	private static File cacheRepository = new File("." + File.separator + "cache" + File.separator);

	public static ThreadedSQL getCharacterPool() {
		return characters_sql;
	}

	public static ThreadedSQL getForumPool() {
		return forums_sql;
	}

	public static ThreadedSQL getVotingPool() {
		return voting_sql;
	}

	public static ThreadedSQL getHiscoresPool() {
		return hiscores_sql;
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
		logger.info("Grabbed MYSQL character connection: " + DatabaseInformationCharacters.host + "");
		logger.info("Grabbed MYSQL character password: " + DatabaseInformationCharacters.password + "");
		logger.info("Grabbed MYSQL forum connection: " + DatabaseInformationForums.host + "");
		logger.info("Grabbed MYSQL forum password: " + DatabaseInformationForums.password + "");
		logger.info("Grabbed MYSQL hiscores connection: " + DatabaseInformationHiscores.host + "");
		logger.info("Grabbed MYSQL hiscores password: " + DatabaseInformationHiscores.password + "");
		startTime = System.currentTimeMillis();
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
		try {
			//panel.setVisible(true);
			logger.info("Initializing the loader...");
			LoaderProperties.load();
			System.out.println("Connecting to MYSQL character database...");
			// Characters SQL
			MySQLDatabaseConfiguration characters = new MySQLDatabaseConfiguration();
			characters.setHost(DatabaseInformationCharacters.host);
			characters.setPort(DatabaseInformationCharacters.port);
			characters.setUsername(DatabaseInformationCharacters.username);
			characters.setPassword(DatabaseInformationCharacters.password);
			characters.setDatabase(DatabaseInformationCharacters.database);
			characters_sql = new ThreadedSQL(characters, 4);
			System.out.println("Connecting to MYSQL forum database...");
			// Forum SQL
			MySQLDatabaseConfiguration forums = new MySQLDatabaseConfiguration();
			forums.setHost(DatabaseInformationForums.host);
			forums.setPort(DatabaseInformationForums.port);
			forums.setUsername(DatabaseInformationForums.username);
			forums.setPassword(DatabaseInformationForums.password);
			forums.setDatabase(DatabaseInformationForums.database);
			forums_sql = new ThreadedSQL(forums, 4);
			MySQLDatabaseConfiguration voting = new MySQLDatabaseConfiguration();
			voting.setHost(DatabaseInformationVoting.host);
			voting.setPort(DatabaseInformationVoting.port);
			voting.setUsername(DatabaseInformationVoting.username);
			voting.setPassword(DatabaseInformationVoting.password);
			voting.setDatabase(DatabaseInformationVoting.database);
			voting_sql = new ThreadedSQL(voting, 4);
			MySQLDatabaseConfiguration hiscores = new MySQLDatabaseConfiguration();
			hiscores.setHost(DatabaseInformationHiscores.host);
			hiscores.setPort(DatabaseInformationHiscores.port);
			hiscores.setUsername(DatabaseInformationHiscores.username);
			hiscores.setPassword(DatabaseInformationHiscores.password);
			hiscores.setDatabase(DatabaseInformationHiscores.database);
			hiscores_sql = new ThreadedSQL(hiscores, 4);
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
