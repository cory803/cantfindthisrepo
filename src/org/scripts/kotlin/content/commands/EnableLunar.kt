package org.scripts.kotlin.content.commands

import com.runelive.GameSettings
import com.runelive.model.Locations
import com.runelive.model.MagicSpellbook
import com.runelive.model.PlayerRights
import com.runelive.model.player.command.Command
import com.runelive.world.content.combat.magic.Autocasting
import com.runelive.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/27/2016.

 * @author Seba
 */
class EnableLunar(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>, privilege: PlayerRights) {
        if (player.location != null && player.location === Locations.Location.WILDERNESS) {
            player.packetSender.sendMessage("You cannot do this at the moment.")
            return
        }
        player.spellbook = MagicSpellbook.LUNAR
        player.packetSender.sendTabInterface(GameSettings.MAGIC_TAB, player.spellbook.interfaceId).sendMessage("Your magic spellbook is changed to lunars..")
        Autocasting.resetAutocast(player, true)
    }
}
