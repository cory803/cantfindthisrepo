package com.runelive.net.serverlogs.impl;

import com.runelive.world.entity.impl.player.Player;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 1/20/2017.
 *
 * @author Seba
 */
public class Other implements ServerLog {

    private final Player player;
    private final String action;

    public Other(Player player, String action) {
        this.player = player;
        this.action = action;
    }

    @Override
    public String createQuery() {
        return "INSERT INTO `chaosps`.`logs_other` (`time`, `username`, `rights`, `action`) VALUES ('" + getTimestamp() + "', '" + player.getUsername() + "', '" + player.getStaffRights().ordinal() + "', '" + action + "');";
    }
}
