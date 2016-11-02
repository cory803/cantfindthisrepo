package com.chaos.world.content.skill.impl.dungeoneering;

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
     * Sets your new dungeon stage.
     *
     * @param stage
     */
    public void setDungeonStage(Dungeoneering.DungeonStage stage) {
        this.dungeonStage = stage;
    }

    public Dungeoneering(Player player) {
        this.dungeonStage = Dungeoneering.DungeonStage.DEFAULT;
        this.player = player;
    }
}
