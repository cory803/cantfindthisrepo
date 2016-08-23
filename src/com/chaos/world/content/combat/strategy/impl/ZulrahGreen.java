package com.chaos.world.content.combat.strategy.impl;

import com.chaos.util.Misc;
import com.chaos.world.content.combat.CombatContainer;
import com.chaos.world.content.combat.CombatType;
import com.chaos.world.content.combat.strategy.CombatStrategy;
import com.chaos.world.entity.impl.Character;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;

/**
 * @author Jonathan Sirens
 */

public class ZulrahGreen implements CombatStrategy {

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
		System.out.println("Green");
		NPC zulrah = (NPC) entity;
		Player player = (Player) victim;
		if (player.getZulrah().isMovingToNextStage() || zulrah.getConstitution() <= 0) {
			return true;
		}
		if(victim.isPlayer()) {
			int chance = Misc.random(1, 4);
			switch(chance) {
				//Shoot with range
				case 1:
				case 2:
				case 3:
				case 4:
					player.getZulrah().next();
					/*
					TaskManager.submit(new Task(1, player, false) {
						int tick = 0;
						@Override
						public void execute() {
							if(tick == 0) {
								zulrah.setChargingAttack(true);
								zulrah.performAnimation(Zulrah.SHOOT);
							}
							if(tick == 1) {
								new Projectile(entity, victim, Zulrah.RANGE_BOLT.getId(), 70, 0, 47, 31, 0).sendProjectile();
							}
							
							if(tick == 2) {
								new CombatHit(zulrah.getCombatBuilder(), new CombatContainer(zulrah, player, 1, CombatType.RANGED, true)).handleAttack();
							}
							if(tick == 4) {
								//Zulrah.next(player, zulrah);
								zulrah.setChargingAttack(false);
								stop();
							}
							tick++;
						}
					});
					*/
					break;
				
			}
		}
		return true;

	}

	@Override
	public int attackDelay(Character entity) {
		return 3;
	}

	@Override
	public int attackDistance(Character entity) {
		return 30;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.RANGED;
	}
}
