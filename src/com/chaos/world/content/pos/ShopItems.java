package com.chaos.world.content.pos;

import com.chaos.model.Item;
import com.chaos.model.input.impl.SearchByItemPOS;
import com.chaos.world.entity.impl.player.Player;

import java.util.ArrayList;

/**
 * The current shop you are viewing.
 * @Author Jonny
 */
public class ShopItems {

    private ArrayList<Long> prices = new ArrayList<>(40);
    private Item[] items = new Item[40];

    /**
     * The prices for the current shop you are viewing.
     * @return
     */
    public ArrayList<Long> getPrices() {
        return this.prices;
    }

    /**
     * The items for the current shop you are viewing.
     * @return
     */
    public Item[] getItems() {
        return this.items;
    }

    /**
     * Clear all the current shop items.
     */
    public void clearAll() {
        this.prices.clear();
        this.items = new Item[40];
    }

    /**
     * Add a item to the array list for items.
     * @param item
     */
    public void addItem(Item item) {
        int index = 0;
        for(Item items: getItems()) {
            if(items == null) {
                System.out.println(""+itemId);
                System.out.println(""+itemId);
                this.items[index] = item;
                break;
            }
            index++;
        }
    }
}
