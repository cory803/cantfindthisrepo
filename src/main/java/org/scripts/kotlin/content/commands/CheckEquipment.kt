package org.scripts.kotlin.content.commands

import com.runelive.model.Flag
import com.runelive.model.StaffRights
import com.runelive.model.container.impl.Equipment
import com.runelive.model.definitions.WeaponAnimations
import com.runelive.model.definitions.WeaponInterfaces
import com.runelive.model.player.command.Command
import com.runelive.world.World
import com.runelive.world.content.BonusManager
import com.runelive.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class CheckEquipment(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
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
