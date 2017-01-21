package com.runelive.net.serverlogs;

import com.runelive.GameServer;
import com.runelive.GameSettings;
import com.runelive.net.serverlogs.impl.ServerLog;

import java.util.LinkedList;
import java.util.Queue;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 1/20/2017.
 *
 * @author Seba
 */
public class ServerLogs {

    /**
     * This is the queue for all pending server logs.
     */
    public static Queue<String> pendingLogs = new LinkedList<>();

    /**
     * A simple way to execute any given {@link ServerLog}
     *
     * @param serverLog
     */
    public static void submit(ServerLog serverLog) {
        //Check to make sure the server log is not null
        if (serverLog == null) {
            return;
        }
        //If we are not saving logs then return
        if (!GameSettings.DATABASE_LOGGING) {
            return;
        }
        pendingLogs.add(serverLog.createQuery());
    }
}
