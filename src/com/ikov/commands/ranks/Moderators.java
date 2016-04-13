package com.ikov.commands.ranks;

import com.ikov.GameSettings;
import com.ikov.model.Locations.Location;
import com.ikov.model.Position;
import com.ikov.util.Misc;
import com.ikov.world.World;
import com.ikov.world.content.PlayerLogs;
import com.ikov.world.content.PlayerPunishment;
import com.ikov.world.content.transportation.TeleportHandler;
import com.ikov.world.content.transportation.TeleportType;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.world.entity.impl.player.PlayerSaving;

public class Moderators {
	
	/**
	* @Author Jonathan Sirens
	* Initiates Command
	**/
	
	public static void initiate_command(final Player player, String[] command, String wholeCommand) {
		if (command[0].equals("unbanvote")) {
			String vote_player = wholeCommand.substring(10);
			if(!PlayerSaving.playerExists(vote_player)) {
				player.getPacketSender().sendMessage("Player "+vote_player+" does not exist.");
				return;
			} else {
				if(!PlayerPunishment.isVoteBanned(vote_player)) {
					player.getPacketSender().sendMessage("Player "+vote_player+" is not vote banned.");
					return;
				}
				Player other = World.getPlayerByName(vote_player);
				PlayerPunishment.unVoteBan(vote_player);
				other.getPacketSender().sendMessage("You have been unbanned from voting.");
				player.getPacketSender().sendMessage("You have unbanned "+other.getUsername()+" from voting.");
			}
		}
		if(command[0].equalsIgnoreCase("banvote")) {
			String vote_player = wholeCommand.substring(8);
			if(!PlayerSaving.playerExists(vote_player)) {
				player.getPacketSender().sendMessage("Player "+vote_player+" does not exist.");
				return;
			} else {
				if(PlayerPunishment.isVoteBanned(vote_player)) {
					player.getPacketSender().sendMessage("Player "+vote_player+" already has an active vote ban.");
					return;
				}
				Player other = World.getPlayerByName(vote_player);
				PlayerPunishment.voteBan(vote_player);
				other.getPacketSender().sendMessage("You have been banned from voting.");
				player.getPacketSender().sendMessage("You have banned "+other.getUsername()+" from voting.");
			}
		}
		if (command[0].equals("tele")) {
			int x = Integer.valueOf(command[1]), y = Integer.valueOf(command[2]);
			int z = player.getPosition().getZ();
			if (command.length > 3)
				z = Integer.valueOf(command[3]);
			Position position = new Position(x, y, z);
			player.moveTo(position);
			player.getPacketSender().sendMessage("Teleporting to " + position.toString());
		}
		if(wholeCommand.startsWith("silenceyell")) {
			String yellmute = wholeCommand.substring(12);
			Player punishee = World.getPlayerByName(yellmute);
			if(!PlayerSaving.playerExists(yellmute)) {
				player.getPacketSender().sendMessage("Player "+yellmute+" does not exist.");
				return;
			}
			punishee.setYellMute(true);
			punishee.getPacketSender().sendMessage("You have been yell muted! Please appeal on the forums.");
			player.getPacketSender().sendMessage("Player "+punishee.getUsername()+" was successfully muted!");
			
		}
		if(wholeCommand.startsWith("unsilenceyell")) {
			String yellmute = wholeCommand.substring(14);
			Player punishee = World.getPlayerByName(yellmute);
			if(!PlayerSaving.playerExists(yellmute)) {
				player.getPacketSender().sendMessage("Player "+yellmute+" does not exist.");
				return;
			}
			punishee.setYellMute(false);
			punishee.getPacketSender().sendMessage("You have been granted your yell ability again.");
			player.getPacketSender().sendMessage("Player "+punishee.getUsername()+" was successfully unmuted!");
			
		}
		if(command[0].equalsIgnoreCase("ban")) {
			String ban_player = wholeCommand.substring(4);
			if(!PlayerSaving.playerExists(ban_player)) {
				player.getPacketSender().sendMessage("Player "+ban_player+" does not exist.");
				return;
			} else {
				if(PlayerPunishment.isPlayerBanned(ban_player)) {
					player.getPacketSender().sendMessage("Player "+ban_player+" already has an active ban.");
					return;
				}
				Player other = World.getPlayerByName(ban_player);
				PlayerPunishment.ban(ban_player);
				if(other != null) {
					World.deregister(other);
				}
				player.getPacketSender().sendMessage("Player "+ban_player+" was successfully banned!");
			}
		}	
		if(command[0].equalsIgnoreCase("mute")) {
			String mute_player = wholeCommand.substring(5);
			if(!PlayerSaving.playerExists(mute_player)) {
				player.getPacketSender().sendMessage("Player "+mute_player+" does not exist.");
				return;
			} else {
				if(PlayerPunishment.isMuted(mute_player)) {
					player.getPacketSender().sendMessage("Player "+mute_player+" already has an active mute.");
					return;
				}
				Player other = World.getPlayerByName(mute_player);
				PlayerPunishment.mute(mute_player);
				player.getPacketSender().sendMessage("Player "+mute_player+" was successfully muted!");
				other.getPacketSender().sendMessage("You have been muted! Please appeal on the forums.");
			}
		}
		if(command[0].equalsIgnoreCase("ipmute")) {
			String mute_player = wholeCommand.substring(7);
			if(!PlayerSaving.playerExists(mute_player)) {
				player.getPacketSender().sendMessage("Player "+mute_player+" does not exist.");
				return;
			} else {
				if(PlayerPunishment.isIpMuted(mute_player)) {
					player.getPacketSender().sendMessage("Player "+mute_player+" already has an active ip mute.");
					return;
				}
				Player other = World.getPlayerByName(mute_player);
				PlayerPunishment.ipMute(mute_player);
				player.getPacketSender().sendMessage("Player "+mute_player+" was successfully ip muted!");
				other.getPacketSender().sendMessage("You have been ip muted! Please appeal on the forums.");
			}
		}
		if(command[0].equalsIgnoreCase("unipmute")) {
			String mute_player = wholeCommand.substring(9);
			if(!PlayerSaving.playerExists(mute_player)) {
				player.getPacketSender().sendMessage("Player "+mute_player+" does not exist.");
				return;
			} else {
				if(!PlayerPunishment.isIpMuted(mute_player)) {
					player.getPacketSender().sendMessage("Player "+mute_player+" does not have an active ip mute!");
					return;
				}
				Player other = World.getPlayerByName(mute_player);
				PlayerPunishment.unIpMute(mute_player);
				player.getPacketSender().sendMessage("Player "+mute_player+" was successfully unipmuted!");
				other.getPacketSender().sendMessage("You have been unipmuted!");
			}
		}
		if(command[0].equalsIgnoreCase("unmute")) {
			String mute_player = wholeCommand.substring(7);
			if(!PlayerSaving.playerExists(mute_player)) {
				player.getPacketSender().sendMessage("Player "+mute_player+" does not exist.");
				return;
			} else {
				if(!PlayerPunishment.isMuted(mute_player)) {
					player.getPacketSender().sendMessage("Player "+mute_player+" is not muted.");
					return;
				}
				Player other = World.getPlayerByName(mute_player);
				PlayerPunishment.unMute(mute_player);
				player.getPacketSender().sendMessage("Player "+mute_player+" was successfully unmuted!");
				other.getPacketSender().sendMessage("You have been unmuted!");
			}
		}
		if(command[0].equalsIgnoreCase("ipban")) {
			String ban_player = wholeCommand.substring(6);
			if(!PlayerSaving.playerExists(ban_player)) {
				player.getPacketSender().sendMessage("Player "+ban_player+" does not exist.");
				return;
			} else {
				Player other = World.getPlayerByName(ban_player);
				String ip;
				if(other == null) {
					ip = PlayerPunishment.getLastIpAddress(ban_player);
				} else {
					ip = other.getHostAddress();
				}
				if(PlayerPunishment.isIpBanned(ip)) {
					player.getPacketSender().sendMessage("Player "+ban_player+" already has an active ip ban on "+ip+".");
					return;
				}
				PlayerPunishment.ipBan(ip);
				PlayerPunishment.ban(ban_player);
				if(other != null) {
					World.deregister(other);
				}
				player.getPacketSender().sendMessage("Player "+ban_player+" was successfully banned on ip "+ip+"!");
			}
		}
		if(command[0].equalsIgnoreCase("unipban")) {
			String ban_player = wholeCommand.substring(8);
			if(!PlayerSaving.playerExists(ban_player)) {
				player.getPacketSender().sendMessage("Player "+ban_player+" does not exist.");
				return;
			} else {
				Player other = World.getPlayerByName(ban_player);
				String ip;
				if(other == null) {
					ip = PlayerPunishment.getLastIpAddress(ban_player);
				} else {
					ip = other.getHostAddress();
				}
				if(!PlayerPunishment.isIpBanned(ip)) {
					player.getPacketSender().sendMessage("Player "+ban_player+" does not have an active ip ban on "+ip+".");
					return;
				}
				PlayerPunishment.unIpBan(ip);
				player.getPacketSender().sendMessage("Player "+ban_player+" was successfully unipbanned on ip "+ip+"!");
			}
		}
		if(command[0].equalsIgnoreCase("unban")) {
			String ban_player = wholeCommand.substring(6);
			if(!PlayerSaving.playerExists(ban_player)) {
				player.getPacketSender().sendMessage("Player "+ban_player+" does not exist.");
				return;
			} else {
				if(!PlayerPunishment.isPlayerBanned(ban_player)) {
					player.getPacketSender().sendMessage("Player "+ban_player+" is not banned.");
					return;
				}
				PlayerPunishment.unBan(ban_player);
				player.getPacketSender().sendMessage("Player "+ban_player+" was successfully unbanned.");
			}
		}
		if(command[0].equalsIgnoreCase("teleto")) {
			String playerToTele = wholeCommand.substring(7);
			Player player2 = World.getPlayerByName(playerToTele);
			if(player2 == null) {
				player.getPacketSender().sendMessage("Cannot find that player online..");
				return;
			} else {
				boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy()) && player.getRegionInstance() == null && player2.getRegionInstance() == null;
				if(canTele && player.getLocation() != Location.DUNGEONEERING) {
					TeleportHandler.teleportPlayer(player, player2.getPosition().copy(), TeleportType.NORMAL);
					player.getPacketSender().sendMessage("Teleporting to player: "+player2.getUsername()+"");
				} else {
					if(player2.getLocation() == Location.DUNGEONEERING) {
						player.getPacketSender().sendMessage("You can not teleport to this player while they are dungeoneering.");
					} else {
						player.getPacketSender().sendMessage("You can not teleport to this player at the moment. Minigame maybe?");
					}
				}
			}
		}
		if(command[0].equalsIgnoreCase("teletome")) {
			String playerToTele = wholeCommand.substring(9);
			Player player2 = World.getPlayerByName(playerToTele);
			if (World.getPlayerByName(playerToTele).getLocation() == Location.DUEL_ARENA) {
				player.getPacketSender().sendMessage("Why are you trying to move a player out of duel arena?");
				return;
			}
			if (player2.getLocation() == Location.DUNGEONEERING)  {
				player.getPacketSender().sendMessage("You cannot teleport a player out of dung?");
				return;
			}
			if (player.getLocation() == Location.WILDERNESS)  {
				player.getPacketSender().sendMessage("You cannot teleport a player into the wild... What're you thinking?");
				return;
			}
			if (player2.getLocation() == Location.DUEL_ARENA) {
				player.getPacketSender().sendMessage("You cannot do this to someone in duel arena.");
				return;
			}
			boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy()) && player.getRegionInstance() == null && player2.getRegionInstance() == null;
			if(canTele) {
				TeleportHandler.teleportPlayer(player2, player.getPosition().copy(), TeleportType.NORMAL);
				player.getPacketSender().sendMessage("Teleporting player to you: "+player2.getUsername()+"");
				player2.getPacketSender().sendMessage("You're being teleported to "+player.getUsername()+"...");
			} else
				player.getPacketSender().sendMessage("You can not teleport that player at the moment. Maybe you or they are in a minigame?");
		}
	}
	
}