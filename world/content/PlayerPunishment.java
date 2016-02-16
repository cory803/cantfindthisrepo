package com.ikov.world.content;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

public class PlayerPunishment {

	public static boolean isPlayerBanned(String username) {
		if(new File(PLAYER_BAN_DIRECTORY + username.toLowerCase()).exists()) {
			return true;
		}
		return false;
	}	
	
	public static boolean isIpBanned(String ip) {
		if(new File(IP_BAN_DIRECTORY + ip.toLowerCase()).exists()) {
			return true;
		}
		return false;
	}	
	
	public static boolean isMacBanned(String mac) {
		if(new File(MAC_BAN_DIRECTORY + mac.toLowerCase()).exists()) {
			return true;
		}
		return false;
	}	
	
	public static boolean isIpMuted(String ip) {
		if(new File(IP_MUTE_DIRECTORY + ip.toLowerCase()).exists()) {
			return true;
		}
		return false;
	}	
	
	public static boolean isMuted(String name) {
		if(new File(PLAYER_MUTE_DIRECTORY + name.toLowerCase()).exists()) {
			return true;
		}
		return false;
	}
	
	public static void ipBan(String ip) {
		try {
			new File(IP_BAN_DIRECTORY + ip).createNewFile();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}		
	
	public static void macBan(String mac) {
		try {
			new File(MAC_BAN_DIRECTORY + mac).createNewFile();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}	
	
	public static void ban(String name) {
		try {
			new File(PLAYER_BAN_DIRECTORY + name).createNewFile();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public static void ipMute(String ip) {
		try {
			new File(IP_MUTE_DIRECTORY + ip).createNewFile();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public static String getLastIpAddress(String name) {
		String line;
		String last_ip = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("./characters/"+name+".json")));	
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty()) {
					break;
				}
				if(line.contains("last-ip-address")) {
					line = line.substring(22).replace("\",", "");
					last_ip = line;
				}
			}
			reader.close();
		} catch (IOException e) {
			
		}
		return last_ip;
	}	
	
	public static String getLastSerialAddress(String name) {
		String line;
		String last_ip = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("./characters/"+name+".json")));	
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty()) {
					break;
				}
				if(line.contains("last-serial-address")) {
					line = line.substring(26).replace("\",", "");
					last_ip = line;
				}
			}
			reader.close();
		} catch (IOException e) {
			
		}
		return last_ip;
	}	
	
	public static String getLastMacAddress(String name) {
		String line;
		String last_ip = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("./characters/"+name+".json")));	
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty()) {
					break;
				}
				if(line.contains("last-mac-address")) {
					line = line.substring(23).replace("\",", "");
					last_ip = line;
				}
			}
			reader.close();
		} catch (IOException e) {
			
		}
		return last_ip;
	}
	
	public static void mute(String name) {
		try {
			new File(PLAYER_MUTE_DIRECTORY + name).createNewFile();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
		
	public static void unBan(String name) {
		new File(PLAYER_BAN_DIRECTORY + name).delete();
	}
	
	public static void unIpBan(String ip) {
		new File(IP_BAN_DIRECTORY + ip).delete();
	}	
	
	public static void unIpMute(String ip) {
		new File(IP_MUTE_DIRECTORY + ip).delete();
	}	
	
	public static void unMute(String name) {
		new File(PLAYER_MUTE_DIRECTORY + name).delete();
	}	
	
	public static void unMacBan(String mac) {
		new File(MAC_BAN_DIRECTORY + mac).delete();
	}
	
	/**
	 * The general punishment folder directory.
	 */
	public static final String PUNISHMENT_DIRECTORY = "./punishments";
	
	/**
	 * Leads to directory where banned account files are stored.
	 */
	public static final String PLAYER_BAN_DIRECTORY = PUNISHMENT_DIRECTORY + "/player_bans/";
	
	/**
	 * Leads to directory where muted account files are stored.
	 */
	public static final String PLAYER_MUTE_DIRECTORY = PUNISHMENT_DIRECTORY + "/player_mutes/";
	
	/**
	 * Leads to directory where banned account files are stored.
	 */
	public static final String IP_BAN_DIRECTORY = PUNISHMENT_DIRECTORY + "/ip_bans/";
	
	/**
	 * Leads to directory where muted account files are stored.
	 */
	public static final String IP_MUTE_DIRECTORY = PUNISHMENT_DIRECTORY + "/ip_mutes/";
	
	/**
	 * Leads to directory where banned account files are stored.
	 */
	public static final String MAC_BAN_DIRECTORY = PUNISHMENT_DIRECTORY + "/mac_bans/";	

}
