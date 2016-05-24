package com.runelive.world.content.minigames.impl;

import com.runelive.world.content.PlayerPanel;
import com.runelive.world.entity.impl.player.Player;

/**
 * @author High105
 */
public class Shrek1 {


	public static void openQuestLog(Player p) {
		for (int i = 8145; i < 8196; i++)
		  p.getPacketSender().sendString(i, "");
		p.getPacketSender().sendInterface(8134);
		p.getPacketSender().sendString(8136, "Close window");
		p.getPacketSender().sendString(8144, "" + questTitle);
		p.getPacketSender().sendString(8145, "");
		int questIntroIndex = 0;
		for (int i = 8147; i < 8147 + questIntro.length; i++) {
		  p.getPacketSender().sendString(i, "@dre@" + questIntro[questIntroIndex]);
		  questIntroIndex++;
		}
		int questGuideIndex = 0;
		for (int i = 8147 + questIntro.length; i < 8147 + questIntro.length + questGuide.length; i++) {
		  if (p.getMinigameAttributes().getShrek1Attributes().getQuestParts() == 0)
		    p.getPacketSender().sendString(i, "" + questGuide[questGuideIndex]);
		  else
		    p.getPacketSender().sendString(i, "@str@" + questGuide[questGuideIndex] + "");
		  questGuideIndex++;
		}
		if (p.getMinigameAttributes().getShrek1Attributes().getQuestParts() == 0) {
			p.getPacketSender().sendString(8152, "");
		}
	}

  public static void giveReward(Player p) {
    p.getPacketSender().sendInterface(12140);
    p.getPacketSender().sendString(12144, "You have completed: Shrek Part I");
    p.getPacketSender().sendString(12150, "A shrek mask");
    p.getPacketSender().sendString(12151, "Melee Xp?!?");
    p.getPacketSender().sendString(12152, "Ogre Head");
    p.getPacketSender().sendString(12153, "");
    p.getPacketSender().sendString(12154, "");
    p.getPacketSender().sendString(12155, "");
    p.getPacketSender().sendString(12147, "1");
    p.addQuestPoints(1);
    PlayerPanel.refreshPanel(p);
  }

  public static String getQuestTabPrefix(Player player) {
    if (player.getMinigameAttributes().getShrek1Attributes().getQuestParts() == 4) {
      return "@gre@";
    } else if (player.getMinigameAttributes().getShrek1Attributes().getQuestParts() != 0) {
      return "@yel@";
    }
    return "@red@";
  }

  private static final String questTitle = "Shrek Part I";
  private static final String[] questIntro = {"The Lord needs help with slaying a creature. ", 
		  "They say whoever helps him needs to be a fierce warrior."};
  private static final String[] questGuide = {"I can start this quest by speaking to Lord",
          "Lardquad who is found in Lumbridge."};
}
