package com.runelive.engine.task.impl;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import com.runelive.GameSettings;
import com.runelive.engine.task.Task;
import com.runelive.model.Animation;
import com.runelive.model.Flag;
import com.runelive.model.GameMode;
import com.runelive.model.GroundItem;
import com.runelive.model.Item;
import com.runelive.model.Locations.Location;
import com.runelive.model.Position;
import com.runelive.model.Skill;
import com.runelive.util.Misc;
import com.runelive.world.World;
import com.runelive.world.content.ItemsKeptOnDeath;
import com.runelive.world.content.PlayerLogs;
import com.runelive.world.entity.impl.GroundItemManager;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;

/**
 * Represents a player's death task, through which the process of dying is handled, the animation,
 * dropping items, etc.
 * 
 * @author relex lawl, redone by Gabbe.
 */

public class PlayerDeathTask extends Task {

  /**
   * The PlayerDeathTask constructor.
   * 
   * @param player The player setting off the task.
   */
  public PlayerDeathTask(Player player) {
    super(1, player, false);
    this.player = player;
  }

  private Player player;
  private int ticks = 5;
  private boolean dropItems = true;
  Position oldPosition;
  Location loc;
  ArrayList<Item> itemsToKeep = null;
  NPC death;

  @Override
  public void execute() {
    if (player == null) {
      stop();
      return;
    }

    try {
      switch (ticks) {
        case 5:
          player.getPacketSender().sendInterfaceRemoval();
          player.getMovementQueue().setLockMovement(true).reset();
          break;
        case 3:
          player.performAnimation(new Animation(0x900));
          player.getPacketSender().sendMessage("Oh dear, you are dead!");
          player.save();
          this.death = getDeathNpc(player);
          break;
        case 1:
          this.oldPosition = player.getPosition().copy();
          this.loc = player.getLocation();
          if (loc != Location.DUNGEONEERING && loc != Location.PEST_CONTROL_GAME
              && loc != Location.DUEL_ARENA && loc != Location.FREE_FOR_ALL_ARENA
              && loc != Location.FREE_FOR_ALL_WAIT && loc != Location.SOULWARS
              && loc != Location.FIGHT_PITS && loc != Location.FIGHT_PITS_WAIT_ROOM
              && loc != Location.FIGHT_CAVES && loc != Location.RECIPE_FOR_DISASTER
              && loc != Location.GRAVEYARD) {
            Player killer = player.getCombatBuilder().getKiller(true);
            // final boolean doubleDeath = player.isDying() && killer.getConstitution() <= 0;
            // if(killer.getLocation() == Location.WILDERNESS || killer.getLocation() ==
            // Location.WILDKEY_ZONE) {
            // if(doubleDeath)
            // killer.restart();
            // }
            boolean spawnItems = false;
            if (dropItems) {
              itemsToKeep = ItemsKeptOnDeath.getItemsToKeep(player);
              final CopyOnWriteArrayList<Item> playerItems = new CopyOnWriteArrayList<Item>();
              playerItems.addAll(player.getInventory().getValidItems());
              playerItems.addAll(player.getEquipment().getValidItems());
              final Position position = player.getPosition();
              if (loc == Location.WILDERNESS || loc == Location.WILDKEY_ZONE) {
                spawnItems = true;
                for (Item item : playerItems) {
                  if (!item.tradeable() || itemsToKeep.contains(item)) {
                    if (!itemsToKeep.contains(item)) {
                      itemsToKeep.add(item);
                    }
                    continue;
                  }
                  if (spawnItems) {
                    if (item != null && item.getId() > 0 && item.getAmount() > 0) {
                    	int address = Misc.random(0, Integer.MAX_VALUE);
                      GroundItemManager.spawnGroundItem(
                          (killer != null && killer.getGameMode() == GameMode.NORMAL ? killer
                              : player),
                          new GroundItem(item, position,
                              killer != null ? killer.getUsername() : player.getUsername(),
                              player.getHostAddress(), false, 150, true, 150, address));
                      if (killer != null) {
                    	PlayerLogs.kills(killer, player, item);
                      }
                    }
                  }
                }
              }
              if (killer != null) {
                killer.getPacketSender()
                    .sendMessage("You have just killed the player " + player.getUsername() + ". ");
                player.getPacketSender().sendMessage(
                    "You were just killed by the player " + killer.getUsername() + ".");
                killer.getPlayerKillingAttributes().add(player);
                player.getPlayerKillingAttributes()
                    .setPlayerDeaths(player.getPlayerKillingAttributes().getPlayerDeaths() + 1);
                player.getPlayerKillingAttributes().setPlayerKillStreak(0);
                player.getPointsHandler().refreshPanel();
              }
              if (loc == Location.WILDERNESS || loc == Location.WILDKEY_ZONE) {
                player.getInventory().resetItems().refreshItems();
                player.getEquipment().resetItems().refreshItems();
              }
            }
          } else
            dropItems = false;
          player.getPacketSender().sendInterfaceRemoval();
          player.setEntityInteraction(null);
          player.getMovementQueue().setFollowCharacter(null);
          player.getCombatBuilder().cooldown(false);
          player.setTeleporting(false);
          player.setWalkToTask(null);
          player.getSkillManager().stopSkilling();
          break;
        case 0:
          if (dropItems) {
            if (loc == Location.WILDERNESS || loc == Location.WILDKEY_ZONE) {
              if (itemsToKeep != null) {
                for (Item it : itemsToKeep) {
                  player.getInventory().add(it.getId(), 1);
                }
                itemsToKeep.clear();
              }
            }
          }
          if (death != null) {
            World.deregister(death);
          }
          player.restart();
          player.getUpdateFlag().flag(Flag.APPEARANCE);
          loc.onDeath(player);
          if (loc != Location.DUNGEONEERING) {
            if (player.getPosition().equals(oldPosition))
              if(player.homeLocation == 0) {
                player.moveTo(GameSettings.DEFAULT_POSITION_VARROCK.copy());
              } else {
                player.moveTo(GameSettings.DEFAULT_POSITION_EDGEVILLE.copy());
              }
          }
          player = null;
          oldPosition = null;
          stop();
          break;
      }
      ticks--;
    } catch (Exception e) {
      setEventRunning(false);
      e.printStackTrace();
      if (player != null) {
        if(player.homeLocation == 0) {
          player.moveTo(GameSettings.DEFAULT_POSITION_VARROCK.copy());
          player.setConstitution(player.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
        } else {
          player.moveTo(GameSettings.DEFAULT_POSITION_EDGEVILLE.copy());
          player.setConstitution(player.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
        }
      }
    }
  }

  public static NPC getDeathNpc(Player player) {
    NPC death = new NPC(2862, new Position(player.getPosition().getX() + 1, player.getPosition().getY() + 1, player.getPosition().getZ()));
    World.register(death);
    death.setEntityInteraction(player);
    death.performAnimation(new Animation(401));
    death.forceChat(randomDeath(player.getUsername()));
    return death;
  }


  public static String randomDeath(String name) {
    switch (Misc.getRandom(8)) {
      case 0:
        return "There is no escape, " + Misc.formatText(name) + "...";
      case 1:
        return "Muahahahaha!";
      case 2:
        return "You belong to me!";
      case 3:
        return "Beware mortals, " + Misc.formatText(name) + " travels with me!";
      case 4:
        return "Your time here is over, " + Misc.formatText(name) + "!";
      case 5:
        return "Now is the time you die, " + Misc.formatText(name) + "!";
      case 6:
        return "I claim " + Misc.formatText(name) + " as my own!";
      case 7:
        return "" + Misc.formatText(name) + " is mine!";
      case 8:
        return "Let me escort you back to Edgeville, " + Misc.formatText(name) + "!";
      case 9:
        return "I have come for you, " + Misc.formatText(name) + "!";
    }
    return "";
  }

}
