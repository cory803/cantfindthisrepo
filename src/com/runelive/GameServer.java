package com.runelive;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.runelive.engine.task.impl.ServerTimeUpdateTask;
import com.runelive.util.ErrorFile;
import com.runelive.util.ShutdownHook;
import com.runelive.net.mysql.ThreadedSQL;
import com.runelive.net.mysql.MySQLDatabaseConfiguration;
import com.runelive.net.mysql.DatabaseInformation;

/**
 * The starting point of RuneLive.
 *
 * @author Gabriel
 * @author Samy
 */
public class GameServer {

    private static final GamePanel panel = new GamePanel();
    private static final GameLoader loader = new GameLoader(GameSettings.GAME_PORT);
    private static final Logger logger = Logger.getLogger("RuneLive");
    private static boolean updating;
    private static long startTime;
    private static ThreadedSQL sql = null;

    public static ThreadedSQL getSQLPool() {
        return sql;
    }
	
    public static void main(String[] params) {
        try {
            System.setErr(new PrintStream(new ErrorFile("errorlogs", "ErrorLog"), true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        startTime = System.currentTimeMillis();
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
        try {
            panel.setVisible(true);
            logger.info("Initializing the loader...");
            System.out.println("Fetching client version...");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        new URL("https://dl.dropboxusercontent.com/u/344464529/IKov/update.txt").openStream()));
                for (int i = 0; i < 1; i++) {
                    GameSettings.client_version = reader.readLine();
                }
                System.out.println("Client version has been set to: " + GameSettings.client_version + "");
            } catch (Exception e) {
                // GameSettings.client_version = "invalid_connection";
                e.printStackTrace();
            }
			MySQLDatabaseConfiguration config = new MySQLDatabaseConfiguration();
			config.setHost(DatabaseInformation.host);
			config.setPort(DatabaseInformation.port);
			config.setUsername(DatabaseInformation.username);
			config.setPassword(DatabaseInformation.password);
			config.setDatabase(DatabaseInformation.database);
			sql = new ThreadedSQL(config, 4);
            loader.init();
            loader.finish();
            logger.info("Starting configuration settings...");
            ServerTimeUpdateTask.start_configuration_process();
            logger.info("Starting voting...");
            logger.info("Finished starting configuration settings...");
            logger.info("The loader has finished loading utility tasks.");
            logger.info("RuneLive is now online on port " + GameSettings.GAME_PORT + "!");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Could not start RuneLive! Program terminated.", ex);
            System.exit(1);
        }
    }

    public static GamePanel getPanel() {
        return panel;
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

    public static long getStartTime() {
        return startTime;
    }
}
