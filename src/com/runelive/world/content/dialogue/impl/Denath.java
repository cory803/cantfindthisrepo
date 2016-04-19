package com.runelive.world.content.dialogue.impl;

import com.runelive.world.content.dialogue.Dialogue;
import com.runelive.world.content.dialogue.DialogueExpression;
import com.runelive.world.content.dialogue.DialogueType;
import com.runelive.world.entity.impl.player.Player;

public class Denath {

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
        return 4663;
      }

      @Override
      public String[] dialogue() {
        return new String[] {
            "@bla@Come back when you have all "
                + player.getMinigameAttributes().getClawQuestAttributes().SAMPLES_NEEDED + " of my",
            "samples. I will make it into something for you."};
      }

    };
  }

}
