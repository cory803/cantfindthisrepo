package org.scripts.kotlin.content.commands

import com.runelive.model.PlayerRights
import com.runelive.model.options.Option
import com.runelive.model.options.twooption.TwoOption
import com.runelive.model.player.command.Command
import com.runelive.model.player.dialog.Dialog
import com.runelive.model.player.dialog.DialogMessage
import com.runelive.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/7/2016.

 * @author Seba
 */
class Empty(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>, privilege: PlayerRights) {
        player.dialog.sendDialog(EmptyDialog(player))
    }

    private inner class EmptyDialog(player: Player) : Dialog(player) {

        init {
            setEndState(1)
            player.npcClickId = 945
        }

        override fun getMessage(): DialogMessage? {
            when (state) {
                0 -> return Dialog.createNpc("Are you sure you want to delete all the items in your inventory? They will not be dropped to the ground rather permanently deleted.")
                1 -> return Dialog.createOption(object : TwoOption("Yes, delete all my inventory items", "No, I have changed my mind.") {
                    override fun execute(player: Player, option: Option.OptionType) {
                        when (option) {
                            Option.OptionType.OPTION_1_OF_2 -> for (item in player.inventory.items) {
                                player.inventory.delete(item)
                            }
                            Option.OptionType.OPTION_2_OF_2 -> player.packetSender.sendInterfaceRemoval()
                        }
                    }
                })
            }
            return null
        }
    }
}
