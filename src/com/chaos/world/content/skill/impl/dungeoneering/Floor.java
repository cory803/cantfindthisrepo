package com.chaos.world.content.skill.impl.dungeoneering;

import com.chaos.world.entity.impl.npc.NPC;

/**
 * Handles the Global Floors
 * @Author Jonny
 */
public abstract class Floor {

    /**
     * Enters the dungeoneering floor
     */
    public abstract void enterFloor();

    /**
     * Gets all the npc spawns for the floor
     * @return
     */
    public abstract NPC[] getNpcs();

    public abstract int getFloorId();

}
