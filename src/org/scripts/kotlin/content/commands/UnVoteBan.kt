package org.scripts.kotlin.content.commands

import com.chaos.model.PlayerRights
import com.chaos.model.player.command.Command
import com.chaos.world.World
import com.chaos.world.content.PlayerPunishment
import com.chaos.world.entity.impl.player.Player
import com.chaos.world.entity.impl.player.PlayerSaving

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class UnVoteBan(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: PlayerRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::unbanvote-playername")
        } else {
            val victim = args[0]
            PlayerSaving.accountExists(victim) { rs ->
                if (rs.next()) {
                    if (!PlayerPunishment.isVoteBanned(victim)) {
                        player.packetSender.sendMessage("That player is currently not vote banned.")
                        return@accountExists
                    }
                    PlayerPunishment.unVoteBan(victim)
                    val p = World.getPlayerByName(victim)
                    if (p != null) {
                        p.packetSender.sendMessage("You have been un vote banned.")
                    }
                    player.packetSender.sendMessage("You have successfully un vote banned " + victim)
                } else {
                    player.packetSender.sendMessage("That account does not exist")
                }
            }
        }
    }
}
