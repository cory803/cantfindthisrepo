package com.runelive.net.packet.impl;

import com.runelive.model.GameObject;
import com.runelive.model.Position;
import com.runelive.model.action.distance.DistanceToObjectAction;
import com.runelive.net.packet.Packet;
import com.runelive.net.packet.PacketListener;
import com.runelive.world.World;
import com.runelive.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.runelive.world.entity.impl.player.Player;

/**
 * This packet listener is called when a player clicked on a game object.
 * 
 * @author relex lawl
 */

public class ObjectActionPacketListener implements PacketListener {

	/**
	 * The PacketListener logger to debug information and print out errors.
	 */

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.isTeleporting() || player.isPlayerLocked() || player.getWalkingQueue().isLockMovement()) {
			return;
		}
		switch (packet.getOpcode()) {
			case FIRST_CLICK: {
				final int x = packet.readLEShortA();
				final int id = packet.readUnsignedShort();
				final int y = packet.readUnsignedShortA();
				final Position position = new Position(x, y, player.getPosition().getZ());
				final GameObject gameObject = new GameObject(id, position);
				if (id > 0 && id != 6 && id != 1765 && id != 5959 && id != 1306 && id != 1530 && id != 1276 && id != 2213 && id != 411
						&& id != 21772 && id != 881 && !Dungeoneering.doingDungeoneering(player)
						&& !World.objectExists(gameObject)) {
					System.out.println("Object dont exist");
					return;
				}
				if (player.isPlayerLocked()) {
					return;
				}
				if (!player.getDragonSpear().elapsed(3000)) {
					player.getPacketSender().sendMessage("You are stunned!");
					return;
				}
				player.getActionQueue().addAction(new DistanceToObjectAction(player, gameObject, 1));
				break;
			}
			case SECOND_CLICK: {
				final int id = packet.readLEShortA();
				final int y = packet.readLEShort();
				final int x = packet.readUnsignedShortA();
				final Position position = new Position(x, y, player.getPosition().getZ());
				final GameObject gameObject = new GameObject(id, position);
				if (id > 0 && id != 6 && id != 2213 && !World.objectExists(gameObject) && id != 4706) {
					player.getPacketSender().sendMessage("An error occured. Error code: " + id)
							.sendMessage("Please report the error to a staff member.");
					return;
				}
				if (player.isPlayerLocked()) {
					return;
				}
				if (!player.getDragonSpear().elapsed(3000)) {
					player.getPacketSender().sendMessage("You are stunned!");
					return;
				}
				player.getActionQueue().addAction(new DistanceToObjectAction(player, gameObject, 2));
				break;
			}
			case THIRD_CLICK: {
				final int id = packet.readUnsignedShortA();
				final int y = packet.readUnsignedShortA();
				final int x = packet.readShort();
				final Position position = new Position(x, y, player.getPosition().getZ());
				final GameObject gameObject = new GameObject(id, position);
				if (id > 0 && id != 6 && !World.objectExists(gameObject)) {
					// player.getPacketSender().sendMessage("An error occured.
					// Errorcode: "+id).sendMessage("Please report the error to a
					// staffmember.");
					return;
				}
				if (player.isPlayerLocked()) {
					return;
				}
				if (!player.getDragonSpear().elapsed(3000)) {
					player.getPacketSender().sendMessage("You are stunned!");
					return;
				}
				player.getActionQueue().addAction(new DistanceToObjectAction(player, gameObject, 3));
				break;
			}
			case FOURTH_CLICK: {
				final int id = packet.readUnsignedShortA();
				final int y = packet.readUnsignedShortA();
				final int x = packet.readShort();
				final Position position = new Position(x, y, player.getPosition().getZ());
				final GameObject gameObject = new GameObject(id, position);
				if (id > 0 && id != 6 && !World.objectExists(gameObject)) {
					// player.getPacketSender().sendMessage("An error occured.
					// Errorcode: "+id).sendMessage("Please report the error to a
					// staffmember.");
					return;
				}
				if (player.isPlayerLocked()) {
					return;
				}
				if (!player.getDragonSpear().elapsed(3000)) {
					player.getPacketSender().sendMessage("You are stunned!");
					return;
				}
				player.getActionQueue().addAction(new DistanceToObjectAction(player, gameObject, 4));
				break;
			}
			case FIFTH_CLICK: {
				final int id = packet.readUnsignedShortA();
				final int y = packet.readUnsignedShortA();
				final int x = packet.readShort();
				final Position position = new Position(x, y, player.getPosition().getZ());
				final GameObject gameObject = new GameObject(id, position);
				if (id > 0 && id != 6 && !World.objectExists(gameObject)) {
					// player.getPacketSender().sendMessage("An error occured.
					// Errorcode: "+id).sendMessage("Please report the error to a
					// staffmember.");
					return;
				}
				if (player.isPlayerLocked()) {
					return;
				}
				if (!player.getDragonSpear().elapsed(3000)) {
					player.getPacketSender().sendMessage("You are stunned!");
					return;
				}
				player.getActionQueue().addAction(new DistanceToObjectAction(player, gameObject, 5));
				break;
			}
		}
	}

	public static final int FIRST_CLICK = 132, SECOND_CLICK = 252, THIRD_CLICK = 70, FOURTH_CLICK = 234,
			FIFTH_CLICK = 228;
}
