package com.ikov.model.input.impl;

import java.io.File;

import com.ikov.GameSettings;
import com.ikov.model.Item;
import com.ikov.model.input.Input;
import com.ikov.world.content.dialogue.DialogueManager;
import com.ikov.world.entity.impl.player.Player;

public class GambleAdvertiser extends Input {

  @Override
  public void handleSyntax(Player player, String syntax) {
    player.getPacketSender().sendInterfaceRemoval();
    File file = new File("./data/saves/clans/" + syntax);
    if (file.exists()) {
      if (player.getInventory().getAmount(995) >= 100000000) {
        player.getInventory().delete(new Item(995, 100000000));
        player.setDialogueActionId(150);
        DialogueManager.start(player, 150);
        if (player.gambler_id == 1) {
          GameSettings.clan_name_1 = syntax;
          GameSettings.gambler_1 = true;
          GameSettings.gambler_timer_1 = 14400;
        } else {
          GameSettings.clan_name_2 = syntax;
          GameSettings.gambler_2 = true;
          GameSettings.gambler_timer_2 = 14400;
        }
      } else {
        player.setDialogueActionId(146);
        DialogueManager.start(player, 146);
      }
    } else {
      player.setDialogueActionId(149);
      DialogueManager.start(player, 149);
    }
  }
}
