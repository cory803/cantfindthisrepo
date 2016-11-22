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
                if (rs.next()) {
                    String owner = rs.getString("owner");
                    int itemId = rs.getInt("itemid");
                    long price = rs.getLong("price");
                    outSearch(player, index, owner, itemId, price);
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
    public static void outSearch(Player player, int index, String owner, int itemId, long price) {
        player.getPlayerOwnedShops().clearSearch();
        System.out.println("index: "+index+"");
        System.out.println("owner: "+owner+"");
        System.out.println("itemId: "+itemId+"");
        System.out.println("price: "+price+"");
        player.getPacketSender().sendString(getFrame(index), ":storeowner:-"+itemId+"-"+price+"-the caption");
    }

    /**
     * Get the string frame for the index (1-100).
     * @param index
     * @return
     */
    public static int getFrame(int index) {
        return 41473 + (index * 4);
    }
}
