package com.ikov.util;

import java.sql.*;
import java.util.Properties;

/**
 * Initiates UPDATE and INSERT values towards
 * the forum to update users, and donator ranks based off
 * in-game features.
 * 
 * @author Jonathan Sirens
 */

public class ForumDatabase {

	//Database connection information
	private static final String database_name = "ikov2_forum";
	private static final String mysql_address = "192.99.148.171";
	private static final String username = "ikov2_root";
	private static final String password = "c-W-U,dL=UHQ";
	static final String mysql_port = "3306";
	
	//Ranks
	public static int regular_donator = 11;
	public static int super_donator = 31;
	public static int extreme_donator = 30;
	public static int legendary_donator = 32;
	public static int uber_donator = 12;
	
	public static int members = 3;
	public static int validating = 1;
	public static int banned = 5;

	//Misc information (disregard)
	private static final Properties prop;
	public static Connection connection = null;

	static {
		prop = new Properties();
		prop.put("user", username);
		prop.put("password", password);
	}


	public static synchronized void connect() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://" + mysql_address + ":" + mysql_port + "/" + database_name, username, password);
	}

	public static void destroy_connection() {
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized Connection getConnection() {
		try {
			if (connection == null || connection.isClosed()) {
				connection = DriverManager.getConnection("jdbc:mysql://" + mysql_address + ":"
			+ mysql_port + "/" + database_name, username, password);
			}
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		}
		return connection;
	}
	
	public static boolean check_has_username(String username) {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT `name` FROM `members` WHERE `name` = '"+username+"'";
            ResultSet results = statement.executeQuery(query);
			while(results.next()) {
				String resulted = results.getString("name");
				if(resulted.toLowerCase().equals(username.toLowerCase())) {
					return true;
				}
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
		return false;
	}	
	
	public static int getCurrentMemberID(String username) {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT `member_group_id` FROM `members` WHERE `name` = '"+username+"'";
            ResultSet results = statement.executeQuery(query);
			while(results.next()) {
				int resulted = results.getInt("member_group_id");
				if(resulted >= 1) {
					return resulted;
				}
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
		return -1;
	}

	public static synchronized void update_donator_rank(String username, int donator_rank) {
		if(donator_rank == 1) {
			donator_rank = regular_donator;
		} else if(donator_rank == 2) {
			donator_rank = super_donator;
		} else if(donator_rank == 3) {
			donator_rank = extreme_donator;
		} else if(donator_rank == 4) {
			donator_rank = legendary_donator;
		} else if(donator_rank == 5) {
			donator_rank = uber_donator;
		} else if(donator_rank == 0) {
			donator_rank = members;
		}
		try {
			PreparedStatement statement = getConnection().prepareStatement("UPDATE `members` SET `member_group_id` = '"+donator_rank+"' WHERE `name` = '"+username+"'");
			statement.execute();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
}