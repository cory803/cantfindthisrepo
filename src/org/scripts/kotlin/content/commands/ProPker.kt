package org.scripts.kotlin.content.commands

import com.chaos.model.StaffRights
import com.chaos.model.Skill
import com.chaos.model.player.command.Command
import com.chaos.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class ProPker(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        player.skillManager.setCurrentLevel(Skill.ATTACK, 125, true)
        player.skillManager.setCurrentLevel(Skill.STRENGTH, 145, true)
        player.skillManager.setCurrentLevel(Skill.RANGED, 145, true)
        player.skillManager.setCurrentLevel(Skill.DEFENCE, 140, true)
        player.skillManager.setCurrentLevel(Skill.MAGIC, 120, true)
        player.skillManager.setCurrentLevel(Skill.PRAYER, 99999, true)
    }
}
