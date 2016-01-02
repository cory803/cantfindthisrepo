package com.ikov.world.content;

import com.ikov.engine.task.Task;
import com.ikov.engine.task.TaskManager;
import com.ikov.engine.task.impl.NPCRespawnTask;
import com.ikov.model.Locations;
import com.ikov.model.Locations.Location;
import com.ikov.model.Position;
import com.ikov.model.RegionInstance;
import com.ikov.model.RegionInstance.RegionInstanceType;
import com.ikov.world.World;
import com.ikov.world.entity.impl.npc.NPC;
import com.ikov.world.entity.impl.player.Player;

public class BossSystem {
		public enum Bosses {
			KBD(50), 
			TD(8349),
			CORP(8133);
			
			private int npcId;
			
			private Bosses(int id) {
				npcId = id;
			}
			public int getBossID() {
				return npcId;
			}
			
		};
	public static void startInstance(Player player, int bossID) {
		player.moveTo(new Position(2392, 9903, player.getIndex() * 4));
		player.setRegionInstance(new RegionInstance(player, RegionInstanceType.BOSS_SYSTEM));
		spawnBoss(player, bossID);
		
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
				n.getDefinition().setRespawnTime(-1);
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
			World.deregister(n);
			TaskManager.submit(new NPCRespawnTask(n, 0));
			leaveInstance(player);
	}
}

