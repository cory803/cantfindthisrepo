package org.scripts.kotlin.content.commands

import com.chaos.model.Locations
import com.chaos.model.StaffRights
import com.chaos.model.player.command.Command
import com.chaos.world.World
import com.chaos.world.content.transportation.TeleportHandler
import com.chaos.world.content.transportation.TeleportType
import com.chaos.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class TeleportPlayerToMe(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::teletome-playername")
        } else {
            val t = World.getPlayerByName(args[0])
            if (t == null) {
                player.packetSender.sendMessage("That player is not online.")
                return
            }

            if (player.username == t.username) {
                player.packetSender.sendMessage("You cannot teleport to yourself noob.")
                return
            }

            if (player.location === Locations.Location.WILDERNESS) {
                player.packetSender.sendMessage("You cannot teleport a player into the wild... What're you thinking?")
                return
            }

            if (t.location === Locations.Location.DUEL_ARENA) {
                player.packetSender.sendMessage("You cannot teleport someone who is in the duel arena.")
                return
            }

            val canTele = TeleportHandler.checkReqs(player, t.position.copy()) && player.regionInstance == null && t.regionInstance == null

            if (canTele) {
                TeleportHandler.teleportPlayer(t, player.position.copy(), TeleportType.NORMAL)
                player.packetSender.sendMessage("Teleporting player to you: " + t.username + "")
                t.packetSender.sendMessage("You're being teleported to " + player.username + "...")
            } else {
                player.packetSender.sendMessage("You cannot teleport the player at the moment.")
            }
        }
    }
}
