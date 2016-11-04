package com.chaos.world.content.skill.impl.dungeoneering;

import com.chaos.GameServer;
import com.chaos.GameSettings;
import com.chaos.model.GroundItem;
import com.chaos.model.Item;
import com.chaos.model.Position;
import com.chaos.util.Misc;
import com.chaos.world.content.skill.impl.dungeoneering.floors.Floor1;
import com.chaos.world.entity.impl.GroundItemManager;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;

public class Dungeoneering {

    /**
     * Tells you what stage you are on in your Dungeon
     */
    public enum DungeonStage {
        DEFAULT(),
        ENTERED(),
        SPAWNED_BOSS(),
        KILLED_BOSS(),

        DungeonStage() {

        }
    }

    private Player player;
    private Floor floor;
    private int kills;
    private int deaths;
    private Dungeoneering.DungeonStage dungeonStage = Dungeoneering.DungeonStage.DEFAULT;
    private Player[] party = {
            null,
            null,
            null,
            null,
    };

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
     * Get the whole dungeoneering party
     * @return
     */
    public Player[] getParty() {
        return this.party;
    }

    /**
     * Set someone to a certain party slot
     * @param index
     * @param other
     */
    public void setParty(int index, Player other) {
        this.party[index] = other;
    }

    /**
     * Add a player to the dungeoneering party
     * @param other
     */
    public void addToParty(Player other) {
        for(int i = 0; i < getParty().length; i++) {
            if(getParty()[i] == null) {
                setParty(i, other);
                break;
            }
        }
    }

    /**
     * Check if the dungeoneering party is full
     * @return
     */
    public boolean isPartyFull() {
        for(int i = 0; i < getParty().length; i++) {
            if(getParty()[i] == null) {
               return false;
            }
        }
        return true;
    }

    /**
     * Get a person in your party
     * @param index
     * @return
     */
    public Player getParty(int index) {
        return this.party[index];
    }

    /**
     * Get the amount of people in the party.
     * @return
     */
    public int getAmountInParty() {
        int amountInParty = 0;
        for(int i = 0; i < getParty().length; i++) {
            if(getParty()[i] !=  null) {
                amountInParty++;
            }
        }
        return amountInParty;
    }

    /**
     * Updates the party interface
     */
    public void updatePartyInterface() {
        int startString = 26236;
        for(int i = 0; i < getParty().length; i++) {
            //player.getPacketSender().sendString(26236, getParty);
            //26236++;
        }
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
        this.kills = 10;
        this.deaths = 2;
        this.party = new Player[] {
                null,
                null,
                null,
                null,
        };
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
        player.getDungeoneering().setDungeonStage(DungeonStage.DEFAULT);
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

    /**
     * Check if you can enter the dungeon
     * @return
     */
    public boolean canEnterDungeon() {
        if(GameServer.isUpdating()) {
            player.getPacketSender().sendMessage("You can't enter a dungeon while an update is happening!");
            return false;
        }
        String plrCannotEnter = null;
        if(player.getSummoning().getFamiliar() != null) {
            player.getPacketSender().sendMessage("You must dismiss your familiar before being allowed to enter a dungeon.");
            return false;
        }
        for(Item t : player.getEquipment().getItems()) {
            if(t != null && t.getId() > 0 && t.getId() != 15707) {
                plrCannotEnter = player.getUsername();
            }
        }
        for(Item t : player.getInventory().getItems()) {
            if(t != null && t.getId() > 0 && t.getId() != 15707) {
                plrCannotEnter = player.getUsername();
            }
        }
        if(plrCannotEnter != null) {
            player.getPacketSender().sendMessage("Your team cannot enter the dungeon because "+plrCannotEnter+" hasn't banked").sendMessage("all of their items.");
            return false;
        }
        return true;
    }

    /**
     * Gets the floor you are currently on for dungeoneering
     * @return
     */
    public int getZ() {
        return this.getPlayer().getIndex() * 4;
    }

    public Item[] MINION_REWARDS = {
        new Item(18173, 1),
        new Item(18173, 1),
        new Item(18159, 2),
        new Item(2436, 1),
        new Item(2440, 1),
        new Item(2442, 1),
        new Item(2444, 1),
        new Item(3040, 1),
        new Item(18165, 1),
        new Item(18165, 1),
        new Item(2434, 1)
    };

    /**
     * Gives the reward for the dungeoneering minions
     */
    public void handleMinionReward(NPC npc) {
        //Bones
        GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526, 1), npc.getPosition(), player.getUsername(), false, 150, false, 200));

        //Random reward
        GroundItemManager.spawnGroundItem(player, new GroundItem(MINION_REWARDS[Misc.inclusiveRandom(MINION_REWARDS.length - 1)], npc.getPosition(), player.getUsername(), false, 150, false, 200));

        //War key
        int chance = Misc.random(player.getDungeoneering().getFloor().getRequiredKills());
        if(chance == 1) {
            GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(18236, 1), npc.getPosition(), player.getUsername(), false, 150, false, 200));
        }
    }

}
