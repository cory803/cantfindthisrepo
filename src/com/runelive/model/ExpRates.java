package com.runelive.model;

import com.runelive.model.container.impl.Bank;
import com.runelive.world.content.Achievements.AchievementData;
import com.runelive.world.content.PlayerPanel;
import com.runelive.world.content.dialogue.DialogueManager;
import com.runelive.world.content.skill.impl.slayer.SlayerMaster;
import com.runelive.world.content.skill.impl.slayer.SlayerTasks;
import com.runelive.world.entity.impl.player.Player;

public enum ExpRates {DEFAULT, SIR, LORD, LEGEND, EXTREME;

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
