package com.chaos.world.content.skill.impl.summoning;

import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;

public class BossPets {

	/**
	 *  Boss Pets
	 *	Effects are given to the player only when used with
	 *	@return
	 */

	public enum BossPet {

		/**
		 *  Chaos Elemental Jr.
		 *	Don't get teleported away in combat
		 *	@Chaos Elemental
		 */
		PET_CHAOS_ELEMENTAL(3200, 5907, 11995),

		/**
		 *  Prince Black Dragon
		 *	Never get damaged by fire breath.
		 *	@All Dragons
		 */
		PRINCE_BLACK_DRAGON(50, 6652, 12653),

		/**
		 *  General Graardor Jr.
		 *	Require no kill count to enter Bandos.
		 *	@Godwars
		 */
		PET_GENERAL_GRAARDOR(3031, 6644, 12650),

		/**
		 *  TzRek-Jad
		 *	Trade a fire cape or tokhaar kal to a player.
		 *	@All Players
		 */
		TZREK_JAD(2745, 5892, 13225),

		/**
		 *  Dark Core
		 *	Receive a 25% defensive boost against Corporeal Beast.
		 *	@Corporeal Beast
		 */
		PET_DARK_CORE(8133, 388, 12816),

		/**
		 *  Kree'arra Jr.
		 *	Require no kill count to enter Armadyl.
		 *	@Godwars
		 */
		PET_KREE_ARRA(6222, 6643, 12649),

		/**
		 *  K'ril Tsutsaroth Jr.
		 *	Require no kill count to enter Zamorak.
		 *	@Godwars
		 */
		PET_KRIL_TSUTSAROTH(6203, 6647, 12652),

		/**
		 *  Zilyana Jr.
		 *	Require no kill count to enter Saradomin.
		 *	@Godwars
		 */
		PET_ZILYANA(6247, 6646, 12651),

		/**
		 *  Dagannoth Supreme Jr.
		 *	Gain a 10% overall damage boost.
		 *	@Dagannoth Supreme
		 */
		PET_DAGANNOTH_SUPREME(2881, 6626, 12643),

		/**
		 *  Dagannoth Prime Jr.
		 *	Gain a 10% overall damage boost.
		 *	@Dagannoth Prime
		 */
		PET_DAGANNOTH_PRIME(2882, 6627, 12644),

		/**
		 *  Dagannoth Rex Jr.
		 *	Gain a 10% overall damage boost.
		 *	@Dagannoth Rex
		 */
		PET_DAGANNOTH_REX(2883, 6641, 12645),

		/**
		 *  Kalphite Princess
		 *	Hit through combat deflection prayers.
		 *	@Kalphite Queen
		 */
		KALPHITE_PRINCESS(6653, 6653, 12654),

		/**
		 *  Hellpuppy
		 *	Gain a 10% increased drop rate.
		 *	@Cerberus
		 */
		HELLPUPPY(1158, 964, 13247),

		/**
		 *  Venenatis Spiderling
		 *	Have unlimited prayer points
		 *	@Venenatis
		 */
		VENENATIS_SPIDERLING(2000, 495, 13177),

		/**
		 *  Callisto Cub
		 *	Block 25% of all damage.
		 *	@Callisto
		 */
		CALLISTO_CUB(6609, 497, 13178),

		/**
		 *  Kraken
		 *	Find your opponent on the first whirlpool.
		 *	@Kraken
		 */
		//TODO: Kraken isn't added yet
		PET_KRAKEN(6656, 6656, 12655),

		/**
		 *  Scorpia's offspring
		 *	Protect yourself from venom & poison.
		 *	@Scorpia
		 */
		SCORPIAS_OFFSPRING(2001, 5547, 13181),

		/**
		 *  Scorpia's offspring
		 *	Protect yourself from venom & poison.
		 *	@Scorpia
		 */
		//TODO: Vetion isn't added yet
		PET_VETION(5536, 5536, 13179),

		/**
		 *  Smoke Devil
		 *	-Undecided-
		 *	@return null
		 */
		SMOKE_DEVIL(6655, 6655, 12648),

		/**
		 *  Beaver
		 *	Gain double the logs from woodcutting.
		 *	Stackable with all other benefits.
		 *	@Woodcutting
		 */
		BEAVER(-1, 6717, 13322),

		/**
		 *  Heron
		 *	Catch double hunter entities.
		 *	Stackable with all other benefits.
		 *	@Hunter
		 */
		HERON(6715, 6715, 13320),

		/**
		 *  Rock Golem
		 *	Mine 2 ores at a time.
		 *	Stackable with all other benefits.
		 *	@Mining
		 */
		ROCK_GOLEM(6716, 6716, 13321),


		/**
		 *  Snakeling
		 *	-Undecided-
		 *	@return null
		 */
		//TODO: Zulrah isn't added yet
		ZULRAH_RANGE(2127, 2127, 12921),
		ZULRAH_MAGE(2129, 2129, 12940),
		ZULRAH_MELEE(2128, 2128, 12939);

		BossPet(int npcId, int spawnNpcId, int itemId) {
			this.npcId = npcId;
			this.spawnNpcId = spawnNpcId;
			this.itemId = itemId;
		}

		public int npcId, spawnNpcId, itemId;

		public static BossPet forId(int itemId) {
			for (BossPet p : BossPet.values()) {
				if (p != null && p.itemId == itemId) {
					return p;
				}
			}
			return null;
		}

		public static BossPet forSpawnId(int spawnNpcId) {
			for (BossPet p : BossPet.values()) {
				if (p != null && p.spawnNpcId == spawnNpcId) {
					return p;
				}
			}
			return null;
		}
	}

	public static boolean pickup(Player player, NPC npc) {
		BossPet pet = BossPet.forSpawnId(npc.getId());
		if (player.getInventory().getFreeSlots() >= 1) {
			if (pet != null) {
				if (player.getSummoning().getFamiliar() != null
						&& player.getSummoning().getFamiliar().getSummonNpc() != null
						&& player.getSummoning().getFamiliar().getSummonNpc().isRegistered()) {
					if (player.getSummoning().getFamiliar().getSummonNpc().getId() == pet.spawnNpcId
							&& player.getSummoning().getFamiliar().getSummonNpc().getIndex() == npc.getIndex()) {
						player.getSummoning().unsummon(true, true);
						player.getPacketSender().sendMessage("You pick up your pet.");
						return true;
					} else {
						player.getPacketSender().sendMessage("This is not your pet to pick up.");
					}
				} else {
					player.getPacketSender().sendMessage("This is not your pet to pick up.");
				}
			}
			return false;
		} else {
			player.getPacketSender().sendMessage("You need atleast 1 free inventory space to pickup your pet.");
			return false;
		}
	}
}