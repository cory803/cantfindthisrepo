package org.scripts.kotlin.content.commands

import com.runelive.model.PlayerRights
import com.runelive.model.player.command.Command
import com.runelive.world.World
import com.runelive.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/27/2016.

 * @author Seba
 */
class DemoteWiki(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: PlayerRights) {
        if (player.rights != PlayerRights.WIKI_MANAGER) {
            player.packetSender.sendMessage("You do not have sufficient privileges to use this command.")
            return
        }
        if (args ==null) {
            player.packetSender.sendMessage("Please use the command as ::demote-playername")
            return
        }
        val demote = World.getPlayerByName(args[0])
        if (demote == null) {
            player.packetSender.sendMessage("Either the player is not online or you typed in the name incorrectly.")
            return
        }
        if (demote.rights == PlayerRights.WIKI_EDITOR) {
            demote.rights = PlayerRights.PLAYER
            demote.packetSender.sendRights()
            demote.packetSender.sendMessage("Your Wiki Editor rank has been taken away.")
            demote.packetSender.sendRights()
            player.packetSender.sendMessage("You have demoted " + demote.username)
        } else {
            player.packetSender.sendMessage("That player is currently not a Wiki Editor")
        }
    }
}
