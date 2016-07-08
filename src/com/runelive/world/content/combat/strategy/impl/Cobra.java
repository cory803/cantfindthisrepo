package com.runelive.world.content.combat.strategy.impl;

import com.runelive.engine.task.Task;
import com.runelive.engine.task.TaskManager;
import com.runelive.model.Animation;
import com.runelive.model.Graphic;
import com.runelive.world.content.combat.CombatContainer;
import com.runelive.world.content.combat.CombatType;
import com.runelive.world.content.combat.HitQueue.CombatHit;
import com.runelive.world.content.combat.strategy.CombatStrategy;
import com.runelive.world.entity.impl.Character;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;

/**
 * @author Jonathan Sirens
 */

public class Cobra implements CombatStrategy {

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
		NPC cobra = (NPC) entity;
		if (cobra.isChargingAttack() || cobra.getConstitution() <= 0) {
			return true;
		}
		cobra.performAnimation(new Animation(1979));
		cobra.setChargingAttack(true);
		Player target = (Player) victim;
		TaskManager.submit(new Task(2, target, false) {
			@Override
			public void execute() {
				cobra.getCombatBuilder().setVictim(target);
				if (victim.isFrozen()) {
					victim.performGraphic(new Graphic(1677));
				} else {
					victim.performGraphic(new Graphic(369));
					victim.getMovementQueue().freeze(15);
				}
				new CombatHit(cobra.getCombatBuilder(), new CombatContainer(cobra, target, 1, CombatType.MAGIC, true))
						.handleAttack();
				cobra.setChargingAttack(false);
				stop();
			}
		});
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
		return CombatType.MAGIC;
	}
}
