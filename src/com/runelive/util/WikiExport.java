package com.runelive.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

import com.runelive.model.Item;
import com.runelive.model.definitions.ItemDefinition;
import com.runelive.model.definitions.NPCDrops;
import com.runelive.model.definitions.NPCDrops.ItemDropAnnouncer;
import com.runelive.model.definitions.NPCDrops.NpcDropItem;

public class WikiExport {

	/**
	 * Dumps random data for the wiki
	 * 
	 */

	public static void exportAllDropTables() {
		for (int i = 1; i < 14500; i++) {
			exportDropTable(i);
		}
	}

	public static void exportDropTable(int npcId) {
		NPCDrops npcDrops = NPCDrops.forId(npcId);
		ArrayList<NpcDropItem> NPC_DROP_ALWAYS = new ArrayList<NpcDropItem>();
		ArrayList<NpcDropItem> NPC_DROP_MISC = new ArrayList<NpcDropItem>();
		ArrayList<NpcDropItem> NPC_DROP_RARE = new ArrayList<NpcDropItem>();
		if (npcDrops != null) {
			for (NpcDropItem item : npcDrops.getDropList()) {
				if (item != null) {
					if (item.getId() > 0) {
						if (item.getChanceId() == 0) {
							NPC_DROP_ALWAYS.add(item);
						} else if (item.getChanceId() >= 1 && item.getChanceId() <= 4) {
							NPC_DROP_MISC.add(item);
						} else {
							NPC_DROP_RARE.add(item);
						}
					}
				}
			}
		}
		if (NPC_DROP_ALWAYS.size() > 0) {
			dumpData("", "" + npcId + "");
			dumpData("{{DropsTableHeader|Name=100%}}", "" + npcId + "");
		}
		for (int i = 0; i < NPC_DROP_ALWAYS.size(); i++) {
			Item dropItem = NPC_DROP_ALWAYS.get(i).getItem();
			if (NPC_DROP_ALWAYS.get(i).getCount().length > 1) {
				dumpData("{{DropsLine|Name=" + ItemDefinition.forId(dropItem.getId()).getName() + "|Quantity="
						+ NPC_DROP_ALWAYS.get(i).getCount1() + "-" + NPC_DROP_ALWAYS.get(i).getCount2()
						+ "|Rarity=always}}", "" + npcId + "");
			} else {
				dumpData("{{DropsLine|Name=" + ItemDefinition.forId(dropItem.getId()).getName() + "|Quantity="
						+ dropItem.getAmount() + "|Rarity=always}}", "" + npcId + "");
			}
		}
		if (NPC_DROP_MISC.size() > 0) {
			dumpData("", "" + npcId + "");
			dumpData("{{DropsTableHeader|Name=Misc}}", "" + npcId + "");
		}
		for (int i = 0; i < NPC_DROP_MISC.size(); i++) {
			Item dropItem = NPC_DROP_MISC.get(i).getItem();
			String chance = "" + NPC_DROP_MISC.get(i).getChance() + "";
			if (NPC_DROP_MISC.get(i) == null) {
				continue;
			}
			if (NPC_DROP_MISC.get(i).getCount().length > 1) {
				if (NPC_DROP_MISC.get(i).getChanceId() == 2) {
					dumpData("{{DropsLine|Name=" + ItemDefinition.forId(dropItem.getId()).getName() + "|Quantity="
							+ NPC_DROP_ALWAYS.get(i).getCount1() + "-" + NPC_DROP_ALWAYS.get(i).getCount2()
							+ "|Rarity=common}}", "" + npcId + "");
				} else {
					dumpData("{{DropsLine|Name=" + ItemDefinition.forId(dropItem.getId()).getName() + "|Quantity="
							+ NPC_DROP_ALWAYS.get(i).getCount1() + "-" + NPC_DROP_ALWAYS.get(i).getCount2() + "|Rarity="
							+ chance.toLowerCase() + "}}", "" + npcId + "");
				}
			} else {
				if (NPC_DROP_MISC.get(i).getChanceId() == 2) {
					dumpData("{{DropsLine|Name=" + ItemDefinition.forId(dropItem.getId()).getName() + "|Quantity="
							+ dropItem.getAmount() + "|Rarity=common}}", "" + npcId + "");
				} else {
					dumpData("{{DropsLine|Name=" + ItemDefinition.forId(dropItem.getId()).getName() + "|Quantity="
							+ dropItem.getAmount() + "|Rarity=" + chance.toLowerCase() + "}}", "" + npcId + "");
				}
			}
		}
		for (int i = 0; i < NPC_DROP_RARE.size(); i++) {
			Item dropItem = NPC_DROP_RARE.get(i).getItem();
			for (int i2 = 0; i2 < ItemDropAnnouncer.TO_ANNOUNCE.length; i2++) {
				if (ItemDropAnnouncer.TO_ANNOUNCE[i2] != dropItem.getId()
						&& i2 == ItemDropAnnouncer.TO_ANNOUNCE.length) {
					dumpData("{{DropsLine|Name=" + ItemDefinition.forId(dropItem.getId()).getName() + "|Quantity="
							+ dropItem.getAmount() + "|Rarity=uncommon}}", "" + npcId + "");
				}
			}
		}
		if (NPC_DROP_RARE.size() > 0) {
			for (int i = 0; i < NPC_DROP_RARE.size(); i++) {
				Item dropItem = NPC_DROP_RARE.get(i).getItem();
				boolean yeah = false;
				for (int i2 = 0; i2 < ItemDropAnnouncer.TO_ANNOUNCE.length; i2++) {
					if (ItemDropAnnouncer.TO_ANNOUNCE[i2] == dropItem.getId()) {
						yeah = true;
					}
				}
				if (yeah && i == NPC_DROP_RARE.size()) {
					dumpData("", "" + npcId + "");
					dumpData("{{DropsTableHeader|Name=Rare Drops}}", "" + npcId + "");
				}
			}
		}
		for (int i = 0; i < NPC_DROP_RARE.size(); i++) {
			Item dropItem = NPC_DROP_RARE.get(i).getItem();
			for (int i2 = 0; i2 < ItemDropAnnouncer.TO_ANNOUNCE.length; i2++) {
				if (ItemDropAnnouncer.TO_ANNOUNCE[i2] == dropItem.getId()) {
					dumpData("{{DropsLine|Name=" + ItemDefinition.forId(dropItem.getId()).getName() + "|Quantity="
							+ dropItem.getAmount() + "|Rarity=rare}}", "" + npcId + "");
				}
			}
		}
	}

	public static void dumpData(String data, String fileName) {
		BufferedWriter bw = null;
		try {
			FileWriter fileWriter = new FileWriter("./wiki/" + fileName + ".txt", true);
			bw = new BufferedWriter(fileWriter);
			bw.write(data);
			bw.newLine();
			bw.flush();
			bw.close();
			fileWriter = null;
			bw = null;
		} catch (Exception exception) {
			System.out.print("Critical error while writing data: " + fileName);
		}
	}

}
