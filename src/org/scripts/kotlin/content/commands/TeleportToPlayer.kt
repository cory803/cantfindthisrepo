package org.scripts.kotlin.content.commands

import com.runelive.model.Locations
import com.runelive.model.PlayerRights
import com.runelive.model.player.command.Command
import com.runelive.world.World
import com.runelive.world.content.transportation.TeleportHandler
import com.runelive.world.content.transportation.TeleportType
import com.runelive.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class TeleportToPlayer(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: PlayerRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::teleto-playername")
        } else {
            val teleTo = World.getPlayerByName(args[0])
            if (teleTo == null) {
                player.packetSender.sendMessage("That player is not online.")
                return
            }
            if (player.username == teleTo.username) {
                player.packetSender.sendMessage("You are unable to teleport to yourself. NOOB")
                return
            }
            val canTele = TeleportHandler.checkReqs(player, teleTo.position.copy()) && player.regionInstance == null && teleTo.regionInstance == null
            if (canTele && player.location !== Locations.Location.DUNGEONEERING) {
                TeleportHandler.teleportPlayer(player, teleTo.position.copy(), TeleportType.NORMAL)
                player.packetSender.sendMessage("Teleporting to player: " + teleTo.username + "")
            } else {
                if (teleTo.location === Locations.Location.DUNGEONEERING) {
                    player.packetSender.sendMessage("You can not teleport to this player while they are dungeoneering.")
                } else {
                    player.packetSender.sendMessage("You can not teleport to this player at the moment. Minigame maybe?")
                }
            }
        }
    }
}
