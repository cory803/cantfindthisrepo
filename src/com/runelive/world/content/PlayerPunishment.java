package com.runelive.world.content;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.runelive.GameServer;
import com.runelive.net.mysql.SQLCallback;
import com.runelive.world.entity.impl.player.Player;
import com.runelive.world.entity.impl.player.PlayerLoading;

public class PlayerPunishment {

	/*
	 * public static boolean isPlayerBanned(String username) { boolean
	 * continuation = false; GameServer.getCharacterPool().executeQuery(
	 * "SELECT * FROM `punishments` WHERE LOWER (`username`) = LOWER('" +
	 * username + "') LIMIT 1", new SQLCallback() { boolean continuation;
	 * 
	 * @Override public void queryComplete(ResultSet rs) throws SQLException {
	 * if (rs.next()) { continuation = true; } }
	 * 
	 * @Override public void queryError(SQLException e) { e.printStackTrace(); }
	 * private SQLCallback init(boolean var){ continuation = var; return this; }
	 * }.init(continuation)); return continuation; }
	 */

	public static boolean isPlayerBanned(String name) {
		if (new File(PLAYER_BAN_DIRECTORY + name.toLowerCase()).exists()) {
			return true;
		}
		return false;
	}

	public static boolean isIpBanned(String ip) {
		if (new File(IP_BAN_DIRECTORY + ip.toLowerCase()).exists()) {
			return true;
		}
		return false;
	}

	public static boolean isMacBanned(String mac) {
		if (new File(MAC_BAN_DIRECTORY + mac.toLowerCase()).exists()) {
			return true;
		}
		return false;
	}

	public static boolean isPcBanned(String add) {
		if (new File(PC_BAN_DIRECTORY + add.toLowerCase()).exists()) {
			return true;
		}
		return false;
	}

	public static boolean isIpMuted(String ip) {
		if (new File(IP_MUTE_DIRECTORY + ip.toLowerCase()).exists()) {
			return true;
		}
		return false;
	}

	public static boolean isMuted(String name) {
		if (new File(PLAYER_MUTE_DIRECTORY + name.toLowerCase()).exists()) {
			return true;
		}
		return false;
	}

	public static boolean isVoteBanned(String name) {
		if (new File(VOTE_BAN_DIRECTORY + name.toLowerCase()).exists()) {
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

	public static void voteBan(String vote) {
		try {
			new File(VOTE_BAN_DIRECTORY + vote).createNewFile();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public static void macBan(String mac) {
	}

	public static void pcBan(String add) {
		if (add.toLowerCase().equals("none")) {
			return;
		}
		try {
			new File(PC_BAN_DIRECTORY + add).createNewFile();
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

	public static Player massBan(Player staff, String victimUsername, Player victim) {
		GameServer.getCharacterPool().executeQuery(
				"SELECT * FROM `accounts` WHERE username = '" + victimUsername + "' LIMIT 1", new SQLCallback() {
					@Override
					public void queryComplete(ResultSet rs) throws SQLException {
						if (rs.next()) {
							String json = rs.getString("json");
							PlayerLoading.decodeJson(victim, json);

							String serial = String.valueOf(victim.getLastMacAddress());
							String ip = victim.getLastIpAddress();
							PlayerPunishment.ban(victimUsername);
							if (victim.getLastIpAddress() != null && !victim.getLastIpAddress().isEmpty()
									&& !victim.getLastIpAddress().equalsIgnoreCase("not-set")) {
								PlayerPunishment.pcBan(serial);
								PlayerPunishment.ipBan(ip);
							}
							staff.getPacketSender()
									.sendMessage("You successfully mass banned '" + victimUsername + "'.");
						}
					}

					@Override
					public void queryError(SQLException e) {
						e.printStackTrace();
					}
				});
		return victim;
	}

	public static Player unmassBan(Player staff, String victimUsername, Player victim) {
		GameServer.getCharacterPool().executeQuery(
				"SELECT * FROM `accounts` WHERE username = '" + victimUsername + "' LIMIT 1", new SQLCallback() {
					@Override
					public void queryComplete(ResultSet rs) throws SQLException {
						if (rs.next()) {
							String json = rs.getString("json");
							PlayerLoading.decodeJson(victim, json);

							String serial = String.valueOf(victim.getLastMacAddress());
							String ip = victim.getLastIpAddress();
							PlayerPunishment.unBan(victimUsername);
							if (victim.getLastIpAddress() != null && !victim.getLastIpAddress().isEmpty()
									&& !victim.getLastIpAddress().equalsIgnoreCase("not-set")) {
								PlayerPunishment.unPcBan(serial);
								PlayerPunishment.unIpBan(ip);
							}
							staff.getPacketSender()
									.sendMessage("You successfully unmass banned '" + victimUsername + "'.");
						}
					}

					@Override
					public void queryError(SQLException e) {
						e.printStackTrace();
					}
				});
		return victim;
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

	public static void unVoteBan(String vote) {
		new File(VOTE_BAN_DIRECTORY + vote).delete();
	}

	public static void unPcBan(String add) {
		new File(PC_BAN_DIRECTORY + add).delete();
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
	 * Leads to directory where muted account files are stored.
	 */
	public static final String VOTE_BAN_DIRECTORY = PUNISHMENT_DIRECTORY + "/vote_bans/";

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

	/**
	 * Leads to directory where banned account files are stored.
	 */
	public static final String PC_BAN_DIRECTORY = PUNISHMENT_DIRECTORY + "/pc_bans/";

}
