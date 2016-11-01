package org.scripts.kotlin.content.commands;

import com.chaos.model.StaffRights;
import com.chaos.model.container.impl.Shop;
import com.chaos.model.player.command.Command;
import com.chaos.world.entity.impl.player.Player;

public class Announce extends Command {

    public Announce(StaffRights staffRights) {
        super(staffRights);
    }

    @Override
    public void execute(Player player, String[] args, StaffRights privilege) {
        if (args == null) {
            player.getPacketSender().sendMessage("Example usage: ::announce-time-text");
        } else {
            int time = Integer.parseInt(args[0]);
            String text = args[1];
            if(time >= 10000) {
                player.getPacketSender().sendMessage("There is a max of 10,000 seconds.");
                return;
            }
            player.getPacketSender().sendString(1, "[ANNOUNCE]-"+time+"-"+text);
            player.getPacketSender().sendMessage("You have sent an announcement for "+time+" seconds.");
        }
    }
}
