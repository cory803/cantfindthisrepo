package com.runelive.commands.impl;

import com.runelive.commands.Command;
import com.runelive.model.PlayerRights;
import com.runelive.model.input.impl.ChangePassword;
import com.runelive.world.entity.impl.player.Player;

public class ChangePasswordCommand extends Command {

  public ChangePasswordCommand(String name) {
    super(name, PlayerRights.PLAYER);
  }

  @Override
  public boolean execute(Player player, String key, String input) throws Exception {
    player.setInputHandling(new ChangePassword());
    player.getPacketSender().sendEnterInputPrompt("Enter a new password:");
    return true;
  }

}
