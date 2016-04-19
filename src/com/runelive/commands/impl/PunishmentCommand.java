package com.runelive.commands.impl;

import com.runelive.commands.StaffCommand;
import com.runelive.model.Locations.Location;
import com.runelive.model.PlayerRights;
import com.runelive.model.Position;
import com.runelive.util.Misc;
import com.runelive.world.World;
import com.runelive.world.content.PlayerLogs;
import com.runelive.world.content.PlayerPunishment;
import com.runelive.world.entity.impl.player.Player;
import com.runelive.world.entity.impl.player.PlayerSaving;

public class PunishmentCommand extends StaffCommand {

  private PlayerRights rights;
  private boolean selfInherit;

  public PunishmentCommand(String name, PlayerRights rights) {
    super(name);
    this.rights = rights;
  }

  public PunishmentCommand(String name, PlayerRights rights, boolean selfInherit) {
    this(name, rights);
    this.selfInherit = selfInherit;
  }

  @Override
  public boolean execute(Player player, String key, String input) throws Exception {
    String playerName = input;
    if (!PlayerSaving.playerExists(playerName)) {
      player.getPacketSender().sendMessage("Player " + playerName + " does not exist.");
      return false;
    }
    Player punishee = World.getPlayerByName(playerName);
    switch (key) {
      case "kick":
        if (punishee != null) {
          if (punishee.getLocation() == Location.DUNGEONEERING) {
            player.getPacketSender().sendMessage("This player is in dung....");
            return false;
          }
          if (punishee.getLocation() == Location.DUEL_ARENA) {
            player.getPacketSender().sendMessage("You cannot do this to someone in duel arena.");
            return false;
          }
          if (punishee.getLocation() != Location.WILDERNESS) {
            World.deregister(punishee);
            player.getPacketSender().sendMessage("Kicked " + punishee.getUsername() + ".");
            PlayerLogs.log(player.getUsername(),
                "" + player.getUsername() + " just kicked " + punishee.getUsername() + "!");
          }
        } else {
          player.getPacketSender().sendMessage("Player not found.");
          return false;
        }
        break;
      case "jail":
        if (punishee != null) {
          int cellAmounts = Misc.inclusiveRandom(1, 10);
          switch (cellAmounts) {
            case 1:
              punishee.moveTo(new Position(1969, 5011, 0));
              break;
            case 2:
              punishee.moveTo(new Position(1969, 5008, 0));
              break;
            case 3:
              punishee.moveTo(new Position(1969, 5005, 0));
              break;
            case 4:
              punishee.moveTo(new Position(1969, 5002, 0));
              break;
            case 5:
              punishee.moveTo(new Position(1969, 4999, 0));
              break;
            case 6:
              punishee.moveTo(new Position(1980, 5011, 0));
              break;
            case 7:
              punishee.moveTo(new Position(1980, 5008, 0));
              break;
            case 8:
              punishee.moveTo(new Position(1980, 5005, 0));
              break;
            case 9:
              punishee.moveTo(new Position(1980, 5002, 0));
              break;
            case 10:
              punishee.moveTo(new Position(1980, 4999, 0));
              break;
          }
          punishee.setJailed(true);
          punishee.forceChat("Ahh shit... They put me in jail.");
          player.getPacketSender().sendMessage(
              "You have sent the player " + playerName + " to jail for breaking the rules.");
        } else {
          player.getPacketSender().sendMessage("Player " + playerName + " not found.");
          return false;
        }
        break;
      case "unjail":
        if (punishee != null) {
          if (punishee.isJailed()) {
            punishee.setJailed(false);
            punishee.forceChat("Im free!!! I'm finally out of jail... Hooray!");
            punishee.moveTo(new Position(3087, 3502, 0));
          } else {
            player.getPacketSender().sendMessage("Player " + playerName + " is not in jail.");
            return false;
          }
        } else {
          player.getPacketSender().sendMessage("Player " + playerName + " not found.");
          return false;
        }
        break;
      case "banvote":
        if (PlayerPunishment.isVoteBanned(playerName)) {
          player.getPacketSender()
              .sendMessage("Player " + playerName + " already has an active vote ban.");
          return false;
        }
        PlayerPunishment.voteBan(playerName);
        punishee.getPacketSender().sendMessage("You have been banned from voting.");
        player.getPacketSender()
            .sendMessage("You have banned " + punishee.getUsername() + " from voting.");
        break;
      case "unbanvote":
        if (!PlayerPunishment.isVoteBanned(playerName)) {
          player.getPacketSender().sendMessage("Player " + playerName + " is not vote banned.");
          return false;
        }
        PlayerPunishment.unVoteBan(playerName);
        punishee.getPacketSender().sendMessage("You have been unbanned from voting.");
        player.getPacketSender()
            .sendMessage("You have unbanned " + punishee.getUsername() + " from voting.");
        break;
      case "mute":
        if (PlayerPunishment.isMuted(playerName)) {
          player.getPacketSender()
              .sendMessage("Player " + playerName + " already has an active mute.");
          return false;
        }
        if (punishee != null)
          punishee.getPacketSender()
              .sendMessage("You have been muted! Please appeal on the forums.");
        PlayerPunishment.mute(playerName);
        player.getPacketSender().sendMessage("Player " + playerName + " was successfully muted!");
        break;
      case "unmute":
        if (!PlayerPunishment.isMuted(playerName)) {
          player.getPacketSender().sendMessage("Player " + playerName + " is not muted.");
          return false;
        }
        PlayerPunishment.unMute(playerName);
        player.getPacketSender().sendMessage("Player " + playerName + " was successfully unmuted!");
        punishee.getPacketSender().sendMessage("You have been unmuted!");
        break;
      case "ipmute":
        if (PlayerPunishment.isIpMuted(playerName)) {
          player.getPacketSender()
              .sendMessage("Player " + playerName + " already has an active ip mute.");
          return false;
        }
        PlayerPunishment.ipMute(playerName);
        player.getPacketSender()
            .sendMessage("Player " + playerName + " was successfully ip muted!");
        punishee.getPacketSender()
            .sendMessage("You have been ip muted! Please appeal on the forums.");
        break;
      case "unipmute":
        if (!PlayerPunishment.isIpMuted(playerName)) {
          player.getPacketSender()
              .sendMessage("Player " + playerName + " does not have an active ip mute!");
          return false;
        }
        PlayerPunishment.unIpMute(playerName);
        player.getPacketSender()
            .sendMessage("Player " + playerName + " was successfully unipmuted!");
        punishee.getPacketSender().sendMessage("You have been unipmuted!");
        break;
      case "ban":
        if (PlayerPunishment.isPlayerBanned(playerName)) {
          player.getPacketSender()
              .sendMessage("Player " + playerName + " already has an active ban.");
          return false;
        }
        PlayerPunishment.ban(playerName);
        if (punishee != null) {
          World.deregister(punishee);
        }
        player.getPacketSender().sendMessage("Player " + playerName + " was successfully banned!");
        break;
      case "unban":
        if (!PlayerPunishment.isPlayerBanned(playerName)) {
          player.getPacketSender().sendMessage("Player " + playerName + " is not banned.");
          return false;
        }
        PlayerPunishment.unBan(playerName);
        player.getPacketSender()
            .sendMessage("Player " + playerName + " was successfully unbanned.");
        break;
      case "ipban":
        if (punishee != null) {
          if (PlayerPunishment.isIpBanned(punishee.getHostAddress())) {
            player.getPacketSender().sendMessage("Player " + playerName
                + " already has an active ip ban on " + punishee.getHostAddress() + ".");
            return false;
          }
          PlayerPunishment.ipBan(punishee.getHostAddress());
          PlayerPunishment.ban(playerName);
          World.deregister(punishee);
          player.getPacketSender().sendMessage("Player " + playerName
              + " was successfully banned on ip " + punishee.getHostAddress() + "!");
        } else {
          player.getPacketSender().sendMessage("Player " + playerName + " not found.");
          return false;
        }
        break;
      case "unipban":
        if (punishee != null) {
          if (!PlayerPunishment.isIpBanned(punishee.getHostAddress())) {
            player.getPacketSender().sendMessage("Player " + playerName
                + " does not have an active ip ban on " + punishee.getHostAddress() + ".");
            return false;
          }
          PlayerPunishment.unIpBan(punishee.getHostAddress());
          player.getPacketSender().sendMessage("Player " + playerName
              + " was successfully unipbanned on ip " + punishee.getHostAddress() + "!");
        } else {
          player.getPacketSender().sendMessage("Player " + playerName + " not found.");
          return false;
        }
        break;
      case "silenceyell":
        if (punishee != null) {
          punishee.setYellMute(true);
          punishee.getPacketSender()
              .sendMessage("You have been yell muted! Please appeal on the forums.");
          player.getPacketSender()
              .sendMessage("Player " + punishee.getUsername() + " was successfully muted!");
        } else {
          player.getPacketSender().sendMessage("Player " + playerName + " not found.");
          return false;
        }
        break;
      case "unsilenceyell":
        if (punishee != null) {
          punishee.setYellMute(false);
          punishee.getPacketSender().sendMessage("You have been granted your yell ability again.");
          player.getPacketSender()
              .sendMessage("Player " + playerName + " was successfully unmuted!");
        } else {
          player.getPacketSender().sendMessage("Player " + playerName + " not found.");
          return false;
        }
        break;

      case "serialban":
      case "macban":
      case "cpuban":
        if (punishee != null) {
          if (PlayerPunishment.isMacBanned(punishee.getMacAddress())) {
            player.getPacketSender().sendMessage("Player " + playerName
                + " already has an active mac ban on " + punishee.getMacAddress() + ".");
            return false;
          }
          PlayerPunishment.macBan(punishee.getMacAddress());
          World.deregister(punishee);
        } else {
          player.getPacketSender().sendMessage("Player " + playerName + " not found.");
          return false;
        }
        break;

      case "unserialban":
      case "unmacban":
      case "uncpuban":
        if (punishee != null) {
          if (!PlayerPunishment.isMacBanned(punishee.getMacAddress())) {
            player.getPacketSender().sendMessage("Player " + playerName
                + " does not have an active mac ban on " + punishee.getMacAddress() + ".");
            return false;
          }
          PlayerPunishment.unMacBan(punishee.getMacAddress());
          player.getPacketSender().sendMessage("Player " + playerName
              + " was successfully un mac banned on mac " + punishee.getMacAddress() + "!");
        } else {
          player.getPacketSender().sendMessage("Player " + playerName + " not found.");
          return false;
        }
        break;
    }
    return true;
  }

  @Override
  public boolean meetsRequirements(Player player) {
    if (selfInherit && player.getRights().inherited(rights) || player.isSpecialPlayer())
      return true;
    if (player.getRights().inherits(rights) || player.isSpecialPlayer())
      return true;
    return false;
  }
}
