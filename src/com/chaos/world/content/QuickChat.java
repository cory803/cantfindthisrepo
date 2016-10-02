package com.chaos.world.content;

import com.chaos.util.Misc;
import com.chaos.util.Stopwatch;
import com.chaos.world.content.skill.impl.slayer.SlayerTasks;
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
        if (!player.getQuickChat().elapsed(2500)) {
            player.getPacketSender().sendMessage("Please wait 2 seconds before using the quick chat again.");
            return;
        }
        if (PlayerPunishment.isMuted(player.getUsername()) || PlayerPunishment.isIpMuted(player.getHostAddress())) {
            player.getPacketSender().sendMessage("You are muted and cannot chat.");
            return;
        }
        player.forceChat("[CHAOS] My " + quickChat[id] + " level is "
                + player.getSkillManager().getMaxLevel(id) + ".");
        player.getQuickChat().reset();
    }
}
