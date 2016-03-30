package com.ikov.world.content.skill;

import com.ikov.GameSettings;
import com.ikov.engine.task.Task;
import com.ikov.engine.task.TaskManager;
import com.ikov.model.Flag;
import com.ikov.model.GameMode;
import com.ikov.model.Graphic;
import com.ikov.model.Locations.Location;
import com.ikov.model.Skill;
import com.ikov.model.container.impl.Equipment;
import com.ikov.model.definitions.WeaponAnimations;
import com.ikov.model.definitions.WeaponInterfaces;
import com.ikov.util.Misc;
import com.ikov.world.World;
import com.ikov.world.content.Achievements;
import com.ikov.world.content.Achievements.AchievementData;
import com.ikov.world.content.BonusManager;
import com.ikov.world.content.BrawlingGloves;
import com.ikov.world.content.WellOfGoodwill;
import com.ikov.world.content.Sounds;
import com.ikov.world.content.Sounds.Sound;
import com.ikov.world.content.combat.prayer.CurseHandler;
import com.ikov.world.content.combat.prayer.PrayerHandler;
import com.ikov.world.entity.impl.player.Player;

/**
 * Handles enchanting of interfaces calculations such as combat level and total
 * level.
 * 
 * @author Jonathan Sirens
 * @author High105
 */

public class Enchanting {
	public static void enchantButtons(Player player, int buttonId) {
		int magicLevel = player.getSkillManager().getCurrentLevel(Skill.MAGIC);
		switch(buttonId) {
		case -16530: //opal
			if(magicLevel < 4) {
				player.getPacketSender().sendMessage("");
				return;
			}
			break;
		case -16521: //sapphire
			break;
		case -16513: //jade
			break;
		case -16505: //pearl
			break;
		case -16497: //emerald
			break;
		case -16489: //red topaz
			break;
		case -16481: //ruby
			break;
		case -16473: //diamond
			break;
		case -16465: //dragonstone
			break;
		case -16457: //onyx
			break;
		}
	}
	public static void update_interface(Player player) {
		player.getPacketSender().sendInterface(49000);
		int magic_level = player.getSkillManager().getCurrentLevel(Skill.MAGIC);
		if (magic_level < 87) {
			player.getPacketSender().sendString(49081, "@red@Magic 87");
		}
		if (magic_level < 68) {
			player.getPacketSender().sendString(49073, "@red@Magic 68");
		}
		if (magic_level < 57) {
			player.getPacketSender().sendString(49065, "@red@Magic 57");
		}
		if (magic_level < 49) {
			player.getPacketSender().sendString(49057, "@red@Magic 49");
		}
		if (magic_level < 29) {
			player.getPacketSender().sendString(49049, "@red@Magic 29");
		}
		if (magic_level < 27) {
			player.getPacketSender().sendString(49041, "@red@Magic 27");
		}
		if (magic_level < 24) {
			player.getPacketSender().sendString(49033, "@red@Magic 24");
		}
		if (magic_level < 14) {
			player.getPacketSender().sendString(49025, "@red@Magic 14");
		}
		if (magic_level < 7) {
			player.getPacketSender().sendString(49017, "@red@Magic 7");
		}
		if (magic_level < 4) {
			player.getPacketSender().sendString(49009, "@red@Magic 4");
		}
	}

}