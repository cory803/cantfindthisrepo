package com.runelive.loginserver;


import com.runelive.GameServer;
import com.runelive.model.player.PlayerDetails;
import com.runelive.threading.task.impl.LoginTask;
import com.runelive.world.entity.impl.player.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class LoginProcessor {

	private static final ConcurrentHashMap<Long, Player> successfulLogins = new ConcurrentHashMap<>();
	private static final Map<Long, PlayerDetails> pending = new ConcurrentHashMap<>();
	private static final Map<Long, Long> loginTime = new ConcurrentHashMap<>();

	public static ConcurrentHashMap<Long, Player> getSuccessfulLogins() {
		return successfulLogins;
	}

	public static void pendLogin(PlayerDetails details) {
		if (LoginProcessor.pending.containsKey(details.getEncodedName())) {
			if (LoginProcessor.loginTime.get(details.getEncodedName()) < (System.currentTimeMillis() - 20000)) {
				GameServer.getLogger().log(Level.INFO, details.getName() + "'s login request took more than 20 seconds to complete and has been removed");
				LoginProcessor.pending.remove(details.getEncodedName());
				LoginProcessor.loginTime.remove(details.getEncodedName());
			} else {
				return;
			}
		}
		LoginProcessor.pending.put(details.getEncodedName(), details);
		LoginProcessor.loginTime.put(details.getEncodedName(), System.currentTimeMillis());
		GameServer.getLoginServer().getPacketCreator().requestLogin(details);
	}

	public static final void emptyPendingLogins() {
		LoginProcessor.pending.clear();
		LoginProcessor.loginTime.clear();
	}

	public static void handleLoginResponse(long hash, int response, int staffRights, int donorRights, String gameSave) {
		try {
			PlayerDetails player = LoginProcessor.pending.get(hash);
			if (player == null) {
				return;
			}
			GameServer.submit(new LoginTask(player, hash, response, staffRights, donorRights, gameSave));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void remove(long hash) {
		LoginProcessor.pending.remove(hash);
		LoginProcessor.loginTime.remove(hash);
	}
}
