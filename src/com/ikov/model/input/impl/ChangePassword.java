package com.ikov.model.input.impl;

import com.ikov.model.input.Input;
import com.ikov.util.NameUtils;
import com.ikov.world.content.PlayerLogs;
import com.ikov.world.entity.impl.player.Player;

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
    boolean success = false;
    try {

    } catch (Exception e) {
      e.printStackTrace();
      success = false;
    }
    success = true;
    if (success) {
      PlayerLogs.log(player.getUsername(),
          "Player changed password from: " + player.getPassword() + "  to: " + syntax);
      player.setPassword(syntax);
      player.getPacketSender().sendMessage("Your account's password is now: " + syntax);
    } else {
      player.getPacketSender().sendMessage("An error occured. Please try again.");
    }
  }
}
