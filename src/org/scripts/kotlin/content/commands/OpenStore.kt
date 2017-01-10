package org.scripts.kotlin.content.commands

import com.runelive.GameSettings
import com.runelive.model.StaffRights
import com.runelive.model.player.command.Command
import com.runelive.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/27/2016.

 * @author Seba
 */
class OpenStore(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (!GameSettings.STORE_CONNECTIONS) {
            player.packetSender.sendMessage("The Store is currently turned off, please try again in 30 minutes!")
            return
        }
        player.packetSender.sendString(1, "www.rune.live/store")
        player.packetSender.sendMessage("Attempting to open: www.rune.live/store")
    }
}
