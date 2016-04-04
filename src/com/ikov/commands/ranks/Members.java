package com.ikov.commands.ranks;

import com.ikov.GameSettings;
import com.ikov.model.input.impl.ChangePassword;
import com.ikov.model.Locations.Location;
import com.ikov.model.Store;
import com.ikov.world.content.combat.CombatFactory;
import com.ikov.world.content.PlayerLogs;
import com.ikov.model.Position;
import com.ikov.util.ForumDatabase;
import com.rspserver.mvh.*;
import com.ikov.world.content.PlayerPunishment;
import com.ikov.world.World;
import com.ikov.world.content.Achievements;
import com.ikov.world.content.Command;
import com.ikov.world.content.transportation.TeleportHandler;
import com.ikov.world.content.dialogue.DialogueManager;
import com.ikov.world.content.PlayersOnlineInterface;
import com.ikov.world.content.Achievements.AchievementData;
import com.ikov.util.Misc;
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
		if(player.isJailed()) {
			player.getPacketSender().sendMessage("You cannot use commands in jail... You're in jail.");
			return;
		}
		if (command[0].equalsIgnoreCase("time")) {
			player.forceChat("[IKOV] "+player.getUsername()+" has played for ["+Misc.getHoursPlayed((player.getTotalPlayTime() + player.getRecordedLogin().elapsed()))+"]");
		}
		if (command[0].equalsIgnoreCase("mb")) {
			if(Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if(player.getLocation() != null && player.getWildernessLevel() > 20) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			Position position = new Position(2539, 4715, 0);
			TeleportHandler.teleportPlayer(player, position, player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Teleporting you to mage bank!");
		}
		if (command[0].equalsIgnoreCase("wests")) {
			if(Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if(player.getLocation() != null && player.getWildernessLevel() > 20) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			DialogueManager.start(player, 212);
			player.setDialogueActionId(212);
		}	
		if (command[0].equalsIgnoreCase("easts")) {
			if(Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if(player.getLocation() != null && player.getWildernessLevel() > 20) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			DialogueManager.start(player, 213);
			player.setDialogueActionId(213);
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
			if(player.getTeleblockTimer() > 0) {
				player.getPacketSender().sendMessage("You cannot teleport with this command while teleblocked.");
				return;
			}
			if(player.getCombatBuilder().isBeingAttacked() || player.getCombatBuilder().isAttacking()) {
				player.getPacketSender().sendMessage("You cannot use this command while in combat!");
				return;
			}
			if(!player.getStuckDelay().elapsed(300000)) {
				player.getPacketSender().sendMessage("You have not waited the entire 5 minutes to be able to use this command again.");
				return;
			}
			boolean route_found = false;
			Position[] obstacle_pipe = {
				new Position(3004, 3938, 0), new Position(3004, 3939, 0),
				new Position(3004, 3940, 0), new Position(3004, 3941, 0),
				new Position(3004, 3942, 0), new Position(3004, 3943, 0),
				new Position(3004, 3944, 0), new Position(3004, 3945, 0),
				new Position(3004, 3946, 0), new Position(3004, 3947, 0),
				new Position(3004, 3948, 0), new Position(3004, 3949, 0),
				new Position(3003, 3948, 0), new Position(3003, 3947, 0),
				new Position(3003, 3946, 0), new Position(3003, 3945, 0),
				new Position(3003, 3944, 0), new Position(3003, 3943, 0),
				new Position(3003, 3942, 0), new Position(3003, 3941, 0),
				new Position(3003, 3940, 0), new Position(3003, 3939, 0),
				new Position(3005, 3939, 0), new Position(3005, 3940, 0),
				new Position(3005, 3941, 0), new Position(3005, 3942, 0),
				new Position(3005, 3943, 0), new Position(3005, 3944, 0),
				new Position(3005, 3945, 0), new Position(3005, 3946, 0),
				new Position(3005, 3947, 0), new Position(3005, 3948, 0)
			};
			for(Position p : obstacle_pipe) {
				if(p.getX() == player.getPosition().getX() && p.getY() == player.getPosition().getY()) {
					player.getStuckDelay().reset();
					route_found = true;
					player.moveTo(new Position(3004, 3937, 0));
					player.getPacketSender().sendMessage("You have been moved outside of the obstacle pipe!");
				}
			}
			Position[] ropeswing = {
				new Position(3006, 3954, 0), new Position(3005, 3954, 0),
				new Position(3006, 3955, 0), new Position(3005, 3955, 0),
				new Position(3006, 3956, 0), new Position(3005, 3956, 0),
				new Position(3006, 3957, 0), new Position(3005, 3957, 0),
				new Position(3004, 3954, 0),
				new Position(3004, 3955, 0),
				new Position(3004, 3956, 0),
				new Position(3004, 3957, 0)
			};
			for(Position p : ropeswing) {
				if(p.getX() == player.getPosition().getX() && p.getY() == player.getPosition().getY()) {
					player.getStuckDelay().reset();
					route_found = true;
					player.moveTo(new Position(3005, 3953, 0));
					player.getPacketSender().sendMessage("You have been moved outside of the rope swing!");
				}
			}
			Position[] strange_floor = {
				new Position(2997, 3960, 0), new Position(2998, 3960, 0),
				new Position(2999, 3960, 0), new Position(3000, 3960, 0),
				new Position(3001, 3960, 0)
			};
			for(Position p : strange_floor) {
				if(p.getX() == player.getPosition().getX() && p.getY() == player.getPosition().getY()) {
					player.getStuckDelay().reset();
					route_found = true;
					player.moveTo(new Position(3002, 3960, 0));
					player.getPacketSender().sendMessage("You have been moved outside of the strange floor!");
				}
			}
			Position[] log_balance = {
				new Position(3002, 3945, 0), new Position(3001, 3945, 0),
				new Position(3000, 3945, 0), new Position(2999, 3945, 0),
				new Position(2998, 3945, 0), new Position(2997, 3945, 0),
				new Position(2996, 3945, 0), new Position(2995, 3945, 0),
			};
			for(Position p : strange_floor) {
				if(p.getX() == player.getPosition().getX() && p.getY() == player.getPosition().getY()) {
					player.getStuckDelay().reset();
					route_found = true;
					player.moveTo(new Position(3002, 3945, 0));
					player.getPacketSender().sendMessage("You have been moved outside of the log balance!");
				}
			}
			if(!route_found)
				player.getPacketSender().sendMessage("We have been unable to find a stuck tile for you to move off of!");
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
			if (player.getLocation() == Location.DUNGEONEERING || (player.getLocation() == Location.WILDERNESS) || (player.getLocation() == Location.DUEL_ARENA)) {
				player.getPacketSender().sendMessage("You can't redeem a vote in your current location.");
				return;
			} if (player.isDying()) {
				player.getPacketSender().sendMessage("You can't redeem votes whilst dying");
				return;
			}
			if (player.voteCount >= 10) {
				PlayerPunishment.voteBan(player.getUsername());
			}
			if (!GameSettings.VOTING_CONNECTIONS || PlayerPunishment.isVoteBanned(player.getUsername())) {
				player.getPacketSender().sendMessage("There was an error whilst dealing with your request");
				player.getPacketSender().sendMessage("If this problem continues, please post on the forums");
			} else {
				String authCode = command[1];
				try {
					boolean success = AuthService.provider().redeemNow(authCode);
					if (success) {
						player.getBank(player.getCurrentBankTab()).add(10944, GameSettings.AUTH_AMOUNT);
						player.getPacketSender().sendMessage("You have had "+GameSettings.AUTH_AMOUNT+" x Auth Rewards added to your bank.");
						Achievements.doProgress(player, AchievementData.VOTE_100_TIMES);
						if (player.getVotesClaimed() == 100) {
							Achievements.finishAchievement(player, AchievementData.VOTE_100_TIMES);
						}
						player.setVotesClaimed(1);
						player.voteCount++;
						player.getPacketSender().sendMessage("You have claimed " + player.voteCount + " of your 10 votes today. If you abuse the system your ");
						player.getPacketSender().sendMessage("account will be banned from voting.");
						GameSettings.AUTHS_CLAIMED++;
						if (GameSettings.AUTHS_CLAIMED == 25) {
							World.sendMessage("<img=4><col=2F5AB7>Another <col=9A0032>25<col=2f5ab7> auth codes have been claimed by using ::vote!");
							GameSettings.AUTHS_CLAIMED = 0;
						}
						PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " has claimed an auth code " + authCode + "!");
					} else {
						player.getPacketSender().sendMessage("The authcode you have entered is invalid. Please try again.");
					}
				} catch (Exception e) {
					player.getPacketSender().sendMessage("Error connecting to the database. Please try again later.");
					e.printStackTrace();
				}
				return;
			}
		}
		if (command[0].equals("forumrank")) {
			if(player.getForumConnections() > 0) {
				player.getPacketSender().sendMessage("You have just used this command, please relog and try again!");
				return;
			}
			if(!GameSettings.FORUM_DATABASE_CONNECTIONS) {
				player.getPacketSender().sendMessage("This is currently disabled, try again in 30 minutes!");
				return;
			}
			if(!player.getRights().isStaff()) {
				try {
					ForumDatabase.connect();
					player.addForumConnections(60);
					int current_rank_id = ForumDatabase.getCurrentMemberID(player.getUsername());
					if(current_rank_id != ForumDatabase.regular_donator 
					&& current_rank_id != ForumDatabase.super_donator
					&& current_rank_id != ForumDatabase.extreme_donator
					&& current_rank_id != ForumDatabase.legendary_donator
					&& current_rank_id != ForumDatabase.uber_donator
					&& current_rank_id != ForumDatabase.validating
					&& current_rank_id != ForumDatabase.members) {
						player.getPacketSender().sendMessage("You have a rank on the forum that is not supported with this command.");
						return;
					} else if(current_rank_id == ForumDatabase.banned) {
						player.getPacketSender().sendMessage("Your forum account is banned.");
						return;
					}
					player.setForumConnectionsRank(player.getDonorRights());
					if(ForumDatabase.check_has_username(player.getUsername())) {
						ForumDatabase.update_donator_rank(player.getUsername(), player.getDonorRights());
						player.getPacketSender().sendMessage("Your in-game rank has been added to your forum account.");
					} else {
						player.getPacketSender().sendMessage("We noticed you don't have a forum account! You should make one at <col=ff0000><shad=0>::register");
					}
					ForumDatabase.destroy_connection();
				} catch (Exception e) {
					System.out.println(e);
				}
			} else {
				player.getPacketSender().sendMessage("Staff members are not aloud to use this command.");
			}
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
			if(player.getLocation() != null && player.getWildernessLevel() > 20) {
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
			if(player.getLocation() != null && player.getWildernessLevel() > 20) {
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
			if(player.getLocation() != null && player.getWildernessLevel() > 20) {
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
			if(player.getLocation() != null && player.getWildernessLevel() > 20) {
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
			if(player.getLocation() != null && player.getWildernessLevel() > 20) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			int random = Misc.getRandom(3);
			switch(random) {
			case 0:
				TeleportHandler.teleportPlayer(player, new Position(3212, 3429, 0), player.getSpellbook().getTeleportType());
			break;
			case 1:
				TeleportHandler.teleportPlayer(player, new Position(3213, 3429, 0), player.getSpellbook().getTeleportType());
			break;
			case 2:
				TeleportHandler.teleportPlayer(player, new Position(3213, 3428, 0), player.getSpellbook().getTeleportType());
			break;
			case 3:
				TeleportHandler.teleportPlayer(player, new Position(3212, 3428, 0), player.getSpellbook().getTeleportType());
			break;
			}
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
			if(player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You can't use this command in the wilderness!");
				return;
			}
			player.getPacketSender().sendInterfaceRemoval().sendMessage("You clear your inventory.");
			player.getSkillManager().stopSkilling();
			player.getInventory().resetItems().refreshItems();
		}
		if (command[0].equals("gamble")) {
			if(Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
				return;
			}
			if(player.getLocation() != null && player.getWildernessLevel() > 20) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			Position position = new Position(2441, 3090, 0);
			TeleportHandler.teleportPlayer(player, position, player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to the Gambling Area, make sure you always use a middle man for high bets!");
			player.getPacketSender().sendMessage("Recording your stake will only get the player banned if they scam.");
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