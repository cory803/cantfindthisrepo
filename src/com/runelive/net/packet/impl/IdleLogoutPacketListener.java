package com.runelive.net.packet.impl;

import com.runelive.model.PlayerRights;
import com.runelive.net.packet.Packet;
import com.runelive.net.packet.PacketListener;
import com.runelive.world.entity.impl.player.Player;

// CALLED EVERY 3 MINUTES OF INACTIVITY

public class IdleLogoutPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR
				|| player.getRights() == PlayerRights.OWNER || player.getRights() == PlayerRights.MANAGER)
			return;
		/*
		 * if(player.logout() &&
		 * (player.getSkillManager().getSkillAttributes().getCurrentTask() ==
		 * null ||
		 * !player.getSkillManager().getSkillAttributes().getCurrentTask().
		 * isRunning())) { World.getPlayers().remove(player); }
		 */
		player.setInactive(true);
	}
}
