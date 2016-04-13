package com.ikov;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ikov.engine.GameEngine;
import com.ikov.engine.task.TaskManager;
import com.ikov.engine.task.impl.ServerTimeUpdateTask;
import com.ikov.model.container.impl.PlayerOwnedShopContainer.PlayerOwnedShopManager;
import com.ikov.model.container.impl.Shop.ShopManager;
import com.ikov.model.definitions.ItemDefinition;
import com.ikov.model.definitions.NPCDrops;
import com.ikov.model.definitions.NpcDefinition;
import com.ikov.model.definitions.WeaponInterfaces;
import com.ikov.net.PipelineFactory;
import com.ikov.net.security.ConnectionHandler;
import com.ikov.world.clip.region.RegionClipping;
import com.ikov.world.content.CustomObjects;
import com.ikov.world.content.Lottery;
import com.ikov.world.content.Scoreboards;
import com.ikov.world.content.WellOfGoodwill;
import com.ikov.world.content.clan.ClanChatManager;
import com.ikov.world.content.combat.effect.CombatPoisonEffect.CombatPoisonData;
import com.ikov.world.content.combat.effect.CombatVenomEffect.CombatVenomData;
import com.ikov.world.content.combat.strategy.CombatStrategies;
import com.ikov.world.content.dialogue.DialogueManager;
import com.ikov.world.content.grandexchange.GrandExchangeOffers;
import com.ikov.world.content.pos.PlayerOwnedShops;
import com.ikov.world.entity.impl.npc.NPC;

/**
 * testCredit: lare96, Gabbe
 */
public final class GameLoader {

  private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(
      new ThreadFactoryBuilder().setNameFormat("GameThread").build());
  private final GameEngine engine;
  private final int port;

  protected GameLoader(int port) {
    this.port = port;
    this.engine = new GameEngine();
  }

  public void finish() throws IOException, InterruptedException {
    ExecutorService networkExecutor = Executors.newCachedThreadPool();
    ServerBootstrap serverBootstrap =
        new ServerBootstrap(new NioServerSocketChannelFactory(networkExecutor, networkExecutor));
    serverBootstrap.setPipelineFactory(new PipelineFactory(new HashedWheelTimer()));
    serverBootstrap.bind(new InetSocketAddress(port));
    executor.scheduleAtFixedRate(engine, 0, GameSettings.ENGINE_PROCESSING_CYCLE_RATE,
        TimeUnit.MILLISECONDS);
    TaskManager.submit(new ServerTimeUpdateTask());
  }

  public void init() throws IOException {
    ConnectionHandler.init();
    RegionClipping.init();
    CustomObjects.init();
    ItemDefinition.init();
    Lottery.init();
    GrandExchangeOffers.init();
    PlayerOwnedShops.init();
    Scoreboards.init();
    WellOfGoodwill.init();
    ClanChatManager.init();
    CombatPoisonData.init();
    CombatVenomData.init();
    CombatStrategies.init();
    NpcDefinition.parseNpcs().load();
    NPCDrops.parseDrops().load();
    WeaponInterfaces.parseInterfaces().load();
    ShopManager.parseShops().load();
    PlayerOwnedShopManager.load();
    DialogueManager.parseDialogues().load();
    NPC.init();
  }

  public GameEngine getEngine() {
    return engine;
  }
}
