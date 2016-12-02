package com.chaos.net.packet.impl;

import com.chaos.GameSettings;
import com.chaos.model.GroundItem;
import com.chaos.model.Item;
import com.chaos.model.Position;
import com.chaos.model.action.distance.DistanceToGroundItemAction;
import com.chaos.net.packet.Packet;
import com.chaos.net.packet.PacketListener;
import com.chaos.world.entity.impl.GroundItemManager;
import com.chaos.world.entity.impl.player.Player;

/**
 * This packet listener is used to pick up ground items that exist in the world.
 * 
 * @author relex lawl
 */

public class PickupItemPacketListener implements PacketListener {

	@Override
	public void handleMessage(final Player player, Packet packet) {
		final int y = packet.readLEShort();
		final int itemId = packet.readShort();
		final int x = packet.readLEShort();
		if (player.isTeleporting())
			return;
		final Position position = new Position(x, y, player.getPosition().getZ());
		if (!player.getLastItemPickup().elapsed(100))
			return;
		if (player.getConstitution() <= 0 || player.isTeleporting())
			return;
		player.setWalkToTask(null);
		player.setCastSpell(null);
		player.getCombatBuilder().cooldown(false);
		if (GameSettings.DEBUG_MODE) {
			// PlayerLogs.log(player,
			// "" + player.getUsername() + " in PickupItemPacketListener: " +
			// itemId + "");
		}
		GroundItem gItem = GroundItemManager.getGroundItem(player, new Item(itemId), position);
		if (gItem != null) {
			player.getActionQueue().addAction(new DistanceToGroundItemAction(player, gItem));
		}

	}
}
