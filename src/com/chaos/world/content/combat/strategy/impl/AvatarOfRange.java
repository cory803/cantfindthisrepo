package com.chaos.world.content.combat.strategy.impl;

import com.chaos.engine.task.Task;
import com.chaos.engine.task.TaskManager;
import com.chaos.model.Animation;
import com.chaos.model.Projectile;
import com.chaos.util.Misc;
import com.chaos.world.content.combat.CombatContainer;
import com.chaos.world.content.combat.CombatType;
import com.chaos.world.content.combat.HitQueue.CombatHit;
import com.chaos.world.content.combat.range.CombatRangedAmmo;
import com.chaos.world.content.combat.strategy.CombatStrategy;
import com.chaos.world.entity.impl.Character;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;

/**
 * @author Jonathan Sirens
 */

public class AvatarOfRange implements CombatStrategy {

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
		NPC avatar = (NPC) entity;
		if (avatar.isChargingAttack() || avatar.getConstitution() <= 0) {
			return true;
		}
		avatar.performAnimation(new Animation(1843));
		avatar.setChargingAttack(true);
		Player target = (Player) victim;
		TaskManager.submit(new Task(1, target, false) {
			int tick = 0;
			@Override
			public void execute() {
				if(tick == 1) {
					for (Player t : Misc.getCombinedPlayerList(target)) {
						CombatRangedAmmo.AmmunitionData ammo = CombatRangedAmmo.AmmunitionData.ICE_ARROW;
						new Projectile(avatar, victim, 995, ammo.getProjectileDelay() + 16,
								ammo.getProjectileSpeed(), 43, 37, 0).sendProjectile();
					}
				}
				if(tick == 2) {
					for (Player t : Misc.getCombinedPlayerList(target)) {
						avatar.getCombatBuilder().setVictim(t);
						new CombatHit(avatar.getCombatBuilder(), new CombatContainer(avatar, t, 1, CombatType.RANGED, true)).handleAttack();
					}
					avatar.setChargingAttack(false);
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
		return 7;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}
}
