package com.runelive.world.content.skill.impl.summoning;

import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;

public class BossPets {

  public static enum BossPet {

    PET_CHAOS_ELEMENTAL(3200, 3033, 11995), PET_KING_BLACK_DRAGON(50, 3030,
            11996), PET_GENERAL_GRAARDOR(6260, 3031, 11997), PET_TZTOK_JAD(2745, 3032,
            11978), PET_CORPOREAL_BEAST(8133, 3034, 12001), PET_KREE_ARRA(6222, 3035,
            12002), PET_KRIL_TSUTSAROTH(6203, 3036, 12003), PET_COMMANDER_ZILYANA(6247, 3037,
            12004), PET_DAGANNOTH_SUPREME(2881, 3038, 12005), PET_DAGANNOTH_PRIME(2882,
            3039, 12006), PET_DAGANNOTH_REX(2883, 3040, 11990), PET_FROST_DRAGON(51,
            3047,
            11991), PET_TORMENTED_DEMON(8349, 3048, 11992), PET_KALPHITE_QUEEN(1158,
            3050, 11993), PET_SLASH_BASH(2060, 3051, 11994), PET_PHOENIX(8549,
            3052, 11989), PET_BANDOS_AVATAR(4540, 3053, 11988), PET_NEX(
            13447, 3054, 11987), PET_JUNGLE_STRYKEWYRM(9467, 3055,
            11986), PET_DESERT_STRYKEWYRM(9465, 3056,
            11985), PET_ICE_STRYKEWYRM(9463, 3057,
            11984), PET_GREEN_DRAGON(941, 3058,
            11983), PET_BABY_BLUE_DRAGON(52, 3059,
            11982), PET_BLUE_DRAGON(55, 3060,
            11981), PET_BLACK_DRAGON(54, 3061,
            11979), PET_QUEEN_WHITE_DRAGON(
            4000, 4001,
            13000), PET_LIME_ROCK_CRAB(
            5507, 5551,
            13001), PET_AQUA_ROCK_CRAB(
            5508, 5552,
            13002), PET_PINK_ROCK_CRAB(
            5509, 5553,
            13003), PET_RED_ROCK_CRAB(
            5510, 5554,
            13004), PET_WHITE_ROCK_CRAB(
            5511,
            5555,
            13005), PET_YELLOW_ROCK_CRAB(
            5512,
            5556,
            13006), PET_ORANGE_ROCK_CRAB(
            5513,
            5557,
            13007), PET_PURPLE_ROCK_CRAB(
            5514,
            5558,
            13008),
    HELLPUPPY(5866, 5870, 12703),
    VENENATIS(2000, 2005, 21103),
    PET_SEAGULL(2707, 2707, 7887),
    PET_ROCK(8648, 8648, 21250),
    PET_CHINCHOMPA(9579, 9579, 21251),
    PET_RACCOON(5559, 5559, 12487);

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
