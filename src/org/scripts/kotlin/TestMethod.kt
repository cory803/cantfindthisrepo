package org.scripts.kotlin

import com.runelive.world.entity.impl.player.Player;

/**
 * Created by Dave on 01/07/2016.
 */
class TestMethod {

    companion object TestMethod {
        fun testMethod(player: Player) {
            player.packetSender.sendMessage("This is an example class for kotlin.");
            player.packetSender.sendMessage("Do you see this?");
            player.packetSender.sendMessage("This works.");
        }
    }
}