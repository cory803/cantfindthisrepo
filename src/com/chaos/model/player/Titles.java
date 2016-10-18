package com.chaos.model.player;

import com.chaos.model.StaffRights;
import com.chaos.world.entity.impl.player.Player;

/**
 * @Author Jonny
 * Sets titles for the player
 */
public class Titles {

    /**
     * Sets the title id for a game mode/staff member title depending
     * on the game mode or staff rights of the player.
     * A better title system will be added at a later date...
     * @param player
     */
    public static void setTitle(Player player) {
        if(player.getStaffRights() == StaffRights.PLAYER) {
            switch(player.getGameModeAssistant().getGameMode()) {
                case KNIGHT:
                    player.setLoyaltyRank(1);
                    break;
                case REALISM:
                    player.setLoyaltyRank(2);
                    break;
                case IRONMAN:
                    player.setLoyaltyRank(3);
                    break;
            }
        } else {
            switch(player.getStaffRights()) {
                case YOUTUBER:
                    player.setLoyaltyRank(20);
                    break;
                case WIKI_EDITOR:
                    player.setLoyaltyRank(15);
                    break;
                case WIKI_MANAGER:
                    player.setLoyaltyRank(16);
                    break;
                case SUPPORT:
                    player.setLoyaltyRank(11);
                    break;
                case MODERATOR:
                    player.setLoyaltyRank(8);
                    break;
                case FORUM_MOD:
                    player.setLoyaltyRank(21);
                    break;
                case GLOBAL_MOD:
                    player.setLoyaltyRank(14);
                    break;
                case ADMINISTRATOR:
                    player.setLoyaltyRank(9);
                    break;
                case MANAGER:
                    player.setLoyaltyRank(17);
                    break;
                case OWNER:
                    player.setLoyaltyRank(10);
                    break;
            }
        }
    }
}
