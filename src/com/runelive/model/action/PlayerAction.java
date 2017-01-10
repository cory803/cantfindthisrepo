package com.runelive.model.action;

import com.runelive.model.Direction;
import com.runelive.world.World;
import com.runelive.world.entity.impl.player.Player;

public abstract class PlayerAction extends Action {
	protected final Player player;

	protected PlayerAction(Player player) {
		this(player, 1);
	}

	protected PlayerAction(Player player, int ticks) {
		super(ticks);
		this.player = player;
	}

	public static void forceWalk(Player c) {
		if (!World.directionBlocked(Direction.WEST, c.getPosition().getZ(), c.getPosition().getX(), c.getPosition().getY(), 1)) {
			c.walkTo(-1, 0);
		} else if (!World.directionBlocked(Direction.NORTH, c.getPosition().getZ(), c.getPosition().getX(), c.getPosition().getY(), 1)) {
			c.walkTo(0, 1);
		} else if (!World.directionBlocked(Direction.SOUTH, c.getPosition().getZ(), c.getPosition().getX(), c.getPosition().getY(), 1)) {
			c.walkTo(0, -1);
		} else if (!World.directionBlocked(Direction.EAST, c.getPosition().getZ(), c.getPosition().getX(), c.getPosition().getY(), 1)) {
			c.walkTo(1, 0);
		}
	}
}
