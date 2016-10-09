package com.chaos.model;

import com.chaos.world.entity.impl.player.Player;

public enum DonatorRights {

    PLAYER("Player", "<col=ff0000>", "", 0),
    PREMIUM("Premium", "<col=ff0000>", "", 7),
    EXTREME("Extreme", "<col=2FAC45>", "<shad=0>", 9),
    LEGENDARY("Legendary", "<col=255>", "<shad=0>", 8),
    UBER("Uber", "<col=ffff00>", "<shad=0>", 11),
    PLATINUM("Platinum", "<col=060000>", "<shad=FFFFF9>", 19);

    private String title;
    private String color;
    private String shad;
    private int crown;

    DonatorRights(String title, String color, String shad, int crown) {
        this.title = title;
        this.color = color;
        this.shad = shad;
        this.crown = crown;
    }

    /**
     * Gets the rank for a certain id.
     *
     * @param id The id (ordinal()) of the rank.
     * @return rights.
     */
    public static DonatorRights forId(int id) {
        for (DonatorRights rights : DonatorRights.values()) {
            if (rights.ordinal() == id) {
                return rights;
            }
        }
        return null;
    }

    /**
     * Gets the color code for a certain rank
     *
     * @return
     */
    public String getColor() {
        return this.color;
    }

    /**
     * Gets the color shad code for a certain rank
     *
     * @return
     */
    public String getShad() {
        return this.shad;
    }

    /**
     * Gets the rank title for a certain rank
     *
     * @return
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Are you a donator?
     *
     * @return
     */
    public boolean isDonator() {
        return this.ordinal() > 0;
    }

    /**
     * Gets the crown for a certain rank
     *
     * @return
     */
    public int getCrown() {
        return this.crown;
    }

    public double getSpecialAccuracyBoost(Player player) {
        if(player.getLocation() == Locations.Location.WILDERNESS || player.getLocation() == Locations. Location.WILDKEY_ZONE) {
            return 0;
        } else {
            return this.ordinal() / 100;
        }
    }

}
