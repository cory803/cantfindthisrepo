package org.scripts.kotlin.core.login

import com.runelive.net.serverlogs.ServerLogs
import com.runelive.net.serverlogs.impl.Connection
import com.runelive.world.content.BankPin
import com.runelive.world.content.pos.PlayerOwnedShops
import com.runelive.world.entity.impl.player.Player

/**
 * Created by Dave on 05/07/2016.
 */

object LoginChecksParser {

    @JvmStatic fun checkLogin(player: Player) {
        /**
         * We order our checks in importance.
         * We log the login
         * We check for pin
         * We collect their coins.
         */
        ServerLogs.submit(Connection(player, "Login"))
        if (player.bankPinAttributes.hasBankPin()
                && !player.bankPinAttributes.hasEnteredBankPin()
                && player.bankPinAttributes.onDifferent(player)) {
            BankPin.init(player, false)
        }
        PlayerOwnedShops.collectCoinsOnLogin(player)
    }
}
