package com.strattus.util;

import java.sql.*;
import java.util.Properties;

import com.strattus.world.entity.impl.player.Player;


public class Auth {

	public static Connection con = null;
	public static Statement stm;
	private static final String DB = "spawnsca_votet";
	private static final String URL = "198.105.215.36";
	private static final String USER = "spawnsca_votet";
	private static final String PASS = ")~*Unq2;oS)H";
	static final String PORT = "3306";

	private static final Properties prop;
	static {
		prop = new Properties();
		prop.put("user", USER);
		prop.put("password", PASS);
		// prop.put("autoReconnect", "true");
		// prop.put("maxReconnects", "4");
	}

	public static Connection conn = null;

	public static synchronized void connect() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://" + URL + ":"
			+ PORT + "/" + DB, USER, PASS);
		// System.out.println("Players Online Handler: Success");
	}

	public static void destroyCon() {
		try {
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized Connection getConnection() {
		try {
			if (conn == null || conn.isClosed()) {
				conn = DriverManager.getConnection("jdbc:mysql://" + URL + ":"
			+ PORT + "/" + DB, USER, PASS);
			}
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		}
		return conn;
	}
	/* Start Vote Handler */

	public static synchronized void giveItems(Player player) {
	}

	public static synchronized boolean checkVote(String auth) {
		try {
			PreparedStatement statement = getConnection().prepareStatement("SELECT `authcode` FROM `votes` WHERE `authcode`= ? AND `used` =  0");
			statement.setString(1, auth);
			ResultSet res = statement.executeQuery();
			
			if (res.next())
				return true;
			else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static synchronized void updateVote(String auth) {
		try {
			PreparedStatement statement = getConnection().prepareStatement("UPDATE `votes` SET `used` = 1 WHERE `authcode` = ?");
			statement.setString(1, auth);
			statement.execute();
			
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

	/* End Vote Handler */
	
}