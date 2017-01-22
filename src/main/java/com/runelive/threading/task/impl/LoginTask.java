package com.runelive.threading.task.impl;

import com.runelive.loginserver.LoginProcessor;
import com.runelive.model.DonatorRights;
import com.runelive.model.StaffRights;
import com.runelive.model.player.PlayerDetails;
import com.runelive.net.login.LoginResponses;
import com.runelive.net.packet.PacketBuilder;
import com.runelive.threading.task.Task;
import com.runelive.world.entity.impl.player.Player;
import com.runelive.world.entity.impl.player.PlayerLoading;
import org.jboss.netty.channel.Channel;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 1/15/2017.
 *
 * @author Seba
 */
public class LoginTask extends Task {

    private final PlayerDetails details;
    private final long hash;
    private final int response;
    private final int staffRights;
    private final int donorRights;
    private final String gameSave;

    public LoginTask(PlayerDetails details, long hash, int response, int staffRights, int donorRights, String gameSave) {
        this.details = details;
        this.hash = hash;
        this.response = response;
        this.staffRights = staffRights;
        this.donorRights = donorRights;
        this.gameSave = gameSave;
    }

    @Override
    public void execute(com.runelive.threading.GameEngine context) {
        context.submitWork(new MyLoginTask(details, hash, response, staffRights, donorRights, gameSave));
    }

    private static final class MyLoginTask implements Runnable {

        private final PlayerDetails details;
        private final long hash;
        private final int response;
        private final int staffRights;
        private final int donorRights;
        private final String gameSave;
        private Player player;

        public MyLoginTask(PlayerDetails details, long hash, int response, int staffRights, int donorRights, String gameSave) {
            this.details = details;
            this.hash = hash;
            this.response = response;
            this.staffRights = staffRights;
            this.donorRights = donorRights;
            this.gameSave = gameSave;
        }

        @Override
        public void run() {
            try {
                int loginResponse = this.translateReponse();
                player = details.getPlayer();
                player.setResponse(loginResponse);
                player.setResponse(LoginResponses.getResponse(player, details));
                player.setStaffRights(StaffRights.forId(staffRights));
                player.setDonatorRights(DonatorRights.forId(donorRights));

                final boolean newAccount = gameSave == null;
                if (newAccount) {
                    player.setNewPlayer(true);
                    player.setPlayerLocked(true);
                    player.setResponse(LoginResponses.LOGIN_SUCCESSFUL);
                }

                if (player.getResponse() == LoginResponses.LOGIN_SUCCESSFUL) {
                    if (!newAccount) {
                        PlayerLoading.decodeJson(player, gameSave);
                    }

                    int rank = player.getCrown();
                    /**
                     * Successful login.
                     */
                    details.getChannel().write(new PacketBuilder().put((byte) 2).put((byte) rank).put((byte) 0).toPacket());
                    /**
                     * Initialize the player in game.
                     */
                    LoginProcessor.getSuccessfulLogins().put(player.getLongUsername(), player);
                    // PlayerHandler.handleLogin(player);
                } else {
                    this.sendReturnCode(details.getChannel(), player.getResponse());
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                LoginProcessor.remove(hash);
            }
        }

        private int translateReponse() {
            switch (response) {
                case 0:
                case 1:
                    return LoginResponses.LOGIN_SUCCESSFUL;
            }

            return LoginResponses.INVALID_CREDENTIALS;
        }

        private void sendReturnCode(final Channel channel, final int code) {
            channel.write(new PacketBuilder().put((byte) code).toPacket())
                    .addListener(listener -> listener.getChannel().close());
        }
    }
}
