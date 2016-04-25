package com.runelive.commands.ranks;

import com.runelive.GameSettings;
import com.runelive.model.Locations.Location;
import com.runelive.model.Position;
import com.runelive.util.Misc;
import com.runelive.world.World;
import com.runelive.world.content.PlayerLogs;
import com.runelive.world.content.PlayerPunishment;
import com.runelive.world.content.transportation.TeleportHandler;
import com.runelive.world.content.transportation.TeleportType;
import com.runelive.world.entity.impl.player.Player;
import com.runelive.world.entity.impl.player.PlayerSaving;

public class WikiEditors {
	
	/**
	* @Author Jonathan Sirens
	* Initiates Command
	**/
	
	public static void initiate_command(final Player player, String[] command, String wholeCommand) {
		if(wholeCommand.toLowerCase().startsWith("yell")) {
			if(PlayerPunishment.isMuted(player.getUsername()) || PlayerPunishment.isIpMuted(player.getHostAddress())) {
				player.getPacketSender().sendMessage("You are muted and cannot yell.");
				return;
			}
			if(World.isGlobalYell() == false) {
				player.getPacketSender().sendMessage("An admin has temporarily disabled the global yell channel.");
				return;
			}
			if(!GameSettings.YELL_STATUS) {
				player.getPacketSender().sendMessage("Yell is currently turned off, please try again in 30 minutes!");
				return;
			}
			String yellMessage = wholeCommand.substring(4, wholeCommand.length());
			World.sendYell("<col=0>[<col=ff7f00><shad=0><img=15>Wiki Editor<img=15></shad><col=0>] "+player.getUsername()+": "+yellMessage);	
		}
	}
	
}