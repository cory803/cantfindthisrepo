package org.scripts.kotlin.content.commands

import com.runelive.model.GameObject
import com.runelive.model.PlayerRights
import com.runelive.model.player.command.Command
import com.runelive.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class SpawnObject(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: PlayerRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::object-objectid")
        } else {
            var id = 0
            try {
                id = Integer.parseInt(args[0])
            } catch (e: NumberFormatException) {
                player.packetSender.sendMessage("Error parsing the int value. Use numbers")
            }

            player.packetSender.sendObject(GameObject(id, player.position, 10, 3))
            player.packetSender.sendMessage("Sending object: " + id)
        }
    }
}
