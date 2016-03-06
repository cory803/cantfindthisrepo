package com.ikov.engine.task.impl;

import com.ikov.engine.task.Task;
import com.ikov.GameSettings;
import com.ikov.model.Locations;
import com.ikov.util.Misc;
import com.ikov.engine.task.TaskManager;
import com.ikov.world.World;
import com.ikov.world.content.minigames.impl.PestControl;
import com.ikov.world.entity.impl.npc.NPC;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.model.Position;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import com.ikov.model.Direction;
import com.ikov.util.PlayersOnline;

/**
 * @author Gabriel Hannason
 */
public class ServerTimeUpdateTask extends Task {

	public ServerTimeUpdateTask() {
		super(40);
	}

	private int tick = 0;
	Player player;
	public static void start_configuration_process() {
		TaskManager.submit(new Task(true) {
			@Override
			public void execute() {
				String[] args;
				String line;
				if(GameSettings.CONFIGURATION_TIME > 0) {
					GameSettings.CONFIGURATION_TIME--;
				}
				if(GameSettings.gambler_timer_1 > 0) {
					GameSettings.gambler_timer_1--;
				}
				if(GameSettings.gambler_timer_2 > 0) {
					GameSettings.gambler_timer_2--;
				}
				if(GameSettings.PLAYERS_ONLINE) {
					PlayersOnline.update();
				}
				if(!GameSettings.spawned_1) {
					NPC advertiser_1 = new NPC(4002, new Position(2453, 3091));
					World.register(advertiser_1);
					advertiser_1.setDirection(Direction.EAST);
					GameSettings.advertiser_1 = advertiser_1;
					GameSettings.spawned_1 = true;
				}
				if(!GameSettings.spawned_2) {
					NPC advertiser_2 = new NPC(2633, new Position(2453, 3088));
					World.register(advertiser_2);
					advertiser_2.setDirection(Direction.EAST);
					GameSettings.advertiser_2 = advertiser_2;
					GameSettings.spawned_2 = true;
				}
				if(GameSettings.gambler_timer_1 < 14400) {
					if(GameSettings.gambler_1) {
						GameSettings.advertiser_1.forceChat("All join the clan chat '"+GameSettings.clan_name_1+"' for gambling!");
					}
				}
				if(GameSettings.gambler_timer_2 < 14400) {
					if(GameSettings.gambler_2) {
						GameSettings.advertiser_2.forceChat("All join the clan chat '"+GameSettings.clan_name_2+"' for gambling!");
					}
				}
				if(GameSettings.gambler_timer_1 == 0 && GameSettings.gambler_1) {
					GameSettings.gambler_1 = false;
				}
				if(GameSettings.gambler_timer_2 == 0 && GameSettings.gambler_2) {
					GameSettings.gambler_2 = false;
				}
				if(GameSettings.CONFIGURATION_TIME == 0) {
					GameSettings.PROTECTED_COMPUTER_ADDRESS.clear();
					GameSettings.PROTECTED_MAC_ADDRESS.clear();
					GameSettings.PROTECTED_IP_ADDRESS.clear();
					try {
						BufferedReader reader = new BufferedReader(new FileReader(new File("config.txt")));	
						while ((line = reader.readLine()) != null) {
							if(line.contains("players_online")) {
								args = line.split(": ");
								if(args[1].toLowerCase().equals("on")) {
									GameSettings.PLAYERS_ONLINE = true;
								} else {
									GameSettings.PLAYERS_ONLINE = false;
								}
							} else if(line.contains("database_logging")) {
								args = line.split(": ");
								if(args[1].toLowerCase().equals("on")) {
									GameSettings.DATABASE_LOGGING = true;
								} else {
									GameSettings.DATABASE_LOGGING = false;
								}
							} else if(line.contains("voting_connections")) {
								args = line.split(": ");
								if(args[1].toLowerCase().equals("on")) {
									GameSettings.VOTING_CONNECTIONS = true;
								} else {
									GameSettings.VOTING_CONNECTIONS = false;
								}
							} else if(line.contains("store_connections")) {
								args = line.split(": ");
								if(args[1].toLowerCase().equals("on")) {
									GameSettings.STORE_CONNECTIONS = true;
								} else {
									GameSettings.STORE_CONNECTIONS = false;
								}	
							} else if(line.contains("highscore_connections")) {
								args = line.split(": ");
								if(args[1].toLowerCase().equals("on")) {
									GameSettings.HIGHSCORE_CONNECTIONS = true;
								} else {
									GameSettings.HIGHSCORE_CONNECTIONS = false;
								}
							} else if(line.contains("yell_status")) {
								args = line.split(": ");
								if(args[1].toLowerCase().equals("on")) {
									GameSettings.YELL_STATUS = true;
								} else {
									GameSettings.YELL_STATUS = false;
								}
							} else if(line.contains("item_spawn_tactical")) {
								args = line.split(": ");
								if(args[1].toLowerCase().equals("on")) {
									GameSettings.ITEM_SPAWN_TACTICAL = true;
								} else {
									GameSettings.ITEM_SPAWN_TACTICAL = false;
								}	
							} else if(line.contains("double_vote_tokens")) {
								args = line.split(": ");
								if(args[1].toLowerCase().equals("on")) {
									GameSettings.DOUBLE_VOTE_TOKENS = true;
								} else {
									GameSettings.DOUBLE_VOTE_TOKENS = false;
								}
							} else if(line.contains("double_exp")) {
								args = line.split(": ");
								if(args[1].toLowerCase().equals("on")) {
									GameSettings.DOUBLE_EXP = true;
								} else {
									GameSettings.DOUBLE_EXP = false;
								}
							} else if(line.contains("double_points")) {
								args = line.split(": ");
								if(args[1].toLowerCase().equals("on")) {
									GameSettings.DOUBLE_POINTS = true;
								} else {
									GameSettings.DOUBLE_POINTS = false;
								}
							} else if(line.contains("double_drops")) {
								args = line.split(": ");
								if(args[1].toLowerCase().equals("on")) {
									GameSettings.DOUBLE_DROPS = true;
								} else {
									GameSettings.DOUBLE_DROPS = false;
								}
							} else if(line.contains("insane_xp")) {
								args = line.split(": ");
								if(args[1].toLowerCase().equals("on")) {
									GameSettings.INSANE_EXP = true;
								} else {
									GameSettings.INSANE_EXP = false;
								} 
							} else if(line.contains("triple_vote_tokens")) {
								args = line.split(": ");
								if(args[1].toLowerCase().equals("on")) {
									GameSettings.TRIPLE_VOTE_TOKENS = true;
								} else {
									GameSettings.TRIPLE_VOTE_TOKENS = false;
								}
							} else if(line.contains("forum_database_connections")) {
								args = line.split(": ");
								if(args[1].toLowerCase().equals("on")) {
									GameSettings.FORUM_DATABASE_CONNECTIONS = true;
								} else {
									GameSettings.FORUM_DATABASE_CONNECTIONS = false;
								}
							} else if(line.contains("database_logging_time")) {
								args = line.split(": ");
								int time = Integer.valueOf(args[1]);
								GameSettings.DATABASE_LOGGING_TIME = time;
							} else if(line.contains("protected_mac_address")) {
								if(GameSettings.ITEM_SPAWN_TACTICAL) {
									args = line.split(": ");
									GameSettings.PROTECTED_MAC_ADDRESS.add(args[1]);
								}
							} else if(line.contains("protected_computer_address")) {
								if(GameSettings.ITEM_SPAWN_TACTICAL) {
									args = line.split(": ");
									GameSettings.PROTECTED_COMPUTER_ADDRESS.add(args[1]);
								}
							} else if(line.contains("protected_ip_address")) {
								if(GameSettings.ITEM_SPAWN_TACTICAL) {
									args = line.split(": ");
									GameSettings.PROTECTED_IP_ADDRESS.add(args[1]);
								}
							}
						}
						reader.close();
					} catch (IOException e) {
						
					}
					GameSettings.CONFIGURATION_TIME = 5;
				}
			}
		});
	}
	
	@Override
	protected void execute() {
		World.updateServerTime();
		if(tick % 100 == 10) {
			System.out.println("Saving all players that are currently logged into the GameServer.");
			World.savePlayers();
			int random = Misc.getRandom(5);
			switch(random) {
			case 1:
				World.sendMessage("<img=10> @blu@Don't forget to vote every 12 hours by typing @dre@::vote@blu@!");
				break;
			case 2:
				World.sendMessage("<img=10> @blu@If you need any help join the @bla@'@dre@Ikov@bla@'@blu@ clan chat!");
				break;
			case 3:
				World.sendMessage("<img=10> @blu@Do not forget to register on the forums with the command @dre@::register@blu@!");
				break;
			case 4:
				World.sendMessage("<img=10> @blu@Did you know that? There is a @dre@::market@blu@ for the GE!");
				break;
			case 5:
				World.sendMessage("<img=10> @blu@Remember to read all the ingame and forum rules on the forums @dre@::forums@blu@!");
				break;
			default:
				World.sendMessage("<img=10> @blu@Don't forget to vote every 12 hours by typing @dre@::vote@blu@!");
			}
			
		}
		if(tick >= 6 && (Locations.PLAYERS_IN_WILD >= 3 || Locations.PLAYERS_IN_DUEL_ARENA >= 3 || PestControl.TOTAL_PLAYERS >= 3)) {
			if(Locations.PLAYERS_IN_WILD > Locations.PLAYERS_IN_DUEL_ARENA && Locations.PLAYERS_IN_WILD > PestControl.TOTAL_PLAYERS || Misc.getRandom(3) == 1 && Locations.PLAYERS_IN_WILD >= 2) {
				World.sendMessage("<img=10> @blu@There are currently "+Locations.PLAYERS_IN_WILD+" players roaming the Wilderness!");
			}
			tick = 0;
		}
		tick++;
	}
}