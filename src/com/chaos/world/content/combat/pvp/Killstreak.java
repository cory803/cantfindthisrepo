package com.chaos.world.content.combat.pvp;

import com.chaos.util.Misc;
import com.chaos.world.World;
import com.chaos.world.entity.impl.player.Player;

public class Killstreak {

	public static void kill(Player player, Player other, int killstreak) {
		int pkPoints = player.getWildernessLevel() + (player.getWildernessLevel() / 2) + Misc.random(60, 100);
		int otherKillstreak = other.getPlayerKillingAttributes().getPlayerKillStreak();
		if (otherKillstreak >= 10 && otherKillstreak < 25) {
			pkPoints += 25;
			World.sendWildernessMessage("<icon=2><shad=ff0000>" + other.getUsername() + "'s killstreak of "
					+ other.getPlayerKillingAttributes().getPlayerKillStreak() + " has been ended by "
					+ player.getUsername() + "!");
		} else if (otherKillstreak >= 25 && otherKillstreak < 50) {
			pkPoints += 50;
			World.sendWildernessMessage("<icon=2><shad=ff0000>" + other.getUsername() + "'s killstreak of "
					+ other.getPlayerKillingAttributes().getPlayerKillStreak() + " has been ended by "
					+ player.getUsername() + "!");
		} else if (otherKillstreak >= 50 && otherKillstreak < 75) {
			pkPoints += 125;
			World.sendMessage("<icon=2><shad=ff0000>" + other.getUsername() + "'s killstreak of "
					+ other.getPlayerKillingAttributes().getPlayerKillStreak() + " has been ended by "
					+ player.getUsername() + "!");
		} else if (otherKillstreak >= 75 && otherKillstreak < 100) {
			pkPoints += 250;
			World.sendMessage("<icon=2><shad=ff0000>" + other.getUsername() + "'s killstreak of "
					+ other.getPlayerKillingAttributes().getPlayerKillStreak() + " has been ended by "
					+ player.getUsername() + "!");
		} else if (otherKillstreak >= 100) {
			pkPoints += 500;
			World.sendMessage("<icon=2><shad=ff0000>" + other.getUsername() + "'s killstreak of "
					+ other.getPlayerKillingAttributes().getPlayerKillStreak() + " has been ended by "
					+ player.getUsername() + "!");
		}
		switch (killstreak) {
		case 10:
			pkPoints += 50;
			World.sendWildernessMessage("<icon=2><shad=ff0000>" + player.getUsername() + " is currently on a "
					+ killstreak + " killstreak bounty of 25 PK Points!");
			break;
		case 25:
			pkPoints += 100;
			World.sendWildernessMessage("<icon=2><shad=ff0000>" + player.getUsername() + " is currently on a "
					+ killstreak + " killstreak bounty of 50 PK Points!");
			break;
		case 50:
			pkPoints += 250;
			World.sendMessage("<icon=2><shad=ff0000>" + player.getUsername() + " is currently on a " + killstreak
					+ " killstreak bounty of 125 PK Points!");
			break;
		case 75:
			pkPoints += 500;
			World.sendMessage("<icon=2><shad=ff0000>" + player.getUsername() + " is currently on a " + killstreak
					+ " killstreak bounty of 250 PK Points!");
			break;
		case 100:
			pkPoints += 1000;
			World.sendMessage("<icon=2><shad=ff0000>" + player.getUsername() + " is currently on a " + killstreak
					+ " killstreak bounty of 500 PK Points!");
			break;
		}
		player.getPointsHandler().setPkPoints(pkPoints, true, false);
		player.getPacketSender().sendMessage("You've received " + pkPoints + " PK Points!");
	}
}
