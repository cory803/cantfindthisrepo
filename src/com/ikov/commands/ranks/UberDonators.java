package com.ikov.commands.ranks;

import com.ikov.GameSettings;
import com.ikov.model.MagicSpellbook;
import com.ikov.world.content.combat.magic.Autocasting;
import com.ikov.world.content.combat.prayer.CurseHandler;
import com.ikov.world.content.combat.prayer.PrayerHandler;
import com.ikov.model.Prayerbook;
import com.ikov.model.GameMode;
import com.ikov.world.content.transportation.TeleportHandler;
import com.ikov.model.Locations.Location;
import com.ikov.model.PlayerRights;
import com.ikov.model.Position;
import com.ikov.util.Misc;
import com.ikov.model.Skill;
import com.ikov.world.World;
import com.ikov.world.content.PlayerPunishment;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.world.content.skill.impl.dungeoneering.Dungeoneering;

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
	}
	
}