package com.runelive.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.runelive.GameServer;
import com.runelive.world.World;
import com.runelive.world.content.PlayerLogs;
import com.runelive.world.content.dialogue.DialogueManager;
import com.runelive.world.entity.impl.player.Player;
import com.runelive.net.mysql.ThreadedSQLCallback;
import com.runelive.model.definitions.ItemDefinition;

public class Store {

	public static void claimItem(Player player) {
		GameServer.getForumPool().executeQuery("Select * from `store_collection_box` WHERE `ign` = '" + player.getUsername() + "'", new ThreadedSQLCallback() {
			@Override
			public void queryComplete(ResultSet rs) throws SQLException {
				if(!rs.next()) {
					player.getPacketSender().sendMessage("You currently don't have anything in your collection box!");
				}
				while(rs.next()) {
					String[] item_ids = rs.getString("item_ids").split(",");
					String[] amounts = rs.getString("amounts").split(",");
					for(int i = 0; i < item_ids.length; i++) {
						if (!player.getInventory().hasRoomFor(Integer.parseInt(item_ids[i]), Integer.parseInt(amounts[i]))) {
							player.getBank(0).add(Integer.parseInt(item_ids[i]), Integer.parseInt(amounts[i]));
							player.getPacketSender().sendMessage("<col=ff0000>"+amounts[i]+"x "+ItemDefinition.forId(Integer.parseInt(item_ids[i])).name+" has been added to your bank.");
						} else {
							player.getInventory().add(Integer.parseInt(item_ids[i]), Integer.parseInt(amounts[i]));
							player.getPacketSender().sendMessage("<col=ff0000>"+amounts[i]+"x "+ItemDefinition.forId(Integer.parseInt(item_ids[i])).name+" has been added to your bank.");
						}
					}
				} 
				GameServer.getForumPool().executeQuery("DELETE FROM `store_collection_box` WHERE `ign` = '" + player.getUsername() + "'", new ThreadedSQLCallback() {
					@Override
					public void queryComplete(ResultSet rs) throws SQLException {
					
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

