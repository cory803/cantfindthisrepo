package com.runelive.world.content.combat.strategy.impl;

import com.runelive.model.Animation;
import com.runelive.util.Misc;
import com.runelive.world.content.combat.CombatContainer;
import com.runelive.world.content.combat.CombatType;
import com.runelive.world.content.combat.strategy.CombatStrategy;
import com.runelive.world.entity.impl.Character;
import com.runelive.world.entity.impl.npc.NPC;

/**
 * @author Jonathan Sirens
 */

public class HarLakkRiftsplitter implements CombatStrategy {

	/**
	 * Animations
	 */
	public Animation meleeHit = new Animation(14380);
	public Animation hugeMelee = new Animation(14384);

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
		NPC monster = (NPC) entity;
		if (monster.isChargingAttack() || monster.getConstitution() <= 0) {
			return true;
		}
		switch(Misc.inclusiveRandom(1, 4)) {
			/**
			 * Regular attack
			 */
			case 1:
				monster.performAnimation(meleeHit);
				monster.getCombatBuilder().setContainer(new CombatContainer(monster, victim, 1, 0, CombatType.MELEE, true));
				break;
			/**
			 * Melee bonus attack
			 */
			case 4:
				monster.performAnimation(hugeMelee);
				monster.getCombatBuilder().setContainer(new CombatContainer(monster, victim, 2, 1, CombatType.MELEE, true));
				break;
		}
		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 7;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}
}
