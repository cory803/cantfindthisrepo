package com.chaos.util;

import com.chaos.model.Item;
import com.chaos.model.npc.drops.LootSystem;
import com.chaos.model.npc.drops.LootTable;

import java.util.Random;

/**
 * Tests the chances of a drop for a specific npc.
 *
 * @Author Jonny
 */
public class TestDropChance {

	/**
	 * Generates a random formula and spits out the answer.
	 * @LootSystem
	 */
	public static void chance(int npcId) {
		int total = 0;
		LootTable table = LootSystem.tables.get(npcId);
		for (int i = 0; i < 100000; i++) {
			Item roll = LootSystem.rollDrop(null, null, table.getSortedLoot());
			if(roll == null) {
				continue;
			}
			if(roll.getId() == 13178) {
				total++;
			}
		}
		System.out.println("["+npcId+"] Total generated in epic rarity drop test: "+total);
	}
}
