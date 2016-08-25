package org.scripts.kotlin.content.commands

import com.chaos.model.Locations
import com.chaos.model.StaffRights
import com.chaos.model.Position
import com.chaos.model.player.command.Command
import com.chaos.world.content.transportation.TeleportHandler
import com.chaos.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/27/2016.

 * @author Seba
 */
class TeleportDuel(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (player.location === Locations.Location.WILDERNESS && player.wildernessLevel >= 20) {
            player.packetSender.sendMessage("You cannot do this at the moment.")
            return
        }
        if (player.location === Locations.Location.DUEL_ARENA) {
            player.packetSender.sendMessage("You can't do this right now.")
            return
        }
        val position = Position(3370, 3267, 0)
        TeleportHandler.teleportPlayer(player, position, player.spellbook.teleportType)
        player.packetSender.sendMessage("Teleporting you to the duel arena!")
    }
}
