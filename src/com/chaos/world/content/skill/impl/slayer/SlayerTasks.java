package com.chaos.world.content.skill.impl.slayer;

/**
 * All Chaos Slayer Tasks
 * @Author Jonny
 */

public enum SlayerTasks {

    /**
     * Turael's slayer tasks
     * @SlayerMaster.TURAEL
     */
    BANSHEE(new int[] {1612}, new int[] {22}, SlayerMasters.TURAEL, 15, 50, 4166),
    BAT(new int[] {412, 78}, new int[] {8, 32}, SlayerMasters.TURAEL, 15, 50, -1),
    CHICKEN(new int[] {41}, new int[] {3}, SlayerMasters.TURAEL, 15, 50, -1),
    ROCK_CRAB(new int[] {1265}, new int[] {20}, SlayerMasters.TURAEL, 15, 50, -1),
    EXPERIMENT(new int[] {1677}, new int[] {27}, SlayerMasters.TURAEL, 15, 50, -1),
    YAK(new int[] {5529}, new int[] {24}, SlayerMasters.TURAEL, 15, 50, -1),
    CRAWLING_HAND(new int[] {1648, 1657}, new int[] {10, 16}, SlayerMasters.TURAEL, 15, 50, -1),
    SPIDER(new int[] {59, 134, 4400}, new int[] {13, 72, 60}, SlayerMasters.TURAEL, 15, 50, -1),
    COW(new int[] {81}, new int[] {8}, SlayerMasters.TURAEL, 15, 50, -1),
    GOBLIN(new int[] {4261, 4262, 4263, 4269, 4270, 4407, 4411, 13469, 2685, 2688, 3583}, new int[] {8, 8, 8, 8, 8, 8, 14, 28, 21, 58}, SlayerMasters.TURAEL, 15, 50, -1),

    /**
     * Mazchna's slayer tasks
     * @SlayerMaster.MAZCHNA
     */
    SKELETON(new int[] {90, 2036, 4385, 4386, 6104, 6105}, new int[] {28, 110, 72, 90, 160, 160}, SlayerMasters.MAZCHNA, 15, 65, -1),
    CHAOS_DRUID(new int[] {181}, new int[] {32}, SlayerMasters.MAZCHNA, 15, 65, -1),
    ZOMBIE(new int[] {73, 75, 76, 5377, 5379}, new int[] {18, 34, 36, 45, 60}, SlayerMasters.MAZCHNA, 15, 65, -1),
    GHOST(new int[] {103, 4387}, new int[] {25, 90}, SlayerMasters.MAZCHNA, 15, 65, -1),
    HILL_GIANT(new int[] {117}, new int[] {36}, SlayerMasters.MAZCHNA, 15, 65, -1),

    /**
     * Vannaka's slayer tasks
     * @SlayerMasters.VANNAKA
     */
    LESSER_DEMON(new int[] {82}, new int[] {90}, SlayerMasters.VANNAKA, 15, 75, -1),
    FIRE_GIANT(new int[] {110, 1583}, new int[] {110, 110}, SlayerMasters.VANNAKA, 15, 75, -1),
    CHAOS_DWARF(new int[] {119}, new int[] {55}, SlayerMasters.VANNAKA, 15, 75, -1),
    MAGIC_AXE(new int[] {127}, new int[] {60}, SlayerMasters.VANNAKA, 15, 75, -1),
    BABY_BLUE_DRAGON(new int[] {52}, new int[] {60}, SlayerMasters.VANNAKA, 15, 75, -1),
    GREEN_DRAGON(new int[] {941, 5362}, new int[] {95, 250}, SlayerMasters.VANNAKA, 15, 75, -1),
    OGRE(new int[] {115}, new int[] {95}, SlayerMasters.VANNAKA, 15, 75, -1),
    INFERNAL_MAGE(new int[] {1643}, new int[] {80}, SlayerMasters.VANNAKA, 15, 75, -1),
    BLOODVELD(new int[] {1618}, new int[] {90}, SlayerMasters.VANNAKA, 15, 75, -1),

    /**
     * Chaeldar's slayer tasks
     * @SlayerMasters.CHAELDAR
     */
    ABERRANT_SPECTRE(new int[] {1604}, new int[] {115}, SlayerMasters.CHAELDAR, 15, 90, -1),
    GARGOYLE(new int[] {1610}, new int[] {125}, SlayerMasters.CHAELDAR, 15, 90, -1),
    NECHRYAEL(new int[] {1613}, new int[] {125}, SlayerMasters.CHAELDAR, 15, 90, -1),
    HELLHOUND(new int[] {49}, new int[] {140}, SlayerMasters.CHAELDAR, 15, 90, -1),
    BLUE_DRAGON(new int[] {52, 55}, new int[] {60, 125}, SlayerMasters.CHAELDAR, 15, 90, -1),
    GREATER_DEMON(new int[] {83}, new int[] {115}, SlayerMasters.CHAELDAR, 15, 90, -1),
    TZHAAR(new int[] {2591, 2600, 2605, 2611}, new int[] {110, 85, 140, 170}, SlayerMasters.CHAELDAR, 15, 90, -1),

    /**
     * Nieve's slayer tasks
     * @SlayerMasters.NIEVE
     */
    BLACK_DEMON(new int[] {84}, new int[] {200}, SlayerMasters.NIEVE, 15, 125, -1),
    FROST_DRAGON(new int[] {51}, new int[] {200}, SlayerMasters.NIEVE, 15, 125, -1),
    ABYSSAL_DEMON(new int[] {1615}, new int[] {140}, SlayerMasters.NIEVE, 15, 125, -1),
    RED_DRAGON(new int[] {53, 3588}, new int[] {175, 65}, SlayerMasters.NIEVE, 15, 125, -1),
    IRON_DRAGON(new int[] {1591}, new int[] {225}, SlayerMasters.NIEVE, 15, 125, -1),
    STEEL_DRAGON(new int[] {1592}, new int[] {275}, SlayerMasters.NIEVE, 15, 125, -1),
    JUNGLE_STRYKEWYRM(new int[] {9467}, new int[] {135}, SlayerMasters.NIEVE, 15, 125, -1),
    GLACOR(new int[] {1382}, new int[] {250}, SlayerMasters.NIEVE, 15, 125, -1),

    /**
     * Duradel's slayer tasks
     * @SlayerMasters.DURADEL
     */
    GENERAL_GRAARDOR(new int[] {6260, 6261, 6263, 6265}, new int[] {700, 200, 200, 200}, SlayerMasters.DURADEL, 15, 50, -1),
    COMMANDER_ZILYANA(new int[] {6247, 6252, 6248, 6250}, new int[] {700, 200, 200, 200}, SlayerMasters.DURADEL, 15, 50, -1),
    KRIL_TSUTSAROTH(new int[] {6203, 6208, 6206, 6204}, new int[] {700, 200, 200, 200}, SlayerMasters.DURADEL, 15, 50, -1),
    KREE_ARRA(new int[] {6222, 6227, 6225, 6223}, new int[] {700, 200, 200, 200}, SlayerMasters.DURADEL, 15, 50, -1),
    GANODERMIC_BEAST(new int[] {130}, new int[] {450}, SlayerMasters.DURADEL, 15, 50, -1),
    DAGANNOTH_KING(new int[] {2882, 2881, 2883}, new int[] {400, 400, 400}, SlayerMasters.DURADEL, 15, 50, -1),
    CORPOREAL_BEAST(new int[] {8133}, new int[] {900}, SlayerMasters.DURADEL, 15, 50, -1),
    KRAKEN(new int[] {502}, new int[] {450}, SlayerMasters.DURADEL, 15, 50, -1),
    CERBERUS(new int[] {5866}, new int[] {450}, SlayerMasters.DURADEL, 15, 50, -1),
    KALPHITE_QUEEN(new int[] {1158}, new int[] {450}, SlayerMasters.DURADEL, 15, 50, -1),
    PHOENIX(new int[] {8549}, new int[] {400}, SlayerMasters.DURADEL, 15, 50, -1),
    BANDOS_AVATAR(new int[] {4540}, new int[] {400}, SlayerMasters.DURADEL, 15, 50, -1),
    BORK(new int[] {5871}, new int[] {800}, SlayerMasters.DURADEL, 15, 50, -1),
    SLASH_BASH(new int[] {2060}, new int[] {300}, SlayerMasters.DURADEL, 15, 50, -1),
    SCORPIA(new int[] {2001}, new int[] {750}, SlayerMasters.DURADEL, 15, 50, -1),
    CALLISTO(new int[] {6609}, new int[] {750}, SlayerMasters.DURADEL, 15, 50, -1),
    VENENATIS(new int[] {2000}, new int[] {750}, SlayerMasters.DURADEL, 15, 50, -1),
    VETION(new int[] {6611}, new int[] {750}, SlayerMasters.DURADEL, 15, 50, -1),
    CHAOS_FANATIC(new int[] {6619}, new int[] {650}, SlayerMasters.DURADEL, 15, 50, -1),
    CRAZY_ARCHAEOLOGIST(new int[] {6618}, new int[] {650}, SlayerMasters.DURADEL, 15, 50, -1);


    SlayerTasks(int[] npcId, int[] experience, SlayerMasters slayerMaster, int minimumAmount, int maximumAmount, int equipmentId) {
        this.npcId = npcId;
        this.experience = experience;
        this.slayerMaster = slayerMaster;
        this.minimumAmount = minimumAmount;
        this.maximumAmount = maximumAmount;
        this.equipmentId = equipmentId;
    }

    private SlayerMasters slayerMaster;
    private int[] npcId;
    private int[] experience;
    private int minimumAmount;
    private int maximumAmount;
    private int equipmentId;

    /**
     * Get the required slayer master(s) for the task.
     * @return
     */
    public SlayerMasters getSlayerMaster() {
        return this.slayerMaster;
    }

    /**
     * Get the required npc id for the task
     * depending on the index you choose.
     * @param index
     * @return
     */
    public int getNpcId(int index) {
        return this.npcId[index];
    }

    /**
     * Get all the npc ids required
     * for the task you are on.
     * @return
     */
    public int[] getNpcIds() {
        return this.npcId;
    }

    /**
     * Get the experience that you gain
     * per kill. Depends on the index.
     * @return
     */
    public int getExperience(int index) {
        return this.experience[index];
    }

    /**
     * Gets a raw version of all possible slayer exps.
     * @return
     */
    public int[] getExperience() {
        return this.experience;
    }

    /**
     * Gets the minimum amount that can be assigned
     * to your slayer assignment to kill.
     * @return
     */
    public int getMinimumAmount() {
        return this.minimumAmount;
    }

    /**
     * Gets the maximum amount that can be assigned
     * to your slayer assignment to kill.
     * @return
     */
    public int getMaximumAmount() {
        return this.maximumAmount;
    }

    /**
     * Gets the item id for any potential required
     * equipment to kill your slayer monster.
     * @return
     */
    public int getEquipmentId() {
        return this.equipmentId;
    }
}
