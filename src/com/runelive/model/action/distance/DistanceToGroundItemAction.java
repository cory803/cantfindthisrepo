package com.runelive.model.action.distance;

import com.runelive.executable.Executable;
import com.runelive.model.GroundItem;
import com.runelive.model.Item;
import com.runelive.model.Position;
import com.runelive.model.action.PlayerAction;
import com.runelive.model.definitions.ItemDefinition;
import com.runelive.world.World;
import com.runelive.world.entity.impl.GroundItemManager;
import com.runelive.world.entity.impl.player.Player;

public final class DistanceToGroundItemAction extends PlayerAction {
	private final GroundItem groundItem;

	public DistanceToGroundItemAction(Player player, GroundItem groundItem) {
		super(player);
		this.groundItem = groundItem;
	}

	@Override
	public ActionPolicy getActionPolicy() {
		return ActionPolicy.CLEAR;
	}

	@Override
	public int execute() {
		if (groundItem  == null) {
			return Executable.STOP;
		}
		Position position = player.getPosition();
		if (!position.equals(groundItem.getPosition()) && !itemOnObject()) {
			return 1;
		}
		Item item = groundItem.getItem();
		boolean canPickup = player.getInventory().getFreeSlots() > 0
				|| (player.getInventory().getFreeSlots() == 0 && ItemDefinition.forId(item.getId()).isStackable()
				&& player.getInventory().contains(item.getId()));
		if (!canPickup) {
			player.getInventory().full();
			return Executable.STOP;
		}
		if(!groundItem.getItem().tradeable() && !groundItem.getOwner().equals(player.getUsername())) {
			player.getPacketSender().sendMessage("You cannot pickup other player's untradable items.");
			return Executable.STOP;
		}

		if (player.getInventory().getAmount(groundItem.getItem().getId())
				+ groundItem.getItem().getAmount() > Integer.MAX_VALUE
				|| player.getInventory().getAmount(groundItem.getItem().getId())
				+ groundItem.getItem().getAmount() <= 0) {
			player.getPacketSender()
					.sendMessage("You cannot hold that amount of this item. Clear your inventory!");
			return Executable.STOP;
		}
		GroundItemManager.pickupGroundItem(player, new Item(item.getId()),
				new Position(groundItem.getPosition().getX(), groundItem.getPosition().getY(), player.getPosition().getZ()));
		return Executable.STOP;
	}

    private boolean itemOnObject() {
        return World.tileBlocked(groundItem.getPosition()) && groundItem.getPosition().distanceTo(player.getPosition()) == 1;
    }

}
