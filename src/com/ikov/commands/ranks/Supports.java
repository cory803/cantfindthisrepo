package com.ikov.commands.ranks;

import com.ikov.GameSettings;
import com.ikov.model.Locations.Location;
import com.ikov.model.Position;
import com.ikov.util.Misc;
import com.ikov.world.World;
import com.ikov.world.content.PlayerLogs;
import com.ikov.world.content.PlayerPunishment;
import com.ikov.world.content.transportation.TeleportHandler;
import com.ikov.world.content.transportation.TeleportType;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.world.entity.impl.player.PlayerSaving;

public class Supports {
	
	/**
	* @Author Jonathan Sirens
	* Initiates Command
	**/
	
	public static void initiate_command(final Player player, String[] command, String wholeCommand) {
		if(wholeCommand.startsWith("jail")) {
				String jail_punishee = wholeCommand.substring(5);
				Player punishee = World.getPlayerByName(jail_punishee);
				if(!PlayerSaving.playerExists(jail_punishee)) {
					player.getPacketSender().sendMessage("Player "+jail_punishee+" does not exist.");
					return;
				} else {
				int cellAmounts = Misc.getRandom(1);
				switch(cellAmounts) {
				case 1:
					punishee.setJailed(true);
					punishee.forceChat("Ahh shit... They put me in jail.");
					punishee.moveTo(new Position(1969, 5011, 0));
					player.getPacketSender().sendMessage("You have sent the player "+jail_punishee+" to jail for breaking the rules.");
				break;
				case 2:
					punishee.setJailed(true);
					punishee.forceChat("Ahh shit... They put me in jail.");
					punishee.moveTo(new Position(1969, 5008, 0));
					player.getPacketSender().sendMessage("You have sent the player "+jail_punishee+" to jail for breaking the rules.");
				break;
				case 3:
					punishee.setJailed(true);
					punishee.forceChat("Ahh shit... They put me in jail.");
					punishee.moveTo(new Position(1969, 5005, 0));
					player.getPacketSender().sendMessage("You have sent the player "+jail_punishee+" to jail for breaking the rules.");
				break;
				case 4:
					punishee.setJailed(true);
					punishee.forceChat("Ahh shit... They put me in jail.");
					punishee.moveTo(new Position(1969, 5002, 0));
					player.getPacketSender().sendMessage("You have sent the player "+jail_punishee+" to jail for breaking the rules.");
				break;
				case 5:
					punishee.setJailed(true);
					punishee.forceChat("Ahh shit... They put me in jail.");
					punishee.moveTo(new Position(1969, 4999, 0));
					player.getPacketSender().sendMessage("You have sent the player "+jail_punishee+" to jail for breaking the rules.");
				break;
				case 6:
					punishee.setJailed(true);
					punishee.forceChat("Ahh shit... They put me in jail.");
					punishee.moveTo(new Position(1980, 5011, 0));
					player.getPacketSender().sendMessage("You have sent the player "+jail_punishee+" to jail for breaking the rules.");
				break;
				case 7:
					punishee.setJailed(true);
					punishee.forceChat("Ahh shit... They put me in jail.");
					punishee.moveTo(new Position(1980, 5008, 0));
					player.getPacketSender().sendMessage("You have sent the player "+jail_punishee+" to jail for breaking the rules.");
				break;
				case 8:
					punishee.setJailed(true);
					punishee.forceChat("Ahh shit... They put me in jail.");
					punishee.moveTo(new Position(1980, 5005, 0));
					player.getPacketSender().sendMessage("You have sent the player "+jail_punishee+" to jail for breaking the rules.");
				break;
				case 9:
					punishee.setJailed(true);
					punishee.forceChat("Ahh shit... They put me in jail.");
					punishee.moveTo(new Position(1980, 5002, 0));
					player.getPacketSender().sendMessage("You have sent the player "+jail_punishee+" to jail for breaking the rules.");
				break;
				case 10:
					punishee.setJailed(true);
					punishee.forceChat("Ahh shit... They put me in jail.");
					punishee.moveTo(new Position(1980, 4999, 0));
					player.getPacketSender().sendMessage("You have sent the player "+jail_punishee+" to jail for breaking the rules.");
				break;
				default:
				}
			}
		}
		if(command[0].equalsIgnoreCase("mute")) {
			String mute_player = wholeCommand.substring(5);
			if(!PlayerSaving.playerExists(mute_player)) {
				player.getPacketSender().sendMessage("Player "+mute_player+" does not exist.");
				return;
			} else {
				if(PlayerPunishment.isMuted(mute_player)) {
					player.getPacketSender().sendMessage("Player "+mute_player+" already has an active mute.");
					return;
				}
				Player other = World.getPlayerByName(mute_player);
				PlayerPunishment.mute(mute_player);
				player.getPacketSender().sendMessage("Player "+mute_player+" was successfully muted!");
				other.getPacketSender().sendMessage("You have been muted! Please appeal on the forums.");
			}
		}
		if(command[0].equalsIgnoreCase("teleto")) {
			String playerToTele = wholeCommand.substring(7);
			Player player2 = World.getPlayerByName(playerToTele);
			if(player2 == null) {
				player.getPacketSender().sendMessage("Cannot find that player online..");
				return;
			} else {
				boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy()) && player.getRegionInstance() == null && player2.getRegionInstance() == null;
				if(canTele && player.getLocation() != Location.DUNGEONEERING) {
					TeleportHandler.teleportPlayer(player, player2.getPosition().copy(), TeleportType.NORMAL);
					player.getPacketSender().sendMessage("Teleporting to player: "+player2.getUsername()+"");
				} else {
					if(player2.getLocation() == Location.DUNGEONEERING) {
						player.getPacketSender().sendMessage("You can not teleport to this player while they are dungeoneering.");
					} else {
						player.getPacketSender().sendMessage("You can not teleport to this player at the moment. Minigame maybe?");
					}
				}
			}
		}

	}
	
}