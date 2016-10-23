package org.scripts.kotlin.content.commands

import com.chaos.GameSettings
import com.chaos.model.Locations
import com.chaos.model.StaffRights
import com.chaos.model.player.command.Command
import com.chaos.world.World
import com.chaos.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class MovePlayerHome(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
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
            player2.moveTo(GameSettings.DEFAULT_POSITION_EDGEVILLE.copy())
            player2.packetSender.sendMessage("You've been teleported home by " + player.username + ".")
            player.packetSender.sendMessage("Sucessfully moved " + player2.username + " to home.")

        }
    }
}
