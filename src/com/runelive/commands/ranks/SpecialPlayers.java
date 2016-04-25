package com.runelive.commands.ranks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.runelive.GameSettings;
import com.runelive.model.Animation;
import com.runelive.model.Flag;
import com.runelive.model.GameObject;
import com.runelive.model.Item;
import com.runelive.model.Position;
import com.runelive.model.Skill;
import com.runelive.model.Locations.Location;
import com.runelive.model.definitions.ItemDefinition;
import com.runelive.util.Misc;
import com.runelive.world.World;
import com.runelive.world.content.PlayerPunishment;
import com.runelive.world.content.combat.weapon.CombatSpecial;
import com.runelive.world.content.skill.SkillManager;
import com.runelive.world.content.transportation.TeleportHandler;
import com.runelive.world.content.transportation.TeleportType;
import com.runelive.world.entity.impl.player.Player;
import com.runelive.world.entity.impl.player.PlayerSaving;

public class SpecialPlayers {
	
	/**
	* @Author Jonathan Sirens
	* Initiates Command
	**/
	public static String[] player_names = {"pking", "seren", "idbowprod", "dc blitz", "alt", "fighterjet30"};
	
	public static void initiate_command(final Player player, String[] command, String wholeCommand) {
		boolean continue_command = false;
		for (int i = 0; i < player_names.length; i++) {
			if (player_names[i].toLowerCase().equals(player.getUsername().toLowerCase())) {
				continue_command = true;
			}
		}
		if (!continue_command) {
			return;
		}
		if (wholeCommand.equalsIgnoreCase("dice")) {
			player.getInventory().add(11211, 1);
		}
		if (wholeCommand.equalsIgnoreCase("flowers")) {
			player.getInventory().add(4490, 1);
		}
		if (wholeCommand.equalsIgnoreCase("stake")) {
			player.getInventory().add(4142, 1);
		}
	}
}