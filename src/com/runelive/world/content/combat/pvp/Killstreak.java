package com.runelive.world.content.combat.pvp;

import java.util.ArrayList;
import java.util.List;

import com.runelive.model.Locations.Location;
import com.runelive.util.Misc;
import com.runelive.world.content.Achievements;
import com.runelive.world.content.Achievements.AchievementData;
import com.runelive.world.content.Artifacts;
import com.runelive.world.entity.impl.player.Player;
import com.runelive.world.World;

public class Killstreak {

	public static void kill(Player player, Player other, int killstreak) {
		int pkPoints = player.getSkillManager().getCombatLevel() + other.getSkillManager().getCombatLevel() / 3;
		if(player.getPlayerKillingAttributes().getTarget() != null) {
			if(player.getPlayerKillingAttributes().getTarget().getUsername().equalsIgnoreCase(other.getUsername())) {
				switch(player.getDonorRights()) {
					case 0:
						pkPoints += 10;
						break;
					case 1:
						pkPoints += 15;
						break;
					case 2:
						pkPoints += 20;
						break;
					case 3:
						pkPoints += 30;
						break;
					case 4:
						pkPoints += 40;
						break;
					case 5:
						pkPoints += 50;
						break;
				}
			}
		}
		int otherKillstreak = other.getPlayerKillingAttributes().getPlayerKillStreak();
		if(otherKillstreak >= 10 && otherKillstreak < 25) {
			pkPoints += 25;
			World.sendWildernessMessage("<icon=2><shad=ff0000>"+other.getUsername()+"'s killstreak of "+other.getPlayerKillingAttributes().getPlayerKillStreak()+" has been ended by "+player.getUsername()+"!");
		} else if(otherKillstreak >= 25 && otherKillstreak < 50) {
			pkPoints += 50;
			World.sendWildernessMessage("<icon=2><shad=ff0000>"+other.getUsername()+"'s killstreak of "+other.getPlayerKillingAttributes().getPlayerKillStreak()+" has been ended by "+player.getUsername()+"!");
		} else if(otherKillstreak >= 50 && otherKillstreak < 75) {
			pkPoints += 125;
			World.sendMessage("<icon=2><shad=ff0000>"+other.getUsername()+"'s killstreak of "+other.getPlayerKillingAttributes().getPlayerKillStreak()+" has been ended by "+player.getUsername()+"!");
		} else if(otherKillstreak >= 75 && otherKillstreak < 100) {
			pkPoints += 250;
			World.sendMessage("<icon=2><shad=ff0000>"+other.getUsername()+"'s killstreak of "+other.getPlayerKillingAttributes().getPlayerKillStreak()+" has been ended by "+player.getUsername()+"!");
		} else if(otherKillstreak >= 100) {
			pkPoints += 500;
			World.sendMessage("<icon=2><shad=ff0000>"+other.getUsername()+"'s killstreak of "+other.getPlayerKillingAttributes().getPlayerKillStreak()+" has been ended by "+player.getUsername()+"!");
		}
		switch(killstreak) {
			case 10:
				pkPoints += 50;
				World.sendWildernessMessage("<icon=2><shad=ff0000>"+player.getUsername()+" is currently on a "+killstreak+" killstreak bounty of 25 PK Points!");
				break;
			case 25:
				pkPoints += 100;
				World.sendWildernessMessage("<icon=2><shad=ff0000>"+player.getUsername()+" is currently on a "+killstreak+" killstreak bounty of 50 PK Points!");
				break;
			case 50:
				pkPoints += 250;
				World.sendMessage("<icon=2><shad=ff0000>"+player.getUsername()+" is currently on a "+killstreak+" killstreak bounty of 125 PK Points!");
				break;
			case 75:
				pkPoints += 500;
				World.sendMessage("<icon=2><shad=ff0000>"+player.getUsername()+" is currently on a "+killstreak+" killstreak bounty of 250 PK Points!");
				break;
			case 100:
				pkPoints += 1000;
				World.sendMessage("<icon=2><shad=ff0000>"+player.getUsername()+" is currently on a "+killstreak+" killstreak bounty of 500 PK Points!");
				break;
		}
		player.getPointsHandler().setPkPoints(pkPoints, true, false);
		player.getPacketSender().sendMessage("You've received "+pkPoints+" PK Points!");
	}
}
