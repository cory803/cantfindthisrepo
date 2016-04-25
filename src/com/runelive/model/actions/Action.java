package com.runelive.model.actions;

import com.runelive.world.entity.impl.player.Player;

/**
 * Represents an action performed by the player.
 * @author Blake
 *
 */
public interface Action {

    public void handle(Player player);

}
