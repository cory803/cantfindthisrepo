package org.scripts.kotlin.content.commands

import com.chaos.model.PlayerRights
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
class ResetPin(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: PlayerRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::resetpin-playername")
        } else {
            val victimUsername = args[0]
            PlayerSaving.accountExists(victimUsername) { rs ->
                if (rs.next()) {
                    // account exists
                    val other = World.getPlayerByName(victimUsername)
                    if (other == null) {
                        AccountTools.resetPin(player, victimUsername, Player(null))
                    } else {
                        if (other.bankPinAttributes.hasBankPin()) {
                            for (i in 0..other.bankPinAttributes.bankPin.size - 1) {
                                other.bankPinAttributes.bankPin[i] = 0
                                other.bankPinAttributes.enteredBankPin[i] = 0
                            }
                            other.bankPinAttributes.setHasBankPin(false)
                            player.packetSender.sendMessage("The player $victimUsername's account pin has been reset.")
                            World.deregister(other)
                        } else {
                            player.packetSender.sendMessage("The player $victimUsername currently does not have an account pin.")
                        }
                    }
                } else {
                    player.packetSender.sendMessage("Player $victimUsername does not exist.")
                }
            }
        }
    }
}
