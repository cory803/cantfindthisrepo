package com.runelive.world.content.combat.strategy.impl;

import com.runelive.engine.task.Task;
import com.runelive.engine.task.TaskManager;
import com.runelive.model.Animation;
import com.runelive.model.Graphic;
import com.runelive.model.GraphicHeight;
import com.runelive.model.Locations;
import com.runelive.model.Locations.Location;
import com.runelive.model.Projectile;
import com.runelive.util.Misc;
import com.runelive.world.content.combat.CombatContainer;
import com.runelive.world.content.combat.CombatType;
import com.runelive.world.content.combat.HitQueue.CombatHit;
import com.runelive.world.content.combat.strategy.CombatStrategy;
import com.runelive.world.entity.impl.Character;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;

public class Graardor implements CombatStrategy {

	public static String[] randomMessage = {"Death to our enemies!", "Brargh!", "Break their bones!", "For the glory of Bandos!",
	"Split their skulls!", "We feast on the bones of our enemies tonight!", "CHAAARGE!", "All glory to Bandos!", "GRRRAAAAAR",
	"FOR THE GLORY OF THE BIG HIGH WAR GOD!"};

	public static String getRandomMessage() {
		return randomMessage[(int) (Math.random() * randomMessage.length)];
	}

	private static final Animation attack_anim = new Animation(7063);
	private static final Graphic graphic1 = new Graphic(1200, GraphicHeight.MIDDLE);

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
		int speechChance = Misc.inclusiveRandom(1, 4);
		NPC graardor = (NPC) entity;
		if (graardor.isChargingAttack() || graardor.getConstitution() <= 0) {
			return true;
		}
		CombatType style = Misc.getRandom(4) <= 1
				&& Locations.goodDistance(graardor.getPosition(), victim.getPosition(), 1) ? CombatType.MELEE
						: CombatType.RANGED;
		if (style == CombatType.MELEE) {
			graardor.performAnimation(new Animation(graardor.getDefinition().getAttackAnimation()));
			graardor.getCombatBuilder()
					.setContainer(new CombatContainer(graardor, victim, 1, 1, CombatType.MELEE, true));
			if(speechChance == 1) {
				graardor.forceChat(getRandomMessage());
			}
		} else {
			graardor.performAnimation(attack_anim);
			graardor.setChargingAttack(true);
			Player target = (Player) victim;
			for (Player t : Misc.getCombinedPlayerList(target)) {
				if (t == null || t.getLocation() != Location.GODWARS_DUNGEON || t.isTeleporting())
					continue;
				if (t.getPosition().distanceToPoint(graardor.getPosition().getX(), graardor.getPosition().getY()) > 20)
					continue;
				new Projectile(graardor, target, graphic1.getId(), 44, 3, 43, 43, 0).sendProjectile();
			}
			TaskManager.submit(new Task(2, target, false) {
				@Override
				public void execute() {
					for (Player t : Misc.getCombinedPlayerList(target)) {
						if (t == null || t.getLocation() != Location.GODWARS_DUNGEON)
							continue;
						graardor.getCombatBuilder().setVictim(t);
						new CombatHit(graardor.getCombatBuilder(),
								new CombatContainer(graardor, t, 1, CombatType.RANGED, true)).handleAttack();
						if(speechChance == 1) {
							graardor.forceChat(getRandomMessage());
						}
					}
					graardor.setChargingAttack(false);
					stop();
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
