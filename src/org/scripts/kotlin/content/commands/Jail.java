package org.scripts.kotlin.content.commands;

import com.runelive.model.PlayerRights;
import com.runelive.model.player.command.Command;
import com.runelive.util.Misc;
import com.runelive.world.World;
import com.runelive.world.entity.impl.player.Player;
import com.runelive.world.entity.impl.player.PlayerSaving;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/28/2016.
 *
 * @author Seba
 */
public class Jail extends Command {

    public Jail(PlayerRights playerRights) {
        super(playerRights);
    }

    @Override
    public void execute(Player player, String[] args, PlayerRights privilege) {
        if (args.length != 1) {
            player.getPacketSender().sendMessage("Please use the command as ::jail-playername");
            return;
        }
        Player punishee = World.getPlayerByName(args[0]);
        /*PlayerSaving.accountExists(args[0], rs -> {
            if (rs.next()) {
                // account exists

                int cellAmounts = Misc.getRandom(1);
                switch (cellAmounts) {

                }
                when (cellAmounts) {
                    1 -> {
                        punishee.isJailed = true
                        punishee.moveTo(Position(1969, 5011, 0))
                    }
                    2 -> {
                        punishee.isJailed = true
                        punishee.moveTo(Position(1969, 5008, 0))
                    }
                    3 -> {
                        punishee.isJailed = true
                        punishee.moveTo(Position(1969, 5005, 0))
                    }
                    4 -> {
                        punishee.isJailed = true
                        punishee.moveTo(Position(1969, 5002, 0))
                    }
                    5 -> {
                        punishee.isJailed = true
                        punishee.moveTo(Position(1969, 4999, 0))
                    }
                    6 -> {
                        punishee.isJailed = true
                        punishee.moveTo(Position(1980, 5011, 0))
                    }
                    7 -> {
                        punishee.isJailed = true
                        punishee.moveTo(Position(1980, 5008, 0))
                    }
                    8 -> {
                        punishee.isJailed = true
                        punishee.moveTo(Position(1980, 5005, 0))
                    }
                    9 -> {
                        punishee.isJailed = true
                        punishee.moveTo(Position(1980, 5002, 0))
                    }
                    10 -> {
                        punishee.isJailed = true
                        punishee.moveTo(Position(1980, 4999, 0))
                    }
                }
            } else {
                player.packetSender.sendMessage("Player $jail_punishee does not exist.")
            }
        });*/

    }
}
