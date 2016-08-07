package org.scripts.kotlin.content.commands

import com.runelive.GameServer
import com.runelive.engine.task.Task
import com.runelive.engine.task.TaskManager
import com.runelive.model.PlayerRights
import com.runelive.model.player.command.Command
import com.runelive.util.FilterExecutable
import com.runelive.world.World
import com.runelive.world.content.Scoreboard
import com.runelive.world.content.WellOfGoodwill
import com.runelive.world.content.clan.ClanChatManager
import com.runelive.world.content.grandexchange.GrandExchangeOffers
import com.runelive.world.content.pos.PlayerOwnedShops
import com.runelive.world.content.skill.impl.dungeoneering.Dungeoneering
import com.runelive.world.entity.impl.player.Player

import java.io.IOException

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class UpdateServer(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: PlayerRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::update-time")
        } else {
            var t = 0
            try {
                t = Integer.parseInt(args[0])
            } catch (e: NumberFormatException) {
                player.packetSender.sendMessage("Error parsing your time argument. Try to use numbers.")
            }

            val time = t
            if (time > 0) {
                GameServer.setUpdating(true)
                World.executeAll(object : FilterExecutable<Player>() {
                    override fun execute(player: Player) {
                        player.packetSender.sendSystemUpdate(time)
                        if (Dungeoneering.doingDungeoneering(player)) {
                            Dungeoneering.leave(player, false, true)
                            player.clickDelay.reset()
                            player.packetSender.sendMessage("You have been forced out of your dungeon due to an update, sorry!")
                        }
                    }
                })
                TaskManager.submit(object : Task(time) {
                    override fun execute() {
                        World.executeAll(object : FilterExecutable<Player>() {
                            override fun execute(player: Player) {
                                World.deregister(player)
                            }
                        })
                        WellOfGoodwill.save()
                        GrandExchangeOffers.save()
                        PlayerOwnedShops.saveShops()
                        try {
                            Scoreboard.save()
                        } catch (e: IOException) {

                        }

                        ClanChatManager.save()
                        GameServer.getLogger().info("Update task finished!")
                        stop()
                    }
                })
            }
        }
    }
}
