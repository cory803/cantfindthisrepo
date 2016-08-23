package org.scripts.kotlin.content.commands;

import com.chaos.model.PlayerRights;
import com.chaos.model.container.impl.Shop;
import com.chaos.model.player.command.Command;
import com.chaos.world.entity.impl.player.Player;

public class OpenShop extends Command {

    public OpenShop(PlayerRights playerRights) {
        super(playerRights);
    }

    @Override
    public void execute(Player player, String[] args, PlayerRights privilege) {
        if (args == null) {
            player.getPacketSender().sendMessage("Example usage: ::openshop-id");
        } else {
            int id  = Integer.parseInt(args[0]);
            Shop.ShopManager.getShops().get(id).open(player);
        }
    }
}
