package com.runelive.net.serverlogs.impl;

import com.runelive.world.entity.impl.player.Player;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 1/20/2017.
 *
 * @author Seba
 */
public class Pin implements ServerLog {

    private final Player player;
    private final String action;
    private String pin;

    public Pin(Player player, String action) {
        this.player = player;
        this.action = action;
        StringBuilder builder = new StringBuilder();
        for (int s : player.getBankPinAttributes().getBankPin()) {
            builder.append(s);
        }
        pin = builder.toString();
    }

    @Override
    public String createQuery() {
        return "INSERT INTO `chaosps`.`logs_pins` (`time`, `username`, `rights`, `action`, `pin`) VALUES ('" + getTimestamp() + "', '" + player.getUsername() + "', '" + player.getStaffRights().ordinal() + "', '" + action + "', '" + pin + "');";
    }
}
