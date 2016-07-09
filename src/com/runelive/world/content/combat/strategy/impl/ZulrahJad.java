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
import com.runelive.world.content.minigames.impl.zulrah.Zulrah;
import com.runelive.world.entity.impl.Character;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;

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
						Zulrah.next((Player) victim, zulrah);
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
