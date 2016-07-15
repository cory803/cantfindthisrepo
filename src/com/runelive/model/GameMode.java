package com.runelive.model;

import com.runelive.model.container.impl.Bank;
import com.runelive.world.content.Achievements.AchievementData;
import com.runelive.world.content.PlayerPanel;
import com.runelive.world.content.dialogue.DialogueManager;
import com.runelive.world.content.skill.impl.slayer.SlayerMaster;
import com.runelive.world.content.skill.impl.slayer.SlayerTasks;
import com.runelive.world.entity.impl.player.Player;

public enum GameMode {

	NORMAL, HARDCORE_IRONMAN, IRONMAN;

	public static void set(Player player, GameMode newMode, boolean death) {
		if (!death && !player.getClickDelay().elapsed(1000))
			return;
		player.getClickDelay().reset();
		player.getPacketSender().sendInterfaceRemoval();
		if (newMode != player.getGameMode() || death) {
			if (!player.newPlayer()) {
				player.getEquipment().resetItems().refreshItems();
				player.getInventory().resetItems().refreshItems();
				for (Bank b : player.getBanks()) {
					b.resetItems();
				}
				player.getSlayer().resetSlayerTask();
				player.getSlayer().setSlayerTask(SlayerTasks.NO_TASK).setAmountToSlay(0).setTaskStreak(0)
						.setSlayerMaster(SlayerMaster.VANNAKA);
				player.getSkillManager().newSkillManager();
				for (Skill skill : Skill.values()) {
					player.getSkillManager().updateSkill(skill);
				}
				for (AchievementData d : AchievementData.values()) {
					player.getAchievementAttributes().setCompletion(d.ordinal(), false);
				}
				player.getMinigameAttributes().getRecipeForDisasterAttributes().reset();
				player.getMinigameAttributes().getNomadAttributes().reset();
				player.getKillsTracker().clear();
				player.getDropLog().clear();
				player.getPointsHandler().reset();
				PlayerPanel.refreshPanel(player);
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				if (player.didReceiveStarter()) {
					if (newMode != GameMode.NORMAL) {
						player.getInventory().add(995, 10000).add(1153, 1).add(1115, 1).add(1067, 1).add(1323, 1)
								.add(1191, 1).add(841, 1).add(882, 50).add(1167, 1).add(1129, 1).add(1095, 1)
								.add(1063, 1).add(579, 1).add(577, 1).add(1011, 1).add(1379, 1).add(556, 50)
								.add(558, 50).add(557, 50).add(555, 50).add(1351, 1).add(1265, 1).add(1007, 1)
								.add(1061, 1).add(330, 100).add(16127, 1);
					} else {
						player.getInventory().add(995, 500000).add(1153, 1).add(1115, 1).add(1067, 1).add(1323, 1)
								.add(1191, 1).add(841, 1).add(882, 1000).add(1167, 1).add(1129, 1).add(1095, 1)
								.add(1063, 1).add(579, 1).add(577, 1).add(1011, 1).add(1379, 1).add(556, 1000)
								.add(558, 1000).add(557, 1000).add(555, 1000).add(1351, 1).add(1265, 1).add(1007, 1)
								.add(1061, 1).add(386, 100).add(16127, 1);
					}
				} else {
					player.getPacketSender().sendMessage("Your connection has received enough starting items.");
				}
			}
		}
		player.setGameMode(newMode);
		player.getPacketSender().sendIronmanMode(newMode.ordinal());
		if (!death) {
			player.getPacketSender().sendMessage("")
					.sendMessage(
							"You've set your gamemode to " + newMode.name().toLowerCase().replaceAll("_", " ") + ".")
					.sendMessage("If you wish to change it, please talk to the town crier in any town.");
		} else {
			player.getPacketSender().sendMessage("Your account progress has been reset.");
		}
		if (player.newPlayer()) {
			player.setPlayerLocked(true);
			DialogueManager.start(player, 216);
			player.setDialogueActionId(216);
		} else {
			player.setPlayerLocked(false);
		}
	}

	/**
	 * Gets the mode for a certain id.
	 * 
	 * @param id
	 *            The id (ordinal()) of the mode.
	 * @return gamemode.
	 */
	public static GameMode forId(int id) {
		for (GameMode rights : GameMode.values()) {
			if (rights.ordinal() == id) {
				return rights;
			}
		}
		return null;
	}

}
