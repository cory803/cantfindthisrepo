package com.ikov.world.content;

import com.ikov.engine.task.Task;
import com.ikov.engine.task.TaskManager;
import com.ikov.model.Animation;
import com.ikov.model.Item;
import com.ikov.model.Locations.Location;
import com.ikov.model.Position;
import com.ikov.model.definitions.NPCDrops;
import com.ikov.world.World;
import com.ikov.world.entity.impl.npc.NPC;
import com.ikov.world.entity.impl.player.Player;

public class Digging {

	public static void dig(final Player player) {
		if (!player.getClickDelay().elapsed(2000))
			return;
		player.getMovementQueue().reset();
		player.getPacketSender().sendMessage("You start digging..");
		player.performAnimation(new Animation(830));
		TaskManager.submit(new Task(2, player, false) {
			/*
			 * (non-Javadoc)
			 * 
			 * @see com.ikov.engine.task.Task#execute()
			 */
			@Override
			public void execute() {
				/**
				 * Clue scrolls
				 */
				/*
				 * if
				 * (/*ClueScrolls.digSpot(player)*DiggingScrolls.digClue(player)
				 * || MapScrolls.digClue(player) ||
				 * CoordinateScrolls.digClue(player)) { stop(); return; }
				 */
				Position targetPosition = null;
				/**
				 * Wilderness Keys
				 **/
				if (player.getLocation() == Location.WILDKEY_ZONE) {
					Position spawnNpc = new Position(player.getPosition().getX() + 1, player.getPosition().getY());
					if (player.getInventory().containsAny(player.allKeys)) {
						if(player.getInventory().contains(1543)) { //red best
							player.getInventory().deleteAmount(1543, 1);
							
						} else if(player.getInventory().contains(1545)) { //yellow 2nd best
							player.getInventory().deleteAmount(1545, 1);
							
						} else if(player.getInventory().contains(1546)) { //blue 3rd best
							player.getInventory().deleteAmount(1546, 1);	
							
						} else if(player.getInventory().contains(1547)) { //magenta 4th best
							player.getInventory().deleteAmount(1547, 1);	
								
						} else if(player.getInventory().contains(1548)) { //green worst
							player.getInventory().deleteAmount(1548, 1);
						
						} else {
							
						}
					}
				}
				/**
				 * Barrows
				 */
				if (inArea(player.getPosition(), 3553, 3301, 3561, 3294))
					targetPosition = new Position(3578, 9706, -1);
				else if (inArea(player.getPosition(), 3550, 3287, 3557, 3278))
					targetPosition = new Position(3568, 9683, -1);
				else if (inArea(player.getPosition(), 3561, 3292, 3568, 3285))
					targetPosition = new Position(3557, 9703, -1);
				else if (inArea(player.getPosition(), 3570, 3302, 3579, 3293))
					targetPosition = new Position(3556, 9718, -1);
				else if (inArea(player.getPosition(), 3571, 3285, 3582, 3278))
					targetPosition = new Position(3534, 9704, -1);
				else if (inArea(player.getPosition(), 3562, 3279, 3569, 3273))
					targetPosition = new Position(3546, 9684, -1);
				else if (inArea(player.getPosition(), 2986, 3370, 3013, 3388))
					targetPosition = new Position(3546, 9684, -1);
				if (targetPosition != null)
					player.moveTo(targetPosition);
				else
					player.getPacketSender().sendMessage("You find nothing of interest.");
				targetPosition = null;
				stop();
			}
		});
		player.getClickDelay().reset();
	}

	private static boolean inArea(Position pos, int x, int y, int x1, int y1) {
		return pos.getX() > x && pos.getX() < x1 && pos.getY() < y && pos.getY() > y1;
	}
}
