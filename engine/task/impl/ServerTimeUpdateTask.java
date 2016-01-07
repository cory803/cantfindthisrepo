package com.ikov.engine.task.impl;

import com.ikov.engine.task.Task;
import com.ikov.GameSettings;
import com.ikov.model.Locations;
import com.ikov.util.Misc;
import com.ikov.engine.task.TaskManager;
import com.ikov.world.World;
import com.ikov.world.content.minigames.impl.PestControl;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.io.BufferedWriter;
import java.io.File;

/**
 * @author Gabriel Hannason
 */
public class ServerTimeUpdateTask extends Task {

	public ServerTimeUpdateTask() {
		super(40);
	}

	private int tick = 0;
	
	public static void start_configuration_process() {
		TaskManager.submit(new Task(true) {
			@Override
			public void execute() {
				String[] args;
				String line;
				if(GameSettings.CONFIGURATION_TIME > 0) {
					GameSettings.CONFIGURATION_TIME--;
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
		if(tick >= 6 && (Locations.PLAYERS_IN_WILD >= 3 || Locations.PLAYERS_IN_DUEL_ARENA >= 3 || PestControl.TOTAL_PLAYERS >= 3)) {
			if(Locations.PLAYERS_IN_WILD > Locations.PLAYERS_IN_DUEL_ARENA && Locations.PLAYERS_IN_WILD > PestControl.TOTAL_PLAYERS || Misc.getRandom(3) == 1 && Locations.PLAYERS_IN_WILD >= 2) {
				World.sendMessage("<img=10> @blu@There are currently "+Locations.PLAYERS_IN_WILD+" players roaming the Wilderness!");
			}
			tick = 0;
		}
		tick++;
	}
}