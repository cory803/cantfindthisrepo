package com.runelive.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.runelive.GameServer;
import com.runelive.GameSettings;
import com.runelive.world.World;
import com.runelive.world.content.Achievements;
import com.runelive.world.content.PlayerLogs;
import com.runelive.world.content.dialogue.DialogueManager;
import com.runelive.world.entity.impl.player.Player;
import com.runelive.net.mysql.ThreadedSQLCallback;
import com.runelive.model.definitions.ItemDefinition;
import com.runelive.world.content.MemberScrolls;
import com.runelive.world.content.PlayerPanel;

public class Voting {

	public static void useAuth(Player player, String a) {
		final String auth = a.replaceAll("[\"\\\'/]", "");
		if(auth.length() > 10) {
			player.getPacketSender().sendMessage("Your auth code can only be under 10 characters.");
			return;
		}
		GameServer.getVotingPool().executeQuery("Select * from `auth` WHERE `auth` = '" + auth + "'", new ThreadedSQLCallback() {
			@Override
			public void queryComplete(ResultSet rs) throws SQLException {
				if(rs == null) {
					player.getPacketSender().sendMessage("You have entered an invalid auth code.");
					return;
				}
				boolean hasGrabbed = false;
				while(rs.next()) {
					hasGrabbed = true;
					if(GameSettings.DOUBLE_VOTE_TOKENS) {
						player.getInventory().add(10944,  2);
					} else if(GameSettings.TRIPLE_VOTE_TOKENS) {
						player.getInventory().add(10944, 3);
					} else {
						player.getInventory().add(10944, 1);
					}
					Achievements.doProgress(player, Achievements.AchievementData.VOTE_100_TIMES);
					player.getPacketSender().sendMessage("Your auth code has been claimed!");
					player.save();
				} 
				if(!hasGrabbed) {
					player.getPacketSender().sendMessage("You have entered an invalid auth code.");
					return;
				}
				GameServer.getVotingPool().executeQuery("DELETE FROM `auth` WHERE `auth` = '" + auth + "'", new ThreadedSQLCallback() {
					@Override
					public void queryComplete(ResultSet rs) throws SQLException {
						//Query complete
					}

					@Override
					public void queryError(SQLException e) {
						e.printStackTrace();
					}
				});	
			}

			@Override
			public void queryError(SQLException e) {
				e.printStackTrace();
			}
		});
	}
}