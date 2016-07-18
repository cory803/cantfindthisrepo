package com.runelive.net.login;

import java.util.concurrent.ConcurrentHashMap;

import org.jboss.netty.channel.Channel;

import com.runelive.GameSettings;
import com.runelive.model.PlayerRights;
import com.runelive.net.PlayerSession;
import com.runelive.net.packet.PacketBuilder;
import com.runelive.world.entity.impl.player.Player;
import com.runelive.world.entity.impl.player.PlayerLoading;

public final class LoginManager {

	private static final ConcurrentHashMap<Long, Player> successfulLogins = new ConcurrentHashMap<>();

	public static ConcurrentHashMap<Long, Player> getSuccessfulLogins() {
		return successfulLogins;
	}

	/**
	 * Starts the login process by adding the player's session info to the
	 * pending login list and starting the loading request (either JSON or SQL).
	 *
	 * @param session
	 * @param msg
	 * @return
	 */
	public static void startLogin(PlayerSession session, LoginDetailsMessage msg) {
		final Player player = session.getPlayer();
		PendingLogin.add(session, msg);
		if (GameSettings.MYSQL_PLAYER_LOADING) {
			PlayerLoading.performSqlRequest(player);
		} else if (GameSettings.JSON_PLAYER_LOADING) {
			PlayerLoading.loadJSON(player);
			LoginManager.finalizeLogin(session, msg);
		}
	}

	/**
	 * Submits the login finalization task to the threaded game engine for the
	 * specified player.
	 *
	 * @param player
	 */
	public static void finalizeLogin(final Player player) {
		PendingLogin pendingLogin = PendingLogin.get(player.getLongUsername());
		if (pendingLogin == null) {
			throw new RuntimeException("SEVERE LOGIN ERROR FOR " + player.getUsername()
					+ "! PendingLogin not found! Response: " + player.getResponse());
		}
		LoginManager.finalizeLogin(pendingLogin.getSession(), pendingLogin.getLoginDetails());
	}

	/**
	 * Gets the rank for the user.
	 * @param player The player we are getting the rank for.
	 * @return The rank assigned to the user
	 */
	private static int getRank(final Player player) {
		PlayerRights rights = player.getRights();
		switch (rights) {
			case DEVELOPER:
				return 18;
			case STAFF_MANAGER:
				return 17;
			case WIKI_MANAGER:
				return 16;
			case WIKI_EDITOR:
				return 15;
			case MANAGER:
				return 14;
			case PLAYER: {
				switch (player.getDonorRights()) {
					case 1:
						return 7;
					case 2:
						return 8;
					case 3:
						return 9;
					case 4:
						return 10;
					case 5:
						return 11;
				}
				if (player.getGameModeAssistant().isIronMan()) {
					return 12;
				}
			}
			default:
				return rights.ordinal();
		}
	}

	/**
	 * Finalizes the login for the specified session and login details.
	 *
	 * @param session
	 * @param msg
	 * @return
	 */
	private static void finalizeLogin(PlayerSession session, LoginDetailsMessage msg) {
		final Player player = session.getPlayer();
		player.setResponse(LoginResponses.getResponse(player, msg));
		final boolean newAccount = player.getResponse() == LoginResponses.NEW_ACCOUNT;
		if (newAccount) {
			player.setNewPlayer(true);
			player.setResponse(LoginResponses.LOGIN_SUCCESSFUL);
		}
		if (player.getResponse() == LoginResponses.LOGIN_SUCCESSFUL) {
			int rank = getRank(player);
			/**
			 * Successful login.
			 */
			session.getChannel().write(new PacketBuilder().put((byte) 2).put((byte) rank).put((byte) 0).toPacket());
			/**
			 * Initialize the player in game.
			 */
			successfulLogins.put(player.getLongUsername(), player);
			// PlayerHandler.handleLogin(player);
		} else {
			LoginManager.sendReturnCode(session.getChannel(), player.getResponse());
		}
		/**
		 * Remove the player's pending login now that the task is complete.
		 */
		PendingLogin.remove(player.getLongUsername());
	}

	public static void sendReturnCode(final Channel channel, final int code) {
		channel.write(new PacketBuilder().put((byte) code).toPacket())
				.addListener(listener -> listener.getChannel().close());
	}

}