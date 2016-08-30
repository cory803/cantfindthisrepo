package org.scripts.kotlin.content.commands;

import com.chaos.model.StaffRights;
import com.chaos.model.player.command.Command;
import com.chaos.world.entity.impl.player.Player;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/28/2016.
 *
 * @author Seba
 */
public class TestingCommand extends Command {

    public TestingCommand(StaffRights staffRights) {
        super(staffRights);
    }

    @Override
    public void execute(Player player, String[] args, StaffRights privilege) {
        /**
         * Lets just send the person the message they pass threw so example ::test-send this message back to the player.
         */
        if (args == null || args.length != 1) {
            player.getPacketSender().sendMessage("Please use the command like ::test-message");
            return;
        }

        player.getPacketSender().sendMessage(args[0]);
    }
}
