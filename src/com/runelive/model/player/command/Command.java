package com.runelive.model.player.command;

import com.runelive.model.PlayerRights;
import com.runelive.world.entity.impl.player.Player;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/26/2016.
 *
 * @author Seba
 */
public abstract class Command {

    /**
     * Constructs our command
     * @param playerRights
     */
    public Command(PlayerRights playerRights) {
        this.playerRights = playerRights;
    }

    /**
     * Executes the command's actions
     * @param player The player executing the command
     * @param args The args that the player passed threw
     * @param privilege The rights of the player.
     */
    public abstract void execute(Player player, String[] args, PlayerRights privilege);

    /**
     * Returns the player rights needed to execute the command.
     * @return
     */
    public PlayerRights getPlayerRights() {
        return playerRights;
    }

    /**
     * The player rights needed to execute this command
     */
    private final PlayerRights playerRights;
}
