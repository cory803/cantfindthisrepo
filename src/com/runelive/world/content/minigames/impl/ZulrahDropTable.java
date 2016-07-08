package com.runelive.world.content.minigames.impl;

import com.runelive.util.Misc;

/**
 * Created by Dave on 09/06/2016.
 */

public enum ZulrahDropTable {

	LAW_RUNE(563, 200), DEATH_RUNE(560, 300), CHAOS_RUNE(562, 500), PURE_ESSENCE(7937, 1500), TOADFLAX(2999,
			20), SNAPDRAGON(3000, 20), DWARF_WEED(268, 20), TORSTOL(270, 20), FLAX(1780, 1000), SNAKESKIN(6290,
					35), DRAGONBOLT_TIPS(9193, 12), MAGIC_LOGS(1514, 100), COAL(454, 300), RUNITE_ORE(452,
							10), MAGIC_SEED(5316, 1), CALQUAT_SEED(5290, 2), PAPAYA_SEED(5288, 1), TOADFLAX_SEED(5296,
									2), SNAPDRAGON_SEED(5300, 2), DWARF_WEED_SEED(5303, 2), TORSTOL_SEED(5304,
											2), DRAGON_BONE(537, 30), BATTLESTAFF(1392, 10), RAW_SHARK(383,
													100), SARADOMIN_FLASK_6(14428, 10), ADAMANT_BAR(2362, 25);

	public int itemId, amount;

	private ZulrahDropTable(int itemId, int amount) {
		this.itemId = itemId;
		this.amount = amount;
	}

	public int getItemId() {
		return itemId;
	}

	public int getAmount() {
		return amount;
	}

	public static ZulrahDropTable forDrop(int id) {
		for (ZulrahDropTable drop : ZulrahDropTable.values()) {
			if (drop.getItemId() == id) {
				return drop;
			}
		}
		return null;
	}

	public static ZulrahDropTable getDrop() {
		ZulrahDropTable drop = ZulrahDropTable.values()[Misc.getRandom(ZulrahDropTable.values().length - 1)];
		return drop;
	}
}
