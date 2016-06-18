package com.runelive.world.content.minigames;

import com.runelive.model.Animation;
import com.runelive.model.Item;
import com.runelive.util.Misc;
import com.runelive.world.World;
import com.runelive.world.content.PlayerLogs;
import com.runelive.world.entity.impl.player.Player;

/**
 * Created by Vados on 16/06/2016.
 */
public class TreasureIslandChest {

    public static int KEY_OF_FEAR = 9725;
    public static int KEY_OF_DEATH = 9722;
    public static int KEY_OF_COBRA = 9724;
    public static int KEY_OF_BLITZ = 9723;
    
    public static Item randomMaterial(Item[] material) {
        return material[(int) (Math.random() * material.length)];
    }
    
    public static Item randomCombatAmmo(Item[] ammo) {
        return ammo[(int) (Math.random() * ammo.length)];
    }
    
    public static Item randomCombatReward(Item[] combat) {
        return combat[(int) (Math.random() * combat.length)];
    }
    
    public static Item randomRareReward(Item[] rare) {
        return rare[(int) (Math.random() * rare.length)];
    }
    
    public static void openChest(Player player) {
        Item[] MaterialReward = {
        	new Item(995, Misc.random(500000, 25000000)),
        	new Item(6571, 1),
            new Item(1632, Misc.random(5, 50)),
            new Item(1618, Misc.random(15, 50)),
            new Item(1622, Misc.random(25, 100)),
            new Item(1620, Misc.random(25, 100)),
            new Item(1514, Misc.random(10, 75)),
            new Item(1516, Misc.random(25, 100)),
            new Item(1518, Misc.random(35, 100)),
            new Item(15271, Misc.random(20, 60)),
            new Item(15273, Misc.random(15, 50)),
            new Item(384, Misc.random(50, 100)),
            new Item(386, Misc.random(25, 100)),
            new Item(3143, Misc.random(30, 50)),
            new Item(3145, Misc.random(20, 50)),
            new Item(2364, Misc.random(1, 10)),
            new Item(2362, Misc.random(5, 20)),
            new Item(2360, Misc.random(10,  30)),
            new Item(452, Misc.random(1, 10)),
            new Item(450, Misc.random(5, 20)),
            new Item(448, Misc.random(10,  30)),
            new Item(208, Misc.random(5, 20)),
            new Item(220, Misc.random(5, 20)),
            new Item(218, Misc.random(5, 20)),
            new Item(210, Misc.random(5, 20)),
            new Item(7937, Misc.random(100, 500)),
            new Item(1437, Misc.random(200, 1000)),
            new Item(18831, Misc.random(5, 25)),
            new Item(537, Misc.random(10, 50))
        };
        Item[] CombatAmmo = {
            new Item(2, Misc.random(100, 500)),
            new Item(9244, Misc.random(20, 100)),
            new Item(9243, Misc.random(20, 100)),
            new Item(9242, Misc.random(20, 100)),
            new Item(892, Misc.random(20,  100)),
            new Item(565, Misc.random(20, 100)),
            new Item(560, Misc.random(20, 100)),
            new Item(555, Misc.random(500, 1000)),
            new Item(9075, Misc.random(20, 100)),
            new Item(557, Misc.random(500, 1000)),
            new Item(563, Misc.random(20, 100)),
            new Item(562, Misc.random(20, 100)),
            new Item(561, Misc.random(20, 100)),
            new Item(11518, Misc.random(1, 10)),
            new Item(11526, Misc.random(1, 10)),
            new Item(14302, Misc.random(1, 10)),
            new Item(14153, Misc.random(10, 30)),
            new Item(14404, Misc.random(10, 30)),
            new Item(14428, Misc.random(10, 30)),
            new Item(14416, Misc.random(10, 30)),
            new Item(14189, Misc.random(10, 30)),
            new Item(14177, Misc.random(10, 30)),
            new Item(12791, Misc.random(5, 25)),
            new Item(12094, Misc.random(5, 25)),
            new Item(12090, Misc.random(5, 25)),
        };
        Item[] CombatReward = {
        	new Item(4151, 1),
            new Item(6737, 1),
            new Item(15486, 1),
            new Item(6914, 1),
            new Item(6889, 1),
            new Item(6585, 1),
            new Item(19111, 1),
            new Item(6570, 1),
            new Item(10551, 1),
            new Item(20072, 1),
            new Item(8850, 1),
            new Item(6731, 1),
            new Item(6735, 1),
            new Item(6733, 1),
            new Item(6666, 1),
            new Item(7158, 1),
            new Item(11730, 1),
            new Item(11732, 1),
        };
        Item[] RareReward = {
            	new Item(21104, 1),
                new Item(21107, 1),
                new Item(12926, 1),
                new Item(21077, 1),
                new Item(21108, 1),
                new Item(21110, 1),
            };
        if(player.getInventory().contains(KEY_OF_DEATH) && player.getInventory().contains(KEY_OF_BLITZ)
          && player.getInventory().contains(KEY_OF_COBRA) && player.getInventory().contains(KEY_OF_FEAR)) {
                player.getInventory().delete(KEY_OF_BLITZ, 1);
                player.getInventory().delete(KEY_OF_COBRA, 1);
                player.getInventory().delete(KEY_OF_DEATH, 1);
                player.getInventory().delete(KEY_OF_FEAR, 1);
                player.performAnimation(new Animation(881));
                if(Misc.random(1, 3) == 2) {
                	player.getInventory().add(randomCombatReward(CombatReward));
                }
                player.getInventory().add(randomCombatAmmo(CombatAmmo));
                player.getInventory().add(randomMaterial(MaterialReward));
                if(Misc.random(1, 300) == 125) {
                	Item rareReward = randomRareReward(RareReward);
                	player.getInventory().add(rareReward);
                    String message = "<icon=1><shad=FF8C38> " + player.getUsername() + " has just received "
                            + rareReward.getDefinition().getName() + " from Treasure Island!";
                        World.sendMessage(message);
                    PlayerLogs.npcdrops(player, rareReward, "Treasure Island");
                }
                player.getLastLoot().reset();
        } else {
            player.getPacketSender().sendMessage("You need the following keys: Key of Blitz, Cobra, Death & Fear to loot this chest.");
        }
    }
}
