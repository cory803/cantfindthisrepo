package com.ikov.world.content.combat;

import com.ikov.model.Locations;
import com.ikov.model.Locations.Location;
import com.ikov.world.entity.impl.Character;

public class CombatDistanceSession {

	/** The combat builder. */
	private CombatBuilder builder;

	/** The victim being hunted. */
	private Character victim;

	/**
	 * Create a new {@link CombatDistanceSession}.
	 * 
	 * @param builder
	 *            the combat builder.
	 * @param victim
	 *            the victim being hunted.
	 */
	public CombatDistanceSession(CombatBuilder builder, Character victim) {
		this.builder = builder;
		this.victim = victim;
	}

	public void process() {
		
		builder.determineStrategy();
		builder.attackTimer = 0;
		builder.cooldown = 0;

		if(builder.getVictim() != null && !builder.getVictim().equals(victim)) {
			builder.reset(true);
			this.stop();
			return;
		}
		
		if(!Location.ignoreFollowDistance(builder.getCharacter())) {
			if (!builder.getCharacter().getPosition().isViewableFrom(victim.getPosition())) {
				builder.reset(true);
				this.stop();
				return;
			}
		}
		
		if(Locations.goodDistance(builder.getCharacter().getPosition(), victim.getPosition(), builder.getStrategy().attackDistance(builder.getCharacter()))) {
			sucessFul();
			this.stop();
			return;
		}
	}
	
	public void stop() {
		builder.setDistanceSession(null);
	}
	
	public void sucessFul() {
		builder.getCharacter().getMovementQueue().reset();
		builder.setVictim(victim);
        builder.setCombatSession(new CombatSession(builder));
	}
}
