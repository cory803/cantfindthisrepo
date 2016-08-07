package org.scripts.kotlin.content.commands

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
class GiveRights(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: PlayerRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::giverights-playername-rights")
        } else {
            val p = World.getPlayerByName(args[0])
            val rights = args[1].toLowerCase()
            var playerRights = PlayerRights.PLAYER
            when (rights) {
                "demote", "derank" -> playerRights = PlayerRights.PLAYER

                "ss", "serversupport", "support" -> playerRights = PlayerRights.SUPPORT

                "mod", "moderator" -> playerRights = PlayerRights.MODERATOR

                "globalmod", "gmod", "global_mod", "globalmoderator" -> playerRights = PlayerRights.GLOBAL_MOD

                "youtube", "youtuber" -> playerRights = PlayerRights.YOUTUBER

                else -> player.packetSender.sendMessage("We were unable to find that command")
            }

            p!!.rights = playerRights
            p.packetSender.sendRights()
            p.packetSender.sendMessage("You have been give " + playerRights.rightName + " rights.")
            player.packetSender.sendMessage("You have given " + args[0] + " rights: " + playerRights.rightName)
        }
    }
}
