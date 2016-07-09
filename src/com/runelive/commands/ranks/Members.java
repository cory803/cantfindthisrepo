package com.runelive.commands.ranks;

import java.text.DecimalFormat;

import com.runelive.GameSettings;
import com.runelive.model.Locations.Location;
import com.runelive.model.Position;
import com.runelive.model.Store;
import com.runelive.model.Voting;
import com.runelive.model.input.impl.ChangePassword;
import com.runelive.util.ForumDatabase;
import com.runelive.util.Misc;
import com.runelive.world.content.Command;
import com.runelive.world.content.PlayersOnlineInterface;
import com.runelive.world.content.clan.ClanChatManager;
import com.runelive.world.content.combat.CombatFactory;
import com.runelive.world.content.combat.DesolaceFormulas;
import com.runelive.world.content.dialogue.DialogueManager;
import com.runelive.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.runelive.world.content.transportation.TeleportHandler;
import com.runelive.world.entity.impl.player.Player;

public class Members {

	/**
	 * @Author Jonathan Sirens Initiates Command
	 **/

	public static void initiate_command(final Player player, String[] command, String wholeCommand) {
		if (player.isJailed()) {
			player.getPacketSender().sendMessage("You cannot use commands in jail... You're in jail.");
			return;
		}
		if (command[0].equalsIgnoreCase("bosses")) {
			player.forceChat("[RuneLive] "+player.getUsername()+" has slain "+player.getBossPoints()+" bosses.");
		}
		if (command[0].equalsIgnoreCase("changelevel")) {
			/*
			 * if(player.getEquipment().getFreeSlots() !=
			 * player.getEquipment().capacity()) {
			 * player.getPacketSender().sendMessage(
			 * "Please unequip all your items first."); return; } String skill =
			 * command[1]; String newLevel = command[2];
			 * if(Integer.parseInt(newLevel) < 1) {
			 * player.getPacketSender().sendMessage(
			 * "You cannot set your level to "+newLevel+"."); return; }
			 * if(Integer.parseInt(newLevel) > 99) {
			 * player.getPacketSender().sendMessage(
			 * "You cannot set your level to "+newLevel+"."); return; }
			 * switch(skill) { case "att": case "attack": case "atk":
			 * if(player.getSkillManager().getMaxLevel(Skill.ATTACK) >
			 * Integer.parseInt(newLevel)) {
			 * player.getPacketSender().sendMessage(
			 * "You just changed your attack from "
			 * +player.getSkillManager().getCurrentLevel(Skill.ATTACK)+" to "
			 * +newLevel+"." );
			 * player.getSkillManager().setMaxLevel(Skill.ATTACK,
			 * Integer.parseInt(newLevel), true);
			 * player.getSkillManager().setCurrentLevel(Skill.ATTACK,
			 * Integer.parseInt(newLevel), true);
			 * player.getSkillManager().setExperience(Skill.ATTACK,
			 * SkillManager.getExperienceForLevel(Integer.parseInt(newLevel)));
			 * } else { player.getPacketSender().sendMessage(
			 * "You cannot set a skill to be a higher level than it currently is."
			 * ); } break; case "def": case "defence": case "defense":
			 * if(player.getSkillManager().getMaxLevel(Skill.DEFENCE) >
			 * Integer.parseInt(newLevel)) {
			 * player.getPacketSender().sendMessage(
			 * "You just changed your defence from "
			 * +player.getSkillManager().getCurrentLevel(Skill.DEFENCE)+" to "
			 * +newLevel+"." );
			 * player.getSkillManager().setMaxLevel(Skill.DEFENCE,
			 * Integer.parseInt(newLevel), true);
			 * player.getSkillManager().setCurrentLevel(Skill.DEFENCE,
			 * Integer.parseInt(newLevel), true);
			 * player.getSkillManager().setExperience(Skill.DEFENCE,
			 * SkillManager.getExperienceForLevel(Integer.parseInt(newLevel)));
			 * } else { player.getPacketSender().sendMessage(
			 * "You cannot set a skill to be a higher level than it currently is."
			 * ); } break; case "pray": case "prayer":
			 * if(player.getSkillManager().getMaxLevel(Skill.PRAYER) >
			 * Integer.parseInt(newLevel)) {
			 * player.getPacketSender().sendMessage(
			 * "You just changed your prayer from "
			 * +player.getSkillManager().getCurrentLevel(Skill.PRAYER)/10+" to "
			 * +newLevel+"." );
			 * player.getSkillManager().setMaxLevel(Skill.PRAYER,
			 * Integer.parseInt(newLevel)*10, true);
			 * player.getSkillManager().setCurrentLevel(Skill.PRAYER,
			 * Integer.parseInt(newLevel)*10, true);
			 * player.getSkillManager().setExperience(Skill.PRAYER,
			 * SkillManager.getExperienceForLevel(Integer.parseInt(newLevel)));
			 * } else { player.getPacketSender().sendMessage(
			 * "You cannot set a skill to be a higher level than it currently is."
			 * ); } break; default: player.getPacketSender().sendMessage(
			 * "The command syntax example - use a skill below: ::changelevel prayer 52"
			 * ); player.getPacketSender().sendMessage(
			 * "You can only adjust prayer, defence and attack stats."); }
			 */
			player.getPacketSender().sendMessage("Currently Disabled.");

		}
		if (command[0].equalsIgnoreCase("kdr")) {
			int KDR = player.getPlayerKillingAttributes().getPlayerKills()
					/ player.getPlayerKillingAttributes().getPlayerDeaths();
			DecimalFormat df = new DecimalFormat("#.00");
			player.forceChat(
					"[RuneLive] My Kill to Death ration is " + player.getPlayerKillingAttributes().getPlayerKills()
							+ " kills to " + player.getPlayerKillingAttributes().getPlayerDeaths()
							+ " deaths, which is " + df.format(KDR) + " K/D.");
		}
		if (command[0].equalsIgnoreCase("time")) {
			player.forceChat("[runelive] " + player.getUsername() + " has played for ["
					+ Misc.getHoursPlayed((player.getTotalPlayTime() + player.getRecordedLogin().elapsed())) + "]");
		}
		if (command[0].equalsIgnoreCase("mb")) {
			if (Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if (player.getLocation() != null && player.getWildernessLevel() > 20) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			Position position = new Position(2539, 4715, 0);
			TeleportHandler.teleportPlayer(player, position, player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Teleporting you to mage bank!");
		}
		if (command[0].equalsIgnoreCase("wests")) {
			if (Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if (player.getLocation() != null && player.getWildernessLevel() > 20) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			DialogueManager.start(player, 212);
			player.setDialogueActionId(212);
		}
		if (command[0].equalsIgnoreCase("easts")) {
			if (Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if (player.getLocation() != null && player.getWildernessLevel() > 20) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			DialogueManager.start(player, 213);
			player.setDialogueActionId(213);
		}
		if (command[0].equalsIgnoreCase("commands")) {
			if (player.getLocation() == Location.DUNGEONEERING) {
				player.getPacketSender().sendMessage("You cannot open the commands in dungeoneering.");
				return;
			}
			player.getPacketSender().sendTab(GameSettings.QUESTS_TAB);
			Command.open(player);
		}
		if (command[0].equals("stuck")) {
			if (player.getTeleblockTimer() > 0) {
				player.getPacketSender().sendMessage("You cannot teleport with this command while teleblocked.");
				return;
			}
			if (player.getCombatBuilder().isBeingAttacked() || player.getCombatBuilder().isAttacking()) {
				player.getPacketSender().sendMessage("You cannot use this command while in combat!");
				return;
			}
			if (!player.getStuckDelay().elapsed(300000)) {
				player.getPacketSender()
						.sendMessage("You have not waited the entire 5 minutes to be able to use this command again.");
				return;
			}
			boolean route_found = false;
			Position[] obstacle_pipe = { new Position(3004, 3938, 0), new Position(3004, 3939, 0),
					new Position(3004, 3940, 0), new Position(3004, 3941, 0), new Position(3004, 3942, 0),
					new Position(3004, 3943, 0), new Position(3004, 3944, 0), new Position(3004, 3945, 0),
					new Position(3004, 3946, 0), new Position(3004, 3947, 0), new Position(3004, 3948, 0),
					new Position(3004, 3949, 0), new Position(3003, 3948, 0), new Position(3003, 3947, 0),
					new Position(3003, 3946, 0), new Position(3003, 3945, 0), new Position(3003, 3944, 0),
					new Position(3003, 3943, 0), new Position(3003, 3942, 0), new Position(3003, 3941, 0),
					new Position(3003, 3940, 0), new Position(3003, 3939, 0), new Position(3005, 3939, 0),
					new Position(3005, 3940, 0), new Position(3005, 3941, 0), new Position(3005, 3942, 0),
					new Position(3005, 3943, 0), new Position(3005, 3944, 0), new Position(3005, 3945, 0),
					new Position(3005, 3946, 0), new Position(3005, 3947, 0), new Position(3005, 3948, 0) };
			for (Position p : obstacle_pipe) {
				if (p.getX() == player.getPosition().getX() && p.getY() == player.getPosition().getY()) {
					player.getStuckDelay().reset();
					route_found = true;
					player.moveTo(new Position(3004, 3937, 0));
					player.getPacketSender().sendMessage("You have been moved outside of the obstacle pipe!");
				}
			}
			Position[] ropeswing = { new Position(3006, 3954, 0), new Position(3005, 3954, 0),
					new Position(3006, 3955, 0), new Position(3005, 3955, 0), new Position(3006, 3956, 0),
					new Position(3005, 3956, 0), new Position(3006, 3957, 0), new Position(3005, 3957, 0),
					new Position(3004, 3954, 0), new Position(3004, 3955, 0), new Position(3004, 3956, 0),
					new Position(3004, 3957, 0) };
			for (Position p : ropeswing) {
				if (p.getX() == player.getPosition().getX() && p.getY() == player.getPosition().getY()) {
					player.getStuckDelay().reset();
					route_found = true;
					player.moveTo(new Position(3005, 3953, 0));
					player.getPacketSender().sendMessage("You have been moved outside of the rope swing!");
				}
			}
			Position[] strange_floor = { new Position(2997, 3960, 0), new Position(2998, 3960, 0),
					new Position(2999, 3960, 0), new Position(3000, 3960, 0), new Position(3001, 3960, 0) };
			for (Position p : strange_floor) {
				if (p.getX() == player.getPosition().getX() && p.getY() == player.getPosition().getY()) {
					player.getStuckDelay().reset();
					route_found = true;
					player.moveTo(new Position(3002, 3960, 0));
					player.getPacketSender().sendMessage("You have been moved outside of the strange floor!");
				}
			}
			Position[] log_balance = { new Position(3002, 3945, 0), new Position(3001, 3945, 0),
					new Position(3000, 3945, 0), new Position(2999, 3945, 0), new Position(2998, 3945, 0),
					new Position(2997, 3945, 0), new Position(2996, 3945, 0), new Position(2995, 3945, 0), };
			for (Position p : strange_floor) {
				if (p.getX() == player.getPosition().getX() && p.getY() == player.getPosition().getY()) {
					player.getStuckDelay().reset();
					route_found = true;
					player.moveTo(new Position(3002, 3945, 0));
					player.getPacketSender().sendMessage("You have been moved outside of the log balance!");
				}
			}
			if (!route_found)
				player.getPacketSender()
						.sendMessage("We have been unable to find a stuck tile for you to move off of!");
		}
		if (command[0].equals("skull")) {
			if (player.getSkullTimer() > 0) {
				player.getPacketSender().sendMessage("You are already skulled!");
				return;
			} else {
				CombatFactory.skullPlayer(player);
			}
		}
		if (command[0].equals("forumrank")) {
			if (player.getForumConnections() > 0) {
				player.getPacketSender().sendMessage("You have just used this command, please relog and try again!");
				return;
			}
			if (!GameSettings.FORUM_DATABASE_CONNECTIONS) {
				player.getPacketSender().sendMessage("This is currently disabled, try again in 30 minutes!");
				return;
			}
			if (!player.getRights().isStaff()) {
				try {
					player.addForumConnections(60);
					ForumDatabase.forumRankUpdate(player);
					player.getPacketSender().sendMessage("Your in-game rank has been added to your forum account.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				player.getPacketSender().sendMessage("Staff members are not allowed to use this command.");
			}
		}
		if (wholeCommand.equalsIgnoreCase("donate") || wholeCommand.equalsIgnoreCase("store")) {
			if (!GameSettings.STORE_CONNECTIONS) {
				player.getPacketSender()
						.sendMessage("The Store is currently turned off, please try again in 30 minutes!");
				return;
			}
			player.getPacketSender().sendString(1, "www.rune.live/store/");
			player.getPacketSender().sendMessage("Attempting to open: www.rune.live/store/");
		}
		if (wholeCommand.equalsIgnoreCase("wiki")) {
			player.getPacketSender().sendString(1, "www.runelive-2.wikia.com/wiki/runelive_2_Wikia");
			player.getPacketSender().sendMessage("Attempting to open: www.rune.live/wiki/");
		}
		if (wholeCommand.startsWith("auth")) {
			if (!GameSettings.VOTING_CONNECTIONS) {
				player.getPacketSender()
						.sendMessage("Voting connections are currently turned off, try again in 30 minutes!");
				return;
			}
			if (!player.getVoteTimer().elapsed(30000)) {
				player.getPacketSender().sendMessage("You have to wait 30 seconds in order to use ::auth!");
				return;
			}
			player.getVoteTimer().reset();
			Voting.useAuth(player, command[1]);
		}
		if (command[0].equalsIgnoreCase("attacks")) {
			int attack = DesolaceFormulas.getMeleeAttack(player);
			int range = DesolaceFormulas.getRangedAttack(player);
			int magic = DesolaceFormulas.getMagicAttack(player);
			player.getPacketSender().sendMessage("@bla@Melee attack: @or2@" + attack + "@bla@, ranged attack: @or2@"
					+ range + "@bla@, magic attack: @or2@" + magic);
		}
		if (command[0].equals("save")) {
			player.save();
			player.getPacketSender().sendMessage("Your progress has been saved.");
		}
		if (command[0].equals("vote")) {
			if (!GameSettings.VOTING_CONNECTIONS) {
				player.getPacketSender().sendMessage("Voting is currently turned off, please try again in 30 minutes!");
				return;
			}
			player.getPacketSender().sendString(1, "www.rune.live/vote/");
			player.getPacketSender().sendMessage("Attempting to open: www.rune.live/vote/");
		}
		if (command[0].equals("help") || command[0].equals("support")) {
			player.getPacketSender().sendString(1,
					"wwwrune.live/forum/index.php?app=core&module=global&section=register");
			player.getPacketSender().sendMessage("Attempting to open: www.rune.live/forum/?app=tickets");
			player.getPacketSender()
					.sendMessage("Please note this requires you to register on the forums, type ::register!");
		}
		if (command[0].equals("register")) {
			player.getPacketSender().sendString(1,
					"www.rune.live/forum/index.php?app=core&module=global&section=register");
			player.getPacketSender().sendMessage(
					"Attempting to open: www.rune.live/forum/index.php?app=core&module=global&section=register");
		}
		if (command[0].equals("forum") || command[0].equals("forums")) {
			player.getPacketSender().sendString(1, "www.rune.live/forum/");
			player.getPacketSender().sendMessage("Attempting to open: www.rune.live/forum/");
		}
		if (command[0].equals("scores") || command[0].equals("hiscores") || command[0].equals("highscores")) {
			if (!GameSettings.HIGHSCORE_CONNECTIONS) {
				player.getPacketSender()
						.sendMessage("Hiscores is currently turned off, please try again in 30 minutes!");
				return;
			}
			player.getPacketSender().sendString(1, "www.rune.live/hiscores/");
			player.getPacketSender().sendMessage("Attempting to open: www.rune.live/hiscores/");
		}
		if (command[0].equals("thread")) {
			int thread = Integer.parseInt(command[1]);
			player.getPacketSender().sendString(1,
					"www.rune.live/forum/index.php?/topic/" + thread + "-threadcommand/");
			player.getPacketSender().sendMessage("Attempting to open: Thread " + thread);
		}
		if (command[0].equals("Farming2")) {
			if (Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if (player.getLocation() != null && player.getWildernessLevel() > 20) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			Position position = new Position(2816, 3463, 0);
			TeleportHandler.teleportPlayer(player, position, player.getSpellbook().getTeleportType());
			;
			player.getPacketSender().sendMessage("Teleporting you home!");
		}

		if (command[0].startsWith("changepass")) {
			player.setInputHandling(new ChangePassword());
			player.getPacketSender().sendEnterInputPrompt("Enter a new password:");
		}
		if (command[0].equals("home")) {
			if (Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if (player.getLocation() != null && player.getWildernessLevel() > 20) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			Position position = new Position(3212, 3428, 0);
			TeleportHandler.teleportPlayer(player, position, player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Teleporting you home!");
		}
		if (command[0].equals("train")) {
			if (Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if (player.getLocation() != null && player.getWildernessLevel() > 20) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			Position position = new Position(2679, 3720, 0);
			TeleportHandler.teleportPlayer(player, position, player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Teleporting you to rock crabs!");
		}
		if (command[0].equals("edge")) {
			if (Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if (player.getLocation() != null && player.getWildernessLevel() > 20) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			Position position = new Position(3087, 3502, 0);
			TeleportHandler.teleportPlayer(player, position, player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Teleporting you to edgeville!");
		}
		if (command[0].equals("duel")) {
			if (Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if (player.getLocation() != null && player.getWildernessLevel() > 20) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			if (player.getLocation() == Location.DUEL_ARENA) {
				player.getPacketSender().sendMessage("You can't do this right now.");
				return;
			}
			Position position = new Position(3370, 3267, 0);
			TeleportHandler.teleportPlayer(player, position, player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Teleporting you to the duel arena!");
		}
		if (command[0].equals("teamspeak")) {
			player.getPacketSender().sendMessage("Teamspeak address: ts3.rune.live");
		}
		if (command[0].equals("market")) {
			if (Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if (player.getLocation() != null && player.getWildernessLevel() > 20) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			int random = Misc.getRandom(3);
			switch (random) {
			case 0:
				TeleportHandler.teleportPlayer(player, new Position(3212, 3429, 0),
						player.getSpellbook().getTeleportType());
				break;
			case 1:
				TeleportHandler.teleportPlayer(player, new Position(3213, 3429, 0),
						player.getSpellbook().getTeleportType());
				break;
			case 2:
				TeleportHandler.teleportPlayer(player, new Position(3213, 3428, 0),
						player.getSpellbook().getTeleportType());
				break;
			case 3:
				TeleportHandler.teleportPlayer(player, new Position(3212, 3428, 0),
						player.getSpellbook().getTeleportType());
				break;
			}
			player.getPacketSender().sendMessage("Welcome to the Market!");
		}
		if (command[0].equals("claim")) {
			if (!GameSettings.STORE_CONNECTIONS) {
				player.getPacketSender().sendMessage("The store is currently offline! Try again in 30 minutes.");
				return;
			}
			if (player.getLocation().equals(Location.DUNGEONEERING)
					|| player.getLocation().equals(Location.WILDERNESS)) {
				player.getPacketSender().sendMessage("You cannot do this here, you'll lose your scroll!");
				return;
			}
			if (player.claimingStoreItems) {
				player.getPacketSender().sendMessage("You already have a active store claim process going...");
				return;
			}
			player.getPacketSender().sendMessage("Checking for any store purchases...");
			Store.claimItem(player);
		}
		if (command[0].equals("empty")) {
			if (player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You can't use this command in the wilderness!");
				return;
			}
			player.getPacketSender().sendInterfaceRemoval().sendMessage("You clear your inventory.");
			player.getSkillManager().stopSkilling();
			player.getInventory().resetItems().refreshItems();
		}
		if (command[0].equals("gamble")) {
			if (Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if (player.getLocation() != null && player.getWildernessLevel() > 20) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			Position position = new Position(2441, 3090, 0);
			TeleportHandler.teleportPlayer(player, position, player.getSpellbook().getTeleportType());
			player.getPacketSender()
					.sendMessage("Welcome to the Gambling Area, make sure you always use a middle man for high bets!");
			player.getPacketSender().sendMessage("Recording your stake will only get the player banned if they scam.");
		}
		if (command[0].equals("players")) {
			player.getPacketSender().sendInterfaceRemoval();
			PlayersOnlineInterface.showInterface(player);
		}
		if (command[0].equalsIgnoreCase("[cn]")) {
			if (player.getInterfaceId() == 40172) {
				ClanChatManager.setName(player, wholeCommand.substring(wholeCommand.indexOf(command[1])));
			}
		}
	}
}