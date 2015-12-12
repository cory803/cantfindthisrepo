package com.ikov.world.content;

import com.ikov.engine.task.Task;
import com.ikov.engine.task.TaskManager;
import com.ikov.model.Locations;
import com.ikov.model.Locations.Location;
import com.ikov.model.Position;
import com.ikov.model.RegionInstance;
import com.ikov.model.RegionInstance.RegionInstanceType;
import com.ikov.util.Misc;
import com.ikov.world.World;
import com.ikov.world.content.dialogue.DialogueManager;
import com.ikov.world.entity.impl.npc.NPC;
import com.ikov.world.entity.impl.player.Player;

public class BossSystem {
	
	public static void spawnBoss(Player player, int timer, int bossID, Position pos, boolean solo) {
		TaskManager.submit(new Task(timer, player, false) {
			@Override
			public void execute() {
				if(player.getRegionInstance() == null /*|| !player.isRegistered() || player.getLocation() != Location.FIGHT_CAVES*/) {
					stop();
					return;
				}
				if (timer <= 0) {
				NPC n = new NPC(bossID, new Position(2399, 5083, player.getPosition().getZ())).setSpawnedFor(player);
				World.register(n);
				player.getRegionInstance().getNpcsList().add(n);
				n.getCombatBuilder().attack(player);
				stop();
				}
			}
		});
	}
	public static void startBoss(Player player, int bossID) {
		Position SPAWN = new Position(2392, 9893, player.getIndex() * 4);
		player.moveTo(new Position(player.getPosition().getX(), player.getPosition().getY(), player.getIndex() * 4));
		player.setRegionInstance(new RegionInstance(player, RegionInstanceType.BOSS_SYSTEM));
		spawnBoss(player, 25, bossID, SPAWN, false);
	}	
	public static void endGame(Player player, boolean resetStats) {
		Locations.Location.BOSS_SYSTEM.leave(player);
		if(resetStats)
			player.restart();
	}
	public static void endBoss(final Player player, NPC n) {
			if(player.getRegionInstance() != null)
			player.getRegionInstance().getNpcsList().remove(n);
			endGame(player, true);
	}
	
}
