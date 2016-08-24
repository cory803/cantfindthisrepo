package org.scripts.kotlin.content.commands

import com.chaos.model.DonatorRights
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
class GiveDonor(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::givedonor-playername-rights")
        } else {
            var rights = 0
            try {
                rights = Integer.parseInt(args[1])
            } catch (e: NumberFormatException) {
                player.packetSender.sendMessage("Error parsing your rights argument, try using numbers.")
            }

            val p = World.getPlayerByName(args[0])
            if (p == null) {
                player.packetSender.sendMessage("We were unable to find that player.")
                return
            }
            p.donatorRights = DonatorRights.forId(rights)
            p.packetSender.sendRights()
            p.packetSender.sendMessage("You have been given donor rights.")
            player.packetSender.sendMessage("You have given " + args[0] + " donor rights " + rights)
        }
    }
}
