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
public class Donation implements ServerLog {

    private final Player player;
    private final Item item;

    public Donation(Player player, Item item) {
        this.player = player;
        this.item = item;
    }

    @Override
    public String createQuery() {
        return "INSERT INTO `chaosps`.`logs_donations` (`time`, `username`, `item_name`, `item_id`, `item_amount`) VALUES ('" + getTimestamp() + "', '" + player.getUsername() + "', '" + item.getDefinition().getName() + "', '" + item.getId() + "', '" + item.getAmount() + "');";
    }
}
