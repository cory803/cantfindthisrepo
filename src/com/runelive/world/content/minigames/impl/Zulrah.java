package com.runelive.world.content.minigames.impl;

import com.runelive.engine.task.Task;
import com.runelive.engine.task.TaskManager;
import com.runelive.model.Locations.Location;
import com.runelive.model.Position;
import com.runelive.model.RegionInstance;
import com.runelive.model.RegionInstance.RegionInstanceType;
import com.runelive.model.definitions.ItemDefinition;
import com.runelive.util.Misc;
import com.runelive.world.World;
import com.runelive.world.content.Achievements;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;

public class Zulrah {

  public static final int ZULRAH_GREEN_NPC_ID = 2043;
  public static final int ZULRAH_BLUE_NPC_ID = 2042;
  public static final int ZULRAH_RED_NPC_ID = 2044;
  public static final int ZULRAH_JAD_NPC_ID = 2045;

  public static void enter_pit(Player player) {
    player.moveTo(new Position(2268, 3069, player.getIndex() * 4));
    player.setRegionInstance(new RegionInstance(player, RegionInstanceType.ZULRAH_PIT));
    start_zulrah(player);
  }

  public static void leave_pit(Player player, boolean resetStats) {
    if (player.getRegionInstance() != null) {
      if (resetStats)
        player.restart();
      for (int i = 0; i < player.getRegionInstance().getNpcsList().size(); i++) {
        if (player.getRegionInstance().getNpcsList().get(i) != null) {
          World.deregister(player.getRegionInstance().getNpcsList().get(i));
          player.getRegionInstance().getNpcsList()
              .remove(player.getRegionInstance().getNpcsList().get(i));
        }
      }
      player.getCombatBuilder().reset(true);
      if (player.getRegionInstance() != null) {
        player.getRegionInstance().destruct();
      }
    }
  }

  public static void start_zulrah(final Player player) {
    TaskManager.submit(new Task(2, player, false) {
      @Override
      public void execute() {
        if (player.getRegionInstance() == null || !player.isRegistered()
            || player.getLocation() != Location.ZULRAH_PIT) {
          stop();
          return;
        }
        NPC n = new NPC(ZULRAH_GREEN_NPC_ID, new Position(2269, 3077, player.getPosition().getZ()));
        World.register(n);
        player.getRegionInstance().getNpcsList().add(n);
        n.getCombatBuilder().attack(player);
        stop();
      }
    });
  }

  public static int[] UnstackableReward = {6571, 1149, 3204, 5698};

  public static int randomUnstackableReward()
  {
    return UnstackableReward[(int) (Math.random() * UnstackableReward.length)];
  }

   public static int loot = randomUnstackableReward();
   public static int stackable_drop = ZulrahDropTable.getDrop().getItemId();
   public static int stackable_amount = ZulrahDropTable.getDrop().getAmount();
   public static int rareDropRoll = Misc.inclusiveRandom(1, 256);

  public static void handleZulrahLoot(Player p) {
    p.getInventory().add(randomUnstackableReward(), 1);
    p.getInventory().add(21080, Misc.inclusiveRandom(100, 300));
    p.getInventory().add(stackable_drop, stackable_amount);
    p.getPacketSender().sendLootMessage("You have received 1 x "+ ItemDefinition.forId(loot).getName()+" from Zulrah.");
    p.getPacketSender().sendLootMessage("You have received some "+ ItemDefinition.forId(stackable_drop).getName()+" from Zulrah.");
    if(rareDropRoll == 256) {
      p.getPacketSender().sendMessage("Lmfao, Dave, get this right you hit your undeveloped rare table");
    }
  }
  public static void handleZulrahDeath(final Player player, NPC n) {
    if (n.getId() == ZULRAH_GREEN_NPC_ID || n.getId() == ZULRAH_RED_NPC_ID
        || n.getId() == ZULRAH_BLUE_NPC_ID || n.getId() == ZULRAH_JAD_NPC_ID) {
      if (player.getRegionInstance() != null) {
        player.getRegionInstance().getNpcsList().remove(n);
      }
      handleZulrahLoot(player);
      leave_pit(player, true);
    }
  }

}
