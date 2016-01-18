package com.ikov.world.content;

import com.ikov.model.container.impl.Equipment;
import com.ikov.util.Misc;
import com.ikov.world.content.ItemDegrading.DegradingItem;
import com.ikov.world.entity.impl.player.Player;

public class BrawlingGloves {
	
	private static int[][] GLOVES_SKILLS = 
		{{13855, 13}, {13848, 5}, {13857, 7},
		{13856, 10}, {13854, 17}, {13853, 21},
		{13852, 14}, {13851, 11}, {13850, 8}, {13849, 16}};
	
	public static int getExperience(Player p, int skill, int exp) {
		int playerGloves = p.getEquipment().getItems()[Equipment.HANDS_SLOT].getId();
		if(playerGloves <= 0)
			return exp;
		for (int i = 0; i < GLOVES_SKILLS.length; i++) {
			if ((playerGloves == GLOVES_SKILLS[i][0]) && (skill == GLOVES_SKILLS[i][1])) {
				exp *= 1.1;
			}
		}
		return exp;
	}
}
