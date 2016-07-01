package com.scripts.kotlin.content.skills.thieving;

import com.runelive.model.input.Input
import com.runelive.world.entity.impl.player.Player
import org.scripts.kotlin.content.skills.thieving.Stall

class ThievBots : Input() {

    override fun handleSyntax(player: Player, syntax: String) {
        player.packetSender.sendInterfaceRemoval()
        if (Stall.botStop == 1) {
            if (syntax.equals("ikov2", ignoreCase = true)) {
                player.packetSender.sendMessage("You have successfully completed the random.")
                player.isPassedRandom = true
            } else {
                player.packetSender.sendMessage("That was not the right answer, please try again.")
                player.isPassedRandom = false
                // player.getPacketSender().sendLogout();
            }
        } else if (Stall.botStop == 2) {
            if (syntax.equals("15", ignoreCase = true)) {
                player.packetSender.sendMessage("You have successfully completed the random.")
                player.isPassedRandom = true
            } else {
                player.packetSender.sendMessage("That was not the right answer, please try again.")
                player.isPassedRandom = false
                // player.getPacketSender().sendLogout();
            }
        } else if (Stall.botStop == 3) {
            if (syntax.equals("no", ignoreCase = true)) {
                player.packetSender.sendMessage("You have successfully completed the random.")
                player.isPassedRandom = true
            } else {
                player.packetSender.sendMessage("That was not the right answer, please try again.")
                player.isPassedRandom = false
                // player.getPacketSender().sendLogout();
            }
        }
    }
}
