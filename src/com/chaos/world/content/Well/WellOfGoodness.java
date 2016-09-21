package com.chaos.world.content.Well;

import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.util.Misc;
import com.chaos.world.World;
import com.chaos.world.entity.impl.player.Player;
import org.scripts.kotlin.content.dialog.Well.LookDownWell;

import java.io.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by high105 on 9/16/2016.
 */
public class WellOfGoodness {

    private static final int LEAST_DONATE_AMOUNT_ACCEPTED = 1000000; // 1m

    private static final int[] AMOUNT_NEEDED = { 10000000, 25000000, 30000000 };
    private static int[] BONUSES_DURATION = { 120, 120, 120 }; // number in minutes

    private static CopyOnWriteArrayList<Player> DONATORS = new CopyOnWriteArrayList<Player>();

    private static long[] START_TIMER = { 0, 0,0 };
    private static int[] MONEY_IN_WELL = { 0, 0, 0 };
    private static boolean[] isFull = { false, false, false };

    public static void init() {
        String[] args;
        String line;
            try {
                BufferedReader reader = new BufferedReader(new FileReader("./data/saves/edgeville-well.txt"));
                while ((line = reader.readLine()) != null) {
                    if (line.contains("well-exp")) {
                        args = line.split(": ");
                        if (Long.parseLong(args[1]) > 0) {
                            isFull[0] = true;
                            START_TIMER[0] = Long.parseLong(args[1]);
                            MONEY_IN_WELL[0] = AMOUNT_NEEDED[0];
                        }
                    } else if (line.contains("well-drops")) {
                        args = line.split(": ");
                        if (Long.parseLong(args[1]) > 0) {
                            isFull[1] = true;
                            START_TIMER[1] = Long.parseLong(args[1]);
                            MONEY_IN_WELL[1] = AMOUNT_NEEDED[1];
                        }
                    } else if (line.contains("well-pkp")) {
                        args = line.split(": ");
                        if (Long.parseLong(args[1]) > 0) {
                            isFull[2] = true;
                            START_TIMER[0] = Long.parseLong(args[1]);
                            MONEY_IN_WELL[1] = AMOUNT_NEEDED[1];
                        }
                    }
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static void save() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("./data/saves/edgeville-well.txt"));
            out.write("well-exp: " + START_TIMER[0]);
            out.newLine();
            out.write("well-drops: " + START_TIMER[1]);
            out.newLine();
            out.write("well-pkp: " + START_TIMER[2]);
            out.newLine();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int getWell(String well) {
        if (well == "exp") {
            return 0;
        } else if (well == "drops") {
            return 1;
        } else if (well == "pkp") {
            return 2;
        } else {
            return 5;
        }
    }

    public static int getMissingAmount(String well) {
        if (well == "exp") {
            return (AMOUNT_NEEDED[getWell(well)] - MONEY_IN_WELL[getWell(well)]);
        } else if (well == "drops") {
            return (AMOUNT_NEEDED[getWell(well)] - MONEY_IN_WELL[getWell(well)]);
        } else if (well == "pkp") {
            return (AMOUNT_NEEDED[getWell(well)] - MONEY_IN_WELL[getWell(well)]);
        } else {
            return 0;
        }
    }

    public static void lookDownWell(Player player, String well) {
        if (checkFull(player, well)) {
            return;
        }
        player.getDialog().sendDialog(new LookDownWell(player, well));
    }

    public static boolean checkFull(Player player, String well) {
        if (well == "exp") {
            if (MONEY_IN_WELL[getWell(well)] == AMOUNT_NEEDED[getWell(well)]) {
                return isFull[getWell(well)] = true;
            }
            return isFull[getWell(well)] = false;
        } else if (well == "drops") {
            if (MONEY_IN_WELL[getWell(well)] == AMOUNT_NEEDED[getWell(well)]) {
                return isFull[getWell(well)] = true;
            }
            return isFull[getWell(well)] = false;
        } else if (well == "pkp") {
            if (MONEY_IN_WELL[getWell(well)] == AMOUNT_NEEDED[getWell(well)]) {
                return isFull[getWell(well)] = true;
            }
            return isFull[getWell(well)] = false;
        }
        return false;
    }

    public static void donate(Player player, int amount, String well) {
        if (checkFull(player, well)) {
            return;
        }
        if (amount < LEAST_DONATE_AMOUNT_ACCEPTED) {
            Dialog.createStatement(DialogHandler.CALM, "You must donate at least 1 million coins.");
            return;
        }
        if (amount > getMissingAmount(well)) {
            amount = getMissingAmount(well);
        }
        boolean usePouch = player.getMoneyInPouch() >= amount;
        if (!usePouch && player.getInventory().getAmount(995) < amount) {
            Dialog.createStatement(DialogHandler.CALM, "You do not have that much money in your inventory or money pouch.");
            return;
        }
        if (usePouch) {
            player.setMoneyInPouch(player.getMoneyInPouch() - amount);
            player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
        } else {
            player.getInventory().delete(995, amount);
        }
//        if (!DONATORS.contains(player)) {
//            DONATORS.add(player);
//        }
        MONEY_IN_WELL[getWell(well)] += amount;
        if (amount > 25000000) {
            switch (getWell(well)) {
                case 1:
                    World.sendMessage("<img=4> <col=6666FF>" + player.getUsername() + " has donated "
                            + Misc.insertCommasToNumber("" + amount + "") + " coins to the Well of Exp!");
                    break;
                case 2:
                    World.sendMessage("<img=4> <col=6666FF>" + player.getUsername() + " has donated "
                            + Misc.insertCommasToNumber("" + amount + "") + " coins to the Well of Wealth!");
                    break;
                case 3:
                    World.sendMessage("<img=4> <col=6666FF>" + player.getUsername() + " has donated "
                            + Misc.insertCommasToNumber("" + amount + "") + " coins to the Well of Execution!");
                    break;
                default:
                    System.out.println("Wtf..... donated to which well?!?");
            }
        }
        Dialog.createStatement(DialogHandler.CALM, "Thank you for your donation.");
        if (getMissingAmount(well) <= 0) {
            switch(getWell(well)) {
                case 1:
                    isFull[getWell(well)] = true;
                    START_TIMER[getWell(well)] = System.currentTimeMillis();
                    World.sendMessage("<img=4> <col=6666FF>The Well of Exp has been filled!");
                    World.sendMessage("<img=4> <col=6666FF>It is now granting everyone 2 hours of 30% bonus experience.");
                    break;
                case 2:
                    isFull[getWell(well)] = true;
                    START_TIMER[getWell(well)] = System.currentTimeMillis();
                    World.sendMessage("<img=4> <col=6666FF>The Well of Wealth has been filled!");
                    World.sendMessage("<img=4> <col=6666FF>It is now granting everyone 2 hours of bonus xp rates.");
                    break;
                case 3:
                    isFull[getWell(well)] = true;
                    START_TIMER[getWell(well)] = System.currentTimeMillis();
                    World.sendMessage("<img=4> <col=6666FF>The Well of Execution has been filled!");
                    World.sendMessage("<img=4> <col=6666FF>It is now granting everyone 2 hours of bonus pk points.");
                    break;
            }
        }
    }

    public static int getMinutesRemaining(String well) {
        return (BONUSES_DURATION[getWell(well)] - Misc.getMinutesPassed(System.currentTimeMillis() - START_TIMER[getWell(well)]));
    }

    public static void setDefaults(String well) {
        isFull[getWell(well)] = false;
        START_TIMER[getWell(well)] = 0;
        MONEY_IN_WELL[getWell(well)] = 0;
    }

    public static void updateState(String well) {
        if (isFull[getWell(well)]) {
            if (getMinutesRemaining(well) <= 0) {
                World.sendMessage("<img=4> <col=6666FF>The Well of Goodwill is no longer granting bonus experience.");
                World.getPlayers()
                        .forEach(p -> p.getPacketSender().sendString(39163, "@or2@Well of Goodwill: @yel@N/A"));
                setDefaults(well);
            }
        }
    }
}
