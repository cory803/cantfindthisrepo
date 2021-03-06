package org.scripts.kotlin.content.commands

import com.runelive.model.StaffRights
import com.runelive.model.Skill
import com.runelive.model.player.command.Command
import com.runelive.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class GodMode(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        player.skillManager.setCurrentLevel(Skill.ATTACK, 99999, true)
        player.skillManager.setCurrentLevel(Skill.STRENGTH, 99999, true)
        player.skillManager.setCurrentLevel(Skill.RANGED, 99999, true)
        player.skillManager.setCurrentLevel(Skill.DEFENCE, 99999, true)
        player.skillManager.setCurrentLevel(Skill.MAGIC, 99999, true)
        player.skillManager.setCurrentLevel(Skill.CONSTITUTION, 99999, true)
        player.skillManager.setCurrentLevel(Skill.PRAYER, 99999, true)
    }
}
