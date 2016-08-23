package com.chaos.world.content;

import com.chaos.util.Misc;
import com.chaos.world.content.minigames.impl.ClawQuest;
import com.chaos.world.content.minigames.impl.FarmingQuest;
import com.chaos.world.content.minigames.impl.Nomad;
import com.chaos.world.content.minigames.impl.RecipeForDisaster;
import com.chaos.world.content.skill.impl.slayer.SlayerTasks;
import com.chaos.world.entity.impl.player.Player;

public class PlayerPanel {

	public static void refreshPanel(Player player) {
		Scoreboard.update(player, 1);
		Scoreboard.update(player, 2);
		Scoreboard.update(player, 4);
		player.getPacketSender().sendString(55072, "---------------------------------");
		player.getPacketSender().sendString(55074, "---------------------------------");

		player.getPacketSender().sendString(55075, "@whi@ ~ Statistics ~");

		// player.getPacketSender().sendString(55077, "@or1@Yell channel:
		// "+(player.yellToggle() ?
		// "@gre@ON" : "@red@OFF")+"");
		// player.getPacketSender().sendString(39164, "@or2@Game Music:
		// "+(player.musicActive() ?
		// "@gre@ON" : "@red@OFF")+"");
		// player.getPacketSender().sendString(39165, "@or2@Game Sounds:
		// "+(player.soundsActive() ?
		// "@gre@ON" : "@red@OFF")+"");
		// player.getPacketSender().sendString(39166, "@or2@Hide Familiars:
		// @red@OFF");
		// player.getPacketSender().sendString(39167, "@or2@Exp Lock:
		// "+(player.experienceLocked() ?
		// "@red@Locked" : "@gre@Unlocked")+"");

		/**
		 * Account info
		 */
		player.getPacketSender().sendString(39168, "@whi@ - Personal Statistics");
		player.getPacketSender().sendString(39169, "@or2@Open Kills Tracker");
		player.getPacketSender().sendString(39170, "@or2@Open Drop Log");
		// player.getPacketSender().sendString(39171, "Time Played - In
		// PlayerProcess");
		player.getPacketSender().sendString(55077, "@red@Claimed:  @gre@$" + player.getAmountDonated());
		player.getPacketSender().sendString(55078, "@red@Game Mode:  @gre@" + player.getGameModeAssistant().getModeName());
		player.getPointsHandler().refreshPanel();
		/**
		 * Points
		 */

		/**
		 * Slayer
		 */
		player.getPacketSender().sendString(55093, "@whi@ ~ Slayer ~");
		player.getPacketSender().sendString(55094, "@red@Master:  @gre@"
				+ Misc.formatText(player.getSlayer().getSlayerMaster().toString().toLowerCase().replaceAll("_", " ")));
		if (player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK)
			player.getPacketSender().sendString(55095,
					"@red@Task:  @gre@"
							+ Misc.formatText(
									player.getSlayer().getSlayerTask().toString().toLowerCase().replaceAll("_", " "))
							+ "");
		else
			player.getPacketSender().sendString(55095,
					"@red@Task:  @gre@"
							+ Misc.formatText(
									player.getSlayer().getSlayerTask().toString().toLowerCase().replaceAll("_", " "))
							+ "s");
		player.getPacketSender().sendString(55096,
				"@red@Task Streak:  @gre@" + player.getSlayer().getTaskStreak() + "");
		player.getPacketSender().sendString(55097,
				"@red@Task Amount:  @gre@" + player.getSlayer().getAmountToSlay() + "");
		if (player.getSlayer().getDuoPartner() != null)
			player.getPacketSender().sendString(55098,
					"@red@Duo Partner:  @gre@" + player.getSlayer().getDuoPartner() + "");
		else
			player.getPacketSender().sendString(55098, "@red@Duo Partner: @gre@None");

		/**
		 * Quests
		 */
		player.getPacketSender().sendString(55202, "Quest Points: " + player.getQuestPoints());
		player.getPacketSender().sendString(55207, RecipeForDisaster.getQuestTabPrefix(player) + "Recipe For Disaster");
		player.getPacketSender().sendString(55208, Nomad.getQuestTabPrefix(player) + "Nomad's Requeim");
		player.getPacketSender().sendString(55209, ClawQuest.getQuestTabPrefix(player) + "The King's Task");
		player.getPacketSender().sendString(55210, FarmingQuest.getQuestTabPrefix(player) + "Farmer's Expedition");

		/**
		 * Links
		 */
		player.getPacketSender().sendString(55100, "@whi@ ~ Links ~");
		player.getPacketSender().sendString(55101, "@gre@Website");
		player.getPacketSender().sendString(55102, "@gre@Forum");
		player.getPacketSender().sendString(55103, "@gre@Vote");
		player.getPacketSender().sendString(55104, "@gre@Store");
		player.getPacketSender().sendString(55105, "@gre@Apply");
		player.getPacketSender().sendString(55106, "@gre@Wiki");
		player.getPacketSender().sendString(55107, "@gre@Support");

		player.getPacketSender().sendString(55109, "@whi@~ Configurations ~");
		player.getPacketSender().sendString(55110,
				"@or1@Yell channel: " + (player.yellToggle() ? "@gre@ON" : "@red@OFF") + "");
		player.getPacketSender().sendString(55111,
				"@or1@Game Music:  " + (player.musicActive() ? "@gre@ON" : "@red@OFF") + "");
		player.getPacketSender().sendString(55112,
				"@or1@Game Sounds:  " + (player.soundsActive() ? "@gre@ON" : "@red@OFF") + "");
		player.getPacketSender().sendString(55113,
				"@or1@Display Tourney:  " + (player.tourneyToggle() ? "@gre@ON" : "@red@OFF") + "");
		player.getPacketSender().sendString(55114,
				"@or1@Exp Lock:  " + (player.experienceLocked() ? "@red@Locked" : "@gre@Unlocked") + "");

		player.getPacketSender().sendString(55115,
				"@or1@Login IP's:  " + (player.showIpAddress() ? "@gre@Enabled" : "@red@Disabled") + "");
		player.getPacketSender().sendString(55116,
				"@or1@Show home loc:  " + (player.showHomeOnLogin() ? "@gre@Enabled" : "@red@Disabled") + "");

		player.getPacketSender().sendString(55117, "@whi@~ Other ~");
		player.getPacketSender().sendString(55121, "@red@Open Drop Log");

	}

}
