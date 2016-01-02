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

public class Members {
	
	/**
	* @Author Jonathan Sirens
	* Initiates Command
	**/
	
	public static void initiate_command(final Player player, String[] command, String wholeCommand) {
		if (command[0].equalsIgnoreCase("auth")) {
			if(!GameSettings.VOTING_CONNECTIONS) {
				player.getPacketSender().sendMessage("Voting is currently turned off, please try again in 30 minutes!");
				return;
			}
			String authCode = command[1];
			if (!GameSettings.MYSQL_ENABLED) {
				player.getPacketSender().sendMessage("Sorry this is currently disabled.");
				return;
			} else {
				try {
					Auth.connect();
					if (Auth.checkVote(authCode)) {
						player.getPacketSender().giveVoteReward();
						Auth.updateVote(authCode);
						if(player.getPacketSender().authCount % 10 == 0) {
							World.sendMessage("[@blu@Vote@bla@] Another 10 auths have been claimed by the global server with ::vote");
						}
					} else {
						player.getPacketSender().sendMessage("The authcode you have entered is invalid.");
					}
				} catch (Exception e) {
					player.getPacketSender().sendMessage("Error connecting to the database. Please try again later.");
					e.printStackTrace();
				}

			}
			return;
		}
		if (wholeCommand.equalsIgnoreCase("donate") || wholeCommand.equalsIgnoreCase("store")) {
			if(!GameSettings.STORE_CONNECTIONS) {
				player.getPacketSender().sendMessage("The Store is currently turned off, please try again in 30 minutes!");
				return;
			}
			player.getPacketSender().sendString(1, "www.ikov2.org/store/");
			player.getPacketSender().sendMessage("Attempting to open: www.ikov2.org/store/");
		}
		if(command[0].equalsIgnoreCase("attacks")) {
			int attack = DesolaceFormulas.getMeleeAttack(player);
			int range = DesolaceFormulas.getRangedAttack(player);
			int magic = DesolaceFormulas.getMagicAttack(player);
			player.getPacketSender().sendMessage("@bla@Melee attack: @or2@"+attack+"@bla@, ranged attack: @or2@"+range+"@bla@, magic attack: @or2@"+magic);
		}
		if (command[0].equals("save")) {
			player.save();
			player.getPacketSender().sendMessage("Your progress has been saved.");
		}
		if (command[0].equals("vote")) {
			if(!GameSettings.VOTING_CONNECTIONS) {
				player.getPacketSender().sendMessage("Voting is currently turned off, please try again in 30 minutes!");
				return;
			}
			player.getPacketSender().sendString(1, "www.ikov2.org/vote/");
			player.getPacketSender().sendMessage("Attempting to open: www.ikov2.org/vote/");
		}
		if (command[0].equals("thread")) {
			int thread = Integer.parseInt(command[1]);
			player.getPacketSender().sendString(1, "www.ikov2.org/forum/index.php?/topic/"+thread+"-threadcommand/");
			player.getPacketSender().sendMessage("Attempting to open: Thread " + thread);
		}
		if (command[0].equals("Farming2")) {
			if(Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if(player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			Position position = new Position(2816, 3463, 0);
			player.moveTo(position);
			player.getPacketSender().sendMessage("Teleporting you home!");
		}
		if (command[0].startsWith("changepass")) {
			player.setInputHandling(new ChangePassword());
			player.getPacketSender().sendEnterInputPrompt("Enter a new password:");
		}
		if (command[0].equals("home")) {
			if(Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if(player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			Position position = new Position(3087, 3502, 0);
			player.moveTo(position);
			player.getPacketSender().sendMessage("Teleporting you home!");
		}
		if (command[0].equals("train")) {
			if(Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if(player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			Position position = new Position(2679, 3720, 0);
			player.moveTo(position);
			player.getPacketSender().sendMessage("Teleporting you to rock crabs!");
		}
		if (command[0].equals("edge")) {
			if(Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if(player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			Position position = new Position(3087, 3502, 0);
			player.moveTo(position);
			player.getPacketSender().sendMessage("Teleporting you to edgeville!");
		}
		if (command[0].equals("wests")) {
			if(Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if(player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			Position position = new Position(2980 + Misc.getRandom(3), 3596 + Misc.getRandom(3), 0);
			player.moveTo(position);
			player.getPacketSender().sendMessage("Teleporting you to wests!");
		}
		if (command[0].equals("easts")) {
			if(Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if(player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			Position position = new Position(3329 + Misc.getRandom(2), 3660 + Misc.getRandom(2), 0);
			player.moveTo(position);
			player.getPacketSender().sendMessage("Teleporting you to wests!");
		}
		if (command[0].equals("market")) {
			if(Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if(player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			Position position = new Position(3164, 3484, 0);
			player.moveTo(position);
			player.getPacketSender().sendMessage("Welcome to the Market!");
		}
		if(command[0].equals("help")) {
			if(player.getLastYell().elapsed(30000)) {
				World.sendStaffMessage("<col=FF0066><img=10> [TICKET SYSTEM]<col=6600FF> "+player.getUsername()+" has requested help. Please help them!");
				player.getLastYell().reset();
				player.getPacketSender().sendMessage("<col=663300>Your help request has been received. Please be patient.");
			} else {
				player.getPacketSender().sendMessage("").sendMessage("<col=663300>You need to wait 30 seconds before using this again.").sendMessage("<col=663300>If it's an emergency, please private message a staff member directly instead.");
			}
		}
		if(command[0].equals("empty")) {
			player.getPacketSender().sendInterfaceRemoval().sendMessage("You clear your inventory.");
			player.getSkillManager().stopSkilling();
			player.getInventory().resetItems().refreshItems();
		}
		if (command[0].equals("gamble")) {
			if(Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if(player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			Position position = new Position(3187, 3435, 0);
			player.moveTo(position);
			player.getPacketSender().sendMessage("Welcome to the Gambling Area!");
		}	
		if(command[0].equals("players")) {
			player.getPacketSender().sendInterfaceRemoval();
			PlayersOnlineInterface.showInterface(player);
		}
		if(command[0].equalsIgnoreCase("[cn]")) {
			if(player.getInterfaceId() == 40172) {
				ClanChatManager.setName(player, wholeCommand.substring(wholeCommand.indexOf(command[1])));
			}
		}
	}
	
}