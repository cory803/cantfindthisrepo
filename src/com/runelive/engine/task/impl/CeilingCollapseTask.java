package com.runelive.engine.task.impl;

import com.runelive.engine.task.Task;
import com.runelive.model.CombatIcon;
import com.runelive.model.Graphic;
import com.runelive.model.Hit;
import com.runelive.model.Hitmask;
import com.runelive.model.Locations.Location;
import com.runelive.util.Misc;
import com.runelive.world.entity.impl.player.Player;

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
		player.dealDamage(new Hit(30 + Misc.getRandom(20), Hitmask.RED, CombatIcon.BLOCK));
	}
}
