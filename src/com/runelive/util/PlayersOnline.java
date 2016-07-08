package com.runelive.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import com.runelive.GameServer;
import com.runelive.world.World;

public class PlayersOnline {

	private static final File FILE_LOCATION = new File("C:/inetpub/wwwroot/online.txt");

	public static void update() {
		GameServer.getLoader().getEngine().submit(() -> {
			try {
				PrintWriter printer = new PrintWriter(FILE_LOCATION);
				printer.print("" + World.getPlayers().size() + "");
				printer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

}
