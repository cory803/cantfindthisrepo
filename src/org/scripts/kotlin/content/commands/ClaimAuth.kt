package org.scripts.kotlin.content.commands

import com.runelive.GameSettings
import com.runelive.model.PlayerRights
import com.runelive.model.Voting
import com.runelive.model.player.command.Command
import com.runelive.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/27/2016.

 * @author Seba
 */
class ClaimAuth(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>, privilege: PlayerRights) {
        if (args.size != 1) {
            player.packetSender.sendMessage("Please use the command as ::auth-authcode")
            return
        }
        if (!GameSettings.VOTING_CONNECTIONS) {
            player.packetSender.sendMessage("Voting connections are currently turned off, try again in 30 minutes!")
            return
        }
        if (!player.voteTimer.elapsed(30000)) {
            player.packetSender.sendMessage("You have to wait 30 seconds in order to use ::auth!")
            return
        }
        player.voteTimer.reset()
        Voting.useAuth(player, args[0])
    }
}
