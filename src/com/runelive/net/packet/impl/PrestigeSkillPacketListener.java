package com.runelive.net.packet.impl;

import com.runelive.GameSettings;
import com.runelive.model.Skill;
import com.runelive.net.packet.Packet;
import com.runelive.net.packet.PacketListener;
import com.runelive.world.entity.impl.player.Player;

public class PrestigeSkillPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int prestigeId = packet.readShort();
		Skill skill = Skill.forPrestigeId(prestigeId);
		if (skill == null) {
			return;
		}
		if (player.getInterfaceId() > 0) {
			player.getPacketSender().sendMessage("Please close all interfaces before doing this.");
			return;
		}
		if (GameSettings.DEBUG_MODE) {
			// PlayerLogs.log(player,
			// "" + player.getUsername() + " in PrestigeSkillPacketListener " +
			// prestigeId + "");
		}
		player.getSkillManager().resetSkill(skill, true);
	}

}
