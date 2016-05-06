package com.runelive.world.content;

import com.runelive.world.content.dialogue.Dialogue;
import com.runelive.world.content.dialogue.DialogueExpression;
import com.runelive.world.content.dialogue.DialogueManager;
import com.runelive.world.content.dialogue.DialogueType;
import com.runelive.world.entity.impl.player.Player;
import com.runelive.model.Store;

public class MemberScrolls {

  public static void checkForRankUpdate(Player player) {
    if (player.getDonorRights() == 1) {
      if (player.getAmountDonated() < 10) {
        player.setAmountDonated(10);
      }
    }
    if (player.getDonorRights() == 2) {
      if (player.getAmountDonated() < 25) {
        player.setAmountDonated(25);
      }
    }
    if (player.getDonorRights() == 3) {
      if (player.getAmountDonated() < 50) {
        player.setAmountDonated(50);
      }
    }
    if (player.getDonorRights() == 4) {
      if (player.getAmountDonated() < 150) {
        player.setAmountDonated(150);
      }
    }
    if (player.getDonorRights() == 5) {
      if (player.getAmountDonated() < 500) {
        player.setAmountDonated(500);
      }
    }
    if (player.getAmountDonated() >= 10 && player.getAmountDonated() < 25
        && player.getDonorRights() != 1) {
      player.setDonorRights(1);
      player.getPacketSender().sendMessage("You've become a Regular Donator! Congratulations!");
    } else if (player.getAmountDonated() >= 25 && player.getAmountDonated() < 50
        && player.getDonorRights() != 2) {
      player.setDonorRights(2);
      player.getPacketSender().sendMessage("You've become a Super Donator! Congratulations!");
    } else if (player.getAmountDonated() >= 50 && player.getAmountDonated() < 150
        && player.getDonorRights() != 3) {
      player.setDonorRights(3);
      player.getPacketSender().sendMessage("You've become a Extreme Donator! Congratulations!");
    } else if (player.getAmountDonated() >= 150 && player.getAmountDonated() < 500
        && player.getDonorRights() != 4) {
      player.setDonorRights(4);
      player.getPacketSender().sendMessage("You've become a Legendary Donator! Congratulations!");
    } else if (player.getAmountDonated() >= 500 && player.getDonorRights() != 5) {
      player.setDonorRights(5);
      player.getPacketSender().sendMessage("You've become a Uber Donator! Congratulations!");
    }
  }

  public static boolean handleScroll(Player player, String name) {
	int item = player.currentScroll;
    int funds = 0;
    int tokens = 0;
    switch (item) {
      case 10943:
		Store.addTokensFromScroll(player, name, 8, item);
        break;
      case 10934:
        Store.addTokensFromScroll(player, name, 20, item);
        break;
      case 10935:
        Store.addTokensFromScroll(player, name, 40, item);
        break;
      case 7629:
        Store.addTokensFromScroll(player, name, 100, item);
        break;
    }
    return false;
  }

  public static Dialogue getTotalFunds(final Player player) {
    return new Dialogue() {

      @Override
      public DialogueType type() {
        return DialogueType.NPC_STATEMENT;
      }

      @Override
      public DialogueExpression animation() {
        return DialogueExpression.NORMAL;
      }

      @Override
      public int npcId() {
        return 4657;
      }

      @Override
      public String[] dialogue() {
        return player.getAmountDonated() > 0
            ? new String[] {"Your account has claimed scrolls worth $" + player.getAmountDonated()
                + " in total.", "Thank you for supporting us!"}
            : new String[] {"Your account has claimed scrolls worth $" + player.getAmountDonated()
                + " in total."};
      }

      @Override
      public Dialogue nextDialogue() {
        return DialogueManager.getDialogues().get(5);
      }
    };
  }
}
