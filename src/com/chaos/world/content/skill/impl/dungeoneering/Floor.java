package com.chaos.world.content.skill.impl.dungeoneering;

import com.chaos.model.Position;
import com.chaos.world.entity.impl.npc.NPC;

/**
 * Handles the Global Floors
 * @Author Jonny
 */
public abstract class Floor {

    /**
     * Enters the Dungeoneering floor
     */
    public abstract void enterFloor();

    /**
     * Leaves the Dungeoneering floor
     */
    public abstract void leaveFloor();

    /**
     * Fight the boss
     */
    public abstract void fightBoss(int side);

    /**
     * Gets all the npc spawns for the floor
     * @return
     */
    public abstract NPC[] getNpcs();

    /**
     * Gets the floor ID for your current floor
     * @return
     */
    public abstract int getFloorId();

    /**
     * The position in which you start your floor.
     * @return
     */
    public abstract Position getStartPosition();

    /**
     * The amount of kills required to kill the boss.
     * @return
     */
    public abstract int getRequiredKills();

}
