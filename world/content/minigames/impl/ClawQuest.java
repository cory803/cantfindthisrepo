package com.ikov.world.content.minigames.impl;

import com.ikov.world.entity.impl.player.Player;

/**
 * @author High105
 */
public class ClawQuest {


	public static void openQuestLog(Player p) {
		for(int i = 8145; i < 8196; i++)
			p.getPacketSender().sendString(i, "");
		p.getPacketSender().sendInterface(8134);
		p.getPacketSender().sendString(8136, "Close window");
		p.getPacketSender().sendString(8144, ""+questTitle);
		p.getPacketSender().sendString(8145, "");
		int questIntroIndex = 0;
		for(int i = 8147; i < 8147+questIntro.length; i++) {
			p.getPacketSender().sendString(i, "@dre@"+questIntro[questIntroIndex]);
			questIntroIndex++;
		}
		int questGuideIndex = 0;
		for(int i = 8147+questIntro.length; i < 8147+questIntro.length+questGuide.length; i++) {
			if(p.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 0)
				p.getPacketSender().sendString(i+3, ""+questGuide[questGuideIndex]);
			else
				p.getPacketSender().sendString(i+3, "@str@"+questGuide[questGuideIndex]+"");
			questGuideIndex++;
		}
			p.getPacketSender().sendString(8150, "Skill Requirements");	
			if(p.getSkillManager().getMaxLevel(17) < 50)
				p.getPacketSender().sendString(8151, "Thieving: 50");
			else
				p.getPacketSender().sendString(8151, "@str@Thieving: 50");
			if(p.getSkillManager().getMaxLevel(10) < 90)
				p.getPacketSender().sendString(8152, "Fishing: 91");
			else
				p.getPacketSender().sendString(8152, "@str@Fishing: 91");
		if(p.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 1) {
			p.getPacketSender().sendString(8155, "The king wants me to go north of the yaks training");
			p.getPacketSender().sendString(8156, "area and find Phingspet. She has something of his.");
			p.getPacketSender().sendString(8157, "He said something about her being in a pasture and");
			p.getPacketSender().sendString(8158, "along the river bank.");
		} else if(p.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 2) {
			p.getPacketSender().sendString(8155, "@str@The king wants me to go north of the yaks training");
			p.getPacketSender().sendString(8156, "@str@area and find Phingspet. She has something of his.");
			p.getPacketSender().sendString(8157, "@str@He said something about her being in a cow pasture and");
			p.getPacketSender().sendString(8158, "@str@along the river.");
			p.getPacketSender().sendString(8159, "He requested me to come back to him with whatever I found.");
		} else if(p.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 3) {
			p.getPacketSender().sendString(8155, "@str@The king wants me to go north of the yaks training");
			p.getPacketSender().sendString(8156, "@str@area and find Phingspet. She has something of his.");
			p.getPacketSender().sendString(8157, "@str@He said something about her being in a cow pasture and");
			p.getPacketSender().sendString(8158, "@str@along the river.");
			p.getPacketSender().sendString(8159, "@str@He requested me to come back to him with whatever I found.");
			p.getPacketSender().sendString(8160, "I need to talk to Denath and convince him to agree to");
			p.getPacketSender().sendString(8161, "help the king. Then inform the king when he says yes.");
		} else if(p.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 4) {
			p.getPacketSender().sendString(8155, "@str@The king wants me to go north of the yaks training");
			p.getPacketSender().sendString(8156, "@str@area and find Phingspet. She has something of his.");
			p.getPacketSender().sendString(8157, "@str@He said something about her being in a cow pasture and");
			p.getPacketSender().sendString(8158, "@str@along the river.");
			p.getPacketSender().sendString(8159, "@str@He requested me to come back to him with whatever I found.");
			p.getPacketSender().sendString(8160, "@str@I need to talk to the GUY and convince him to agree to");
			p.getPacketSender().sendString(8161, "@str@help the king. Then inform the king when he says yes.");
			p.getPacketSender().sendString(8162, "I need to bring Denath 100 noted rocktails before he");
			p.getPacketSender().sendString(8163, "agrees to help the king. Then I must tell the king that");
			p.getPacketSender().sendString(8164, "Denath has agreed to help.");
		} else if(p.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 5) {
			p.getPacketSender().sendString(8155, "@str@The king wants me to go north of the yaks training");
			p.getPacketSender().sendString(8156, "@str@area and find Phingspet. She has something of his.");
			p.getPacketSender().sendString(8157, "@str@He said something about her being in a cow pasture and");
			p.getPacketSender().sendString(8158, "@str@along the river.");
			p.getPacketSender().sendString(8159, "@str@He requested me to come back to him with whatever I found.");
			p.getPacketSender().sendString(8160, "@str@I need to talk to the GUY and convince him to agree to");
			p.getPacketSender().sendString(8161, "@str@help the king. Then inform the king when he says yes.");
			p.getPacketSender().sendString(8162, "@str@I need to bring Denath 100 noted rocktails before he");
			p.getPacketSender().sendString(8163, "@str@agrees to help the king. Then I must tell the king that");
			p.getPacketSender().sendString(8164, "@str@Denath has agreed to help.");
			p.getPacketSender().sendString(8165, "I must let the king know that Denath will help us.");
		} else if(p.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 6) {
			p.getPacketSender().sendString(8155, "@str@The king wants me to go north of the yaks training");
			p.getPacketSender().sendString(8156, "@str@area and find Phingspet. She has something of his.");
			p.getPacketSender().sendString(8157, "@str@He said something about her being in a cow pasture and");
			p.getPacketSender().sendString(8158, "@str@along the river.");
			p.getPacketSender().sendString(8159, "@str@He requested me to come back to him with whatever I found.");
			p.getPacketSender().sendString(8160, "@str@I need to talk to the GUY and convince him to agree to");
			p.getPacketSender().sendString(8161, "@str@help the king. Then inform the king when he says yes.");
			p.getPacketSender().sendString(8162, "@str@I need to bring Denath 100 noted rocktails before he");
			p.getPacketSender().sendString(8163, "@str@agrees to help the king. Then I must tell the king that");
			p.getPacketSender().sendString(8164, "@str@Denath has agreed to help.");
			p.getPacketSender().sendString(8165, "@str@I must let the king know that Denath will help us.");
			p.getPacketSender().sendString(8166, "@dre@Quest complete!");
		}
	}
	public static void giveReward(Player player) {
		if(player.getInventory().getFreeSlots() > 4) {
			//add rewards
		}
	}
	public static String getQuestTabPrefix(Player player) {
		if(player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() != 0) {
			return "@yel@";
		} else if(player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 6) {
			return "@gre@";
		}
		return "@red@";
	}

	private static final String questTitle = "The King's Task";
	private static final String[] questIntro ={
		"King Healthorg needs your help.", 
		"He has a task that needs done that only the strongest",
		"warriors can complete.",
	};
	private static final String[] questGuide ={
		"Help the King and he will reward you greatly.",
		"he can be found in the varrock castle."
	};
}
