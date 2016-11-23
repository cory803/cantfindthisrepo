package com.chaos.world.content.pos;

import com.chaos.util.Misc;
import com.chaos.world.entity.impl.player.Player;
import org.scripts.kotlin.content.dialog.npcs.POSMerchant2;

/*
 * Created by High105 on 11/22/2016.
 */
public class PosFeaturedShops {

    private final static int SIZE = 10;
    private final static int TIMER = 120;

    public static boolean[] isEmpty = new boolean[SIZE];
    public static long timeRemaining[] = new long[SIZE];
    public static String shopOwner[] = new String[SIZE];

    private static int[] featuredShopNameIds = {41422, 41426, 41430, 41434, 41438,  41442, 41446, 41450, 41454, 41458};
    private static int[] featuredShopLastsIds = {41423, 41427, 41431, 41435, 41439, 41443, 41447, 41451, 41455, 41459};

    public static void buyShop(Player player, int x) {
        if (!isEmpty[x]) {
            for (int i = 0; i < SIZE; i++) {
                if (shopOwner[i] == player.getUsername()) {
                    player.getPacketSender().sendMessage("You already purchased a featured shop.");
                    return;
                }
            }
            player.setNpcClickId(2593);
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
                player.getPacketSender().sendString(featuredShopNameIds[j], "@whi@" + shopOwner[j]);
                player.getPacketSender().sendString(featuredShopLastsIds[j], "@whi@" + getMinutesRemaining(player, j) + " Min Left");
            }
        }
    }
    /**
     * When the timer runs out call here
     * @param x which slot 0-9 to reset
     **/
    public static void resetSlot(Player player, int x) {
        PosFeaturedShops.isEmpty[x] = false;
        PosFeaturedShops.timeRemaining[x] = 0;
        PosFeaturedShops.shopOwner[x] = null;
        resetInterface(player);
    }

    public static int getMinutesRemaining(Player player, int x) {
        int remain = (TIMER - Misc.getMinutesPassed(System.currentTimeMillis() - timeRemaining[x]));
        if (remain == 0) {
            resetSlot(player, x);
        }
        return remain;
    }
}
