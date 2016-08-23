package org.scripts.kotlin.content.commands

import com.chaos.model.Locations
import com.chaos.model.PlayerRights
import com.chaos.model.Position
import com.chaos.model.player.command.Command
import com.chaos.world.content.skill.impl.dungeoneering.Dungeoneering
import com.chaos.world.content.transportation.TeleportHandler
import com.chaos.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/7/2016.

 * @author Seba
 */
class TeleportWests(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: PlayerRights) {
        if (Dungeoneering.doingDungeoneering(player)) {
            player.packetSender.sendMessage("You can't use this command in a dungeon.")
            return
        }
        if (player.location === Locations.Location.WILDERNESS && player.wildernessLevel >= 20) {
            player.packetSender.sendMessage("You cannot do this at the moment.")
            return
        }
        val position = Position(2980, 3597, 0)
        TeleportHandler.teleportPlayer(player, position, player.spellbook.teleportType)
        player.packetSender.sendMessage("Teleporting you to west pking!")
    }
}
