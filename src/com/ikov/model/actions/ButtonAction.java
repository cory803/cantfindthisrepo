package com.ikov.model.actions;

import com.ikov.world.entity.impl.player.Player;

/**
 * Represents a button click performed by the player.
 * @author Blake
 *
 */
public interface ButtonAction {
	
	public void handle(Player player);

}
