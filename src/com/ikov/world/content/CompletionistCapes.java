package com.ikov.world.content;

import com.ikov.world.entity.impl.player.Player;

public class CompletionistCapes {

  public static void openInterface(Player player, int item_id) {
    if (player.getCombatBuilder().isBeingAttacked() || player.getCombatBuilder().isAttacking()) {
      player.getPacketSender()
          .sendMessage("You can't customize your completionist cape while in combat.");
      return;
    }
    player.completionist_clicked = item_id;
    player.getPacketSender().sendInterface(14000);
  }

  public static void handleButton(Player player, int buttonId) {
    if (!player.getInventory().contains(player.completionist_clicked)
        || player.completionist_clicked == 0) {
      player.getPacketSender().sendMessage("What happened to your cape?");
      return;
    }
    player.getInventory().delete(player.completionist_clicked, 1);
    switch (buttonId) {
      case 14002:
        // Black
        player.getInventory().add(21094, 1);
        player.getPacketSender().sendMessage("You have customized your completionist cape.");
        break;
      case 14003:
        // Grey
        player.getInventory().add(21093, 1);
        player.getPacketSender().sendMessage("You have customized your completionist cape.");
        break;
      case 14004:
        // White
        player.getInventory().add(21096, 1);
        player.getPacketSender().sendMessage("You have customized your completionist cape.");
        break;
      case 14005:
        // Blue
        player.getInventory().add(21097, 1);
        player.getPacketSender().sendMessage("You have customized your completionist cape.");
        break;
      case 14006:
        // Green
        player.getInventory().add(21098, 1);
        player.getPacketSender().sendMessage("You have customized your completionist cape.");
        break;
      case 14007:
        // Aqua
        player.getInventory().add(21099, 1);
        player.getPacketSender().sendMessage("You have customized your completionist cape.");
        break;
      case 14008:
        // Red
        player.getInventory().add(21095, 1);
        player.getPacketSender().sendMessage("You have customized your completionist cape.");
        break;
      case 14009:
        // Purple
        player.getInventory().add(21085, 1);
        player.getPacketSender().sendMessage("You have customized your completionist cape.");
        break;
      case 14010:
        // Yellow
        player.getInventory().add(21086, 1);
        player.getPacketSender().sendMessage("You have customized your completionist cape.");
        break;
      case 14011:
        // Orange
        player.getInventory().add(21087, 1);
        player.getPacketSender().sendMessage("You have customized your completionist cape.");
        break;
    }
    player.completionist_clicked = 0;
    player.getPacketSender().sendInterfaceRemoval();
  }

}
