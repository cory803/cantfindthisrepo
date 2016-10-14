package com.chaos.model;

import com.chaos.GameServer;
import com.chaos.net.mysql.SQLCallback;
import com.chaos.world.content.MemberScrolls;
import com.chaos.world.content.PlayerLogs;
import com.chaos.world.content.PlayerPanel;
import com.chaos.world.entity.impl.player.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DonatorBox {

    private final static int BOX10 = 5;
    private final static int BOX25 = 6;
    private final static int BOX100 = 7;

    public static boolean addItems(Player player, int itemID) {
        if (!player.getInventory().hasRoomFor(itemID, 1)) {
            player.getBank(0).add(itemID, 1);
            return false;
        } else {
            player.getInventory().add(itemID, 1);
            return true;
        }
    }

    public static void openBox(Player player, int itemID) {
        int claim = 0;
        switch (itemID) {
            case BOX10:
                claim = 10;
                if (!player.getInventory().hasRoomFor(20435, claim)) {
                    player.getInventory().add(itemID, claim);
                    player.incrementAmountDonated(claim);
                }
                break;
            case BOX25:
                claim = 25;
                if (!player.getInventory().hasRoomFor(20435, claim)) {
                    player.getInventory().add(itemID, claim);
                    player.incrementAmountDonated(claim);
                }
                break;
            case BOX100:
                claim = 100;
                if (!player.getInventory().hasRoomFor(20435, claim)) {
                    player.getInventory().add(itemID, claim);
                    player.incrementAmountDonated(claim);
                }
                break;
        }
    }
    public static void claimItem(Player player) {
        player.claimingStoreItems = true;
        GameServer.getForumPool().executeQuery(
                "SELECT * FROM donation WHERE username = " + player.getUsername().replaceAll(" ", "_"), new SQLCallback() {
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
                            int prod = Integer.parseInt(rs.getString("productid"));
                            int price = Integer.parseInt(rs.getString("price"));
                            if (prod == 1 && price == 3) {
                                hasGrabbed = addItems(player, BOX10);
                            } else if (prod == 2 && price == 6) {
                                hasGrabbed = addItems(player, BOX10);
                            } else if (prod == 3 && price == 9) {
                                hasGrabbed = addItems(player, BOX10);
                            }
                        }
//                        PlayerLogs.donations(player, forum_id, credits,
//                                new Item(Integer.parseInt(item_ids[i]), Integer.parseInt(amounts[i])));
                        MemberScrolls.checkForRankUpdate(player);
                        PlayerPanel.refreshPanel(player);
                        player.save();
                        if (!hasGrabbed) {
                            player.claimingStoreItems = false;
                            player.getPacketSender()
                                    .sendMessage("You currently don't have anything in your collection box!");
                            return;
                        }
                        GameServer.getForumPool().executeQuery(
                                "DELETE FROM `donation` WHERE `username` = '" + player.getUsername() + "'",
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
}
