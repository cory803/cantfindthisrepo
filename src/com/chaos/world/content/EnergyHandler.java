package com.chaos.world.content;

import com.chaos.model.Animation;
import com.chaos.model.Flag;
import com.chaos.world.entity.impl.player.Player;

/**
 * Handles a player's run energy
 * 
 * @author Gabriel Hannason Thanks to Russian for formula!
 */
public class EnergyHandler {

	public static void rest(Player player) {
		if (player.busy() || player.getCombatBuilder().isBeingAttacked() || player.getCombatBuilder().isAttacking()) {
			player.getPacketSender().sendMessage("You cannot do this right now.");
			return;
		}
		player.getWalkingQueue().clear();
		player.setResting(true);

		player.performAnimation(new Animation(11786));
		player.getCharacterAnimations().setStandingAnimation(2034);
		player.getUpdateFlag().flag(Flag.APPEARANCE);
		player.getPacketSender().sendMessage("You begin to rest...");
	}
}
