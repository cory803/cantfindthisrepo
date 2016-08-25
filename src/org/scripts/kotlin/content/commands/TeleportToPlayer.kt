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
class TeleportToPlayer(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
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
            if (canTele) {
                TeleportHandler.teleportPlayer(player, teleTo.position.copy(), TeleportType.NORMAL)
                player.packetSender.sendMessage("Teleporting to player: " + teleTo.username + "")
            } else {
                player.packetSender.sendMessage("You can not teleport to this player at the moment. Minigame maybe?")
            }
        }
    }
}
