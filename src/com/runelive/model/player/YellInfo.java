package com.runelive.model.player;

import com.runelive.world.entity.impl.player.Player;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/19/2016.
 *
 * @author Seba
 */
public enum YellInfo {

    DONOR(7, "ff0000"),
    SUPER(8, "255"),
    EXTREME(9, "2FAC45"),
    LEGENDARY(10, "3E0069"),
    UBER(11, "ffff00"),
    YOUTUBER(5, "ff0000"),
    SUPPORT(4, "589fe1"),
    MODERATOR(1, "31a4ff"),
    GLOBAL_MOD(6, "00ff00"),
    DEVELOPER(18, "4d4dff"),
    ADMINISTRATOR(14, "ffff00"),
    MANAGER(3, "ff0000"),
    OWNER(3, "ff0000");

    private int imageId;
    private String colorCode;

    YellInfo(int imageId, String colorCode) {
        this.imageId = imageId;
        this.colorCode = colorCode;
    }

    public int getImageId() {
        return imageId;
    }

    public String getColorCode() {
        return colorCode;
    }

    public static YellInfo getRankInfo(Player player) {
        switch (player.getRights()) {
            case YOUTUBER:
                return YOUTUBER;
            case SUPPORT:
                return SUPPORT;
            case MODERATOR:
                return MODERATOR;
            case GLOBAL_MOD:
                return GLOBAL_MOD;
            case ADMINISTRATOR:
                return ADMINISTRATOR;
            case MANAGER:
                return MANAGER;
            case OWNER:
                return OWNER;
        }
        switch (player.getDonorRights()) {
            case 1:
                return DONOR;
            case 2:
                return SUPER;
            case 3:
                return EXTREME;
            case 4:
                return LEGENDARY;
            case 5:
                return UBER;

            default:
                return DONOR;
        }
    }
}
