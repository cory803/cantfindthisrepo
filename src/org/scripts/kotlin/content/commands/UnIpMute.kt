package org.scripts.kotlin.content.commands

import com.runelive.model.PlayerRights
import com.runelive.model.player.command.Command
import com.runelive.world.World
import com.runelive.world.content.PlayerPunishment
import com.runelive.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class UnIpMute(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: PlayerRights) {
        if (args == null) {
            player.packetSender.sendMessage("Exmaple usage: ::unipmute-playername")
        } else {
            val p = World.getPlayerByName(args[0])
            if (p == null) {
                player.packetSender.sendMessage("Unable to find that player.")
                return
            }
            if (!PlayerPunishment.isIpMuted(p.hostAddress)) {
                player.packetSender.sendMessage(p.username + " currently does not have an active ban.")
                return
            }
            PlayerPunishment.unIpMute(p.hostAddress)
            player.packetSender.sendMessage("You have successfully un ipmuted " + p.username)
            p.packetSender.sendMessage("You have been un ipmuted.")
        }
    }
}