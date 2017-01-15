package org.scripts.kotlin.content.commands

import com.runelive.GameSettings
import com.runelive.model.Locations
import com.runelive.model.StaffRights
import com.runelive.model.player.command.Command
import com.runelive.world.entity.impl.player.Player

/**
 * Created by Affliction on 10/01/2017.
 */

class Rules(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (player.location == Locations.Location.WILDERNESS) {
            player.packetSender.sendMessage("This wouldn't be wise to open your browser in the wild!")
            return
        }
        player.packetSender.sendString(1, "http://rune.live/forums/index.php?/topic/1178-official-chaos-rules/")
        player.packetSender.sendMessage("Attempting to open: Official server rules")
    }
}
