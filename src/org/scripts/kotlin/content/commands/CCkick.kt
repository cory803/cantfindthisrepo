package org.scripts.kotlin.content.commands

import com.runelive.model.Locations
import com.runelive.model.StaffRights
import com.runelive.model.player.command.Command
import com.runelive.world.World
import com.runelive.world.content.clan.ClanChatManager
import com.runelive.world.entity.impl.player.Player

class CCkick(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::cckick-playername")
        } else {
            val kick = World.getPlayerByName(args[0])
            if (kick == null) {
                player.packetSender.sendMessage("We are unable to find that player.")
                return
            }
            if (kick.currentClanChat == null) {
                player.packetSender.sendMessage("You cannot kick someone who isn't in a clanchat")
                return
            }
            if (kick.staffRights.isStaff == null) {
                player.packetSender.sendMessage("You cannot kick another staff member.")
                return
            }
            if (kick.currentClanChat.ownerName == kick.username) {
                player.packetSender.sendMessage("You cannot kick someone who is in their own clanchat.")
                return
            }
            ClanChatManager.kick(player, kick)
            player.packetSender.sendMessage("You have successfully kicked " + kick.username + ".")
        }
    }
}
