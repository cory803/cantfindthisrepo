package com.chaos.world.content.pos;

import com.chaos.GameServer;
import com.chaos.model.Item;
import com.chaos.net.mysql.SQLCallback;
import com.chaos.world.entity.impl.player.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Handles MYSQL Code for Searching POS Items
 * @Author Jonny
 */
public class GrabShopItems {

    /**
     * Starts the initial search for the item
     * @param player
     * @param itemSearch
     */
    public static Player grabShopItems(Player player, String owner) {
        GameServer.getServerPool().executeQuery(
        "SELECT * FROM `positems` WHERE `owner` LIKE '"+owner+"' LIMIT 40", new SQLCallback() {
            @Override
            public void queryComplete(ResultSet rs) throws SQLException {
                while (rs.next()) {
                    String owner = rs.getString("owner");
                    String caption = rs.getString("caption");
                    int itemId = rs.getInt("itemid");
                    int amount = rs.getInt("amount");
                    long price = rs.getLong("price");
                    player.getPlayerOwnedShops().getShopItems().addItem(new Item(itemId, amount));
                    player.getPlayerOwnedShops().getShopItems().getPrices().add(price);
                }
            }

            @Override
            public void queryError(SQLException e) {
                e.printStackTrace();
            }
        });
        return player;
    }

}
