package com.runelive.world.content.combat.strategy.impl;

import com.runelive.engine.task.Task;
import com.runelive.engine.task.TaskManager;
import com.runelive.model.Animation;
import com.runelive.model.Projectile;
import com.runelive.world.content.combat.CombatContainer;
import com.runelive.world.content.combat.CombatType;
import com.runelive.world.content.combat.strategy.CombatStrategy;
import com.runelive.world.entity.impl.Character;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;

public class Geerin implements CombatStrategy {

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return victim.isPlayer()
				&& ((Player) victim).getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom();
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		NPC geerin = (NPC) entity;
		if (geerin.isChargingAttack() || victim.getConstitution() <= 0 || geerin.getConstitution() <= 0) {
			return true;
		}

		geerin.performAnimation(new Animation(geerin.getDefinition().getAttackAnimation()));
		geerin.setChargingAttack(true);

		geerin.getCombatBuilder().setContainer(new CombatContainer(geerin, victim, 1, 3, CombatType.RANGED, true));

		TaskManager.submit(new Task(1, geerin, false) {
			int tick = 0;

			@Override
			public void execute() {
				if (tick == 1) {
					new Projectile(geerin, victim, 1837, 44, 3, 43, 43, 0).sendProjectile();
					geerin.setChargingAttack(false);
					stop();
				}
				tick++;
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
		return 6;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.RANGED;
	}
}
