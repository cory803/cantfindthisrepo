package com.runelive.model;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.runelive.util.Misc;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/26/2016.
 *
 * @author Seba
 */
public enum PlayerRights {

    /**
     * Regular Players
     */
    PLAYER(0, 0, null, null),

    /**
     * Ironmen
     */
    IRONMAN(12, 0, "808080", null),
    HARDCORE_IRONMAN(12, 0, "808080", null),

    /**
     * Donor Ranks
     */
    REGULAR_DONOR(7, 1, "ff0000", null),
    SUPER_DONOR(8, 2, "255", null),
    EXTREME_DONOR(9, 3, "2FAC45", null),
    LEGENDARY_DONOR(10, 4, "3E0069", null),
    UBER_DONOR(11, 5, "ffff00", "0"),

    /**
     * Other Ranks (Not Staff)
     */
    YOUTUBER(5, 1, "ff0000", "620000"),
    WIKI_EDITOR(15, 1, "ff7f00", "0"),
    WIKI_MANAGER(16, 3, "31a4ff", "0"),

    /**
     * Staff Ranks
     */
    SUPPORT(4, 6, "589fe1", "0"),
    MODERATOR(1, 7, "31a4ff", "0"),
    GLOBAL_MOD(6, 8, "00ff00", "0"),
    ADMINISTRATOR(14, 9, "ffff00", "0"),
    STAFF_MANAGER(17, 10, "ff0000", "2C0000"),
    MANAGER(3, 11, "ff0000", "2C0000"),

    /**
     * Owner Ranks
     */
    DEVELOPER(18, 12, "4d4dff", "0"),
    OWNER(3, 12, "ff0000", "0");

    /**
     * The clients image value
     */
    private int clientValue;

    /**
     * The rights of which the player can use commands
     */
    private int rights;

    /**
     * The color of the yell the player gets.
     */
    private String yellColor;

    /**
     * The shadow for the player's yell.
     */
    private String shadow;

    PlayerRights(int clientValue, int rights, String yellColor, String shadow) {
        this.clientValue = clientValue;
        this.rights = rights;
        this.yellColor = yellColor;
        this.shadow = shadow;
    }

    /**
     * Sets the command rights for a player (This can be used for donor ironman)
     * @param rights The rights we will set the player to.
     */
    public void setRights(int rights) {
        this.rights = rights;
    }

    /**
     * Returns the client's image value.
     * @return The client image
     */
    public int getClientValue() {
        return clientValue;
    }

    /**
     * Returns the command rights for the player
     * @return The rights for the player
     */
    public int getRights() {
        return rights;
    }

    /**
     * Returns the yell color for the player.
     * @return The yell color
     */
    public String getYellColor() {
        return yellColor;
}

    /**
     * Returns the shadow for the player's yell
     * @return The shadow
     */
    public String getShadow() {
        return shadow;
    }

    /**
     * Returns a formatted version of the players rights.
     * @return The formatted name.
     */
    public String getRightName() {
        return Misc.formatText(this.toString().toLowerCase().replace("_", ""));
    }

    /**
     * Returns the PlayerRights for the given id.
     * @param id The id we are searching
     * @return The player rights for the search.
     */
    public static PlayerRights forId(int id) {
        return PlayerRights.values()[id];
    }

    /**
     * Retursn the PlayersRights for the given name.
     * @param name The right name we are searching
     * @return The player rights for the name.
     */
    public static PlayerRights forName(String name) {
        for (PlayerRights rights : PlayerRights.values()) {
            if (rights.name().toLowerCase().equals(name.toLowerCase())) {
                return rights;
            }
        }
        return PLAYER;
    }

    /**
     * Contains the list of ranks that are staff members.
     */
    private static final ImmutableSet<PlayerRights> STAFF = Sets.immutableEnumSet(
            SUPPORT, MODERATOR, GLOBAL_MOD, ADMINISTRATOR, STAFF_MANAGER, MANAGER, DEVELOPER, OWNER
    );

    /**
     * Contains the list of ranks that can stream.
     */
    private static final ImmutableSet<PlayerRights> CAN_STREAM = Sets.immutableEnumSet(
            YOUTUBER, SUPPORT, MODERATOR, GLOBAL_MOD, ADMINISTRATOR, STAFF_MANAGER, MANAGER, DEVELOPER, OWNER
    );

    /**
     * Returns if the player is a staff member.
     * @return Is staff member
     */
    public boolean isStaff() {
        return STAFF.contains(this);
    }

    /**
     * Returns if the player can stream.
     * @return Can the player stream.
     */
    public boolean canStream() {
        return CAN_STREAM.contains(this);
    }
}
