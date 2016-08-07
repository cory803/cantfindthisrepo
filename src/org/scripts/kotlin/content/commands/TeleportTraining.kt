package org.scripts.kotlin.content.commands

import com.runelive.model.Locations
import com.runelive.model.PlayerRights
import com.runelive.model.Position
import com.runelive.model.player.command.Command
import com.runelive.world.content.skill.impl.dungeoneering.Dungeoneering
import com.runelive.world.content.transportation.TeleportHandler
import com.runelive.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/27/2016.

 * @author Seba
 */
class TeleportTraining(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: PlayerRights) {
        if (Dungeoneering.doingDungeoneering(player)) {
            player.packetSender.sendMessage("You can't use this command in a dungeon.")
            return
        }
        if (player.location === Locations.WILDERNESS && player.wildernessLevel >= 20) {
            player.packetSender.sendMessage("You cannot do this at the moment.")
            return
        }
        val position = Position(2679, 3720, 0)
        TeleportHandler.teleportPlayer(player, position, player.spellbook.teleportType)
        player.packetSender.sendMessage("Teleporting you to rock crabs!")
    }
}
