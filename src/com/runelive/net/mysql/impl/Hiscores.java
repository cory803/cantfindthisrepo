package com.runelive.net.mysql.impl;

import com.runelive.GameServer;
import com.runelive.model.PlayerRights;
import com.runelive.model.Skill;
import com.runelive.net.mysql.SQLCallback;
import com.runelive.world.content.skill.SkillManager;
import com.runelive.world.entity.impl.player.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/18/2016.
 *
 * @author Seba
 */
public class Hiscores implements SQLCallback {

    /**
     * Construct our hiscores entry.
     * @param player
     */
    public Hiscores(Player player) {
        this.player = player;
    }

    /**
     * Defines the player we doing the update for.
     */
    private Player player;

    /**
     * Executes the hiscores entry.
     * @return
     */
    public Hiscores execute() {
        try {
            PreparedStatement preparedStatement = GameServer.getVotingPool().prepareStatement("DELETE FROM `skills` WHERE `skills`.`playerName` = ?");
            preparedStatement.setString(1, player.getUsername());

            GameServer.getVotingPool().executeQuery(preparedStatement, this);

            preparedStatement = GameServer.getVotingPool().prepareStatement("INSERT INTO `skills` (`skillsindex`, `playerName`, `playerRank`, `rights`, `TotalLevel`, `TotalXP`, `Attack`, `Defence`, `Strength`, `Hitpoints`, `Ranged`, `Prayer`, `Magic`, `Cooking`, `Woodcutting`, `Fletching`, `Fishing`, `Firemaking`, `Crafting`, `Smithing`, `Mining`, `Herblore`, `Agility`, `Thieving`, `Slayer`, `Farming`, `Runecrafting`, `Dungeoneering`, `Summoning`, `Hunter`, `Construction`) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, player.getUsername());
            preparedStatement.setByte(2, (byte) player.getGameModeAssistant().ordinal());
            preparedStatement.setByte(3, (byte) getRank());
            preparedStatement.setShort(4, getTotals());
            preparedStatement.setLong(5, getXp());
            for (int i = 0; i < SkillManager.MAX_SKILLS; i++) {
                preparedStatement.setInt((i + 6), player.getSkillManager().getExperience(Skill.values()[i]));
            }

            GameServer.getVotingPool().executeQuery(preparedStatement, this);
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Returns the rank of the player so the hiscores can then show their rank ingame.
     * @return
     */
    private int getRank() {
        PlayerRights rights = player.getRights();
        switch (rights) {
            case SUPPORT:
                return 6;
            case YOUTUBER:
                return 7;
            case GLOBAL_MOD:
                return 8;
            case ADMINISTRATOR:
                return 9;
            case WIKI_MANAGER:
                return 10;
            case WIKI_EDITOR:
                return 11;
            case MODERATOR:
                return 13;
            case PLAYER: {
                switch (player.getDonorRights()) {
                    case 1:
                        return 1;
                    case 2:
                        return 2;
                    case 3:
                        return 3;
                    case 4:
                        return 4;
                    case 5:
                        return 5;
                }
                if (player.getGameModeAssistant().isIronMan()) {
                    return 12;
                }
            }
            default:
                return 0;
        }
    }

    /**
     * Returns the total level for the player.
     * @return
     */
    private short getTotals() {
        int totals = 0;
        for (Skill skill : Skill.values()) {
            totals += player.getSkillManager().getMaxLevel(skill);
        }
        return (short) totals;
    }

    /**
     * Returns the total xp for the player.
     * @return
     */
    private long getXp() {
        long xp = 0;
        for (Skill skill : Skill.values()) {
            xp += player.getSkillManager().getExperience(skill);
        }
        return xp;
    }


    @Override
    public void queryComplete(ResultSet result) throws SQLException {

    }

    @Override
    public void queryError(SQLException e) {

    }


}
