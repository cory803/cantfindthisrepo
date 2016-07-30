package org.scripts.kotlin.content.commands;

import com.runelive.model.PlayerRights;
import com.runelive.model.Position;
import com.runelive.model.player.command.Command;
import com.runelive.world.World;
import com.runelive.world.entity.impl.player.Player;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/28/2016.
 *
 * @author Seba
 */
public class Unjail extends Command {

    public Unjail(PlayerRights playerRights) {
        super(playerRights);
    }

    @Override
    public void execute(Player player, String[] args, PlayerRights privilege) {
        if (args.length != 1) {
            player.getPacketSender().sendMessage("Please use the command like ::unjail-player");
            return;
        }
        Player player1 = World.getPlayerByName(args[0]);
        if (player1 == null) {
            player.getPacketSender().sendMessage("Either the player is offline or you typed in a wrong playername.");
        }
        player1.setJailed(false);
        player1.forceChat("Im free!!! I'm finally out of jail... Hooray!");
        player1.moveTo(new Position(3087, 3502, 0));
        player.getPacketSender().sendMessage("You have successfully unjailed " + player1.getUsername());
    }
}
