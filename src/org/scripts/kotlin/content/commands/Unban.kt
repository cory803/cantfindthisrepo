package org.scripts.kotlin.content.commands

import com.chaos.model.StaffRights
import com.chaos.model.player.command.Command
import com.chaos.world.content.PlayerPunishment
import com.chaos.world.entity.impl.player.Player
import com.chaos.world.entity.impl.player.PlayerSaving

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class Unban(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Exmaple usage: ::unban-playername")
        } else {
            val victim = args[0]
            PlayerSaving.accountExists(victim) { rs ->
                if (rs.next()) {
                    // account exists
                    if (!PlayerPunishment.isPlayerBanned(victim)) {
                        player.packetSender.sendMessage("Player $victim does not have an active ban!")
                        return@accountExists
                    }
                    PlayerPunishment.unBan(victim)
                    player.packetSender.sendMessage("Player $victim was successfully unbanned!")
                } else {
                    player.packetSender.sendMessage("Player $victim does not exist.")
                }
            }
        }
    }
}
