package com.runelive.commands;

import com.runelive.model.PlayerRights;
import com.runelive.world.entity.impl.player.Player;

public abstract class DonatorCommand extends Command {

  private int donatorRights;

  public int getDonorRights() {
    return donatorRights;
  }

  public DonatorCommand(String name, int donatorRights) {
    super(name, PlayerRights.PLAYER);
    this.donatorRights = donatorRights;
  }

  @Override
  public abstract boolean execute(Player player, String key, String input) throws Exception;

}
