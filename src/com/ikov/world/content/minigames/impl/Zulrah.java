package com.ikov.world.content.minigames.impl;

import com.ikov.engine.task.Task;
import com.ikov.engine.task.TaskManager;
import com.ikov.model.Locations.Location;
import com.ikov.model.Position;
import com.ikov.model.RegionInstance;
import com.ikov.model.RegionInstance.RegionInstanceType;
import com.ikov.world.World;
import com.ikov.world.entity.impl.npc.NPC;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.world.content.Achievements;
import com.ikov.world.content.Achievements.AchievementData;

public class Zulrah {

	public static final int ZULRAH_GREEN_NPC_ID = 2043;
	public static final int ZULRAH_BLUE_NPC_ID = 2042;
	public static final int ZULRAH_RED_NPC_ID = 2044;
	public static final int ZULRAH_JAD_NPC_ID = 2045;

	public static void enter_pit(Player player) {
		player.moveTo(new Position(2268, 3069, player.getIndex() * 4));
		player.setRegionInstance(new RegionInstance(player, RegionInstanceType.ZULRAH_PIT));
		start_zulrah(player);
	}

	public static void leave_pit(Player player, boolean resetStats) {
		if (player.getRegionInstance() != null) {
			if(resetStats)
				player.restart();
			for(int i = 0; i < player.getRegionInstance().getNpcsList().size(); i++) {
				if(player.getRegionInstance().getNpcsList().get(i) != null) {
					World.deregister(player.getRegionInstance().getNpcsList().get(i));
					player.getRegionInstance().getNpcsList().remove(player.getRegionInstance().getNpcsList().get(i));
				}
			}
			player.getCombatBuilder().reset(true);
			if(player.getRegionInstance() != null) {
				player.getRegionInstance().destruct();
			}
		}
	}

	public static void start_zulrah(final Player player) {
		TaskManager.submit(new Task(2, player, false) {
			@Override
			public void execute() {
				if(player.getRegionInstance() == null || !player.isRegistered() || player.getLocation() != Location.ZULRAH_PIT) {
					stop();
				return;
				}
				NPC n = new NPC(ZULRAH_GREEN_NPC_ID, new Position(2269, 3077, player.getPosition().getZ()));
				World.register(n);
				player.getRegionInstance().getNpcsList().add(n);
				n.getCombatBuilder().attack(player);
				stop();
			}
		});
	}

	public static void handleZulrahDeath(final Player player, NPC n) {
		if(n.getId() == ZULRAH_GREEN_NPC_ID || n.getId() == ZULRAH_RED_NPC_ID || n.getId() == ZULRAH_BLUE_NPC_ID || n.getId() == ZULRAH_JAD_NPC_ID) {
			if(player.getRegionInstance() != null) {
				player.getRegionInstance().getNpcsList().remove(n);
			}
//			Achievements.doProgress(player, AchievementData.UNLOCK_ALL_LOYALTY_TITLES);
			leave_pit(player, true);
		}
	}

}
