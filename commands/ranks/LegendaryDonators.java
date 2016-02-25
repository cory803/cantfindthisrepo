package com.ikov.commands.ranks;

import com.ikov.GameSettings;
import com.ikov.model.MagicSpellbook;
import com.ikov.world.content.combat.magic.Autocasting;
import com.ikov.world.content.combat.prayer.CurseHandler;
import com.ikov.world.content.combat.prayer.PrayerHandler;
import com.ikov.model.Prayerbook;
import com.ikov.model.Locations.Location;
import com.ikov.model.PlayerRights;
import com.ikov.model.GameMode;
import com.ikov.model.Position;
import com.ikov.model.Skill;
import com.ikov.world.World;
import com.ikov.world.content.PlayerPunishment;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.world.content.skill.impl.dungeoneering.Dungeoneering;

public class LegendaryDonators {
	
	/**
	* @Author Jonathan Sirens
	* Initiates Command
	**/
	
	public static void initiate_command(final Player player, String[] command, String wholeCommand) {
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
			int delay = player.getRights().getYellDelay();
			if(!player.getLastYell().elapsed((delay * 1000))) {
				player.getPacketSender().sendMessage("You must wait at least "+delay+" seconds between every yell-message you send.");
				return;
			}
			String yellMessage = wholeCommand.substring(4, wholeCommand.length());
			if(yellMessage.contains("<img=") || yellMessage.contains("<col=") || yellMessage.contains("<shad=")) {
				player.getPacketSender().sendMessage("You are not aloud to put these symbols in your yell message.");
				return;
			}
			if(player.getGameMode() == GameMode.IRONMAN) {
				World.sendYell("<img=33> [<col=808080><shad=0>Ironman</col></shad>] "+player.getUsername()+": "+yellMessage);	
				player.getLastYell().reset();
				return;
			}
			if(player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
				World.sendYell("<img=32> [<col=ffffff><shad=0>Hardcore</col></shad>] "+player.getUsername()+": "+yellMessage);	
				player.getLastYell().reset();
				return;
			}
			World.sendYell("<img=8> <col=0>[<col=3E0069>Legendary<col=0>] "+player.getUsername()+": "+yellMessage);	
			player.getLastYell().reset();
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
		if (command[0].equals("dzone")) {
			if(Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if(player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			Position position = new Position(3424, 2919, 0);
			player.moveTo(position);
			player.getPacketSender().sendMessage("Thanks for supporting Ikov!");
		}
	}
	
}