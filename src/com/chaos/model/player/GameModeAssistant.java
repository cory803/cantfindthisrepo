package com.chaos.model.player;

import com.chaos.model.Skill;
import com.chaos.util.Misc;
import com.chaos.world.content.Achievements;
import com.chaos.world.content.PlayerPanel;
import com.chaos.world.content.skill.SkillManager;
import com.chaos.world.entity.impl.player.Player;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/16/2016.
 *
 * @author Seba
 */
public class GameModeAssistant {

    /**
     * Constructs our gamemode assistant.
     * @param player
     */
    public GameModeAssistant(Player player) {
        this.player = player;
    }

    /**
     * Defines the player this assistant is for.
     */
    private final Player player;

    /**
     * Define what game mode the player is on. If they dont have one we will give them default by default.
     */
    private GameMode gameMode = GameMode.KNIGHT;

    /**
     * Returns the ordinal of the game mode.  We will use this for saving.
     * @return
     */
    public int ordinal() {
        return this.gameMode.ordinal();
    }

    /**
     * This is only used to set the game mode from a save.
     * @param gameMode
     */
    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Returns the formatted name of the gamemode.
     * @return
     */
    public String getModeName() {
        return Misc.formatText(this.gameMode.toString().toLowerCase().replace("_", ""));
    }

    /**
     * Returns the game mode's exp rate.
     * @return
     */
    public int getModeExpRate() {
        return this.gameMode.getModeExpRate();
    }

    /**
     * Returns the drop rate of the game mode.
     * @return
     */
    public double getMonsterDropRate() {
        return this.gameMode.getMonsterDropRate();
    }

    /**
     * Returns the special recovery rate of the game mode.
     * @return
     */
    public int getSpecialRecoveryRate() {
        return this.gameMode.getSpecialRecoveryRate();
    }

    /**
     * Returns the prayer drain rate for the player.
     * @return
     */
    public double getPrayerDrainRate() {
        return this.gameMode.getPrayerDrainRate();
    }

    /**
     * Returns the game mode of the player.
     * @return
     */
    public GameMode getGameMode() {
        return this.gameMode;
    }

    /**
     * Checks to see if the player is a iron man.
     * @return
     */
    public boolean isIronMan() {
        return this.gameMode == GameMode.IRONMAN;
    }

    /**
     * This will process a game mode switch.
     * @param gamemode
     */
    public void setNewGamemode(GameMode gamemode) {
        if (this.gameMode.getModeExpRate() < gamemode.getModeExpRate()) {
            /**
             * We are going to adjust the xp fo the player since they are moving to a higher xp rate.
             */
            double adjust = (double) gamemode.getModeExpRate() / (double) this.gameMode.getModeExpRate();
            for (int i = 0; i < SkillManager.MAX_SKILLS; i++) {
                Skill skill = Skill.values()[i];
                int newXp =  player.getSkillManager().getExperience(skill);
                /**
                 * We are going to subtract the starting xp for hp.  This is because the player can make a new account
                 * then simply move to sir and bam they have 1,154 xp times 125.
                 */
                if (skill == Skill.CONSTITUTION) {
                   newXp -= 1_154; //We are doing this because someone can my a realism then move to sir and gain loads of free levels.
                }
                /**
                 * We are now going to adjust their xp.
                 */
                newXp *= adjust;
                /**
                 * We will add their xp back because if they did try to cheat the system they will now have 1 hp.  AKA they cannot train.
                 */
                if (skill == Skill.CONSTITUTION) {
                    newXp += 1_154; //We will then add the xp back.
                }
                /**
                 * If their xp rate is more than the cap we need to set it at cap.
                 */
                if (newXp > 1_000_000_000) {
                    newXp = 1_000_000_000;
                }
                player.getSkillManager().setExperience(skill, newXp);
                player.getSkillManager().setMaxLevel(skill, (SkillManager.isNewSkill(skill) ? SkillManager.getLevelForExperience(skill, newXp) * 10 : SkillManager.getLevelForExperience(skill, newXp)));
                player.getSkillManager().setCurrentLevel(skill, (SkillManager.isNewSkill(skill) ? SkillManager.getLevelForExperience(skill, newXp) * 10 : SkillManager.getLevelForExperience(skill, newXp)));
            }
        } else {
            /**
             * Time to reset all of the players skills
             */
            for (int i = 0; i < SkillManager.MAX_SKILLS; i++) {
                Skill skill = Skill.values()[i];
                /**
                 * We need to make sure they have the correct hp when they move to the lower ranks.
                 */
                if (skill == Skill.CONSTITUTION) {
                    player.getSkillManager().setMaxLevel(Skill.CONSTITUTION, 100);
                    player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 100);
                    player.getSkillManager().setExperience(Skill.CONSTITUTION, SkillManager.getExperienceForLevel(10));
                    continue;
                }
                /**
                 * We need to make sure they have the correct prayer level
                 */
                if (skill == Skill.PRAYER) {
                    player.getSkillManager().setMaxLevel(skill, 10);
                    player.getSkillManager().setCurrentLevel(skill, 10);
                    player.getSkillManager().setExperience(skill, 0);
                    continue;
                }
                player.getSkillManager().setMaxLevel(skill, 1);
                player.getSkillManager().setCurrentLevel(skill, 1);
                player.getSkillManager().setExperience(skill, 0);
            }
            /**
             * We will now reset all of the player's achievements.
             */
            for (int i = 0; i < Achievements.AchievementData.values().length; i++) {
                player.getAchievementAttributes().setCompletion(i, false);
            }
            for (int i = 0; i < player.getAchievementAttributes().getProgress().length; i ++) {
                player.getAchievementAttributes().setProgress(i, 0);
            }
            if (player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6) {
                Achievements.finishAchievement(player, Achievements.AchievementData.DEFEAT_NOMAD);
                Achievements.finishAchievement(player, Achievements.AchievementData.DEFEAT_THE_CULINAROMANCER);
            }
            player.getPointsHandler().setAchievementPoints(0, false);
        }
        this.gameMode = gamemode;
        player.getSlayer().setSlayerMaster(null);
        player.getSlayer().setSlayerTask(null);
        player.getSlayer().setDuoSlayer(null);
        player.getSlayer().setAmountLeft(0);
        PlayerPanel.refreshPanel(player);
        Achievements.updateInterface(player);
    }

}