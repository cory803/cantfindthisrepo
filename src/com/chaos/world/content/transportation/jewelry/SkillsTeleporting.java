package com.chaos.world.content.transportation.jewelry;

import com.chaos.model.Item;
import com.chaos.model.Position;
import com.chaos.model.container.impl.Equipment;
import com.chaos.world.content.transportation.TeleportHandler;
import com.chaos.world.content.transportation.TeleportType;
import com.chaos.world.entity.impl.player.Player;

public class SkillsTeleporting {

    public static void rub(Player player, int item) {
        if (player.getInterfaceId() > 0)
            player.getPacketSender().sendInterfaceRemoval();
        player.setSelectedSkillingItem(item);
    }

    public static void teleport(Player player, Position location) {
        if (!TeleportHandler.checkReqs(player, location, true)) {
            return;
        }
        if (!player.getClickDelay().elapsed(4500) || player.getWalkingQueue().isLockMovement())
            return;
        int pItem = player.getSelectedSkillingItem();
        if (!player.getInventory().contains(pItem) && !player.getEquipment().contains(pItem))
            return;
        boolean inventory = !player.getEquipment().contains(pItem);
        if (pItem >= 11105 && pItem <= 11111) {
            int newItem = (pItem + 2);
            if (inventory) {
                player.getInventory().delete(pItem, 1).add(newItem, 1).refreshItems();
            } else {
                player.getEquipment().delete(player.getEquipment().getItems()[Equipment.AMULET_SLOT]);
                player.getEquipment().setItem(Equipment.AMULET_SLOT, new Item(newItem, 1));
                player.getEquipment().refreshItems();
            }
            if (newItem == 11113) {
                player.getPacketSender().sendMessage("Your necklace has run out of charges.");
            }
        }
        player.setSelectedSkillingItem(-1);
        player.getPacketSender().sendInterfaceRemoval();
        TeleportHandler.teleportPlayer(player, location, TeleportType.JEWELRY_TELE);
    }
}
