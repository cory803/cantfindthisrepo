package com.runelive.world.content.combat;

import com.runelive.model.Graphic;
import com.runelive.model.Skill;
import com.runelive.model.container.impl.Equipment;
import com.runelive.model.definitions.ItemDefinition;
import com.runelive.util.Misc;
import com.runelive.world.content.combat.effect.EquipmentBonus;
import com.runelive.world.content.combat.magic.CombatSpell;
import com.runelive.world.content.combat.prayer.CurseHandler;
import com.runelive.world.content.combat.prayer.PrayerHandler;
import com.runelive.world.content.combat.range.CombatRangedAmmo.RangedWeaponData;
import com.runelive.world.content.combat.weapon.FightType;
import com.runelive.world.content.skill.SkillManager;
import com.runelive.world.entity.impl.Character;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;

public class DesolaceFormulas {

	/*
	 * =========================================================================
	 * =====
	 */
	/*
	 * ===================================MELEE=================================
	 * ====
	 */

	public static int calculateMaxMeleeHit(Character entity, Character victim) {
		double maxHit = 0;
		if (entity.isNpc()) {
			NPC npc = (NPC) entity;
			maxHit = npc.getDefinition().getMaxHit();
			if (npc.getStrengthWeakened()[0]) {
				maxHit -= (int) ((0.10) * (maxHit));
			} else if (npc.getStrengthWeakened()[1]) {
				maxHit -= (int) ((0.20) * (maxHit));
			} else if (npc.getStrengthWeakened()[2]) {
				maxHit -= (int) ((0.30) * (maxHit));
			}

			/** CUSTOM NPCS **/
			if (npc.getId() == 2026) { // Dharok the wretched
				maxHit += (int) ((npc.getDefaultConstitution() - npc.getConstitution()) * 0.2);
			}
		} else {
			Player plr = (Player) entity;

			double base = 0;
			double effective = getEffectiveStr(plr);
			double specialBonus = 1;
			if (plr.isSpecialActivated()) {
				specialBonus = plr.getCombatSpecial().getStrengthBonus();
			}
			double strengthBonus = plr.getBonusManager().getOtherBonus()[0];
			base = (10 + effective + (strengthBonus / 8) + ((effective * strengthBonus) / 65)) / 11;
			if (plr.getEquipment().getItems()[3].getId() == 4718 && plr.getEquipment().getItems()[0].getId() == 4716
					&& plr.getEquipment().getItems()[4].getId() == 4720
					&& plr.getEquipment().getItems()[7].getId() == 4722)
				base += ((plr.getSkillManager().getMaxLevel(Skill.CONSTITUTION) - plr.getConstitution()) * .045) + 1;
			if (specialBonus > 1)
				base = (base * specialBonus);
			if (hasObsidianEffect(plr) || EquipmentBonus.wearingVoid(plr, CombatType.MELEE))
				base = (base * 1.2);

			if (victim.isNpc()) {
				NPC npc = (NPC) victim;
				if (npc.getDefenceWeakened()[0]) {
					base += (int) ((0.10) * (base));
				} else if (npc.getDefenceWeakened()[1]) {
					base += (int) ((0.20) * (base));
				} else if (npc.getDefenceWeakened()[2]) {
					base += (int) ((0.30) * (base));
				}
				if (npc.getId() == plr.getSlayer().getSlayerTask().getNpcId()) {
					if (plr.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 15492) {
						base *= 1.20;
					}
				}

				/** SLAYER HELMET **/
				if (npc.getId() == plr.getSlayer().getSlayerTask().getNpcId()) {
					if (plr.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 13263) {
						base *= 1.10;
					}
				}
			}
			maxHit = (base *= 10);
		}
		if (victim.isPlayer()) {
			Player p = (Player) victim;
			if (p.hasStaffOfLightEffect()) {
				maxHit = maxHit / 2;
				p.performGraphic(new Graphic(2319));
			}
		}
		return (int) Math.floor(maxHit);
	}

	/**
	 * Calculates a player's Melee attack level (how likely that they're going
	 * to hit through defence)
	 * 
	 * @param plr
	 *            The player's Meelee attack level
	 * @return The player's Melee attack level
	 */
	@SuppressWarnings("incomplete-switch")
	public static int getMeleeAttack(Player plr) {
		int attackLevel = plr.getSkillManager().getCurrentLevel(Skill.ATTACK);
		switch (plr.getFightType().getStyle()) {
		case AGGRESSIVE:
			attackLevel += 3;
			break;
		case CONTROLLED:
			attackLevel += 1;
			break;
		}
		boolean hasVoid = EquipmentBonus.wearingVoid(plr, CombatType.MELEE);

		if (PrayerHandler.isActivated(plr, PrayerHandler.CLARITY_OF_THOUGHT)) {
			attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.05;
		} else if (PrayerHandler.isActivated(plr, PrayerHandler.IMPROVED_REFLEXES)) {
			attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.1;
		} else if (PrayerHandler.isActivated(plr, PrayerHandler.INCREDIBLE_REFLEXES)) {
			attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.15;
		} else if (PrayerHandler.isActivated(plr, PrayerHandler.CHIVALRY)) {
			attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.15;
		} else if (PrayerHandler.isActivated(plr, PrayerHandler.PIETY)) {
			attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.2;
		} else if (CurseHandler.isActivated(plr, CurseHandler.LEECH_ATTACK)) {
			attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.05 + plr.getLeechedBonuses()[2];
		} else if (CurseHandler.isActivated(plr, CurseHandler.TURMOIL)) {
			attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.3 + plr.getLeechedBonuses()[2];
		}

		if (hasVoid) {
			attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.1;
		}
		attackLevel *= plr.isSpecialActivated() ? plr.getCombatSpecial().getAccuracyBonus() : 1;
		int i = (int) plr.getBonusManager().getAttackBonus()[bestMeleeAtk(plr)];

		if (hasObsidianEffect(plr) || hasVoid)
			i *= 1.20;
		return (int) (attackLevel + (attackLevel * 0.15) + (i + i * 0.04));
	}

	/**
	 * Calculates a player's Melee Defence level
	 * 
	 * @param plr
	 *            The player to calculate Melee defence for
	 * @return The player's Melee defence level
	 */
	public static int getMeleeDefence(Player plr) {
		int defenceLevel = plr.getSkillManager().getCurrentLevel(Skill.DEFENCE);
		int i = (int) plr.getBonusManager().getDefenceBonus()[bestMeleeDef(plr)];
		if (plr.getPrayerActive()[PrayerHandler.THICK_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.05;
		} else if (plr.getPrayerActive()[PrayerHandler.ROCK_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.1;
		} else if (plr.getPrayerActive()[PrayerHandler.STEEL_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.15;
		} else if (plr.getPrayerActive()[PrayerHandler.CHIVALRY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.2;
		} else if (plr.getPrayerActive()[PrayerHandler.PIETY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		} else if (plr.getPrayerActive()[PrayerHandler.RIGOUR]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		} else if (plr.getPrayerActive()[PrayerHandler.AUGURY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		} else if (plr.getCurseActive()[CurseHandler.TURMOIL]) { // turmoil
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.15;
		}
		return (int) (defenceLevel + (defenceLevel * 0.15) + (i + i * 0.05));
	}

	public static int bestMeleeDef(Player p) {
		if (p.getBonusManager().getDefenceBonus()[0] > p.getBonusManager().getDefenceBonus()[1]
				&& p.getBonusManager().getDefenceBonus()[0] > p.getBonusManager().getDefenceBonus()[2]) {
			return 0;
		}
		if (p.getBonusManager().getDefenceBonus()[1] > p.getBonusManager().getDefenceBonus()[0]
				&& p.getBonusManager().getDefenceBonus()[1] > p.getBonusManager().getDefenceBonus()[2]) {
			return 1;
		}
		return p.getBonusManager().getDefenceBonus()[2] <= p.getBonusManager().getDefenceBonus()[0]
				|| p.getBonusManager().getDefenceBonus()[2] <= p.getBonusManager().getDefenceBonus()[1] ? 0 : 2;
	}

	public static int bestMeleeAtk(Player p) {
		if (p.getBonusManager().getAttackBonus()[0] > p.getBonusManager().getAttackBonus()[1]
				&& p.getBonusManager().getAttackBonus()[0] > p.getBonusManager().getAttackBonus()[2]) {
			return 0;
		}
		if (p.getBonusManager().getAttackBonus()[1] > p.getBonusManager().getAttackBonus()[0]
				&& p.getBonusManager().getAttackBonus()[1] > p.getBonusManager().getAttackBonus()[2]) {
			return 1;
		}
		return p.getBonusManager().getAttackBonus()[2] <= p.getBonusManager().getAttackBonus()[1]
				|| p.getBonusManager().getAttackBonus()[2] <= p.getBonusManager().getAttackBonus()[0] ? 0 : 2;
	}

	/**
	 * Obsidian items
	 */

	public static final int[] obsidianWeapons = { 746, 747, 6523, 6525, 6526, 6527, 6528 };

	public static boolean hasObsidianEffect(Player plr) {
		if (plr.getEquipment().getItems()[2].getId() != 11128)
			return false;

		for (int weapon : obsidianWeapons) {
			if (plr.getEquipment().getItems()[3].getId() == weapon)
				return true;
		}
		return false;
	}

	@SuppressWarnings("incomplete-switch")
	public static int getStyleBonus(Player plr) {
		switch (plr.getFightType().getStyle()) {
		case AGGRESSIVE:
		case ACCURATE:
			return 3;
		case CONTROLLED:
			return 1;
		}
		return 0;
	}

	public static double getEffectiveStr(Player plr) {
		return ((plr.getSkillManager().getCurrentLevel(Skill.STRENGTH)) * getPrayerStr(plr)) + getStyleBonus(plr);
	}

	public static double getPrayerStr(Player plr) {
		if (plr.getPrayerActive()[1] || plr.getCurseActive()[CurseHandler.LEECH_STRENGTH])
			return 1.05;
		else if (plr.getPrayerActive()[6])
			return 1.1;
		else if (plr.getPrayerActive()[14])
			return 1.15;
		else if (plr.getPrayerActive()[24])
			return 1.18;
		else if (plr.getPrayerActive()[25])
			return 1.23;
		else if (plr.getCurseActive()[CurseHandler.TURMOIL])
			return 1.24;
		return 1;
	}

	/**
	 * Calculates a player's Ranged attack (level). Credits: Dexter Morgan
	 * 
	 * @param plr
	 *            The player to calculate Ranged attack level for
	 * @return The player's Ranged attack level
	 */
	public static int getRangedAttack(Player plr) {
		int rangeLevel = plr.getSkillManager().getCurrentLevel(Skill.RANGED);
		boolean hasVoid = EquipmentBonus.wearingVoid(plr, CombatType.RANGED);
		double accuracy = plr.isSpecialActivated() ? plr.getCombatSpecial().getAccuracyBonus() : 1;
		rangeLevel *= accuracy;
		if (hasVoid) {
			rangeLevel += SkillManager.getLevelForExperience(plr.getSkillManager().getExperience(Skill.RANGED)) * 0.15;
		}
		if (plr.getCurseActive()[PrayerHandler.SHARP_EYE] || plr.getCurseActive()[CurseHandler.SAP_RANGER]) {
			rangeLevel *= 1.05;
		} else if (plr.getPrayerActive()[PrayerHandler.HAWK_EYE]) {
			rangeLevel *= 1.10;
		} else if (plr.getPrayerActive()[PrayerHandler.EAGLE_EYE]) {
			rangeLevel *= 1.15;
		} else if (plr.getPrayerActive()[PrayerHandler.RIGOUR]) {
			rangeLevel *= 1.22;
		} else if (plr.getCurseActive()[CurseHandler.LEECH_RANGED]) {
			rangeLevel *= 1.10;
		}
		if (plr.getEquipment().contains(19672)) {
			rangeLevel *= 1.00;
		}
		if (hasVoid && accuracy > 1.15)
			rangeLevel *= 1.68;
		/*
		 * Slay helm if(plr.getAdvancedSkills().getSlayer().getSlayerTask() !=
		 * null && plr.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() ==
		 * 15492) { if(plr.getCombatAttributes().getCurrentEnemy() != null &&
		 * plr.getCombatAttributes().getCurrentEnemy().isNpc()) { NPC n =
		 * (NPC)plr.getCombatAttributes().getCurrentEnemy(); if(n != null &&
		 * n.getId() ==
		 * plr.getAdvancedSkills().getSlayer().getSlayerTask().getNpcId())
		 * rangeLevel *= 1.12; } }
		 */
		return (int) (rangeLevel + (plr.getBonusManager().getAttackBonus()[4] * 2));
	}

	/**
	 * Calculates a player's Ranged defence level.
	 * 
	 * @param plr
	 *            The player to calculate the Ranged defence level for
	 * @return The player's Ranged defence level
	 */
	public static int getRangedDefence(Player plr) {
		int defenceLevel = plr.getSkillManager().getCurrentLevel(Skill.DEFENCE);
		if (plr.getPrayerActive()[PrayerHandler.THICK_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.05;
		} else if (plr.getPrayerActive()[PrayerHandler.ROCK_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.1;
		} else if (plr.getPrayerActive()[PrayerHandler.STEEL_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.15;
		} else if (plr.getPrayerActive()[PrayerHandler.CHIVALRY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.2;
		} else if (plr.getPrayerActive()[PrayerHandler.PIETY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		} else if (plr.getPrayerActive()[PrayerHandler.RIGOUR]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		} else if (plr.getPrayerActive()[PrayerHandler.AUGURY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		} else if (plr.getCurseActive()[CurseHandler.TURMOIL]) { // turmoil
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.20 + plr.getLeechedBonuses()[0];
		}
		return (int) (defenceLevel + plr.getBonusManager().getDefenceBonus()[4]
				+ (plr.getBonusManager().getDefenceBonus()[4] / 2));
	}

	public static int getMagicAttack(Player plr) {
		boolean voidEquipment = EquipmentBonus.wearingVoid(plr, CombatType.MAGIC);
		int attackLevel = plr.getSkillManager().getCurrentLevel(Skill.MAGIC);
		if (voidEquipment)
			attackLevel += plr.getSkillManager().getCurrentLevel(Skill.MAGIC) * 0.2;
		if (plr.getPrayerActive()[PrayerHandler.MYSTIC_WILL] || plr.getCurseActive()[CurseHandler.SAP_MAGE]) {
			attackLevel *= 1.05;
		} else if (plr.getPrayerActive()[PrayerHandler.MYSTIC_LORE]) {
			attackLevel *= 1.10;
		} else if (plr.getPrayerActive()[PrayerHandler.MYSTIC_MIGHT]) {
			attackLevel *= 1.15;
		} else if (plr.getPrayerActive()[PrayerHandler.AUGURY]) {
			attackLevel *= 1.22;
		} else if (plr.getCurseActive()[CurseHandler.LEECH_MAGIC]) {
			attackLevel *= 1.18;
		}
		attackLevel *= plr.isSpecialActivated() ? plr.getCombatSpecial().getAccuracyBonus() : 1;

		return (int) (attackLevel + (plr.getBonusManager().getAttackBonus()[3] * 2));
	}

	/**
	 * Calculates a player's magic defence level
	 * 
	 * @return The player's magic defence level
	 */
	public static int getMagicDefence(Player plr) {

		int defenceLevel = plr.getSkillManager().getCurrentLevel(Skill.DEFENCE) / 2
				+ plr.getSkillManager().getCurrentLevel(Skill.MAGIC) / 2;

		if (plr.getPrayerActive()[PrayerHandler.THICK_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.05;
		} else if (plr.getPrayerActive()[PrayerHandler.ROCK_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.1;
		} else if (plr.getPrayerActive()[PrayerHandler.STEEL_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.15;
		} else if (plr.getPrayerActive()[PrayerHandler.CHIVALRY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.2;
		} else if (plr.getPrayerActive()[PrayerHandler.PIETY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		} else if (plr.getPrayerActive()[PrayerHandler.RIGOUR]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		} else if (plr.getPrayerActive()[PrayerHandler.AUGURY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		} else if (plr.getCurseActive()[CurseHandler.TURMOIL]) { // turmoil
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.20 + plr.getLeechedBonuses()[0];
		}

		return (int) (defenceLevel + plr.getBonusManager().getDefenceBonus()[3]
				+ (plr.getBonusManager().getDefenceBonus()[3] / 3));
	}

	/**
	 * Calculates a player's magic max hit
	 * 
	 * @return The player's magic max hit damage
	 */
	public static int getMagicMaxhit(Character c) {
		int damage = 0;
		CombatSpell spell = c.getCurrentlyCasting();
		if (spell != null) {
			if (spell.maximumHit() > 0)
				damage += spell.maximumHit();
			else {
				if (c.isNpc()) {
					damage = ((NPC) c).getDefinition().getMaxHit();
				} else {
					damage = 1;
				}
			}
		}

		if (c.isNpc()) {
			if (spell == null) {
				damage = Misc.getRandom(((NPC) c).getDefinition().getMaxHit());
			}
			return damage;
		}

		Player p = (Player) c;
		double damageMultiplier = 1;

		switch (p.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId()) {
		case 4675:
		case 6914:
		case 15246:
			damageMultiplier += .10;
			break;
		case 18355:
			damageMultiplier += .20;
			break;
		}

		boolean specialAttack = p.isSpecialActivated();

		int maxHit = -1;

		if (specialAttack) {
			switch (p.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId()) {

			case 19780:
				if (p.getCurrentlyCasting() != null) {
					damage = maxHit = spell.maximumHit();
				} else {
					damage = maxHit = 750;
				}
				break;
			case 11730:
				if (p.getCurrentlyCasting() != null) {
					damage = maxHit = spell.maximumHit();
				} else {
					damage = maxHit = 310;
				}
				break;
			case 19674:
				damageMultiplier += 0.25;
				break;
			case 11235:
			case 21016:
			case 21017:
			case 21018:
			case 21019:
			case 21020:
			case 21021:
			case 21022:
			case 21023:
				if (EquipmentBonus.wearingVoid(p, CombatType.RANGED)) {
					damage = maxHit = 480;
				} else {
					damage = maxHit = 420;
				}
				break;
			case 13905:
			case 13907:
				if (p.getCurrentlyCasting() != null) {
					damage = maxHit = spell.maximumHit();
				} else {
					damage = maxHit = 485;
				}
				break;
			}
		} else {
			damageMultiplier += 0.25;
		}

		if (p.getEquipment().getItems()[Equipment.AMULET_SLOT].getId() == 18335) {
			damageMultiplier += .10;
		}

		damage *= damageMultiplier;

		if (maxHit > 0) {
			if (damage > maxHit) {
				damage = maxHit;
			}
		}

		return damage;
	}
}
