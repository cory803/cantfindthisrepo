package com.runelive.world.content.minigames;

import com.runelive.util.Misc;
import com.runelive.world.entity.impl.player.Player;

/**
 * Created by Vados on 16/06/2016.
 */
public class TreasureIslandChest {

    public int KEY_OF_FEAR = 9725;
    public int KEY_OF_DEATH = 9722;
    public int KEY_OF_COBRA = 9724;
    public int KEY_OF_BLITZ = 9723;

    public static int[] MaterialReward = {6571};

    public static int randomMaterial()
    {
        return MaterialReward[(int) (Math.random() * MaterialReward.length)];
    }

    public static int[] CombatAmmo = {6571};

    public static int randomCombatAmmo()
    {
        return CombatAmmo[(int) (Math.random() * CombatAmmo.length)];
    }

    public static int[] CombatReward = {657};

    public static int randomCombatReward()
    {
        return CombatReward[(int) (Math.random() * CombatReward.length)];
    }

    public void openChest(Player player) {
        if (player.getInventory().getFreeSlots() <= 2) {
            player.getPacketSender().sendMessage("You need atleast 3 Inventory Spaces to loot from the chest.");
        }
        if(player.getInventory().contains(KEY_OF_DEATH) && player.getInventory().contains(KEY_OF_BLITZ)
          && player.getEquipment().contains(KEY_OF_COBRA) && player.getInventory().contains(KEY_OF_FEAR)) {
                player.getInventory().delete(KEY_OF_BLITZ, 1);
                player.getInventory().delete(KEY_OF_COBRA, 1);
                player.getInventory().delete(KEY_OF_DEATH, 1);
                player.getInventory().delete(KEY_OF_FEAR, 1);
                player.getInventory().add(randomCombatReward(), 1);
                player.getInventory().add(randomCombatAmmo(), Misc.inclusiveRandom(250, 500));
                player.getInventory().add(randomMaterial(), Misc.inclusiveRandom(100, 300));
                player.getLastLoot().reset();
        } else {
            player.getPacketSender().sendMessage("You need the following keys: Key of Blitz, Cobra, Death & Fear to loot this chest.");
        }
    }
}
