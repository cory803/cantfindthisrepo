package com.chaos.world.content;

import com.chaos.model.Flag;
import com.chaos.util.Misc;
import com.chaos.world.entity.impl.player.Player;
import com.chaos.world.content.Achievements.AchievementData;

public class Titles {

	public enum TitleData {

		PKER("@dre@", AchievementData.DEFEAT_30_PLAYERS, 30, 4587),
		GODLIKE("@god@", AchievementData.BURY_500_FROST_DRAGON_BONES, 500, 18830),
		CHEF("@or3@", AchievementData.COOK_500_MANTA, 500, 391),
		TERMINATOR("@ter@", AchievementData.DEFEAT_500_BOSSES, 500, 11694),
		FISHERMAN("@fro@", AchievementData.FISH_1500_ROCKTAILS, 1500, 309),
		LUMBERJACK("@lum@", AchievementData.CHOP_2500_MAGIC_LOGS, 2500, 6739);

		TitleData(String color, AchievementData data, int amount, int interfaceItem) {
			this.color = color;
			this.achievement = data;
			this.amount = amount;
			this.interfaceItem = interfaceItem;
		}

		private AchievementData achievement;
		private int amount;
		private int interfaceItem;
		private String color;

		public int getAmount() {
			return amount;
		}

		public AchievementData getAchievement() {
			return achievement;
		}

		/**
		 * Returns the formatted name of the slayer task.
		 * @return
		 */
		public String getName() {
			return Misc.formatText(this.toString().toLowerCase().replace("_", " "));
		}

		public int getInterfaceItem() {
			return interfaceItem;
		}

		public String getColor() {
			return color;
		}
	}

	/**
	 * Handle a button for a title
	 * @param player
	 * @param buttonId
	 */
	public static boolean handleButtons(Player player, int buttonId) {
		int value = 0;
		for (TitleData titleData : TitleData.values()) {
			if(buttonId == -20126 + value) {
				boolean completed = player.getAchievementAttributes().getCompletion()[titleData.getAchievement().ordinal()];
				if(!completed) {
					player.getPacketSender().sendMessage("You have not unlocked this title yet.");
					return true;
				}
				player.setTitle(titleData.getColor() + titleData.getName());
				player.getPacketSender().sendMessage("<col=ff0000>You have activated the title "+titleData.getName()+".");
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				return true;
			}
			value += 9;
		}
		return false;
	}

	/**
	 * Open the interface and write the Strings
	 * @param player
	 */
	public static void openInterface(Player player) {
		int value = 0;
		for (TitleData titleData : TitleData.values()) {
			boolean completed = player.getAchievementAttributes().getCompletion()[titleData.getAchievement().ordinal()];
			boolean progress = titleData.getAchievement().progressData != null
					&& player.getAchievementAttributes().getProgress()[titleData.getAchievement().progressData[0]] > 0;
			if(completed) {
				player.getPacketSender().sendString(45406 + value, titleData.getColor() +titleData.getName());
				player.getPacketSender().sendString(45413 + value, "@gre@Activate");
				player.getPacketSender().sendString(45408 + value, "@gre@"+titleData.getAchievement().getName());
				if(titleData.getAchievement().progressData != null) {
					player.getPacketSender().sendString(45409 + value, "@gre@("+titleData.getAmount()+"/"+titleData.getAmount()+")");
				}
			} else if(progress) {
				player.getPacketSender().sendString(45406 + value, "@yel@"+titleData.getName());
				player.getPacketSender().sendString(45413 + value, "@yel@Locked");
				player.getPacketSender().sendString(45408 + value, "@yel@"+titleData.getAchievement().getName());
				if(titleData.getAchievement().progressData != null) {
					player.getPacketSender().sendString(45409 + value, "@yel@("+player.getAchievementAttributes().getProgress()[titleData.getAchievement().progressData[0]]+"/"+titleData.getAmount()+")");
				}
			} else {
				player.getPacketSender().sendString(45406 + value, "@red@"+titleData.getName());
				player.getPacketSender().sendString(45413 + value, "@red@Locked");
				player.getPacketSender().sendString(45408 + value, "@red@" + titleData.getAchievement().getName());
				if(titleData.getAchievement().progressData != null) {
					player.getPacketSender().sendString(45409 + value, "@red@(0/"+titleData.getAmount()+")");
				}
			}
			player.getPacketSender().sendItemOnInterface(45414 + value, titleData.getInterfaceItem(), 0, 1);
			value+= 9;
		}
	}
}
