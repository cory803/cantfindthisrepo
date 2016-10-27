package org.scripts.kotlin.content.commands

import com.chaos.GameSettings
import com.chaos.model.Locations
import com.chaos.model.MagicSpellbook
import com.chaos.model.StaffRights
import com.chaos.model.player.command.Command
import com.chaos.world.content.Achievements
import com.chaos.world.content.combat.magic.Autocasting
import com.chaos.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/27/2016.

 * @author Seba
 */
class EnableLunar(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (player.location != null && player.location === Locations.Location.WILDERNESS) {
            player.packetSender.sendMessage("You cannot do this at the moment.")
            return
        }
        player.spellbook = MagicSpellbook.LUNAR
        player.packetSender.sendTabInterface(GameSettings.MAGIC_TAB, player.spellbook.interfaceId).sendMessage("Your magic spellbook is changed to lunars..")
        Autocasting.resetAutocast(player, true)
        Achievements.finishAchievement(player, Achievements.AchievementData.SWITCH_SPELLBOOK)
    }
}
