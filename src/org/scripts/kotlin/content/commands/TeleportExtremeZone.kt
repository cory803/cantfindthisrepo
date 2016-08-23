package org.scripts.kotlin.content.commands

import com.chaos.model.Locations
import com.chaos.model.PlayerRights
import com.chaos.model.Position
import com.chaos.model.player.command.Command
import com.chaos.util.Misc
import com.chaos.world.content.skill.impl.dungeoneering.Dungeoneering
import com.chaos.world.content.transportation.TeleportHandler
import com.chaos.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/27/2016.

 * @author Seba
 */
class TeleportExtremeZone(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: PlayerRights) {
        if (Dungeoneering.doingDungeoneering(player)) {
            player.packetSender.sendMessage("You can't use this command in a dungeon.")
            return
        }
        if (player.location === Locations.Location.WILDERNESS && player.wildernessLevel >= 20) {
            player.packetSender.sendMessage("You cannot do this at the moment.")
            return
        }
        var position: Position? = null
        val ran = Misc.getRandom(3)
        when (ran) {
            0 -> position = Position(3363, 9641, 0)
            1 -> position = Position(3364, 9640, 0)
            2 -> position = Position(3363, 9639, 0)
            3 -> position = Position(3362, 9640, 0)
        }
        TeleportHandler.teleportPlayer(player, position, player.spellbook.teleportType)
        player.packetSender.sendMessage("<img=9><col=00ff00><shad=0> Welcome to the Extreme Donator Zone!")
    }
}
