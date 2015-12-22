package com.ikov.world.content.combat.strategy.impl;

import com.ikov.engine.task.Task;
import com.ikov.engine.task.TaskManager;
import com.ikov.model.Animation;
import com.ikov.model.Graphic;
import com.ikov.model.Locations;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.model.Projectile;
import com.ikov.util.Misc;
import com.ikov.world.World;
import com.ikov.world.content.combat.CombatContainer;
import com.ikov.world.content.combat.CombatType;
import com.ikov.world.content.combat.strategy.CombatStrategy;
import com.ikov.world.entity.impl.Character;
import com.ikov.world.entity.impl.npc.NPC;
import com.ikov.world.content.minigames.impl.Zulrah;
import com.ikov.model.Position;

public class Zulrah_Green implements CombatStrategy {

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
		//zulrah.forceChat("Embrace darkness!");
		NPC zulrah = (NPC)entity;
		killer = (Player)victim;
		if(victim.getConstitution() <= 0 || victim.getConstitution() <= 0) {
			return true;
		}
		if(killer.getZulrahTime() <= 0) {
			killer.setZulrahTime(200);
		}
		int rotation_switch_chance = Misc.getRandom(40);
		boolean rotate = false;
		if(killer.getZulrahTime() < 50) {
			if(killer.getZulrahTime() - rotation_switch_chance < 0) {
				rotate = true;
			}
		}
		if(rotate) {
			zulrah.performAnimation(go_down);
			killer.setRotatingZulrah(true);
			if(killer.getZulrahMovement() == 0) {
				killer.addZulrahMovement(1);
			}
			if(killer.getZulrahMovement() == 12 && killer.getZulrahRotation() != 2) {
				killer.setZulrahMovement(1);
			} else if(killer.getZulrahMovement() == 11 && killer.getZulrahRotation() == 2) {
				killer.setZulrahMovement(1);
			}
			zulrah.getCombatBuilder().setContainer(new CombatContainer(zulrah, victim, 1, 5, CombatType.RANGED, true));
			killer.setZulrahHealth(zulrah.getConstitution());
			TaskManager.submit(new Task(2, zulrah, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 3) {
						if(killer.getZulrahRotation() >= 0) {
							if(killer.getZulrahMovement() == 1) {
								killer.setZulrahHealth(zulrah.getConstitution());
								killer.addZulrahMovement(1);
								killer.setZulrahMoving(true);
								zulrah.appendDeath();
								NPC new_zulrah_form = new NPC(Zulrah.ZULRAH_RED_NPC_ID, new Position(2269, 3075, killer.getPosition().getZ())).setSpawnedFor(killer);
								World.register(new_zulrah_form);
								killer.setZulrahMoving(false);
								new_zulrah_form.performAnimation(go_up);
							}
						}
						stop();
					}
					tick++;
				}
			});
		} else {
			zulrah.performAnimation(shoot);
			zulrah.getCombatBuilder().setContainer(new CombatContainer(zulrah, victim, 1, 5, CombatType.RANGED, true));
			killer.setZulrahHealth(zulrah.getConstitution());
			TaskManager.submit(new Task(2, zulrah, false) {
				int tick = 0;
				@Override
				public void execute() {
					switch(tick) {
					case 1:
						new Projectile(zulrah, victim, 1044, 44, 3, 43, 31, 0).sendProjectile();
						stop();
						break;
					}
					tick++;
				}
			});
		}
		return false;
	}
	
	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 20;
	}
	
	private static Player killer = null;
	private static final Animation shoot = new Animation(5069);
	private static final Animation go_down = new Animation(5072);
	private static final Animation go_up = new Animation(5073);
	private static final Graphic range_graphic = new Graphic(1044);
	
	@Override
	public CombatType getCombatType() {
		return CombatType.RANGED;
	}
}