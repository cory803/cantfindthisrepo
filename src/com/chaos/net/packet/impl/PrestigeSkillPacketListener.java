package com.chaos.net.packet.impl;

import com.chaos.GameSettings;
import com.chaos.model.Skill;
import com.chaos.net.packet.Packet;
import com.chaos.net.packet.PacketListener;
import com.chaos.world.entity.impl.player.Player;

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
		if(prestigeId == 4) {
			if (player.getCannon() != null) {
				player.getPacketSender().sendMessage("You can't do this while you have a cannon up.");
				return;
			}
		}
		if (GameSettings.DEBUG_MODE) {
			// PlayerLogs.log(player,
			// "" + player.getUsername() + " in PrestigeSkillPacketListener " +
			// prestigeId + "");
		}
		player.getSkillManager().resetSkill(skill, true);
	}

}
