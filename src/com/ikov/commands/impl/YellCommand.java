package com.ikov.commands.impl;

import java.util.concurrent.TimeUnit;

import com.ikov.GameSettings;
import com.ikov.commands.Command;
import com.ikov.model.GameMode;
import com.ikov.model.PlayerRights;
import com.ikov.util.Misc;
import com.ikov.world.World;
import com.ikov.world.content.PlayerPunishment;
import com.ikov.world.entity.impl.player.Player;

public class YellCommand extends Command {

	public YellCommand(String name) {
		super(name, PlayerRights.PLAYER);
	}

	@Override
	public boolean execute(Player player, String input) throws Exception {
		if (!GameSettings.YELL_STATUS) {
			player.getPacketSender().sendMessage("Yell is currently turned off, please try again in 30 minutes!");
			return false;
		}
		if (PlayerPunishment.isMuted(player.getUsername()) || PlayerPunishment.isIpMuted(player.getHostAddress())) {
			player.getPacketSender().sendMessage("You are muted and cannot yell.");
			return false;
		}
		if (player.isYellMute()) {
			player.getPacketSender().sendMessage("You are muted from yelling and cannot yell.");
			return false;
		}
		if (!player.getRights().isStaff()) {
			if (input.contains("<img=") || input.contains("<col=") || input.contains("<shad=")) {
				player.getPacketSender().sendMessage("You are not aloud to put these symbols in your yell message.");
				return false;
			}
		}

		String title = "";
		int timer = 0;

		switch (player.getRights()) {
		case PLAYER:
			if (player.getDonorRights() == 0) {
				return false;
			}
			if (!World.isGlobalYell()) {
				player.getPacketSender().sendMessage("An admin has temporarily disabled the global yell channel.");
				return false;
			}
			switch (player.getDonorRights()) {
			case 1: // Normal donor
				title = "<img=7> <col=0>[<col=ff0000>Donator<col=0>]";
				timer = 35;
				break;
			case 2: // Super Donator
				title = "<img=8> <col=0>[@blu@Super@bla@]";
				timer = 25;
				break;
			case 3: // Extreme Donator
				title = "<img=9> <col=0>[<col=2FAC45>Extreme<col=0>]";
				timer = 10;
				break;
			case 4: // Legendary Donator
				if (!player.getYellTag().equals("invalid_yell_tag_set")) {
					title = "<img=10> <col=0>[<col=3E0069>" + player.getYellTag() + "<col=0>]";
				} else {
					title = "<img=10> <col=0>[<col=3E0069>Legendary<col=0>]";
				}
				timer = 5;
				break;
			case 5: // Uber Donator
				if (!player.getYellTag().equals("invalid_yell_tag_set")) {
					title = "<img=11> <col=0>[<col=ffff00><shad=0>" + player.getYellTag() + "<col=0></shad>]";
				} else {
					title = "<img=11> <col=0>[<col=ffff00><shad=0>Uber</shad><col=0>]";
				}
				break;
			}
			if (player.getGameMode() == GameMode.IRONMAN) {
				title = "<img=12> [<col=808080><shad=0>Ironman</col></shad>]";
			}
			if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
				title = "<img=13> [<col=ffffff><shad=0>Hardcore</col></shad>]";
			}
			break;
		case ADMINISTRATOR:
			title = "<col=0>[<col=ffff00><shad=0><img=2>Administrator<img=2></shad><col=0>]";
			break;
		case COMMUNITY_MANAGER:
			title = "<col=0>[<col=ffff00><shad=0><img=2>Administrator<img=2></shad><col=0>]";
			break;
		case GLOBAL_MOD:
			title = "<col=0>[<col=00ff00><shad=0><img=6>Global Mod<img=6></shad><col=0>]";
			break;
		case MODERATOR:
			title = "<col=0>[<col=31a4ff><shad=0><img=1>Moderator<img=1></shad><col=0>]";
			break;
		case OWNER:
			title = "<col=0>[<col=ff0000><shad=0><img=3>Owner<img=3></shad><col=0>]";
			break;
		case STAFF_MANAGER:
			title = "<col=0>[<col=000000><shad=ffffff><img=17>Staff Manager<img=17></shad><col=0>]";
			break;
		case SUPPORT:
			title = "<col=0>[<col=589fe1><shad=0><img=4>Support<img=4></shad><col=0>]";
			break;
		case WIKI_EDITOR:
			title = "<col=0>[<col=ff7f00><shad=0><img=15>Wiki Editor<img=15></shad><col=0>]";
			break;
		case WIKI_MANAGER:
			title = "<col=0>[<col=31a4ff><shad=0><img=16>Wiki Manager<img=16></shad><col=0>]";
			break;
		case YOUTUBER:
			break;
		}
		if (timer > 0) {
			if (!player.getYellTimer().elapsed(timer * 1000)) {
				player.getPacketSender().sendMessage("Do not flood the yell channel. You must wait another " + Misc.getTimeLeft(0, timer, TimeUnit.SECONDS) + " seconds before yelling again.");
				return false;
			}
			player.getYellTimer().reset();
		}
		World.sendYell(title + " " + player.getUsername() + ": " + input);
		return true;
	}

}
