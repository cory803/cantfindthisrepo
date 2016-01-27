package com.ikov.world.content.minigames.impl;

import com.ikov.engine.task.Task;
import com.ikov.engine.task.TaskManager;
import com.ikov.model.Locations.Location;
import com.ikov.model.Locations;
import com.ikov.model.Position;
import com.ikov.model.RegionInstance;
import com.ikov.model.RegionInstance.RegionInstanceType;
import com.ikov.util.Misc;
import com.ikov.world.World;
import com.ikov.world.entity.impl.npc.NPC;
import com.ikov.world.entity.impl.player.Player;

public class Zulrah {

	public static final int ZULRAH_GREEN_NPC_ID = 2043;
	public static final int ZULRAH_BLUE_NPC_ID = 2042;
	public static final int ZULRAH_RED_NPC_ID = 2044;
	public static final int ZULRAH_JAD_NPC_ID = 2045;

	public static void enter_pit(Player player) {
		player.moveTo(new Position(2268, 3069, player.getIndex() * 4));
		player.setRegionInstance(new RegionInstance(player, RegionInstanceType.FIGHT_CAVE));
		int rotation = Misc.getRandom(3);
		player.setZulrahRotation(rotation);
		start_zulrah(player);
	}

	public static void leave_pit(Player player, boolean resetStats) {
		Locations.Location.ZULRAH_PIT.leave(player);
		if(resetStats)
			player.restart();
	}

	public static void start_zulrah(final Player player) {
		if(player.getZulrahRotation() == 0) {
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {
					if(player.getRegionInstance() == null || !player.isRegistered() || player.getLocation() != Location.ZULRAH_PIT) {
						stop();
						return;
					}
					NPC n = new NPC(ZULRAH_GREEN_NPC_ID, new Position(2269, 3077, player.getPosition().getZ())).setSpawnedFor(player);
					World.register(n);
					player.getRegionInstance().getNpcsList().add(n);
					n.getCombatBuilder().attack(player);
					stop();
				}
			});
		} else if(player.getZulrahRotation() == 1) {
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {
					if(player.getRegionInstance() == null || !player.isRegistered() || player.getLocation() != Location.ZULRAH_PIT) {
						stop();
						return;
					}
					NPC n = new NPC(ZULRAH_GREEN_NPC_ID, new Position(2269, 3077, player.getPosition().getZ())).setSpawnedFor(player);
					World.register(n);
					player.getRegionInstance().getNpcsList().add(n);
					n.getCombatBuilder().attack(player);
					stop();
				}
			});
		} else if(player.getZulrahRotation() == 2) {
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {
					if(player.getRegionInstance() == null || !player.isRegistered() || player.getLocation() != Location.ZULRAH_PIT) {
						stop();
						return;
					}
					NPC n = new NPC(ZULRAH_GREEN_NPC_ID, new Position(2269, 3077, player.getPosition().getZ())).setSpawnedFor(player);
					World.register(n);
					player.getRegionInstance().getNpcsList().add(n);
					n.getCombatBuilder().attack(player);
					stop();
				}
			});
		} else if(player.getZulrahRotation() == 3) {
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {
					if(player.getRegionInstance() == null || !player.isRegistered() || player.getLocation() != Location.ZULRAH_PIT) {
						stop();
						return;
					}
					NPC n = new NPC(ZULRAH_GREEN_NPC_ID, new Position(2269, 3076, player.getPosition().getZ())).setSpawnedFor(player);
					World.register(n);
					player.getRegionInstance().getNpcsList().add(n);
					n.getCombatBuilder().attack(player);
					stop();
				}
			});
		}
	}

	public static void handleZulrahDeath(final Player player, NPC n) {
		if(n.getId() == ZULRAH_GREEN_NPC_ID || n.getId() == ZULRAH_RED_NPC_ID || n.getId() == ZULRAH_BLUE_NPC_ID || n.getId() == ZULRAH_JAD_NPC_ID) {
			if(player.getRegionInstance() != null) {
				player.getRegionInstance().getNpcsList().remove(n);
			}
			//leave_pit(player, true);
		}
	}

}
