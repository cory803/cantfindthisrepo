package org.scripts.kotlin.content.commands

import com.chaos.model.PlayerRights
import com.chaos.model.input.impl.SetPassword
import com.chaos.model.player.command.Command
import com.chaos.world.World
import com.chaos.world.entity.impl.player.Player
import com.chaos.world.entity.impl.player.PlayerSaving

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class SetPassword(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: PlayerRights) {
        if(args == null) {
            player.packetSender.sendMessage("Example usage: ::setpass-playername")
        } else {
            val victimUsername = args[0]
            PlayerSaving.accountExists(victimUsername) { rs ->
                if (rs.next()) {
                    // account exists
                    val other = World.getPlayerByName(victimUsername)
                    if (other == null) {
                        player.changingPasswordOf = victimUsername
                        player.inputHandling = SetPassword()
                        player.packetSender.sendEnterInputPrompt("(OFFLINE) Enter a new password for " + victimUsername)
                    } else {
                        player.changingPasswordOf = victimUsername
                        player.inputHandling = SetPassword()
                        player.packetSender.sendEnterInputPrompt("(ONLINE) Enter a new password for " + victimUsername)
                    }
                } else {
                    player.packetSender.sendMessage("Player $victimUsername does not exist.")
                }
            }
        }
    }
}
