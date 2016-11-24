package com.chaos.world.content.combat.strategy.impl;

import com.chaos.engine.task.Task;
import com.chaos.engine.task.TaskManager;
import com.chaos.model.Animation;
import com.chaos.model.Graphic;
import com.chaos.model.GraphicHeight;
import com.chaos.util.Misc;
import com.chaos.world.content.combat.CombatContainer;
import com.chaos.world.content.combat.CombatType;
import com.chaos.world.content.combat.HitQueue.CombatHit;
import com.chaos.world.content.combat.strategy.CombatStrategy;
import com.chaos.world.entity.impl.Character;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;

/**
 * Vados 14/05/2016 22:40PM
 */

public class Venenatis implements CombatStrategy {

	private static final Animation attack_anim = new Animation(5327);
	private static final Graphic graphic1 = new Graphic(1200, GraphicHeight.MIDDLE);

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
		NPC venantis = (NPC) entity;
		if (venantis.isChargingAttack() || venantis.getConstitution() <= 0) {
			return true;
		}
		venantis.performAnimation(new Animation(venantis.getDefinition().getAttackAnimation()));
		venantis.getCombatBuilder().setContainer(new CombatContainer(venantis, victim, 1, 0, CombatType.MELEE, true));
		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 12;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
