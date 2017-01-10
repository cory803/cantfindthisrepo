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
class Ban(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::ban-playername-time-timeunit")
            player.packetSender.sendMessage("Time units are M for minutes, H for hours, D for days.")
        } else {
            val victim = args[0]
            if (args.size == 1) {
                player.optionContainer.display(object : TwoOption("Yes permanently ban " + victim, "No, I want to apply a timed ban.") {
                    override fun execute(player: Player, option: Option.OptionType) {
                        when (option) {
                            Option.OptionType.OPTION_1_OF_2 -> handleBan(player, victim, -1L)
                            Option.OptionType.OPTION_2_OF_2 -> {
                                player.packetSender.sendMessage("Example usage: ::ban-playername-time-timeunit")
                                player.packetSender.sendMessage("Time units are M for minutes, H for hours, D for days.")
                            }
                        }
                    }
                })
            } else {
                var time = 0

                try {
                    time = Integer.parseInt(args[1])
                } catch (e: NumberFormatException) {
                    player.packetSender.sendMessage("There was an error parsing your time argument. Please use numbers")
                }

                val u = args[2].toLowerCase()
                val unit = u[0]

                var calculatedTime: Long = 0

                when (unit) {
                    'm' -> calculatedTime = (time * Misc.MINUTE).toLong()
                    'h' -> calculatedTime = (time * Misc.HOUR).toLong()
                    'd' -> calculatedTime = (time * Misc.DAY).toLong()
                }

                calculatedTime += System.currentTimeMillis()

                handleBan(player, victim, calculatedTime)
            }
        }
    }

    private fun handleBan(player: Player, victim: String, time: Long?) {
        player.packetSender.sendInterfaceRemoval()
        PlayerSaving.accountExists(victim) { rs ->
            if (rs.next()) {
                if (PlayerPunishment.isPlayerBanned(victim)) {
                    player.packetSender.sendMessage("That player is already banned")
                    return@accountExists
                }
                val p = World.getPlayerByName(victim)
                PlayerPunishment.ban(victim, time)
                if (p != null) {
                    World.deregister(p)
                }
                player.packetSender.sendMessage("You have successfully banned " + victim)
            } else {
                player.packetSender.sendMessage("The account $victim does not exist.")
            }
        }
    }
}
