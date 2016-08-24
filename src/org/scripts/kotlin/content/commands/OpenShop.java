package org.scripts.kotlin.content.commands;

import com.chaos.model.StaffRights;
import com.chaos.model.container.impl.Shop;
import com.chaos.model.player.command.Command;
import com.chaos.world.entity.impl.player.Player;

public class OpenShop extends Command {

    public OpenShop(StaffRights staffRights) {
        super(staffRights);
    }

    @Override
    public void execute(Player player, String[] args, StaffRights privilege) {
        if (args == null) {
            player.getPacketSender().sendMessage("Example usage: ::openshop-id");
        } else {
            int id  = Integer.parseInt(args[0]);
            Shop.ShopManager.getShops().get(id).open(player);
        }
    }
}
