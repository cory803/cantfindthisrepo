package com.chaos.world.content.pos;

import com.chaos.model.Item;
import com.chaos.model.input.impl.ChangePassword;
import com.chaos.model.input.impl.SearchByItemPOS;
import com.chaos.world.content.skill.impl.dungeoneering.floors.Floor1;
import com.chaos.world.entity.impl.player.Player;

import java.util.ArrayList;

/**
 * The Chaos Player Owned Shops
 * @Author Jonny
 */
public class PlayerOwnedShops {

    private Player player;

    ArrayList<String> owners = new ArrayList<>(100);
    ArrayList<Integer> itemIds = new ArrayList<>(100);
    ArrayList<Integer> prices = new ArrayList<>(100);

    /**
     * Initiate the default setters.
     * @param player
     */
    public PlayerOwnedShops(Player player) {
        this.player = player;
    }

    /**
     * Get the player for the instance.
     * @return
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Opens the player owned shops search interface
     * @param player
     */
    public void openSearch() {
        getPlayer().getPacketSender().sendString(41409, ":clearinterface:");
        getPlayer().getPacketSender().sendInterface(41409);
        showFeaturedShops();
    }

    /**
     * Show all of the current featured shops
     * @param player
     */
    public void showFeaturedShops() {
        //TODO: Add featured shops
    }

    /**
     * Handles all of the buttons
     * @param buttonId
     * @return
     */
    public boolean handleButtons(int buttonId) {
        switch(buttonId) {
            case -24073:
                getPlayer().setInputHandling(new SearchByItemPOS());
                getPlayer().getPacketSender().sendEnterInputPrompt("Enter a item name:");
                return true;
        }
        return false;
    }

    /**
     * Clears all search results.
     */
    public void clearSearch() {
        getPlayer().getPacketSender().sendString(41409, ":clearinterface:");
        this.owners.clear();
        this.itemIds.clear();
        this.prices.clear();
    }
}
