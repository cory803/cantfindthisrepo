package com.chaos.world.content.combat.form.max.v1;

import com.chaos.model.Item;
import com.chaos.model.Locations;
import com.chaos.model.Prayerbook;
import com.chaos.model.Skill;
import com.chaos.model.container.impl.Equipment;
import com.chaos.model.definitions.ItemDefinition;
import com.chaos.model.definitions.WeaponInterfaces;
import com.chaos.world.content.BonusManager;
import com.chaos.world.content.combat.CombatType;
import com.chaos.world.content.combat.effect.EquipmentBonus;
import com.chaos.world.content.combat.form.max.MaxHitCalculator;
import com.chaos.world.content.combat.prayer.CurseHandler;
import com.chaos.world.content.combat.prayer.PrayerHandler;
import com.chaos.world.content.combat.weapon.CombatSpecial;
import com.chaos.world.content.combat.weapon.FightStyle;
import com.chaos.world.content.skill.impl.slayer.SlayerMasters;
import com.chaos.world.entity.impl.Character;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;

/**
 * An implementation of {@link MaxHitCalculator} used for our {@link com.chaos.world.content.combat.CombatType#RANGED}
 * attack type.
 *
 * @author Relex
 */
public final class RangedMaxHitCalculator implements MaxHitCalculator {

	@Override
	public int getMaxHit(Character source, Character victim) {

		if(source.isNpc()) {
			return ((NPC)source).getDefinition().getMaxHit();
		}

		/*
		 * Defining some constants.
		 */
		Item[] equipment = source.isPlayer() ? ((Player)source).getEquipment().getItems() : null;
		
		Item weapon = null;
		if(source.isPlayer()) {
			weapon = ((Player)source).getEquipment().get(Equipment.WEAPON_SLOT);
		}

		boolean usingSpecial = false;
		if(source.isPlayer()) {
			usingSpecial = ((Player)source).isSpecialActivated();
		}
		
		Prayerbook prayerBook = Prayerbook.NORMAL;
		if(source.isPlayer()) {
			prayerBook = ((Player)source).getPrayerbook();
		}

		boolean[] activePrayer = source.getPrayerActive();
		boolean[] activeCurses = source.getCurseActive();

		FightStyle attackStyle = FightStyle.CONTROLLED;
		if(weapon != null) {
			if(source.isPlayer()) {
				attackStyle = ((Player) source).getFightType().getStyle();
			}
		}
		
		/*
		 * Effective strength level
		 */
		int effectiveLevel = 0;

		if(source.isPlayer()) {
			effectiveLevel = ((Player)source).getSkillManager().getCurrentLevel(Skill.RANGED);
		}

		if (prayerBook == Prayerbook.CURSES
				&& activePrayer[CurseHandler.CurseData.LEECH_RANGED.ordinal()]) {
//			//currently for every 25 seconds of our leech curse being active, it will add +1 to our effectiveLevel
//			int timerBoost = (int) ((System.currentTimeMillis() - source.getFields().getPrayerActiveTime()[Curse.LEECH_RANGED.ordinal()]) / 25000);
//			effectiveLevel += 4 + (timerBoost > 5 ? 5 : timerBoost);
		}
		
		
		/*
		 * Prayer and curses
		 */
		double prayerBonus = 1.0;
		
		if (prayerBook == Prayerbook.NORMAL) {
			
			if (activePrayer[PrayerHandler.SHARP_EYE]) {
				prayerBonus += 0.05;
			} else if (activePrayer[PrayerHandler.HAWK_EYE]) {
				prayerBonus += 0.1;
			} else if (activePrayer[PrayerHandler.EAGLE_EYE]) {
				prayerBonus += 0.15;
			} else if (activePrayer[PrayerHandler.RIGOUR]) {
				prayerBonus += 0.2;
			}
			
		} else if (prayerBook == Prayerbook.CURSES) {
			
			if (activeCurses[CurseHandler.CurseData.LEECH_RANGED.ordinal()]) {
				prayerBonus += 0.05;
			}
		}
		
		/*
		 * Combat style bonuses
		 */
		int effectiveStrength = (int) (effectiveLevel * prayerBonus) + 8;
		
		if (attackStyle == FightStyle.ACCURATE) {
			effectiveStrength += 3;
		}
		
		/*
		 * Void knight set bonuses
		 * Bonuses must be applied twice to get the real effect
		 */
		if(source.isPlayer()) {
			if (EquipmentBonus.wearingEliteVoid((Player)source, CombatType.RANGED)) {
				effectiveStrength *= 1.15;
				effectiveStrength *= 1.15;
			} else if (EquipmentBonus.wearingVoid((Player)source, CombatType.RANGED)) {
				effectiveStrength *= 1.1;
				effectiveStrength *= 1.1;
			}
		}

		/*
		 * Equipment bonuses
		 */
		double equipmentBonus = 0;

		if(source.isPlayer()) {
			Player player = ((Player)source);
			if(player.getWeapon() != WeaponInterfaces.WeaponInterface.SHORTBOW && player.getWeapon() != WeaponInterfaces.WeaponInterface.BALLISTA && player.getWeapon() != WeaponInterfaces.WeaponInterface.LONGBOW && player.getWeapon() != WeaponInterfaces.WeaponInterface.CROSSBOW) {
				equipmentBonus = ((Player) source).getBonusManager().getOtherBonus()[BonusManager.BONUS_RANGE] - player.getEquipment().get(Equipment.AMMUNITION_SLOT).getDefinition().getBonus()[15];
			} else {
				equipmentBonus = ((Player) source).getBonusManager().getOtherBonus()[BonusManager.BONUS_RANGE];
			}
		}
		/*
		 * Base damage
		 */
		int maxHit = (int) (5 + effectiveStrength * (equipmentBonus + 64) / 64);
		
		/*
		 * Bonus damage
		 */
		if(source.isPlayer()) {
			if (usingSpecial) {
				CombatSpecial special = ((Player)source).getCombatSpecial();
				if (special != null) {
					maxHit *= special.getStrengthBonus();
					//maxHit += special.getExtraDamage(source, victim);
				}
			}
		}


		if(source.isPlayer()) {
			Player player = ((Player)source);
			if (victim.isNpc() && equipment[Equipment.RING_SLOT].getId() >= 15398 && equipment[Equipment.RING_SLOT].getId() <= 15402) {
				if(player.getSlayer().getSlayerMaster() != null) {
					if(player.getSlayer().getSlayerMaster() == SlayerMasters.KURADAL) {
						if(player.getLocation() == Locations.Location.KURADALS_DUNGEON) {
							maxHit *= 1.1;
						}
					}
				}
			}
		}
		
		if (equipment != null) {			
//			if (victim.isNpc() && equipment[Equipment.RING_SLOT].getId() >= 15398 && equipment[Equipment.RING_SLOT].getId() <= 15402
//					&& source.getPlayerFields().getSkillAttributes().getSlayerAssignment() != null) {
//				//ferocious ring bonus on slayer tasks
//				SlayerKey key = MobDefinition.forId(victim.getId()).getSlayerKey();
//				if (source.getPlayerFields().getSkillAttributes().getSlayerAssignment().getKey() == key) {
//					maxHit += 40;
//				}
//			} else if (victim.isMob() && victim.getId() == 9462 && equipment[Equipment.CAPE_SLOT].getId() == 6570) {
//				//ice strykewyrm while wearing a fire cape bonus
//				maxHit += 40;
//			}
		}
		
		return maxHit;
	}

}