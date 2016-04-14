package com.ikov.world.content.skill.impl.thieving;

import com.ikov.model.Animation;
import com.ikov.model.Item;
import com.ikov.model.Skill;
import com.ikov.util.Misc;
import com.ikov.world.content.Achievements;
import com.ikov.world.content.Achievements.AchievementData;
import com.ikov.world.entity.impl.player.Player;

public class Stalls {
  private static int botStop;

  public static int getBotStop() {
    return botStop;
  }

  public static void setBotStop(int botStop) {
    Stalls.botStop = botStop;
  }

  public static void stealFromStall(Player player, int lvlreq, int xp, int reward, String message) {
    /*
     * if (player.isPassedRandom() == false) { if(getBotStop() == 1) { player.setInputHandling(new
     * ThievBots()); player.getPacketSender().sendEnterInputPrompt("Please enter ikov2"); } else
     * if(getBotStop() == 2) { player.setInputHandling(new ThievBots());
     * player.getPacketSender().sendEnterInputPrompt("What is 10+5?"); } else if(getBotStop() == 3)
     * { player.setInputHandling(new ThievBots()); player.getPacketSender().sendEnterInputPrompt(
     * "Are you botting?"); } } else {
     */
    if (player.getInventory().getFreeSlots() < 1) {
      player.getPacketSender().sendMessage("You need some more inventory space to do this.");
      return;
    }
    if (player.getCombatBuilder().isBeingAttacked()) {
      player.getPacketSender()
          .sendMessage("You must wait a few seconds after being out of combat before doing this.");
      return;
    }
    if (!player.getClickDelay().elapsed(2000))
      return;
    if (player.getSkillManager().getMaxLevel(Skill.THIEVING) < lvlreq) {
      player.getPacketSender().sendMessage(
          "You need a Thieving level of at least " + lvlreq + " to steal from this stall.");
      return;
    }
    player.performAnimation(new Animation(881));
    player.getPacketSender().sendMessage(message);
    player.getPacketSender().sendInterfaceRemoval();
    player.getSkillManager().addExperience(Skill.THIEVING, xp);
    player.getClickDelay().reset();
    player.getInventory().add(reward, 1);
    player.getSkillManager().stopSkilling();
    /*
     * setBotStop(Misc.getRandom(50)); if(getBotStop() == 1) { player.setPassedRandom(false);
     * player.setInputHandling(new ThievBots()); player.getPacketSender().sendEnterInputPrompt(
     * "Please enter ikov2"); } else if(getBotStop() == 2) { player.setPassedRandom(false);
     * player.setInputHandling(new ThievBots()); player.getPacketSender().sendEnterInputPrompt(
     * "What is 10+5?"); } else if(getBotStop() == 3) { player.setPassedRandom(false);
     * player.setInputHandling(new ThievBots()); player.getPacketSender().sendEnterInputPrompt(
     * "Are you botting?"); }
     */
    if (reward == 15009)
      Achievements.finishAchievement(player, AchievementData.STEAL_A_RING);
    else if (reward == 11998) {
      Achievements.doProgress(player, AchievementData.STEAL_140_SCIMITARS);
      Achievements.doProgress(player, AchievementData.STEAL_5000_SCIMITARS);
    }
  }

  public static void stealFromStall(Player player, int lvlreq, int xp, Item item, String message,
      boolean which_stall) {
    /*
     * if (player.isPassedRandom() == false) { if(getBotStop() == 1) { player.setInputHandling(new
     * ThievBots()); player.getPacketSender().sendEnterInputPrompt("Please enter ikov2"); } else
     * if(getBotStop() == 2) { player.setInputHandling(new ThievBots());
     * player.getPacketSender().sendEnterInputPrompt("What is 10+5?"); } else if(getBotStop() == 3)
     * { player.setInputHandling(new ThievBots()); player.getPacketSender().sendEnterInputPrompt(
     * "Are you botting?"); } } else {
     */
    if (player.getInventory().getFreeSlots() < 1) {
      player.getPacketSender().sendMessage("You need some more inventory space to do this.");
      return;
    }
    if (player.getCombatBuilder().isBeingAttacked()) {
      player.getPacketSender()
          .sendMessage("You must wait a few seconds after being out of combat before doing this.");
      return;
    }
    if (!player.getClickDelay().elapsed(1700))
      return;
    if (player.getSkillManager().getMaxLevel(Skill.THIEVING) < lvlreq) {
      player.getPacketSender().sendMessage(
          "You need a Thieving level of at least " + lvlreq + " to steal from this stall.");
      return;
    }
    player.performAnimation(new Animation(881));
    player.getPacketSender().sendMessage("You steal " + item.getAmount() + " coins.");
    if (which_stall) {
      int fishyType = Misc.getRandom(6);
      if (fishyType == 1) {
        player.getInventory().add(15271, Misc.getRandom(10));
      } else if (fishyType == 2) {
        player.getInventory().add(384, Misc.getRandom(10));
      } else if (fishyType == 3) {
        player.getInventory().add(3143, Misc.getRandom(10));
      }
    } else {
      int veggieStall = Misc.getRandom(32);
      int amount = Misc.getRandom(2);
      amount++;
      switch (veggieStall) {
        case 1:
          player.getInventory().add(200, amount);
          break;
        case 2:
          player.getInventory().add(202, amount);
          break;
        case 3:
          player.getInventory().add(204, amount);
          break;
        case 4:
          player.getInventory().add(206, amount);
          break;
        case 5:
          player.getInventory().add(208, amount);
          break;
        case 6:
          player.getInventory().add(210, amount);
          break;
        case 7:
          player.getInventory().add(212, amount);
          break;
        case 8:
          player.getInventory().add(214, amount);
          break;
        case 9:
          player.getInventory().add(216, amount);
          break;
        case 10:
          player.getInventory().add(218, amount);
          break;
        case 11:
          player.getInventory().add(220, amount);
          break;
      }
    }
    player.getPacketSender().sendInterfaceRemoval();
    player.getSkillManager().addExperience(Skill.THIEVING, xp);
    player.getClickDelay().reset();
    player.getInventory().add(item.getId(), item.getAmount());
    player.getSkillManager().stopSkilling();
    /*
     * setBotStop(Misc.getRandom(50)); if(getBotStop() == 1) { player.setPassedRandom(false);
     * player.setInputHandling(new ThievBots()); player.getPacketSender().sendEnterInputPrompt(
     * "Please enter ikov2"); } else if(getBotStop() == 2) { player.setPassedRandom(false);
     * player.setInputHandling(new ThievBots()); player.getPacketSender().sendEnterInputPrompt(
     * "What is 10+5?"); } else if(getBotStop() == 3) { player.setPassedRandom(false);
     * player.setInputHandling(new ThievBots()); player.getPacketSender().sendEnterInputPrompt(
     * "Are you botting?"); }
     */
  }
}
