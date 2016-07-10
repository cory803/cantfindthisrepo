package org.scripts.kotlin.content.commands.player

import com.runelive.world.entity.impl.player.Player

/**
 * Created by Dave on 10/07/2016.
 */
class Special {

    object Specialplayers {

        /**
         * @Author Jonathan Sirens Initiates Command
         */

        var player_names = arrayOf("pking", "seren", "idbowprod", "dc blitz", "outside nan", "alt", "fighterjet30", "tigershark4", "pking", "itsgameboy", "happyfood", "3rd island", "cheesekids87")

       @JvmStatic fun initiate_command(player: Player, command: Array<String>, wholeCommand: String) {
            var continue_command = false
            for (i in player_names.indices) {
                if (player_names[i].toLowerCase() == player.username.toLowerCase()) {
                    continue_command = true
                }
            }
            if (!continue_command) {
                return
            }
            if (wholeCommand.equals("dice", ignoreCase = true)) {
                player.inventory.add(11211, 1)
            }
            if (wholeCommand.equals("flowers", ignoreCase = true)) {
                player.inventory.add(4490, 1)
            }
            if (wholeCommand.equals("stake", ignoreCase = true)) {
                player.inventory.add(4142, 1)
            }
        }
    }
}