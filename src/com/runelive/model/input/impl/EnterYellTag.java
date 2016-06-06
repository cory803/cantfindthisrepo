package com.runelive.model.input.impl;

import com.runelive.model.Item;
import com.runelive.model.input.Input;
import com.runelive.world.content.dialogue.DialogueManager;
import com.runelive.world.entity.impl.player.Player;

public class EnterYellTag extends Input {

  @Override
  public void handleSyntax(Player player, String syntax) {
    String yell_tag = syntax;
    boolean dont_send = false;
    int value = 500000000;
    String[] not_allowed = {"owner", "moderator", "admin", "fuck", "bitch", "shit", "nigger",
        "cancer", ".com", ".org", ".net", "asshole", "faggot", "porn", "penis", "vagina",
        "ballsack", "nutsack", "<", ">", "img=", "col=", "shad=", "hair pube"};
    for (int i = 0; i < not_allowed.length; i++) {
      if (yell_tag.toLowerCase().contains(not_allowed[i])) {
        DialogueManager.sendStatement(player, "You are not allowed to have that in your yell tag!");
        dont_send = true;
      }
    }
    if (dont_send) {
      return;
    }
    if (player.getInventory().getAmount(995) >= value) {
      player.getInventory().delete(new Item(995, value));
    } else if (player.getMoneyInPouch() >= value) {
      player.setMoneyInPouch((player.getMoneyInPouch() - value));
    }
    player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
    DialogueManager.sendStatement(player, "Your yell tag has been set to " + yell_tag + "!");
    player.setYellTag(yell_tag);
  }
}
