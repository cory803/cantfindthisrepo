package com.ikov.commands.ranks;

import java.util.concurrent.TimeUnit;

import com.ikov.GameSettings;
import com.ikov.model.GameMode;
import com.ikov.model.Locations.Location;
import com.ikov.util.Misc;
import com.ikov.world.content.transportation.TeleportHandler;
import com.ikov.model.PlayerRights;
import com.ikov.model.Position;
import com.ikov.world.World;
import com.ikov.world.content.PlayerPunishment;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.world.content.skill.impl.dungeoneering.Dungeoneering;

public class ExtremeDonators {
	
	/**
	* @Author Jonathan Sirens
	* Initiates Command
	**/
	
	public static void initiate_command(final Player player, String[] command, String wholeCommand) {
		if(player.isJailed()) {
			player.getPacketSender().sendMessage("You cannot use commands in jail... You're in jail.");
			return;
		}
		if(wholeCommand.toLowerCase().startsWith("yell")) {
			if(player.getRights() != PlayerRights.PLAYER) {
				return;
			}
			if(PlayerPunishment.isMuted(player.getUsername()) || PlayerPunishment.isIpMuted(player.getHostAddress())) {
				player.getPacketSender().sendMessage("You are muted and cannot yell.");
				return;
			}
			if(World.isGlobalYell() == false) {
				player.getPacketSender().sendMessage("An admin has temporarily disabled the global yell channel.");
				return;
			}
			if(player.isYellMute()) {
				player.getPacketSender().sendMessage("You are muted from yelling and cannot yell.");
				return;
			}
			if(!GameSettings.YELL_STATUS) {
				player.getPacketSender().sendMessage("Yell is currently turned off, please try again in 30 minutes!");
				return;
			}
			if(!player.getYellTimer().elapsed(10000)) {
				player.getPacketSender().sendMessage("Do not flood the yell channel. You must wait another "+Misc.getTimeLeft(0, 10, TimeUnit.SECONDS)+" seconds before yelling again.");
				return;
			}
			String yellMessage = wholeCommand.substring(4, wholeCommand.length());
			if(yellMessage.contains("<img=") || yellMessage.contains("<col=") || yellMessage.contains("<shad=")) {
				player.getPacketSender().sendMessage("You are not aloud to put these symbols in your yell message.");
				return;
			}
			if(player.getGameMode() == GameMode.IRONMAN) {
				World.sendYell("<img=12> [<col=808080><shad=0>Ironman</col></shad>] "+player.getUsername()+": "+yellMessage);	
				player.getYellTimer().reset();
				return;
			}
			if(player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
				World.sendYell("<img=13> [<col=ffffff><shad=0>Hardcore</col></shad>] "+player.getUsername()+": "+yellMessage);	
				player.getYellTimer().reset();
				return;
			}
			World.sendYell("<img=9> <col=0>[<col=2FAC45>Extreme<col=0>] "+player.getUsername()+": "+yellMessage);	
			player.getYellTimer().reset();
		}
		if (command[0].equals("ezone")) {
			if(Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if(player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			Position position = new Position(3362, 9640, 0);
			int ran = Misc.getRandom(3);
			switch(ran) {
				case 0:
					position = new Position(3363, 9641, 0);
				break;
				case 1:
					position = new Position(3364, 9640, 0);
				break;
				case 2:
					position = new Position(3363, 9639, 0);
				break;
				case 3:
					position = new Position(3362, 9640, 0);
				break;
			}
			TeleportHandler.teleportPlayer(player, position, player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("<img=9><col=00ff00><shad=0> Welcome to the Extreme Donator Zone!");
		}
		if (command[0].equals("dzone")) {
			if(Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if(player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			Position position = new Position(2514, 3860, 0);
			player.moveTo(position);
			player.getPacketSender().sendMessage("[<col=ff0000>Donator Zone</col>] Welcome to the donator zone.");
		}
	}
	
}