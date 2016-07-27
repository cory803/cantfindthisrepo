package org.scripts.kotlin.content.commands

import com.runelive.model.PlayerRights
import com.runelive.model.player.command.Command
import com.runelive.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/27/2016.

 * @author Seba
 */
class ChangePassword(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>, privilege: PlayerRights) {
        player.inputHandling = com.runelive.model.input.impl.ChangePassword()
        player.packetSender.sendEnterInputPrompt("Enter a new password:")
    }
}
