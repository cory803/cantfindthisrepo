package com.chaos.net.packet.impl;

import com.chaos.model.PlayerRights;
import com.chaos.model.action.distance.DistanceToNpcAction;
import com.chaos.model.action.executable.NpcMenuActionFourExecutable;
import com.chaos.model.action.executable.NpcMenuActionOneExecutable;
import com.chaos.model.action.executable.NpcMenuActionThreeExecutable;
import com.chaos.model.action.executable.NpcMenuActionTwoExecutable;
import com.chaos.model.definitions.NpcDefinition;
import com.chaos.net.packet.Packet;
import com.chaos.net.packet.PacketListener;
import com.chaos.world.World;
import com.chaos.world.content.BankPin;
import com.chaos.world.content.combat.CombatFactory;
import com.chaos.world.content.combat.magic.CombatSpell;
import com.chaos.world.content.combat.magic.CombatSpells;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;

public class NPCOptionPacketListener implements PacketListener {

	private static void attackNPC(Player player, Packet packet) {
		if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
				&& player.getBankPinAttributes().onDifferent(player)) {
			BankPin.init(player, false);
			return;
		}
		int index = packet.readShortA();
		if (index < 0 || index > World.getNpcs().capacity())
			return;
		final NPC interact = World.getNpcs().get(index);
		if (interact == null)
			return;

		if (!NpcDefinition.getDefinitions()[interact.getId()].isAttackable()) {
			return;
		}

		if (interact.getConstitution() <= 0) {
			player.getWalkingQueue().clear();
			return;
		}

		if (player.getCombatBuilder().getStrategy() == null) {
			player.getCombatBuilder().determineStrategy();
		}

		if (CombatFactory.checkAttackDistance(player, interact)) {
			player.getWalkingQueue().clear();
		}

		if (player.getRights() == PlayerRights.DEVELOPER) {
			player.getPacketSender().sendMessage("Attacking npc id: " + interact.getId());
		}
		if (interact.getId() == 1172) {
			if (!player.getDrankBraverly()) {
				player.getPacketSender()
						.sendMessage("You need to drink a braverly potion before fighting this monster.");
				return;
			} else {
				player.getCombatBuilder().attack(interact);
			}
		}
		player.getCombatBuilder().attack(interact);
		interact.getCombatBuilder().setVictim(player);
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.isTeleporting() || player.isPlayerLocked() || player.getWalkingQueue().isLockMovement())
			return;
		switch (packet.getOpcode()) {
			case ATTACK_NPC: {
				attackNPC(player, packet);
				break;
			}
			case FIRST_CLICK_OPCODE: {
				int index = packet.readLEShort();
				if (index < 0 || index > World.getNpcs().capacity())
					return;
				final NPC npc = World.getNpcs().get(index);
				if (npc == null)
					return;
				player.getActionQueue().addAction(new DistanceToNpcAction(player, npc, new NpcMenuActionOneExecutable(player, npc)));
				break;
			}
			case SECOND_CLICK_OPCODE: {
				int index = packet.readLEShortA();
				if (index < 0 || index > World.getNpcs().capacity())
					return;
				final NPC npc = World.getNpcs().get(index);
				if (npc == null)
					return;
				player.getActionQueue().addAction(new DistanceToNpcAction(player, npc, new NpcMenuActionTwoExecutable(player, npc)));
				break;
			}
			case THIRD_CLICK_OPCODE: {
				int index = packet.readShort();
				if (index < 0 || index > World.getNpcs().capacity())
					return;
				final NPC npc = World.getNpcs().get(index);
				if (npc == null)
					return;
				player.getActionQueue().addAction(new DistanceToNpcAction(player, npc, new NpcMenuActionThreeExecutable(player, npc)));
				break;
			}
			case FOURTH_CLICK_OPCODE: {
				int index = packet.readLEShort();
				if (index < 0 || index > World.getNpcs().capacity())
					return;
				final NPC npc = World.getNpcs().get(index);
				if (npc == null)
					return;
				player.getActionQueue().addAction(new DistanceToNpcAction(player, npc, new NpcMenuActionFourExecutable(player, npc)));
				break;
			}
			case MAGE_NPC: {
				int npcIndex = packet.readLEShortA();
				int spellId = packet.readShortA();

				if (npcIndex < 0 || spellId < 0 || npcIndex > World.getNpcs().capacity()) {
					return;
				}

				NPC n = World.getNpcs().get(npcIndex);
				player.setEntityInteraction(n);

				CombatSpell spell = CombatSpells.getSpell(spellId);

				if (n == null || spell == null) {
					player.getWalkingQueue().clear();
					return;
				}

				if (!NpcDefinition.getDefinitions()[n.getId()].isAttackable()) {
					player.getWalkingQueue().clear();
					return;
				}

				if (n.getConstitution() <= 0) {
					player.getWalkingQueue().clear();
					return;
				}

				if (spell.getSpellbook() != null && spell.getSpellbook() != player.getSpellbook()) {
					player.getPacketSender()
							.sendMessage("You can't do this! Please report how you did this to an admin. [mgc2]");
					player.getWalkingQueue().clear();
					return;
				}

				player.setPositionToFace(n.getPosition());
				player.setCastSpell(spell);
				if (player.getCombatBuilder().getStrategy() == null) {
					player.getCombatBuilder().determineStrategy();
				}
				if (CombatFactory.checkAttackDistance(player, n)) {
					player.getWalkingQueue().clear();
				}
				player.getCombatBuilder().resetCooldown();
				player.getCombatBuilder().attack(n);
				break;
			}
		}
	}

	public static final int ATTACK_NPC = 72, FIRST_CLICK_OPCODE = 155, MAGE_NPC = 131, SECOND_CLICK_OPCODE = 17,
			THIRD_CLICK_OPCODE = 21, FOURTH_CLICK_OPCODE = 18;
}