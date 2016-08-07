package org.scripts.kotlin.content.commands

import com.runelive.model.Flag
import com.runelive.model.PlayerRights
import com.runelive.model.Skill
import com.runelive.model.player.command.Command
import com.runelive.world.content.skill.SkillManager
import com.runelive.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class Master(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: PlayerRights) {
        for (skill in Skill.values()) {
            val level = SkillManager.getMaxAchievingLevel(skill)
            player.skillManager.setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
                    SkillManager.getExperienceForLevel(if (level == 120) 120 else 99))
        }
        player.packetSender.sendMessage("You are now a master of all skills.")
        player.updateFlag.flag(Flag.APPEARANCE)
    }
}
