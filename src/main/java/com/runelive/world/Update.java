package com.runelive.world;

import com.runelive.GameServer;
import com.runelive.threading.event.Event;
import com.runelive.util.FilterExecutable;
import com.runelive.world.entity.impl.player.Player;

public final class Update {

	public static boolean announced;
	public static boolean running;
	public static int seconds;
	public static long startTime;

	public static boolean canLogin() {
		if (!Update.running) {
			return true;
		}
		return Update.getSecondsRemaining() > (60 * 2);
	}

	private static boolean shutdownCheck = false;

	public static void process() {
		if (Update.running && !Update.announced) {
			World.executeAll(UpdateFilterExecutable.INSTANCE);
			Update.announced = true;
		}
		if (Update.running && Update.getSecondsRemaining() <= 0) {
			World.executeAll(player -> World.deregister(player));
			if (shutdownCheck) {
				return;
			}
			shutdownCheck = true;
            GameServer.submit(new Event(5000) {
				int cycle = 0;
				@Override
				public void execute() {
					boolean locked = World.getPlayers().size() > 0;
					if (locked) {
						System.err.println("Waiting to close server [players: " + World.getPlayers().size() + ", cycle: " + cycle + "]");
					}
					if (cycle >= 3 && !locked) {
						System.err.println("All players disconnected and save packets sent. Closing gracefully.");
						System.exit(0);
					}
					cycle++;
				}
			});
		}
	}

    public static int announceCounter = 0;

	public static void update(Player player) {
		if (!Update.running) {
			return;
		}
		if (player.isUpdateAnnounced()) {
			return;
		}
		player.getPacketSender().sendSystemUpdate(Update.getSecondsRemaining() * 50 / 30);
		player.setUpdateAnnounced(true);
	}

	private static int getSecondsRemaining() {
		return (int) (((Update.startTime - System.currentTimeMillis()) + (Update.seconds * 1000)) / 1000);
	}

	private static class UpdateFilterExecutable implements FilterExecutable<Player> {

		public static final UpdateFilterExecutable INSTANCE = new UpdateFilterExecutable();

		@Override
		public void execute(Player player) {
			player.setUpdateAnnounced(false);
		}
	}
}