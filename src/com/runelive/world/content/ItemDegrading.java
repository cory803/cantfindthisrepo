package com.runelive.world.content;

import com.runelive.model.Flag;
import com.runelive.model.Item;
import com.runelive.model.Locations;
import com.runelive.model.container.impl.Equipment;
import com.runelive.model.definitions.ItemDefinition;
import com.runelive.world.entity.impl.player.Player;

public class ItemDegrading {

	public static boolean handleItemDegrading(Player p, DegradingItem d) {
		if (d == null || p == null)
			return false;
		if (p.getLocation().equals(Locations.Location.DUEL_ARENA))
			return false;
		int equipId = p.getEquipment().getItems()[d.equipSlot].getId();
		if (equipId == d.nonDeg || equipId == d.deg) {
			int maxCharges = d.degradingCharges;
			int currentCharges = getAndIncrementCharge(p, d, false);
			boolean degradeCompletely = currentCharges >= maxCharges;
			if (equipId == d.deg && !degradeCompletely && d.deg != d.nonDeg) {
				return true;
			}
			if (d.deg != d.nonDeg)
				degradeCompletely = degradeCompletely && equipId == d.deg;
			else if (currentCharges >= maxCharges && currentCharges < d.degradingCharges)
				return true;
			p.getEquipment().setItem(d.equipSlot, new Item(degradeCompletely ? -1 : d.deg)).refreshItems();
			p.getUpdateFlag().flag(Flag.APPEARANCE);
			String ext = !degradeCompletely ? "degraded slightly" : "turned into dust";
			p.getPacketSender().sendMessage(
					"Your " + ItemDefinition.forId(equipId).getName().replace(" (deg)", "") + " has " + ext + "!");
			if (degradeCompletely) {
				getAndIncrementCharge(p, d, true);
			}
			return true;
		} else {
			return false;
		}
	}

	public static int getAndIncrementCharge(Player p, DegradingItem d, boolean reset) {
		switch (d) {
		case RING_OF_RECOIL:
			if (reset) {
				return p.setRecoilCharges(0);
			} else {
				return p.setRecoilCharges(p.getRecoilCharges() + 1);
			}
		case STATIUS_FULL_HELM:
		case STATIUS_PLATEBODY:
		case STATIUS_PLATELEGS:
		case STATIUS_WARHAMMER:
			if (reset)
				return p.setStatiusCharges(d.equipSlot, 0);
			else
				return p.getStatiusCharges()[d.equipSlot]++;
		case VESTAS_CHAINBODY:
		case VESTAS_PLATESKIRT:
		case VESTAS_LONGSWORD:
			if (reset)
				return p.setVestaCharges(d.equipSlot, 0);
			else
				return p.getVestaCharges()[d.equipSlot]++;
		case VESTAS_SPEAR: // ONE LESS SINCE LONGSWORD IS ALSO A WEAPON
			// -- TWICE SLOT 3
			if (reset)
				return p.setVestaCharges(d.equipSlot - 1, 0);
			else
				return p.getVestaCharges()[d.equipSlot - 1]++;
		case ZURIELS_HOOD:
		case ZURIELS_ROBE_TOP:
		case ZURIELS_ROBE_BOTTOM:
		case ZURIELS_STAFF:
			if (reset)
				return p.setZurielsCharges(d.equipSlot, 0);
			else
				return p.getZurielsCharges()[d.equipSlot]++;
		case MORRIGANS_COIF:
		case MORRIGANS_LEATHER_BODY:
		case MORRIGANS_LEATHER_CHAPS:
			if (reset)
				return p.setMorrigansCharges(d.equipSlot, 0);
			else
				return p.getMorrigansCharges()[d.equipSlot]++;
		case CORRUPT_STATIUS_FULL_HELM:
		case CORRUPT_STATIUS_PLATEBODY:
		case CORRUPT_STATIUS_PLATELEGS:
		case CORRUPT_STATIUS_WARHAMMER:
			if (reset)
				return p.setCorruptStatiusCharges(d.equipSlot, 0);
			else
				return p.getCorruptStatiusCharges()[d.equipSlot]++;
		case CORRUPT_VESTAS_CHAINBODY:
		case CORRUPT_VESTAS_PLATESKIRT:
		case CORRUPT_VESTAS_LONGSWORD:
			if (reset)
				return p.setCorruptVestaCharges(d.equipSlot, 0);
			else
				return p.getCorruptVestaCharges()[d.equipSlot]++;
		case CORRUPT_VESTAS_SPEAR: // ONE LESS SINCE LONGSWORD IS ALSO A WEAPON
			// -- TWICE SLOT 3
			if (reset)
				return p.setCorruptVestaCharges(d.equipSlot - 1, 0);
			else
				return p.getCorruptVestaCharges()[d.equipSlot - 1]++;
		case CORRUPT_ZURIELS_HOOD:
		case CORRUPT_ZURIELS_ROBE_TOP:
		case CORRUPT_ZURIELS_ROBE_BOTTOM:
		case CORRUPT_ZURIELS_STAFF:
			if (reset)
				return p.setCorruptZurielsCharges(d.equipSlot, 0);
			else
				return p.getCorruptZurielsCharges()[d.equipSlot]++;
		case CORRUPT_MORRIGANS_COIF:
		case CORRUPT_MORRIGANS_LEATHER_BODY:
		case CORRUPT_MORRIGANS_LEATHER_CHAPS:
			if (reset)
				return p.setCorruptMorrigansCharges(d.equipSlot, 0);
			else
				return p.getCorruptMorrigansCharges()[d.equipSlot]++;

		}
		return d.degradingCharges;
	}

	/*
	 * The enum holding all degradeable equipment
	 */
	public enum DegradingItem {

		/*
		 * Recoil
		 */
		RING_OF_RECOIL(2550, 2550, Equipment.RING_SLOT, 100),

		STATIUS_FULL_HELM(13896, 13898, Equipment.HEAD_SLOT, 600), STATIUS_PLATEBODY(13884, 13886, Equipment.BODY_SLOT,
				600), STATIUS_PLATELEGS(13890, 13892, Equipment.LEG_SLOT,
						600), STATIUS_WARHAMMER(13902, 13904, Equipment.WEAPON_SLOT, 600),

		VESTAS_CHAINBODY(13887, 13889, Equipment.BODY_SLOT, 600), VESTAS_PLATESKIRT(13893, 13895, Equipment.LEG_SLOT,
				600), VESTAS_LONGSWORD(13899, 13901, Equipment.WEAPON_SLOT,
						600), VESTAS_SPEAR(13905, 13907, Equipment.WEAPON_SLOT, 600),

		ZURIELS_HOOD(13864, 13866, Equipment.HEAD_SLOT, 600), ZURIELS_ROBE_TOP(13858, 13860, Equipment.BODY_SLOT,
				600), ZURIELS_ROBE_BOTTOM(13861, 13863, Equipment.LEG_SLOT,
						600), ZURIELS_STAFF(13867, 13869, Equipment.WEAPON_SLOT, 600),

		MORRIGANS_COIF(13876, 13878, Equipment.HEAD_SLOT, 600), MORRIGANS_LEATHER_BODY(13870, 13872,
				Equipment.BODY_SLOT, 600), MORRIGANS_LEATHER_CHAPS(13873, 13875, Equipment.LEG_SLOT, 600),

		// Statius's equipment

		CORRUPT_STATIUS_FULL_HELM(13920, 13922, Equipment.HEAD_SLOT, 200), CORRUPT_STATIUS_PLATEBODY(13908, 13910,
				Equipment.BODY_SLOT, 200), CORRUPT_STATIUS_PLATELEGS(13914, 13916, Equipment.LEG_SLOT,
						200), CORRUPT_STATIUS_WARHAMMER(13926, 13928, Equipment.WEAPON_SLOT, 200),

		// Vesta's equipment

		CORRUPT_VESTAS_CHAINBODY(13911, 13913, Equipment.BODY_SLOT, 200), CORRUPT_VESTAS_PLATESKIRT(13917, 13919,
				Equipment.LEG_SLOT, 200), CORRUPT_VESTAS_LONGSWORD(13923, 13925, Equipment.WEAPON_SLOT,
						160), CORRUPT_VESTAS_SPEAR(13929, 13931, Equipment.WEAPON_SLOT, 200),

		// Zuriel's equipment

		CORRUPT_ZURIELS_HOOD(13938, 13940, Equipment.HEAD_SLOT, 200), CORRUPT_ZURIELS_ROBE_TOP(13932, 13934,
				Equipment.BODY_SLOT, 200), CORRUPT_ZURIELS_ROBE_BOTTOM(13935, 13937, Equipment.LEG_SLOT,
						200), CORRUPT_ZURIELS_STAFF(13941, 13943, Equipment.WEAPON_SLOT, 200),

		// Morrigan's equipment

		CORRUPT_MORRIGANS_COIF(13950, 13952, Equipment.HEAD_SLOT, 200), CORRUPT_MORRIGANS_LEATHER_BODY(13944, 13946,
				Equipment.BODY_SLOT, 200), CORRUPT_MORRIGANS_LEATHER_CHAPS(13944, 13946, Equipment.LEG_SLOT, 200);

		DegradingItem(int nonDeg, int deg, int equipSlot, int degradingCharges) {
			this.nonDeg = nonDeg;
			this.deg = deg;
			this.equipSlot = equipSlot;
			this.degradingCharges = degradingCharges;
		}

		private int nonDeg, deg;
		private int equipSlot;
		private int degradingCharges;

		public static DegradingItem forNonDeg(int item) {
			for (DegradingItem d : DegradingItem.values()) {
				if (d.nonDeg == item) {
					return d;
				}
			}
			return null;
		}

		public static DegradingItem forID(int item) {
			for (DegradingItem d : DegradingItem.values()) {
				if (d.deg == item || d.nonDeg == item) {
					return d;
				}
			}
			return null;
		}
	}
}
