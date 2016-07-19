package com.runelive.world.clip.region.doors;

import com.runelive.model.GameObject;
import com.runelive.model.definitions.GameObjectDefinition;
import com.runelive.world.World;
import com.runelive.world.clip.region.RegionClipping;

/**
 *
 * @author Jonathan Sirens
 */
public final class DoorManager {
	
	public static boolean isDoor(GameObject gameObject) {
		if (gameObject.getName() != null) {
			final String name = gameObject.getName().toLowerCase();
			if (name.equals("door")|| name.equals("gate")) {
				return open(gameObject);
			}
		}
		return false;
	}

	public static boolean open(GameObject door) {
		final GameObjectDefinition definition = GameObjectDefinition.forId(door.getId());
				
		if (definition.getActions() == null
				|| definition.getActions()[0] == null)
			return false;
		
		final String action = definition.getActions()[0].toLowerCase();
				
		int rotation = door.getRotation();
		int object_id = door.getId();
		
		final boolean open_door = action.equals("open");
		final boolean close_door = action.equals("close");
		
		if (open_door) {
			rotation = door.getRotation() - 1;
			object_id = door.getId() - 1;
		} else if (close_door) {
			rotation = door.getRotation() + 1;
			object_id = door.getId() + 1;
		}
		
		GameObject new_door = new GameObject(object_id, rotation, door.getType(), door.getPosition());
			
		if (new_door.getName() == null || door.getName() == null 
				|| door.getName().equalsIgnoreCase("Door") && !new_door.getName().equalsIgnoreCase("Door")
				|| door.getName().equalsIgnoreCase("Gate") && !new_door.getName().equalsIgnoreCase("Gate")) {
			
			if (open_door) {
				rotation = door.getRotation() - 1;
				object_id = door.getId() + 1;
			} else if (close_door) {
				rotation = door.getRotation() + 1;
				object_id = door.getId() - 1;
			}
			
			new_door = new GameObject(object_id, rotation, door.getType(), door.getPosition());
			if (new_door.getName() == null || door.getName() == null 
					|| door.getName().equalsIgnoreCase("Door") && !new_door.getName().equalsIgnoreCase("Door")
					|| door.getName().equalsIgnoreCase("Gate") && !new_door.getName().equalsIgnoreCase("Gate")) {
				return false;
			}
		}
		
		World.deregister(door);
		World.register(new_door);
						
		if (GameObjectDefinition.forId(new_door.getId()).getActions() != null
				&& GameObjectDefinition.forId(new_door.getId()).getActions()[0] != null) {
			int clip_x_offset = 0;
			int clip_y_offset = 0;
		
			if (close_door) {
				switch (door.getRotation()) {
				case 2:
				case -1:
					clip_x_offset = 1;
					break;
				}
			} else if (open_door) {
				switch (door.getRotation()) {
				case 0:
					clip_x_offset = -1;
					break;
				case 1:
					clip_y_offset = 1;
					break;
				case 2:
					clip_x_offset = 1;
					break;
				case 3:
					clip_y_offset = -1;
					break;
				}
			}
			RegionClipping.removeClipping(door.getPosition().getX() + clip_x_offset,
				door.getPosition().getY() + clip_y_offset, door.getPosition().getZ(), 0);
		}
		return true;
	}
}