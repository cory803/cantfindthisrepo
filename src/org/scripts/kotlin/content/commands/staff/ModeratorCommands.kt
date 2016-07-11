package org.scripts.kotlin.content.commands.staff

import com.runelive.model.Locations
import com.runelive.util.Misc
import com.runelive.world.World
import com.runelive.world.content.PlayerPunishment
import com.runelive.world.content.transportation.TeleportHandler
import com.runelive.world.content.transportation.TeleportType
import com.runelive.world.entity.impl.player.Player
import com.runelive.world.entity.impl.player.PlayerSaving

/**
 * Created by Dave on 10/07/2016.
 */
class ModeratorCommands {

    object  Moderators {

        /**
         * @Author Jonathan Sirens Initiates Command
         */

       @JvmStatic fun initiate_command(player: Player, command: Array<String>, wholeCommand: String) {
            if (command[0].equals("massban", ignoreCase = true)) {
                val victimUsername = wholeCommand.substring(8)
                PlayerSaving.accountExists(victimUsername) { rs ->
                    if (rs.next()) {
                        // account exists
                        val other = World.getPlayerByName(victimUsername)
                        if (other == null) {
                            PlayerPunishment.massBan(player, victimUsername, Player(null))
                        } else {
                            val address = other.macAddress.toString()
                            val ip = other.hostAddress
                            PlayerPunishment.ban(victimUsername)
                            PlayerPunishment.pcBan(address)
                            PlayerPunishment.ipBan(ip)
                            World.deregister(other)
                            player.packetSender.sendMessage("You successfully mass banned '$victimUsername'.")
                        }
                    } else {
                        player.packetSender.sendMessage("Player $victimUsername does not exist.")
                    }
                }
            }
            if (command[0] == "gold") {
                val p = World.getPlayerByName(wholeCommand.substring(5))
                if (p != null) {
                    var gold: Long = 0
                    for (item in p.inventory.items) {
                        if (item != null && item.id > 0 && item.tradeable())
                            gold += item.definition.value.toLong()
                    }
                    for (item in p.equipment.items) {
                        if (item != null && item.id > 0 && item.tradeable())
                            gold += item.definition.value.toLong()
                    }
                    for (i in 0..8) {
                        for (item in p.getBank(i).items) {
                            if (item != null && item.id > 0 && item.tradeable())
                                gold += item.definition.value.toLong()
                        }
                    }
                    gold += p.moneyInPouch
                    player.packetSender.sendMessage(
                            p.username + " has " + Misc.insertCommasToNumber(gold.toString()) + " coins.")
                } else
                    player.packetSender.sendMessage("Can not find player online.")
            }
            if (command[0].equals("unmassban", ignoreCase = true)) {
                val victimUsername = wholeCommand.substring(10)
                PlayerSaving.accountExists(victimUsername) { rs ->
                    if (rs.next()) {
                        // account exists
                        val other = World.getPlayerByName(victimUsername)
                        if (other == null) {
                            PlayerPunishment.unmassBan(player, victimUsername, Player(null))
                        } else {
                            val address = other.macAddress.toString()
                            val ip = other.hostAddress
                            PlayerPunishment.unBan(victimUsername)
                            PlayerPunishment.unPcBan(address)
                            PlayerPunishment.unIpBan(ip)
                            World.deregister(other)
                            player.packetSender.sendMessage("You successfully unmass banned '$victimUsername'.")
                        }
                    } else {
                        player.packetSender.sendMessage("Player $victimUsername does not exist.")
                    }
                }
            }
            if (command[0].equals("teletome", ignoreCase = true)) {
                val playerToTele = wholeCommand.substring(9)
                val player2 = World.getPlayerByName(playerToTele)
                if (World.getPlayerByName(playerToTele)!!.location === Locations.Location.DUEL_ARENA) {
                    player.packetSender.sendMessage("Why are you trying to move a player out of duel arena?")
                    return
                }
                if (player2.location === Locations.Location.DUNGEONEERING) {
                    player.packetSender.sendMessage("You cannot teleport a player out of dung?")
                    return
                }
                if (player.location === Locations.Location.WILDERNESS) {
                    player.packetSender.sendMessage("You cannot teleport a player into the wild... What're you thinking?")
                    return
                }
                if (player2.location === Locations.Location.DUEL_ARENA) {
                    player.packetSender.sendMessage("You cannot do this to someone in duel arena.")
                    return
                }
                val canTele = TeleportHandler.checkReqs(player, player2.position.copy())
                        && player.regionInstance == null && player2.regionInstance == null
                if (canTele) {
                    TeleportHandler.teleportPlayer(player2, player.position.copy(), TeleportType.NORMAL)
                    player.packetSender.sendMessage("Teleporting player to you: " + player2.username + "")
                    player2.packetSender.sendMessage("You're being teleported to " + player.username + "...")
                } else
                    player.packetSender.sendMessage(
                            "You can not teleport that player at the moment. Maybe you or they are in a minigame?")
            }
            if (wholeCommand.startsWith("silenceyell")) {
                val yellmute = wholeCommand.substring(12)
                val punishee = World.getPlayerByName(yellmute)
                PlayerSaving.accountExists(yellmute) { rs ->
                    if (rs.next()) {
                        // account exists
                        punishee.isYellMute = true
                        punishee.packetSender.sendMessage("You have been yell muted! Please appeal on the forums.")
                        player.packetSender.sendMessage("Player " + punishee.username + " was successfully muted!")
                    } else {
                        player.packetSender.sendMessage("Player $yellmute does not exist.")
                    }
                }
            }
            if (wholeCommand.startsWith("unsilenceyell")) {
                val yellmute = wholeCommand.substring(14)
                val punishee = World.getPlayerByName(yellmute)
                PlayerSaving.accountExists(yellmute) { rs ->
                    if (rs.next()) {
                        // account exists
                        punishee.isYellMute = false
                        punishee.packetSender.sendMessage("You have been granted your yell ability again.")
                        player.packetSender.sendMessage("Player " + punishee.username + " was successfully unmuted!")
                    } else {
                        player.packetSender.sendMessage("Player $yellmute does not exist.")
                    }
                }
            }
            if (command[0].equals("ban", ignoreCase = true)) {
                val ban_player = wholeCommand.substring(4)
                PlayerSaving.accountExists(ban_player) { rs ->
                    if (rs.next()) {
                        // account exists
                        if (PlayerPunishment.isPlayerBanned(ban_player)) {
                            player.packetSender.sendMessage("Player $ban_player already has an active ban.")
                            return@accountExists
                        }
                        val other = World.getPlayerByName(ban_player)
                        PlayerPunishment.ban(ban_player)
                        if (other != null) {
                            World.deregister(other)
                        }
                        player.packetSender.sendMessage("Player $ban_player was successfully banned!")
                    } else {
                        player.packetSender.sendMessage("Player $ban_player does not exist.")
                    }
                }
            }
            if (command[0].equals("ipmute", ignoreCase = true)) {
                val mute_player = wholeCommand.substring(7)
                PlayerSaving.accountExists(mute_player) { rs ->
                    if (rs.next()) {
                        // account exists
                        if (PlayerPunishment.isIpMuted(mute_player)) {
                            player.packetSender.sendMessage("Player $mute_player already has an active ip mute.")
                            return@accountExists
                        }
                        val other = World.getPlayerByName(mute_player)
                        PlayerPunishment.ipMute(mute_player)
                        player.packetSender.sendMessage("Player $mute_player was successfully ip muted!")
                        other.packetSender.sendMessage("You have been ip muted! Please appeal on the forums.")
                    } else {
                        player.packetSender.sendMessage("Player $mute_player does not exist.")
                    }
                }
            }
            if (command[0].equals("unipmute", ignoreCase = true)) {
                val mute_player = wholeCommand.substring(9)
                PlayerSaving.accountExists(mute_player) { rs ->
                    if (rs.next()) {
                        // account exists
                        if (!PlayerPunishment.isIpMuted(mute_player)) {
                            player.packetSender.sendMessage("Player $mute_player does not have an active ip mute!")
                            return@accountExists
                        }
                        val other = World.getPlayerByName(mute_player)
                        PlayerPunishment.unIpMute(mute_player)
                        player.packetSender.sendMessage("Player $mute_player was successfully unipmuted!")
                        other.packetSender.sendMessage("You have been unipmuted!")
                    } else {
                        player.packetSender.sendMessage("Player $mute_player does not exist.")
                    }
                }
            }
            if (command[0].equals("unban", ignoreCase = true)) {
                val ban_player = wholeCommand.substring(6)
                PlayerSaving.accountExists(ban_player) { rs ->
                    if (rs.next()) {
                        // account exists
                        if (!PlayerPunishment.isPlayerBanned(ban_player)) {
                            player.packetSender.sendMessage("Player $ban_player is not banned.")
                            return@accountExists
                        }
                        PlayerPunishment.unBan(ban_player)
                        player.packetSender.sendMessage("Player $ban_player was successfully unbanned.")
                    } else {
                        player.packetSender.sendMessage("Player $ban_player does not exist.")
                    }
                }
            }
            if (command[0] == "unbanvote") {
                val vote_player = wholeCommand.substring(10)
                PlayerSaving.accountExists(vote_player) { rs ->
                    if (rs.next()) {
                        // account exists
                        if (!PlayerPunishment.isVoteBanned(vote_player)) {
                            player.packetSender.sendMessage("Player $vote_player is not vote banned.")
                            return@accountExists
                        }
                        val other = World.getPlayerByName(vote_player)
                        PlayerPunishment.unVoteBan(vote_player)
                        other.packetSender.sendMessage("You have been unbanned from voting.")
                        player.packetSender.sendMessage("You have unbanned " + other.username + " from voting.")
                    } else {
                        player.packetSender.sendMessage("Player $vote_player does not exist.")
                    }
                }
            }
            if (command[0].equals("banvote", ignoreCase = true)) {
                val vote_player = wholeCommand.substring(8)
                PlayerSaving.accountExists(vote_player) { rs ->
                    if (rs.next()) {
                        // account exists
                        if (PlayerPunishment.isVoteBanned(vote_player)) {
                            player.packetSender.sendMessage("Player $vote_player already has an active vote ban.")
                            return@accountExists
                        }
                        val other = World.getPlayerByName(vote_player)
                        PlayerPunishment.voteBan(vote_player)
                        other.packetSender.sendMessage("You have been banned from voting.")
                        player.packetSender.sendMessage("You have banned " + other.username + " from voting.")
                    } else {
                        player.packetSender.sendMessage("Player $vote_player does not exist.")
                    }
                }
            }
        }
    }

    object GlobalModerators {

        @JvmStatic fun initiate_command(player: Player, command: Array<String>, wholeCommand: String) {

            }
        }
    }
