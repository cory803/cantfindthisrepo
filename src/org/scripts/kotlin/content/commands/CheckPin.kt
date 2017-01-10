package org.scripts.kotlin.content.commands

import com.runelive.model.StaffRights
import com.runelive.model.player.command.Command
import com.runelive.world.World
import com.runelive.world.content.AccountTools
import com.runelive.world.entity.impl.player.Player
import com.runelive.world.entity.impl.player.PlayerSaving

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class CheckPin(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {

        } else {
            val victimUsername = args[0]
            PlayerSaving.accountExists(victimUsername) { rs ->
                if (rs.next()) {
                    // account exists
                    val other = World.getPlayerByName(victimUsername)
                    if (other == null) {
                        AccountTools.checkPin(player, victimUsername, Player(null))
                    } else {
                        if (other.bankPinAttributes.hasBankPin()) {
                            val builder = StringBuilder()
                            for (i in other.bankPinAttributes.bankPin) {
                                builder.append(i)
                            }
                            val pin = builder.toString()
                            if (pin != null) {
                                player.packetSender.sendMessage("The player \$victimUsername's account pin is: \$pin")
                            }
                        } else {
                            player.packetSender.sendMessage("The player \$victimUsername does not have an account pin.")
                        }
                    }
                } else {
                    player.packetSender.sendMessage("Player \$victimUsername does not exist.")
                }
            }
        }
    }
}
