package com.runelive.world.content.tasks;

import com.runelive.model.definitions.ItemDefinition;
import com.runelive.util.Misc;
import com.runelive.world.entity.impl.player.Player;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Dave on 27/05/2016.
 */

public class DailyTaskManager {

        private static int getTodayDate() {
            Calendar cal = new GregorianCalendar();
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);
            return (month * 100 + day);
        }

        private static int taskCount = 2;

        private static String dailyTask(int i){
            switch(i){
                /**
                 * Skilling tasks
                 */
                case 0:
                    return "Catch 100 Fish from any fishing spot.";
                case 1:
                    return "Chop down 100 Logs from any tree.";
                case 2:
                    return "Steal 100 times from any thieving stall.";
                case 3:
                    return "Fletch 100 Short or Long bows.";
                case 4:
                    return "Fletch 1000 Bolts or Arrows.";
                case 5:
                    return "Add 1000 gem tips to Bolts.";
                case 6:
                    return "Successfully cook 100 Fish.";
                case 7:
                    return "Infuse 100 Summoning Pouches.";
                case 8:
                    return "Make 100 Summoning Scrolls.";
                case 9:
                    return "Exchange stardust.";
                case 10:
                    return "Brew 100 Potions.";
                /**
                 * Combat related tasks (easy)
                 */
                case 11:
                    return "Cast Low/High Alchemy 200 times.";
                case 12:
                    return "Complete 3 Slayer Tasks.";
                case 13:
                    return "Kill 5 General Graardor's";
                case 14:
                    return "Kill 5 Commander Zilyana's";
                case 15:
                    return "Kill 5 K'ril Tsutsaroth";
                case 16:
                    return "Kill 5 K'ree Arra's";
                case 22:
                    return "Kill 5 Tormented Demons";
                case 23:
                    return "Kill 5 Kalphite Queen";
                /**
                 * Combat related tasks (medium)
                 */
                case 17:
                    return "Complete 5 Slayer Tasks.";
                case 18:
                    return "Kill 10 General Graardor's";
                case 19:
                    return "Kill 10 Commander Zilyana's";
                case 20:
                    return "Kill 10 K'ril Tsutsaroth";
                case 21:
                    return "Kill 10 K'ree Arra's";
                case 24:
                    return "Kill 10 Tormented Demons";
                case 25:
                    return "Kill 10 Kalphite Queen";
                /**
                 * Combat related tasks (Hard)
                 */
                case 26:
                    return "Kill 5 of Nex.";
                case 27:
                    return "Complete 10 Slayer Tasks.";
                case 28:
                    return "Kill 15 General Graardor's";
                case 29:
                    return "Kill 15 Commander Zilyana's";
                case 30:
                    return "Kill 15 K'ril Tsutsaroth";
                case 31:
                    return "Kill 15 K'ree Arra's";
                case 32:
                    return "Kill 15 Tormented Demons";
                case 33:
                    return "Kill 15 Corporeal Beast.";
            }
            return "Error code ##INVALID TASK SET## out of bounds exception caught "+i+".";
        }

    public static void addCoinsToPouch(Player player) {
        long rewardCoins = Misc.inclusiveRandom(1_250_000, 2_500_000);
        player.setMoneyInPouch((player.getMoneyInPouch() + ((long) rewardCoins)));
        player.getPacketSender().sendMessage("<col=ff0000>"+ Misc.formatAmount((long)rewardCoins)+"</col> coins have been added to your money pouch.");
    }

    public static void handleTaskReward(Player p) {
        if(p.dailyTask == 0 && p.dailyTaskProgress >= 100 && !p.completedDailyTask) {
            p.completedDailyTask = true;
            p.dailyTaskProgress = 0;
            p.dailyTask = 0;
            addCoinsToPouch(p);
        }
    }

    public static void checkTaskProgress(Player p) {
        if(p.dailyTask == 0 && p.dailyTaskProgress >= 100) {
            handleTaskReward(p);
        } else if(p.dailyTask == 0 && p.dailyTaskProgress <= 99) {
            p.getPacketSender().sendMessage("You have caught "+p.dailyTaskProgress+" / 100 Fish.");
        }
    }

        public static void doTaskProgress(Player p) {
            if(!p.completedDailyTask) {
                p.dailyTaskProgress++;
                checkTaskProgress(p);
            }
        }

        public static void giveNewTask(Player p){
            if(p.dailyTaskDate == getTodayDate()){
                if(!p.completedDailyTask){
                    p.getPacketSender().sendMessage("Your daily task is: " +dailyTask(p.dailyTask));
                } else {
                    p.getPacketSender().sendMessage("You already finished your daily task, Come back tomorrow for a new one.");
                }
            } else {
                p.dailyTaskDate = getTodayDate();
                p.dailyTask = Misc.inclusiveRandom(0, 2);
                p.getPacketSender().sendMessage("Your task for today is: "+dailyTask(p.dailyTask));
            }
        }
}
