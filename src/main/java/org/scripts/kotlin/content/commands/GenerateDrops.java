package org.scripts.kotlin.content.commands;

import com.runelive.model.StaffRights;
import com.runelive.model.player.command.Command;
import com.runelive.world.entity.impl.player.Player;

/**
 * Initiates the command ::generatedrops-amount-npc id
 * @author Seba
 */
public class GenerateDrops extends Command {

    public GenerateDrops(StaffRights staffRights) {
        super(staffRights);
    }

    @Override
    public void execute(Player player, String[] args, StaffRights privilege) {
        int npcId = Integer.parseInt(args[1]);
        int amount = Integer.parseInt(args[0]);
        player.getDropGenerator().generate(player, amount, npcId);
    }
}
