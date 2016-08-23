package org.scripts.kotlin.content.commands

import com.chaos.model.PlayerRights
import com.chaos.model.player.command.Command
import com.chaos.world.content.PlayerPunishment
import com.chaos.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class UnMute(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: PlayerRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example: ::unmute-playername")
        } else {
            if (!PlayerPunishment.isMuted(args[0])) {
                player.packetSender.sendMessage(args[0] + " is currently not muted.")
                return
            }
            PlayerPunishment.unMute(args[0])
            player.packetSender.sendMessage(args[0] + " has been successfully unmuted")
        }
    }
}
