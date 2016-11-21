package com.chaos.world.content.pos;

import com.chaos.model.Item;
import com.chaos.model.container.impl.POSContainer;
import com.chaos.model.container.impl.Shop;
import com.chaos.model.input.impl.EnterAmountToBuyFromShop;
import com.chaos.model.input.impl.EnterAmountToSellToShop;
import com.chaos.model.input.impl.SearchByItemPOS;
import com.chaos.world.entity.impl.player.Player;

import java.util.ArrayList;

/**
 * The Chaos Player Owned Shops
 * @Author Jonny
 */
public class PlayerOwnedShops {

    private Player player;

    private String inShop;

    ArrayList<String> owners = new ArrayList<>(100);
    ArrayList<String> captions = new ArrayList<>(100);
    ArrayList<Integer> itemIds = new ArrayList<>(100);
    ArrayList<Long> prices = new ArrayList<>(100);

    private ShopItems shopItems = new ShopItems();

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
     * Get the sshop tiems you are currently viewing.
     * @return
     */
    public ShopItems getShopItems() {
        return this.shopItems;
    }

    /**
     * Get who's shop you are in
     * @return
     */
    public String inShop() {
        return this.inShop;
    }

    /**
     * Set who's shop you are in
     * @param shop
     */
    public void setShop(String shop) {
        this.inShop = shop;
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
        if(buttonId >= -24062 && buttonId <= -23666) {
            int index = 100 - (((-23666 - buttonId) / 4) + 1);
            int arrayIndex = this.owners.size() - 1;
            if(index <= arrayIndex) {
                System.out.println("Open shop");
                this.setShop(this.owners.get(index));
                Player test = GrabShopItems.grabShopItems(player, this.owners.get(index));
                if(test != null) {
                    POSContainer.POSManager.addShop(0, this.owners.get(index), this.owners.get(index) + " - " + this.captions.get(index) + "", this.getShopItems().getItems());
                    POSContainer.POSManager.getShops().get(0).open(getPlayer(), true);
                }
            }
            return true;
        }
        switch(buttonId) {
            case -24073:
                getPlayer().setInputHandling(new SearchByItemPOS());
                getPlayer().getPacketSender().sendEnterInputPrompt("Enter a item name:");
                return true;
        }
        return false;
    }

    /**
     * Adds shop details to your searched shops.
     * @param owner
     * @param caption
     * @param itemId
     * @param price
     */
    public void addShop(String owner, String caption, int itemId, long price) {
        this.owners.add(owner);
        this.captions.add(caption);
        this.itemIds.add(itemId);
        this.prices.add(price);
    }

    /**
     * Clears all search results.
     */
    public void clearSearch() {
        getPlayer().getPacketSender().sendString(41409, ":clearinterface:");
        this.owners.clear();
        this.captions.clear();
        this.itemIds.clear();
        this.prices.clear();
    }
}
