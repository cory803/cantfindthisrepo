package com.runelive.world.content;

import com.runelive.model.definitions.ItemDefinition;
import com.runelive.util.Misc;
import com.runelive.util.Stopwatch;
import com.runelive.world.entity.impl.player.Player;

import java.util.concurrent.TimeUnit;

/**
 * Created by Dave on 06/05/2016.
 */
public class Gamble {

    public static void gambleRoll(Player player, int item_id) {
            player.getLastRoll().reset();
            ItemDefinition itemDef = ItemDefinition.forId(item_id);
            int ROLL_MOB = Misc.inclusiveRandom(38, 70) - 4 + Misc.inclusiveRandom(3, 30);
            int ROLL_PLAYER = Misc.inclusiveRandom(1, 70) - 5 + Misc.inclusiveRandom(0, 30);
            if (ROLL_PLAYER > ROLL_MOB) {
                player.forceChat("Yes I've won! 2 x " + itemDef.forId(item_id).getName() + "");
                player.getInventory().add(item_id, 2);
                player.getPacketSender().sendMessage("Congratulations! You've won, the gambler rolled @dre@" + ROLL_MOB + " @bla@& you rolled @dre@" + ROLL_PLAYER + ".");
            } else {
                player.forceChat("Oh no, I've lost my " + itemDef.forId(item_id).getName() + "!");
                player.getPacketSender().sendMessage("Unlucky, you've lost! the gambler rolled @dre@" + ROLL_MOB + " @bla@& you rolled @dre@" + ROLL_PLAYER + ".");
            }
        }
    }