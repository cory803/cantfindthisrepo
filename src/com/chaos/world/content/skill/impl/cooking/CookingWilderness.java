package com.chaos.world.content.skill.impl.cooking;

import com.chaos.engine.task.Task;
import com.chaos.engine.task.TaskManager;
import com.chaos.model.Animation;
import com.chaos.model.Skill;
import com.chaos.model.definitions.ItemDefinition;
import com.chaos.model.input.impl.EnterAmountToCook;
import com.chaos.world.content.Achievements;
import com.chaos.world.content.Achievements.AchievementData;
import com.chaos.world.entity.impl.player.Player;

public class CookingWilderness {

	public static void selectionInterface(Player player, CookingWildernessData CookingWildernessData) {
		if (CookingWildernessData == null)
			return;
		player.setSelectedSkillingItem(CookingWildernessData.getRawItem());
		player.setInputHandling(new EnterAmountToCook());
		player.getPacketSender().sendString(2799, ItemDefinition.forId(CookingWildernessData.getCookedItem()).getName())
				.sendInterfaceModel(1746, CookingWildernessData.getCookedItem(), 150).sendChatboxInterface(4429);
		player.getPacketSender().sendString(2800, "How many would you like to cook?");
	}

	public static void cook(final Player player, final int rawFish, final int amount) {
		final CookingWildernessData fish = CookingWildernessData.forFish(rawFish);
		if (fish == null)
			return;
		player.getSkillManager().stopSkilling();
		player.getPacketSender().sendInterfaceRemoval();
		if (!CookingWildernessData.canCook(player, rawFish))
			return;
		player.performAnimation(new Animation(896));
		player.setCurrentTask(new Task(2, player, false) {
			int amountCooked = 0;

			@Override
			public void execute() {
				if (!CookingWildernessData.canCook(player, rawFish)) {
					stop();
					return;
				}
				player.performAnimation(new Animation(896));
				if (ItemDefinition.forId(rawFish + 1).isNoted()) {
					player.getInventory().delete(rawFish + 1, 1);
				} else {
					player.getInventory().delete(rawFish, 1);
				}
				if (!CookingWildernessData.success(player, 3, fish.getLevelReq() - 2, fish.getStopBurn())) {
					if (ItemDefinition.forId(fish.getBurntItem() + 1).isNoted()) {
						player.getInventory().add(fish.getBurntItem() + 1, 1);
					} else {
						player.getInventory().add(fish.getBurntItem(), 1);
					}
					player.getPacketSender().sendMessage("You accidently burn the " + fish.getName() + ".");
				} else {
					if (ItemDefinition.forId(fish.getCookedItem() + 1).isNoted()) {
						player.getInventory().add(fish.getCookedItem() + 1, 1);
					} else {
						player.getInventory().add(fish.getCookedItem(), 1);
					}
					player.getSkillManager().addSkillExperience(Skill.COOKING, fish.getXp() + 1000);
					if (fish == CookingWildernessData.SALMON) {
						Achievements.finishAchievement(player, AchievementData.COOK_A_SALMON);
					} else if (fish == CookingWildernessData.ROCKTAIL) {
						Achievements.doProgress(player, AchievementData.COOK_25_ROCKTAILS);
						Achievements.doProgress(player, AchievementData.COOK_1000_ROCKTAILS);
					}
				}
				amountCooked++;
				if (amountCooked >= amount)
					stop();
			}

			@Override
			public void stop() {
				setEventRunning(false);
				player.setSelectedSkillingItem(-1);
				player.performAnimation(new Animation(65535));
			}
		});
		TaskManager.submit(player.getCurrentTask());
	}
}
