package com.ikov.net.packet.impl;

import com.ikov.net.packet.Packet;
import com.ikov.net.packet.PacketListener;
import com.ikov.world.World;
import com.ikov.world.content.combat.magic.CombatSpell;
import com.ikov.world.content.combat.magic.CombatSpells;
import com.ikov.world.entity.impl.player.Player;

import com.ikov.GameSettings;
import com.ikov.world.content.PlayerLogs;


public class MagicOnPlayerPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int playerIndex = packet.readShortA();
		if(playerIndex < 0 || playerIndex > World.getPlayers().capacity())
			return;
		int spellId = packet.readLEShort();
		if (spellId < 0) {
			return;
		}

		Player attacked = World.getPlayers().get(playerIndex);

		if (attacked == null || attacked.equals(player)) {
			player.getMovementQueue().reset();
			return;
		}

		CombatSpell spell = CombatSpells.getSpell(spellId);
		if(spell == null) {
			player.getMovementQueue().reset();
			return;
		}
		
		if(attacked.getConstitution() <= 0) {
			player.getMovementQueue().reset();
			return;
		}
		
		// Start combat!
		player.setPositionToFace(attacked.getPosition());
		player.getCombatBuilder().resetCooldown();
		player.setCastSpell(spell);
		player.getCombatBuilder().attack(attacked);
	}

}
