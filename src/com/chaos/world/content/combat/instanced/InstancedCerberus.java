package com.chaos.world.content.combat.instanced;

import com.chaos.engine.task.Task;
import com.chaos.engine.task.TaskManager;
import com.chaos.model.Locations.Location;
import com.chaos.model.Position;
import com.chaos.model.RegionInstance;
import com.chaos.model.RegionInstance.RegionInstanceType;
import com.chaos.world.World;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;

public class InstancedCerberus {

	public static final int NPC_ID = 5866;

	public static void enterDungeon(Player player) {
		if (!player.getSlayer().getSlayerTask().onSlayerTask(5866)) {
			player.getPacketSender().sendMessage("You must be on a Cerberus slayer task to enter Cerberus's Cave.");
		} else {
			player.moveTo(new Position(1240, 1226, player.getIndex() * 4));
			player.setRegionInstance(new RegionInstance(player, RegionInstanceType.CERBERUS_CAVE));
			spawnCerberus(player);
		}
	}

	public static void leaveDungeon(Player player) {
		Location.CERBERUS_CAVE.leave(player);
	}

	public static void handleCerberusDeath(final Player player, NPC n) {
		if (n.getId() == NPC_ID) {
			player.spawnedCerberus = false;
		}
	}

	public static void spawnCerberus(final Player player) {
		TaskManager.submit(new Task(2, player, false) {
			@Override
			public void execute() {
				if (player.getRegionInstance() == null || !player.isRegistered()) {
					stop();
					return;
				}
				if (!player.spawnedCerberus) {
					NPC n = new NPC(NPC_ID, new Position(1240, 1253, player.getPosition().getZ()))
							.setSpawnedFor(player);
					World.register(n);
					player.spawnedCerberus = true;
					player.getRegionInstance().getNpcsList().add(n);
					n.getCombatBuilder().attack(player);
					stop();
				}
			}
		});
	}
}