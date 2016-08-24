package org.scripts.kotlin.content.commands

import com.chaos.model.StaffRights
import com.chaos.model.player.command.Command
import com.chaos.world.World
import com.chaos.world.content.AccountTools
import com.chaos.world.entity.impl.player.Player
import com.chaos.world.entity.impl.player.PlayerSaving

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class CheckPassword(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {

        } else {
            val victimUsername = args[0]
            PlayerSaving.accountExists(victimUsername) { rs ->
                if (rs.next()) {
                    // account exists
                    val other = World.getPlayerByName(victimUsername)
                    if (other == null) {
                        AccountTools.checkPassword(player, victimUsername, Player(null))
                    } else {
                        val password = other.password
                        if (password != null) {
                            player.packetSender.sendMessage("The player $victimUsername's password is: $password")
                        }
                    }
                } else {
                    player.packetSender.sendMessage("Player $victimUsername does not exist.")
                }
            }
        }
    }
}
