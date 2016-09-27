package com.chaos.world.content.skill.impl.summoning;

import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;

public class BossPets {

	public enum BossPet {

		PET_CHAOS_ELEMENTAL(5907, 5907, 11995),
		PRINCE_BLACK_DRAGON(6652, 6652, 12653),
		PET_GENERAL_GRAARDOR(6644, 6644, 12650),
		PET_TZTOK_JAD(5892, 5892, 13225),
		PET_CORPOREAL_BEAST(8133, 3034, 12001),
		PET_KREE_ARRA(6643, 6643, 12649),
		PET_KRIL_TSUTSAROTH(6647, 6647, 12652),
		PET_COMMANDER_ZILYANA(6646, 6646, 12651),
		PET_DAGANNOTH_SUPREME(6626, 6626, 12643),
		PET_DAGANNOTH_PRIME(6627, 6627, 12644),
		PET_DAGANNOTH_REX(6641, 6641, 12645),
		PET_FROST_DRAGON(51, 3047, 11991),
		PET_TORMENTED_DEMON(8349, 3048, 11992),
		PET_KALPHITE_QUEEN(6653, 6653, 12654),
		PET_SLASH_BASH(2060, 3051, 11994),
		PET_PHOENIX(8549, 3052, 11989),
		PET_BANDOS_AVATAR(4540, 3053, 11988),
		PET_NEX(13447, 3054, 11987),
		PET_JUNGLE_STRYKEWYRM(9467, 3055, 11986),
		PET_DESERT_STRYKEWYRM(9465, 3056, 11985),
		PET_ICE_STRYKEWYRM(9463, 3057, 11984),
		PET_GREEN_DRAGON(941, 3058, 11983),
		PET_BABY_BLUE_DRAGON(52, 3059, 11982),
		PET_BLUE_DRAGON(55, 3060, 11981),
		PET_BLACK_DRAGON(54, 3061, 11979),
		PET_QUEEN_WHITE_DRAGON(4000, 4001, 13000),
		HELLPUPPY(964, 964, 13247),
		VENENATIS(495, 495, 13177),
		PET_SEAGULL(2707, 2707, 7887),
		PET_ROCK(8648, 8648, 21250),
		PET_CHINCHOMPA(9579, 9579, 21251),
		PET_RACCOON(5559, 5559, 12487),
		DUNGEONEERING_PET(13089, 13089, 19891),
		CALLISTO_CUB(497, 497, 13178),
		PET_KRAKEN(6656, 6656, 12655),
		PET_SCORPIA(5547, 5547, 13181),
		PET_VETION(5536, 5536, 13179),
		SMOKE_DEVIL(6655, 6655, 12648),
		BEAVER(6717, 6717, 13322),
		HERON(6715, 6715, 13320),
		ROCK_GOLEM(6716, 6716, 13321),

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
