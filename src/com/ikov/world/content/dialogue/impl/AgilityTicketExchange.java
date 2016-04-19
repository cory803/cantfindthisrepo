package com.ikov.world.content.dialogue.impl;

import com.ikov.model.input.impl.BuyAgilityExperience;
import com.ikov.world.content.dialogue.Dialogue;
import com.ikov.world.content.dialogue.DialogueExpression;
import com.ikov.world.content.dialogue.DialogueType;
import com.ikov.world.entity.impl.player.Player;

public class AgilityTicketExchange {

  public static Dialogue getDialogue(Player player) {
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
        return 437;
      }

      @Override
      public String[] dialogue() {
        return new String[] {"@bla@How many tickets would you like to exchange",
            "for experience? One ticket currently grants", "@red@7680@bla@ Agility experience."};
      }

      public Dialogue nextDialogue() {
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
            return 437;
          }

          @Override
          public String[] dialogue() {
            return new String[] {"@bla@How many tickets would you like to exchange",
                "for experience? One ticket currently grants",
                "@red@7680@bla@ Agility experience."};
          }

          @Override
          public void specialAction() {
            player.getPacketSender().sendInterfaceRemoval();
            player.setInputHandling(new BuyAgilityExperience());
            player.getPacketSender()
                .sendEnterAmountPrompt("How many tickets would you like to exchange?");
          }
        };

      }
    };
  }

}
