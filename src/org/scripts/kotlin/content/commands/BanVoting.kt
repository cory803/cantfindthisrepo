package org.scripts.kotlin.content.commands

import com.chaos.model.StaffRights
import com.chaos.model.options.Option
import com.chaos.model.options.twooption.TwoOption
import com.chaos.model.player.command.Command
import com.chaos.util.Misc
import com.chaos.world.World
import com.chaos.world.content.PlayerPunishment
import com.chaos.world.entity.impl.player.Player
import com.chaos.world.entity.impl.player.PlayerSaving

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class BanVoting(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::banvote-playername-time-timeunit")
            player.packetSender.sendMessage("Time units are M for minutes, H for hours, D for days.")
        } else {
            val victim = args[0]
            if (args.size == 1) {
                player.optionContainer.display(object : TwoOption("Yes permanently vote ban " + victim, "No, I want to apply a timed vote ban.") {
                    override fun execute(player: Player, option: Option.OptionType) {
                        when (option) {
                            Option.OptionType.OPTION_1_OF_2 -> handleVoteBan(player, victim, -1L)
                            Option.OptionType.OPTION_2_OF_2 -> {
                                player.packetSender.sendMessage("Example usage: ::banvote-playername-time-timeunit")
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

                handleVoteBan(player, victim, calculatedTime)
            }
        }
    }

    private fun handleVoteBan(player: Player, victim: String, time: Long?) {
        PlayerSaving.accountExists(victim) { rs ->
            if (rs.next()) {
                if (PlayerPunishment.isVoteBanned(victim)) {
                    player.packetSender.sendMessage("That player is already vote banned")
                    return@accountExists
                }
                val p = World.getPlayerByName(victim)
                PlayerPunishment.voteBan(victim, time)
                player.packetSender.sendMessage("You have successfully vote banned " + victim)
                if (p != null) {
                    p.packetSender.sendMessage("You have been banned from voting.")
                }
            } else {
                player.packetSender.sendMessage("The account $victim does not exist.")
            }
        }
    }
}
