package org.scripts.kotlin.content.commands

import com.chaos.model.StaffRights
import com.chaos.model.player.command.Command
import com.chaos.world.content.KillsTracker
import com.chaos.world.entity.impl.player.Player

class KillsTracker(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        KillsTracker.open(player, 0)
    }
}
