package org.scripts.kotlin.content.commands

import com.chaos.model.StaffRights
import com.chaos.model.player.command.Command
import com.chaos.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/27/2016.

 * @author Seba
 */
class ChangePassword(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        player.inputHandling = com.chaos.model.input.impl.ChangePassword()
        player.packetSender.sendEnterInputPrompt("Enter a new password:")
    }
}
