package com.ikov.net.packet.impl;

import com.ikov.model.Locations.Location;
import com.ikov.net.packet.Packet;
import com.ikov.net.packet.PacketListener;
import com.ikov.util.Misc;
import com.ikov.world.World;
import com.ikov.world.entity.impl.player.Player;

import com.ikov.GameSettings;
import com.ikov.world.content.PlayerLogs;


public class DungeoneeringPartyInvitatationPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		String plrToInvite = Misc.readString(packet.getBuffer());
		if(plrToInvite == null || plrToInvite.length() <= 0)
			return;
		plrToInvite = Misc.formatText(plrToInvite);
		if(player.getLocation() == Location.DUNGEONEERING) {
			if(player.getMinigameAttributes().getDungeoneeringAttributes().getParty() == null || player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner() == null)
				return;
			player.getPacketSender().sendInterfaceRemoval();
			if(player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner() != player) {
				player.getPacketSender().sendMessage("Only the party leader can invite other players.");
				return;
			}
			Player invite = World.getPlayerByName(plrToInvite);
			if(invite == null) {
				player.getPacketSender().sendMessage("That player is currently not online.");
				return;
			}
			player.getMinigameAttributes().getDungeoneeringAttributes().getParty().invite(invite);
		}
		if(GameSettings.DEBUG_MODE) {
			PlayerLogs.log(player.getUsername(), ""+player.getUsername()+" in DungeoneeringPartyInvitatationPacketListener");
		}
	}
}
