package com.chaos.engine.task.impl;

import com.chaos.engine.task.Task;
import com.chaos.model.CombatIcon;
import com.chaos.model.Graphic;
import com.chaos.model.Hit;
import com.chaos.model.Hitmask;
import com.chaos.model.Locations.Location;
import com.chaos.util.Misc;
import com.chaos.world.entity.impl.player.Player;

/**
 * Barrows
 * 
 * @author Gabriel Hannason
 */
public class CeilingCollapseTask extends Task {

	public CeilingCollapseTask(Player player) {
		super(9, player, false);
		this.player = player;
	}

	private Player player;

	@Override
	public void execute() {
		if (player == null || !player.isRegistered() || player.getLocation() != Location.BARROWS
				|| player.getLocation() == Location.BARROWS && player.getPosition().getY() < 8000) {
			player.getPacketSender().sendCameraNeutrality();
			stop();
			return;
		}
		player.performGraphic(new Graphic(60));
		player.getPacketSender().sendMessage("Some rocks fall from the ceiling and hit you.");
		player.forceChat("Ouch!");
		player.dealDamage(null, new Hit(30 + Misc.getRandom(20), Hitmask.RED, CombatIcon.BLOCK));
	}
}
