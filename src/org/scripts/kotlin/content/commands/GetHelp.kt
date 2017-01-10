package org.scripts.kotlin.content.commands

import com.runelive.model.StaffRights
import com.runelive.model.player.command.Command
import com.runelive.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/27/2016.

 * @author Seba
 */
class GetHelp(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        player.packetSender.sendString(1, "www.rune.live/forums/")
        player.packetSender.sendMessage("Attempting to open: www.rune.live/forums/")
        player.packetSender.sendMessage("Please note this requires you to register on the forums, type ::register!")
    }
}
