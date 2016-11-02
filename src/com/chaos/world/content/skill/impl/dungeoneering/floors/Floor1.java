package com.chaos.world.content.skill.impl.dungeoneering.floors;

import com.chaos.model.Position;
import com.chaos.model.StaffRights;
import com.chaos.world.content.skill.impl.dungeoneering.Floor;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;

/**
 * Handles the first dungeoneering floor
 * @Author Jonny
 */
public class Floor1 extends Floor {

    private Player player;

    public Floor1(Player player) {
        this.player = player;
    }

    private NPC[] npcs = new NPC[] {
        new NPC(1, new Position(1, 1))
    };

    @Override
    public void enterFloor() {

    }

    @Override
    public NPC[] getNpcs() {
        return this.npcs;
    }

    @Override
    public int getFloorId() {
        return 1;
    }

}
