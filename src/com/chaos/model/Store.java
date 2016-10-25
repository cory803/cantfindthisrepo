package com.chaos.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.chaos.GameServer;
import com.chaos.model.definitions.ItemDefinition;
import com.chaos.net.mysql.SQLCallback;
import com.chaos.world.content.Scrolls;
import com.chaos.world.content.PlayerLogs;
import com.chaos.world.content.PlayerPanel;
import com.chaos.world.entity.impl.player.Player;

public class Store {

	public static void claimItem(Player player) {
		player.claimingStoreItems = true;
		GameServer.getWebsitePool().executeQuery(
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
								PlayerLogs.donations(player, forum_id, credits,
										new Item(Integer.parseInt(item_ids[i]), Integer.parseInt(amounts[i])));
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
						GameServer.getWebsitePool().executeQuery(
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
		GameServer.getWebsitePool().executeQuery(
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

	public static void addTokensFromScroll(Player player, String name, int amount, int item) {
		GameServer.getWebsitePool().executeQuery("SELECT `name` FROM `members` WHERE `name` = '" + name + "' LIMIT 1",
				new SQLCallback() {
					@Override
					public void queryComplete(ResultSet rs) throws SQLException {
						boolean went = false;
						while (rs.next()) {
							went = true;
							if (!player.getInventory().contains(item)) {
								player.getPacketSender().sendMessage("What happened to your scroll?");
							} else {
								player.getInventory().delete(item, 1);
								GameServer.getWebsitePool().executeQuery("UPDATE `members` SET credits = credits + "
										+ amount + " WHERE `name` = '" + name + "' LIMIT 1", new SQLCallback() {
											@Override
											public void queryComplete(ResultSet rs) throws SQLException {
												switch (item) {
												case 10943:
													player.getPacketSender().sendMessage(
															"Please note, once you use your tokens on the site, you will get donator ranks!");
													player.getPacketSender().sendMessage(
															"The forum account " + name + " has gained 10 tokens.");
													PlayerPanel.refreshPanel(player);
													PlayerLogs.scrolls(player, 10, name);
													player.save();
													break;
												case 10934:
													player.getPacketSender().sendMessage(
															"Please note, once you use your tokens on the site, you will get donator ranks!");
													player.getPacketSender().sendMessage(
															"The forum account " + name + " has gained 25 tokens.");
													PlayerPanel.refreshPanel(player);
													PlayerLogs.scrolls(player, 25, name);
													player.save();
													break;
												case 10935:
													player.getPacketSender().sendMessage(
															"Please note, once you use your tokens on the site, you will get donator ranks!");
													player.getPacketSender().sendMessage(
															"The forum account " + name + " has gained 50 tokens.");
													PlayerPanel.refreshPanel(player);
													PlayerLogs.scrolls(player, 50, name);
													player.save();
													break;
												case 7629:
													player.getPacketSender().sendMessage(
															"Please note, once you use your tokens on the site, you will get donator ranks!");
													player.getPacketSender().sendMessage(
															"The forum account " + name + " has gained 125 tokens.");
													PlayerPanel.refreshPanel(player);
													PlayerLogs.scrolls(player, 125, name);
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
						}
						if (!went) {
							player.getPacketSender().sendMessage(
									"A forum account has not been found with this name, use ::register to create it!");
						}
					}

					@Override
					public void queryError(SQLException e) {
						e.printStackTrace();
					}
				});
	}

}
