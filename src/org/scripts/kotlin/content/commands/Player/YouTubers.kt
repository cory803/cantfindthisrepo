package org.scripts.kotlin.content.commands.player

import com.runelive.model.Locations.Location
import com.runelive.world.entity.impl.player.Player

/**
 * Dummy Text
 */

object YouTubers {

    /**
     * @Author Jonathan Sirens Initiates Command
     */

    @JvmStatic fun initiate_command(player: Player, command: Array<String>, wholeCommand: String) {
        if (command[0] == "bank") {
            if (player.location === Location.WILDERNESS) {
                player.packetSender.sendMessage("You can't do this inside the wilderness!")
                return
            }
            if (player.combatBuilder.isBeingAttacked || player.combatBuilder.isAttacking) {
                player.packetSender.sendMessage("You cannot use this command while in combat!")
                return
            }
            player.getBank(player.currentBankTab).open()
            player.packetSender.sendMessage("You have opened your bank!")
        }
        if (player.isJailed) {
            player.packetSender.sendMessage("You cannot use commands in jail... You're in jail.")
            return
        }
    }

}