package org.runelive.world.content.combat.instanced

import com.chaos.engine.task.Task
import com.chaos.engine.task.TaskManager
import com.chaos.model.GameObject
import com.chaos.model.Locations.Location
import com.chaos.model.Position
import com.chaos.model.RegionInstance
import com.chaos.model.RegionInstance.RegionInstanceType
import com.chaos.world.World
import com.chaos.world.entity.impl.npc.NPC
import com.chaos.world.entity.impl.player.Player

/**
 * @Author Vados
 */

object InstancedCerberus {

    val NPC_ID = 5866

    @JvmStatic fun enterDungeon(player: Player) {
        if (player.slayer.slayerTask.equals("CERBERUS")) {
            player.packetSender.sendMessage("You must be on a Cerberus slayer task to enter Cerberus's Cave.")
        } else {
            player.moveTo(Position(1240, 1226, player.index * 4))
            player.regionInstance = RegionInstance(player, RegionInstanceType.CERBERUS_CAVE)
            spawnCerberus(player)
        }
    }

    @JvmStatic fun leaveDungeon(player: Player) {
        Location.CERBERUS_CAVE.leave(player)
    }

    @JvmStatic fun handleCerberusDeath(player: Player, n: NPC) {
        if (n.id == NPC_ID) {
            player.spawnedCerberus = false
        }
    }

    fun spawnCerberus(player: Player) {
        TaskManager.submit(object : Task(2, player, false) {
            public override fun execute() {
                if (player.regionInstance == null || !player.isRegistered) {
                    stop()
                    return
                }
                if (!player.spawnedCerberus) {
                    val n = NPC(NPC_ID, Position(1240, 1253, player.position.z)).setSpawnedFor(player)
                    World.register(n)
                    player.spawnedCerberus = true
                    player.regionInstance.npcsList.add(n)
                    n.combatBuilder.attack(player)
                    stop()
                }
            }
        })
    }
}
