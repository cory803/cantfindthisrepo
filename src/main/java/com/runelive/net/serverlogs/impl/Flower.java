package com.runelive.net.serverlogs.impl;

import com.runelive.world.entity.impl.player.Player;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 1/20/2017.
 *
 * @author Seba
 */
public class Flower implements ServerLog {

    private final Player player;
    private final String plant;

    public Flower(Player player, String plant) {
        this.player = player;
        this.plant = plant;
    }

    @Override
    public String createQuery() {
        return "INSERT INTO `chaosps`.`logs_flowers` (`time`, `username`, `plant`) VALUES ('" + getTimestamp() + "', '" + player.getUsername() + "', '" + plant + "');";
    }
}
