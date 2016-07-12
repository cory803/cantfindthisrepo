package com.runelive.model.definitions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.runelive.GameSettings;
import com.runelive.model.Graphic;
import com.runelive.model.GroundItem;
import com.runelive.model.Item;
import com.runelive.model.Locations.Location;
import com.runelive.model.Position;
import com.runelive.model.Skill;
import com.runelive.model.container.impl.Bank;
import com.runelive.model.container.impl.Equipment;
import com.runelive.util.JsonLoader;
import com.runelive.util.Misc;
import com.runelive.world.World;
import com.runelive.world.content.DropLog;
import com.runelive.world.content.DropLog.DropLogEntry;
import com.runelive.world.content.PlayerLogs;
import com.runelive.world.content.clan.ClanChatManager;
import com.runelive.world.content.minigames.impl.WarriorsGuild;
import com.runelive.world.content.skill.impl.prayer.BonesData;
import com.runelive.world.content.skill.impl.summoning.CharmingImp;
import com.runelive.world.entity.impl.GroundItemManager;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;

/**
 * Controls the npc drops
 * 
 * @author 2012 <http://www.rune-server.org/members/dexter+morgan/>, Gabbe &
 *         Samy
 * 
 */
public class NPCDrops {

	/**
	 * The map containing all the npc drops.
	 */
	private static Map<Integer, NPCDrops> dropControllers = new HashMap<Integer, NPCDrops>();

	public static JsonLoader parseDrops() {

		ItemDropAnnouncer.init();

		return new JsonLoader() {

			@Override
			public void load(JsonObject reader, Gson builder) {
				int[] npcIds = builder.fromJson(reader.get("npcIds"), int[].class);
				NpcDropItem[] drops = builder.fromJson(reader.get("drops"), NpcDropItem[].class);

				NPCDrops d = new NPCDrops();
				d.npcIds = npcIds;
				d.drops = drops;
				for (int id : npcIds) {
					dropControllers.put(id, d);
				}
			}

			@Override
			public String filePath() {
				return "./data/def/json/drops.json";
			}
		};
	}

	/**
	 * The id's of the NPC's that "owns" this class.
	 */
	private int[] npcIds;

	/**
	 * All the drops that belongs to this class.
	 */
	private NpcDropItem[] drops;

	/**
	 * Gets the NPC drop controller by an id.
	 * 
	 * @return The NPC drops associated with this id.
	 */
	public static NPCDrops forId(int id) {
		return dropControllers.get(id);
	}

	public static Map<Integer, NPCDrops> getDrops() {
		return dropControllers;
	}

	/**
	 * Gets the drop list
	 * 
	 * @return the list
	 */
	public NpcDropItem[] getDropList() {
		return drops;
	}

	/**
	 * Gets the npcIds
	 * 
	 * @return the npcIds
	 */
	public int[] getNpcIds() {
		return npcIds;
	}

	/**
	 * Represents a npc drop item
	 */
	public static class NpcDropItem {

		/**
		 * The id.
		 */
		private final int id;

		/**
		 * Array holding all the amounts of this item.
		 */
		private final int[] count;

		/**
		 * The chance of getting this item.
		 */
		private final int chance;

		/**
		 * New npc drop item
		 * 
		 * @param id
		 *            the item
		 * @param count
		 *            the count
		 * @param chance
		 *            the chance
		 */
		public NpcDropItem(int id, int[] count, int chance) {
			this.id = id;
			this.count = count;
			this.chance = chance;
		}

		/**
		 * Gets the item id.
		 * 
		 * @return The item id.
		 */
		public int getId() {
			return id;
		}

		/**
		 * Gets the chance of a drop.
		 * 
		 * @return The drop chance.
		 */
		public int getChanceId() {
			return chance;
		}

		/**
		 * Gets the chance.
		 * 
		 * @return The chance.
		 */
		public int[] getCount() {
			return count;
		}

		/**
		 * Gets the chance.
		 * 
		 * @return The chance.
		 */
		public DropChance getChance() {
			switch (chance) {
			case 1:
				return DropChance.ALMOST_ALWAYS; // 50% <-> 1/2
			case 2:
				return DropChance.VERY_COMMON; // 20% <-> 1/5
			case 3:
				return DropChance.COMMON; // 5% <-> 1/20
			case 4:
				return DropChance.UNCOMMON; // 2% <-> 1/50
			case 5:
				return DropChance.RARE; // 0.5% <-> 1/200
			case 6:
				return DropChance.LEGENDARY; // 0.2% <-> 1/500
			case 7:
				return DropChance.LEGENDARY_2;
			case 8:
				return DropChance.LEGENDARY_3;
			case 9:
				return DropChance.LEGENDARY_4;
			case 10:
				return DropChance.LEGENDARY_5;
			default:
				return DropChance.ALWAYS; // 100% <-> 1/1
			}
		}

		/**
		 * Gets the item
		 * 
		 * @return the item
		 */
		public Item getItem() {
			int amount = 0;
			for (int i = 0; i < count.length; i++)
				amount += count[i];
			if (amount > count[0])
				amount = count[0] + Misc.getRandom(count[1]);
			return new Item(id, amount);
		}

		public int getCount1() {
			return count[0];
		}

		public int getCount2() {
			return count[count.length];
		}
	}

	public enum DropChance {
		ALWAYS(0), ALMOST_ALWAYS(2), VERY_COMMON(5), COMMON(15), UNCOMMON(70), NOTTHATRARE(100), RARE(250), LEGENDARY(
				500), LEGENDARY_2(800), LEGENDARY_3(1000), LEGENDARY_4(1200), LEGENDARY_5(1500);

		DropChance(int randomModifier) {
			this.random = randomModifier;
		}

		private int random;

		public int getRandom() {
			return this.random;
		}
	}

	/**
	 * Drops items for a player after killing an npc. A player can max receive
	 * one item per drop chance.
	 * 
	 * @param p
	 *            Player to receive drop.
	 * @param npc
	 *            NPC to receive drop FROM.
	 */
	public static void dropItems(Player p, NPC npc) {
		if (npc.getLocation() == Location.WARRIORS_GUILD)
			WarriorsGuild.handleDrop(p, npc);
		NPCDrops drops = NPCDrops.forId(npc.getId());
		if (drops == null)
			return;
		final boolean goGlobal = p.getPosition().getZ() >= 0 && p.getPosition().getZ() < 4;
		final boolean ringOfWealth = p.getEquipment().get(Equipment.RING_SLOT).getId() == 2572;
		final boolean ringOfWealthi = p.getEquipment().get(Equipment.RING_SLOT).getId() == 21110;
		final Position npcPos = npc.getPosition().copy();
		boolean[] dropsReceived = new boolean[12];

		if (drops.getDropList().length > 0 && p.getPosition().getZ() >= 0 && p.getPosition().getZ() < 4) {
			if (p.getLocation() == Location.WILDERNESS || p.getLocation() == Location.WILDKEY_ZONE) {
				wildernessScrollDrop(p, npc.getDefinition().getCombatLevel(), npcPos);
			}
			casketDrop(p, npc.getDefinition().getCombatLevel(), npcPos);
			if (npc.getLocation() == Location.WILDERNESS)
				wildKeys(p, npc.getDefinition().getCombatLevel(), npcPos);
		}

		for (int i = 0; i < drops.getDropList().length; i++) {
			if (drops.getDropList()[i].getItem().getId() <= 0
					|| drops.getDropList()[i].getItem().getId() > ItemDefinition.getMaxAmountOfItems()
					|| drops.getDropList()[i].getItem().getAmount() <= 0) {
				continue;
			}

			final DropChance dropChance = drops.getDropList()[i].getChance();

			if (dropChance == DropChance.ALWAYS) {
				drop(p, drops.getDropList()[i].getItem(), npc, npcPos, goGlobal);
			} else {
				if (shouldDrop(dropsReceived, dropChance, ringOfWealth, ringOfWealthi)) {
					drop(p, drops.getDropList()[i].getItem(), npc, npcPos, goGlobal);
					dropsReceived[dropChance.ordinal()] = true;
				}
			}
		}
	}

	public static void dropBossSystem(Player p, NPC npc) {
		NPCDrops drops = NPCDrops.forId(npc.getId());
		if (drops == null)
			return;
		final boolean goGlobal = p.getPosition().getZ() >= 0 && p.getPosition().getZ() < 4;
		final boolean ringOfWealth = p.getEquipment().get(Equipment.RING_SLOT).getId() == 2572;
		final boolean ringOfWealthi = p.getEquipment().get(Equipment.RING_SLOT).getId() == 21110;
		final Position npcPos = new Position(npc.getPosition().getX(), npc.getPosition().getY(), 0);
		boolean[] dropsReceived = new boolean[12];

		if (drops.getDropList().length > 0 && p.getPosition().getZ() >= 0 && p.getPosition().getZ() < 4) {
			casketDrop(p, npc.getDefinition().getCombatLevel(), npcPos);
			wildKeys(p, npc.getDefinition().getCombatLevel(), npcPos);
		}

		for (int i = 0; i < drops.getDropList().length; i++) {
			if (drops.getDropList()[i].getItem().getId() <= 0
					|| drops.getDropList()[i].getItem().getId() > ItemDefinition.getMaxAmountOfItems()
					|| drops.getDropList()[i].getItem().getAmount() <= 0) {
				continue;
			}

			final DropChance dropChance = drops.getDropList()[i].getChance();

			if (dropChance == DropChance.ALWAYS) {
				drop(p, drops.getDropList()[i].getItem(), npc, npcPos, goGlobal);
			} else {
				if (shouldDrop(dropsReceived, dropChance, ringOfWealth, ringOfWealthi)) {
					drop(p, drops.getDropList()[i].getItem(), npc, npcPos, goGlobal);
					dropsReceived[dropChance.ordinal()] = true;
				}
			}
		}
	}

	public static boolean shouldDrop(boolean[] b, DropChance chance, boolean ringOfWealth, boolean ringOfWealthi) {
		int random = chance.getRandom();
		if (ringOfWealth && random >= 60) {
			random -= (random / 10);
			random -= (random / 20);
		} else if (ringOfWealth && random >= 60) {
			random -= (random / 5);
			random -= (random / 10);
		}
		return !b[chance.ordinal()] && Misc.getRandom(random) == 1;
	}

	public static void drop(Player player, Item item, NPC npc, Position pos, boolean goGlobal) {
		if (player.getInventory().contains(18337) && BonesData.forId(item.getId()) != null) {
			player.getPacketSender().sendGlobalGraphic(new Graphic(777), pos);
			player.getSkillManager().addExperience(Skill.PRAYER, BonesData.forId(item.getId()).getBuryingXP());
			return;
		}
		int itemId = item.getId();
		int amount = item.getAmount();

		if (itemId == CharmingImp.GOLD_CHARM || itemId == CharmingImp.GREEN_CHARM || itemId == CharmingImp.CRIM_CHARM
				|| itemId == CharmingImp.BLUE_CHARM || itemId == CharmingImp.TALON_BEAST_CHARM  || itemId == CharmingImp.OBSIDIAN_CHARM
				 || itemId == CharmingImp.ABYSSAL_CHARM) {
			if (player.getInventory().contains(6500) && CharmingImp.handleCharmDrop(player, itemId, amount)) {
				return;
			}
		}

		Player toGive = player;

		boolean ccAnnounce = false;
		if (Location.inMulti(player)) {
			if (player.getCurrentClanChat() != null && player.getCurrentClanChat().getLootShare()) {
				CopyOnWriteArrayList<Player> playerList = new CopyOnWriteArrayList<Player>();
				for (Player member : player.getCurrentClanChat().getMembers()) {
					if (member != null) {
						if (member.getPosition().isWithinDistance(player.getPosition())) {
							playerList.add(member);
						}
					}
				}
				if (playerList.size() > 0) {
					toGive = playerList.get(Misc.getRandom(playerList.size() - 1));
					if (toGive == null || toGive.getCurrentClanChat() == null
							|| toGive.getCurrentClanChat() != player.getCurrentClanChat()) {
						toGive = player;
					}
					ccAnnounce = true;
				}
			}
		}

		if (itemId == 18778) { // Effigy, don't drop one if player already has
								// one
			if (toGive.getInventory().contains(18778) || toGive.getInventory().contains(18779)
					|| toGive.getInventory().contains(18780) || toGive.getInventory().contains(18781)) {
				return;
			}
			for (Bank bank : toGive.getBanks()) {
				if (bank == null) {
					continue;
				}
				if (bank.contains(18778) || bank.contains(18779) || bank.contains(18780) || bank.contains(18781)) {
					return;
				}
			}
		}

		if (ItemDropAnnouncer.announce(itemId)) {
			String itemName = item.getDefinition().getName();
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			String npcName = Misc.formatText(npc.getDefinition().getName());
			switch (itemId) {
			case 14484:
				itemMessage = "a pair of Dragon Claws";
				break;
			case 20000:
			case 20001:
			case 20002:
				itemMessage = itemName;
				break;
			}
			switch (npc.getId()) {
			case 50:
			case 3200:
			case 8133:
			case 4540:
			case 1160:
			case 8549:
				npcName = "The " + npcName + "";
				break;
			case 51:
			case 54:
			case 5363:
			case 8349:
			case 1592:
			case 1591:
			case 1590:
			case 1615:
			case 9463:
			case 9465:
			case 9467:
			case 1382:
			case 13659:
			case 11235:
				npcName = "" + Misc.anOrA(npcName) + " " + npcName + "";
				break;
			}
			String message = "<icon=1><shad=FF8C38> " + toGive.getUsername() + " has just received " + itemMessage
					+ " from " + npcName + "!";
			World.sendMessage(message);

			if (ccAnnounce) {
				ClanChatManager.sendMessage(player.getCurrentClanChat(),
						"<col=16777215>[<col=255>Lootshare<col=16777215>]<col=3300CC> " + toGive.getUsername()
								+ " received " + itemMessage + " from " + npcName + "!");
			}

			PlayerLogs.npcdrops(toGive, item, npcName);
		}

		GroundItemManager.spawnGroundItem(toGive,
				new GroundItem(item, pos, toGive.getUsername(), false, 150, goGlobal, 200));
		if (GameSettings.DOUBLE_DROPS) {
			GroundItemManager.spawnGroundItem(toGive,
					new GroundItem(item, pos, toGive.getUsername(), false, 150, goGlobal, 200));
			DropLog.submit(toGive, new DropLogEntry(itemId, item.getAmount()));

		}
		DropLog.submit(toGive, new DropLogEntry(itemId, item.getAmount()));
	}

	public static void casketDrop(Player player, int combat, Position pos) {
		int chance = 1 + combat;
		if (Misc.getRandom(combat <= 50 ? 1300 : 1000) < chance) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(7956), pos, player.getUsername(), false, 150, true, 200));
		}
	}

	public static void wildernessScrollDrop(Player player, int combat, Position pos) {
		int chance = 50;
		if (Misc.getRandom(chance) == 3) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(15355), pos, player.getUsername(), false, 150, true, 200));
		}
	}

	public static int selectKey(int[] array) {
		int rnd = new Random().nextInt(array.length);
		return array[rnd];
	}

	public static void wildKeys(Player player, int combat, Position pos) {
		int chance = 1 + combat;
		if (Misc.getRandom(combat <= 50 ? 400 : 350) < 5) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(selectKey(player.allKeys)), pos,
					player.getUsername(), false, 150, true, 200));
			String message = "<icon=1><col=FF8C38>" + player.getUsername()
					+ " has just received a wilderness key as a random drop!";
			World.sendMessage(message);
		}
	}

	public static class ItemDropAnnouncer {

		private static List<Integer> ITEM_LIST;

		public static final int[] TO_ANNOUNCE = new int[] { 21114, 21113, 21112, 21111, 21110, 12703, 1543, 1545, 1546,
				21074, 21077, 21075, 21076, 21078, 21079, 1547, 1548, 6571, 14484, 4224, 11702, 11704, 11706, 12926,
				21107, 21108, 21109, 10887, 11708, 11704, 11724, 11726, 11728, 11718, 11720, 11722, 11730, 11716, 14876,
				11286, 13427, 6731, 6737, 6735, 4151, 21372, 2513, 15259, 13902, 13890, 13884, 13861, 13858, 13864,
				13905, 13887, 13893, 13899, 13873, 13879, 13876, 13870, 6571, 14008, 14009, 14010, 14011, 14012, 14013,
				14014, 14015, 14016, 13750, 13748, 13746, 13752, 11335, 15486, 13870, 13873, 13876, 13884, 13890, 13896,
				13902, 13858, 13861, 13864, 13867, 11995, 11996, 11997, 11978, 12001, 12002, 12003, 12004, 12005, 12006,
				11990, 11991, 11992, 11993, 11994, 11989, 11988, 11987, 11986, 11985, 11984, 11983, 11982, 11981, 11979,
				13659, 11235, 13754, 20000, 20001, 20002, 15103, 15104, 15105, 15106, 21000, 21001, 21002, 21003, 21004,
				21005, 21006, 21007, 1053, 1830, 1055, 1057, 1831, 1832, 1834, 8000, 21008, 21009, 21010, 21011, 21012,
				21013, 21014, 21015, 21016, 21017, 6564, 6576, 21018, 21019, 21020, 21021, 21022, 21023, 21024, 21025,
				21026, 21027, 21028, 21029, 15017, 21030, 21031, 21032, 21033, 21034, 21035, 21036, 21037, 21038, 21039,
				21040, 21041, 21042, 10330, 10332, 10334, 10336, 10338, 10340, 10342, 10344, 10346, 10348, 10350, 10352,
				21047, 21038, 13000, 13001, 13002, 13003, 13004, 13005, 13006, 13007, 13008, 20171, 21089, 21090, 21103,
				21091, 21092, 21100, 21101, 15259, 15241, 13576, 21104, 19672, 19673, 19674, 21111, 21112, 21113, 11694,
				14004, 14005, 14006, 14007, 21136 };

		private static void init() {
			ITEM_LIST = new ArrayList<Integer>();
			for (int i : TO_ANNOUNCE) {
				ITEM_LIST.add(i);
			}
		}

		public static boolean announce(int item) {
			return ITEM_LIST.contains(item);
		}
	}
}
