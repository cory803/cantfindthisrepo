package com.chaos.world.content;

import com.chaos.world.World;
import com.chaos.world.entity.impl.player.Player;

public class PlayerPanel {

	public static void refreshPanel(Player player) {
		//TODO: Add player panel
		player.getPacketSender().sendString(55072, "Website: @cha@chaosps.com");
		player.getPacketSender().sendString(55073, "Players online: @cha@"+ World.getPlayers().size());
		player.getPacketSender().sendString(55074, "Staff online: @cha@"+ World.getPlayers().size());
		player.getPacketSender().sendString(55075, "");
		player.getPacketSender().sendString(55076, "Title: @cha@None");
		player.getPacketSender().sendString(55077, "Game mode: @cha@Knight");
		player.getPacketSender().sendString(55078, "Rank: @cha@Player");
		player.getPacketSender().sendString(55079, "Pk points: @cha@0");
		player.getPacketSender().sendString(55080, "Vote points: @cha@0");
		player.getPacketSender().sendString(55081, "Slayer points: @cha@0");
	}

}
