package com.chaos.world.content.skill.impl.dungeoneering.floors;

import com.chaos.model.Position;
import com.chaos.util.Misc;
import com.chaos.world.World;
import com.chaos.world.content.skill.impl.dungeoneering.Dungeoneering;
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

    private NPC[] NPCS = new NPC[] {
            //Battlemages
            new NPC(912, new Position(3285, 9170)),
            new NPC(912, new Position(3311, 9174)),
            new NPC(912, new Position(3308, 9188)),

            //Dark wizards
            new NPC(174, new Position(3299, 9170)),
            new NPC(174, new Position(3303, 9171)),
            new NPC(174, new Position(3306, 9180)),

            //Zombies
            new NPC(75, new Position(3300, 9176)),
            new NPC(75, new Position(3297, 9178)),

            //Skeletons
            new NPC(93, new Position(3285, 9179)),

            //Ghosts
            new NPC(103, new Position(3278, 9177)),
            new NPC(103, new Position(3277, 9183)),
            new NPC(103, new Position(3280, 9188)),
            new NPC(103, new Position(3288, 9188)),

            //Giant spiders
            new NPC(60, new Position(3300, 9203)),
            new NPC(60, new Position(3288, 9202)),

            //Poison spiders
            new NPC(134, new Position(3281, 9203)),
            new NPC(134, new Position(3307, 9203)),

    };

    private Position START = new Position(3277, 9171, player.getIndex() * 4);

    @Override
    public void enterFloor() {
        player.getDungeoneering().setDungeonStage(Dungeoneering.DungeonStage.ENTERED);
        for(NPC npc: NPCS) {
            npc.getDefinition().setAggressive(true);
            npc.setWalking(true);
            npc.walkingDistance = 2;
            npc.getDefinition().setRespawnTime(-1);
            World.register(npc);
        }
        player.moveTo(getStartPosition());
    }

    @Override
    public void leaveFloor() {
        player.moveTo(new Position(3464 - Misc.inclusiveRandom(0, 7), 3722 - Misc.inclusiveRandom(0, 4), 0));
        for(NPC npc: NPCS) {
            World.deregister(npc);
        }
        player.getDungeoneering().setKills(0);
        player.getDungeoneering().setDeaths(0);
    }

    @Override
    public void fightBoss(int side) {
        if(side == 0) { //west
            player.moveTo(new Position(3280, 9199, player.getIndex() * 4));
        } else if(side == 1) { //east
            player.moveTo(new Position(3306, 9199, player.getIndex() * 4));
        }
    }

    @Override
    public NPC[] getNpcs() {
        return this.NPCS;
    }

    @Override
    public int getFloorId() {
        return 1;
    }

    @Override
    public Position getStartPosition() {
        return this.START;
    }

    @Override
    public int getRequiredKills() {
        return 10;
    }

}
