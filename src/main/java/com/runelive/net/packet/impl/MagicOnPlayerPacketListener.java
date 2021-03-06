package com.runelive.net.packet.impl;

import com.runelive.net.packet.Packet;
import com.runelive.net.packet.PacketListener;
import com.runelive.world.World;
import com.runelive.world.content.combat.magic.CombatSpell;
import com.runelive.world.content.combat.magic.CombatSpells;
import com.runelive.world.entity.impl.player.Player;

public class MagicOnPlayerPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int playerIndex = packet.readShortA();
		if (playerIndex < 0 || playerIndex > World.getPlayers().capacity())
			return;
		int spellId = packet.readLEShort();
		if (spellId < 0) {
			return;
		}

		Player attacked = World.getPlayers().get(playerIndex);

		if (attacked == null || attacked.equals(player)) {
			player.getWalkingQueue().clear();
			return;
		}

		CombatSpell spell = CombatSpells.getSpell(spellId);
		if (spell == null) {
			player.getWalkingQueue().clear();
			return;
		}

		if (attacked.getConstitution() <= 0) {
			player.getWalkingQueue().clear();
			return;
		}

		if (spell.getSpellbook() != null && spell.getSpellbook() != player.getSpellbook()) {
			player.getPacketSender()
					.sendMessage("You can't do this! Please report how you did this to an admin. [mgc1]");
			player.getWalkingQueue().clear();
			return;
		}

		// Start combat!
		player.setPositionToFace(attacked.getPosition());
		player.getCombatBuilder().resetCooldown();
		player.setCastSpell(spell);
		player.getCombatBuilder().attack(attacked);
	}

}
