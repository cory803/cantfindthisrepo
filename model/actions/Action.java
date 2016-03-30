package com.ikov.model.actions;

import com.ikov.world.entity.impl.player.Player;

/**
 * Represents an action performed by the player.
 * @author Blake
 *
 */
public interface Action {
	
	public void handle(Player player);

}
