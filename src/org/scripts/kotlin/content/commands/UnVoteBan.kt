package org.scripts.kotlin.content.commands

import com.runelive.model.PlayerRights
import com.runelive.model.player.command.Command
import com.runelive.world.World
import com.runelive.world.content.PlayerPunishment
import com.runelive.world.entity.impl.player.Player
import com.runelive.world.entity.impl.player.PlayerSaving

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
