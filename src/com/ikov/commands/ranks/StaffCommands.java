package com.ikov.commands.ranks;

import com.ikov.GameSettings;
import com.ikov.commands.CommandHandler;
import com.ikov.commands.StaffCommand;
import com.ikov.model.Flag;
import com.ikov.model.Locations.Location;
import com.ikov.model.PlayerRights;
import com.ikov.model.Position;
import com.ikov.model.Skill;
import com.ikov.util.Misc;
import com.ikov.world.World;
import com.ikov.world.content.PlayerPunishment;
import com.ikov.world.content.transportation.TeleportHandler;
import com.ikov.world.content.transportation.TeleportType;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.world.entity.impl.player.PlayerSaving;

public class StaffCommands {

  public static void init() {
    CommandHandler.submit(new StaffCommand("staffzone") {
      @Override
      public boolean execute(Player player, String key, String input) throws Exception {
        if (input.equals("all")) {
          for (Player players : World.getPlayers()) {
            if (players != null) {
              if (players.getRights().isStaff()) {
                TeleportHandler.teleportPlayer(players, new Position(2846, 5147),
                    TeleportType.NORMAL);
              }
            }
          }
        } else {
          TeleportHandler.teleportPlayer(player, new Position(2846, 5147), TeleportType.NORMAL);
        }
        return true;
      }
    });
    CommandHandler.submit(new StaffCommand("saveall") {

      @Override
      public boolean execute(Player player, String key, String input) throws Exception {

        World.savePlayers();
        player.getPacketSender().sendMessage("Saved players!");
        return true;
      }

    });
    CommandHandler.submit(new StaffCommand("movehome") {

      @Override
      public boolean execute(Player player, String key, String input) throws Exception {
        String player2 = input;
        player2 = Misc.formatText(player2.replaceAll("_", " "));
        Player playerToMove = World.getPlayerByName(player2);
        if (playerToMove == null) {
          player.getPacketSender().sendMessage("Player not found.");
          return false;
        }
        if (player.getRights().equals(PlayerRights.SUPPORT)
            || player.getRights().equals(PlayerRights.MODERATOR)
            || player.getRights().equals(PlayerRights.GLOBAL_MOD)) {
          if (playerToMove.getUsername().equalsIgnoreCase(player2)
              && player.getLocation() == Location.WILDERNESS) {
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
        playerToMove.getPacketSender()
            .sendMessage("You've been teleported home by " + player.getUsername() + ".");
        player.getPacketSender()
            .sendMessage("Sucessfully moved " + playerToMove.getUsername() + " to home.");
        return true;
      }

    });
    CommandHandler.submit(new StaffCommand("teleto") {

      @Override
      public boolean execute(Player player, String key, String input) throws Exception {
        String playerToTele = input;
        Player player2 = World.getPlayerByName(playerToTele);
        if (player2 == null) {
          player.getPacketSender().sendMessage("Player not found.");
          return false;
        } else {
          boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy())
              && player.getRegionInstance() == null && player2.getRegionInstance() == null;
          if (canTele && player.getLocation() != Location.DUNGEONEERING) {
            TeleportHandler.teleportPlayer(player, player2.getPosition().copy(),
                TeleportType.NORMAL);
            player.getPacketSender()
                .sendMessage("Teleporting to player: " + player2.getUsername() + "");
          } else {
            if (player2.getLocation() == Location.DUNGEONEERING) {
              player.getPacketSender()
                  .sendMessage("You can not teleport to this player while they are dungeoneering.");
            } else {
              player.getPacketSender().sendMessage(
                  "You can not teleport to this player at the moment. Minigame maybe?");
            }
          }
        }
        return true;
      }

    });
    CommandHandler.submit(new StaffCommand("massban") {

      @Override
      public boolean execute(Player player, String key, String input) throws Exception {
        String playerName = input;
        if (!PlayerSaving.playerExists(playerName)) {
          player.getPacketSender().sendMessage("Player " + playerName + " does not exist.");
          return false;
        } else {
          Player other = World.getPlayerByName(playerName);
          String mac;
          if (other == null) {
            mac = PlayerPunishment.getLastMacAddress(playerName);
          } else {
            mac = other.getMacAddress();
          }
          String ip;
          if (other == null) {
            ip = PlayerPunishment.getLastIpAddress(playerName);
          } else {
            ip = other.getHostAddress();
          }
          String address;
          if (other == null) {
            address = PlayerPunishment.getLastComputerAddress(playerName);
          } else {
            address = other.getComputerAddress();
          }
          PlayerPunishment.pcBan(address);
          PlayerPunishment.macBan(mac);
          PlayerPunishment.ipBan(ip);
          PlayerPunishment.ban(playerName);
          if (other != null) {
            World.deregister(other);
          }
          player.getPacketSender()
              .sendMessage("Player " + playerName + " was successfully mass banned!");
        }
        return true;
      }

      @Override
      public boolean meetsRequirements(Player player) {
        if (player.getRights().inherited(PlayerRights.MODERATOR) || player.isSpecialPlayer())
          return true;
        return false;
      }

    });
    CommandHandler.submit(new StaffCommand("unmassban") {

      @Override
      public boolean execute(Player player, String key, String input) throws Exception {
        String playerName = input;
        if (!PlayerSaving.playerExists(playerName)) {
          player.getPacketSender().sendMessage("Player " + playerName + " does not exist.");
          return false;
        } else {
          Player other = World.getPlayerByName(playerName);
          String mac;
          if (other == null) {
            mac = PlayerPunishment.getLastMacAddress(playerName);
          } else {
            mac = other.getMacAddress();
          }
          String ip;
          if (other == null) {
            ip = PlayerPunishment.getLastIpAddress(playerName);
          } else {
            ip = other.getHostAddress();
          }
          String address;
          if (other == null) {
            address = PlayerPunishment.getLastComputerAddress(playerName);
          } else {
            address = other.getComputerAddress();
          }
          PlayerPunishment.unPcBan(address);
          PlayerPunishment.unMacBan(mac);
          PlayerPunishment.unIpBan(ip);
          PlayerPunishment.unVoteBan(playerName);
          PlayerPunishment.unBan(playerName);
          player.getPacketSender()
              .sendMessage("Player " + playerName + " was successfully un mass banned!");
        }
        return true;
      }

      @Override
      public boolean meetsRequirements(Player player) {
        if (player.getRights().inherited(PlayerRights.MODERATOR) || player.isSpecialPlayer())
          return true;
        return false;
      }

    });
    CommandHandler.submit(new StaffCommand("getip") {

      @Override
      public boolean execute(Player player, String key, String input) throws Exception {
        String player_name = input;
        String last_ip = PlayerPunishment.getLastIpAddress(player_name);
        player.getPacketSender().sendMessage(player_name + "'s ip address is " + last_ip);
        return true;
      }

      @Override
      public boolean meetsRequirements(Player player) {
        return player.getRights().inherits(PlayerRights.ADMINISTRATOR) || player.isSpecialPlayer();
      }

    });
    CommandHandler.submit(new StaffCommand("hp") {

      @Override
      public boolean execute(Player player, String key, String input) throws Exception {
        player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 99999, true);
        return true;
      }

      @Override
      public boolean meetsRequirements(Player player) {
        return player.getRights().inherits(PlayerRights.ADMINISTRATOR) || player.isSpecialPlayer();
      }

    });
    CommandHandler.submit(new StaffCommand("toggleinvis") {

      @Override
      public boolean execute(Player player, String key, String input) throws Exception {
        player.setNpcTransformationId(player.getNpcTransformationId() > 0 ? -1 : 8254);
        player.getUpdateFlag().flag(Flag.APPEARANCE);
        return true;
      }

      @Override
      public boolean meetsRequirements(Player player) {
        return player.getRights().inherits(PlayerRights.ADMINISTRATOR) || player.isSpecialPlayer();
      }

    });
    CommandHandler.submit(new StaffCommand("giverights") {

      @Override
      public boolean execute(Player player, String key, String input) throws Exception {
        String[] command = input.split(" ");
        String rights = command[0];
        Player target = World.getPlayerByName(command[1]);
        if (target == null) {
          player.getPacketSender().sendMessage("This player is not online.");
          return false;
        }
        if (!player.getRights().equals(PlayerRights.OWNER)
            && target.getRights() != PlayerRights.MODERATOR
            && target.getRights() != PlayerRights.SUPPORT
            && target.getRights() != PlayerRights.PLAYER) {
          player.getPacketSender().sendMessage("You can't use this command on this person.");
          return false;
        }
        switch (rights) {
          case "demote":
          case "derank":
            target.setRights(PlayerRights.PLAYER);
            target.getPacketSender().sendMessage("You have been demoted...");
            target.getPacketSender().sendRights();
            break;
          case "ss":
          case "serversupport":
          case "support":
            target.setRights(PlayerRights.SUPPORT);
            target.getPacketSender().sendMessage("Your player rights has been changed to support.");
            target.getPacketSender().sendRights();
            break;
          case "mod":
          case "moderator":
            target.setRights(PlayerRights.MODERATOR);
            target.getPacketSender()
                .sendMessage("Your player rights has been changed to moderator.");
            target.getPacketSender().sendRights();
            break;
          default:
            player.getPacketSender().sendMessage("Command not found - Use ss, mod, admin or dev.");
        }
        return false;
      }

      @Override
      public boolean meetsRequirements(Player player) {
        return player.getRights().inherited(PlayerRights.ADMINISTRATOR);
      }

    });
    CommandHandler.submit(new StaffCommand("tele") {

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

      @Override
      public boolean meetsRequirements(Player player) {
        return player.getRights().inherits(PlayerRights.MODERATOR) || player.isSpecialPlayer();
      }

    });
    CommandHandler.submit(new StaffCommand("teletome") {

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
        boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy())
            && player.getRegionInstance() == null && player2.getRegionInstance() == null;
        if (canTele) {
          TeleportHandler.teleportPlayer(player2, player.getPosition().copy(), TeleportType.NORMAL);
          player.getPacketSender()
              .sendMessage("Teleporting player to you: " + player2.getUsername() + "");
          player2.getPacketSender()
              .sendMessage("You're being teleported to " + player.getUsername() + "...");
        } else
          player.getPacketSender().sendMessage(
              "You can not teleport that player at the moment. Maybe you or they are in a minigame?");
        return true;
      }

      @Override
      public boolean meetsRequirements(Player player) {
        return player.getRights().inherits(PlayerRights.MODERATOR) || player.isSpecialPlayer();
      }

    });
  }

}
