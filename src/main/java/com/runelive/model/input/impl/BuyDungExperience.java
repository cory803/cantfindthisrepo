package com.runelive.model.input.impl;

import com.runelive.model.Skill;
import com.runelive.model.input.EnterAmount;
import com.runelive.model.player.GameMode;
import com.runelive.util.Misc;
import com.runelive.world.content.PlayerPanel;
import com.runelive.world.entity.impl.player.Player;

public class BuyDungExperience extends EnterAmount {

	@Override
	public void handleAmount(Player player, long value) {
		int amount = (int) value;
		if (value > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE;
		}
		if (amount < 10000) {
			player.getPacketSender().sendMessage("You can't purchase under 10,000 experience.");
			return;
		} else {
			int experience;
			if(player.getGameModeAssistant().getGameMode() == GameMode.REALISM || player.getGameModeAssistant().getGameMode() == GameMode.IRONMAN) {
				experience = 10;
			} else {
				experience = 100;
			}
			int amt = (int) amount / experience;
			int xp = (int) amount;
			if(player.getPointsHandler().getDungeoneeringTokens() < amt) {
				player.getPacketSender().sendMessage("You do not have enough dungeoneering tokens.");
				return;
			}
			player.getPointsHandler().setDungeoneeringTokens(-amt, true);
			player.getSkillManager().addExactExperience(Skill.DUNGEONEERING, xp, false);
			player.getPacketSender().sendMessage("You have purchased "+Misc.format(xp)+" Dungeoneering experience for "+Misc.format(amt)+" tokens.");
			PlayerPanel.refreshPanel(player);
		}
	}

}
