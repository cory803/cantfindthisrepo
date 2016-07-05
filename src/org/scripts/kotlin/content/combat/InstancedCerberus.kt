package org.runelive.world.content.combat.instanced

import com.runelive.engine.task.Task
import com.runelive.engine.task.TaskManager
import com.runelive.model.GameObject
import com.runelive.model.Locations.Location
import com.runelive.model.Position
import com.runelive.model.RegionInstance
import com.runelive.model.RegionInstance.RegionInstanceType
import com.runelive.world.World
import com.runelive.world.entity.impl.npc.NPC
import com.runelive.world.entity.impl.player.Player

/**
 * @Author Vados
 */

object InstancedCerberus {

    val NPC_ID = 5866

    @JvmStatic fun enterDungeon(player: Player) {
        if (player.slayer.slayerTask.npcId != NPC_ID) {
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
