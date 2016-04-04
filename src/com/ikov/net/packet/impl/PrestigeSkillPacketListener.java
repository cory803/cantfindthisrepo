package com.ikov.net.packet.impl;

import com.ikov.model.Skill;
import com.ikov.net.packet.Packet;
import com.ikov.net.packet.PacketListener;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.GameSettings;
import com.ikov.world.content.PlayerLogs;


public class PrestigeSkillPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int prestigeId = packet.readShort();
		Skill skill = Skill.forPrestigeId(prestigeId);
		if(skill == null) {
			return;
		}
		if(player.getInterfaceId() > 0) {
			player.getPacketSender().sendMessage("Please close all interfaces before doing this.");
			return;
		}
		if(GameSettings.DEBUG_MODE) {
			PlayerLogs.log(player.getUsername(), ""+player.getUsername()+" in PrestigeSkillPacketListener "+prestigeId+"");
		}
		player.getSkillManager().resetSkill(skill, true);
	}

}
