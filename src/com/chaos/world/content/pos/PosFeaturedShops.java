package com.chaos.world.content.pos;

import com.chaos.world.entity.impl.player.Player;
import org.scripts.kotlin.content.dialog.npcs.POSMerchant2;

import java.util.Arrays;

/*
 * Created by High105 on 11/22/2016.
 */
public class PosFeaturedShops {

    final static int SIZE = 10;

    public static boolean[] isEmpty = new boolean[SIZE];
    public static int timeRemaining[] = new int[SIZE];
    public static String shopOwner[] = new String[SIZE];

    private static int[] featuredShopNameIds = {41422, 41426, 41430, 41434, 41438,  41442, 41446, 41450, 41454, 41458};
    private static int[] featuredShopLastsIds = {41423, 41427, 41431, 41435, 41439, 41443, 41447, 41451, 41455, 41459};
    private static int[] buttonIds = {-24115, -24111, -24107, -24103, -24099, -24095, -24091, -24087, -24083, -24079};

    public static void buyShop(Player player, int x) {
        if (!isEmpty[x]) {
            for (int i = 0; i < SIZE; i++) {
                if (shopOwner[i] == player.getUsername()) {
                    player.getPacketSender().sendMessage("You already purchased a featured shop.");
                    return;
                }
            }
            player.getDialog().sendDialog(new POSMerchant2(player, x));
        } else {
            PlayerOwnedShops.openShop(shopOwner[x], player);
        }
    }

    public static boolean handlePosButtons(Player player, int button) {
        switch (button) {
            case -24115:
                buyShop(player, 0);
                break;
            case -24111:
                buyShop(player, 1);
                break;
            case -24107:
                buyShop(player, 2);
                break;
            case -24103:
                buyShop(player, 3);
                break;
            case -24099:
                buyShop(player, 4);
                break;
            case -24095:
                buyShop(player, 5);
                break;
            case -24091:
                buyShop(player, 6);
                break;
            case -24087:
                buyShop(player, 7);
                break;
            case -24083:
                buyShop(player, 8);
                break;
            case -24079:
                buyShop(player, 9);
                break;
        }
        return true;
    }


    public static void resetInterface(Player player) {
        for (int j = 0; j < SIZE; j++) {
            if (!isEmpty[j]) {
                player.getPacketSender().sendString(featuredShopNameIds[j], "Rent Shop");
                player.getPacketSender().sendString(featuredShopLastsIds[j], "\t\t10m");
            } else {
                player.getPacketSender().sendString(featuredShopNameIds[j], ""+shopOwner[j]);
                player.getPacketSender().sendString(featuredShopLastsIds[j], "Lasts: "+timeRemaining[j]);
            }
        }
    }
}
