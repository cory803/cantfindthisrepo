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
public class Kill implements ServerLog {

    private final Player killer;
    private final Player victim;
    private final Item item;

    public Kill(Player killer, Player victim, Item item) {
        this.killer = killer;
        this.victim = victim;
        this.item = item;
    }

    @Override
    public String createQuery() {
        return "INSERT INTO `chaosps`.`logs_kills` (`time`, `killer`, `victim`, `item_name`, `item_id`, `item_amount`) VALUES ('" + getTimestamp() + "', '" + killer.getUsername() + "', '" + victim.getUsername() + "', '" + item.getDefinition().getName() + "', '" + item.getId() + "', '" + item.getAmount() + "');";
    }
}
