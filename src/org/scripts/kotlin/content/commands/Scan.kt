package org.scripts.kotlin.content.commands

import com.runelive.model.PlayerRights
import com.runelive.model.player.command.Command
import com.runelive.world.World
import com.runelive.world.content.AccountTools
import com.runelive.world.entity.impl.player.Player
import com.runelive.world.entity.impl.player.PlayerSaving

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/28/2016.

 * @author Seba
 */
class Scan(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>, privilege: PlayerRights) {
        if (args.size != 1) {
            player.packetSender.sendMessage("Please use the command as ::scan-username")
            return
        }
        PlayerSaving.accountExists(args[0]) { rs ->
            if (rs.next()) {
                // account exists
                val other = World.getPlayerByName(args[0])
                if (other == null) {
                    AccountTools.scan(player, args[0], Player(null))
                } else {
                    AccountTools.outScan(player, args[0], other.macAddress, other)
                }
            } else {
                player.packetSender.sendMessage("Player " + args[0] + " does not exist.")
            }
        }
    }
}
