package org.scripts.kotlin.content.commands.player

import com.runelive.GameSettings
import com.runelive.model.PlayerRights
import com.runelive.world.World
import com.runelive.world.content.PlayerPunishment
import com.runelive.world.entity.impl.player.Player

/**
 * Created by Dave on 10/07/2016.
 */
class Wiki {

    object WikiEditors {

        /**
         * @Author Jonathan Sirens Initiates Command
         */

        @JvmStatic fun initiate_command(player: Player, command: Array<String>, wholeCommand: String) {
            if (wholeCommand.toLowerCase().startsWith("yell")) {
                if (PlayerPunishment.isMuted(player.username) || PlayerPunishment.isIpMuted(player.hostAddress)) {
                    player.packetSender.sendMessage("You are muted and cannot yell.")
                    return
                }
                if (World.isGlobalYell() == false) {
                    player.packetSender.sendMessage("An admin has temporarily disabled the global yell channel.")
                    return
                }
                if (!GameSettings.YELL_STATUS) {
                    player.packetSender.sendMessage("Yell is currently turned off, please try again in 30 minutes!")
                    return
                }
                 val yellmessage = wholeCommand.substring(4, wholeCommand.length)
                if (player.rights == PlayerRights.WIKI_EDITOR)
                    World.sendYell("<col=0>[<col=ff7f00><shad=0><img=15>Wiki Editor<img=15></shad><col=0>] " + player.username + ": " + yellmessage, player)
                if(player.rights == PlayerRights.WIKI_MANAGER)
                    World.sendYell("<col=0>[<col=31a4ff><shad=0><img=16>Wiki Manager<img=16></shad><col=0>] " + player.username + ": " + yellmessage, player)
            }
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