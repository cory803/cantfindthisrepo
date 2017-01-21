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
public class Stake implements ServerLog {

    private final Player winner;
    private final Player looser;
    private final Item item;

    public Stake(Player winner, Player looser, Item item) {
        this.winner = winner;
        this.looser = looser;
        this.item = item;
    }

    @Override
    public String createQuery() {
        return "INSERT INTO `chaosps`.`logs_staking` (`time`, `winner`, `looser`, `item_name`, `item_id`, `item_amount`) VALUES ('" + getTimestamp() + "', '" + winner.getUsername() + "', '" + looser.getUsername() + "', '" + item.getDefinition().getName() + "', '" + item.getId() + "', '" + item.getAmount() + "');";
    }
}
