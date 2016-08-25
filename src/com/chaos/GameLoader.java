package com.chaos;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.chaos.cache.Archive;
import com.chaos.model.definitions.*;
import com.chaos.model.npc.drops.LootSystem;
import com.chaos.util.FontUtils;
import com.chaos.world.World;
import com.chaos.world.doors.DoorManager;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.chaos.engine.GameEngine;
import com.chaos.engine.task.TaskManager;
import com.chaos.engine.task.impl.ServerTimeUpdateTask;
import com.chaos.model.container.impl.PlayerOwnedShopContainer.PlayerOwnedShopManager;
import com.chaos.model.container.impl.Shop.ShopManager;
import com.chaos.net.PipelineFactory;
import com.chaos.net.security.ConnectionHandler;
import com.chaos.world.content.CustomObjects;
import com.chaos.world.content.Lottery;
import com.chaos.world.content.Scoreboard;
import com.chaos.world.content.clan.ClanChatManager;
import com.chaos.world.content.combat.effect.CombatPoisonEffect.CombatPoisonData;
import com.chaos.world.content.combat.effect.CombatVenomEffect.CombatVenomData;
import com.chaos.world.content.combat.strategy.CombatStrategies;
import com.chaos.world.content.pos.PlayerOwnedShops;
import com.chaos.world.entity.impl.npc.NPC;

/**
 * testCredit: lare96, Gabbe
 */
public final class GameLoader {

	private final ScheduledExecutorService executor = Executors
			.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("GameThread").build());
	private final GameEngine engine;
	private final int port;

	protected GameLoader(int port) {
		this.port = port;
		this.engine = new GameEngine();
	}

	public void finish() throws IOException, InterruptedException {
		ExecutorService networkExecutor = Executors.newCachedThreadPool();
		ServerBootstrap serverBootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(networkExecutor, networkExecutor));
		serverBootstrap.setPipelineFactory(new PipelineFactory(new HashedWheelTimer()));
		serverBootstrap.bind(new InetSocketAddress(port));
		executor.scheduleAtFixedRate(engine, 0, GameSettings.ENGINE_PROCESSING_CYCLE_RATE, TimeUnit.MILLISECONDS);
		TaskManager.submit(new ServerTimeUpdateTask());
	}

	public void init() throws Exception {
		ConnectionHandler.init();
		FontUtils.initialize(new Archive(GameServer.cache.getFile(0, 1)));
		GameObjectDefinition.init();
		World.loadRegions(new Archive(GameServer.cache.getFile(0, 5)));
		CustomObjects.init();
		ItemDefinition.init().load();
		Lottery.init();
		DoorManager.init();
		PlayerOwnedShops.init();
		ClanChatManager.init();
		CombatPoisonData.init();
		CombatVenomData.init();
		CombatStrategies.init();
		Scoreboard.load();
		NpcDefinition.parseNpcs().load();
		LootSystem.loadDropTables();
		WeaponInterfaces.parseInterfaces().load();
		ShopManager.parseShops().load();
		PlayerOwnedShopManager.load();
		NPC.init();
	}

	public GameEngine getEngine() {
		return engine;
	}
}
