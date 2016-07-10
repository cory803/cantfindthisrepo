package org.scripts.kotlin.content.commands.staff

import com.runelive.GameSettings
import com.runelive.model.Locations.Location
import com.runelive.model.Position
import com.runelive.util.Misc
import com.runelive.world.World
import com.runelive.world.content.AccountTools
import com.runelive.world.content.PlayerPunishment
import com.runelive.world.content.transportation.TeleportHandler
import com.runelive.world.content.transportation.TeleportType
import com.runelive.world.entity.impl.player.Player
import com.runelive.world.entity.impl.player.PlayerSaving

object ServerSupports {

    /**
     * @Author Jonathan Sirens Initiates Command
     */

    @JvmStatic fun initiate_command(player: Player, command: Array<String>, wholeCommand: String) {
        if (wholeCommand.startsWith("unjail")) {
            val jail_punishee = wholeCommand.substring(7)
            val punishee = World.getPlayerByName(jail_punishee)
            punishee.isJailed = false
            punishee.forceChat("Im free!!! I'm finally out of jail... Hooray!")
            punishee.moveTo(Position(3087, 3502, 0))
        }
        if (command[0] == "scan") {
            val victimUsername = wholeCommand.substring(5)
            PlayerSaving.accountExists(victimUsername) { rs ->
                if (rs.next()) {
                    // account exists
                    val other = World.getPlayerByName(victimUsername)
                    if (other == null) {
                        AccountTools.scan(player, victimUsername, Player(null))
                    } else {
                        AccountTools.outScan(player, victimUsername, other.macAddress.toString(), other)
                    }
                } else {
                    player.packetSender.sendMessage("Player $victimUsername does not exist.")
                }
            }
        }
        if (wholeCommand.startsWith("jail")) {
            val jail_punishee = wholeCommand.substring(5)
            val punishee = World.getPlayerByName(jail_punishee)
            PlayerSaving.accountExists(jail_punishee) { rs ->
                if (rs.next()) {
                    // account exists

                    val cellAmounts = Misc.getRandom(1)
                    when (cellAmounts) {
                        1 -> {
                            punishee.isJailed = true
                            punishee.moveTo(Position(1969, 5011, 0))
                        }
                        2 -> {
                            punishee.isJailed = true
                            punishee.moveTo(Position(1969, 5008, 0))
                        }
                        3 -> {
                            punishee.isJailed = true
                            punishee.moveTo(Position(1969, 5005, 0))
                        }
                        4 -> {
                            punishee.isJailed = true
                            punishee.moveTo(Position(1969, 5002, 0))
                        }
                        5 -> {
                            punishee.isJailed = true
                            punishee.moveTo(Position(1969, 4999, 0))
                        }
                        6 -> {
                            punishee.isJailed = true
                            punishee.moveTo(Position(1980, 5011, 0))
                        }
                        7 -> {
                            punishee.isJailed = true
                            punishee.moveTo(Position(1980, 5008, 0))
                        }
                        8 -> {
                            punishee.isJailed = true
                            punishee.moveTo(Position(1980, 5005, 0))
                        }
                        9 -> {
                            punishee.isJailed = true
                            punishee.moveTo(Position(1980, 5002, 0))
                        }
                        10 -> {
                            punishee.isJailed = true
                            punishee.moveTo(Position(1980, 4999, 0))
                        }
                    }
                } else {
                    player.packetSender.sendMessage("Player $jail_punishee does not exist.")
                }
            }
        }
        if (command[0].equals("mute", ignoreCase = true)) {
            val mute_player = wholeCommand.substring(5)
            PlayerSaving.accountExists(mute_player) { rs ->
                if (rs.next()) {
                    // account exists
                    if (PlayerPunishment.isMuted(mute_player)) {
                        player.packetSender.sendMessage("Player $mute_player already has an active mute.")
                        return@accountExists
                    }
                    val other = World.getPlayerByName(mute_player)
                    PlayerPunishment.mute(mute_player)
                    player.packetSender.sendMessage("Player $mute_player was successfully muted!")
                    other.packetSender.sendMessage("You have been muted! Please appeal on the forums.")
                    return@accountExists
                } else {
                    player.packetSender.sendMessage("Player $mute_player does not exist.")
                }
            }
        }
        if (command[0].equals("unmute", ignoreCase = true)) {
            val mute_player = wholeCommand.substring(7)
            PlayerSaving.accountExists(mute_player) { rs ->
                if (rs.next()) {
                    if (!PlayerPunishment.isMuted(mute_player)) {
                        player.packetSender.sendMessage("Player $mute_player is not muted.")
                        return@accountExists
                    }
                    val other = World.getPlayerByName(mute_player)
                    PlayerPunishment.unMute(mute_player)
                    player.packetSender.sendMessage("Player $mute_player was successfully unmuted!")
                    other.packetSender.sendMessage("You have been unmuted!")
                    return@accountExists
                } else {
                    player.packetSender.sendMessage("Player $mute_player does not exist.")
                }
            }
        }
        if (command[0].equals("staffzone")) {
                TeleportHandler.teleportPlayer(player, Position(2846, 5147), TeleportType.NORMAL)
        }
        if (command[0].equals("teleto", ignoreCase = true)) {
            val playerToTele = wholeCommand.substring(7)
            val player2 = World.getPlayerByName(playerToTele)
            if (player2 == null) {
                player.packetSender.sendMessage("Cannot find that player online..")
                return
            } else {
                val canTele = TeleportHandler.checkReqs(player, player2.position.copy())
                        && player.regionInstance == null && player2.regionInstance == null
                if (canTele && player.location !== Location.DUNGEONEERING) {
                    TeleportHandler.teleportPlayer(player, player2.position.copy(), TeleportType.NORMAL)
                    player.packetSender.sendMessage("Teleporting to player: " + player2.username + "")
                } else {
                    if (player2.location === Location.DUNGEONEERING) {
                        player.packetSender.sendMessage("You can not teleport to this player while they are dungeoneering.")
                    } else {
                        player.packetSender.sendMessage("You can not teleport to this player at the moment. Minigame maybe?")
                    }
                }
            }
        }
        if (command[0].equals("movehome", ignoreCase = true)) {
            var player2 = command[1]
            player2 = Misc.formatText(player2.replace("_".toRegex(), " "))
            if (command.size >= 3 && command[2] != null)
                player2 += " " + Misc.formatText(command[2].replace("_".toRegex(), " "))
            if (World.getPlayerByName(player2)!!.location === Location.DUEL_ARENA) {
                player.packetSender.sendMessage("Why are you trying to move a player out of duel arena?")
                return
            }
            if (player.location === Location.WILDERNESS) {
                player.packetSender.sendMessage("You cannot move yourself out of the wild.")
                return
            }
            val playerToMove = World.getPlayerByName(player2)
            if (playerToMove!!.location === Location.DUNGEONEERING) {
                player.packetSender.sendMessage("You cannot move someone out of dung.")
                return
            }
            if (playerToMove!!.location === Location.DUEL_ARENA) {
                player.packetSender.sendMessage("You cannot do this to someone in duel arena.")
                return
            }
            if (playerToMove != null) {
                playerToMove.moveTo(GameSettings.DEFAULT_POSITION_VARROCK.copy())
                playerToMove.packetSender.sendMessage("You've been teleported home by " + player.username + ".")
                player.packetSender.sendMessage("Sucessfully moved " + playerToMove.username + " to home.")
            }
        }
        if (command[0].equals("kick", ignoreCase = true)) {
            val player2 = wholeCommand.substring(5)
            val playerToKick = World.getPlayerByName(player2)
            if (World.getPlayerByName(player2)!!.location === Location.DUEL_ARENA) {
                player.packetSender.sendMessage("Why are you trying to move a player out of duel arena?")
                return
            }
            if (playerToKick.location === Location.DUEL_ARENA) {
                player.packetSender.sendMessage("You cannot do this to someone in duel arena.")
                return
            }
            if (playerToKick.location !== Location.WILDERNESS) {
                playerToKick.forceOffline = true
                World.deregister(playerToKick)
                player.packetSender.sendMessage("Kicked " + playerToKick.username + ".")
            }
        }
    }

}