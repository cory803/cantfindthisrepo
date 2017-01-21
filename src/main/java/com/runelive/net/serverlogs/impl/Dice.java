package com.runelive.net.serverlogs.impl;

import com.runelive.world.entity.impl.player.Player;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 1/20/2017.
 *
 * @author Seba
 */
public class Dice implements ServerLog {

    private final Player player;
    private final int roll;

    public Dice(Player player, int roll) {
        this.player = player;
        this.roll = roll;
    }

    @Override
    public String createQuery() {
        return "INSERT INTO `chaosps`.`logs_dicing` (`time`, `username`, `roll`) VALUES ('" + getTimestamp() + "', '" + player.getUsername() + "', '" + roll + "');";
    }
}
