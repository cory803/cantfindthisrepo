package com.ikov.util;

import java.sql.*;
import java.util.Properties;

/**
 * Initiates claiming a auth code.
 * 
 * @author High105/Tanner
 */

public class Auth {

	public static Connection con = null;
	public static Statement stm;
	private static final String DB = "ikov2_vote";
	private static final String URL = "192.99.148.171";
	private static final String USER = "ikov2_root";
	private static final String PASS = "c-W-U,dL=UHQ";
	static final String PORT = "3306";

	private static final Properties prop;

	static {
		prop = new Properties();
		prop.put("user", USER);
		prop.put("password", PASS);
	}

	public static Connection conn = null;

	public static synchronized void connect() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://" + URL + ":" + PORT + "/" + DB, USER, PASS);
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
	
}