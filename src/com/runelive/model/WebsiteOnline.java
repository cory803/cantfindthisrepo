package com.runelive.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import com.runelive.GameServer;
import com.runelive.net.mysql.ThreadedSQLCallback;

public class WebsiteOnline {

	public static void updateOnline(int amountOnline) {
		GameServer.getCharacterPool().executeQuery("UPDATE `online` SET `amount`="+amountOnline+" WHERE 1", new ThreadedSQLCallback() {
			@Override
			public void queryComplete(ResultSet rs) throws SQLException {
				//Query is complete
			}

			@Override
			public void queryError(SQLException e) {
				e.printStackTrace();
			}
		});
	}
}