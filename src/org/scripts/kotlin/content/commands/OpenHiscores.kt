package org.scripts.kotlin.content.commands

import com.chaos.GameSettings
import com.chaos.model.PlayerRights
import com.chaos.model.player.command.Command
import com.chaos.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/27/2016.

 * @author Seba
 */
class OpenHiscores(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: PlayerRights) {
        if (!GameSettings.HIGHSCORE_CONNECTIONS) {
            player.packetSender.sendMessage("Hiscores is currently turned off, please try again in 30 minutes!")
            return
        }
        player.packetSender.sendString(1, "www.rune.live/hiscores/")
        player.packetSender.sendMessage("Attempting to open: www.rune.live/hiscores/")
    }
}
