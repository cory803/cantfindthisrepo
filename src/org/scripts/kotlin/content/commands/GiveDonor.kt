package org.scripts.kotlin.content.commands

import com.runelive.model.PlayerRights
import com.runelive.model.player.command.Command
import com.runelive.world.World
import com.runelive.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class GiveDonor(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>, privilege: PlayerRights) {
        if (args.size != 2) {
            player.packetSender.sendMessage("Example usage: ::givedonor-playername-rights")
        } else {
            var rights = 0
            try {
                rights = Integer.parseInt(args[1])
            } catch (e: NumberFormatException) {
                player.packetSender.sendMessage("Error parsing your rights argument, try using numbers.")
            }

            val p = World.getPlayerByName(args[0])
            if (p == null) {
                player.packetSender.sendMessage("We were unable to find that player.")
                return
            }
            p.donorRights = rights
            p.packetSender.sendRights()
            p.packetSender.sendMessage("You have been given donor rights.")
            player.packetSender.sendMessage("You have given " + args[0] + " donor rights " + rights)
        }
    }
}
