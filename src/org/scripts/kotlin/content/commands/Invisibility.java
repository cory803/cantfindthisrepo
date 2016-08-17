package org.scripts.kotlin.content.commands;

import com.runelive.model.PlayerRights;
import com.runelive.model.player.command.Command;
import com.runelive.world.entity.impl.player.Player;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/7/2016.
 *
 * @author Seba
 */
public class Invisibility extends Command {

    public Invisibility(PlayerRights playerRights) {
        super(playerRights);
    }

    @Override
    public void execute(Player player, String[] args, PlayerRights privilege) {
        if (args == null) {
            player.setInvisible(!player.isInvisible());
            player.getPacketSender().sendMessage("You are now " + (player.isInvisible() ? "invisible." : "visible."));
        }
    }
}
