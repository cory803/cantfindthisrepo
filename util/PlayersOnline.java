package com.ikov.util;

import java.sql.*;

import com.ikov.world.World;
import com.ikov.world.entity.impl.player.Player;

public class PlayersOnline {

	public static Connection con = null;
	public static Statement stm;

	public static void createCon() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://162.252.9.87/ikovorg_online", "ikovorg_online", "6*Z6qGRee4Jt");
			stm = con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ResultSet query(String s) throws SQLException {
		try {
			if (s.toLowerCase().startsWith("select")) {
				ResultSet rs = stm.executeQuery(s);
				return rs;
			} else {
				stm.executeUpdate(s);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void destroyCon() {
		try {
			stm.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	public static boolean offline(Player c) {
		try {
			query("DELETE FROM `online` WHERE id = 1;");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean online(Player c) {
		try {
			query("INSERT INTO `online` (id, currentlyonline) VALUES('1','"+World.getPlayers().size()+"');");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}