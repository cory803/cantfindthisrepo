package com.runelive.net.serverlogs.impl;

import com.runelive.world.entity.impl.player.Player;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 1/20/2017.
 *
 * @author Seba
 */
public class Command implements ServerLog {

    private final Player player;
    private final String command;

    public Command(Player player, String command) {
        this.player = player;
        this.command = command;
    }

    @Override
    public String createQuery() {
        return "INSERT INTO `chaosps`.`logs_commands` (`time`, `username`, `command`) VALUES ('" + getTimestamp() + "', '" + player.getUsername() + "', '" + command + "');";
    }
}
