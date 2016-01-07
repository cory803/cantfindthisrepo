package com.ikov.commands.ranks;

import com.ikov.GameSettings;
import com.ikov.model.input.impl.ChangePassword;
import com.ikov.model.Locations.Location;
import com.ikov.model.Store;
import com.ikov.model.Position;
import com.ikov.model.Skill;
import com.ikov.util.Auth;
import com.ikov.util.Misc;
import com.ikov.world.World;
import com.ikov.world.content.Command;
import com.ikov.world.content.PlayersOnlineInterface;
import com.ikov.world.content.clan.ClanChatManager;
import com.ikov.world.content.combat.DesolaceFormulas;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.world.content.skill.impl.dungeoneering.Dungeoneering;

public class Members {
	
	/**
	* @Author Jonathan Sirens
	* Initiates Command
	**/
	
	public static void initiate_command(final Player player, String[] command, String wholeCommand) {
		if (command[0].equalsIgnoreCase("changelevel")) {
			String skill = command[1];
			String newLevel = command[2];
			switch(skill) {
			case "att":
			case "attack":
			case "atk":
				if(player.getSkillManager().getMaxLevel(Skill.ATTACK) > Integer.parseInt(newLevel)) {
					player.getPacketSender().sendMessage("You just changed your attack from "+player.getSkillManager().getCurrentLevel(Skill.ATTACK)+" to "+newLevel+"." );
					player.getSkillManager().setMaxLevel(Skill.ATTACK, Integer.parseInt(newLevel), true);
					player.getSkillManager().setCurrentLevel(Skill.ATTACK, Integer.parseInt(newLevel), true);
				} else {
					player.getPacketSender().sendMessage("You cannot set a skill to be a higher level than it currently is.");
				}
				break;
			case "def":
			case "defence":
			case "defense":
				if(player.getSkillManager().getMaxLevel(Skill.DEFENCE) > Integer.parseInt(newLevel)) {
					player.getPacketSender().sendMessage("You just changed your defence from "+player.getSkillManager().getCurrentLevel(Skill.DEFENCE)+" to "+newLevel+"." );
					player.getSkillManager().setMaxLevel(Skill.DEFENCE, Integer.parseInt(newLevel), true);
					player.getSkillManager().setCurrentLevel(Skill.DEFENCE, Integer.parseInt(newLevel), true);
				} else {
					player.getPacketSender().sendMessage("You cannot set a skill to be a higher level than it currently is.");
				}
				break;
			case "pray":
			case "prayer":
				if(player.getSkillManager().getMaxLevel(Skill.PRAYER) > Integer.parseInt(newLevel)) {
					player.getPacketSender().sendMessage("You just changed your prayer from "+player.getSkillManager().getCurrentLevel(Skill.PRAYER)+" to "+newLevel+"." );
					player.getSkillManager().setMaxLevel(Skill.PRAYER, Integer.parseInt(newLevel), true);
					player.getSkillManager().setCurrentLevel(Skill.PRAYER, Integer.parseInt(newLevel), true);
				} else {
					player.getPacketSender().sendMessage("You cannot set a skill to be a higher level than it currently is.");
				}
				break;
				default:
					player.getPacketSender().sendMessage("The command syntax example - use a skill below: ::changelevel prayer 52");
					player.getPacketSender().sendMessage("You can only adjust prayer, defence and attack stats.");
			}
			
		}
		if (command[0].equalsIgnoreCase("commands")) {
			player.getPacketSender().sendTab(GameSettings.QUESTS_TAB);
			Command.open(player);
		}
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
						Logs.write_data(player.getUsername()+ ".txt", "auth_claims", "An auth code has been claimed.");
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
		if (command[0].equals("scores") || command[0].equals("hiscores") || command[0].equals("highscores")) {
			if(!GameSettings.HIGHSCORE_CONNECTIONS) {
				player.getPacketSender().sendMessage("Hiscores is currently turned off, please try again in 30 minutes!");
				return;
			}
			player.getPacketSender().sendString(1, "www.ikov2.org/hiscores/");
			player.getPacketSender().sendMessage("Attempting to open: www.ikov2.org/hiscores/");
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
		if(command[0].equals("claim")) {
			if(!GameSettings.STORE_CONNECTIONS) {
				player.getPacketSender().sendMessage("The store is currently offline! Try again in 30 minutes.");
				return;
			}
			player.getPacketSender().sendMessage("Checking for any store purchases...");
			Store.start_store_process(player);
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