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

public class GlobalModerators {
	
	/**
	* @Author Jonathan Sirens
	* Initiates Command
	**/
	
	public static void initiate_command(final Player player, String[] command, String wholeCommand) {
		if (command[0].equals("staffzone")) {
			if (command.length > 1 && command[1].equals("all")) {
				for (Player players : World.getPlayers()) {
					if (players != null) {
						if (players.getRights().isStaff()) {
							TeleportHandler.teleportPlayer(players, new Position(2846, 5147), TeleportType.NORMAL);
						}
					}
				}
			} else {
				TeleportHandler.teleportPlayer(player, new Position(2846, 5147), TeleportType.NORMAL);
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
		if(command[0].equalsIgnoreCase("ipban")) {
			String ban_player = wholeCommand.substring(6);
			if(!PlayerSaving.accountExists(player, ban_player)) {
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
		if(command[0].equalsIgnoreCase("serialban") ||(command[0].equalsIgnoreCase("cpuban")) ||(command[0].equalsIgnoreCase("macban"))) {
			String ban_player = command[1];
			if(!PlayerSaving.accountExists(player, ban_player)) {
				player.getPacketSender().sendMessage("Player "+ban_player+" does not exist.");
				return;
			} else {
				Player other = World.getPlayerByName(ban_player);
				String mac;
				if(other == null) {
					mac = PlayerPunishment.getLastMacAddress(ban_player);
				} else {
					mac = other.getMacAddress();
				}
				if(PlayerPunishment.isMacBanned(mac)) {
					player.getPacketSender().sendMessage("Player "+ban_player+" already has an active mac ban on "+mac+".");
					return;
				}
				PlayerPunishment.macBan(mac);
				if(other != null) {
					World.deregister(other);
				}
				player.getPacketSender().sendMessage("Player "+ban_player+" was successfully mac banned on mac "+mac+"!");
			}
		}		
		if(command[0].equalsIgnoreCase("unserialban") || command[0].equalsIgnoreCase("unmacban") || command[0].equalsIgnoreCase("uncpuban")) {
			String ban_player = command[1];
			if(!PlayerSaving.accountExists(player, ban_player)) {
				player.getPacketSender().sendMessage("Player "+ban_player+" does not exist.");
				return;
			} else {
				Player other = World.getPlayerByName(ban_player);
				String mac;
				if(other == null) {
					mac = PlayerPunishment.getLastMacAddress(ban_player);
				} else {
					mac = player.getMacAddress();
				}
				if(!PlayerPunishment.isMacBanned(mac)) {
					player.getPacketSender().sendMessage("Player "+ban_player+" does not have an active mac ban on "+mac+".");
					return;
				}
				PlayerPunishment.unMacBan(mac);
				player.getPacketSender().sendMessage("Player "+ban_player+" was successfully un mac banned on mac "+mac+"!");
			}
		}
		if(command[0].equalsIgnoreCase("unipban")) {
			String ban_player = wholeCommand.substring(8);
			if(!PlayerSaving.accountExists(player, ban_player)) {
				player.getPacketSender().sendMessage("Player "+ban_player+" does not exist.");
				return;
			} else {
				Player other = World.getPlayerByName(ban_player);
				String ip;
				if(other == null) {
					ip = PlayerPunishment.getLastIpAddress(ban_player);
				} else {
					ip = player.getHostAddress();
				}
				if(!PlayerPunishment.isIpBanned(ip)) {
					player.getPacketSender().sendMessage("Player "+ban_player+" does not have an active ip ban on "+ip+".");
					return;
				}
				PlayerPunishment.unIpBan(ip);
				player.getPacketSender().sendMessage("Player "+ban_player+" was successfully unipbanned on ip "+ip+"!");
			}
		}
		if(command[0].equalsIgnoreCase("massban")) {
			String ban_player = wholeCommand.substring(8);
			if(!PlayerSaving.accountExists(player, ban_player)) {
				player.getPacketSender().sendMessage("Player "+ban_player+" does not exist.");
				return;
			} else {
				Player other = World.getPlayerByName(ban_player);
				String mac;
				if(other == null) {
					mac = PlayerPunishment.getLastMacAddress(ban_player);
				} else {
					mac = other.getMacAddress();
				}
				String ip;
				if(other == null) {
					ip = PlayerPunishment.getLastIpAddress(ban_player);
				} else {
					ip = other.getHostAddress();
				}	
				String address;
				if(other == null) {
					address = PlayerPunishment.getLastComputerAddress(ban_player);
				} else {
					address = other.getComputerAddress();
				}
				PlayerPunishment.pcBan(address);
				PlayerPunishment.macBan(mac);
				PlayerPunishment.ipBan(ip);
				PlayerPunishment.ban(ban_player);
				if(other != null) {
					World.deregister(other);
				}
				player.getPacketSender().sendMessage("Player "+ban_player+" was successfully mass banned!");
			}
		}
		if(command[0].equalsIgnoreCase("unmassban")) {
			String ban_player = wholeCommand.substring(10);
			if(!PlayerSaving.accountExists(player, ban_player)) {
				player.getPacketSender().sendMessage("Player "+ban_player+" does not exist.");
				return;
			} else {
				Player other = World.getPlayerByName(ban_player);
				String mac;
				if(other == null) {
					mac = PlayerPunishment.getLastMacAddress(ban_player);
				} else {
					mac = other.getMacAddress();
				}
				String ip;
				if(other == null) {
					ip = PlayerPunishment.getLastIpAddress(ban_player);
				} else {
					ip = other.getHostAddress();
				}	
				String address;
				if(other == null) {
					address = PlayerPunishment.getLastComputerAddress(ban_player);
				} else {
					address = other.getComputerAddress();
				}
				PlayerPunishment.unPcBan(address);
				PlayerPunishment.unMacBan(mac);
				PlayerPunishment.unVoteBan(ban_player);
				PlayerPunishment.unIpBan(ip);
				PlayerPunishment.unBan(ban_player);
				player.getPacketSender().sendMessage("Player "+ban_player+" was successfully un mass banned!");
			}
		}
		if (command[0].equals("unbanvote")) {
			String vote_player = command[1];
			if(!PlayerSaving.accountExists(player, vote_player)) {
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
			String vote_player = command[1];
			if(!PlayerSaving.accountExists(player, vote_player)) {
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
		if(wholeCommand.startsWith("silenceyell")) {
			String yellmute = wholeCommand.substring(12);
			Player punishee = World.getPlayerByName(yellmute);
			if(!PlayerSaving.accountExists(player, yellmute)) {
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
			if(!PlayerSaving.accountExists(player, yellmute)) {
				player.getPacketSender().sendMessage("Player "+yellmute+" does not exist.");
				return;
			}
			punishee.setYellMute(false);
			punishee.getPacketSender().sendMessage("You have been granted your yell ability again.");
			player.getPacketSender().sendMessage("Player "+punishee.getUsername()+" was successfully unmuted!");
			
		}
		if(wholeCommand.startsWith("unjail")) {
			String jail_punishee = wholeCommand.substring(7);
			Player punishee = World.getPlayerByName(jail_punishee);
			punishee.setJailed(false);
			punishee.forceChat("Im free!!! I'm finally out of jail... Hooray!");
			punishee.moveTo(new Position(3087, 3502, 0));
		}
		if(wholeCommand.startsWith("jail")) {
				String jail_punishee = wholeCommand.substring(5);
				Player punishee = World.getPlayerByName(jail_punishee);
				if(!PlayerSaving.accountExists(player, jail_punishee)) {
					player.getPacketSender().sendMessage("Player "+jail_punishee+" does not exist.");
					return;
				} else {
				int cellAmounts = Misc.getRandom(1);
				switch(cellAmounts) {
				case 1:
					punishee.setJailed(true);
					punishee.forceChat("Ahh shit... They put me in jail.");
					punishee.moveTo(new Position(1969, 5011, 0));
					player.getPacketSender().sendMessage("You have sent the player "+jail_punishee+" to jail for breaking the rules.");
				break;
				case 2:
					punishee.setJailed(true);
					punishee.forceChat("Ahh shit... They put me in jail.");
					punishee.moveTo(new Position(1969, 5008, 0));
					player.getPacketSender().sendMessage("You have sent the player "+jail_punishee+" to jail for breaking the rules.");
				break;
				case 3:
					punishee.setJailed(true);
					punishee.forceChat("Ahh shit... They put me in jail.");
					punishee.moveTo(new Position(1969, 5005, 0));
					player.getPacketSender().sendMessage("You have sent the player "+jail_punishee+" to jail for breaking the rules.");
				break;
				case 4:
					punishee.setJailed(true);
					punishee.forceChat("Ahh shit... They put me in jail.");
					punishee.moveTo(new Position(1969, 5002, 0));
					player.getPacketSender().sendMessage("You have sent the player "+jail_punishee+" to jail for breaking the rules.");
				break;
				case 5:
					punishee.setJailed(true);
					punishee.forceChat("Ahh shit... They put me in jail.");
					punishee.moveTo(new Position(1969, 4999, 0));
					player.getPacketSender().sendMessage("You have sent the player "+jail_punishee+" to jail for breaking the rules.");
				break;
				case 6:
					punishee.setJailed(true);
					punishee.forceChat("Ahh shit... They put me in jail.");
					punishee.moveTo(new Position(1980, 5011, 0));
					player.getPacketSender().sendMessage("You have sent the player "+jail_punishee+" to jail for breaking the rules.");
				break;
				case 7:
					punishee.setJailed(true);
					punishee.forceChat("Ahh shit... They put me in jail.");
					punishee.moveTo(new Position(1980, 5008, 0));
					player.getPacketSender().sendMessage("You have sent the player "+jail_punishee+" to jail for breaking the rules.");
				break;
				case 8:
					punishee.setJailed(true);
					punishee.forceChat("Ahh shit... They put me in jail.");
					punishee.moveTo(new Position(1980, 5005, 0));
					player.getPacketSender().sendMessage("You have sent the player "+jail_punishee+" to jail for breaking the rules.");
				break;
				case 9:
					punishee.setJailed(true);
					punishee.forceChat("Ahh shit... They put me in jail.");
					punishee.moveTo(new Position(1980, 5002, 0));
					player.getPacketSender().sendMessage("You have sent the player "+jail_punishee+" to jail for breaking the rules.");
				break;
				case 10:
					punishee.setJailed(true);
					punishee.forceChat("Ahh shit... They put me in jail.");
					punishee.moveTo(new Position(1980, 4999, 0));
					player.getPacketSender().sendMessage("You have sent the player "+jail_punishee+" to jail for breaking the rules.");
				break;
				default:
				}
			}
		}
		if(command[0].equalsIgnoreCase("ban")) {
			String ban_player = wholeCommand.substring(4);
			if(!PlayerSaving.accountExists(player, ban_player)) {
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
			if(!PlayerSaving.accountExists(player, mute_player)) {
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
			if(!PlayerSaving.accountExists(player, mute_player)) {
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
			if(!PlayerSaving.accountExists(player, mute_player)) {
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
			if(!PlayerSaving.accountExists(player, mute_player)) {
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
			if(!PlayerSaving.accountExists(player, ban_player)) {
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
			if(!PlayerSaving.accountExists(player, ban_player)) {
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
			if(!PlayerSaving.accountExists(player, ban_player)) {
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
		if(command[0].equalsIgnoreCase("saveall")) {
			World.savePlayers();
			player.getPacketSender().sendMessage("Saved players!");
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
		if(command[0].equalsIgnoreCase("movehome")) {
			String player2 = command[1];
			player2 = Misc.formatText(player2.replaceAll("_", " "));
			if (World.getPlayerByName(player2).getLocation() == Location.DUEL_ARENA) {
				player.getPacketSender().sendMessage("Why are you trying to move a player out of duel arena?");
				return;
			}
			if(command.length >= 3 && command[2] != null)
				player2 += " "+Misc.formatText(command[2].replaceAll("_", " "));
			Player playerToMove = World.getPlayerByName(player2);
			if (playerToMove.getLocation() == Location.DUNGEONEERING)  {
				player.getPacketSender().sendMessage("You cannot teleport a player out of dung?");
				return;
			}
			if(playerToMove != null) {
				playerToMove.moveTo(GameSettings.DEFAULT_POSITION.copy());
				playerToMove.getPacketSender().sendMessage("You've been teleported home by "+player.getUsername()+".");
				player.getPacketSender().sendMessage("Sucessfully moved "+playerToMove.getUsername()+" to home.");
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
		if(wholeCommand.toLowerCase().startsWith("yell")) {
			if(PlayerPunishment.isMuted(player.getUsername()) || PlayerPunishment.isIpMuted(player.getHostAddress())) {
				player.getPacketSender().sendMessage("You are muted and cannot yell.");
				return;
			}
			if(!GameSettings.YELL_STATUS) {
				player.getPacketSender().sendMessage("Yell is currently turned off, please try again in 30 minutes!");
				return;
			}
			String yellMessage = wholeCommand.substring(4, wholeCommand.length());
			World.sendYell("<col=0>[<col=00ff00><shad=0><img=6>Global Mod<img=6></shad><col=0>] "+player.getUsername()+": "+yellMessage);	
		}
		if(command[0].equalsIgnoreCase("kick")) {
			String player2 = wholeCommand.substring(5);
			Player playerToKick = World.getPlayerByName(player2);
			if (playerToKick.getLocation() == Location.DUEL_ARENA) {
				player.getPacketSender().sendMessage("You cannot do this to someone in duel arena.");
				return;
			}
			if(playerToKick.getLocation() != Location.WILDERNESS) {
				World.deregister(playerToKick);
				player.getPacketSender().sendMessage("Kicked "+playerToKick.getUsername()+".");
				PlayerLogs.log(player.getUsername(), ""+player.getUsername()+" just kicked "+playerToKick.getUsername()+"!");
			}
		}
	}
	
}