package com.runelive.world.content.minigames.impl.zulrah;

import com.runelive.engine.task.Task;
import com.runelive.engine.task.TaskManager;
import com.runelive.model.Animation;
import com.runelive.model.Flag;
import com.runelive.model.Graphic;
import com.runelive.model.Locations;
import com.runelive.model.Locations.Location;
import com.runelive.model.Position;
import com.runelive.model.RegionInstance;
import com.runelive.model.RegionInstance.RegionInstanceType;
import com.runelive.util.Misc;
import com.runelive.world.World;
import com.runelive.world.content.dialogue.DialogueManager;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.npc.NPCMovementCoordinator.Coordinator;
import com.runelive.world.entity.impl.player.Player;

public class Zulrah {
	
	//Weakness is magic, uses range to attack with
	public static final int GREEN_ZULRAH = 2043;
	
	//Weakness is range, uses range and mage to attack with
	public static final int BLUE_ZULRAH = 2042;
	
	//Weakness is magic, uses melee to attack with
	public static final int RED_ZULRAH = 2044;
	
	//Weakness is magic, uses mage and range to attack with
	public static final int JAD_ZULRAH = 2045;
	
	//Zulrah's animation to go down to the next faze
	public static final Animation GO_DOWN = new Animation(5072);
	
	//Zulrah's animation to go up to the next faze
	public static final Animation GO_UP = new Animation(5073);
	
	//Zulrah's attack animation to shoot at a player
	public static final Animation SHOOT = new Animation(5069);
	
	//Zulrah's attack animation to lung at a player
	public static final Animation LUNG = new Animation(5807);
	
	//Zulrah's target animation to target a player
	public static final Animation TARGET = new Animation(5806);
	
	//Zulrah's fire ball magic attack graphic
	public static final Graphic FIRE_BALL = new Graphic(1046);
	
	//Zulrah's range bolt range attack graphic
	public static final Graphic RANGE_BOLT = new Graphic(1044);
	
	//Zulrah's toxic cloud graphic
	public static final Graphic TOXIC_CLOUD = new Graphic(1045);
	
	//Snakeling spawn graphic
	public static final Graphic SNAKELING_SPAWN = new Graphic(1047);
	
	//Zulrah's toxic venom cloud positions that are possible
	public static final Position[] TOXIC_CLOUD_POSITIONS = {
		new Position(2273, 3073),
		new Position(2273, 3071),
		new Position(2270, 3069),
		new Position(2267, 3069),
		new Position(2263, 3070),
		new Position(2263, 3074),
		new Position(2263, 3076)
	};
	
	
	/*
	 * Enter the Zulrah Island
	 */
	public static void enterIsland(Player player) {
		player.moveTo(new Position(2268, 3069, player.getPrivateFloor()));
		player.setLocation(Location.ZULRAH_PIT);
		player.setRegionInstance(new RegionInstance(player, RegionInstanceType.ZULRAH_PIT));
		spawnZulrah(player);
	}
	
	/*
	 * Leave the Zulrah Island
	 */
	public static void leaveIsland(Player player, boolean resetStats) {
		Locations.Location.ZULRAH_PIT.leave(player);
		if (resetStats)
			player.restart();
	}

	/*
	 * Spawn the first faze of Zulrah
	 */
	public static void spawnZulrah(final Player player) {
		TaskManager.submit(new Task(1, player, false) {
			int tick = 0;
			@Override
			public void execute() {
				if (player.getRegionInstance() == null || !player.isRegistered()
						|| player.getLocation() != Location.ZULRAH_PIT) {
					stop();
					return;
				}
				if(tick == 1) {
					DialogueManager.sendStatement(player, "Prepare for battle!");
				}
				if(tick == 3) {
					//This adds onto the current zulrah rotation # that you have saved
					player.nextZulrahRotation();
					//This adds onto the current zulrah kill step
					player.nextZulrahStep();
					//Grabs the current zulrah ID and sets the current position
					int zulrahId = player.getZulrahID();
					//Declares the npc zulrah
					NPC n = new NPC(zulrahId, player.getZulrahPosition()).setSpawnedFor(player);
					//Adds the npc zulrah to the official game
					World.register(n);
					player.getRegionInstance().getNpcsList().add(n);
					//The npc zulrah attacks the player
					n.getCombatBuilder().attack(player);
					stop();
				}
				tick++;
			}
		});
	}
	
	/*
	 * Spawn the next faze of Zulrah @nextZulrahStep
	 */
	public static void next(final Player player, final NPC zulrah) {
		player.nextZulrahStep();
		TaskManager.submit(new Task(1, player, false) {
			int tick = 0;
			int health = zulrah.getConstitution();
			int id = player.getZulrahID();
			NPC nextZulrah = new NPC(id, player.getZulrahPosition());
			@Override
			public void execute() {
				if(tick == 0) {
					zulrah.setChargingAttack(true);
					zulrah.performAnimation(GO_DOWN);
				}
				if(tick == 2) {
					if(zulrah.isRegistered()) {
						World.deregister(zulrah);
						player.getRegionInstance().getNpcsList().remove(zulrah);
					}
				}
				if(tick == 3) {
					nextZulrah = new NPC(id, player.getZulrahPosition()).setSpawnedFor(player);
					nextZulrah.getMovementCoordinator().setCoordinator(new Coordinator(false, -1));
					World.register(nextZulrah);
					player.getRegionInstance().getNpcsList().add(nextZulrah);
					nextZulrah.setConstitution(health);
					nextZulrah.performAnimation(GO_UP);
				}
				if(tick == 4) {
					nextZulrah.setChargingAttack(false);
					nextZulrah.getCombatBuilder().attack(player);
					stop();
				}
				tick++;
			}
		});
	}

	/*
	 * Kill the Zulrah
	 */
	public static void handleZulrahDeath(final Player player, NPC n) {
		
	}
}
