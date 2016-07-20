package org.scripts.kotlin.content.commands.player

import com.runelive.GameSettings
import com.runelive.model.Locations.Location
import com.runelive.model.PlayerRights
import com.runelive.model.player.YellInfo
import com.runelive.world.World
import com.runelive.world.content.PlayerPunishment
import com.runelive.world.entity.impl.player.Player

/**
 * Dummy Text
 */

object YouTubers {

    /**
     * @Author Jonathan Sirens Initiates Command
     */

    @JvmStatic fun initiate_command(player: Player, command: Array<String>, wholeCommand: String) {
        if (command[0] == "bank") {
            if (player.location === Location.WILDERNESS) {
                player.packetSender.sendMessage("You can't do this inside the wilderness!")
                return
            }
            if (player.combatBuilder.isBeingAttacked || player.combatBuilder.isAttacking) {
                player.packetSender.sendMessage("You cannot use this command while in combat!")
                return
            }
            player.getBank(player.currentBankTab).open()
            player.packetSender.sendMessage("You have opened your bank!")
        }
        if (player.isJailed) {
            player.packetSender.sendMessage("You cannot use commands in jail... You're in jail.")
            return
        }
        if (wholeCommand.toLowerCase().startsWith("yell")) {
            if (World.isGlobalYell() == false) {
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
            val yellmessage = wholeCommand.substring(4, wholeCommand.length)
            if (yellmessage.contains("<img=") || yellmessage.contains("<col=") || yellmessage.contains("<shad=")) {
                player.packetSender.sendMessage("You are not allowed to put these symbols in your yell message.")
                return
            }
            if (player.yellTag != "invalid_yell_tag_set") {
                World.sendYell("<img=" + YellInfo.getRankInfo(player).imageId + "> <col=0>[<col=" + YellInfo.getRankInfo(player).colorCode + "><shad=0>" + player.yellTag + "<col=0></shad>] "
                        + player.username + ": " + yellmessage, player)
                player.yellTimer.reset()
                return
            }
            if(player.rights == PlayerRights.YOUTUBER) {
                World.sendYell("<img=5> <col=0>[<col=ff0000><shad=620000>YouTuber</shad><col=0>] " + player.username + ": " + yellmessage, player)
                return
            }
            if(player.rights == PlayerRights.SUPPORT) {
                World.sendYell("<col=0>[<col=589fe1><shad=0><img=4>Support<img=4></shad><col=0>] " + player.username + ": " + yellmessage, player)
                return
            }
            if(player.rights == PlayerRights.MODERATOR) {
                World.sendYell("<col=0>[<col=31a4ff><shad=0><img=1>Moderator<img=1></shad><col=0>] " + player.getUsername() + ": " + yellmessage, player);
                return
            }
            if(player.rights == PlayerRights.GLOBAL_MOD) {
                World.sendYell("<col=0>[<col=00ff00><shad=0><img=6>Global Mod<img=6></shad><col=0>] " + player.username + ": " + yellmessage, player)
                return
            }
            if(player.rights == PlayerRights.ADMINISTRATOR) {
                World.sendYell("<col=0>[<col=ffff00><shad=0><img=14>Administrator<img=14></shad><col=0>] " + player.username + ": " + yellmessage, player)
                return
            }
            if(player.rights == PlayerRights.MANAGER) {
                World.sendYell("<col=0>[<col=ff0000><shad=2C0000><img=3>Manager<img=3></shad><col=0>] " + player.username + ": " + yellmessage, player)
                return
            }
            if(player.rights == PlayerRights.OWNER) {
                World.sendYell("<col=0>[<col=ff0000><shad=0><img=3>Owner<img=3></shad><col=0>] " + player.username + ": " + yellmessage, player)
                return
            }
            /* Error: PlayerRights.DEVELOPER does not exist
            if(player.rights == PlayerRights.DEVELOPER) {
                World.sendYell("<col=0>[<col=484192><shad=0><img=18>Developer<img=18></shad><col=0>] " + player.username + ": " + yellmessage, player)
                return
            }
            */
            if (player.gameModeAssistant.isIronMan) {
                World.sendYell("<img=12> [<col=808080><shad=0>Ironman</col></shad>] " + player.username + ": "
                        + yellmessage, player)
                return
            }
            if (player.donorRights == 1) {
                World.sendYell("<img=7> <col=0>[<col=ff0000>Donator<col=0>] " + player.username + ": " + yellmessage, player)
            }
            if (player.donorRights == 2) {
                World.sendYell("<img=8> <col=0>[@blu@Super@bla@] " + player.username + ": " + yellmessage, player)
            }
            if (player.donorRights == 3) {
                World.sendYell("<img=9> <col=0>[<col=2FAC45>Extreme<col=0>] " + player.username + ": " + yellmessage, player)
            }
            if (player.donorRights == 4) {
                World.sendYell("<img=10> <col=0>[<col=3E0069>Legendary<col=0>] " + player.username + ": " + yellmessage, player)
            }
            if (player.donorRights == 5) {
                World.sendYell("<img=11> <col=0>[<col=ffff00><shad=0>Uber</shad><col=0>] " + player.username + ": " + yellmessage, player)
            }
        }

    }

}