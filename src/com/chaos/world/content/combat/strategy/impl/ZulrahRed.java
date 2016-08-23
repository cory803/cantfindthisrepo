package com.chaos.world.content.combat.strategy.impl;

import com.chaos.engine.task.Task;
import com.chaos.engine.task.TaskManager;
import com.chaos.model.Position;
import com.chaos.world.content.combat.CombatContainer;
import com.chaos.world.content.combat.CombatType;
import com.chaos.world.content.combat.HitQueue.CombatHit;
import com.chaos.world.content.combat.strategy.CombatStrategy;
import com.chaos.world.content.minigames.impl.zulrah.Zulrah;
import com.chaos.world.entity.impl.Character;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;

/**
 * @author Jonathan Sirens
 */

public class ZulrahRed implements CombatStrategy {

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
		System.out.println("Red");
		NPC zulrah = (NPC) entity;
		if (zulrah.isChargingAttack() || zulrah.getConstitution() <= 0) {
			return true;
		}
		if(victim.isPlayer()) {
			Player player = (Player) victim;
			TaskManager.submit(new Task(1, player, false) {
				int tick = 0;
				Position position = new Position(0,0);
				@Override
				public void execute() {
					if(tick == 0) {
						zulrah.setChargingAttack(true);
						position = player.getPosition();
						zulrah.setPositionToFace(position);
					}
					if(tick == 3) {
						zulrah.setPositionToFace(position);
						zulrah.performAnimation(Zulrah.LUNG);
						System.out.println("Pos x: "+position.getX()+" pos y: "+position.getY());
						if(player.getPosition().getX() == position.getX() && player.getPosition().getY() == position.getY()) {
							new CombatHit(zulrah.getCombatBuilder(), new CombatContainer(zulrah, player, 1, CombatType.MELEE, true)).handleAttack();
						}
					}
					if(tick == 7) {
						((Player) victim).getZulrah().next();
						zulrah.setChargingAttack(false);
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
		return 7;
	}

	@Override
	public int attackDistance(Character entity) {
		return 15;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}
}
