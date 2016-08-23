package org.scripts.kotlin.content.commands

import com.chaos.GameSettings
import com.chaos.model.PlayerRights
import com.chaos.model.player.command.Command
import com.chaos.world.World
import com.chaos.world.content.PlayerPunishment
import com.chaos.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/27/2016.

 * @author Seba
 */
class Yell(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: PlayerRights) {
        if (args == null) {
            player.packetSender.sendMessage("Please use the command as ::yell-message no spaces use -")
            return
        }
        if (!World.isGlobalYell()) {
            player.packetSender.sendMessage("An admin has temporarily disabled the global yell channel.")
            return
        }
        if (PlayerPunishment.isMuted(player.username) || PlayerPunishment.isIpMuted(player.hostAddress)) {
            player.packetSender.sendMessage("You are muted and cannot yell.")
            return
        }
        if (player.isYellMute) {
            player.packetSender.sendMessage("You are muted from yelling and cannot yell.")
            return
        }
        if (!GameSettings.YELL_STATUS) {
            player.packetSender.sendMessage("Yell is currently turned off, please try again in 30 minutes!")
            return
        }
        val yellmessage = args[0]
        if (yellmessage.contains("<img=") || yellmessage.contains("<col=") || yellmessage.contains("<shad=")) {
            player.packetSender.sendMessage("You are not allowed to put these symbols in your yell message.")
            return
        }
        if (yellmessage.contains("hitbox") || yellmessage.contains("HITBOX") || yellmessage.contains(".TV") || yellmessage.contains(".tv") && !player.rights.canStream()) {
            player.packetSender.sendMessage("You are not permitted to advertise streams.")
            return
        }

        val builder = StringBuilder(1024)
        val isStaff = player.rights.isStaff || player.rights == PlayerRights.WIKI_EDITOR || player.rights == PlayerRights.WIKI_MANAGER

        /**
         * Time to build our message
         */
        run {
            if (!isStaff) {
                builder.append("<img=")
                builder.append(player.rights.clientValue)
                builder.append("> ")
            }

            builder.append("<col=0>[<col=")
            builder.append(player.rights.yellColor)
            builder.append(">")

            if (player.rights.shadow != null) {
                builder.append("<shad=")
                builder.append(player.rights.shadow)
                builder.append(">")
            }

            if (isStaff) {
                builder.append("<img=")
                builder.append(player.rights.clientValue)
                builder.append(">")
            }

            if (player.yellTag != "invalid_yell_tag_set") {
                builder.append(player.yellTag)
            } else {
                builder.append(player.rights.rightName.replace(" Donor", ""))
            }

            if (isStaff) {
                builder.append("<img=")
                builder.append(player.rights.clientValue)
                builder.append(">")
            }

            if (player.rights.shadow != null) {
                builder.append("</shad>")
            }

            builder.append("<col=0>] ")
            builder.append(player.username)
            builder.append(": ")
            builder.append(yellmessage)
        }

        World.sendYell(builder.toString(), player)
    }
}
