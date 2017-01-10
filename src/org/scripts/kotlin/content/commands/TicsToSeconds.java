package org.scripts.kotlin.content.commands;

import com.runelive.model.StaffRights;
import com.runelive.model.player.command.Command;
import com.runelive.util.Misc;
import com.runelive.world.entity.impl.player.Player;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/28/2016.
 *
 * @author Seba
 */
public class TicsToSeconds extends Command {

    public TicsToSeconds(StaffRights staffRights) {
        super(staffRights);
    }

    @Override
    public void execute(Player player, String[] args, StaffRights privilege) {
        if(args.length == 0) {
            player.getPacketSender().sendMessage("You must use the command as ::convert-number");
            return;
        }
        int x = Integer.parseInt(args[0]);
        player.getPacketSender().sendMessage("X ticks converted to: "+Misc.ticksToSeconds(x) + " seconds.");
    }
}
