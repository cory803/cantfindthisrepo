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

public class Administrators {
	
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
		if(wholeCommand.startsWith("globalyell")) {
			player.getPacketSender().sendMessage("Retype the command to renable/disable the yell channel.");
			World.setGlobalYell(!World.isGlobalYell());
			World.sendMessage("<img=10> @blu@The yell channel has been @dre@"+(World.isGlobalYell() ? "@dre@enabled@blu@ again!" : "@dre@disabled@blu@ temporarily!"));
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
				if(!PlayerSaving.playerExists(jail_punishee)) {
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
		if(command[0].equalsIgnoreCase("saveall")) {
			World.savePlayers();
			player.getPacketSender().sendMessage("Saved players!");
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
				PlayerPunishment.macBan(mac);
				PlayerPunishment.ipBan(ip);
				PlayerPunishment.ban(ban_player);
				if(other != null) {
					World.deregister(other);
				}
				player.getPacketSender().sendMessage("Player "+ban_player+" was successfully mass banned!");
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
		if(command[0].equalsIgnoreCase("movehome")) {
			String player2 = command[1];
			player2 = Misc.formatText(player2.replaceAll("_", " "));
			if(command.length >= 3 && command[2] != null)
				player2 += " "+Misc.formatText(command[2].replaceAll("_", " "));
			Player playerToMove = World.getPlayerByName(player2);
			if(playerToMove != null) {
				playerToMove.moveTo(GameSettings.DEFAULT_POSITION.copy());
				playerToMove.getPacketSender().sendMessage("You've been teleported home by "+player.getUsername()+".");
				player.getPacketSender().sendMessage("Sucessfully moved "+playerToMove.getUsername()+" to home.");
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
				PlayerPunishment.unBan(ban_player);
				player.getPacketSender().sendMessage("Player "+ban_player+" was successfully un mass banned!");
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
			World.sendYell("<col=0>[<col=ffff00><shad=0><img=3>Administrator<img=3></shad><col=0>] "+player.getUsername()+": "+yellMessage);	
		}
		if(command[0].equalsIgnoreCase("kick")) {
			String player2 = wholeCommand.substring(5);
			Player playerToKick = World.getPlayerByName(player2);
			if(playerToKick.getLocation() == Location.DUNGEONEERING) {
				player.getPacketSender().sendMessage("This player is in dung....");
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