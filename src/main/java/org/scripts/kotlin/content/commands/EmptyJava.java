package org.scripts.kotlin.content.commands;

import com.runelive.model.Item;
import com.runelive.model.StaffRights;
import com.runelive.model.player.command.Command;
import com.runelive.world.entity.impl.player.Player;
import org.scripts.kotlin.content.dialog.EmptyDialog;

/**
 * Created on 1/13/2017
 *
 * @author High105
 */
public class EmptyJava extends Command {

    public EmptyJava(StaffRights staffRights) {
        super(staffRights);
    }

    @Override
    public void execute(Player player, String[] args, StaffRights privilege) {
        if (args == null) {
            if (player.getEmptyToggle()) {
                for (Item i : player.getInventory().getItems()) {
                    player.getInventory().delete(i);
                }
                player.getPacketSender().sendMessage("You have emptied all of the items in your inventory.");
            } else {
                player.getDialog().sendDialog(new EmptyDialog(player));
            }
        }
    }
}
