package com.runelive.world.content;

import java.io.*;
import java.nio.file.Files;
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

	private static boolean readPunished(File f) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = br.readLine();
			br.close();
			if (line == null) {
				return true;
			} else {
				Long time = Long.parseLong(line);
				if (System.currentTimeMillis() - time > 0) {
					return true;
				} else {
					f.delete();
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}

	private static void checkPunishment(File f, Long time) {
		try {
			f.createNewFile();
			if (time != -1) {
				writePunished(f, time);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writePunished(File f, Long time) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(time.toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static boolean isPlayerBanned(String name) {
		File f = new File(PLAYER_BAN_DIRECTORY + name.toLowerCase());
		return f.exists() && readPunished(f);
	}

	public static boolean isIpBanned(String ip) {
		File f = new File(IP_BAN_DIRECTORY + ip.toLowerCase());
		return f.exists() && readPunished(f);
	}

	public static boolean isPcBanned(String add) {
		File f = new File(PC_BAN_DIRECTORY + add.toLowerCase());
		return f.exists() && readPunished(f);
	}

	public static boolean isIpMuted(String ip) {
		File f = new File(IP_MUTE_DIRECTORY + ip.toLowerCase());
		return f.exists() && readPunished(f);
	}

	public static boolean isMuted(String name) {
		File f = new File(PLAYER_MUTE_DIRECTORY + name.toLowerCase());
		return f.exists() && readPunished(f);
	}

	public static boolean isVoteBanned(String name) {
		File f = new File(VOTE_BAN_DIRECTORY + name.toLowerCase());
		return f.exists() && readPunished(f);
	}

	public static void ipBan(String ip, Long time) {
		File f = new File(IP_BAN_DIRECTORY + ip);
		checkPunishment(f, time);
	}

	public static void voteBan(String vote, Long time) {
		File f = new File(VOTE_BAN_DIRECTORY + vote);
		checkPunishment(f, time);
	}


	public static void pcBan(String add, Long time) {
		if (add.toLowerCase().equals("none")) {
			return;
		}
		File f = new File(PC_BAN_DIRECTORY + add);
		checkPunishment(f, time);
	}

	public static void ban(String name, Long time) {
		File f = new File(PLAYER_BAN_DIRECTORY + name);
		checkPunishment(f, time);
	}

	public static void ipMute(String ip, Long time) {
		File f = new File(IP_MUTE_DIRECTORY + ip);
		checkPunishment(f, time);
	}

	public static void massBan(Player staff, String victimUsername, Long time) {
		Player victim = new Player(null);
		GameServer.getCharacterPool().executeQuery(
				"SELECT * FROM `accounts` WHERE username = '" + victimUsername + "' LIMIT 1", new SQLCallback() {
					@Override
					public void queryComplete(ResultSet rs) throws SQLException {
						if (rs.next()) {
							String json = rs.getString("json");
							PlayerLoading.decodeJson(victim, json);

							String serial = String.valueOf(victim.getLastMacAddress());
							String ip = victim.getLastIpAddress();
							PlayerPunishment.ban(victimUsername, time);
							if (victim.getLastIpAddress() != null && !victim.getLastIpAddress().isEmpty()
									&& !victim.getLastIpAddress().equalsIgnoreCase("not-set")) {
								PlayerPunishment.pcBan(serial, time);
								PlayerPunishment.ipBan(ip, time);
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
	}

	public static Player unmassBan(Player staff, String victimUsername) {
		Player victim = new Player(null);
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

	public static void mute(String name, Long time) {
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
