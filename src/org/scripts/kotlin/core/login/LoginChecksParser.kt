package org.scripts.kotlin.core.login

import com.chaos.world.content.BankPin
import com.chaos.world.content.PlayerLogs
import com.chaos.world.entity.impl.player.Player

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
        PlayerLogs.connections(player, "Login");
        if (player.bankPinAttributes.hasBankPin()
                && !player.bankPinAttributes.hasEnteredBankPin()
                && player.bankPinAttributes.onDifferent(player)) {
            BankPin.init(player, false)
        }
    }
}
