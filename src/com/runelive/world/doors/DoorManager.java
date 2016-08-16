package com.runelive.world.doors;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.GsonBuilder;
import com.runelive.model.GameObject;
import com.runelive.model.HashedPosition;
import com.runelive.world.World;
import com.runelive.world.content.CustomObjects;

/**
 *
 * @author relex lawl
 */
public final class DoorManager {
		
	public static void init() throws IOException {		
		int amount = 0;
		final long startup = System.currentTimeMillis();
		
		final FileReader reader = new FileReader("./data/def/doors/doors.json");
		final Door[] doors = new GsonBuilder().setPrettyPrinting().create().fromJson(reader, Door[].class);
		
		System.out.println("Loading global door configurations...");
		
		for (Door door : doors) {
			GLOBAL_DOORS.put(door.getOpenState().getPosition(), door);
			GLOBAL_DOORS.put(door.getClosedState().getPosition(), door);
			amount++;
		}
		
		reader.close();
		System.out.println("Loaded " + amount + " door configurations in " + (System.currentTimeMillis() - startup) + "ms");
	}
	
	public static boolean isDoor(GameObject gameObject) {
		System.out.println("Its a door");
		Door door = GLOBAL_DOORS.get(new HashedPosition(gameObject.getPosition()));
		
		if (door != null) {
			System.out.println("Doors not null");
			GameObject newDoor = null;
			boolean removeClipping = false;
			
			if (gameObject.getId() == door.getOpenState().getId()) {
				System.out.println("Doors is opened");
				//the door is opened and we are going to close it
				newDoor = new GameObject(door.getClosedState().getId(), door.getClosedState().getDirection(),
						gameObject.getType(), door.getClosedState().getPosition());
			} else if (gameObject.getId() == door.getClosedState().getId()) {
				//the door is closed and we are going to open it
				newDoor = new GameObject(door.getOpenState().getId(), door.getOpenState().getDirection(),
						gameObject.getType(), door.getOpenState().getPosition());
				removeClipping = true;
			}
			
			if (newDoor != null) {
				if (removeClipping || newDoor.getId() == 1517 || newDoor.getId() == 1520) {
					//these doors aren't meant to be kept open, so they are clipped
					//even while open, so we're manually removing the clipping
					//RegionClipping.removeClipping(newDoor);
					//RegionClipping.removeClipping(gameObject);
				}
				System.out.println("Change the door.");

				CustomObjects.deleteGlobalObject(gameObject);
				CustomObjects.spawnGlobalObject(new GameObject(-1, gameObject.getRotation(), gameObject.getType(), gameObject.getPosition()));
				CustomObjects.spawnGlobalObject(newDoor);
			}
			
			return true;
		}
		
		return false;
	}
	
	private static final Map<HashedPosition, Door> GLOBAL_DOORS = new HashMap<>();
}