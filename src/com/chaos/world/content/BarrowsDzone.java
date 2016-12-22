package com.chaos.world.content;

import com.chaos.util.Misc;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;

/**
 * Created by High105 on 12/22/2016.
 */
public class BarrowsDzone {

    public static int[] barrowsBrother = { 2026, 2027, 2028, 2029, 2030 };

    private static boolean rollDrop() {
        int chance = Misc.random(1, 35);
        if (chance == 30) {
            //We will drop something
            return true;
        }
        return false;
    }

    public static void killedBrother(Player player, NPC npc) {
        //Did you kill a brother?
        switch (npc.getId()) {
            case 2026:
                if (rollDrop()) {

                }
                break;
            case 2027:
                break;
            case 2028:
                break;
            case 2029:
                break;
            case 2030:
                break;
        }
    }


}
