package com.chaos.world.content.combat.strategy.impl;

import com.chaos.engine.task.Task;
import com.chaos.engine.task.TaskManager;
import com.chaos.model.Animation;
import com.chaos.model.Graphic;
import com.chaos.world.content.combat.CombatContainer;
import com.chaos.world.content.combat.CombatType;
import com.chaos.world.content.combat.HitQueue.CombatHit;
import com.chaos.world.content.combat.strategy.CombatStrategy;
import com.chaos.world.entity.impl.Character;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;

/**
 * @author Jonathan Sirens
 */

public class Necrolord implements CombatStrategy {

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
					victim.getWalkingQueue().freeze(15);
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
