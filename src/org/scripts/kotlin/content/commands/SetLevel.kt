package org.scripts.kotlin.content.commands

import com.chaos.model.PlayerRights
import com.chaos.model.Skill
import com.chaos.model.player.command.Command
import com.chaos.world.content.skill.SkillManager
import com.chaos.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class SetLevel(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: PlayerRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::setlevel-skillid-skilllevel")
        } else {
            var skillId = 0
            var level = 0

            try {
                skillId = Integer.parseInt(args[0])
                level = Integer.parseInt(args[1])
            } catch (e: NumberFormatException) {
                player.packetSender.sendMessage("Error parsing arguments.  Use numbers.")
            }

            if (level > 15000) {
                player.packetSender.sendMessage("You can only have a maxmium level of 15000.")
                return
            }
            val skill = Skill.forId(skillId)
            player.skillManager.setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
                    SkillManager.getExperienceForLevel(level))
            player.packetSender.sendMessage("You have set your " + skill!!.getName() + " level to " + level)
        }
    }
}
