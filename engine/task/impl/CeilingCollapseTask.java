package com.strattus.engine.task.impl;


import com.strattus.engine.task.Task;
import com.strattus.model.CombatIcon;
import com.strattus.model.Graphic;
import com.strattus.model.Hit;
import com.strattus.model.Hitmask;
import com.strattus.model.Locations.Location;
import com.strattus.util.Misc;
import com.strattus.world.entity.impl.player.Player;

/**
 * Barrows
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
		if(player == null || !player.isRegistered() || player.getLocation() != Location.BARROWS || player.getLocation() == Location.BARROWS && player.getPosition().getY() < 8000) {
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
