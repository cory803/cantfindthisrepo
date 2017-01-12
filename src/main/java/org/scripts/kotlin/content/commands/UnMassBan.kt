package org.scripts.kotlin.content.commands

import com.runelive.model.StaffRights
import com.runelive.model.player.command.Command
import com.runelive.world.content.PlayerPunishment
import com.runelive.world.entity.impl.player.Player
import com.runelive.world.entity.impl.player.PlayerSaving

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class UnMassBan(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::unmassban-playername")
        } else {
            val victim = args[0]
            PlayerSaving.accountExists(victim) { rs ->
                if (rs.next()) {
                    PlayerPunishment.unmassBan(player, victim)
                } else {
                    player.packetSender.sendMessage("The account $victim does not exist.")
                }
            }
        }
    }
}
