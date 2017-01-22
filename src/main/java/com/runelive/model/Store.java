package com.runelive.model;

import com.runelive.GameServer;
import com.runelive.model.definitions.ItemDefinition;
import com.runelive.net.mysql.SQLCallback;
import com.runelive.net.serverlogs.ServerLogs;
import com.runelive.net.serverlogs.impl.Donation;
import com.runelive.world.content.PlayerPanel;
import com.runelive.world.content.Scrolls;
import com.runelive.world.entity.impl.player.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Store {

	public static void claimItem(Player player) {
		player.claimingStoreItems = true;
		GameServer.getStorePool().executeQuery(
				"Select * from `store_collection_box` WHERE `ign` = '" + player.getUsername() + "'", new SQLCallback() {
					@Override
					public void queryComplete(ResultSet rs) throws SQLException {
						if (rs == null) {
							player.getPacketSender()
									.sendMessage("You currently don't have anything in your collection box!");
							return;
						}
						boolean hasGrabbed = false;
						while (rs.next()) {
							hasGrabbed = true;
							String[] item_ids = rs.getString("item_ids").split(",");
							String[] amounts = rs.getString("amounts").split(",");
							int credits = rs.getInt("credits");
							int forum_id = rs.getInt("forum_id");
							boolean alreadyGotAmountClaimed = false;
							for (int i = 0; i < item_ids.length; i++) {
								if (Integer.parseInt(item_ids[i]) == 7629) {
									credits -= 100;
								} else if (Integer.parseInt(item_ids[i]) == 10934) {
									credits -= 25;
								} else if (Integer.parseInt(item_ids[i]) == 10935) {
									credits -= 50;
								} else if (Integer.parseInt(item_ids[i]) == 10943) {
									credits -= 10;
								}
							}
							for (int i = 0; i < item_ids.length; i++) {
								if (!player.getInventory().hasRoomFor(Integer.parseInt(item_ids[i]),
										Integer.parseInt(amounts[i]))) {
									player.getBank(0).add(Integer.parseInt(item_ids[i]), Integer.parseInt(amounts[i]));
									player.getPacketSender()
											.sendMessage("<col=ff0000>" + amounts[i] + "x "
													+ ItemDefinition.forId(Integer.parseInt(item_ids[i])).name
													+ " has been added to your bank.");
								} else {
									player.getInventory().add(Integer.parseInt(item_ids[i]),
											Integer.parseInt(amounts[i]));
									player.getPacketSender()
											.sendMessage("<col=ff0000>" + amounts[i] + "x "
													+ ItemDefinition.forId(Integer.parseInt(item_ids[i])).name
													+ " has been added to your inventory.");
								}
								ServerLogs.submit(new Donation(player, new Item(Integer.parseInt(item_ids[i]), Integer.parseInt(amounts[i]))));
								int itemId = Integer.parseInt(item_ids[i]);
								if (!alreadyGotAmountClaimed) {
									if (itemId != 7629 && itemId != 10934 && itemId != 10935 && itemId != 10943) {
										player.incrementAmountDonated(credits);
										alreadyGotAmountClaimed = true;
									}
								}
							}
							Scrolls.updateRank(player);
							PlayerPanel.refreshPanel(player);
							player.save();
						}
						if (!hasGrabbed) {
							player.claimingStoreItems = false;
							player.getPacketSender()
									.sendMessage("You currently don't have anything in your collection box!");
							return;
						}
						GameServer.getStorePool().executeQuery(
								"DELETE FROM `store_collection_box` WHERE `ign` = '" + player.getUsername() + "'",
								new SQLCallback() {
									@Override
									public void queryComplete(ResultSet rs) throws SQLException {
										player.claimingStoreItems = false;
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
		GameServer.getStorePool().executeQuery(
				"UPDATE `members` SET credits = credits + " + amount + " WHERE `name` = '" + name + "' LIMIT 1",
				new SQLCallback() {
					@Override
					public void queryComplete(ResultSet rs) throws SQLException {
						if (rs.next()) {

						}
					}

					@Override
					public void queryError(SQLException e) {
						e.printStackTrace();
					}
				});
	}

}
