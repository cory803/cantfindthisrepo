package com.chaos.world.content.combat.strategy.impl;

import com.chaos.engine.task.Task;
import com.chaos.engine.task.TaskManager;
import com.chaos.world.content.combat.CombatContainer;
import com.chaos.world.content.combat.CombatType;
import com.chaos.world.content.combat.strategy.CombatStrategy;
import com.chaos.world.entity.impl.Character;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;

/**
 * @author Jonathan Sirens
 */

public class ZulrahJad implements CombatStrategy {

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
		System.out.println("Jad");
		NPC zulrah = (NPC) entity;
		if (zulrah.isChargingAttack() || zulrah.getConstitution() <= 0) {
			return true;
		}
		if(victim.isPlayer()) {
			Player player = (Player) victim;
			TaskManager.submit(new Task(1, player, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 2) {
						((Player) victim).getZulrah().next();
						stop();
					}
					
					tick++;
				}
			});
		}
		/*
		fear.performAnimation(new Animation(426));
		fear.setChargingAttack(true);
		Player target = (Player) victim;
		TaskManager.submit(new Task(2, target, false) {
			@Override
			public void execute() {
				fear.getCombatBuilder().setVictim(target);
				AmmunitionData ammo = AmmunitionData.ICE_ARROW;
				new Projectile(fear, victim, ammo.getProjectileId(), ammo.getProjectileDelay() + 16,
						ammo.getProjectileSpeed(), ammo.getStartHeight(), ammo.getEndHeight(), 0).sendProjectile();
				new CombatHit(fear.getCombatBuilder(), new CombatContainer(fear, target, 1, CombatType.RANGED, true))
						.handleAttack();
				fear.setChargingAttack(false);
				stop();
			}
		});
		*/
		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		return 7;
	}

	@Override
	public int attackDistance(Character entity) {
		return 15;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}