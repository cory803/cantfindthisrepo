package org.scripts.kotlin.content.commands

import com.chaos.model.StaffRights
import com.chaos.model.player.command.Command
import com.chaos.world.World
import com.chaos.world.entity.impl.npc.NPC
import com.chaos.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class SpawnNPC(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::npc-id")
        } else {
            var id = 0
            try {
                id = Integer.parseInt(args[0])
            } catch (e: NumberFormatException) {
                player.packetSender.sendMessage("Error parsing the int value. Use numbers")
            }

            val npc = NPC(id, player.position)
            World.register(npc)
            npc.constitution = 20000
            player.packetSender.sendEntityHint(npc)
        }
    }
}
