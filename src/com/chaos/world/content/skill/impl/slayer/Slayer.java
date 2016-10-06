package com.chaos.world.content.skill.impl.slayer;

import com.chaos.model.Skill;
import com.chaos.util.Misc;
import com.chaos.world.content.PlayerPanel;
import com.chaos.world.entity.impl.player.Player;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The Slayer Skill
 * @Author Jonny
 */

public class Slayer {

    Player player;
    private SlayerMasters slayerMaster;
    private SlayerTasks slayerTask;
    private int amountLeft;
    private int slayerStreak;

    public Slayer(Player player) {
        this.player = player;
    }

    /**
     * Get the slayer master that is assigned to the
     * players slayer task.
     * @return
     */
    public SlayerMasters getSlayerMaster() {
        return this.slayerMaster;
    }

    /**
     * Set the player a new slayer master depending on
     * what slayer task he is currently on.
     * @param slayerMaster
     */
    public void setSlayerMaster(SlayerMasters slayerMaster) {
        this.slayerMaster = slayerMaster;
    }

    /**
     * Get the slayer task that the player is currently on.
     * @return
     */
    public SlayerTasks getSlayerTask() {
        return this.slayerTask;
    }

    /**
     * Gets the total amount left on your slayer task.
     * @return
     */
    public int getAmountLeft() {
        return this.amountLeft;
    }

    /**
     * Sets the total amount left to kill on your slayer task.
     * @param amountLeft
     */
    public void setAmountLeft(int amountLeft) {
        this.amountLeft = amountLeft;
    }

    /**
     * Set the player a new slayer task.
     * @param slayerTask
     */
    public void setSlayerTask(SlayerTasks slayerTask) {
        this.slayerTask = slayerTask;
    }

    /**
     * Checks if you have the correct requirements to get
     * a slayer task from a certain slayer master.
     * @param slayerMaster
     * @return
     */
    public boolean hasMasterRequirements(SlayerMasters slayerMaster) {
        if(player.getSkillManager().getCombatLevel() >= slayerMaster.getCombatLevelRequirement()) {
            return true;
        }
        return false;
    }

    /**
     * Assigns a slayer task for your player
     * @param slayerMaster
     */
    public void assignSlayerTask(SlayerMasters slayerMaster, boolean bypass) {
        if(!bypass) {
            if (player.getSlayer().getSlayerTask() != null) {
                player.setNpcClickId(slayerMaster.getNpcId());
                player.getDialog().sendDialog(new SlayerDialog(player, 2));
                return;
            }
        }
        ArrayList<SlayerTasks> possibleTasks = new ArrayList<>();
        for(SlayerTasks slayerTasks: SlayerTasks.values()) {
            if(slayerTasks.getSlayerMaster() == slayerMaster || slayerTasks.getSlayerMaster().ordinal() == slayerMaster.ordinal() - 1) {
                if(!possibleTasks.contains(slayerTasks)) {
                    possibleTasks.add(slayerTasks);
                }
            }
        }
        Collections.shuffle(possibleTasks);
        SlayerTasks myTask = possibleTasks.get(0);
        player.getSlayer().setSlayerMaster(slayerMaster);
        player.getSlayer().setSlayerTask(myTask);
        player.getSlayer().setAmountLeft(Misc.random(myTask.getMinimumAmount(), myTask.getMaximumAmount()));
        player.setNpcClickId(slayerMaster.getNpcId());
        player.getDialog().sendDialog(new SlayerDialog(player, 3));
        PlayerPanel.refreshPanel(player);
    }

    /**
     * Gets the name of your current slayer task
     * @return
     */
    public String getTaskName() {
        return this.getSlayerTask().getName();
    }

    /**
     * Opens the slayer dialog to check how many
     * monsters you have left to kill before your
     * task is over.
     */
    public void checkAmountLeft() {
        player.setNpcClickId(player.getSlayer().getSlayerMaster().getNpcId());
        player.getDialog().sendDialog(new SlayerDialog(player, 5));
    }

    /**
     * Opens the slayer dailog to check if your master
     * has any recommendations for your current task.
     */
    public void checkRecommendations() {
        player.setNpcClickId(player.getSlayer().getSlayerMaster().getNpcId());
        player.getDialog().sendDialog(new SlayerDialog(player, 6));
    }

    /**
     * Gives you an easier slayer task
     * @param slayerMaster
     */
    public void giveEasierTask(SlayerMasters slayerMaster) {
        if(player.getSlayer().getSlayerMaster().ordinal() > slayerMaster.ordinal()) {
            player.getSlayer().assignSlayerTask(slayerMaster, true);
        } else {
            player.setNpcClickId(player.getSlayer().getSlayerMaster().getNpcId());
            player.getDialog().sendDialog(new SlayerDialog(player, 7));
        }
    }

    /**
     * Check if you killed a slayer npc,
     * if you did reward yourself!
     * @param npcId
     */
    public void killedNpc(int npcId) {
        if(player.getSlayer().getSlayerTask() == null) {
            return;
        }
        for(int i = 0; i <= player.getSlayer().getSlayerTask().getNpcIds().length; i++) {
            if(player.getSlayer().getSlayerTask().getNpcId(i) == npcId) {
                player.getSlayer().decrementAmountLeft(1, i);
                break;
            }
        }
    }

    /**
     * Decrements a certain amount from the amount
     * of kills you have left to kill on your task.
     * @param decrement
     */
    public void decrementAmountLeft(int decrement, int taskIndex) {
        player.getSkillManager().addSkillExperience(Skill.SLAYER, player.getSlayer().getSlayerTask().getExperience(taskIndex));
        this.amountLeft -= decrement;
        if(this.amountLeft == 0) {
            player.getSlayer().addSlayerStreak(1);
            player.getPointsHandler().setSlayerPoints(this.getPointsToGive(), true);
            player.setNpcClickId(player.getSlayer().getSlayerMaster().getNpcId());
            player.getDialog().sendDialog(new SlayerDialog(player, 8));
            player.getSlayer().setSlayerMaster(null);
            player.getSlayer().setSlayerTask(null);
            player.getSlayer().setAmountLeft(0);
        } else if(this.amountLeft == 10 || this.amountLeft == 25 || this.amountLeft == 50 || this.amountLeft == 100 || this.amountLeft == 200) {
            player.getPacketSender().sendMessage("You have "+this.amountLeft+" "+player.getSlayer().getTaskName()+"s left on your current Slayer Task.");
        }
        PlayerPanel.refreshPanel(player);
    }

    /**
     * Get the current slayer streak that you are on
     * @return
     */
    public int getSlayerStreak() {
        return this.slayerStreak;
    }

    /**
     * Add onto your current slayer streak.
     * @param i
     */
    public void addSlayerStreak(int i) {
        this.slayerStreak += i;
    }

    /**
     * Sets a slayer streak
     * @param i
     */
    public void setSlayerStreak(int i) {
        this.slayerStreak = i;
    }

    /**
     * Reset your current slayer streak.
     */
    public void resetSlayerStreak() {
        this.slayerStreak = 0;
    }

    public int getPointsToGive() {
        for(int i = 1; i <= 20; i++) {
            if (this.slayerStreak == 10 * i && 10 * i != 50) {
                return player.getSlayer().getSlayerMaster().getTenTaskBonus();
            } else if (this.slayerStreak == 50 * i)
                return player.getSlayer().getSlayerMaster().getFiftyTaskBonus();
        }
        return player.getSlayer().getSlayerMaster().getPointsPerTask();
    }

    /**
     * Tells you if you are on a slayer task
     * depending on a npc id
     * @param npcId
     * @return
     */
    public boolean onSlayerTask(int npcId) {
        for(int i = 0; i <= this.getSlayerTask().getNpcIds().length; i++) {
            if(this.getSlayerTask().getNpcId(i) == npcId) {
                return true;
            }
        }
        return false;
    }
}