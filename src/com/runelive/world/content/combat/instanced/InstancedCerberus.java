package com.runelive.world.content.combat.instanced;

import com.runelive.engine.task.Task;
import com.runelive.engine.task.TaskManager;
import com.runelive.model.Locations.Location;
import com.runelive.model.Position;
import com.runelive.model.RegionInstance;
import com.runelive.model.RegionInstance.RegionInstanceType;
import com.runelive.world.World;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;

public class InstancedCerberus {

	public static final int NPC_ID = 5866;

	public static void enterDungeon(Player player) {
		if (player.getSlayer().getSlayerTask().getNpcId() != 5866) {
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