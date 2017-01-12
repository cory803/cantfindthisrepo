package com.runelive.world.content.combat.strategy.impl;

import com.runelive.model.Animation;
import com.runelive.model.Projectile;
import com.runelive.util.Misc;
import com.runelive.world.content.combat.CombatContainer;
import com.runelive.world.content.combat.CombatType;
import com.runelive.world.content.combat.strategy.CombatStrategy;
import com.runelive.world.entity.impl.Character;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;

/**
 * Vados 14/05/2016 22:40PM
 */

public class Skotizo implements CombatStrategy {

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		NPC skotizo = (NPC) entity;
		if (skotizo.isChargingAttack() || skotizo.getConstitution() <= 0) {
			return true;
		}
		Player target = (Player) victim;
		int chance = Misc.inclusiveRandom(1, 3);
		if(chance == 2) {
			new Projectile(skotizo, target, 1067, 44, 3, 43, 30, 0).sendProjectile();
			skotizo.performAnimation(new Animation(skotizo.getDefinition().getAttackAnimation()));
			skotizo.getCombatBuilder().setContainer(new CombatContainer(skotizo, victim, 1, 0, CombatType.MAGIC, true));
		} else {
			skotizo.performAnimation(new Animation(skotizo.getDefinition().getAttackAnimation()));
			skotizo.getCombatBuilder().setContainer(new CombatContainer(skotizo, victim, 1, 0, CombatType.MELEE, true));
		}
		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 2;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}
}
