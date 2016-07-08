package org.scripts.kotlin.core.login

import com.runelive.world.entity.impl.player.Player;
import com.runelive.GameSettings
import com.runelive.world.content.WellOfGoodwill
import java.util.concurrent.TimeUnit

/**
 * Created by Dave on 01/07/2016.
 */

class LoginMessageParser {
    companion object LoginMessageParser {
        fun sendLogin(player: Player) {
            if (GameSettings.DOUBLE_EXP) {
                player.getPacketSender().sendMessage(
                        "@bla@Welcome to RuneLive! We're currently in Double EXP mode! (@red@X2.0@bla@)")
            } else if (GameSettings.INSANE_EXP) {
                player.getPacketSender().sendMessage(
                        "@bla@Welcome to RuneLive! We're currently in Insane EXP mode! (@red@X8.0@bla@)")
            } else {
                player.getPacketSender().sendMessage(
                        "@bla@Welcome to RuneLive! We're currently in Normal EXP mode! (@red@X1.0@bla@)")
            }
            if (player.getHomeLocation() == 0 && player.showHomeOnLogin()) {
                player.getPacketSender().sendMessage("@blu@Your home location is set to: @dre@Varrock@blu@.")
            } else if (player.getHomeLocation() == 1 && player.showHomeOnLogin()) {
                player.getPacketSender().sendMessage("@blu@Your home location is set to: @dre@Edgeville@blu@.")
            }
            val days = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - player.getLastLogin())

            if (player.getLastIpAddress() != null && player.showIpAddress()) {
                if (days > 0) {
                    player.getPacketSender().sendMessage("You last logged in @blu@" + days + "@bla@ "
                            + (if (days > 1) "days" else "day") + " ago from @blu@" + player.getLastIpAddress())
                } else {
                    player.getPacketSender().sendMessage("You last logged in earlier today from @blu@" + player.getLastIpAddress())
                }
            } else if ((player.getLastIpAddress() != null && !player.showIpAddress())) {
                if (days > 0) {
                    player.getPacketSender().sendMessage("You last logged in @blu@" + days + "@bla@ "
                            + (if (days > 1) "days" else "day") + " ago.")
                } else {
                    player.getPacketSender().sendMessage("You last logged in earlier today.")
                }
            }
            if (player.experienceLocked())
                player.getPacketSender().sendMessage("@red@Warning: your experience is currently locked.")

            if (GameSettings.DOUBLE_POINTS) {
                player.getPacketSender().sendMessage(
                        "<img=4> <col=008FB2>RuneLive currently has a double points event going on, make sure to use it!")
            }

            if (GameSettings.DOUBLE_VOTE_TOKENS) {
                player.getPacketSender().sendMessage(
                        "<img=4> <col=008FB2>RuneLive currently has a double vote rewards event going on, make sure to use it!")
            }

            if (GameSettings.TRIPLE_VOTE_TOKENS) {
                player.getPacketSender().sendMessage(
                        "<img=4> <col=008FB2>RuneLive currently has a triple vote rewards event going on, make sure to use it!")
            }

            if (WellOfGoodwill.isActive()) {
                if (player.getDonorRights() > 0) {
                    player.getPacketSender().sendMessage(
                            "<img=4> <col=008FB2>The Well of Goodwill is granting 50% bonus experience for another "
                                    + WellOfGoodwill.getMinutesRemaining() + " minutes.")
                } else {
                    player.getPacketSender().sendMessage(
                            "<img=4> <col=008FB2>The Well of Goodwill is granting 30% bonus experience for another "
                                    + WellOfGoodwill.getMinutesRemaining() + " minutes.")
                }
            }
        }
    }
}