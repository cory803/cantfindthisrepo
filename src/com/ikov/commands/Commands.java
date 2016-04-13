package com.ikov.commands;

import com.ikov.commands.ranks.CommunityManagers;
import com.ikov.commands.ranks.Members;
import com.ikov.commands.ranks.Owners;
import com.ikov.commands.ranks.SpecialPlayers;
import com.ikov.commands.ranks.WikiManagers;
import com.ikov.model.PlayerRights;
import com.ikov.world.content.PlayerLogs;
import com.ikov.world.entity.impl.player.Player;

/**
 * Initiates a command for each rank/file.
 * 
 * @author Jonathan Sirens
 */

public class Commands {

  public static void initiate_commands(Player player, String[] parts, String whole_command) {
    PlayerLogs.log(player.getUsername(),
        "" + player.getUsername() + " has done command " + whole_command + "");

    // Regular Commands
    Members.initiate_command(player, parts, whole_command);
    SpecialPlayers.initiate_command(player, parts, whole_command);

    if (player.getUsername().equalsIgnoreCase("manny")) {
      CommunityManagers.initiate_command(player, parts, whole_command);
      return;
    }
    // Staff Members
    if (player.getRights() == PlayerRights.OWNER) {
      Owners.initiate_command(player, parts, whole_command);
    }
    if (player.getRights() == PlayerRights.COMMUNITY_MANAGER) {
      CommunityManagers.initiate_command(player, parts, whole_command);
    }
    if (player.getRights() == PlayerRights.WIKI_MANAGER) {
      WikiManagers.initiate_command(player, parts, whole_command);
    }

  }
}
