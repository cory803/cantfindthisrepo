package com.runelive.world.content.transportation;

import com.runelive.engine.task.Task;
import com.runelive.engine.task.TaskManager;
import com.runelive.model.Animation;
import com.runelive.model.Graphic;
import com.runelive.model.Locations.Location;
import com.runelive.model.PlayerRights;
import com.runelive.model.Position;
import com.runelive.world.content.BankPin;
import com.runelive.world.content.Sounds;
import com.runelive.world.content.Sounds.Sound;
import com.runelive.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.runelive.world.entity.impl.player.Player;

public class TeleportHandler {

	public static void teleportPlayer(final Player player, final Position targetLocation,
			final TeleportType teleportType, final boolean isFromHouse) {
		if (player.isJailed()) {
			player.getPacketSender().sendMessage("You can't teleport out of jail.");
			return;
		}
		if (teleportType != TeleportType.LEVER) {
			if (!checkReqs(player, targetLocation, isFromHouse)) {
				return;
			}
		}
		if (!player.getClickDelay().elapsed(4500) || player.getWalkingQueue().isLockMovement())
			return;
		player.currentDialog = null;
		player.setTeleporting(true).getWalkingQueue().setLockMovement(true).clear();
		cancelCurrentActions(player);
		player.performAnimation(teleportType.getStartAnimation());
		player.performGraphic(teleportType.getStartGraphic());
		Sounds.sendSound(player, Sound.TELEPORT);
		TaskManager.submit(new Task(1, player, true) {
			int tick = 0;

			@Override
			public void execute() {
				switch (teleportType) {
				case LEVER:
					if (tick == 0)
						player.performAnimation(new Animation(2140));
					else if (tick == 2) {
						player.performAnimation(new Animation(8939, 20));
						player.performGraphic(new Graphic(1576));
					} else if (tick == 4) {
						player.performAnimation(new Animation(8941));
						player.performGraphic(new Graphic(1577));
						player.moveTo(targetLocation).setPosition(targetLocation);
						player.getWalkingQueue().setLockMovement(false).clear();
						stop();
					}
					break;
				default:
					if (tick == teleportType.getStartTick()) {
						cancelCurrentActions(player);
						player.performAnimation(teleportType.getEndAnimation());
						player.performGraphic(teleportType.getEndGraphic());
						player.setInConstructionDungeon(false);
						if (Dungeoneering.doingDungeoneering(player)) {
							final Position dungEntrance = player.getMinigameAttributes().getDungeoneeringAttributes()
									.getParty().getDungeoneeringFloor().getEntrance().copy()
									.setZ(player.getPosition().getZ());
							player.moveTo(dungEntrance).setPosition(dungEntrance);
						} else {
							player.moveTo(targetLocation).setPosition(targetLocation);
						}

						player.setTeleporting(false);
					} else if (tick == teleportType.getStartTick() + 3) {
						player.getWalkingQueue().setLockMovement(false).clear();
					} else if (tick == teleportType.getStartTick() + 4)
						stop();
					break;
				}
				tick++;
			}

			@Override
			public void stop() {
				setEventRunning(false);
				player.setTeleporting(false);
				player.getClickDelay().reset(0);
			}
		});
		player.getClickDelay().reset();
	}

	public static void teleportPlayer(final Player player, final Position targetLocation,
			final TeleportType teleportType) {
		if (player.isJailed()) {
			player.getPacketSender().sendMessage("You can't teleport out of jail.");
			return;
		}
		if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
				&& player.getBankPinAttributes().onDifferent(player)) {
			BankPin.init(player, false);
			return;
		}
		if (player.getLocation() == Location.DUNGEONEERING && Dungeoneering.doingDungeoneering(player)
				|| player.getLocation() == Location.DUEL_ARENA && player.getDueling().duelingStatus == 5) {
			player.getPacketSender().sendMessage("You cannot do this now");
			return;
		}
		teleportPlayer(player, targetLocation, teleportType, false);
	}

	public static boolean interfaceOpen(Player player) {
		if (player.getInterfaceId() > 0 && player.getInterfaceId() != 50100) {
			player.getPacketSender().sendMessage("Please close the interface you have open before opening another.");
			return true;
		}
		return false;
	}

	/**
	 * Handles using teleports with jewelery under 30 wilderenss.
	 **/
	public static boolean checkReqs(Player player, Position targetLocation, boolean jeweleryTeleport) {
		if (player.getConstitution() <= 0)
			return false;
		if (player.getTeleblockTimer() > 0) {
			player.getPacketSender().sendInterfaceRemoval();
			player.getPacketSender().sendMessage("A magical spell is blocking you from teleporting.");
			return false;
		}
		if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
				&& player.getBankPinAttributes().onDifferent(player)) {
			BankPin.init(player, false);
			return false;
		}
		if (player.isDying()) {
			player.getPacketSender().sendInterfaceRemoval();
			player.getPacketSender().sendMessage("You can't teleport in mid death.");
			return false;
		}
		if (player.getLocation() == Location.JAIL && player.isJailed()) {
			player.getPacketSender().sendMessage("You can't teleport out of jail");
		}
		if (player.getLocation() == Location.WILDKEY_ZONE || player.getLocation() == Location.WILDERNESS) {
			if (player.getWildernessLevel() > 20) {
				if (player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.OWNER
						|| player.getRights() == PlayerRights.MANAGER) {
					player.getPacketSender()
							.sendMessage("@red@You've teleported out of deep Wilderness, logs have been written.");
					return true;
				}
				player.getPacketSender().sendInterfaceRemoval();
				player.getPacketSender().sendMessage("Teleport spells are blocked in this level of Wilderness.");
				player.getPacketSender()
						.sendMessage("You must be below level 20 of Wilderness to use teleportation spells.");
				return false;
			}
		}
		if (player.getLocation() == null) {
			player.getPacketSender().sendInterfaceRemoval();
			player.getPacketSender()
					.sendMessage("The location you have tried to teleport to is null, please report @ :;support!");
			return false;
		}
		if (player.isPlayerLocked() || player.isCrossingObstacle()) {
			player.getPacketSender().sendInterfaceRemoval();
			player.getPacketSender().sendMessage("You cannot teleport right now.");
			return false;
		}
		return true;
	}

	/**
	 * Handles using teleports without jewelery under 20 wilderenss.
	 **/
	public static boolean checkReqs(Player player, Position targetLocation) {
		if (player.getConstitution() <= 0)
			return false;
		if (player.getTeleblockTimer() > 0) {
			player.getPacketSender().sendInterfaceRemoval();
			player.getPacketSender().sendMessage("A magical spell is blocking you from teleporting.");
			return false;
		}
		if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
				&& player.getBankPinAttributes().onDifferent(player)) {
			BankPin.init(player, false);
			return false;
		}
		if (player.isDying()) {
			player.getPacketSender().sendInterfaceRemoval();
			player.getPacketSender().sendMessage("You can't teleport in mid death.");
			return false;
		}
		if (player.getLocation() != null && !player.getLocation().canTeleport(player)) {
			player.getPacketSender().sendInterfaceRemoval();
			return false;
		}
		if (player.isPlayerLocked() || player.isCrossingObstacle()) {
			player.getPacketSender().sendInterfaceRemoval();
			player.getPacketSender().sendMessage("You cannot teleport right now.");
			return false;
		}
		return true;
	}

	public static void cancelCurrentActions(Player player) {
		player.getPacketSender().sendInterfaceRemoval();
		//player.setTeleporting(false);
		player.setWalkToTask(null);
		player.setInputHandling(null);
		player.getSkillManager().stopSkilling();
		player.setEntityInteraction(null);
		player.getCombatBuilder().cooldown(false);
		player.setResting(false);
	}
}
