package com.ikov.world.content;

import com.ikov.util.Misc;
import com.ikov.world.content.minigames.impl.ClawQuest;
import com.ikov.world.content.minigames.impl.Nomad;
import com.ikov.world.content.minigames.impl.RecipeForDisaster;
import com.ikov.world.content.skill.impl.slayer.SlayerTasks;
import com.ikov.world.entity.impl.player.Player;

public class PlayerPanel {

	public static void refreshPanel(Player player) {
		
		player.getPacketSender().sendString(55072, "---------------------------------");
		player.getPacketSender().sendString(55074, "---------------------------------");

		player.getPacketSender().sendString(55075, " ");
		player.getPacketSender().sendString(55076, "@whi@ ~ Statistics ~");
		
		
		
		
		player.getPacketSender().sendString(55077, "@or1@Yell channel: "+(player.yellToggle() ? "@gre@ON" : "@red@OFF")+"");
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
		player.getPacketSender().sendString(55078, "@red@Claimed:  @gre@$"+player.getAmountDonated());
		player.getPacketSender().sendString(39179, "@red@Bossing Points:  @gre@"+player.getBossPoints());
		player.getPointsHandler().refreshPanel();
		/**
		 * Points
		 */

		/**
		 * Slayer
		 */
		player.getPacketSender().sendString(55092, "@whi@ ~ Slayer ~");
		player.getPacketSender().sendString(55093, "@red@Master:  @gre@"+Misc.formatText(player.getSlayer().getSlayerMaster().toString().toLowerCase().replaceAll("_", " ")));
		if(player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK) 
			player.getPacketSender().sendString(55094, "@red@Task:  @gre@"+Misc.formatText(player.getSlayer().getSlayerTask().toString().toLowerCase().replaceAll("_", " "))+"");
		else
			player.getPacketSender().sendString(55094, "@red@Task:  @gre@"+Misc.formatText(player.getSlayer().getSlayerTask().toString().toLowerCase().replaceAll("_", " "))+"s");
		player.getPacketSender().sendString(55095, "@red@Task Streak:  @gre@"+player.getSlayer().getTaskStreak()+"");
		player.getPacketSender().sendString(55096, "@red@Task Amount:  @gre@"+player.getSlayer().getAmountToSlay()+"");
		if(player.getSlayer().getDuoPartner() != null)
			player.getPacketSender().sendString(55097, "@red@Duo Partner:  @gre@"+player.getSlayer().getDuoPartner()+"");
		else
			player.getPacketSender().sendString(55097, "@red@Duo Partner: @gre@None");

		/**
		 * Quests
		 */
		player.getPacketSender().sendString(55202, "Quest Points: " + player.getQuestPoints());
		player.getPacketSender().sendString(55207, RecipeForDisaster.getQuestTabPrefix(player) + "Recipe For Disaster");
		player.getPacketSender().sendString(55208, Nomad.getQuestTabPrefix(player) + "Nomad's Requeim");
		player.getPacketSender().sendString(55209, ClawQuest.getQuestTabPrefix(player) + "The King's Task");

		/**
		 * Links
		 */
		player.getPacketSender().sendString(55099, "@whi@ ~ Links ~");
		player.getPacketSender().sendString(55100, "@gre@Website");
		player.getPacketSender().sendString(55101, "@gre@Forum");
		player.getPacketSender().sendString(55102, "@gre@Vote");
		player.getPacketSender().sendString(55103, "@gre@Store");
		player.getPacketSender().sendString(55104, "@gre@Apply");
		player.getPacketSender().sendString(55105, "@gre@Wiki");
		player.getPacketSender().sendString(55106, "@gre@Support");
		
		player.getPacketSender().sendString(55108, "@whi@~ Configurations ~");
		player.getPacketSender().sendString(55109, "@or1@Yell channel: "+(player.yellToggle() ? "@gre@ON" : "@red@OFF")+"");
		player.getPacketSender().sendString(55110, "@or1@Game Music:  "+(player.musicActive() ? "@gre@ON" : "@red@OFF")+"");
		player.getPacketSender().sendString(55111, "@or1@Game Sounds:  "+(player.soundsActive() ? "@gre@ON" : "@red@OFF")+"");
		player.getPacketSender().sendString(55112, "@or1@Hide Familiars:  @red@OFF");
		player.getPacketSender().sendString(55113, "@or1@Exp Lock:  "+(player.experienceLocked() ? "@red@Locked" : "@gre@Unlocked")+"");
		
		player.getPacketSender().sendString(55115, "@whi@~ Other ~");
		player.getPacketSender().sendString(55119, "@red@Open Drop Log");
		
		
		
		
	}

}
