package com.chaos.world.content.skill.impl.dungeoneering;

import com.chaos.GameSettings;
import com.chaos.model.Position;
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
    private int kills;
    private int deaths;
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
     * Gets how many kills your party has
     * @return
     */
    public int getKills() {
        return this.kills;
    }

    /**
     * Gets how many deaths your party has.
     * @return
     */
    public int getDeaths() {
        return this.deaths;
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

    /**
     * Sets X amount of kills for your party.
     * @param kills
     */
    public void setKills(int kills) {
        this.kills = kills;
        updateInterface();
    }

    /**
     * Add X amount of kills for your party.
     * @param kills
     */
    public void addKills(int kills) {
        this.kills += kills;
        updateInterface();
    }

    /**
     * Sets X amount of deaths for your party.
     * @param deaths
     */
    public void setDeaths(int deaths) {
        this.deaths = deaths;
        updateInterface();
    }

    /**
     * Add X amount of deaths for your party.
     * @param deaths
     */
    public void addDeaths(int deaths) {
        this.deaths += deaths;
        updateInterface();
    }

    public Dungeoneering(Player player) {
        this.player = player;
        this.floor = new Floor1(player);
        this.kills = 0;
        this.deaths = 0;
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

    /**
     * When using the open party interface tab option.
     * This will change your tab interface and do all the setters.
     */
    public void showTab() {
        player.getPacketSender().sendDungeoneeringTabIcon(true);
        player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 26224);
        player.getPacketSender().sendTab(GameSettings.QUESTS_TAB);
        player.getPacketSender().sendString(26240, ""+getFloor().getFloorId());
    }

    /**
     * Leave the exit room.
     */
    public void leaveExitRoom() {
        player.moveTo(new Position(3457, 3725, 0));
    }

    /**
     * Update the kills/deaths interface
     */
    public void updateInterface() {
        player.getPacketSender().sendString(37508, "Party kills: "+getKills());
        player.getPacketSender().sendString(37509, "Party deaths: "+getDeaths());
    }

    /**
     * Tells you if you have the required amount of kills
     * to proceed onto the boss room.
     * @return
     */
    public boolean hasRequiredKills() {
        return getKills() >= getFloor().getRequiredKills();
    }

}
