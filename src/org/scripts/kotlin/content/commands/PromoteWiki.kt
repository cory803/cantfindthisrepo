package org.scripts.kotlin.content.commands

import com.google.common.collect.Sets
import com.chaos.model.PlayerRights
import com.chaos.model.player.command.Command
import com.chaos.world.World
import com.chaos.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/27/2016.

 * @author Seba
 */
class PromoteWiki(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player?, args: Array<String>?, privilege: PlayerRights) {
        if (player!!.rights != PlayerRights.WIKI_MANAGER) {
            player.packetSender.sendMessage("You do not have sufficient privileges to use this command.")
            return
        }
        if (args == null) {
            player!!.packetSender.sendMessage("Please use the command as ::promote-playername")
            return
        }
        val promote = World.getPlayerByName(args[0])
        if (promote == null) {
            player!!.packetSender.sendMessage("Either that player is not online or you typed the name incorrectly.")
            return
        }

        if (PROMOTABLE.contains(promote.rights)) {
            promote.rights = PlayerRights.WIKI_EDITOR
            promote.packetSender.sendRights()
            promote.packetSender.sendMessage("Congratulations, " + player!!.username + " has promoted you to a Wiki Editor!")
            player.packetSender.sendMessage("You have promoted " + promote.username + " to a Wiki Editor!")
        } else {
            player!!.packetSender.sendMessage("This player's current rank cannot be promoted")
        }
    }

    companion object {

        private val PROMOTABLE = Sets.immutableEnumSet(
                PlayerRights.PLAYER, PlayerRights.IRONMAN, PlayerRights.HARDCORE_IRONMAN, PlayerRights.REGULAR_DONOR, PlayerRights.LEGENDARY_DONOR, PlayerRights.EXTREME_DONOR, PlayerRights.SUPER_DONOR, PlayerRights.UBER_DONOR)
    }
}
