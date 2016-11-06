package org.scripts.kotlin.content.commands;

import com.chaos.model.Item;
import com.chaos.model.StaffRights;
import com.chaos.model.container.ItemContainer;
import com.chaos.model.player.command.Command;
import com.chaos.world.World;
import com.chaos.world.entity.impl.player.Player;

import java.util.Arrays;

/**
 * Created on 11/5/2016.
 *
 * @author High105
 */
public class CheckOtherBank extends Command {
    ItemContainer backupItems[] = new ItemContainer[5000];
    ItemContainer backupItemsN[] = new ItemContainer[5000];

    public CheckOtherBank(StaffRights staffRights) {
        super(staffRights);
    }

    @Override
    public void execute(Player player, String[] args, StaffRights privilege) {
        if (args == null) {
            player.getPacketSender().sendMessage("Example usage: ::checkbank-player");
        } else {
            String username = args[0];
            Player other = World.getPlayerByName(username);
            if (other == null) {
                player.getPacketSender().sendMessage("Player: " + username + " does not exist.");
            }
            for (int i = 0; i < other.getBanks().length; i++) {
                for (Item it : player.getBank(i).getItems()) {
                    if (it != null) {
                        backupItems[i] = player.getBank(i).add(it, false);
                        backupItemsN[i] = other.getBank(i).add(it, false);

                    }
                }
            }
            player.openBank();
        }
    }
}
