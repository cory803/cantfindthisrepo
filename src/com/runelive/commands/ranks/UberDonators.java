package com.runelive.commands.ranks;

import com.runelive.GameSettings;
import com.runelive.model.MagicSpellbook;
import com.runelive.world.content.combat.magic.Autocasting;
import com.runelive.world.content.combat.prayer.CurseHandler;
import com.runelive.world.content.combat.prayer.PrayerHandler;
import com.runelive.model.Prayerbook;
import com.runelive.model.GameMode;
import com.runelive.world.content.transportation.TeleportHandler;
import com.runelive.model.Locations.Location;
import com.runelive.model.PlayerRights;
import com.runelive.model.Position;
import com.runelive.util.Misc;
import com.runelive.model.Skill;
import com.runelive.world.World;
import com.runelive.world.content.PlayerPunishment;
import com.runelive.world.entity.impl.player.Player;
import com.runelive.world.content.skill.impl.dungeoneering.Dungeoneering;

public class UberDonators {
	
	/**
	* @Author Jonathan Sirens
	* Initiates Command
	**/
	
	public static void initiate_command(final Player player, String[] command, String wholeCommand) {
		if(player.isJailed()) {
			player.getPacketSender().sendMessage("You cannot use commands in jail... You're in jail.");
			return;
		}
		if(wholeCommand.toLowerCase().startsWith("yell")) {
			if(player.getRights() != PlayerRights.PLAYER) {
				return;
			}
			if(World.isGlobalYell() == false) {
				player.getPacketSender().sendMessage("An admin has temporarily disabled the global yell channel.");
				return;
			}
			if(PlayerPunishment.isMuted(player.getUsername()) || PlayerPunishment.isIpMuted(player.getHostAddress())) {
				player.getPacketSender().sendMessage("You are muted and cannot yell.");
				return;
			}
			if(player.isYellMute()) {
				player.getPacketSender().sendMessage("You are muted from yelling and cannot yell.");
				return;
			}
			if(!GameSettings.YELL_STATUS) {
				player.getPacketSender().sendMessage("Yell is currently turned off, please try again in 30 minutes!");
				return;
			}
			String yellMessage = wholeCommand.substring(4, wholeCommand.length());
			if(yellMessage.contains("<img=") || yellMessage.contains("<col=") || yellMessage.contains("<shad=")) {
				player.getPacketSender().sendMessage("You are not aloud to put these symbols in your yell message.");
				return;
			}
			if(player.getGameMode() == GameMode.IRONMAN) {
				World.sendYell("<img=12> [<col=808080><shad=0>Ironman</col></shad>] "+player.getUsername()+": "+yellMessage);	
				return;
			}
			if(player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
				World.sendYell("<img=13> [<col=ffffff><shad=0>Hardcore</col></shad>] "+player.getUsername()+": "+yellMessage);	
				return;
			}
			if(!player.getYellTag().equals("invalid_yell_tag_set")) {
				World.sendYell("<img=11> <col=0>[<col=ffff00><shad=0>"+player.getYellTag()+"<col=0></shad>] "+player.getUsername()+": "+yellMessage);	
				player.getYellTimer().reset();
				return;
			}
			World.sendYell("<img=11> <col=0>[<col=ffff00><shad=0>Uber</shad><col=0>] "+player.getUsername()+": "+yellMessage);	
		}
		if (command[0].equals("ezone")) {
			if(Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if(player.getLocation() != null && player.getWildernessLevel() > 20) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			Position position = new Position(3362, 9640, 0);
			int ran = Misc.getRandom(3);
			switch(ran) {
				case 0:
					position = new Position(3363, 9641, 0);
				break;
				case 1:
					position = new Position(3364, 9640, 0);
				break;
				case 2:
					position = new Position(3363, 9639, 0);
				break;
				case 3:
					position = new Position(3362, 9640, 0);
				break;
			}
			TeleportHandler.teleportPlayer(player, position, player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("<img=9><col=00ff00><shad=0> Welcome to the Extreme Donator Zone!");
		}
		if (command[0].equals("dzone")) {
			if(Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if(player.getLocation() != null && player.getWildernessLevel() > 20) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			Position position = new Position(2514, 3860, 0);
			player.moveTo(position);
			player.getPacketSender().sendMessage("[<col=ff0000>Donator Zone</col>] Welcome to the donator zone.");
		}
		if (command[0].equals("ancients")) {
			if(player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			player.setSpellbook(MagicSpellbook.ANCIENT);
			player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId()).sendMessage("Your magic spellbook is changed to ancients..");
			Autocasting.resetAutocast(player, true);
		}	
		if (command[0].equals("togglepray")) {
			if(player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 30) {
				player.getPacketSender().sendMessage("You need a Defence level of at least 30 to use this altar.");
				return;
			}
			if(player.getPrayerbook() == Prayerbook.NORMAL) {
				player.getPacketSender().sendMessage("You sense a surge of power flow through your body!");
				player.setPrayerbook(Prayerbook.CURSES);
			} else {
				player.getPacketSender().sendMessage("You sense a surge of purity flow through your body!");
				player.setPrayerbook(Prayerbook.NORMAL);
			}
			player.getPacketSender().sendTabInterface(GameSettings.PRAYER_TAB, player.getPrayerbook().getInterfaceId());
			PrayerHandler.deactivateAll(player);
			CurseHandler.deactivateAll(player);
		}
		if (command[0].equals("moderns")) {
			if(player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			player.setSpellbook(MagicSpellbook.NORMAL);
			player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId()).sendMessage("Your magic spellbook is changed to moderns..");
			Autocasting.resetAutocast(player, true);
		}
		if (command[0].equals("lunars")) {
			if(player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			player.setSpellbook(MagicSpellbook.LUNAR);
			player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId()).sendMessage("Your magic spellbook is changed to lunars..");
			Autocasting.resetAutocast(player, true);
		}
	}
	
}