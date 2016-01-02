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

public class Moderators {
	
	/**
	* @Author Jonathan Sirens
	* Initiates Command
	**/
	
	public static void initiate_command(final Player player, String[] command, String wholeCommand) {
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
			World.sendMessage("<col=0>[<col=31a4ff><shad=0><img=1>Moderator<img=1></shad><col=0>] "+player.getUsername()+": "+yellMessage);	
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
	}
	
}