package com.runelive.commands.ranks;

import com.runelive.GameSettings;
import com.runelive.commands.CommandHandler;
import com.runelive.commands.DonatorCommand;
import com.runelive.model.Locations.Location;
import com.runelive.model.MagicSpellbook;
import com.runelive.model.Position;
import com.runelive.model.Prayerbook;
import com.runelive.model.Skill;
import com.runelive.util.Misc;
import com.runelive.world.content.combat.magic.Autocasting;
import com.runelive.world.content.combat.prayer.CurseHandler;
import com.runelive.world.content.combat.prayer.PrayerHandler;
import com.runelive.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.runelive.world.content.transportation.TeleportHandler;
import com.runelive.world.entity.impl.player.Player;

public class DonatorCommands {

  private static final int REGULAR = 1;
  private static final int SUPER = 2;
  private static final int EXTREME = 3;
  private static final int LEGENDARY = 4;
  private static final int UBER = 5;

  public static void init() {
    CommandHandler.submit(new DonatorCommand("dzone", REGULAR) {

      @Override
      public boolean execute(Player player, String key, String input) throws Exception {
        if (Dungeoneering.doingDungeoneering(player)) {
          player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
          return false;
        }
        if (player.getLocation() != null && player.getWildernessLevel() > 20) {
          player.getPacketSender().sendMessage("You cannot do this at the moment.");
          return false;
        }
        Position position = new Position(2514, 3860, 0);
        player.moveTo(position);
        player.getPacketSender()
            .sendMessage("[<col=ff0000>Donator Zone</col>] Welcome to the donator zone.");
        return true;
      }

    });
    CommandHandler.submit(new DonatorCommand("ezone", EXTREME) {

      @Override
      public boolean execute(Player player, String key, String input) throws Exception {
        if (Dungeoneering.doingDungeoneering(player)) {
          player.getPacketSender().sendMessage("You can't use this command in a dungeon.");
          return false;
        }
        if (player.getLocation() != null && player.getWildernessLevel() > 20) {
          player.getPacketSender().sendMessage("You cannot do this at the moment.");
          return false;
        }
        Position position = new Position(3362, 9640, 0);
        int ran = Misc.getRandom(3);
        switch (ran) {
          case 0:
            position = new Position(3363, 9641, 0);
            break;
          case 1:
            position = new Position(3364, 9640, 0);
            break;
          case 2:
            position = new Position(3363, 9639, 0);
            break;
          case 3:
            position = new Position(3362, 9640, 0);
            break;
        }
        TeleportHandler.teleportPlayer(player, position, player.getSpellbook().getTeleportType());
        player.getPacketSender()
            .sendMessage("<img=9><col=00ff00><shad=0> Welcome to the Extreme Donator Zone!");
        return true;
      }

    });
    CommandHandler.submit(new DonatorCommand("togglepray", LEGENDARY) {

      @Override
      public boolean execute(Player player, String key, String input) throws Exception {
        if (player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 30) {
          player.getPacketSender()
              .sendMessage("You need a Defence level of at least 30 to use this altar.");
          return false;
        }
        if (player.getPrayerbook() == Prayerbook.NORMAL) {
          player.getPacketSender()
              .sendMessage("You sense a surge of power flow through your body!");
          player.setPrayerbook(Prayerbook.CURSES);
        } else {
          player.getPacketSender()
              .sendMessage("You sense a surge of purity flow through your body!");
          player.setPrayerbook(Prayerbook.NORMAL);
        }
        player.getPacketSender().sendTabInterface(GameSettings.PRAYER_TAB,
            player.getPrayerbook().getInterfaceId());
        PrayerHandler.deactivateAll(player);
        CurseHandler.deactivateAll(player);
        return true;
      }

    });
    CommandHandler.submit(new DonatorCommand("ancients", LEGENDARY) {

      @Override
      public boolean execute(Player player, String key, String input) throws Exception {
        if (player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
          player.getPacketSender().sendMessage("You cannot do this at the moment.");
          return false;
        }
        player.setSpellbook(MagicSpellbook.ANCIENT);
        player.getPacketSender()
            .sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId())
            .sendMessage("Your magic spellbook is changed to ancients..");
        Autocasting.resetAutocast(player, true);
        return true;
      }

    });
    CommandHandler.submit(new DonatorCommand("moderns", LEGENDARY) {

      @Override
      public boolean execute(Player player, String key, String input) throws Exception {
        if (player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
          player.getPacketSender().sendMessage("You cannot do this at the moment.");
          return false;
        }
        player.setSpellbook(MagicSpellbook.NORMAL);
        player.getPacketSender()
            .sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId())
            .sendMessage("Your magic spellbook is changed to moderns..");
        Autocasting.resetAutocast(player, true);
        return true;
      }

    });
    CommandHandler.submit(new DonatorCommand("lunars", LEGENDARY) {

      @Override
      public boolean execute(Player player, String key, String input) throws Exception {
        if (player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
          player.getPacketSender().sendMessage("You cannot do this at the moment.");
          return false;
        }
        player.setSpellbook(MagicSpellbook.LUNAR);
        player.getPacketSender()
            .sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId())
            .sendMessage("Your magic spellbook is changed to lunars..");
        Autocasting.resetAutocast(player, true);
        return true;
      }

    });
  }

}
