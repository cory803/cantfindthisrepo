package org.scripts.kotlin.content.commands

import com.chaos.GameSettings
import com.chaos.model.Locations
import com.chaos.model.PlayerRights
import com.chaos.model.Store
import com.chaos.model.player.command.Command
import com.chaos.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/27/2016.

 * @author Seba
 */
class ClaimStorePurchase(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: PlayerRights) {
        if (!GameSettings.STORE_CONNECTIONS) {
            player.packetSender.sendMessage("The store is currently offline! Try again in 30 minutes.")
            return
        }
        if (player.location === Locations.Location.DUNGEONEERING || player.location === Locations.Location.WILDERNESS) {
            player.packetSender.sendMessage("You cannot do this here, you'll lose your scroll!")
            return
        }
        if (player.claimingStoreItems) {
            player.packetSender.sendMessage("You already have a active store claim process going...")
            return
        }
        player.packetSender.sendMessage("Checking for any store purchases...")
        Store.claimItem(player)
    }
}
