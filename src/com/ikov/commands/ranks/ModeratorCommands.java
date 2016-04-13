package com.ikov.commands.ranks;

import com.ikov.commands.CommandHandler;
import com.ikov.commands.ModeratorCommand;
import com.ikov.model.Locations.Location;
import com.ikov.model.Position;
import com.ikov.world.World;
import com.ikov.world.content.PlayerPunishment;
import com.ikov.world.content.transportation.TeleportHandler;
import com.ikov.world.content.transportation.TeleportType;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.world.entity.impl.player.PlayerSaving;

public class ModeratorCommands {

	public static void init() {
		CommandHandler.submit(new ModeratorCommand("tele") {

			@Override
			public boolean execute(Player player, String key, String input) throws Exception {
				String[] command = input.split(" ");
				int x = Integer.valueOf(command[0]), y = Integer.valueOf(command[1]);
				int z = player.getPosition().getZ();
				if (command.length > 2)
					z = Integer.valueOf(command[2]);
				Position position = new Position(x, y, z);
				player.moveTo(position);
				player.getPacketSender().sendMessage("Teleporting to " + position.toString());
				return true;
			}

		});
		CommandHandler.submit(new ModeratorCommand("teletome") {

			@Override
			public boolean execute(Player player, String key, String input) throws Exception {
				String playerName = input;
				Player player2 = World.getPlayerByName(playerName);
				if (player2 == null) {
					player.getPacketSender().sendMessage("Player " + playerName + " not found.");
					return false;
				}
				if (player2.getLocation() == Location.DUNGEONEERING) {
					player.getPacketSender().sendMessage("You cannot teleport a player out of dung.");
					return false;
				}
				if (player.getLocation() == Location.WILDERNESS) {
					player.getPacketSender().sendMessage("You cannot teleport a player into the wild.");
					return false;
				}
				if (player2.getLocation() == Location.DUEL_ARENA) {
					player.getPacketSender().sendMessage("You cannot do this to someone in duel arena.");
					return false;
				}
				boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy()) && player.getRegionInstance() == null && player2.getRegionInstance() == null;
				if (canTele) {
					TeleportHandler.teleportPlayer(player2, player.getPosition().copy(), TeleportType.NORMAL);
					player.getPacketSender().sendMessage("Teleporting player to you: " + player2.getUsername() + "");
					player2.getPacketSender().sendMessage("You're being teleported to " + player.getUsername() + "...");
				} else
					player.getPacketSender().sendMessage("You can not teleport that player at the moment. Maybe you or they are in a minigame?");
				return true;
			}

		});
		CommandHandler.submit(new ModeratorCommand("silenceyell") {

			@Override
			public boolean execute(Player player, String key, String input) throws Exception {
				String playerName = input;
				Player punishee = World.getPlayerByName(playerName);
				if (punishee != null) {
					punishee.setYellMute(true);
					punishee.getPacketSender().sendMessage("You have been yell muted! Please appeal on the forums.");
					player.getPacketSender().sendMessage("Player " + punishee.getUsername() + " was successfully muted!");
				} else {
					player.getPacketSender().sendMessage("Player " + playerName + " not found.");
					return false;
				}
				return true;
			}

		});
		CommandHandler.submit(new ModeratorCommand("unsilenceyell") {

			@Override
			public boolean execute(Player player, String key, String input) throws Exception {
				String playerName = input;
				Player punishee = World.getPlayerByName(playerName);
				if (punishee != null) {
					punishee.setYellMute(false);
					punishee.getPacketSender().sendMessage("You have been granted your yell ability again.");
					player.getPacketSender().sendMessage("Player " + playerName + " was successfully unmuted!");
				} else {
					player.getPacketSender().sendMessage("Player " + playerName + " not found.");
					return false;
				}
				return true;
			}

		});
		CommandHandler.submit(new ModeratorCommand("banvote") {

			@Override
			public boolean execute(Player player, String key, String input) throws Exception {
				String playerName = input;
				if (!PlayerSaving.playerExists(playerName)) {
					player.getPacketSender().sendMessage("Player " + playerName + " does not exist.");
					return false;
				} else {
					if (PlayerPunishment.isVoteBanned(playerName)) {
						player.getPacketSender().sendMessage("Player " + playerName + " already has an active vote ban.");
						return false;
					}
					Player other = World.getPlayerByName(playerName);
					PlayerPunishment.voteBan(playerName);
					other.getPacketSender().sendMessage("You have been banned from voting.");
					player.getPacketSender().sendMessage("You have banned " + other.getUsername() + " from voting.");
				}
				return true;
			}

		});
		CommandHandler.submit(new ModeratorCommand("unbanvote") {

			@Override
			public boolean execute(Player player, String key, String input) throws Exception {
				String playerName = input;
				if (!PlayerSaving.playerExists(playerName)) {
					player.getPacketSender().sendMessage("Player " + playerName + " does not exist.");
					return false;
				} else {
					if (!PlayerPunishment.isVoteBanned(playerName)) {
						player.getPacketSender().sendMessage("Player " + playerName + " is not vote banned.");
						return false;
					}
					Player other = World.getPlayerByName(playerName);
					PlayerPunishment.unVoteBan(playerName);
					other.getPacketSender().sendMessage("You have been unbanned from voting.");
					player.getPacketSender().sendMessage("You have unbanned " + other.getUsername() + " from voting.");
				}
				return true;
			}

		});
		CommandHandler.submit(new ModeratorCommand("ban") {

			@Override
			public boolean execute(Player player, String key, String input) throws Exception {
				String playerName = input;
				if (!PlayerSaving.playerExists(playerName)) {
					player.getPacketSender().sendMessage("Player " + playerName + " does not exist.");
					return false;
				} else {
					if (PlayerPunishment.isPlayerBanned(playerName)) {
						player.getPacketSender().sendMessage("Player " + playerName + " already has an active ban.");
						return false;
					}
					Player other = World.getPlayerByName(playerName);
					PlayerPunishment.ban(playerName);
					if (other != null) {
						World.deregister(other);
					}
					player.getPacketSender().sendMessage("Player " + playerName + " was successfully banned!");
				}
				return true;
			}

		});
		CommandHandler.submit(new ModeratorCommand("unban") {

			@Override
			public boolean execute(Player player, String key, String input) throws Exception {
				String playerName = input;
				if (!PlayerSaving.playerExists(playerName)) {
					player.getPacketSender().sendMessage("Player " + playerName + " does not exist.");
					return false;
				} else {
					if (!PlayerPunishment.isPlayerBanned(playerName)) {
						player.getPacketSender().sendMessage("Player " + playerName + " is not banned.");
						return false;
					}
					PlayerPunishment.unBan(playerName);
					player.getPacketSender().sendMessage("Player " + playerName + " was successfully unbanned.");
				}
				return true;
			}

		});
		CommandHandler.submit(new ModeratorCommand("ipmute") {

			@Override
			public boolean execute(Player player, String key, String input) throws Exception {
				String playerName = input;
				if (!PlayerSaving.playerExists(playerName)) {
					player.getPacketSender().sendMessage("Player " + playerName + " does not exist.");
					return false;
				} else {
					if (PlayerPunishment.isIpMuted(playerName)) {
						player.getPacketSender().sendMessage("Player " + playerName + " already has an active ip mute.");
						return false;
					}
					Player other = World.getPlayerByName(playerName);
					PlayerPunishment.ipMute(playerName);
					player.getPacketSender().sendMessage("Player " + playerName + " was successfully ip muted!");
					other.getPacketSender().sendMessage("You have been ip muted! Please appeal on the forums.");
				}
				return true;
			}

		});
		CommandHandler.submit(new ModeratorCommand("unipmute") {

			@Override
			public boolean execute(Player player, String key, String input) throws Exception {
				String playerName = input;
				if (!PlayerSaving.playerExists(playerName)) {
					player.getPacketSender().sendMessage("Player " + playerName + " does not exist.");
					return false;
				} else {
					if (!PlayerPunishment.isIpMuted(playerName)) {
						player.getPacketSender().sendMessage("Player " + playerName + " does not have an active ip mute!");
						return false;
					}
					Player other = World.getPlayerByName(playerName);
					PlayerPunishment.unIpMute(playerName);
					player.getPacketSender().sendMessage("Player " + playerName + " was successfully unipmuted!");
					other.getPacketSender().sendMessage("You have been unipmuted!");
				}
				return true;
			}

		});
		CommandHandler.submit(new ModeratorCommand("unmute") {

			@Override
			public boolean execute(Player player, String key, String input) throws Exception {
				String playerName = input;
				if (!PlayerSaving.playerExists(playerName)) {
					player.getPacketSender().sendMessage("Player " + playerName + " does not exist.");
					return false;
				} else {
					if (!PlayerPunishment.isMuted(playerName)) {
						player.getPacketSender().sendMessage("Player " + playerName + " is not muted.");
						return false;
					}
					Player other = World.getPlayerByName(playerName);
					PlayerPunishment.unMute(playerName);
					player.getPacketSender().sendMessage("Player " + playerName + " was successfully unmuted!");
					other.getPacketSender().sendMessage("You have been unmuted!");
				}
				return true;
			}

		});
		CommandHandler.submit(new ModeratorCommand("ipban") {

			@Override
			public boolean execute(Player player, String key, String input) throws Exception {
				String playerName = input;
				if (!PlayerSaving.playerExists(playerName)) {
					player.getPacketSender().sendMessage("Player " + playerName + " does not exist.");
					return false;
				} else {
					Player other = World.getPlayerByName(playerName);
					String ip;
					if (other == null) {
						ip = PlayerPunishment.getLastIpAddress(playerName);
					} else {
						ip = other.getHostAddress();
					}
					if (PlayerPunishment.isIpBanned(ip)) {
						player.getPacketSender().sendMessage("Player " + playerName + " already has an active ip ban on " + ip + ".");
						return false;
					}
					PlayerPunishment.ipBan(ip);
					PlayerPunishment.ban(playerName);
					if (other != null) {
						World.deregister(other);
					}
					player.getPacketSender().sendMessage("Player " + playerName + " was successfully banned on ip " + ip + "!");
				}
				return true;
			}

		});
		CommandHandler.submit(new ModeratorCommand("unipban") {

			@Override
			public boolean execute(Player player, String key, String input) throws Exception {
				String playerName = input;
				if (!PlayerSaving.playerExists(playerName)) {
					player.getPacketSender().sendMessage("Player " + playerName + " does not exist.");
					return false;
				} else {
					Player other = World.getPlayerByName(playerName);
					String ip;
					if (other == null) {
						ip = PlayerPunishment.getLastIpAddress(playerName);
					} else {
						ip = other.getHostAddress();
					}
					if (!PlayerPunishment.isIpBanned(ip)) {
						player.getPacketSender().sendMessage("Player " + playerName + " does not have an active ip ban on " + ip + ".");
						return false;
					}
					PlayerPunishment.unIpBan(ip);
					player.getPacketSender().sendMessage("Player " + playerName + " was successfully unipbanned on ip " + ip + "!");
				}
				return true;
			}

		});
	}

}
