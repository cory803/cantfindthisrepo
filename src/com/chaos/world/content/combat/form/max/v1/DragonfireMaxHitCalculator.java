package com.chaos.world.content.combat.form.max.v1;

import com.chaos.world.content.combat.form.max.MaxHitCalculator;
import com.chaos.world.entity.impl.Character;
import com.chaos.world.entity.impl.player.Player;

/**
 * An implementation of {@link MaxHitCalculator} used for our {@link org.niobe.model.AttackType#DRAGON_FIRE}
 * attack type.
 *
 * @author Relex
 */
public final class DragonfireMaxHitCalculator implements MaxHitCalculator {

	@Override
	public int getMaxHit(Character source, Character victim) {
		//TODO: get the magic bonus for players (if we ever decide to use player dragon-fire attacks)
		//and get combat level/magic level for mobs to decide max hit
		int maxHit = 320;
		return maxHit;
	}
}
