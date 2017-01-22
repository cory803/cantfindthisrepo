package com.runelive.threading.event;

import com.runelive.GameServer;
import com.runelive.GameSettings;
import com.runelive.net.serverlogs.ServerLogs;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 1/21/2017.
 *
 * @author Seba
 */
public class ServerLogEvent extends Event {

    /**
     * How often should we send the pending queries to the {@link com.runelive.loginserver.LoginServer}.
     */
    private static final int EXECUTE_TIME = 30 * 1000; // 30 Seconds

    public ServerLogEvent() {
        super(EXECUTE_TIME);
    }

    @Override
    public void execute() {
        //If we are not logging the server logs then stop this event.
        if (!GameSettings.DATABASE_LOGGING) {
            this.stop();
        }
        try {
            String query;

            StringBuilder builder = new StringBuilder();

            //Starting building our single lined query to send to the loginserver.
            while ((query = ServerLogs.pendingLogs.poll()) != null) {
                builder.append(query);
            }

            //Check to see if we actually have any pending queries.
            if (builder.toString().length() > 0) {
                GameServer.getLoginServer().getPacketCreator().submitServerLogs(builder.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
