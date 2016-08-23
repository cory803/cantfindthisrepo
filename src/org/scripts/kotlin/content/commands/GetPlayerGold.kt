package org.scripts.kotlin.content.commands

import com.chaos.model.PlayerRights
import com.chaos.model.player.command.Command
import com.chaos.util.Misc
import com.chaos.world.World
import com.chaos.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class GetPlayerGold(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: PlayerRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::gold-playername")
        } else {
            val p = World.getPlayerByName(args[0])
            if (p == null) {
                player.packetSender.sendMessage("That player is not online.")
            }

            var gold: Long = 0

            for (item in p!!.inventory.items) {
                if (item != null && item.id > 0 && item.tradeable()) {
                    gold += item.definition.value.toLong()
                }
            }

            for (item in p.equipment.items) {
                if (item != null && item.id > 0 && item.tradeable()) {
                    gold += item.definition.value.toLong()
                }
            }

            for (i in 0..8) {
                for (item in p.getBank(i).items) {
                    if (item != null && item.id > 0 && item.tradeable()) {
                        gold += item.definition.value.toLong()
                    }
                }
            }

            if(p.summoning.familiar != null && p.summoning.familiar.summonNpc != null && p.summoning.beastOfBurden != null) {
                for (item in p.summoning.beastOfBurden.items) {
                    if (item != null && item.id > 0 && item.tradeable()) {
                        gold += item.definition.value.toLong()
                    }
                }
            }

            gold += p.moneyInPouch

            player.packetSender.sendMessage(p.username + " has " + Misc.insertCommasToNumber(gold.toString()) + " coins.")
        }
    }
}
