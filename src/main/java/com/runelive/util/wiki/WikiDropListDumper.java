package com.runelive.util.wiki;

import com.runelive.model.definitions.ItemDefinition;
import com.runelive.model.definitions.NpcDefinition;
import com.runelive.model.npc.drops.LootItem;
import com.runelive.model.npc.drops.LootSystem;
import com.runelive.model.npc.drops.LootTable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Dumps a list of all drops in Chaos.
 * Used for the Wiki.
 *
 * @Author Jonny
 */
public class WikiDropListDumper {

	/**
	 * Dumps LootSystem into a .txt file.
	 * @LootSystem
	 */
	public static void dump() {
		try {
			File file = new File("lists/wiki_drops.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			for (int i = 0; i < NpcDefinition.getDefinitions().length; i++) {
				LootTable table = LootSystem.tables.get(i);
				if(table == null) {
					continue;
				}
				if(NpcDefinition.forId(i) == null) {
					continue;
				}
				write(bw, "Monster: "+NpcDefinition.forId(i).getName()+ "(Level: "+NpcDefinition.forId(i).getCombatLevel()+") (id = "+i+")");
				write(bw, "---------------");
				write(bw, "");
				LootItem[][] tableSorted = table.getSortedLoot();
				for (int i2 = 0; i2 < tableSorted.length; i2++) {
					if(tableSorted[i2].length > 0) {
						if (i2 == 0) {
							write(bw, "{{DropsTableHeader|Name=100% Drops}}");
							for (int i3 = 0; i3 < tableSorted[i2].length; i3++) {
								if (tableSorted[i2][i3].getMaximum() != tableSorted[i2][i3].getMinimum()) {
									write(bw, "{{DropsLine|Name="+ItemDefinition.forId(tableSorted[i2][i3].getId()).getName()+"|Quantity=" + tableSorted[i2][i3].getMinimum() + "-" + tableSorted[i2][i3].getMaximum() + "|Rarity=always}}");
								} else {
									write(bw, "{{DropsLine|Name="+ItemDefinition.forId(tableSorted[i2][i3].getId()).getName()+"|Quantity=1|Rarity=always}}");
								}
							}
							bw.newLine();
							bw.newLine();
//						} else if (i2 == 1) {
//							write(bw, "{{DropsTableHeader|Name=Common Drops}}");
//							for (int i3 = 0; i3 < tableSorted[i2].length; i3++) {
//								if (tableSorted[i2][i3].getMaximum() != tableSorted[i2][i3].getMinimum()) {
//									write(bw, "{{DropsLine|Name="+ItemDefinition.forId(tableSorted[i2][i3].getId()).getName()+"|Quantity=" + tableSorted[i2][i3].getMinimum() + "-" + tableSorted[i2][i3].getMaximum() + "|Rarity=common}}");
//								} else {
//									write(bw, "{{DropsLine|Name="+ItemDefinition.forId(tableSorted[i2][i3].getId()).getName()+"|Quantity=1|Rarity=common}}");
//								}
//							}
//							bw.newLine();
//							bw.newLine();
//						} else if (i2 == 2) {
//							write(bw, "{{DropsTableHeader|Name=Uncommon Drops}}");
//							for (int i3 = 0; i3 < tableSorted[i2].length; i3++) {
//								if (tableSorted[i2][i3].getMaximum() != tableSorted[i2][i3].getMinimum()) {
//									write(bw, "{{DropsLine|Name="+ItemDefinition.forId(tableSorted[i2][i3].getId()).getName()+"|Quantity=" + tableSorted[i2][i3].getMinimum() + "-" + tableSorted[i2][i3].getMaximum() + "|Rarity=uncommon}}");
//								} else {
//									write(bw, "{{DropsLine|Name="+ItemDefinition.forId(tableSorted[i2][i3].getId()).getName()+"|Quantity=1|Rarity=uncommon}}");
//								}
//							}
//							bw.newLine();
//							bw.newLine();
//						} else if (i2 == 3) {
//							write(bw, "{{DropsTableHeader|Name=Rare Drops (1/" + NpcDefinition.forId(i).getRare() + ")}}");
//							for (int i3 = 0; i3 < tableSorted[i2].length; i3++) {
//								if (tableSorted[i2][i3].getMaximum() != tableSorted[i2][i3].getMinimum()) {
//									write(bw, "{{DropsLine|Name="+ItemDefinition.forId(tableSorted[i2][i3].getId()).getName()+"|Quantity=" + tableSorted[i2][i3].getMinimum() + "-" + tableSorted[i2][i3].getMaximum() + "|Rarity=rare}}");
//								} else {
//									write(bw, "{{DropsLine|Name="+ItemDefinition.forId(tableSorted[i2][i3].getId()).getName()+"|Quantity=1|Rarity=rare}}");
//								}
//							}
//							bw.newLine();
//							bw.newLine();
						} else if (i2 == 4) {
							write(bw, "{{DropsTableHeader|Name=Very Rare Drops (1/" + NpcDefinition.forId(i).getVeryRare() + ")}}");
							for (int i3 = 0; i3 < tableSorted[i2].length; i3++) {
								if (tableSorted[i2][i3].getMaximum() != tableSorted[i2][i3].getMinimum()) {
									write(bw, "{{DropsLine|Name="+ItemDefinition.forId(tableSorted[i2][i3].getId()).getName()+"|Quantity=" + tableSorted[i2][i3].getMinimum() + "-" + tableSorted[i2][i3].getMaximum() + "|Rarity=very rare}}");
								} else {
									write(bw, "{{DropsLine|Name="+ItemDefinition.forId(tableSorted[i2][i3].getId()).getName()+"|Quantity=1|Rarity=very rare}}");
								}
							}
							bw.newLine();
							bw.newLine();
						} else if (i2 == 5) {
							write(bw, "{{DropsTableHeader|Name=Epic Drops (1/" + NpcDefinition.forId(i).getEpicRare() + ")}}");
							for (int i3 = 0; i3 < tableSorted[i2].length; i3++) {
								if (tableSorted[i2][i3].getMaximum() != tableSorted[i2][i3].getMinimum()) {
									write(bw, "{{DropsLine|Name="+ItemDefinition.forId(tableSorted[i2][i3].getId()).getName()+"|Quantity=" + tableSorted[i2][i3].getMinimum() + "-" + tableSorted[i2][i3].getMaximum() + "|Rarity=epic}}");
								} else {
									write(bw, "{{DropsLine|Name="+ItemDefinition.forId(tableSorted[i2][i3].getId()).getName()+"|Quantity=1|Rarity=epic}}");
								}
							}
							bw.newLine();
							bw.newLine();
						}
					}
				}
				write(bw, "");
			}
			bw.close();
			System.out.println("Successfully finished dumping drops into lists/drops.txt");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes to a BufferedWriter
	 * @param bw
	 * @param content
	 */
	public static void write(BufferedWriter bw, String content) {
		try {
			bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
