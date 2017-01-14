package org.scripts.kotlin.content.commands;

import com.runelive.model.StaffRights;
import com.runelive.model.player.command.Command;
import com.runelive.world.entity.impl.player.Player;

/**
 * Created on 1/13/2017.
 *
 * @author High105
 */
public class EmptyToggle extends Command {

    public EmptyToggle(StaffRights staffRights) {
        super(staffRights);
    }

    @Override
    public void execute(Player player, String[] args, StaffRights privilege) {
        if (args == null) {
            player.setEmptytoggle(!player.getEmptyToggle());
            if (!player.getEmptyToggle()) {
                player.getPacketSender().sendMessage("You have turned your empty warning on. There is now a warning");
            } else {
                player.getPacketSender().sendMessage("You have turned your empty toggle warning off. There is no longer a warning");
            }
        }
    }
}
