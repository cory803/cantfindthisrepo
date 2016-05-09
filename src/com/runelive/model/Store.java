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
import com.runelive.world.content.MemberScrolls;
import com.runelive.world.content.PlayerPanel;

public class Store {

	public static void claimItem(Player player) {
		GameServer.getForumPool().executeQuery("Select * from `store_collection_box` WHERE `ign` = '" + player.getUsername() + "'", new ThreadedSQLCallback() {
			@Override
			public void queryComplete(ResultSet rs) throws SQLException {
				if(rs == null) {
					player.getPacketSender().sendMessage("You currently don't have anything in your collection box!");
					return;
				}
				boolean hasGrabbed = false;
				while(rs.next()) {
					hasGrabbed = true;
					String[] item_ids = rs.getString("item_ids").split(",");
					String[] amounts = rs.getString("amounts").split(",");
					int credits = rs.getInt("credits");
					for(int i = 0; i < item_ids.length; i++) {
						if (!player.getInventory().hasRoomFor(Integer.parseInt(item_ids[i]), Integer.parseInt(amounts[i]))) {
							player.getBank(0).add(Integer.parseInt(item_ids[i]), Integer.parseInt(amounts[i]));
							player.getPacketSender().sendMessage("<col=ff0000>"+amounts[i]+"x "+ItemDefinition.forId(Integer.parseInt(item_ids[i])).name+" has been added to your bank.");
						} else {
							player.getInventory().add(Integer.parseInt(item_ids[i]), Integer.parseInt(amounts[i]));
							player.getPacketSender().sendMessage("<col=ff0000>"+amounts[i]+"x "+ItemDefinition.forId(Integer.parseInt(item_ids[i])).name+" has been added to your inventory.");
						}
						if(item_ids[i] != 7629 && item_ids[i] != 10934 && item_ids[i] != 10935 && item_ids[i] != 10943)
							player.incrementAmountDonated(credits);
					}
					MemberScrolls.checkForRankUpdate(player);
					PlayerPanel.refreshPanel(player);
					player.save();
				} 
				if(!hasGrabbed) {
					player.getPacketSender().sendMessage("You currently don't have anything in your collection box!");
					return;
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
	
	public static void addTokens(String name, int amount) {
		GameServer.getForumPool().executeQuery("UPDATE `members` SET credits = credits + "+amount+" WHERE `name` = '"+name+"' LIMIT 1", new ThreadedSQLCallback() {
			@Override
			public void queryComplete(ResultSet rs) throws SQLException {
				if(rs.next()) {
					
				}
			}

			@Override
			public void queryError(SQLException e) {
				e.printStackTrace();
			}
		});
	}	
	
	public static void addTokensFromScroll(Player player, String name, int amount, int item) {
		GameServer.getForumPool().executeQuery("SELECT `name` FROM `members` WHERE `name` = '"+name+"' LIMIT 1", new ThreadedSQLCallback() {
			@Override
			public void queryComplete(ResultSet rs) throws SQLException {
				boolean went = false;
				while(rs.next()) {
					went = true;
					GameServer.getForumPool().executeQuery("UPDATE `members` SET credits = credits + "+amount+" WHERE `name` = '"+name+"' LIMIT 1", new ThreadedSQLCallback() {
						@Override
						public void queryComplete(ResultSet rs) throws SQLException {
							switch(item) {
								case 10943:
									player.getInventory().delete(item, 1);
									player.incrementAmountDonated(10);
									player.getPacketSender().sendMessage("Your account has gained funds worth $10. Your total is now at $" + player.getAmountDonated() + ".");
									MemberScrolls.checkForRankUpdate(player);
									player.getPacketSender().sendMessage("The forum account "+name+" has gained 10 tokens.");
									PlayerPanel.refreshPanel(player);
									player.save();
									break;
								case 10934:
									player.getInventory().delete(item, 1);
									player.incrementAmountDonated(25);
									player.getPacketSender().sendMessage("Your account has gained funds worth $25. Your total is now at $" + player.getAmountDonated() + ".");
									MemberScrolls.checkForRankUpdate(player);
									player.getPacketSender().sendMessage("The forum account "+name+" has gained 25 tokens.");
									PlayerPanel.refreshPanel(player);
									player.save();
									break;
								case 10935:
									player.getInventory().delete(item, 1);
									player.incrementAmountDonated(50);
									player.getPacketSender().sendMessage("Your account has gained funds worth $50. Your total is now at $" + player.getAmountDonated() + ".");
									MemberScrolls.checkForRankUpdate(player);
									player.getPacketSender().sendMessage("The forum account "+name+" has gained 50 tokens.");
									PlayerPanel.refreshPanel(player);
									player.save();
									break;
								case 7629:
									player.getInventory().delete(item, 1);
									player.incrementAmountDonated(125);
									player.getPacketSender().sendMessage("Your account has gained funds worth $125. Your total is now at $" + player.getAmountDonated() + ".");
									MemberScrolls.checkForRankUpdate(player);
									player.getPacketSender().sendMessage("The forum account "+name+" has gained 125 tokens.");
									PlayerPanel.refreshPanel(player);
									player.save();
									break;
							}
						}

						@Override
						public void queryError(SQLException e) {
							e.printStackTrace();
						}
					});	
				}
				if(!went) {
					player.getPacketSender().sendMessage("A forum account has not been found with this name, use ::register to create it!");
				}
			}

			@Override
			public void queryError(SQLException e) {
				e.printStackTrace();
			}
		});
	}	

}

