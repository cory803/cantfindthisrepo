package org.scripts.kotlin.content.commands.player

import com.runelive.GameSettings
import com.runelive.model.PlayerRights
import com.runelive.world.World
import com.runelive.world.content.PlayerPunishment
import com.runelive.world.entity.impl.player.Player


class Wiki {

    object WikiEditors {

        /**
         * @Author Jonathan Sirens Initiates Command
         */

        @JvmStatic fun initiate_command(player: Player, command: Array<String>, wholeCommand: String) {
        }
    }

    object WikiManagers {

        /**
         * @Author Jonathan Sirens Initiates Command
         */

        @JvmStatic fun initiate_command(player: Player, command: Array<String>, wholeCommand: String) {
            val other_player_name = "invalid_name_process"
            when (command[0].toLowerCase()) {
                "promote" -> {
                    val target = World.getPlayerByName(wholeCommand.substring(8))
                    if (target!!.rights == PlayerRights.PLAYER) {
                        if (target == null) {
                            player.packetSender.sendMessage("Player must be online to give them wiki editor.")
                        } else {
                            target.rights = PlayerRights.WIKI_EDITOR
                            target.packetSender.sendMessage("You have been given Wiki Editor!")
                            player.packetSender.sendMessage("You have given " + wholeCommand.substring(8) + " the rank Wiki Editor!")
                            target.packetSender.sendRights()
                        }
                    } else {
                        player.packetSender.sendMessage("You can't promote someone who already has a rank.")
                    }
                }
                "demote" -> {
                    val target2 = World.getPlayerByName(wholeCommand.substring(7))
                    if (target2!!.rights == PlayerRights.WIKI_EDITOR) {
                        if (target2 == null) {
                            player.packetSender.sendMessage("Player must be online to demote them!")
                        } else {
                            target2.rights = PlayerRights.PLAYER
                            target2.packetSender.sendMessage("Your Wiki Editor rank has been taken.")
                            player.packetSender.sendMessage(
                                    "You have demoted " + wholeCommand.substring(7) + " from the rank Wiki Editor.")
                            target2.packetSender.sendRights()
                        }
                    } else {
                        player.packetSender.sendMessage("This player isn't on the Wiki Team, or their above you!.")
                    }
                }
            }
        }
    }
}