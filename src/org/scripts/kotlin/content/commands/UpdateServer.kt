package org.scripts.kotlin.content.commands

import com.chaos.GameServer
import com.chaos.engine.task.Task
import com.chaos.engine.task.TaskManager
import com.chaos.model.StaffRights
import com.chaos.model.player.command.Command
import com.chaos.util.FilterExecutable
import com.chaos.world.World
import com.chaos.world.content.Well.WellOfGoodness
import com.chaos.world.content.clan.ClanChatManager
import com.chaos.world.entity.impl.player.Player

import java.io.IOException

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class UpdateServer(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
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
                    }
                })
                TaskManager.submit(object : Task(time) {
                    override fun execute() {
                        World.executeAll(object : FilterExecutable<Player>() {
                            override fun execute(player: Player) {
                                World.deregister(player)
                            }
                        })
                        ClanChatManager.save()
                        WellOfGoodness.save()
                        GameServer.getLogger().info("Update task finished!")
                        stop()
                    }
                })
            }
        }
    }
}
