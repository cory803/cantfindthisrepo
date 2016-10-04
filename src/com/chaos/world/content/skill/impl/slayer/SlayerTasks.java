package com.chaos.world.content.skill.impl.slayer;

/**
 * All Chaos Slayer Tasks
 * @Author Jonny
 */

public enum SlayerTasks {

    /**
     * Rock crabs slayer task
     */
    ROCK_CRABS(new int[] {1265}, 10, SlayerMasters.TURAEL, 15, 50, -1),
    MEN(new int[] {1}, 5, SlayerMasters.TURAEL, 15, 50, -1);


    SlayerTasks(int[] npcId, int experience, SlayerMasters slayerMaster, int minimumAmount, int maximumAmount, int equipmentId) {
        this.npcId = npcId;
        this.experience = experience;
        this.slayerMaster = slayerMaster;
        this.minimumAmount = minimumAmount;
        this.maximumAmount = maximumAmount;
        this.equipmentId = equipmentId;
    }

    private SlayerMasters slayerMaster;
    private int[] npcId;
    private int experience;
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
     * per kill.
     * @return
     */
    public int getExperience() {
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
