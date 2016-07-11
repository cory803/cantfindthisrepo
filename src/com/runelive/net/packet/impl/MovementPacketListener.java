package com.runelive.net.packet.impl;

import com.runelive.GameSettings;
import com.runelive.model.Animation;
import com.runelive.model.Position;
import com.runelive.model.input.impl.ChangePassword;
import com.runelive.model.movement.MovementQueue;
import com.runelive.model.movement.PathFinder;
import com.runelive.net.packet.Packet;
import com.runelive.net.packet.PacketListener;
import com.runelive.world.content.BankPin;
import com.runelive.world.content.dialogue.DialogueManager;
import com.runelive.world.content.dialogue.impl.Tutorial;
import com.runelive.world.content.minigames.impl.Dueling;
import com.runelive.world.content.minigames.impl.Dueling.DuelRule;
import com.runelive.world.entity.impl.player.Player;

/**
 * This packet listener is called when a player has clicked on either the
 * mini-map or the actual game map to move around.
 * 
 * @author Gabriel Hannason
 */

public class MovementPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {

		player.setEntityInteraction(null);
		player.getSkillManager().stopSkilling();

		player.getMovementQueue().setFollowCharacter(null);

		if (packet.getOpcode() != COMMAND_MOVEMENT_OPCODE) {
			player.setWalkToTask(null);
			player.setCastSpell(null);
			player.getCombatBuilder().cooldown(false);
		}

		if (!checkReqs(player, packet.getOpcode()))
			return;

		player.getPacketSender().sendInterfaceRemoval();
		player.setTeleporting(false);
		player.setInactive(false);

		int steps = packet.getSize();
		if (packet.getOpcode() == 248) {
			steps -= 14;
		}
		steps -= 5;
		steps /= 2;
		if (steps < 0) {
			return;
		}
		player.getMovementQueue().reset();
		if (steps > 50) {
			return;
		}
		int[] offsetsX = player.offsetX;
		int[] offsetsY = player.offsetY;
		int firstX = packet.readLEShortA();
		for (int i = 0; i < steps; i++) {
			offsetsX[i] = packet.readByte();
			offsetsY[i] = packet.readByte();
			if (offsetsX[i] > 104 || offsetsY[i] > 104) {
				return;
			}
		}
		int firstY = packet.readLEShort();
		player.getMovementQueue().addStep(new Position(firstX, firstY));
		for (int i = 0; i < steps; i++) {
			player.getMovementQueue().addStep(new Position(firstX + offsetsX[i], firstY + offsetsY[i]));
		}
	}

	public boolean checkReqs(Player player, int opcode) {
		if (player.isFrozen()) {
			if (opcode != COMMAND_MOVEMENT_OPCODE)
				player.getMovementQueue().reset();
			player.getPacketSender().sendMessage("A magical force stops you from moving.");
			return false;
		}
		if (!player.getDragonSpear().elapsed(3000)) {
			if (opcode != COMMAND_MOVEMENT_OPCODE)
				player.getMovementQueue().reset();
			player.getPacketSender().sendMessage("You are stunned!");
			return false;
		}
		if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
				&& player.getBankPinAttributes().onDifferent(player)) {
			BankPin.init(player, false);
			return false;
		}
		if (player.getTrading().inTrade() && System.currentTimeMillis() - player.getTrading().lastAction <= 1000) {
			return false;
		}
		if (Dueling.checkRule(player, DuelRule.NO_MOVEMENT)) {
			if (opcode != COMMAND_MOVEMENT_OPCODE)
				player.getPacketSender().sendMessage("Movement has been turned off in this duel!");
			return false;
		}
		if (player.isResting()) {
			player.setResting(false);
			player.performAnimation(new Animation(11788));
			return false;
		}
		if (player.continueTutorial() && player.isPlayerLocked() && !player.getBankPinAttributes().hasBankPin()) {
			DialogueManager.start(player, Tutorial.get(player, 15));
			return false;
		}
		if (player.continueSkipTutorial() && player.isPlayerLocked() && !player.getBankPinAttributes().hasBankPin()) {
			DialogueManager.start(player, Tutorial.get(player, 17));
			return false;
		}
		if (player.continueLoginAccountPin() && player.isPlayerLocked()
				&& !player.getBankPinAttributes().hasBankPin()) {
			DialogueManager.start(player, Tutorial.get(player, 17));
			return false;
		}
		if (player.getPasswordChanging() && player.getPasswordChange() != GameSettings.PASSWORD_CHANGE) {
			player.setInputHandling(new ChangePassword());
			player.getPacketSender().sendEnterInputPrompt("Please enter a new password to set for your account:");
			return false;
		}
		if (player.isPlayerLocked() || player.isCrossingObstacle())
			return false;
		if (player.isNeedsPlacement()) {
			return false;
		}
		return !player.getMovementQueue().isLockMovement();
	}

	public static final int COMMAND_MOVEMENT_OPCODE = 98;
	public static final int GAME_MOVEMENT_OPCODE = 164;
	public static final int MINIMAP_MOVEMENT_OPCODE = 248;

}
