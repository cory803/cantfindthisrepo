package com.runelive.net.serverlogs.impl;

import com.runelive.model.Item;
import com.runelive.world.entity.impl.player.Player;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 1/20/2017.
 *
 * @author Seba
 */
public class Drop implements ServerLog {

    private final Player player;
    private final long itemAddress;
    private final Item item;

    public Drop(Player player, long itemAddress, Item item) {
        this.player = player;
        this.itemAddress = itemAddress;
        this.item = item;
    }

    @Override
    public String createQuery() {
        return "INSERT INTO `chaosps`.`logs_drops` (`time`, `username`, `item_address`, `item_id`, `item_amount`, `item_name`) VALUES ('"  + getTimestamp() +"', '" + player.getUsername() + "', '" + itemAddress + "', '" + item.getId() + "', '" + item.getAmount() + "', '" + item.getDefinition().getName() + "');";
    }
}
