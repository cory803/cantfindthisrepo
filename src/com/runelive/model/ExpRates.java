package com.runelive.model;
import com.runelive.world.entity.impl.player.Player;

public enum ExpRates {DEFAULT, SIR, LORD, LEGEND, EXTREME, REALISM;


    private String name;

    public static void set(Player player, ExpRates expRate) {
        player.setExpRate(expRate);
        //player.getPacketSender().sendIronmanMode(expRate.ordinal());
    }


    /**
     * Gets the mode for a certain id.
     *
     * @param id The id (ordinal()) of the mode.
     * @return gamemode.
     */
    public static ExpRates forId(int id) {
        for (ExpRates exp : ExpRates.values()) {
            if (exp.ordinal() == id) {
                return exp;
            }
        }
        return null;
    }

}
