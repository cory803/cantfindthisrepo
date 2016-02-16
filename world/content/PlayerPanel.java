package com.ikov.world.content;

import com.ikov.util.Misc;
import com.ikov.world.content.minigames.impl.Nomad;
import com.ikov.world.content.minigames.impl.RecipeForDisaster;
import com.ikov.world.content.skill.impl.slayer.SlayerTasks;
import com.ikov.world.entity.impl.player.Player;

public class PlayerPanel {

	public static void refreshPanel(Player player) {
		/**
		 * General info
		 */
		player.getPacketSender().sendString(39155, "Ikov2.org - Main World");
		player.getPacketSender().sendString(39159, "@whi@   - General Information");

		player.getPacketSender().sendString(39162, "@whi@   - Configurations");
		player.getPacketSender().sendString(39163, "@or2@Yell Channel: "+(player.yellToggle() ? "@gre@ON" : "@red@OFF")+"");
		player.getPacketSender().sendString(39164, "@or2@Game Music:  "+(player.musicActive() ? "@gre@ON" : "@red@OFF")+"");
		player.getPacketSender().sendString(39165, "@or2@Game Sounds:  "+(player.soundsActive() ? "@gre@ON" : "@red@OFF")+"");
		player.getPacketSender().sendString(39166, "@or2@Hide Familiars:  @red@OFF");
		player.getPacketSender().sendString(39167, "@or2@Exp Lock:  "+(player.experienceLocked() ? "@red@Locked" : "@gre@Unlocked")+"");
		
		/**
		 * Account info
		 */
		player.getPacketSender().sendString(39168, "@whi@ - Personal Statistics");
		player.getPacketSender().sendString(39169, "@or2@Open Kills Tracker");
		player.getPacketSender().sendString(39170, "@or2@Open Drop Log");
		//player.getPacketSender().sendString(39171, "Time Played - In PlayerProcess");
		player.getPacketSender().sendString(39172, "@or2@Claimed:  @yel@$"+player.getAmountDonated());
		player.getPacketSender().sendString(39179, "@or2@Bossing Points:  @yel@"+player.getBossPoints());
		player.getPointsHandler().refreshPanel();
		/**
		 * Points
		 */

		/**
		 * Slayer
		 */
		player.getPacketSender().sendString(39189, "@or3@ - @whi@ Slayer");
		player.getPacketSender().sendString(39190, "@or2@Master:  @yel@"+Misc.formatText(player.getSlayer().getSlayerMaster().toString().toLowerCase().replaceAll("_", " ")));
		if(player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK) 
			player.getPacketSender().sendString(39191, "@or2@Task:  @yel@"+Misc.formatText(player.getSlayer().getSlayerTask().toString().toLowerCase().replaceAll("_", " "))+"");
		else
			player.getPacketSender().sendString(39191, "@or2@Task:  @yel@"+Misc.formatText(player.getSlayer().getSlayerTask().toString().toLowerCase().replaceAll("_", " "))+"s");
		player.getPacketSender().sendString(39192, "@or2@Task Streak:  @yel@"+player.getSlayer().getTaskStreak()+"");
		player.getPacketSender().sendString(39193, "@or2@Task Amount:  @yel@"+player.getSlayer().getAmountToSlay()+"");
		if(player.getSlayer().getDuoPartner() != null)
			player.getPacketSender().sendString(39194, "@or2@Duo Partner:  @yel@"+player.getSlayer().getDuoPartner()+"");
		else
			player.getPacketSender().sendString(39194, "@or2@Duo Partner:");

		/**
		 * Quests
		 */
		player.getPacketSender().sendString(39198, "@or3@ - @whi@ Quests");
		player.getPacketSender().sendString(39199, RecipeForDisaster.getQuestTabPrefix(player) + "Recipe For Disaster");
		player.getPacketSender().sendString(39200, Nomad.getQuestTabPrefix(player) + "Nomad's Requeim");

		/**
		 * Links
		 */
		player.getPacketSender().sendString(39202, "@or3@ - @whi@ Links");
		player.getPacketSender().sendString(39203, "@or2@Forum");
		player.getPacketSender().sendString(39204, "@or2@Rules");
		player.getPacketSender().sendString(39205, "@or2@Store");
		player.getPacketSender().sendString(39206, "@or2@Vote");
		player.getPacketSender().sendString(39207, "@or2@Hiscores");
		player.getPacketSender().sendString(39208, "@or2@Report");
	}

}
