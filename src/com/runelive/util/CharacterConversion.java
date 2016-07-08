package com.runelive.util;

import java.io.File;
import java.io.FilenameFilter;

import org.jboss.netty.channel.Channel;

import com.runelive.net.PlayerSession;
import com.runelive.world.entity.impl.player.Player;
import com.runelive.world.entity.impl.player.PlayerLoading;
import com.runelive.world.entity.impl.player.PlayerSaving;

public class CharacterConversion {

	public static void convert(Channel channel) {

		File[] files = new File("characters").listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".json");
			}
		});

		for (File file : files) {
			try {
				export(file.getName().replaceAll(".json", ""), channel);
			} catch (Throwable e) {
				e.printStackTrace();
				continue;
			}
		}

	}

	public static void export(String name, Channel channel) throws Throwable {

		System.out.println("Converting: " + name);
		PlayerSession session = new PlayerSession(channel);
		Player player = new Player(session).setUsername(name).setLongUsername(NameUtils.stringToLong(name))
				.setPassword("default_pw").setHostAddress("host").setComputerAddress("pcaddress");
		int result = PlayerLoading.loadJSON(player);
		System.out.println("" + player.getPassword());
		PlayerSaving.createNewAccount(player);
		PlayerSaving.saveGame(player);
	}

}