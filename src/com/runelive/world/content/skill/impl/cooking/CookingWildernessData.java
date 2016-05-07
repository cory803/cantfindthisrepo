package com.runelive.world.content.skill.impl.cooking;

import java.security.SecureRandom;

import com.runelive.model.Skill;
import com.runelive.world.entity.impl.player.Player;
import com.runelive.world.content.Emotes.Skillcape_Data;

/**
 * Data for the cooking skill.
 * 
 * @author Admin Gabriel
 */
public enum CookingWildernessData {

  SHRIMP(317, 315, 7954, 1, 1500, 33, "shrimp"),
  ANCHOVIES(321, 319, 323, 1, 1500, 34, "anchovies"),
  TROUT(335, 333, 343, 15, 3500, 50, "trout"),
  COD(341, 339, 343, 18, 3750, 54, "cod"),
  SALMON(331, 329, 343, 25, 3750, 58, "salmon"),
  TUNA(359, 361, 367, 30, 5000, 58, "tuna"),
  LOBSTER(377, 379, 381, 40, 6000, 74, "lobster"),
  BASS(363, 365, 367, 40, 8000, 75, "bass"),
  SWORDFISH(371, 373, 375, 45, 7000, 86, "swordfish"),
  MONKFISH(7944, 7946, 7948, 62, 7500, 91, "monkfish"),
  SHARK(383, 385, 387, 80, 10500, 94, "shark"),
  SEA_TURTLE(395, 397, 399, 82, 10550, 105, "sea turtle"),
  MANTA_RAY(389, 391, 393, 91, 10550, 99, "manta ray"),
  ROCKTAIL(15270, 15272, 15274, 92, 11250, 99, "rocktail"),
  CAVEFISH(15264, 15266, 15268, 88, 8560, 94, "cavefish"),
  KARAMBWAN(3142, 3144, 3148, 30, 9500, 90, "karambwan"),
  HEIM_CRAB(17797, 18159, 18179, 5, 50, 40, "heim crab"),
  RED_EYE(17799, 18161, 18181, 10, 75, 45, "red-eye"),
  DUSK_EEL(17801, 18163, 18183, 12, 82, 47, "dusk eel"),
  GIANT_FLATFISH(17803, 18165, 18185, 15, 90, 50, "giant flatfish"),
  SHORT_FINNED_EEL(17805, 18167, 18187, 18, 114, 54, "short-finned eel"),
  WEB_SNIPPER(17807, 18169, 18189, 30, 211, 60, "web snipper"),
  BOULDABASS(17809, 18171, 18191, 40, 288, 75, "bouldabass"),
  SALVE_EEL(17811, 18173, 18193, 60, 415, 81, "salve eel"),
  BLUE_CRAB(17813, 18175, 18195, 75, 1066, 92, "blue crab");

  int rawItem, cookedItem, burntItem, levelReq, xp, stopBurn;
  String name;

  CookingWildernessData(int rawItem, int cookedItem, int burntItem, int levelReq, int xp, int stopBurn,
      String name) {
    this.rawItem = rawItem;
    this.cookedItem = cookedItem;
    this.burntItem = burntItem;
    this.levelReq = levelReq;
    this.xp = xp;
    this.stopBurn = stopBurn;
    this.name = name;
  }

  public int getRawItem() {
    return rawItem;
  }

  public int getCookedItem() {
    return cookedItem;
  }

  public int getBurntItem() {
    return burntItem;
  }

  public int getLevelReq() {
    return levelReq;
  }

  public int getXp() {
    return xp;
  }

  public int getStopBurn() {
    return stopBurn;
  }

  public String getName() {
    return name;
  }

  public static CookingWildernessData forFish(int fish) {
    for (CookingWildernessData data : CookingWildernessData.values()) {
      if (data.getRawItem() == fish) {
        return data;
      }
    }
    return null;
  }

  public static final int[] cookingRanges = {2732, 114, 2728, 9682};

  public static boolean isRange(int object) {
    for (int i : cookingRanges)
      if (object == i)
        return true;
    return false;
  }

  /**
   * Get's the rate for burning or successfully cooking food.
   * 
   * @param player Player cooking.
   * @return Successfully cook food.
   */
  public static boolean success(Player player, int burnBonus, int levelReq,
                                int stopBurn) {
    if (player.getSkillManager().getCurrentLevel(Skill.COOKING) >= stopBurn || Skillcape_Data.MASTER_COOKING.isWearingCape(player) || Skillcape_Data.COOKING.isWearingCape(player)) {
      return true;
    }
    double burn_chance = (45.0 - burnBonus);
    double cook_level = player.getSkillManager().getCurrentLevel(
            Skill.COOKING);
    double lev_needed = (double) levelReq;
    double burn_stop = (double) stopBurn;
    double multi_a = (burn_stop - lev_needed);
    double burn_dec = (burn_chance / multi_a);
    double multi_b = (cook_level - lev_needed);
    burn_chance -= (multi_b * burn_dec);
    double randNum = cookingRandom.nextDouble() * 100.0;
    return burn_chance <= randNum;
  }

  private static SecureRandom cookingRandom = new SecureRandom(); // The random factor

  public static boolean canCook(Player player, int id) {
    CookingWildernessData fish = forFish(id);
    if (fish == null)
      return false;
    if (player.getSkillManager().getMaxLevel(Skill.COOKING) < fish.getLevelReq() - 2) {
      player.getPacketSender().sendMessage(
          "You need a Cooking level of atleast " + (fish.getLevelReq() - 2) + " to cook this.");
      return false;
    }
    if (!player.getInventory().contains(id + 1)) {
      player.getPacketSender().sendMessage("You have run out of fish.");
      return false;
    }
    return true;
  }

}
