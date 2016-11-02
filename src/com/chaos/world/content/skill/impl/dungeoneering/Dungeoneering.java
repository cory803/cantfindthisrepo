package com.chaos.world.content.skill.impl.dungeoneering;

import com.chaos.world.content.diversions.hourly.HourlyDiversion;
import com.chaos.world.content.diversions.hourly.ShootingStar;
import com.chaos.world.content.minigames.impl.Dueling;
import com.chaos.world.content.skill.impl.dungeoneering.floors.Floor1;
import com.chaos.world.entity.impl.player.Player;

public class Dungeoneering {

    /**
     * Tells you what stage you are on in your Dungeon
     */
    public enum DungeonStage {
        DEFAULT(),
        ENTERED(),
        BOSS(),

        DungeonStage() {

        }
    }

    private Player player;
    private Floor floor;
    private Dungeoneering.DungeonStage dungeonStage = Dungeoneering.DungeonStage.DEFAULT;

    /**
     * Obtain what dungeon stage you are on
     *
     * @return
     */
    public Dungeoneering.DungeonStage getDungeonStage() {
        return this.dungeonStage;
    }

    /**
     * Get the player for the dungeon
     * @return
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get your current dungeoneering floor
     * @return
     */
    public Floor getFloor() {
        return this.floor;
    }

    /**
     * Set a new dungeoneering floor
     * @param floor
     */
    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    /**
     * Sets your new dungeon stage.
     *
     * @param stage
     */
    public void setDungeonStage(Dungeoneering.DungeonStage stage) {
        this.dungeonStage = stage;
    }

    public Dungeoneering(Player player) {
        this.player = player;
    }

    /**
     * Change your dungeoneering floor
     */
    public void changeFloor() {
        if(getFloor().getFloorId() == 1) {
            setFloor(new Floor1(player));
        }
        player.getPacketSender().sendString(26240, ""+getFloor().getFloorId());

    }
}
