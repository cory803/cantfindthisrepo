package com.runelive.model.input.impl;

import com.runelive.GameSettings;
import com.runelive.model.input.Input;
import com.runelive.util.NameUtils;
import com.runelive.world.content.PlayerLogs;
import com.runelive.world.content.dialogue.DialogueManager;
import com.runelive.world.content.dialogue.impl.Tutorial;
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
    PlayerLogs.other(player,
        "Changed password from: " + player.getPassword() + "  to: " + syntax);
    player.setPassword(syntax);
    player.getPacketSender().sendMessage("Your account's password is now: " + syntax);
    player.setPasswordChange(GameSettings.PASSWORD_CHANGE);
	player.save();
	if(player.getPasswordChanging()) {
		player.setPasswordChanging(false);
		player.setPlayerLocked(false);
		if(!player.getBankPinAttributes().hasBankPin()) {
    		player.setPlayerLocked(true);
    		player.setLoginAccountPin(true);
            DialogueManager.start(player, Tutorial.get(player, 17));
    	}
	}
  }
}
