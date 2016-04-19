package com.ikov.world.content.skill.impl.prayer;

import com.ikov.engine.task.Task;
import com.ikov.engine.task.TaskManager;
import com.ikov.model.Animation;
import com.ikov.model.Graphic;
import com.ikov.model.Skill;
import com.ikov.model.definitions.ItemDefinition;
import com.ikov.model.input.impl.EnterAmountOfBonesToSacrifice;
import com.ikov.world.content.Achievements;
import com.ikov.world.content.Achievements.AchievementData;
import com.ikov.world.entity.impl.player.Player;

public class BonesOnAltar {

  public static void openInterface(Player player, int itemId, boolean gilded) {
    player.getSkillManager().stopSkilling();
    player.setSelectedSkillingItem(itemId);
    player.setInputHandling(new EnterAmountOfBonesToSacrifice());
    player.getPacketSender()
            .sendString(2799, ItemDefinition.forId(itemId).getName())
            .sendInterfaceModel(1746, itemId, 150)
            .sendChatboxInterface(4429);
    player.getPacketSender().sendString(2800,
            "How many would you like to offer?");
    player.setAttribute("gilded", gilded);
  }

  public static void offerBones(final Player player, final int amount) {
    final int boneId = player.getSelectedSkillingItem();
    final boolean gilded = player.getAttributes().get("gilded") != null ? (boolean)player.getAttributes().get("gilded") : false;
    player.getSkillManager().stopSkilling();
    final BonesData currentBone = BonesData.forId(boneId);
    if (currentBone == null)
      return;
    player.getPacketSender().sendInterfaceRemoval();
    player.setCurrentTask(new Task(2, player, true) {
      int amountSacrificed = 0;

      @Override
      public void execute() {
        if (amountSacrificed >= amount) {
          stop();
          return;
        }
        if (!player.getInventory().contains(boneId)) {
          player.getPacketSender().sendMessage(
                  "You have run out of "
                          + ItemDefinition.forId(boneId).getName()
                          + ".");
          stop();
          return;
        }
        if (player.getInteractingObject() != null) {
          player.setPositionToFace(player.getInteractingObject()
                  .getPosition().copy());
          player.getInteractingObject().performGraphic(
                  new Graphic(624));
        }
        if (currentBone == BonesData.BIG_BONES)
          Achievements.finishAchievement(player,
                  AchievementData.BURY_A_BIG_BONE);
        else if (currentBone == BonesData.FROSTDRAGON_BONES) {
          Achievements.doProgress(player,
                  AchievementData.BURY_25_FROST_DRAGON_BONES);
          Achievements.doProgress(player,
                  AchievementData.BURY_500_FROST_DRAGON_BONES);
        }
        amountSacrificed++;
        player.getInventory().delete(boneId, 1);
        player.performAnimation(new Animation(713));
        player.getSkillManager().addExperience(Skill.PRAYER,
                (int) (currentBone.getBuryingXP() * (gilded ? 1.7 : 1.5)));
      }

      @Override
      public void stop() {
        setEventRunning(false);
        player.getPacketSender().sendMessage(
                "You have pleased the gods with your "
                        + (amountSacrificed == 1 ? "sacrifice"
                        : "sacrifices") + ".");
      }
    });
    TaskManager.submit(player.getCurrentTask());
  }
}
