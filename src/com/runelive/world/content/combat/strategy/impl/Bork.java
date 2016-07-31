package com.runelive.world.content.combat.strategy.impl;

import com.runelive.engine.task.Task;
import com.runelive.engine.task.TaskManager;
import com.runelive.model.Animation;
import com.runelive.model.Graphic;
import com.runelive.model.GraphicHeight;
import com.runelive.model.Locations;
import com.runelive.util.Misc;
import com.runelive.world.content.combat.CombatContainer;
import com.runelive.world.content.combat.CombatType;
import com.runelive.world.content.combat.HitQueue.CombatHit;
import com.runelive.world.content.combat.strategy.CombatStrategy;
import com.runelive.world.entity.impl.Character;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;

/**
 * Vados 14/05/2016 22:40PM
 */

public class Bork implements CombatStrategy {

	private static final Animation rangedAttack = new Animation(8757);
	private static final Graphic rangedGraphic = new Graphic(1005);

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
		NPC bork = (NPC) entity;
		if (bork.isChargingAttack() || bork.getConstitution() <= 0) {
			return true;
		}
		Player target = (Player) victim;
		CombatType style = CombatType.MELEE;
		int ran = Misc.random(5, 10);
		switch(ran) {
			case 5:
			case 6:
			case 7:
				style = CombatType.RANGED;
				break;
		}
		if (!Locations.goodDistance(bork.getPosition().copy(), victim.getPosition().copy(), 3)) {
			style = CombatType.RANGED;
		}
		if (style == CombatType.MELEE) {
			bork.performAnimation(new Animation(bork.getDefinition().getAttackAnimation()));
			bork.getCombatBuilder().setContainer(new CombatContainer(bork, victim, 1, 1, CombatType.MELEE, true));
		} else {
			bork.setChargingAttack(true);
			bork.performAnimation(rangedAttack);
			TaskManager.submit(new Task(1, bork, false) {
				int tick = 0;
				@Override
				public void execute() {
					bork.getCombatBuilder().setVictim(target);
					if(tick == 1) {
						bork.performGraphic(rangedGraphic);
					}
					if (tick == 2) {
						new CombatHit(bork.getCombatBuilder(), new CombatContainer(bork, target, 1, CombatType.RANGED, true))
								.handleAttack();
						bork.setChargingAttack(false);
						stop();
					}
					tick++;
				}
			});
		}
		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 3;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
