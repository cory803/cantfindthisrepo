package com.runelive.world.content.combat.effect;

import com.runelive.model.Item;
import com.runelive.model.container.impl.Equipment;
import com.runelive.world.content.combat.CombatType;
import com.runelive.world.entity.impl.player.Player;

public class EquipmentBonus {

	public static boolean wearingVoid(Player player, CombatType attackType) {
		int amount = 0;
		Item head = player.getEquipment().getItems()[Equipment.HEAD_SLOT];
		Item body = player.getEquipment().getItems()[Equipment.BODY_SLOT];
		Item legs = player.getEquipment().getItems()[Equipment.LEG_SLOT];
		Item gloves = player.getEquipment().getItems()[Equipment.HANDS_SLOT];
		Item shield = player.getEquipment().getItems()[Equipment.SHIELD_SLOT];

		if(attackType == CombatType.RANGED) {
			for(int i = 0; i < VOID_ARMOUR.length; i++) {
				if(head.getId() == VOID_ARMOUR[i]) {
					amount++;
				} else if(body.getId() == VOID_ARMOUR[i]) {
					amount++;
				} else if(legs.getId() == VOID_ARMOUR[i]) {
					amount++;
				} else if(gloves.getId() == VOID_ARMOUR[i]) {
					amount++;
				} else if(shield.getId() == VOID_ARMOUR[i]) {
					amount++;
				}
			}
		}
		return amount >= 4;
	}

	public static boolean wearingEliteVoid(Player player, CombatType attackType) {

	}

	private static final int MAGE_VOID_HELM = 11663;

	private static final int RANGED_VOID_HELM = 11664;

	private static final int MELEE_VOID_HELM = 11665;

	private static final int VOID_KNIGHT_DEFLECTOR = 19712;

	public static final int[][] BERSERKER_BUFF = { { Equipment.AMULET_SLOT, 11128 }, { Equipment.RING_SLOT, 6737 },
			{ Equipment.WEAPON_SLOT, 6528 } };

	public static final int[][] BERSERKER_I_BUFF = { { Equipment.AMULET_SLOT, 11128 }, { Equipment.RING_SLOT, 15220 },
			{ Equipment.WEAPON_SLOT, 6528 } };

	public static int[] VOID_ARMOUR = new int[]{8839, 8840, 8842, 19711};

	public static final int[] ELITE_VOID_ARMOUR = { 19785, 19786, 8842 };
}
