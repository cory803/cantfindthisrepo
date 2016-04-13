package com.ikov.commands.ranks;

import com.ikov.GameSettings;
import com.ikov.model.Flag;
import com.ikov.model.Item;
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
import com.ikov.model.Skill;

public class Administrators {
	
	/**
	* @Author Jonathan Sirens
	* Initiates Command
	**/
	
	public static void initiate_command(final Player player, String[] command, String wholeCommand) {
		if(wholeCommand.equalsIgnoreCase("hp")) {
			player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 99999, true);
		}
		if(wholeCommand.startsWith("globalyell")) {
			player.getPacketSender().sendMessage("Retype the command to renable/disable the yell channel.");
			World.setGlobalYell(!World.isGlobalYell());
			World.sendMessage("<img=4> @blu@The yell channel has been @dre@"+(World.isGlobalYell() ? "@dre@enabled@blu@ again!" : "@dre@disabled@blu@ temporarily!"));
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
		if(command[0].equalsIgnoreCase("toggleinvis")) {
			player.setNpcTransformationId(player.getNpcTransformationId() > 0 ? -1 : 8254);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if(command[0].equalsIgnoreCase("teletome")) {
			String playerToTele = wholeCommand.substring(9);
			Player player2 = World.getPlayerByName(playerToTele);
			if(player2.getLocation() == Location.DUNGEONEERING) {
				player.getPacketSender().sendMessage("This player is in dung....");
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
		if (command[0].equals("tele")) {
			int x = Integer.valueOf(command[1]), y = Integer.valueOf(command[2]);
			int z = player.getPosition().getZ();
			if (command.length > 3)
				z = Integer.valueOf(command[3]);
			Position position = new Position(x, y, z);
			player.moveTo(position);
			player.getPacketSender().sendMessage("Teleporting to " + position.toString());
		}
		if(command[0].equalsIgnoreCase("movetome")) {
			String playerToTele = wholeCommand.substring(9);
			Player player2 = World.getPlayerByName(playerToTele);
			if(player2.getLocation() == Location.DUNGEONEERING) {
				player.getPacketSender().sendMessage("This player is in dung....");
				return;
			}
			boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy()) && player.getRegionInstance() == null && player2.getRegionInstance() == null;
			if(canTele) {
				player.getPacketSender().sendMessage("Moving player: "+player2.getUsername()+"");
				player2.getPacketSender().sendMessage("You've been moved to "+player.getUsername());
				player2.moveTo(player.getPosition().copy());
			} else
				player.getPacketSender().sendMessage("Failed to move player to your coords. Are you or them in a minigame?");
		}
		if(command[0].contains("host")) {
			String plr = wholeCommand.substring(command[0].length()+1);
			Player playr2 = World.getPlayerByName(plr);
			if(playr2 != null) {
				player.getPacketSender().sendMessage(""+playr2.getUsername()+" host IP: "+playr2.getHostAddress()+", serial number: "+playr2.getSerialNumber());
			} else
				player.getPacketSender().sendMessage("Could not find player: "+plr);
		}
		if(command[0].equals("gold")) {
			Player p = World.getPlayerByName(wholeCommand.substring(5));
			if(p != null) {
				long gold = 0;
				for(Item item : p.getInventory().getItems()) {
					if(item != null && item.getId() > 0 && item.tradeable())
						gold+= item.getDefinition().getValue();
				}
				for(Item item : p.getEquipment().getItems()) {
					if(item != null && item.getId() > 0 && item.tradeable())
						gold+= item.getDefinition().getValue();
				}
				for(int i = 0; i < 9; i++) {
					for(Item item : p.getBank(i).getItems()) {
						if(item != null && item.getId() > 0 && item.tradeable())
							gold+= item.getDefinition().getValue();
					}
				}
				gold += p.getMoneyInPouch();
				player.getPacketSender().sendMessage(p.getUsername() + " has "+Misc.insertCommasToNumber(String.valueOf(gold))+" coins.");
			} else
				player.getPacketSender().sendMessage("Can not find player online.");
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
		if(command[0].equalsIgnoreCase("serialban") ||(command[0].equalsIgnoreCase("cpuban")) ||(command[0].equalsIgnoreCase("macban"))) {
			String ban_player = command[1];
			if(!PlayerSaving.playerExists(ban_player)) {
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
			if(!PlayerSaving.playerExists(ban_player)) {
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
			if(!PlayerSaving.playerExists(ban_player)) {
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
			if(!PlayerSaving.playerExists(ban_player)) {
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
			if(!PlayerSaving.playerExists(ban_player)) {
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
				PlayerPunishment.unIpBan(ip);
				PlayerPunishment.unVoteBan(ban_player);
				PlayerPunishment.unBan(ban_player);
				player.getPacketSender().sendMessage("Player "+ban_player+" was successfully un mass banned!");
			}
		}
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
		if(command[0].equalsIgnoreCase("getip")) {
			String player_name = wholeCommand.substring(6);
			String last_ip = PlayerPunishment.getLastIpAddress(player_name);
			player.getPacketSender().sendMessage(player_name + "'s ip address is "+last_ip);
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
			World.sendYell("<col=0>[<col=ffff00><shad=0><img=2>Administrator<img=2></shad><col=0>] "+player.getUsername()+": "+yellMessage);	
		}
	}
	
}