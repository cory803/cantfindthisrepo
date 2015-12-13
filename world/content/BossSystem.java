package com.ikov.world.content;

import com.ikov.engine.task.Task;
import com.ikov.engine.task.TaskManager;
import com.ikov.model.Locations;
import com.ikov.model.Locations.Location;
import com.ikov.model.Position;
import com.ikov.model.RegionInstance;
import com.ikov.model.RegionInstance.RegionInstanceType;
import com.ikov.world.World;
import com.ikov.world.entity.impl.npc.NPC;
import com.ikov.world.entity.impl.player.Player;

public class BossSystem {
	private static int bossId;
	public static int getBossID() {
			return bossId;
		}

		public static void setBossID(int bossID) {
			bossId = bossID;
		}
		public enum Bosses {
			KBD(50), BOSS(1);
			
			private int npcId;
			
			private Bosses(int id) {
				npcId = id;
			}
			
		};
	public static void startInstance(Player player) {
		player.moveTo(new Position(2392, 9903, player.getIndex() * 4));
		player.setRegionInstance(new RegionInstance(player, RegionInstanceType.BOSS_SYSTEM));
		spawnBoss(player, getBossID());
	}

	public static void leaveInstance(Player player) {
		Locations.Location.BOSS_SYSTEM.leave(player);
	}

	public static void spawnBoss(final Player player, int bossID) {
		TaskManager.submit(new Task(2, player, false) {
			@Override
			public void execute() {
				if(player.getRegionInstance() == null || !player.isRegistered() || player.getLocation() != Location.BOSS_SYSTEM) {
					stop();
					return;
				}
				NPC n = new NPC(bossID, new Position(2392, 9894, player.getPosition().getZ())).setSpawnedFor(player);
				World.register(n);
				player.getRegionInstance().getNpcsList().add(n);
				n.getCombatBuilder().attack(player);
				stop();
			}
		});
	}

	public static void bossKilled(final Player player, NPC n) {
			if(player.getRegionInstance() != null)
				player.getRegionInstance().getNpcsList().remove(n);
			leaveInstance(player);
	}

}
