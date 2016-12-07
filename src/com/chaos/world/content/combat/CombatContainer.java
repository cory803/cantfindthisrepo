package com.chaos.world.content.combat;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

import com.chaos.engine.task.Task;
import com.chaos.engine.task.TaskManager;
import com.chaos.model.CombatIcon;
import com.chaos.model.Hit;
import com.chaos.model.Hitmask;
import com.chaos.util.Misc;
import com.chaos.world.content.Achievements;
import com.chaos.world.content.combat.weapon.CombatSpecial;
import com.chaos.world.entity.impl.Character;
import com.chaos.world.entity.impl.player.Player;

/**
 * A container that holds all of the data needed for a single combat hook.
 * 
 * @author lare96
 */
public class CombatContainer {

	/** The attacker in this combat hook. */
	private Character attacker;

	/** The victim in this combat hook. */
	private Character victim;

	/** The hits that will be dealt during this combat hook. */
	private ContainerHit[] hits;

	/** The skills that will be given experience. */
	private int[] experience;

	/** The combat type that is being used during this combat hook. */
	private CombatType combatType;

	/** If accuracy should be taken into account. */
	private boolean checkAccuracy;

	/** If at least one hit in this container is accurate. */
	private boolean accurate;

	/** The modified damage, used for bolt effects etc **/
	private int modifiedDamage;

	/** The delay before the hit is executed **/
	private int hitDelay;

	/**
	 * Create a new {@link CombatContainer}.
	 * 
	 * @param attacker
	 *            the attacker in this combat hook.
	 * @param victim
	 *            the victim in this combat hook.
	 * @param hitAmount
	 *            the amount of hits to deal this combat hook.
	 * @param hitType
	 *            the combat type that is being used during this combat hook
	 * @param checkAccuracy
	 *            if accuracy should be taken into account.
	 */
	public CombatContainer(Character attacker, Character victim, int hitAmount, CombatType hitType,
			boolean checkAccuracy) {
		this.attacker = attacker;
		this.victim = victim;
		this.combatType = hitType;
		this.checkAccuracy = checkAccuracy;
		this.hits = prepareHits(hitAmount);
		this.experience = getSkills(hitType);
		this.hitDelay = hitType == CombatType.MELEE ? 0
				: hitType == CombatType.RANGED ? 1
						: hitType == CombatType.MAGIC || hitType == CombatType.DRAGON_FIRE ? 1 : 1;
		attacker.setLastCombatType(hitType);
	}

	public CombatContainer(Character attacker, Character victim, int hitAmount, int hitDelay, CombatType hitType,
			boolean checkAccuracy) {
		this.attacker = attacker;
		this.victim = victim;
		this.combatType = hitType;
		this.checkAccuracy = checkAccuracy;
		this.hits = prepareHits(hitAmount);
		this.experience = getSkills(hitType);
		this.hitDelay = hitDelay;
		attacker.setLastCombatType(hitType);
	}

	/**
	 * Create a new {@link CombatContainer} that will deal no hit this turn.
	 * Used for things like spells that have special effects but don't deal
	 * damage.
	 * 
	 * @param checkAccuracy
	 *            if accuracy should be taken into account.
	 */
	public CombatContainer(Character attacker, Character victim, CombatType hitType, boolean checkAccuracy) {
		this(attacker, victim, 0, hitType, checkAccuracy);
	}

	/**
	 * Prepares the hits that will be dealt this combat hook.
	 * 
	 * @param hitAmount
	 *            the amount of hits to deal, maximum 4 and minimum 0.
	 * @return the hits that will be dealt this combat hook.
	 */
	private final ContainerHit[] prepareHits(int hitAmount) {

		// Check the hit amounts.
		if (hitAmount > 4) {
			throw new IllegalArgumentException("Illegal number of hits! The maximum number of hits per turn is 4.");
		} else if (hitAmount < 0) {
			throw new IllegalArgumentException("Illegal number of hits! The minimum number of hits per turn is 0.");
		}

		// No hit for this turn, but we still need to calculate accuracy.
		if (hitAmount == 0) {
			accurate = checkAccuracy ? CombatFactory.rollAccuracy(attacker, victim, combatType) : true;
			return new ContainerHit[] {};
		}

		// Create the new array of hits, and populate it. Here we do the maximum
		// hit and accuracy calculations.
		ContainerHit[] array = new ContainerHit[hitAmount];
		for (int i = 0; i < array.length; i++) {
			boolean accuracy = checkAccuracy ? CombatFactory.rollAccuracy(attacker, victim, combatType) : true;
			array[i] = new ContainerHit(CombatFactory.getHit(attacker, victim, combatType), accuracy);
			if (array[i].isAccurate()) {
				accurate = true;
			}
		}

		/** SPECS **/

		if (attacker.isPlayer() && ((Player) attacker).isSpecialActivated()) {
			if (((Player) attacker).getCombatSpecial() == CombatSpecial.DRAGON_CLAWS && hitAmount == 4) {
				int total = array[0].getHit().getDamage();
				int totalPossible = DesolaceFormulas.calculateMaxMeleeHit(attacker, victim);
				//System.out.println("Total possible in dragon claw spec: "+totalPossible);
				array[0].getHit().setDamage(total / 2);
				array[1].getHit().setDamage(total / 4);
				array[2].getHit().setDamage(total / 8);
				array[3].getHit().setDamage(total / 8);

				if(total >= 700) {
					Achievements.finishAchievement((Player) attacker, Achievements.AchievementData.HIT_700_WITH_SPECIAL_ATTACK);
				}
			} else if (((Player) attacker).getCombatSpecial() == CombatSpecial.DARK_BOW && hitAmount == 2) {
				for (int i = 0; i < hitAmount; i++) {
					if (array[i].getHit().getDamage() < 80) {
						array[i].getHit().setDamage(80);
					}
					array[i].setAccurate(true);
				}
			}
			int total = 0;
			for (int i = 0; i < hitAmount; i++) {
				total += array[i].getHit().getDamage();
			}
			if(total >= 700) {
				Achievements.finishAchievement((Player) attacker, Achievements.AchievementData.HIT_700_WITH_SPECIAL_ATTACK);
			}
		}
		return array;
	}

	public void setHits(ContainerHit[] hits) {
		this.hits = hits;
		prepareHits(hits.length);
	}

	/**
	 * Performs an action on every single hit in this container. the action to
	 * perform on every single hit.
	 */
	protected final void allHits(Consumer<ContainerHit> c) {
		Arrays.stream(hits).filter(Objects::nonNull).forEach(c);
	}

	public final int getDamage() {
		int damage = 0;
		for (ContainerHit hit : hits) {
			if (hit == null)
				continue;
			boolean bypass = false;
			if(attacker.isPlayer()) {
				Player player = (Player)attacker;
				if(player.getCombatSpecial() == CombatSpecial.DRAGON_CLAWS || player.getCombatSpecial() == CombatSpecial.KORASIS_SWORD) {
					bypass = true;
				}
			}
			if (!hit.accurate && !bypass) {
				int absorb = hit.getHit().getAbsorb();
				hit.hit = new Hit(0, Hitmask.RED, CombatIcon.BLOCK);
				hit.hit.setAbsorb(absorb);
			}
			damage += hit.hit.getDamage();
		}
		if(attacker.isPlayer()) {
			Player player = (Player) attacker;
			if (player.getCombatSpecial() == CombatSpecial.KORASIS_SWORD) {
				if(damage <= 150) {
					damage = Misc.inclusiveRandom(150, 250);
				}
			}
		}
		return damage;
	}

	public final void dealDamage(CombatBuilder builder, CombatContainer container) {
		TaskManager.submit(new Task(1, attacker, false) {
			int tick = 0;
			@Override
			public void execute() {
				tick++;
				if(tick == 1) {

					CombatFactory.applyPrayerProtection(container, builder);

					if (hits.length == 1) {
						victim.dealDamage(attacker, hits[0].getHit());
					} else if (hits.length == 2) {
						victim.dealDoubleDamage(attacker, hits[0].getHit(), hits[1].getHit());
					} else if (hits.length == 3) {
						victim.dealTripleDamage(attacker, hits[0].getHit(), hits[1].getHit(), hits[2].getHit());
					} else if (hits.length == 4) {
						victim.dealQuadrupleDamage(attacker, hits[0].getHit(), hits[1].getHit(), hits[2].getHit(), hits[3].getHit());
					}
					stop();
				}
			}
		});
	}

	public int getTotalDamage() {
		int total = 0;
		if (hits.length == 1) {
			return hits[0].getHit().getDamage();
		} else if (hits.length == 2) {
			return hits[0].getHit().getDamage() + hits[1].getHit().getDamage();
		} else if (hits.length == 3) {
			return hits[0].getHit().getDamage() + hits[1].getHit().getDamage() + hits[2].getHit().getDamage();
		} else if (hits.length == 4) {
			return hits[0].getHit().getDamage() + hits[1].getHit().getDamage() + hits[2].getHit().getDamage() + hits[3].getHit().getDamage();
		}
		return hits[0].getHit().getDamage();
	}

	/**
	 * Gets all of the skills that will be trained.
	 * 
	 * @param type
	 *            the combat type being used.
	 * 
	 * @return an array of skills that this attack will train.
	 */
	private final int[] getSkills(CombatType type) {
		if (attacker.isNpc()) {
			return new int[] {};
		}
		return ((Player) attacker).getFightType().getStyle().skill(type);
	}

	public void setModifiedDamage(int modifiedDamage) {
		this.modifiedDamage = modifiedDamage;
	}

	public int getModifiedDamage() {
		return modifiedDamage;
	}

	/**
	 * A dynamic method invoked when the victim is hit with an attack. An
	 * example of usage is using this to do some sort of special effect when the
	 * victim is hit with a spell. <b>Do not reset combat builder in this
	 * method!</b>
	 * 
	 * @param damage
	 *            the damage inflicted with this attack, always 0 if the attack
	 *            isn't accurate.
	 * @param accurate
	 *            if the attack is accurate.
	 */
	public void onHit(int damage, boolean accurate) {
	}

	/**
	 * Gets the hits that will be dealt during this combat hook.
	 * 
	 * @return the hits that will be dealt during this combat hook.
	 */
	public final ContainerHit[] getHits() {
		return hits;
	}

	/**
	 * Gets the skills that will be given experience.
	 * 
	 * @return the skills that will be given experience.
	 */
	public final int[] getExperience() {
		return experience;
	}

	/**
	 * Sets the amount of hits that will be dealt during this combat hook.
	 * 
	 * @param hitAmount
	 *            the amount of hits that will be dealt during this combat hook.
	 */
	public final void setHitAmount(int hitAmount) {
		this.hits = prepareHits(hitAmount);
	}

	/**
	 * Gets the combat type that is being used during this combat hook.
	 * 
	 * @return the combat type that is being used during this combat hook.
	 */
	public final CombatType getCombatType() {
		return combatType;
	}

	/**
	 * Sets the combat type that is being used during this combat hook.
	 * 
	 * @param combatType
	 *            the combat type that is being used during this combat hook.
	 */
	public final void setCombatType(CombatType combatType) {
		this.combatType = combatType;
	}

	/**
	 * Gets if accuracy should be taken into account.
	 * 
	 * @return true if accuracy should be taken into account.
	 */
	public final boolean isCheckAccuracy() {
		return checkAccuracy;
	}

	/**
	 * Sets if accuracy should be taken into account.
	 * 
	 * @param checkAccuracy
	 *            true if accuracy should be taken into account.
	 */
	public final void setCheckAccuracy(boolean checkAccuracy) {
		this.checkAccuracy = checkAccuracy;
	}

	/**
	 * Gets if at least one hit in this container is accurate.
	 * 
	 * @return true if at least one hit in this container is accurate.
	 */
	public final boolean isAccurate() {
		return accurate;
	}

	/**
	 * Gets the hit delay before the hit is executed.
	 * 
	 * @return the hit delay.
	 */
	public int getHitDelay() {
		return hitDelay;
	}

	/**
	 * A single hit that is dealt during a combat hook.
	 * 
	 * @author lare96
	 */
	public static class ContainerHit {

		/** The actual hit that will be dealt. */
		private Hit hit;

		/** The accuracy of the hit to be dealt. */
		private boolean accurate;

		/**
		 * Create a new {@link ContainerHit}.
		 * 
		 * @param hit
		 *            the actual hit that will be dealt.
		 * @param accurate
		 *            the accuracy of the hit to be dealt.
		 */
		public ContainerHit(Hit hit, boolean accurate) {
			this.hit = hit;
			this.accurate = accurate;
		}

		/**
		 * Gets the actual hit that will be dealt.
		 * 
		 * @return the actual hit that will be dealt.
		 */
		public Hit getHit() {
			return hit;
		}

		/**
		 * Sets the actual hit that will be dealt.
		 * 
		 * @param hit
		 *            the actual hit that will be dealt.
		 */
		public void setHit(Hit hit) {
			this.hit = hit;
		}

		/**
		 * Gets the accuracy of the hit to be dealt.
		 * 
		 * @return the accuracy of the hit to be dealt.
		 */
		public boolean isAccurate() {
			return accurate;
		}

		/**
		 * Sets the accuracy of the hit to be dealt.
		 * 
		 * @param accurate
		 *            the accuracy of the hit to be dealt.
		 */
		public void setAccurate(boolean accurate) {
			this.accurate = accurate;
		}
	}
}
