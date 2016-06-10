package com.runelive.engine;

import java.util.concurrent.*;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import com.runelive.world.content.Scoreboard;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.runelive.GamePanel;
import com.runelive.GameServer;
import com.runelive.engine.task.TaskManager;
import com.runelive.world.World;
import com.runelive.world.content.clan.ClanChatManager;
import com.runelive.world.content.grandexchange.GrandExchangeOffers;
import com.runelive.world.content.pos.PlayerOwnedShops;

/**
 * @author lare96
 * @author Gabriel Hannason
 */
public final class GameEngine implements Runnable {

    private final GamePanel panel = GameServer.getPanel();

    private final ScheduledExecutorService logicService = GameEngine.createLogicService();

    // private static final int PROCESS_GAME_TICK = 2;

    // private EngineState engineState = EngineState.PACKET_PROCESSING;

    // private int engineTick = 0;

    @Override
    public void run() {
        try {
      /*
       * switch(engineState) { case PACKET_PROCESSING: World.getPlayers().forEach($it ->
       * $it.getSession().handlePrioritizedMessageQueue()); break; case GAME_PROCESSING:
       * TaskManager.sequence(); World.sequence(); break; } engineState = next();
       */
            long start = System.currentTimeMillis();
            TaskManager.sequence();
            long task_start = System.currentTimeMillis();
            long taskCycle = task_start - start;
            World.sequence();
            long end = System.currentTimeMillis() - start;
            panel.addCycleTime(end);
            panel.addTaskCycle(taskCycle);
            panel.addGeneral();
        } catch (Throwable e) {
            e.printStackTrace();
            World.logError("game_engine_error_log.txt", (Exception) e);
            World.savePlayers();
            GrandExchangeOffers.save();
            PlayerOwnedShops.saveShops();
            ClanChatManager.save();
        }
    }

  /*
   * private EngineState next() { if (engineTick == PROCESS_GAME_TICK) { engineTick = 0; return
   * EngineState.GAME_PROCESSING; } engineTick++; return EngineState.PACKET_PROCESSING; } private
   * enum EngineState { PACKET_PROCESSING, GAME_PROCESSING; }
   */

    public void submit(Runnable t) {
        try {
            logicService.execute(t);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * STATIC
     **/

    public static ScheduledExecutorService createLogicService() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.setRejectedExecutionHandler(new CallerRunsPolicy());
        executor.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("LogicServiceThread").build());
        executor.setKeepAliveTime(45, TimeUnit.SECONDS);
        executor.allowCoreThreadTimeOut(true);
        return Executors.unconfigurableScheduledExecutorService(executor);
    }
}
