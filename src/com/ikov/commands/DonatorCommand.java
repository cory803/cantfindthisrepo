package com.ikov.commands;

import com.ikov.model.PlayerRights;
import com.ikov.world.entity.impl.player.Player;

public class DonatorCommand extends Command {
	
	private int donatorRights;
	
	public int getDonorRights() {
		return donatorRights;
	}

	public DonatorCommand(String name, int donatorRights) {
		super(name, PlayerRights.PLAYER);
		this.donatorRights = donatorRights;
	}

	@Override
	public boolean execute(Player player, String input) throws Exception {
		if (player.getDonorRights() >= getDonorRights())
			return true;
		return false;
	}

}
