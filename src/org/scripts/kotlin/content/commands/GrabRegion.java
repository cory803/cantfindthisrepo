package org.scripts.kotlin.content.commands;

import com.chaos.ect.dropwriting.Drop;
import com.chaos.ect.dropwriting.DropManager;
import com.chaos.ect.dropwriting.DropTable;
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
public class GrabRegion extends Command {

    public GrabRegion(StaffRights staffRights) {
        super(staffRights);
    }

    @Override
    public void execute(Player player, String[] args, StaffRights privilege) {
        //Grabs a region id based on where you are standing.
        int regionX = player.getPosition().getX() >> 3;
        int regionY = player.getPosition().getY() >> 3;
        int regionId = ((regionX / 8) << 8) + (regionY / 8);
        player.getPacketSender().sendMessage("Region id: "+regionId);
    }
}
