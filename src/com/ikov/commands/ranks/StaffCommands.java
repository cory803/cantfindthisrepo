package com.ikov.commands.ranks;

import com.ikov.GameSettings;
import com.ikov.commands.CommandHandler;
import com.ikov.commands.StaffCommand;
import com.ikov.model.PlayerRights;
import com.ikov.model.Position;
import com.ikov.model.Locations.Location;
import com.ikov.util.Misc;
import com.ikov.world.World;
import com.ikov.world.content.PlayerLogs;
import com.ikov.world.content.transportation.TeleportHandler;
import com.ikov.world.content.transportation.TeleportType;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.world.entity.impl.player.PlayerSaving;

public class StaffCommands {

	public static void init() {
		CommandHandler.submit(new StaffCommand("staffzone") {
			@Override
			public boolean execute(Player player, String input) throws Exception {
				if (input.equals("all")) {
					for (Player players : World.getPlayers()) {
						if (players != null) {
							if (players.getRights().isStaff()) {
								TeleportHandler.teleportPlayer(players, new Position(2846, 5147), TeleportType.NORMAL);
							}
						}
					}
				} else {
					TeleportHandler.teleportPlayer(player, new Position(2846, 5147), TeleportType.NORMAL);
				}
				return true;
			}
		});
		CommandHandler.submit(new StaffCommand("kick") {

			@Override
			public boolean execute(Player player, String input) throws Exception {
				Player playerToKick = World.getPlayerByName(input);
				if (playerToKick != null) {
					if(playerToKick.getLocation() == Location.DUNGEONEERING) {
						player.getPacketSender().sendMessage("This player is in dung....");
						return false;
					}
					if (playerToKick.getLocation() == Location.DUEL_ARENA) {
						player.getPacketSender().sendMessage("You cannot do this to someone in duel arena.");
						return false;
					}
					if (playerToKick.getLocation() != Location.WILDERNESS) {
						World.deregister(playerToKick);
						player.getPacketSender().sendMessage("Kicked " + playerToKick.getUsername() + ".");
						PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " just kicked " + playerToKick.getUsername() + "!");
						return true;
					}
				} else {
					player.getPacketSender().sendMessage("Player not found.");
				}
				return false;
			}

		});
		CommandHandler.submit(new StaffCommand("saveall") {

			@Override
			public boolean execute(Player player, String input) throws Exception {

				World.savePlayers();
				player.getPacketSender().sendMessage("Saved players!");
				return true;
			}

		});
		CommandHandler.submit(new StaffCommand("movehome") {

			@Override
			public boolean execute(Player player, String input) throws Exception {
				String player2 = input;
				player2 = Misc.formatText(player2.replaceAll("_", " "));
				Player playerToMove = World.getPlayerByName(player2);
				if (playerToMove == null) {
					player.getPacketSender().sendMessage("Player not found.");
					return false;
				}
				if (player.getRights().equals(PlayerRights.SUPPORT) || player.getRights().equals(PlayerRights.MODERATOR) || player.getRights().equals(PlayerRights.GLOBAL_MOD)) {
					if (playerToMove.getUsername().equalsIgnoreCase(player2) && player.getLocation() == Location.WILDERNESS) {
						player.getPacketSender().sendMessage("You cannot move yourself out of the wild.");
						return false;
					}
					if (playerToMove.getLocation() == Location.DUNGEONEERING) {
						player.getPacketSender().sendMessage("You cannot move someone out of dung.");
						return false;
					}
					if (playerToMove.getLocation() == Location.DUEL_ARENA) {
						player.getPacketSender().sendMessage("You cannot do this to someone in duel arena.");
						return false;
					}
				}
				playerToMove.moveTo(GameSettings.DEFAULT_POSITION.copy());
				playerToMove.getPacketSender().sendMessage("You've been teleported home by " + player.getUsername() + ".");
				player.getPacketSender().sendMessage("Sucessfully moved " + playerToMove.getUsername() + " to home.");
				return true;
			}

		});
	}

}
