package com.runelive.world.content.combat.strategy.impl;

import com.runelive.engine.task.Task;
import com.runelive.engine.task.TaskManager;
import com.runelive.model.Animation;
import com.runelive.model.Projectile;
import com.runelive.world.content.combat.CombatContainer;
import com.runelive.world.content.combat.CombatType;
import com.runelive.world.content.combat.HitQueue.CombatHit;
import com.runelive.world.content.combat.range.CombatRangedAmmo.AmmunitionData;
import com.runelive.world.content.combat.strategy.CombatStrategy;
import com.runelive.world.entity.impl.Character;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;

/**
 * @author Jonathan Sirens
 */

public class Sagittare implements CombatStrategy {

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
		NPC fear = (NPC) entity;
		if (fear.isChargingAttack() || fear.getConstitution() <= 0) {
			return true;
		}
		fear.performAnimation(new Animation(13276));
		fear.setChargingAttack(true);
		Player target = (Player) victim;
		TaskManager.submit(new Task(1, target, false) {
			@Override
			public void execute() {
				fear.getCombatBuilder().setVictim(target);
				AmmunitionData ammo = AmmunitionData.ICE_ARROW;
				new Projectile(fear, victim, ammo.getProjectileId(), ammo.getProjectileDelay() + 16,
						ammo.getProjectileSpeed() + 15, ammo.getStartHeight(), ammo.getEndHeight(), 0).sendProjectile();
				new CombatHit(fear.getCombatBuilder(), new CombatContainer(fear, target, 1, CombatType.RANGED, true))
						.handleAttack();
				fear.setChargingAttack(false);
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
		return CombatType.RANGED;
	}
}
