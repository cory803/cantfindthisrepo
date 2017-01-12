package com.runelive;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.runelive.cache.Archive;
import com.runelive.engine.GameEngine;
import com.runelive.engine.task.TaskManager;
import com.runelive.engine.task.impl.ServerTimeUpdateTask;
import com.runelive.model.container.impl.PlayerOwnedShopContainer.PlayerOwnedShopManager;
import com.runelive.model.container.impl.Shop.ShopManager;
import com.runelive.model.definitions.*;
import com.runelive.model.npc.drops.LootSystem;
import com.runelive.net.PipelineFactory;
import com.runelive.net.security.ConnectionHandler;
import com.runelive.util.FontUtils;
import com.runelive.world.World;
import com.runelive.world.content.CustomObjects;
import com.runelive.world.content.Scoreboard;
import com.runelive.world.content.clan.ClanChatManager;
import com.runelive.world.content.combat.effect.CombatPoisonEffect.CombatPoisonData;
import com.runelive.world.content.combat.effect.CombatVenomEffect.CombatVenomData;
import com.runelive.world.content.combat.strategy.CombatStrategies;
import com.runelive.world.content.diversions.hourly.HourlyDiversionManager;
import com.runelive.world.content.lottery.LotterySaving;
import com.runelive.world.content.pos.PlayerOwnedShops;
import com.runelive.world.content.wells.WellOfGoodness;
import com.runelive.world.doors.DoorManager;
import com.runelive.world.entity.impl.npc.NPC;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * testCredit: lare96, Gabbe, Jonny, High105
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
		CacheObjectDefinition.load(new Archive(GameServer.cache.getFile(0, 2)));
		World.loadRegions(new Archive(GameServer.cache.getFile(0, 5)));
		CustomObjects.init();
		ItemDefinition.init().load();
		DoorManager.init();
		PlayerOwnedShops.init();
		LotterySaving.load();
		PlayerOwnedShopManager.load();
		ClanChatManager.init();
		CombatPoisonData.init();
		CombatVenomData.init();
		CombatStrategies.init();
		Scoreboard.load();
		NpcDefinition.parseNpcs().load();
		LootSystem.loadDropTables();
		WeaponInterfaces.parseInterfaces().load();
		ShopManager.parseShops().load();
		NPC.init();
		WellOfGoodness.init();
		HourlyDiversionManager.init();

		/**
		 * Test drop rate formulas
		 */
		//TestDropChance.chance(6609);

		/**
		 * Test formulas for the game
		 */
		//Formulas.generate();

		/**
		 * Dumps a list of item drops into
		 * lists/drops.txt
		 */
		//DropListDumper.dump();

		/**
		 * Dumps a list of treasure island loot into
		 * lists/treasure_island_loot.txt
		 */
		//TreasureIslandLootDumper.dump();

		/**
		 * Dumps a list of wiki drop tables
		 * lists/drops.txt
		 */
		//WikiDropListDumper.dump();

		/**
		 * Dumps a list of item definitions into
		 * lists/items.txt
		 */
		//ItemListDumper.dump();

		/**
		 * Dumps a list of npc definitions into
		 * lists/npcs.txt
		 */
		//NpcListDumper.dump();

		/**
		 * Dump a drop table for a specific npc id.
		 */
		//WikiDumper.dumpNpcDropDefinition(7286);
		//DropManager.saveDrops();
	}

	public GameEngine getEngine() {
		return engine;
	}
}
