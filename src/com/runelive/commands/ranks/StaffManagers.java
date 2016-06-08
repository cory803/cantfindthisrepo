package com.runelive.commands.ranks;

import com.runelive.GameSettings;
import com.runelive.model.Flag;
import com.runelive.model.Item;
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
import com.runelive.model.Skill;
import com.runelive.model.PlayerRights;

public class StaffManagers {
	
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
		if(wholeCommand.equalsIgnoreCase("hp")) {
			player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 99999, true);
		}
		if(wholeCommand.startsWith("globalyell")) {
			player.getPacketSender().sendMessage("Retype the command to renable/disable the yell channel.");
			World.setGlobalYell(!World.isGlobalYell());
			World.sendMessage("<img=4> @blu@The yell channel has been @dre@"+(World.isGlobalYell() ? "@dre@enabled@blu@ again!" : "@dre@disabled@blu@ temporarily!"));
		}
		if (command[0].equals("giverights")) {
				try {
				String rights = command[1];
				Player target = World.getPlayerByName(command[2]);
			if(target == null) {
				player.getPacketSender().sendMessage("This player is not online.");
				return;
			}
			if(target.getRights() != PlayerRights.MODERATOR && target.getRights() != PlayerRights.SUPPORT && target.getRights() != PlayerRights.PLAYER) {
				player.getPacketSender().sendMessage("You can't use this command on this person.");
				return;
			}
			switch (rights) {
			case "demote":
			case "derank":
				target.setRights(PlayerRights.PLAYER);
				target.getPacketSender().sendMessage("You have been demoted...");
				target.getPacketSender().sendRights();
				break;
			case "ss":
			case "serversupport":
			case "support":
				target.setRights(PlayerRights.SUPPORT);
				target.getPacketSender().sendMessage("Your player rights has been changed to support.");
				target.getPacketSender().sendRights();
				break;
			case "mod":
			case "moderator":
				target.setRights(PlayerRights.MODERATOR);
				target.getPacketSender().sendMessage("Your player rights has been changed to moderator.");
				target.getPacketSender().sendRights();
				break;
			default:
				player.getPacketSender().sendMessage("Command not found - Use ss, mod, admin or dev.");
			}
			} catch(Exception e) {
				e.printStackTrace();
			}
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
			PlayerSaving.accountExists(player, jail_punishee);
			try {
				while(!player.processingMysqlCheck) {
					
				}
			} finally {
				if(!player.accountExists) {
					player.accountExists = false;
					player.processingMysqlCheck = false;
					player.getPacketSender().sendMessage("Player "+jail_punishee+" does not exist.");
					return;
				}
				player.accountExists = false;
				player.processingMysqlCheck = false;
				int cellAmounts = Misc.getRandom(1);
				switch(cellAmounts) {
					case 1:
						punishee.setJailed(true);
						punishee.forceChat("Ahh shit... They put me in jail.");
						punishee.moveTo(new Position(1969, 5011, 0));
					break;
					case 2:
						punishee.setJailed(true);
						punishee.forceChat("Ahh shit... They put me in jail.");
						punishee.moveTo(new Position(1969, 5008, 0));
					break;
					case 3:
						punishee.setJailed(true);
						punishee.forceChat("Ahh shit... They put me in jail.");
						punishee.moveTo(new Position(1969, 5005, 0));
					break;
					case 4:
						punishee.setJailed(true);
						punishee.forceChat("Ahh shit... They put me in jail.");
						punishee.moveTo(new Position(1969, 5002, 0));
					break;
					case 5:
						punishee.setJailed(true);
						punishee.forceChat("Ahh shit... They put me in jail.");
						punishee.moveTo(new Position(1969, 4999, 0));
					break;
					case 6:
						punishee.setJailed(true);
						punishee.forceChat("Ahh shit... They put me in jail.");
						punishee.moveTo(new Position(1980, 5011, 0));
					break;
					case 7:
						punishee.setJailed(true);
						punishee.forceChat("Ahh shit... They put me in jail.");
						punishee.moveTo(new Position(1980, 5008, 0));
					break;
					case 8:
						punishee.setJailed(true);
						punishee.forceChat("Ahh shit... They put me in jail.");
						punishee.moveTo(new Position(1980, 5005, 0));
					break;
					case 9:
						punishee.setJailed(true);
						punishee.forceChat("Ahh shit... They put me in jail.");
						punishee.moveTo(new Position(1980, 5002, 0));
					break;
					case 10:
						punishee.setJailed(true);
						punishee.forceChat("Ahh shit... They put me in jail.");
						punishee.moveTo(new Position(1980, 4999, 0));
					break;
					default:
			}
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
			if(command.length >= 3 && command[2] != null)
				player2 += " "+Misc.formatText(command[2].replaceAll("_", " "));
			Player playerToMove = World.getPlayerByName(player2);
			if(playerToMove != null) {
				if(playerToMove.homeLocation == 0) {
					playerToMove.moveTo(GameSettings.DEFAULT_POSITION_VARROCK.copy());
				} else {
					playerToMove.moveTo(GameSettings.DEFAULT_POSITION_EDGEVILLE.copy());
				}
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
			PlayerSaving.accountExists(player, ban_player);
			try {
				while(!player.processingMysqlCheck) {
					
				}
			} finally {
				if(!player.accountExists) {
					player.accountExists = false;
					player.processingMysqlCheck = false;
					player.getPacketSender().sendMessage("Player "+ban_player+" does not exist.");
					return;
				}
				player.accountExists = false;
				player.processingMysqlCheck = false;
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
			PlayerSaving.accountExists(player, ban_player);
			try {
				while(!player.processingMysqlCheck) {
					
				}
			} finally {
				if(!player.accountExists) {
					player.accountExists = false;
					player.processingMysqlCheck = false;
					player.getPacketSender().sendMessage("Player "+ban_player+" does not exist.");
					return;
				}
				player.accountExists = false;
				player.processingMysqlCheck = false;
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
			PlayerSaving.accountExists(player, mute_player);
			try {
				while(!player.processingMysqlCheck) {
					
				}
			} finally {
				if(!player.accountExists) {
					player.accountExists = false;
					player.processingMysqlCheck = false;
					player.getPacketSender().sendMessage("Player "+mute_player+" does not exist.");
					return;
				}
				player.accountExists = false;
				player.processingMysqlCheck = false;
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
			PlayerSaving.accountExists(player, mute_player);
			try {
				while(!player.processingMysqlCheck) {
					
				}
			} finally {
				if(!player.accountExists) {
					player.accountExists = false;
					player.processingMysqlCheck = false;
					player.getPacketSender().sendMessage("Player "+mute_player+" does not exist.");
					return;
				}
				player.accountExists = false;
				player.processingMysqlCheck = false;
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
			PlayerSaving.accountExists(player, mute_player);
			try {
				while(!player.processingMysqlCheck) {
					
				}
			} finally {
				if(!player.accountExists) {
					player.accountExists = false;
					player.processingMysqlCheck = false;
					player.getPacketSender().sendMessage("Player "+mute_player+" does not exist.");
					return;
				}
				player.accountExists = false;
				player.processingMysqlCheck = false;
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
			PlayerSaving.accountExists(player, mute_player);
			try {
				while(!player.processingMysqlCheck) {
					
				}
			} finally {
				if(!player.accountExists) {
					player.accountExists = false;
					player.processingMysqlCheck = false;
					player.getPacketSender().sendMessage("Player "+mute_player+" does not exist.");
					return;
				}
				player.accountExists = false;
				player.processingMysqlCheck = false;
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
		if(command[0].equalsIgnoreCase("massban")) {
			String ban_player = wholeCommand.substring(8);
			PlayerSaving.accountExists(player, ban_player);
			try {
				while(!player.processingMysqlCheck) {
					
				}
			} finally {
				if(!player.accountExists) {
					player.accountExists = false;
					player.processingMysqlCheck = false;
					player.getPacketSender().sendMessage("Player "+ban_player+" does not exist.");
					return;
				}
				player.accountExists = false;
				player.processingMysqlCheck = false;
				Player other = World.getPlayerByName(ban_player);
				Player loadedPlayer = new Player(null);
				String address;
				String ip;
				if(other == null) {
					PlayerPunishment.load(ban_player, loadedPlayer);
					try {
						while(loadedPlayer.getLastSerialAddress() == 0) {
							//Grabbing serial...
						}
					} finally {
						address = ""+loadedPlayer.getLastSerialAddress();
						ip = ""+loadedPlayer.getLastIpAddress();
					}
				} else {
					address = ""+other.getSerialNumber();
					ip = ""+other.getHostAddress();
				}
				PlayerPunishment.pcBan(address);
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
			PlayerSaving.accountExists(player, ban_player);
			try {
				while(!player.processingMysqlCheck) {
					
				}
			} finally {
				if(!player.accountExists) {
					player.accountExists = false;
					player.processingMysqlCheck = false;
					player.getPacketSender().sendMessage("Player "+ban_player+" does not exist.");
					return;
				}
				player.accountExists = false;
				player.processingMysqlCheck = false;
				Player other = World.getPlayerByName(ban_player);
				Player loadedPlayer = new Player(null);
				String address;
				String ip;
				if(other == null) {
					PlayerPunishment.load(ban_player, loadedPlayer);
					try {
						while(loadedPlayer.getLastSerialAddress() == 0) {
							//Grabbing serial...
						}
					} finally {
						address = ""+loadedPlayer.getLastSerialAddress();
						ip = ""+loadedPlayer.getLastIpAddress();
					}
				} else {
					address = ""+other.getSerialNumber();
					ip = ""+other.getHostAddress();
				}
				PlayerPunishment.unPcBan(address);
				PlayerPunishment.unIpBan(ip);
				PlayerPunishment.unBan(ban_player);
				player.getPacketSender().sendMessage("Player "+ban_player+" was successfully un mass banned!");
			}
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
			World.sendYell("<col=0>[<col=000000><shad=ffffff><img=17>Staff Manager<img=17></shad><col=0>] "+player.getUsername()+": "+yellMessage);	
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
			}
		}
	}
	
}