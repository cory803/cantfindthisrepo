package com.runelive.net.serverlogs.impl;

import com.runelive.world.entity.impl.player.Player;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 1/20/2017.
 *
 * @author Seba
 */
public class Connection implements ServerLog {

    private final Player player;
    private final String action;

    public Connection(Player player, String action) {
        this.player = player;
        this.action = action;
    }

    @Override
    public String createQuery() {
        return "INSERT INTO `chaosps`.`logs_connections` (`time`, `username`, `action`, `ip_address`, `serial_address`, `mac_address`, `rights`) VALUES ('" + getTimestamp() + "', '" + player.getUsername() + "', '" + action + "', '" + player.getHostAddress() + "', '" + player.getSerialNumber() + "', '" + player.getMacAddress() + "', '" + player.getStaffRights().ordinal() + "');";
    }
}
