package org.scripts.kotlin.content.commands

import com.runelive.GameServer
import com.runelive.engine.task.Task
import com.runelive.engine.task.TaskManager
import com.runelive.model.StaffRights
import com.runelive.model.player.command.Command
import com.runelive.util.FilterExecutable
import com.runelive.world.World
import com.runelive.world.content.wells.WellOfGoodness
import com.runelive.world.content.clan.ClanChatManager
import com.runelive.world.content.lottery.LotterySaving
import com.runelive.world.entity.impl.player.Player
import com.runelive.world.content.pos.PlayerOwnedShops

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
                World.executeAll( { player -> player.packetSender.sendSystemUpdate(time) } )
                TaskManager.submit(object : Task(time) {
                    override fun execute() {
                        World.executeAll( { player -> World.deregister(player) } )
                        LotterySaving.save()
                        PlayerOwnedShops.save()
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
