package org.scripts.kotlin.content.commands

import com.runelive.model.StaffRights
import com.runelive.model.player.command.Command
import com.runelive.world.content.KillsTracker
import com.runelive.world.entity.impl.player.Player

class KillsTracker(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        KillsTracker.open(player, 0)
    }
}
