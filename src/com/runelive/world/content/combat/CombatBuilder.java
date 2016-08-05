package com.runelive.world.content.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.runelive.model.Locations;
import com.runelive.model.action.distance.CombatFollowMobileAction;
import com.runelive.util.Misc;
import com.runelive.util.Stopwatch;
import com.runelive.world.content.combat.CombatContainer.ContainerHit;
import com.runelive.world.content.combat.strategy.CombatStrategy;
import com.runelive.world.entity.Entity;
import com.runelive.world.entity.impl.Character;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;

/**
 * Holds methods for running the entire combat process.
 *
 * @author lare96
 */
public class CombatBuilder {

	private Character character;
	private Character victim;
	private Character lastAttacker;

	private HitQueue hitQueue = new HitQueue();
	private CombatSession combatSession;
	private CombatDistanceSession distanceSession;
	private CombatContainer container;

	private Map<Player, CombatDamageCache> damageMap = new HashMap<>();
	private Stopwatch lastAttack = new Stopwatch();
	private CombatStrategy strategy;
	protected int attackTimer;
	protected int cooldown;
	private boolean retaliated;

	public CombatBuilder(Character entity) {
		this.character = entity;
	}

	public void process() {
		hitQueue.process();
		if (distanceSession != null) {
			distanceSession.process();
		}
		if (combatSession != null) {
			combatSession.process();
		}
	}

	public void attack(Character target) {
		if (character.equals(target)) {
			return;
		}
		if(target.isNpc()) {
			NPC npc = (NPC)target;
			if(npc.getSpawnedFor() != null) {
				if (npc.getSpawnedFor() != character) {
					if(character.isPlayer()) {
						Player player = (Player) character;
						player.getPacketSender().sendMessage("This monster was not spawned for you.");
						return;
					}
				}
			}
		}
		if (target.equals(victim)) {
			determineStrategy();

			if (!character.getPosition().equals(victim.getPosition()) && character.getPosition()
					.isWithinDistance(victim.getPosition(), strategy.attackDistance(character))) {
				character.getWalkingQueue().clear();
			}
		}

		if (character.isPlayer()) {
			((Player) character).getActionQueue().addAction(new CombatFollowMobileAction(((Player) character), target));
		}
		if (character.getInteractingEntity() != target)
			character.setEntityInteraction(target);

		// If the combat task is running, change targets.
		if (combatSession != null) {
			victim = target;

			if (lastAttacker == null || lastAttacker != victim) {
				setDidAutoRetaliate(false);
			}

			if (character.isPlayer()) {
				Player player = (Player) character;
				if (player.isAutocast() || player.getCastSpell() == null || attackTimer < 1) {
					cooldown = 0;
				}
			}
			return;
		}
		distanceSession = new CombatDistanceSession(this, target);
	}

	/**
	 * Resets this combat builder by discarding various values associated with
	 * the combat process.
	 */
	public void reset(boolean resetAttack) {
		victim = null;
		distanceSession = null;
		combatSession = null;
		container = null;
		if (resetAttack) {
			attackTimer = 0;
		}
		strategy = null;
		cooldown = 0;
		character.setEntityInteraction(null);
	}

	/**
	 * Starts the cooldown sequence.
	 */
	public void cooldown(boolean resetAttack) {

		// Check if we're even actively in combat.
		if (strategy == null)
			return;

		cooldown = 5;

		character.setEntityInteraction(null);

		// Reset attack timer if needed.
		if (resetAttack) {
			attackTimer = strategy.attackDelay(character);
		}
	}

	public void resetCooldown() {
		this.cooldown = 0;
	}

	/**
	 * Performs a search on the <code>damageMap</code> to find which
	 * {@link Player} dealt the most damage on this controller.
	 *
	 * @param clearMap
	 *            <code>true</code> if the map should be discarded once the
	 *            killer is found, <code>false</code> if no data in the map
	 *            should be modified.
	 * @return the player who killed this entity, or <code>null</code> if an npc
	 *         or something else killed this entity.
	 */
	public Player getKiller(boolean clearMap) {

		// Return null if no players killed this entity.
		if (damageMap.size() == 0) {
			return null;
		}

		// The damage and killer placeholders.
		int damage = 0;
		Player killer = null;

		for (Entry<Player, CombatDamageCache> entry : damageMap.entrySet()) {

			// Check if this entry is valid.
			if (entry == null) {
				continue;
			}

			// Check if the cached time is valid.
			long timeout = entry.getValue().getStopwatch().elapsed();
			if (timeout > CombatFactory.DAMAGE_CACHE_TIMEOUT) {
				continue;
			}

			// Check if the key for this entry is dead or has logged
			// out.
			Player player = entry.getKey();
			if (player.getConstitution() <= 0 || !player.isRegistered()) {
				continue;
			}

			// If their damage is above the placeholder value, they become the
			// new 'placeholder'.
			if (entry.getValue().getDamage() > damage) {
				damage = entry.getValue().getDamage();
				killer = entry.getKey();
			}
		}

		// Clear the damage map if needed.
		if (clearMap)
			damageMap.clear();

		// Return the killer placeholder.
		return killer;
	}

	/**
	 * Adds damage to the damage map, as long as the argued amount of damage is
	 * above 0 and the argued entity is a player.
	 *
	 * @param entity
	 *            the entity to add damage for.
	 * @param amount
	 *            the amount of damage to add for the argued entity.
	 */
	public void addDamage(Character entity, int amount) {

		if (amount < 1 || entity.isNpc()) {
			return;
		}

		Player player = (Player) entity;
		if (damageMap.containsKey(player)) {
			damageMap.get(player).incrementDamage(amount);
			return;
		}

		damageMap.put(player, new CombatDamageCache(amount));
	}

	/**
	 * Determines if this entity is attacking another entity.
	 *
	 * @return true if this entity is attacking another entity.
	 */
	public boolean isAttacking() {
		return victim != null;
	}

	public boolean isBeingAttacked() {
		return !character.getLastCombat().elapsed(5000);
	}

	public Character getCharacter() {
		return character;
	}

	public Character getVictim() {
		return victim;
	}

	public void setVictim(Character victim) {
		this.victim = victim;
	}

	public boolean isCooldown() {
		return cooldown > 0;
	}

	public void setAttackTimer(int attackTimer) {
		this.attackTimer = attackTimer;
	}

	public CombatBuilder incrementAttackTimer(int amount) {
		this.attackTimer += amount;
		return this;
	}

	public int getAttackTimer() {
		return this.attackTimer;
	}

	public Character getLastAttacker() {
		return lastAttacker;
	}

	public void setLastAttacker(Character lastAttacker) {
		this.lastAttacker = lastAttacker;
	}

	public CombatStrategy getStrategy() {
		return strategy;
	}

	public CombatSession getCombatSession() {
		return combatSession;
	}

	public CombatDistanceSession getDistanceSession() {
		return distanceSession;
	}

	public HitQueue getHitQueue() {
		return hitQueue;
	}

	public void setCombatSession(CombatSession combatTask) {
		this.combatSession = combatTask;
	}

	public void setDistanceSession(CombatDistanceSession distanceTask) {
		this.distanceSession = distanceTask;
	}

	public void determineStrategy() {
		this.strategy = character.determineStrategy();
	}

	public CombatContainer getContainer() {
		if (this.container != null)
			return container;
		return strategy.attack(character, victim);
	}

	public CombatType getCombatType() {
		return strategy.getCombatType();
	}

	public boolean didAutoRetaliate() {
		return retaliated;
	}

	public void setDidAutoRetaliate(boolean retaliated) {
		this.retaliated = retaliated;
	}

	public Stopwatch getLastAttack() {
		return lastAttack;
	}

	public void setContainer(CombatContainer customContainer) {
		if (customContainer != null && customContainer.getHits() != null && this.container != null) {
			ContainerHit[] totalHits = Misc.concat(this.container.getHits(), customContainer.getHits());
			this.container = customContainer;
			if (!(totalHits.length > 4 || totalHits.length < 0)) {
				this.container.setHits(totalHits);
			}
		} else
			this.container = customContainer;
	}

	/**
	 * A value held in the damage map for caching damage dealt against an
	 * {@link Entity}.
	 *
	 * @author lare96
	 */
	private static class CombatDamageCache {

		/**
		 * The amount of cached damage.
		 */
		private int damage;

		/**
		 * The stopwatch to time how long the damage is cached.
		 */
		private final Stopwatch stopwatch;

		/**
		 * Create a new {@link CombatDamageCache}.
		 *
		 * @param damage
		 *            the amount of cached damage.
		 */
		public CombatDamageCache(int damage) {
			this.damage = damage;
			this.stopwatch = new Stopwatch().reset();
		}

		/**
		 * Gets the amount of cached damage.
		 *
		 * @return the amount of cached damage.
		 */
		public int getDamage() {
			return damage;
		}

		/**
		 * Increments the amount of cached damage.
		 *
		 * @param damage
		 *            the amount of cached damage to add.
		 */
		public void incrementDamage(int damage) {
			this.damage += damage;
			this.stopwatch.reset();
		}

		/**
		 * Gets the stopwatch to time how long the damage is cached.
		 *
		 * @return the stopwatch to time how long the damage is cached.
		 */
		public Stopwatch getStopwatch() {
			return stopwatch;
		}
	}

	/**
	 * Executes the task instantly
	 **/
	public void instant() {
		combatSession.process();
	}
}
