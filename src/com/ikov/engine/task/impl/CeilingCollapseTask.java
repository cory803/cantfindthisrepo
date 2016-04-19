package com.ikov.engine.task.impl;


import com.ikov.engine.task.Task;
import com.ikov.model.CombatIcon;
import com.ikov.model.Graphic;
import com.ikov.model.Hit;
import com.ikov.model.Hitmask;
import com.ikov.model.Locations.Location;
import com.ikov.util.Misc;
import com.ikov.world.entity.impl.player.Player;

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
