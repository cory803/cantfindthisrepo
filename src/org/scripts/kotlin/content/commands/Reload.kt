package org.scripts.kotlin.content.commands

import com.runelive.model.PlayerRights
import com.runelive.model.container.impl.Shop
import com.runelive.model.definitions.ItemDefinition
import com.runelive.model.npc.drops.LootSystem
import com.runelive.model.player.command.Command
import com.runelive.util.FilterExecutable
import com.runelive.world.World
import com.runelive.world.entity.impl.npc.NPC
import com.runelive.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class Reload(playerRights: PlayerRights) : Command(playerRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: PlayerRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::reload-object")
            player.packetSender.sendMessage("Objects: npcs, drops, commands, itemdef")
        } else {
            val `object` = args[0].toLowerCase()

            when (`object`) {
                "npcs" -> {
                    World.executeNpcs(object : FilterExecutable<NPC>() {
                        override fun execute(npc: NPC) {
                            World.deregister(npc)
                        }
                    })
                    NPC.init()
                    player.packetSender.sendMessage("Successfully reloaded npcs.")
                }

                "drops" -> {
                    LootSystem.loadDropTables()
                    player.packetSender.sendMessage("Successfully reloaded drops")
                }

                "commands" -> {
                }

                "itemdef" -> {
                    ItemDefinition.init()
                    player.packetSender.sendMessage("Successfully reloaded item defs.")
                }
                "shops" -> {
                    Shop.ShopManager.parseShops().load()
                    player.packetSender.sendMessage("Successfully reloaded shops.")
                }
            }
        }
    }
}
