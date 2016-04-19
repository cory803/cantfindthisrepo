package com.runelive.commands.ranks;

import java.util.concurrent.TimeUnit;

import com.runelive.GameSettings;
import com.runelive.model.Locations.Location;
import com.runelive.model.Position;
import com.runelive.model.Store;
import com.runelive.util.ForumDatabase;
import com.runelive.util.Misc;
import com.runelive.world.content.Command;
import com.runelive.world.content.PlayersOnlineInterface;
import com.runelive.world.content.clan.ClanChatManager;
import com.runelive.world.content.combat.CombatFactory;
import com.runelive.world.content.combat.DesolaceFormulas;
import com.runelive.world.content.dialogue.DialogueManager;
import com.runelive.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.runelive.world.content.transportation.TeleportHandler;
import com.runelive.world.entity.impl.player.Player;

public class Members {

  /**
   * @Author Jonathan Sirens Initiates Command
   **/

  public static void initiate_command(final Player player, String[] command, String wholeCommand) {
    if (!player.getRights().isStaff() && player.isJailed()) {
      player.getPacketSender().sendMessage("You cannot use commands in jail... You're in jail.");
      return;
    }
    if (command[0].equalsIgnoreCase("time")) {
      player.forceChat("[RUNELIVE] " + player.getUsername() + " has played for ["
          + Misc.getHoursPlayed((player.getTotalPlayTime() + player.getRecordedLogin().elapsed()))
          + "]");
    }
    if (command[0].equalsIgnoreCase("mb")) {
      if (Dungeoneering.doingDungeoneering(player)) {
        player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
        return;
      }
      if (player.getLocation() != null && player.getWildernessLevel() > 20) {
        player.getPacketSender().sendMessage("You cannot do this at the moment.");
        return;
      }
      Position position = new Position(2539, 4715, 0);
      TeleportHandler.teleportPlayer(player, position, player.getSpellbook().getTeleportType());
      player.getPacketSender().sendMessage("Teleporting you to mage bank!");
    }
    if (command[0].equalsIgnoreCase("wests")) {
      if (Dungeoneering.doingDungeoneering(player)) {
        player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
        return;
      }
      if (player.getLocation() != null && player.getWildernessLevel() > 20) {
        player.getPacketSender().sendMessage("You cannot do this at the moment.");
        return;
      }
      DialogueManager.start(player, 212);
      player.setDialogueActionId(212);
    }
    if (command[0].equalsIgnoreCase("easts")) {
      if (Dungeoneering.doingDungeoneering(player)) {
        player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
        return;
      }
      if (player.getLocation() != null && player.getWildernessLevel() > 20) {
        player.getPacketSender().sendMessage("You cannot do this at the moment.");
        return;
      }
      DialogueManager.start(player, 213);
      player.setDialogueActionId(213);
    }
    if (command[0].equalsIgnoreCase("commands")) {
      if (player.getLocation() == Location.DUNGEONEERING) {
        player.getPacketSender().sendMessage("You cannot open the commands in dungeoneering.");
        return;
      }
      player.getPacketSender().sendTab(GameSettings.QUESTS_TAB);
      Command.open(player);
    }
    if (command[0].equals("stuck")) {
      if (player.getTeleblockTimer() > 0) {
        player.getPacketSender()
            .sendMessage("You cannot teleport with this command while teleblocked.");
        return;
      }
      if (player.getCombatBuilder().isBeingAttacked() || player.getCombatBuilder().isAttacking()) {
        player.getPacketSender().sendMessage("You cannot use this command while in combat!");
        return;
      }
      if (!player.getStuckDelay().elapsed(300000)) {
        player.getPacketSender().sendMessage(
            "You have not waited the entire 5 minutes to be able to use this command again.");
        return;
      }
      boolean route_found = false;
      Position[] obstacle_pipe = {new Position(3004, 3938, 0), new Position(3004, 3939, 0),
          new Position(3004, 3940, 0), new Position(3004, 3941, 0), new Position(3004, 3942, 0),
          new Position(3004, 3943, 0), new Position(3004, 3944, 0), new Position(3004, 3945, 0),
          new Position(3004, 3946, 0), new Position(3004, 3947, 0), new Position(3004, 3948, 0),
          new Position(3004, 3949, 0), new Position(3003, 3948, 0), new Position(3003, 3947, 0),
          new Position(3003, 3946, 0), new Position(3003, 3945, 0), new Position(3003, 3944, 0),
          new Position(3003, 3943, 0), new Position(3003, 3942, 0), new Position(3003, 3941, 0),
          new Position(3003, 3940, 0), new Position(3003, 3939, 0), new Position(3005, 3939, 0),
          new Position(3005, 3940, 0), new Position(3005, 3941, 0), new Position(3005, 3942, 0),
          new Position(3005, 3943, 0), new Position(3005, 3944, 0), new Position(3005, 3945, 0),
          new Position(3005, 3946, 0), new Position(3005, 3947, 0), new Position(3005, 3948, 0)};
      for (Position p : obstacle_pipe) {
        if (p.getX() == player.getPosition().getX() && p.getY() == player.getPosition().getY()) {
          player.getStuckDelay().reset();
          route_found = true;
          player.moveTo(new Position(3004, 3937, 0));
          player.getPacketSender().sendMessage("You have been moved outside of the obstacle pipe!");
        }
      }
      Position[] ropeswing = {new Position(3006, 3954, 0), new Position(3005, 3954, 0),
          new Position(3006, 3955, 0), new Position(3005, 3955, 0), new Position(3006, 3956, 0),
          new Position(3005, 3956, 0), new Position(3006, 3957, 0), new Position(3005, 3957, 0),
          new Position(3004, 3954, 0), new Position(3004, 3955, 0), new Position(3004, 3956, 0),
          new Position(3004, 3957, 0)};
      for (Position p : ropeswing) {
        if (p.getX() == player.getPosition().getX() && p.getY() == player.getPosition().getY()) {
          player.getStuckDelay().reset();
          route_found = true;
          player.moveTo(new Position(3005, 3953, 0));
          player.getPacketSender().sendMessage("You have been moved outside of the rope swing!");
        }
      }
      Position[] strange_floor = {new Position(2997, 3960, 0), new Position(2998, 3960, 0),
          new Position(2999, 3960, 0), new Position(3000, 3960, 0), new Position(3001, 3960, 0)};
      for (Position p : strange_floor) {
        if (p.getX() == player.getPosition().getX() && p.getY() == player.getPosition().getY()) {
          player.getStuckDelay().reset();
          route_found = true;
          player.moveTo(new Position(3002, 3960, 0));
          player.getPacketSender().sendMessage("You have been moved outside of the strange floor!");
        }
      }
      Position[] log_balance = {new Position(3002, 3945, 0), new Position(3001, 3945, 0),
          new Position(3000, 3945, 0), new Position(2999, 3945, 0), new Position(2998, 3945, 0),
          new Position(2997, 3945, 0), new Position(2996, 3945, 0), new Position(2995, 3945, 0),};
      for (Position p : strange_floor) {
        if (p.getX() == player.getPosition().getX() && p.getY() == player.getPosition().getY()) {
          player.getStuckDelay().reset();
          route_found = true;
          player.moveTo(new Position(3002, 3945, 0));
          player.getPacketSender().sendMessage("You have been moved outside of the log balance!");
        }
      }
      if (!route_found)
        player.getPacketSender()
            .sendMessage("We have been unable to find a stuck tile for you to move off of!");
    }
    if (command[0].equals("skull")) {
      if (player.getSkullTimer() > 0) {
        player.getPacketSender().sendMessage("You are already skulled!");
        return;
      } else {
        CombatFactory.skullPlayer(player);
      }
    }
    if (command[0].equalsIgnoreCase("auth")) {
      player.getPacketSender()
          .sendMessage("Voting has been disabled until we move to a new system.");
      player.getPacketSender().sendMessage(
          "We have confirmed the lag and mass disconnections is due to our old vote system.");
    }
    if (command[0].equals("forumrank")) {
      if (!player.getForumDelay().elapsed(30000)) {
        player.getPacketSender()
            .sendMessage("You must wait another "
                + Misc.getTimeLeft(player.getLastAuthTime().getTime(), 30, TimeUnit.SECONDS)
                + " seconds before attempting this.");
        return;
      }
      if (!GameSettings.FORUM_DATABASE_CONNECTIONS) {
        player.getForumDelay().reset();
        player.getPacketSender()
            .sendMessage("This is currently disabled, try again in 30 minutes!");
        return;
      }
      if (!player.getRights().isStaff()) {
        try {
          ForumDatabase.connect();
          player.addForumConnections(60);
          int current_rank_id = ForumDatabase.getCurrentMemberID(player.getUsername());
          if (current_rank_id != ForumDatabase.regular_donator
              && current_rank_id != ForumDatabase.super_donator
              && current_rank_id != ForumDatabase.extreme_donator
              && current_rank_id != ForumDatabase.legendary_donator
              && current_rank_id != ForumDatabase.uber_donator
              && current_rank_id != ForumDatabase.validating
              && current_rank_id != ForumDatabase.members) {
            player.getPacketSender().sendMessage(
                "You have a rank on the forum that is not supported with this command.");
            player.getForumDelay().reset();
            return;
          } else if (current_rank_id == ForumDatabase.banned) {
            player.getPacketSender().sendMessage("Your forum account is banned.");
            player.getForumDelay().reset();
            return;
          }
          player.setForumConnectionsRank(player.getDonorRights());
          if (ForumDatabase.check_has_username(player.getUsername())) {
            ForumDatabase.update_donator_rank(player.getUsername(), player.getDonorRights());
            player.getPacketSender()
                .sendMessage("Your in-game rank has been added to your forum account.");
            player.getForumDelay().reset();
          } else {
            player.getPacketSender().sendMessage(
                "We noticed you don't have a forum account! You should make one at <col=ff0000><shad=0>::register");
            player.getForumDelay().reset();
          }
          ForumDatabase.destroy_connection();
        } catch (Exception e) {
          System.out.println(e);
        }
      } else {
        player.getPacketSender().sendMessage("Staff members are not allowed to use this command.");
        player.getForumDelay().reset();
      }
    }
    if (wholeCommand.equalsIgnoreCase("wiki")) {
      player.getPacketSender().sendString(1, "www.runelive-2.wikia.com/wiki/Ikov_2_Wikia");
      player.getPacketSender().sendMessage("Attempting to open: www.ikov2.org/wiki/");
    }
    if (command[0].equalsIgnoreCase("attacks")) {
      int attack = DesolaceFormulas.getMeleeAttack(player);
      int range = DesolaceFormulas.getRangedAttack(player);
      int magic = DesolaceFormulas.getMagicAttack(player);
      player.getPacketSender().sendMessage("@bla@Melee attack: @or2@" + attack
          + "@bla@, ranged attack: @or2@" + range + "@bla@, magic attack: @or2@" + magic);
    }
    if (command[0].equals("save")) {
      player.save();
      player.getPacketSender().sendMessage("Your progress has been saved.");
    }
    if (command[0].equals("help") || command[0].equals("support")) {
      player.getPacketSender().sendString(1,
          "wwwikov2.org/forum/index.php?app=core&module=global&section=register");
      player.getPacketSender().sendMessage("Attempting to open: www.ikov2.org/forum/?app=tickets");
      player.getPacketSender()
          .sendMessage("Please note this requires you to register on the forums, type ::register!");
    }
    if (command[0].equals("register")) {
      player.getPacketSender().sendString(1,
          "www.ikov2.org/forum/index.php?app=core&module=global&section=register");
      player.getPacketSender().sendMessage(
          "Attempting to open: www.ikov2.org/forum/index.php?app=core&module=global&section=register");
    }
    if (command[0].equals("forum") || command[0].equals("forums")) {
      player.getPacketSender().sendString(1, "www.ikov2.org/forum/");
      player.getPacketSender().sendMessage("Attempting to open: www.ikov2.org/forum/");
    }
    if (command[0].equals("scores") || command[0].equals("hiscores")
        || command[0].equals("highscores")) {
      if (!GameSettings.HIGHSCORE_CONNECTIONS) {
        player.getPacketSender()
            .sendMessage("Hiscores is currently turned off, please try again in 30 minutes!");
        return;
      }
      player.getPacketSender().sendString(1, "www.ikov2.org/hiscores/");
      player.getPacketSender().sendMessage("Attempting to open: www.ikov2.org/hiscores/");
    }
    if (command[0].equals("thread")) {
      int thread = Integer.parseInt(command[1]);
      player.getPacketSender().sendString(1,
          "www.ikov2.org/forum/index.php?/topic/" + thread + "-threadcommand/");
      player.getPacketSender().sendMessage("Attempting to open: Thread " + thread);
    }
    if (command[0].equals("Farming2")) {
      if (Dungeoneering.doingDungeoneering(player)) {
        player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
        return;
      }
      if (player.getLocation() != null && player.getWildernessLevel() > 20) {
        player.getPacketSender().sendMessage("You cannot do this at the moment.");
        return;
      }
      Position position = new Position(2816, 3463, 0);
      TeleportHandler.teleportPlayer(player, position, player.getSpellbook().getTeleportType());;
      player.getPacketSender().sendMessage("Teleporting you home!");
    }
    if (command[0].equals("home")) {
      if (Dungeoneering.doingDungeoneering(player)) {
        player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
        return;
      }
      if (player.getLocation() != null && player.getWildernessLevel() > 20) {
        player.getPacketSender().sendMessage("You cannot do this at the moment.");
        return;
      }
      Position position = new Position(3087, 3502, 0);
      TeleportHandler.teleportPlayer(player, position, player.getSpellbook().getTeleportType());
      player.getPacketSender().sendMessage("Teleporting you home!");
    }
    if (command[0].equals("train")) {
      if (Dungeoneering.doingDungeoneering(player)) {
        player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
        return;
      }
      if (player.getLocation() != null && player.getWildernessLevel() > 20) {
        player.getPacketSender().sendMessage("You cannot do this at the moment.");
        return;
      }
      Position position = new Position(2679, 3720, 0);
      TeleportHandler.teleportPlayer(player, position, player.getSpellbook().getTeleportType());
      player.getPacketSender().sendMessage("Teleporting you to rock crabs!");
    }
    if (command[0].equals("edge")) {
      if (Dungeoneering.doingDungeoneering(player)) {
        player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
        return;
      }
      if (player.getLocation() != null && player.getWildernessLevel() > 20) {
        player.getPacketSender().sendMessage("You cannot do this at the moment.");
        return;
      }
      Position position = new Position(3087, 3502, 0);
      TeleportHandler.teleportPlayer(player, position, player.getSpellbook().getTeleportType());
      player.getPacketSender().sendMessage("Teleporting you to edgeville!");
    }
    if (command[0].equals("teamspeak")) {
      player.getPacketSender().sendMessage("Teamspeak address: ts3.ikov2.org");
    }
    if (command[0].equals("market")) {
      if (Dungeoneering.doingDungeoneering(player)) {
        player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
        return;
      }
      if (player.getLocation() != null && player.getWildernessLevel() > 20) {
        player.getPacketSender().sendMessage("You cannot do this at the moment.");
        return;
      }
      int random = Misc.getRandom(3);
      switch (random) {
        case 0:
          TeleportHandler.teleportPlayer(player, new Position(3212, 3429, 0),
              player.getSpellbook().getTeleportType());
          break;
        case 1:
          TeleportHandler.teleportPlayer(player, new Position(3213, 3429, 0),
              player.getSpellbook().getTeleportType());
          break;
        case 2:
          TeleportHandler.teleportPlayer(player, new Position(3213, 3428, 0),
              player.getSpellbook().getTeleportType());
          break;
        case 3:
          TeleportHandler.teleportPlayer(player, new Position(3212, 3428, 0),
              player.getSpellbook().getTeleportType());
          break;
      }
      player.getPacketSender().sendMessage("Welcome to the Market!");
    }
    if (command[0].equals("claim")) {
      if (!GameSettings.STORE_CONNECTIONS) {
        player.getPacketSender()
            .sendMessage("The store is currently offline! Try again in 30 minutes.");
        return;
      }
      player.getPacketSender().sendMessage("Checking for any store purchases...");
      Store.start_store_process(player);
    }
    if (command[0].equals("empty")) {
      if (player.getLocation() == Location.WILDERNESS) {
        player.getPacketSender().sendMessage("You can't use this command in the wilderness!");
        return;
      }
      player.getPacketSender().sendInterfaceRemoval().sendMessage("You clear your inventory.");
      player.getSkillManager().stopSkilling();
      player.getInventory().resetItems().refreshItems();
    }
    if (command[0].equals("gamble")) {
      if (Dungeoneering.doingDungeoneering(player)) {
        player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
        return;
      }
      if (player.getLocation() != null && player.getWildernessLevel() > 20) {
        player.getPacketSender().sendMessage("You cannot do this at the moment.");
        return;
      }
      Position position = new Position(2441, 3090, 0);
      TeleportHandler.teleportPlayer(player, position, player.getSpellbook().getTeleportType());
      player.getPacketSender().sendMessage(
          "Welcome to the Gambling Area, make sure you always use a middle man for high bets!");
      player.getPacketSender()
          .sendMessage("Recording your stake will only get the player banned if they scam.");
    }
    if (command[0].equals("players")) {
      player.getPacketSender().sendInterfaceRemoval();
      PlayersOnlineInterface.showInterface(player);
    }
    if (command[0].equalsIgnoreCase("[cn]")) {
      if (player.getInterfaceId() == 40172) {
        ClanChatManager.setName(player, wholeCommand.substring(wholeCommand.indexOf(command[1])));
      }
    }
  }
}
