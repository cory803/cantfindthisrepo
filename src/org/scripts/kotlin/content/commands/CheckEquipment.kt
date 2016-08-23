package org.scripts.kotlin.content.commands

import com.chaos.model.Flag
import com.chaos.model.PlayerRights
import com.chaos.model.container.impl.Equipment
import com.chaos.model.definitions.WeaponAnimations
import com.chaos.model.definitions.WeaponInterfaces
import com.chaos.model.player.command.Command
import com.chaos.world.World
import com.chaos.world.content.BonusManager
import com.chaos.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class CheckEquipment(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: PlayerRights) {
        if (args == null) {
            player.packetSender.sendMessage("Exmaple usage: ::checkequip-playername")
        } else {
            val player2 = World.getPlayerByName(args[0])
            if (player2 == null) {
                player.packetSender.sendMessage("That player is not online.")
                return
            }
            player.equipment.setItems(player2.equipment.copiedItems).refreshItems()
            WeaponInterfaces.assign(player, player.equipment.get(Equipment.WEAPON_SLOT))
            WeaponAnimations.assign(player, player.equipment.get(Equipment.WEAPON_SLOT))
            BonusManager.update(player)
            player.updateFlag.flag(Flag.APPEARANCE)
        }
    }
}
