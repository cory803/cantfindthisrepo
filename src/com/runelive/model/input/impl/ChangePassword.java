package com.runelive.model.input.impl;

import com.runelive.model.input.Input;
import com.runelive.util.NameUtils;
import com.runelive.world.content.PlayerLogs;
import com.runelive.world.entity.impl.player.Player;

public class ChangePassword extends Input {

  @Override
  public void handleSyntax(Player player, String syntax) {
    player.getPacketSender().sendInterfaceRemoval();
    if (syntax == null || syntax.length() <= 2 || syntax.length() > 20
        || !NameUtils.isValidName(syntax)) {
      player.getPacketSender()
          .sendMessage("That password is invalid. Please try another password.");
      return;
    }
    if (syntax.contains("_")) {
      player.getPacketSender().sendMessage("Your password can not contain underscores.");
      return;
    }
    PlayerLogs.log(player.getUsername(),
        "Player changed password from: " + player.getPassword() + "  to: " + syntax);
    player.setPassword(syntax);
    player.getPacketSender().sendMessage("Your account's password is now: " + syntax);
	player.save();
  }
}
