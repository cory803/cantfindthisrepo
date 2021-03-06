package org.scripts.kotlin.content.commands

import com.runelive.model.StaffRights
import com.runelive.model.options.Option
import com.runelive.model.options.twooption.TwoOption
import com.runelive.model.player.command.Command
import com.runelive.util.Misc
import com.runelive.world.World
import com.runelive.world.content.PlayerPunishment
import com.runelive.world.entity.impl.player.Player
import com.runelive.world.entity.impl.player.PlayerSaving

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class YellMute(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::yellmute-playername-time-timeunit")
            player.packetSender.sendMessage("Time units are M for minutes, H for hours, D for days.")
            return
        }
        val victim = args[0]
        if (args.size == 1) {
            player.optionContainer.display(object : TwoOption("Yes, permanent yell mute " + victim, "No, I want to time yell mute this player.") {
                override fun execute(player: Player, option: Option.OptionType) {
                    if (option == Option.OptionType.OPTION_1_OF_2) {
                        mutePlayer(player, victim, -1)
                    } else if (option == Option.OptionType.OPTION_2_OF_2) {
                        player.packetSender.sendMessage("Example usage: ::yellmute-playername-time-timeunit")
                        player.packetSender.sendMessage("Time units are M for minutes, H for hours, D for days.")
                    }
                }
            })
        } else {
            var time = -1
            try {
                time = Integer.parseInt(args[1])
            } catch (e: NumberFormatException) {
                player.packetSender.sendMessage("There was an error parsing your time argument. Try using numbers!")
            }

            val u = args[2].toLowerCase()
            val unit = u[0]

            var calculatedTime = 0

            when (unit) {
                'm' -> calculatedTime = time * Misc.MINUTE
                'h' -> calculatedTime = time * Misc.HOUR
                'd' -> calculatedTime = time * Misc.DAY
            }

            calculatedTime += System.currentTimeMillis().toInt()

            mutePlayer(player, victim, calculatedTime.toLong())
        }
    }

    /**
     * Process our mute
     */
    private fun mutePlayer(player: Player, victim: String, time: Long) {
        player.packetSender.sendInterfaceRemoval()
        val v = World.getPlayerByName(victim)
        if (v == null) {
            PlayerSaving.accountExists(victim) { rs ->
                if (rs.next()) {
                    if (PlayerPunishment.isYellMuted(victim)) {
                        player.packetSender.sendMessage("Player $victim already has an active yell mute.")
                        return@accountExists
                    }
                    PlayerPunishment.yellMute(victim, time)
                    player.packetSender.sendMessage(victim + " was successfully yell muted.")
                } else {
                    player.packetSender.sendMessage("Player $victim does not exist.")
                }
            }
        } else {
            if (PlayerPunishment.isYellMuted(victim)) {
                player.packetSender.sendMessage("Player $victim already has an active yell mute.")
                return
            }
            PlayerPunishment.yellMute(victim, time)
            player.packetSender.sendMessage(victim + " was successfully yell muted.")
            v.packetSender.sendMessage("You have been yell muted!")
            v.packetSender.sendMessage("If you feel this is unjustified you can appeal on the forums.")
        }
    }
}
