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
