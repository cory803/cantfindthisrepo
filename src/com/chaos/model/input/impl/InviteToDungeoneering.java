package com.chaos.model.input.impl;

import com.chaos.model.Locations.Location;
import com.chaos.model.input.Input;
import com.chaos.util.Misc;
import com.chaos.world.World;
import com.chaos.world.entity.impl.player.Player;

public class InviteToDungeoneering extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		player.getPacketSender().sendInterfaceRemoval();
		if (syntax == null || syntax.length() <= 0)
			return;
		String plrToInvite = Misc.formatText(syntax);
		if (player.getLocation() == Location.DUNGEONEERING) {
			if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty() == null
					|| player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner() == null)
				return;
			player.getPacketSender().sendInterfaceRemoval();
			if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner() != player) {
				player.getPacketSender().sendMessage("Only the party leader can invite other players.");
				return;
			}
			Player invite = World.getPlayerByName(plrToInvite);
			if (invite == null) {
				player.getPacketSender().sendMessage("That player is currently not online.");
				return;
			}
			player.getMinigameAttributes().getDungeoneeringAttributes().getParty().invite(invite);
		}
	}
}
