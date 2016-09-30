package com.chaos.world.content;

import java.util.concurrent.CopyOnWriteArrayList;

import com.chaos.engine.task.Task;
import com.chaos.engine.task.TaskManager;
import com.chaos.model.*;
import com.chaos.world.World;
import com.chaos.world.doors.Door;
import com.chaos.world.doors.DoorManager;
import com.chaos.world.entity.impl.GroundItemManager;
import com.chaos.world.entity.impl.player.Player;

/**
 * Handles customly spawned objects (mostly global but also privately for
 * players)
 * 
 * @author Gabriel Hannason
 */
public class CustomObjects {

	private static final int DISTANCE_SPAWN = 70; // Spawn if within 70 squares
													// of distance
	private static final CopyOnWriteArrayList<GameObject> CUSTOM_OBJECTS = new CopyOnWriteArrayList<GameObject>();
	public static final CopyOnWriteArrayList<GameObject> REMOVE_OBJECTS = new CopyOnWriteArrayList<GameObject>();

	public static void init() {
		for (int i = 0; i < CLIENT_OBJECTS.length; i++) {
			int id = CLIENT_OBJECTS[i][0];
			int x = CLIENT_OBJECTS[i][1];
			int y = CLIENT_OBJECTS[i][2];
			int z = CLIENT_OBJECTS[i][3];
			int face = CLIENT_OBJECTS[i][4];
			GameObject object = new GameObject(id, new Position(x, y, z));
			object.setRotation(face);
			World.addObject(object);
		}
		for (int i = 0; i < CUSTOM_OBJECTS_SPAWNS.length; i++) {
			int id = CUSTOM_OBJECTS_SPAWNS[i][0];
			int x = CUSTOM_OBJECTS_SPAWNS[i][1];
			int y = CUSTOM_OBJECTS_SPAWNS[i][2];
			int z = CUSTOM_OBJECTS_SPAWNS[i][3];
			int face = CUSTOM_OBJECTS_SPAWNS[i][4];
			GameObject object = new GameObject(id, new Position(x, y, z));
			object.setRotation(face);
			CUSTOM_OBJECTS.add(object);
			World.register(object);
		}
	}

	private static void handleList(GameObject object, String handleType) {
		switch (handleType.toUpperCase()) {
		case "DELETE":
			for (GameObject objects : CUSTOM_OBJECTS) {
				if (objects.getId() == object.getId() && object.getPosition().equals(objects.getPosition())) {
					CUSTOM_OBJECTS.remove(objects);
				}
			}
			break;
		case "ADD":
			if (!CUSTOM_OBJECTS.contains(object)) {
				CUSTOM_OBJECTS.add(object);
			}
			break;
		case "EMPTY":
			CUSTOM_OBJECTS.clear();
			break;
		}
	}

	public static void spawnObject(Player p, GameObject object) {
		if (object.getId() != -1) {
			p.getPacketSender().sendObject(object);
			if (!World.objectExists(object)) {
				World.addObject(object);
			}
		} else {
			deleteObject(p, object);
		}
	}

	public static void deleteObject(Player p, GameObject object) {
		p.getPacketSender().sendObjectRemoval(object);
		if (World.objectExists(object)) {
			World.remove(object);
		}
	}

	public static void deleteGlobalObject(GameObject object) {
		handleList(object, "delete");
		World.deregister(object);
	}

	public static void spawnGlobalObject(GameObject object) {
		handleList(object, "add");
		World.register(object);
	}

	public static void spawnGlobalObjectWithinDistance(GameObject object) {
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			if (object.getPosition().isWithinDistance(player.getPosition(), DISTANCE_SPAWN)) {
				spawnObject(player, object);
			}
		}
	}

	public static void deleteGlobalObjectWithinDistance(GameObject object) {
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			if (object.getPosition().isWithinDistance(player.getPosition(), DISTANCE_SPAWN)) {
				deleteObject(player, object);
			}
		}
	}

	public static boolean objectExists(Position pos) {
		return getGameObject(pos) != null;
	}

	public static GameObject getGameObject(Position pos) {
		for (GameObject objects : CUSTOM_OBJECTS) {
			if (objects != null && objects.getPosition().equals(pos)) {
				return objects;
			}
		}
		return null;
	}

	public static void handleRegionChange(Player p) {
		for (GameObject object : REMOVE_OBJECTS) {
			if (object == null)
				continue;
			if (object.getPosition().isWithinDistance(p.getPosition(), DISTANCE_SPAWN)) {
				deleteObject(p, object);
			}
		}
		for (GameObject object : CUSTOM_OBJECTS) {
			if (object == null)
				continue;
			if (object.getPosition().isWithinDistance(p.getPosition(), DISTANCE_SPAWN)) {
				spawnObject(p, object);
			}
		}
	}

	public static void objectRespawnTask(Player p, final GameObject tempObject, final GameObject mainObject,
			final int cycles) {
		deleteObject(p, mainObject);
		spawnObject(p, tempObject);
		TaskManager.submit(new Task(cycles) {
			@Override
			public void execute() {
				deleteObject(p, tempObject);
				spawnObject(p, mainObject);
				this.stop();
			}
		});
	}

	public static void globalObjectRespawnTask(final GameObject tempObject, final GameObject mainObject,
			final int cycles) {
		deleteGlobalObject(mainObject);
		spawnGlobalObject(tempObject);
		TaskManager.submit(new Task(cycles) {
			@Override
			public void execute() {
				deleteGlobalObject(tempObject);
				spawnGlobalObject(mainObject);
				this.stop();
			}
		});
	}

	public static void globalObjectRemovalTask(final GameObject object, final int cycles) {
		spawnGlobalObject(object);
		TaskManager.submit(new Task(cycles) {
			@Override
			public void execute() {
				deleteGlobalObject(object);
				this.stop();
			}
		});
	}

	public static void globalFiremakingTask(final GameObject fire, final Player player, final int cycles) {
		spawnGlobalObject(fire);
		TaskManager.submit(new Task(cycles) {
			@Override
			public void execute() {
				deleteGlobalObject(fire);
				if (player.getInteractingObject() != null && player.getInteractingObject().getId() == 2732) {
					player.setInteractingObject(null);
				}
				this.stop();
			}

			@Override
			public void stop() {
				setEventRunning(false);
				GroundItemManager.spawnGroundItem(player,
						new GroundItem(new Item(592), fire.getPosition(), player.getUsername(), false, 150, true, 100));
			}
		});
	}

	/**
	 * Contains
	 * 
	 * @param ObjectId
	 *            - The object ID to spawn
	 * @param absX
	 *            - The X position of the object to spawn
	 * @param absY
	 *            - The Y position of the object to spawn
	 * @param Z
	 *            - The Z position of the object to spawn
	 * @param face
	 *            - The position the object will face
	 */

	// Only adds clips to these objects, they are spawned clientsided
	// NOTE: You must add to the client's customobjects array to make them
	// spawn, this is just
	// clipping!
	private static final int[][] CLIENT_OBJECTS = {
			//rune ore @ BH
			{ 14859, 3118, 3690, 0, 1 },
			{ 14859, 3117, 3691, 0, 1 },
			{ 14859, 3119, 3689, 0, 1 },
			{ 14859, 3117, 3688, 0, 1 },
			{ 14859, 3116, 3688, 0, 1 },
			{ 14859, 3115, 3690, 0, 1 },
			{ 14859, 3116, 3689, 0, 1 },
			//giant crystal
			/* Training Grounds Skillzone */
			{ 62, 2508, 3363, 0, 2 },
			//dummies
			{ -1, 2516, 3369, 0, 0 },
			{ -1, 2514, 3369, 0, 0 },
			{ -1, 2514, 3367, 0, 0 },
			{ -1, 2515, 3365, 0, 0 },
			{ -1, 2516, 3370, 0, 0 },
			{ -1, 2513, 3371, 0, 0 },
			{ -1, 2511, 3365, 0, 0 },
			{ -1, 2511, 3369, 0, 0 },
			{ -1, 2511, 3373, 0, 0 },
			{ -1, 2510, 3367, 0, 0 },
			{ -1, 2508, 3366, 0, 0 },
			{ -1, 2509, 3371, 0, 0 },
			{ -1, 2507, 3368, 0, 0 },
			{ -1, 2505, 3370, 0, 0 },
			{ -1, 2514, 3384, 0, 0 }, // table
			//bank booths
			{ 2213, 2515, 3383, 0, 2 },
			{ 2213, 2514, 3383, 0, 2 },
			//ores
			{ 9709, 2523, 3377, 0, 0 },
			{ 9708, 2524, 3377, 0, 0 },
			{ 9715, 2525, 3377, 0, 0 },
			{ 9714, 2526, 3377, 0, 0 },
			{ 9718, 2527, 3377, 0, 0 },
			{ 9717, 2528, 3377, 0, 0 },
			{ 29217, 2529, 3377, 0, 0 },
			{ 29215, 2530, 3377, 0, 0 },
			{ 9720, 2531, 3377, 0, 0 },
			{ 25370, 2532, 3377, 0, 0 },
			{ 11941, 2533, 3377, 0, 0 },
			{ 9708, 2523, 3373, 0, 0 },
			{ 9714, 2524, 3373, 0, 0 },
			{ 9717, 2525, 3373, 0, 0 },
			{ 29217, 2526, 3373, 0, 0 },
			{ 29215, 2527, 3373, 0, 0 },
			{ 29215, 2528, 3373, 0, 0 },
			{ 9720, 2529, 3373, 0, 0 },
			{ 9720, 2530, 3373, 0, 0 },
			{ 25368, 2531, 3373, 0, 0 },
			{ 25370, 2532, 3373, 0, 0 },
			{ 11941, 2533, 3373, 0, 0 },
			//furnace & anvil
			{ 6189, 2532, 3370, 0, 2 },
			{ 2783, 2531, 3369, 0, 2 },
			//stalls
			{ 4876, 2527, 3369, 0, 2 },
			{ 4875, 2526, 3369, 0, 2 },
			{ 4874, 2525, 3369, 0, 2 },
			{ 4877, 2524, 3369, 0, 2 },
			{ 4878, 2523, 3369, 0, 2 },

			{ 6552, 3232, 2887, 0, 0 },

			// 1 = west
			// 2 = north
			// 3 = east
			// 4 = south

	};

	/**
	 * Contains
	 * 
	 * @param ObjectId
	 *            - The object ID to spawn
	 * @param absX
	 *            - The X position of the object to spawn
	 * @param absY
	 *            - The Y position of the object to spawn
	 * @param Z
	 *            - The Z position of the object to spawn
	 * @param face
	 *            - The position the object will face
	 */

	// Objects that are handled by the server on regionchange
	private static final int[][] CUSTOM_OBJECTS_SPAWNS = {
			{ 2274, 3652, 3488, 0, 0 },
			{ 9326, 3001, 3960, 0, 0 },

	};
}
