package com.chaos.world.content.pos;

import com.chaos.GameServer;
import com.chaos.model.input.impl.SearchByItemPOS;
import com.chaos.net.mysql.SQLCallback;
import com.chaos.world.entity.impl.player.Player;
import com.chaos.world.entity.impl.player.PlayerLoading;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles MYSQL Code for Searching POS Items
 * @Author Jonny
 */
public class SearchItemSql {

    /**
     * Starts the initial search for the item
     * @param player
     * @param itemSearch
     */
    public static void search(Player player, String itemSearch) {
        GameServer.getServerPool().executeQuery(
        "SELECT * FROM `positems` WHERE `itemname` LIKE '%"+itemSearch+"%' LIMIT 100", new SQLCallback() {
            @Override
            public void queryComplete(ResultSet rs) throws SQLException {
                int index = 0;
                while (rs.next()) {
                    String owner = rs.getString("owner");
                    String caption = rs.getString("caption");
                    int itemId = rs.getInt("itemid");
                    long price = rs.getLong("price");
                    outSearch(player, index, owner, caption, itemId, price);
                    index++;
                }
            }

            @Override
            public void queryError(SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Spits out the item that you have searched
     * @param player
     * @param index
     */
    public static void outSearch(Player player, int index, String owner, String caption, int itemId, long price) {
        player.getPacketSender().sendString(getFrame(41472, index), owner);
        player.getPacketSender().sendString(getFrame(41473, index), ":storeowner:-"+itemId+"-"+price+"-"+caption);
        player.getPlayerOwnedShops().addShop(owner, caption, itemId, price);
    }

    /**
     * Get the string frame for the index (1-100).
     * @param index
     * @return
     */
    public static int getFrame(int id, int index) {
        return id + (index * 4);
    }
}
