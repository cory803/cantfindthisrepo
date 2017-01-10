package com.runelive.world.content.transportation.jewelry;

import com.runelive.model.Item;
import com.runelive.model.Position;
import com.runelive.model.container.impl.Equipment;
import com.runelive.world.content.transportation.TeleportHandler;
import com.runelive.world.content.transportation.TeleportType;
import com.runelive.world.entity.impl.player.Player;
import org.scripts.kotlin.content.dialog.teleports.jewlery.Jewelery;

public class GloryTeleporting {

	public static void rub(Player player, int item) {
		if (player.getInterfaceId() > 0)
			player.getPacketSender().sendInterfaceRemoval();
		player.setSelectedSkillingItem(item);
		player.getDialog().sendDialog(new Jewelery(player));
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
		if (pItem >= 1706 && pItem <= 1712) {
			int newItem = (pItem - 2);
			if (inventory) {
				player.getInventory().delete(pItem, 1).add(newItem, 1).refreshItems();
			} else {
				player.getEquipment().delete(player.getEquipment().getItems()[Equipment.AMULET_SLOT]);
				player.getEquipment().setItem(Equipment.AMULET_SLOT, new Item(newItem, 1));
				player.getEquipment().refreshItems();
			}
			if (newItem == 1704) {
				player.getPacketSender().sendMessage("Your amulet has run out of charges.");
			}
		}
		player.setSelectedSkillingItem(-1);
		player.getPacketSender().sendInterfaceRemoval();
		TeleportHandler.teleportPlayer(player, location, TeleportType.JEWELRY_TELE);
	}
}
