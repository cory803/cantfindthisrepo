package org.scripts.kotlin.content.commands

import com.runelive.GameSettings
import com.runelive.model.Locations
import com.runelive.model.PlayerRights
import com.runelive.model.player.command.Command
import com.runelive.world.World
import com.runelive.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class MovePlayerHome(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: PlayerRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::movehome-playername")
        } else {
            val player2 = World.getPlayerByName(args[0])
            if (player2 == null) {
                player.packetSender.sendMessage("We were unable to find that player")
                return
            }
            if (player2.location === Locations.Location.DUEL_ARENA) {
                player.packetSender.sendMessage("Why are you trying to move a player out of duel arena?")
                return
            }
            if (player.username == player2.username && player.location === Locations.Location.WILDERNESS) {
                player.packetSender.sendMessage("You cannot move yourself out of the wild.")
                return
            }
            if (player2.location === Locations.Location.DUNGEONEERING) {
                player.packetSender.sendMessage("You cannot move someone out of dung.")
                return
            }
            player2.moveTo(GameSettings.DEFAULT_POSITION_VARROCK.copy())
            player2.packetSender.sendMessage("You've been teleported home by " + player.username + ".")
            player.packetSender.sendMessage("Sucessfully moved " + player2.username + " to home.")

        }
    }
}
