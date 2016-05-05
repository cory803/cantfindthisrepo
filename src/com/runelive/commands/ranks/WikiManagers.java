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
import com.runelive.util.ForumDatabase;
import com.runelive.model.PlayerRights;

public class WikiManagers {
	
	/**
	* @Author Jonathan Sirens
	* Initiates Command
	**/
	
	public static void initiate_command(final Player player, String[] command, String wholeCommand) {
		String other_player_name = "invalid_name_process";
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
			World.sendYell("<col=0>[<col=31a4ff><shad=0><img=16>Wiki Manager<img=16></shad><col=0>] "+player.getUsername()+": "+yellMessage);	
		}
		switch(command[0].toLowerCase()) {
			case "promote":
				Player target = World.getPlayerByName(wholeCommand.substring(8));
				if(target.getRights() == PlayerRights.PLAYER) {
					if (target == null) {
						player.getPacketSender().sendMessage("Player must be online to give them wiki editor.");
					} else {
						target.setRights(PlayerRights.WIKI_EDITOR);
						target.getPacketSender().sendMessage("You have been given Wiki Editor!");
						player.getPacketSender().sendMessage("You have given "+wholeCommand.substring(8)+" the rank Wiki Editor!");
						target.getPacketSender().sendRights();
					}
				} else {
					player.getPacketSender().sendMessage("You can't promote someone who already has a rank.");
				}
			break;
			case "demote":
				Player target2 = World.getPlayerByName(wholeCommand.substring(7));
				if(target2.getRights() == PlayerRights.WIKI_EDITOR) {
					if (target2 == null) {
						player.getPacketSender().sendMessage("Player must be online to demote them!");
					} else {
						target2.setRights(PlayerRights.PLAYER);
						target2.getPacketSender().sendMessage("Your Wiki Editor rank has been taken.");
						player.getPacketSender().sendMessage("You have demoted "+wholeCommand.substring(7)+" from the rank Wiki Editor.");
						target2.getPacketSender().sendRights();
					}
				} else {
					player.getPacketSender().sendMessage("You can't demote someone who is not a wiki editor.");
				}
			break;
		}
	}
	
}