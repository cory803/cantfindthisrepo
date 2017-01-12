package com.runelive.util.wiki;

import com.runelive.ect.dropwriting.Drop;
import com.runelive.ect.dropwriting.DropManager;
import com.runelive.ect.dropwriting.DropTable;
import com.runelive.model.definitions.ItemDefinition;
import com.runelive.model.definitions.NpcDefinition;
import com.runelive.world.World;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Jonathan on 9/1/2016.
 */
public class WikiDumper {

    public static ArrayList<Integer> NPCS_TO_DUMP = new ArrayList<Integer>();

    public static void dumpNpcDefinitions() {
        for (int i = 0; i <= World.getNpcs().size(); i++) {
            if(World.getNpcs().get(i) == null) {
                continue;
            }
            if(!NPCS_TO_DUMP.contains(World.getNpcs().get(i).getId()) && World.getNpcs().get(i).getDefinition().isAttackable()) {
                NPCS_TO_DUMP.add(World.getNpcs().get(i).getId());
                System.out.println("Dumping npc definition " + World.getNpcs().get(i).getId() + "");
                WikiDumper.dumpNpcDropDefinition(World.getNpcs().get(i).getId());
            }
        }
    }

    public static void dumpNpcDropDefinition(int npcId) {
        System.out.println("Starting wiki dump of npc drop ID: "+npcId);
        ArrayList<String> lines = getNpcPage(NpcDefinition.forId(npcId).getName().replaceAll(" ", "_"));
        Iterator<String> iterator = lines.iterator();
        try {
            while (iterator.hasNext()) {
                String line = iterator.next();
                if(line.contains("</td><td style=\"text-align:left;\"> ")) {
                    String[] itemNameVars = line.split("title=\"");
                    String itemName = itemNameVars[1];
                    itemName = itemName.substring(0, itemName.indexOf("\"> "));
                    line = iterator.next();
                    String[] valueVars = line.split("</td><td> ");
                    String valueAmount = valueVars[1];
                    String value1 = "none";
                    String value2 = "none";
                    boolean noted = false;
                    int itemId = 0;
                    if(valueAmount.contains("–")) {
                        if(valueAmount.contains(";")) {
                            switch(npcId) {
                                case 4381:
                                case 4382:
                                case 4383:
                                    if(itemName.toLowerCase().equals("blood rune")) {
                                        value1 = "3";
                                        value2 = "16";
                                    }
                                    break;
                                case 6225:
                                    if(itemName.toLowerCase().equals("coins")) {
                                        value1 = "50";
                                        value2 = "500";
                                    }
                                    break;
                                case 1154:
                                    if(itemName.toLowerCase().equals("air rune")) {
                                        value1 = "15";
                                        value2 = "29";
                                    }
                                    if(itemName.toLowerCase().equals("fire rune")) {
                                        value1 = "6";
                                        value2 = "60";
                                    }
                                    break;
                            }
                        } else {
                            String[] valueVars2 = valueAmount.split("–");
                            value1 = valueVars2[0];
                            value2 = valueVars2[1];
                        }
                    } else {
                        value1 = valueAmount;
                    }
                    if(value1.contains(" (noted)") || value2.contains(" (noted)")) {
                        noted = true;
                        value1 = value1.replace(" (noted)", "");
                        value2 = value2.replace(" (noted)", "");
                    }
                    line = iterator.next();
                    String[] rarityVars = line.split("; color:#000000;\"> ");
                    String rarityName = rarityVars[1];
                    if(rarityName.toLowerCase().contains("very rare")) {
                        rarityName = "epic";
                    }
                    if(rarityName.contains("<small>")) {
                        rarityName = rarityName.substring(0, rarityName.indexOf(" <small>"));
                        if(rarityName.toLowerCase().contains("rare")) {
                            rarityName = "very rare";
                        }
                    }
                    if(rarityName.contains(" <sup id=\"cite_ref")) {
                        rarityName = rarityName.substring(0, rarityName.indexOf(" <sup id=\"cite_ref"));
                    }
                    Drop.RARITY rarity = Drop.RARITY.COMMON;
                    switch(rarityName.toLowerCase()) {
                        case "always":
                            rarity = Drop.RARITY.ALWAYS;
                            break;
                        case "common":
                            rarity = Drop.RARITY.COMMON;
                            break;
                        case "uncommon":
                            rarity = Drop.RARITY.UNCOMMON;
                            break;
                        case "rare":
                            rarity = Drop.RARITY.RARE;
                            break;
                        case "very rare":
                            rarity = Drop.RARITY.VERY_RARE;
                            break;
                        case "epic":
                            rarity = Drop.RARITY.EPIC;
                            break;
                    }
                    if(itemName.contains(" axe")) {
                       itemName = itemName.replace("axe", "hatchet");
                    }
                    if(itemName.contains("Runite ")) {
                       itemName = itemName.replace("Runite", "Rune");
                    }
                    if(itemName.contains("half of key")) {
                       itemName = itemName.replace("half of key", "half of a key");
                    }
                    if(itemName.contains(" leaf")) {
                       itemName = itemName.replace(" leaf", "");
                    }
                    if(itemName.contains("ranarr weed")) {
                       itemName = itemName.replace(" weed", "");
                    }
                    if(itemName.contains("Ranarr weed")) {
                       itemName = itemName.replace("Ranarr weed", "Grimy ranarr weed");
                    }
                    if(itemName.contains("Mystic robe bottom (blue)")) {
                       itemName = itemName.replace("Mystic robe bottom (blue)", "Mystic robe bottom");
                    }
                    if(itemName.contains("Blue d'hide vamb")) {
                       itemName = itemName.replace("Blue d'hide vamb", "Blue d'hide vambraces");
                    }
                    if(itemName.contains("Rune ore")) {
                       itemName = itemName.replace("Rune ore", "Runite ore");
                    }
                    if(itemName.contains("Mystic robe top (blue)")) {
                       itemName = itemName.replace("Mystic robe top (blue)", "Mystic robe top");
                    }
                    if(itemName.contains("Dwarf weed")) {
                       itemName = itemName.replace("Dwarf weed", "Grimy dwarf weed");
                    }
                    if(itemName.contains("\" class=\"mw-redirect")) {
                       itemName = itemName.replace("\" class=\"mw-redirect", "");
                    }
                    if(itemName.contains("Mystic boots (blue)")) {
                       itemName = itemName.replace("Mystic boots (blue)", "Mystic boots");
                    }
                    if(itemName.contains("Mystic gloves (blue)")) {
                       itemName = itemName.replace("Mystic gloves (blue)", "Mystic gloves");
                    }
                    if(itemName.contains("Blue wizard hat")) {
                       itemName = itemName.replace("Blue wizard hat", "Wizard hat");
                    }
                    if(itemName.contains("Blue wizard robe")) {
                       itemName = itemName.replace("Blue wizard robe", "Wizard robe");
                    }
                    if(itemName.contains("Wizard hat (black)")) {
                       itemName = itemName.replace("Wizard hat (black)", "Wizard hat");
                    }
                    if(itemName.contains("Adamantite bar")) {
                       itemName = itemName.replace("Adamantite bar", "Adamant bar");
                    }
                    if(itemName.contains("strength(3)")) {
                       itemName = itemName.replace("strength(3)", "strength (3)");
                    }
                    if(itemName.contains("attack(3)")) {
                       itemName = itemName.replace("attack(3)", "attack (3)");
                    }
                    if(itemName.contains("defence(3)")) {
                       itemName = itemName.replace("defence(3)", "defence (3)");
                    }
                    if(itemName.contains("potion(3)")) {
                       itemName = itemName.replace("potion(3)", "potion (3)");
                    }
                    if(itemName.contains(" strength(1)")) {
                       itemName = itemName.replace(" strength(1)", " strength (1)");
                    }
                    if(itemName.contains(" defence(2)")) {
                       itemName = itemName.replace(" defence(2)", " defence (2)");
                    }
                    if(itemName.contains(" defence(")) {
                       itemName = itemName.replace(" defence(", " defence (");
                    }
                    if(itemName.contains(" restore(")) {
                       itemName = itemName.replace(" restore(", " restore (");
                    }
                    if(itemName.contains("Mystic hat (dark)")) {
                       itemName = itemName.replace("Mystic hat (dark)", "Mystic hat");
                    }
                    if(itemName.contains("Mystic boots (dark)")) {
                       itemName = itemName.replace("Mystic boots (dark)", "Mystic boots");
                    }
                    if(itemName.contains(" restore(3)")) {
                       itemName = itemName.replace(" restore(3)", " restore (3)");
                    }
                    if(itemName.contains(" potion(")) {
                       itemName = itemName.replace(" potion(", " potion (");
                    }
                    if(itemName.contains(" strength(")) {
                       itemName = itemName.replace(" strength(", " strength (");
                    }
                    if(itemName.contains(" brew(3)")) {
                       itemName = itemName.replace(" brew(3)", " brew (3)");
                    }
                    if(itemName.contains("Rune bolt")) {
                       itemName = itemName.replace("Rune bolt", "Runite bolt");
                    }
                    if(itemName.contains("Rune limb")) {
                       itemName = itemName.replace("Rune limb", "Runite limb");
                    }
                    if(itemName.contains("Red d'hide vamb")) {
                       itemName = itemName.replace("Red d'hide vamb", "Red d'hide vambraces");
                    }
                    if(itemName.contains("Seers ring")) {
                       itemName = itemName.replace("Seers ring", "Seers' ring");
                    }
                    if(itemName.contains("Antifire potion")) {
                       itemName = itemName.replace("Antifire potion", "Antifire");
                    }
                    if(itemName.contains(" attack(")) {
                       itemName = itemName.replace(" attack(", " attack (");
                    }
                    if(itemName.contains(" brew(")) {
                       itemName = itemName.replace(" brew(", " brew (");
                    }
                    if(itemName.contains("Mystic robe bottom (dark)")) {
                       itemName = itemName.replace("Mystic robe bottom (dark)", "Mystic robe bottom");
                    }
                    if(itemName.contains("Mystic robe top (dark)")) {
                       itemName = itemName.replace("Mystic robe top (dark)", "Mystic robe top");
                    }
                    if(itemName.contains("Mystic gloves (dark)")) {
                       itemName = itemName.replace("Mystic gloves (dark)", "Mystic gloves");
                    }
                    if(itemName.contains("Antipoison(")) {
                       itemName = itemName.replace("Antipoison(", "Antipoison (");
                    }
                    if(itemName.contains("Grimy dwarf weed seed")) {
                       itemName = itemName.replace("Grimy dwarf weed seed", "Dwarf weed seed");
                    }
                    if(itemName.contains("Grimy ranarr weed")) {
                       itemName = itemName.replace("Grimy ranarr weed", "Grimy ranarr");
                    }
                    if(itemName.contains("Archers ring")) {
                       itemName = itemName.replace("Archers ring", "Archers' ring");
                    }

                    boolean found = false;
                    for (int i = 0; i < ItemDefinition.getMaxAmountOfItems() - 1; i++) {
                        if (found) {
                            continue;
                        }
                        if (ItemDefinition.forId(i).getName().toLowerCase().equalsIgnoreCase(itemName.toLowerCase())) {
                            itemId = i;
                            found = true;
                        }
                    }
                    if (!found) {
                        System.out.println("No drop found for npc id "+npcId+", item name: "+itemName);
                        continue;
                    }
                    if(value2.equals("none")) {
                        value2 = value1;
                    }
                    if(value1.toLowerCase().equals("unknown") || value2.toLowerCase().equals("unknown")) {
                        value1 = value1.replaceAll("Unknown", "1");
                        value2 = value2.replaceAll("Unknown", "1");
                    }
                    value1 = value1.replaceAll(",", "");
                    value2 = value2.replaceAll(",", "");

                    if(noted) {
                        ItemDefinition definition1 = ItemDefinition.forId(itemId);
                        ItemDefinition definition2 = ItemDefinition.forId(itemId + 1);
                        if(!definition1.isNoted() && definition2.isNoted() && definition1.getName().equals(definition2.getName())) {
                            itemId = itemId + 1;
                        }
                    }

                    if(value1.contains(";")) {
                        value1 = value1.substring(0, value1.indexOf(";"));
                        value2 = value2.substring(value2.lastIndexOf(";") + 2);
                    }

                    if(itemId == 617) { //Coins fix
                        itemId = 995;
                    }
                    //System.out.println("Item name: " + itemName + ", Amount: " + value1 + "-" + value2 + ", Rarity: " + rarityName);

                    NpcDefinition npcDef = NpcDefinition.forId(npcId);
                    if(npcDef.getCombatLevel() < 20) {
                        DropManager.addCharm(npcId, DropTable.CHARM.GOLD, 1, 100);
                    } else if(npcDef.getCombatLevel() < 50) {
                        DropManager.addCharm(npcId, DropTable.CHARM.GOLD, 1, 50);
                        DropManager.addCharm(npcId, DropTable.CHARM.GREEN, 1, 50);
                    } else if(npcDef.getCombatLevel() < 100) {
                        DropManager.addCharm(npcId, DropTable.CHARM.CRIMS, 1, 50);
                        DropManager.addCharm(npcId, DropTable.CHARM.GREEN, 1, 25);
                    } else if(npcDef.getCombatLevel() < 150) {
                        DropManager.addCharm(npcId, DropTable.CHARM.CRIMS, 1, 30);
                        DropManager.addCharm(npcId, DropTable.CHARM.GREEN, 1, 25);
                        DropManager.addCharm(npcId, DropTable.CHARM.BLUE, 1, 50);
                    } else {
                        DropManager.addCharm(npcId, DropTable.CHARM.GREEN, 1, 50);
                        DropManager.addCharm(npcId, DropTable.CHARM.BLUE, 1, 50);
                    }
                    DropManager.addDrop(npcId, itemId, Integer.parseInt(value1), Integer.parseInt(value2), rarity, Drop.CONDITION.NONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getNpcPage(String name) {
        if(name.equals("Grave_scorpion")) {
            name = "Scorpion";
        }
        ArrayList<String> PAGE_LINES = new ArrayList<String>();
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;

        try {
            url = new URL("http://2007.runescape.wikia.com/wiki/"+name);
            is = url.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                PAGE_LINES.add(line);
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                // nothing to see here
            }
        }
        return PAGE_LINES;
    }
}
