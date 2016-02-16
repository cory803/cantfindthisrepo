package com.ikov.util;

import java.sql.*;

import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import com.ikov.GameServer;
import com.ikov.world.World;
import com.ikov.world.entity.impl.player.Player;

public class PlayersOnline {

	private static final File FILE_LOCATION = new File("C:/inetpub/wwwroot/online.txt");
	
	public static void update() {
		GameServer.getLoader().getEngine().submit(() -> {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_LOCATION, true));
				PrintWriter printer = new PrintWriter(FILE_LOCATION);
				printer.print(""+World.getPlayers().size()+"");
				printer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
}