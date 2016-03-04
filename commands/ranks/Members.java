package com.ikov.commands.ranks;

import java.util.concurrent.TimeUnit;

import com.ikov.GameSettings;
import com.ikov.model.input.impl.ChangePassword;
import com.ikov.model.Locations.Location;
import com.ikov.model.Store;
import com.ikov.world.content.combat.CombatFactory;
import com.ikov.model.PlayerRights;
import com.ikov.model.Position;
import com.ikov.util.Auth;
import com.ikov.util.Misc;
import com.ikov.world.World;
import com.ikov.world.content.Achievements;
import com.ikov.world.content.Command;
import com.ikov.world.content.transportation.TeleportHandler;
import com.ikov.world.content.PlayersOnlineInterface;
import com.ikov.world.content.Achievements.AchievementData;
import com.ikov.world.content.clan.ClanChatManager;
import com.ikov.world.content.combat.DesolaceFormulas;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.util.Logs;
import com.ikov.world.content.skill.impl.dungeoneering.Dungeoneering;

public class Members {
	
	/**
	* @Author Jonathan Sirens
	* Initiates Command
	**/
	
	public static void initiate_command(final Player player, String[] command, String wholeCommand) {
		if (command[0].equalsIgnoreCase("changelevel")) {
			/*if(player.getEquipment().getFreeSlots() != player.getEquipment().capacity()) {
				player.getPacketSender().sendMessage("Please unequip all your items first.");
				return;
			}
			String skill = command[1];
			String newLevel = command[2];
			if(Integer.parseInt(newLevel) < 1) {
				player.getPacketSender().sendMessage("You cannot set your level to "+newLevel+".");
				return;
			}
			if(Integer.parseInt(newLevel) > 99) {
				player.getPacketSender().sendMessage("You cannot set your level to "+newLevel+".");
				return;
			}
			switch(skill) {
			case "att":
			case "attack":
			case "atk":
				if(player.getSkillManager().getMaxLevel(Skill.ATTACK) > Integer.parseInt(newLevel)) {
					player.getPacketSender().sendMessage("You just changed your attack from "+player.getSkillManager().getCurrentLevel(Skill.ATTACK)+" to "+newLevel+"." );
					player.getSkillManager().setMaxLevel(Skill.ATTACK, Integer.parseInt(newLevel), true);
					player.getSkillManager().setCurrentLevel(Skill.ATTACK, Integer.parseInt(newLevel), true);
					player.getSkillManager().setExperience(Skill.ATTACK, SkillManager.getExperienceForLevel(Integer.parseInt(newLevel)));
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
					player.getSkillManager().setExperience(Skill.DEFENCE, SkillManager.getExperienceForLevel(Integer.parseInt(newLevel)));
				} else {
					player.getPacketSender().sendMessage("You cannot set a skill to be a higher level than it currently is.");
				}
				break;
			case "pray":
			case "prayer":
				if(player.getSkillManager().getMaxLevel(Skill.PRAYER) > Integer.parseInt(newLevel)) {
					player.getPacketSender().sendMessage("You just changed your prayer from "+player.getSkillManager().getCurrentLevel(Skill.PRAYER)/10+" to "+newLevel+"." );
					player.getSkillManager().setMaxLevel(Skill.PRAYER, Integer.parseInt(newLevel)*10, true);
					player.getSkillManager().setCurrentLevel(Skill.PRAYER, Integer.parseInt(newLevel)*10, true);
					player.getSkillManager().setExperience(Skill.PRAYER, SkillManager.getExperienceForLevel(Integer.parseInt(newLevel)));
				} else {
					player.getPacketSender().sendMessage("You cannot set a skill to be a higher level than it currently is.");
				}
				break;
				default:
					player.getPacketSender().sendMessage("The command syntax example - use a skill below: ::changelevel prayer 52");
					player.getPacketSender().sendMessage("You can only adjust prayer, defence and attack stats.");
			}*/ 
					player.getPacketSender().sendMessage("Currently Disabled.");
			
		}
		if (command[0].equalsIgnoreCase("staffonline")) {
			boolean isStaff = player.getRights() == PlayerRights.OWNER || player.getRights() == PlayerRights.ADMINISTRATOR || player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.SUPPORT;
		}
		if (command[0].equalsIgnoreCase("commands")) {
			if(player.getLocation() == Location.DUNGEONEERING) {
				player.getPacketSender().sendMessage("You cannot open the commands in dungeoneering.");
				return;
			}
			player.getPacketSender().sendTab(GameSettings.QUESTS_TAB);
			Command.open(player);
		}
		if (command[0].equals("stuck")) {
			if(player.getCombatBuilder().isBeingAttacked() || player.getCombatBuilder().isAttacking()) {
				player.getPacketSender().sendMessage("You cannot use this command while in combat!");
				return;
			}
			if(!player.getStuckDelay().elapsed(300000)) {
				player.getPacketSender().sendMessage("You have not waited the entire 5 minutes to be able to use this command again.");
				return;
			}
			player.getStuckDelay().reset();
			player.moveTo(new Position(3087, 3502, 0));
			player.getPacketSender().sendMessage("You have gotten yourself unstuck. You must wait 5 minutes to use it again.");
		}
		if (command[0].equals("skull")) {
			if(player.getSkullTimer() > 0) {
				player.getPacketSender().sendMessage("You are already skulled!");
				return;
			} else {
			CombatFactory.skullPlayer(player);
			}
		}
		if (command[0].equalsIgnoreCase("auth")) {
			if(player.getLocation() == Location.DUNGEONEERING) {
				player.getPacketSender().sendMessage("You can't claim a vote in Dungeoneering!");
				return;
			}
			boolean can_continue = true;
			if (player.voteCount > 4) {
				player.setCanVote(false);
			}
			if(!GameSettings.VOTING_CONNECTIONS) {
				player.getPacketSender().sendMessage("Voting is currently turned off, please try again in 30 minutes!");
				return;
			}
			if(player.isCanVote() == false) {
				player.getPacketSender().sendMessage("You have been banned from voting for abusing the system. Appeal online.");
				return;
			}
			if(GameSettings.DOUBLE_VOTE_TOKENS) {
				if(player.getInventory().getFreeSlots() <= 1) {
					player.getPacketSender().sendMessage("You need to have atleast 2 free inventory spaces to claim an auth code!");
					can_continue = false;
				}
			} else {
				if(player.getInventory().getFreeSlots() == 0) {
					player.getPacketSender().sendMessage("You need to have atleast 1 free inventory spaces to claim an auth code!");
					can_continue = false;
				}
			}
			if(!can_continue) {
				return;
			}
			String authCode = command[1];
				try {
					Auth.connect();
					if (Auth.checkVote(authCode)) {
						if(GameSettings.DOUBLE_VOTE_TOKENS) {
							player.getInventory().add(10944, 2);
						} else {
							player.getInventory().add(10944, 1);	
						}	
						
						if(GameSettings.TRIPLE_VOTE_TOKENS) {
							player.getInventory().add(10944, 1);	
						}

						Achievements.doProgress(player, AchievementData.VOTE_100_TIMES);
						if (player.getVotesClaimed() == 100) {
							Achievements.finishAchievement(player, AchievementData.VOTE_100_TIMES);
						}
						player.setVotesClaimed(1);
						player.voteCount++;
						player.getPacketSender().sendMessage("You have claimed "+player.voteCount+" of your 5 votes today. If you abuse the system your ");
						player.getPacketSender().sendMessage("account will be banned from voting.");
						GameSettings.AUTHS_CLAIMED++;
						if(GameSettings.AUTHS_CLAIMED == 25) {
							World.sendMessage("<img=10><col=2F5AB7>Another <col=9A0032>25<col=2f5ab7> auth codes have been claimed by using ::vote!");
							GameSettings.AUTHS_CLAIMED = 10;
						}
						Auth.updateVote(authCode);
						Logs.write_data(player.getUsername()+ ".txt", "auth_claims", "An auth code has been claimed.");

					} else {
						player.getPacketSender().sendMessage("The authcode you have entered is invalid.");
					}
				} catch (Exception e) {
					player.getPacketSender().sendMessage("Error connecting to the database. Please try again later.");
					e.printStackTrace();
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
		if (wholeCommand.equalsIgnoreCase("wiki")) {
			player.getPacketSender().sendString(1, "www.ikov-2.wikia.com/wiki/Ikov_2_Wikia");
			player.getPacketSender().sendMessage("Attempting to open: www.ikov2.org/wiki/");
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
		if (command[0].equals("help") || command[0].equals("support")) {
			player.getPacketSender().sendString(1, "wwwikov2.org/forum/index.php?app=core&module=global&section=register");
			player.getPacketSender().sendMessage("Attempting to open: www.ikov2.org/forum/?app=tickets");
			player.getPacketSender().sendMessage("Please note this requires you to register on the forums, type ::register!");
		}
		if (command[0].equals("register")) {
			player.getPacketSender().sendString(1, "www.ikov2.org/forum/index.php?app=core&module=global&section=register");
			player.getPacketSender().sendMessage("Attempting to open: www.ikov2.org/forum/index.php?app=core&module=global&section=register");
		}
		if (command[0].equals("forum") || command[0].equals("forums")) {
			player.getPacketSender().sendString(1, "www.ikov2.org/forum/");
			player.getPacketSender().sendMessage("Attempting to open: www.ikov2.org/forum/");
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
			TeleportHandler.teleportPlayer(player, position, player.getSpellbook().getTeleportType());;
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
			TeleportHandler.teleportPlayer(player, position, player.getSpellbook().getTeleportType());
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
			TeleportHandler.teleportPlayer(player, position, player.getSpellbook().getTeleportType());
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
			TeleportHandler.teleportPlayer(player, position, player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Teleporting you to edgeville!");
		}
		if (command[0].equals("teamspeak")) {
			player.getPacketSender().sendMessage("Teamspeak address: ts3.ikov2.org");
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
			TeleportHandler.teleportPlayer(player, position, player.getSpellbook().getTeleportType());;
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
			Position position = new Position(2441, 3090, 0);
			TeleportHandler.teleportPlayer(player, position, player.getSpellbook().getTeleportType());;
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