package org.scripts.kotlin.content.commands

import com.chaos.model.Item
import com.chaos.model.PlayerRights
import com.chaos.model.player.command.Command
import com.chaos.world.World
import com.chaos.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class GiveItem(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: PlayerRights) {
        if (args == null) {
            player.packetSender.sendMessage("Exmaple usage: ::giveitem-playername-itemid-amount")
        } else if (args.size == 3) {
            val give = World.getPlayerByName(args[0])
            if (give == null) {
                player.packetSender.sendMessage("That player is not online")
                return
            }
            var itemId = 0
            var amount = 0

            try {
                itemId = Integer.parseInt(args[1])
                amount = Integer.parseInt(args[2])
            } catch (e: NumberFormatException) {
                player.packetSender.sendMessage("Error parsing your int arguments.  Use numbers")
            }

            val item = Item(itemId, amount)
            give.inventory.add(item)
            give.packetSender.sendMessage("You have been given " + amount + " " + item.definition.getName())
            player.packetSender.sendMessage("You have given " + give.username + " " + amount + " " + item.definition.getName())
        }
    }
}
