package com.chaos.world.content;

import com.chaos.util.Stopwatch;
import com.chaos.world.entity.impl.player.Player;

/**
 * Created by tanner on 8/31/2016.
 */
public class QuickChat {

    public static String[] quickChat = new String[] { "Attack", "Defence", "Strength", "Constitution",
    "Range", "Prayer", "Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting",
    "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting",
    "Construction", "Hunter", "Summoning", "Dungeoneering"};

    public static void handleButtons(Player player, int id) {
        Stopwatch timer = new Stopwatch();
        if (timer.elapsed(5000)) {
            if (PlayerPunishment.isMuted(player.getUsername()) || PlayerPunishment.isIpMuted(player.getHostAddress())) {
                player.getPacketSender().sendMessage("You are muted and cannot chat.");
                return;
            }
            player.forceChat("[CHAOS] My " + quickChat[id] + " level is "
                    + player.getSkillManager().getMaxLevel(id) + ".");
            timer.reset();
        } else {
            player.getPacketSender().sendMessage("Please wait 5 seconds before using the quick chat again.");
        }
    }

}
