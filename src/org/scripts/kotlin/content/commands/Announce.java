package org.scripts.kotlin.content.commands;

import com.runelive.model.StaffRights;
import com.runelive.model.player.command.Command;
import com.runelive.world.entity.impl.player.Player;

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
            player.setInputHandling(new com.runelive.model.input.impl.GlobalAnnouncement());
            player.getPacketSender().sendEnterInputPrompt("Enter a announcement:");
        }
    }
}
