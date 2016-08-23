package com.chaos.world.content.combat.strategy.impl;

import com.chaos.engine.task.Task;
import com.chaos.engine.task.TaskManager;
import com.chaos.model.*;
import com.chaos.util.Misc;
import com.chaos.world.content.combat.CombatContainer;
import com.chaos.world.content.combat.CombatType;
import com.chaos.world.content.combat.HitQueue.CombatHit;
import com.chaos.world.content.combat.prayer.CurseHandler;
import com.chaos.world.content.combat.prayer.PrayerHandler;
import com.chaos.world.content.combat.strategy.CombatStrategy;
import com.chaos.world.entity.impl.Character;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;

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
		if (!Locations.goodDistance(bork.getPosition().copy(), victim.getPosition().copy(), 1)) {
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
						if(target.getConstitution() <= 100 && target.getConstitution() > 0) {
							target.dealDamage(null, new Hit(target.getConstitution(), Hitmask.RED2, CombatIcon.NONE));
							bork.forceChat("Hehehehe poisoned");
						}
						int random = Misc.random(5, 10);
						int random2 = Misc.random(5, 10);
						if (PrayerHandler.isActivated(target, PrayerHandler.PROTECT_FROM_MISSILES)
								|| PrayerHandler.isActivated(target, PrayerHandler.PROTECT_FROM_MELEE)
								|| CurseHandler.isActivated(target, CurseHandler.CurseData.DEFLECT_MELEE.ordinal())
								|| CurseHandler.isActivated(target, CurseHandler.CurseData.DEFLECT_MISSILES.ordinal())) {
							if (random == 7) {
								if (target.getConstitution() > 0) {
									String[] forceChats = {
											"Haha very sneaky!",
											"Do you think I am a fool?",
											"I see your prayer!",
											"Bow to me, I am a god!",
											"Protection hmm? Take that!",
									};
									target.dealDamage(null, new Hit(Misc.random(25, 100), Hitmask.DARK_PURPLE, CombatIcon.NONE));
									bork.forceChat(forceChats[(int) (Math.random() * forceChats.length)]);
									PrayerHandler.resetPrayers(target, PrayerHandler.OVERHEAD_PRAYERS, -1);
									CurseHandler.deactivateCurses(target, CurseHandler.OVERHEAD_CURSES);
								}
							}
						}
						if(random2 == 7 && random != 7 && !PrayerHandler.isActivated(target, PrayerHandler.PROTECT_FROM_MISSILES)
								|| !PrayerHandler.isActivated(target, PrayerHandler.PROTECT_FROM_MELEE)
								|| !CurseHandler.isActivated(target, CurseHandler.CurseData.DEFLECT_MELEE.ordinal())
								|| !CurseHandler.isActivated(target, CurseHandler.CurseData.DEFLECT_MISSILES.ordinal())) {
								String[] forceChats = {
										"You will suffer the pain of 10 deaths!",
										"I will kill you!",
										"Misery will come upon you!",
										"Dieeeeeeeeeee!!!",
										"You think you can take my items that easily???",
										"Darkness will strike you!",
										"Are you a true warrior? Prove it!!",
										"Grrrrrrrrrrrrrrrrrrrrrrr!!!",
								};
								bork.forceChat(forceChats[(int) (Math.random() * forceChats.length)]);
						}
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