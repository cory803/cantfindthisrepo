package org.scripts.kotlin.core.login

import com.chaos.model.Locations
import com.chaos.world.content.Achievements
import com.chaos.world.content.PlayerPanel
import com.chaos.world.content.clan.ClanChatManager
import com.chaos.world.entity.impl.player.Player

/**
 * Created by Dave on 01/07/2016.
 */

class LoginLoaderAssetts {

    companion object LoginLoaderAssetts {
        fun loadAssetts(player : Player) {
            if (player.pointsHandler.achievementPoints > Achievements.AchievementData.values().size) {
                player.pointsHandler.setAchievementPoints(Achievements.AchievementData.values().size, false)
            }
            Locations.login(player)
            player.getPacketSender().sendString(1, "[CLEAR]")
            ClanChatManager.handleLogin(player)
            PlayerPanel.refreshPanel(player)
        }
    }
}
