package com.chaos.model.input.impl;

import com.chaos.GameSettings;
import com.chaos.model.Skill;
import com.chaos.model.input.EnterAmount;
import com.chaos.util.Misc;
import com.chaos.world.content.dialogue.DialogueManager;
import com.chaos.world.entity.impl.player.Player;

public class BuyDungExperience extends EnterAmount {

	@Override
	public void handleAmount(Player player, long value) {
		int amount = (int) value;
		if (value > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE;
		}
		// player.getPacketSender().sendInterfaceRemoval();
		if (player.getPointsHandler().getDungeoneeringTokens() < value / 3) {
			DialogueManager.sendStatement(player, "You do not have enough Dungeoneering Tokens for this!");
		} else if (value < 100) {
			DialogueManager.sendStatement(player, "You can't purchase under 100 experience.");
		} else {
			int amt = (int) value / 3;
			int xp = (int) value;
			player.getPointsHandler().setDungeoneeringTokens(-amt, true);
			if (GameSettings.DOUBLE_EXP) {
				xp /= 2;
			}
			player.getSkillManager().addSkillExperience(Skill.DUNGEONEERING, xp);
			DialogueManager.sendStatement(player, "You have purchased " + Misc.format(value) + " experience.");
		}
	}

}
