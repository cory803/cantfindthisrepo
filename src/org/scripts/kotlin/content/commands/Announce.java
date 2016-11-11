package org.scripts.kotlin.content.commands;

import com.chaos.model.StaffRights;
import com.chaos.model.container.impl.Shop;
import com.chaos.model.input.impl.*;
import com.chaos.model.player.command.Command;
import com.chaos.world.World;
import com.chaos.world.entity.impl.player.Player;

public class Announce extends Command {

    public Announce(StaffRights staffRights) {
        super(staffRights);
    }

    @Override
    public void execute(Player player, String[] args, StaffRights privilege) {
        if (args == null) {
            player.getPacketSender().sendMessage("Example usage: ::announce-time");
        } else {
            player.setAnnouncementTime(Integer.parseInt(args[0]));
            player.setInputHandling(new com.chaos.model.input.impl.GlobalAnnouncement());
            player.getPacketSender().sendEnterInputPrompt("Enter a announcement:");
        }
    }
}
