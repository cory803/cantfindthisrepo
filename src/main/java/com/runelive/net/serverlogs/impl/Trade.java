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
public class Trade implements ServerLog {

    private final Player owner;
    private final Player collector;
    private final Item item;

    public Trade(Player owner, Player collector, Item item) {
        this.owner = owner;
        this.collector = collector;
        this.item = item;
    }

    @Override
    public String createQuery() {
        return "INSERT INTO `chaosps`.`logs_trade` (`time`, `owner`, `collector`, `item_name`, `item_id`, `item_amount`) VALUES ('" + getTimestamp() + "', '" + owner.getUsername() + "', '" + collector.getUsername() + "', '" + item.getDefinition().getName() + "', '" + item.getId() + "', '" + item.getAmount() + "');";
    }
}
