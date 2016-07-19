package com.runelive;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.runelive.cache.Archive;
import com.runelive.model.definitions.*;
import com.runelive.model.npc.drops.LootSystem;
import com.runelive.util.FontUtils;
import com.runelive.world.clip.region.doors.DoubleDoor;
import com.runelive.world.clip.region.doors.SingleDoor;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.runelive.engine.GameEngine;
import com.runelive.engine.task.TaskManager;
import com.runelive.engine.task.impl.ServerTimeUpdateTask;
import com.runelive.model.container.impl.PlayerOwnedShopContainer.PlayerOwnedShopManager;
import com.runelive.model.container.impl.Shop.ShopManager;
import com.runelive.net.PipelineFactory;
import com.runelive.net.security.ConnectionHandler;
import com.runelive.world.clip.region.RegionClipping;
import com.runelive.world.content.CustomObjects;
import com.runelive.world.content.Lottery;
import com.runelive.world.content.Scoreboard;
import com.runelive.world.content.WellOfGoodwill;
import com.runelive.world.content.clan.ClanChatManager;
import com.runelive.world.content.combat.effect.CombatPoisonEffect.CombatPoisonData;
import com.runelive.world.content.combat.effect.CombatVenomEffect.CombatVenomData;
import com.runelive.world.content.combat.strategy.CombatStrategies;
import com.runelive.world.content.dialogue.DialogueManager;
import com.runelive.world.content.grandexchange.GrandExchangeOffers;
import com.runelive.world.content.pos.PlayerOwnedShops;
import com.runelive.world.entity.impl.npc.NPC;

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
		RegionClipping.loadRegions(new Archive(GameServer.cache.getFile(0, 5)));
		FontUtils.initialize(new Archive(GameServer.cache.getFile(0, 1)));
		GameObjectDefinition.init();
		CustomObjects.init();
		ItemDefinition.init().load();
		Lottery.init();
		GrandExchangeOffers.init();
		//SingleDoor.getSingleton().load();
		//DoubleDoor.getSingleton().load();
		PlayerOwnedShops.init();
		WellOfGoodwill.init();
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
		DialogueManager.parseDialogues().load();
		NPC.init();
	}

	public GameEngine getEngine() {
		return engine;
	}
}
