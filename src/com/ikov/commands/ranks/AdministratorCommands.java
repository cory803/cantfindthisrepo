package com.ikov.commands.ranks;

import com.ikov.commands.Command;
import com.ikov.commands.CommandHandler;
import com.ikov.model.PlayerRights;
import com.ikov.world.World;
import com.ikov.world.entity.impl.player.Player;

public class AdministratorCommands {

	public static void init() {
		CommandHandler.submit(new Command("globalyell", PlayerRights.ADMINISTRATOR) {

			@Override
			public boolean execute(Player player, String key, String input) throws Exception {
				player.getPacketSender().sendMessage("Retype the command to renable/disable the yell channel.");
				World.setGlobalYell(!World.isGlobalYell());
				World.sendMessage("<img=4> @blu@The yell channel has been @dre@" + (World.isGlobalYell() ? "@dre@enabled@blu@ again!" : "@dre@disabled@blu@ temporarily!"));
				return true;
			}

		});
		CommandHandler.submit(new Command("host", PlayerRights.ADMINISTRATOR) {

			@Override
			public boolean execute(Player player, String key, String input) throws Exception {
				String playerName = input;
				Player player2 = World.getPlayerByName(playerName);
				if (player2 != null) {
					player.getPacketSender().sendMessage("" + player2.getUsername() + " host IP: " + player2.getHostAddress() + ", serial number: " + player2.getSerialNumber());
				} else
					player.getPacketSender().sendMessage("Could not find player: " + playerName);
				return true;
			}

		});
	}

}
