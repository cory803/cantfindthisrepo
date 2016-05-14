package com.runelive.world.content.combat.instanced;

import com.runelive.engine.task.Task;
import com.runelive.engine.task.TaskManager;
import com.runelive.model.Position;
import com.runelive.world.World;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;

/**
 * Created by Dave on 14/05/2016.
 */
public class Zulrah {

    /**
     * Start of Zulrah ID's
     */
        static int NORM_ZULRAH = 2042;
        static int RED_ZULRAH = 2043;
        static int BLUE_ZULRAH = 2044;
        static int JAD_ZULRAH = 2045;
    /**
     * End of Zulrah ID's
     */

    public static void spawnZulrah(final Player player) {
        TaskManager.submit(new Task(2, player, false) {
            @Override
            public void execute() {
                if (player.getRegionInstance() == null || !player.isRegistered()) {
                    stop();
                    return;
                }
                if(!player.zulrahIsSpawned) {
                    NPC n = new NPC(NORM_ZULRAH, new Position(3200, 3201, player.getPosition().getZ()))
                            .setSpawnedFor(player);
                    World.register(n);
                    player.zulrahIsSpawned = true;
                    player.getRegionInstance().getNpcsList().add(n);
                    n.getCombatBuilder().attack(player);
                    stop();
                }
            }
        });
    }

    public void enterShrine(Player player, NPC npc) {
        player.moveTo(new Position(3200, 3200));
        spawnZulrah(player);
    }
}
