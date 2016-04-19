package com.runelive.commands.ranks;

import com.runelive.commands.Command;
import com.runelive.commands.CommandHandler;
import com.runelive.model.Item;
import com.runelive.model.PlayerRights;
import com.runelive.util.Misc;
import com.runelive.world.World;
import com.runelive.world.entity.impl.player.Player;

public class AdministratorCommands {

  public static void init() {
    CommandHandler.submit(new Command("globalyell", PlayerRights.ADMINISTRATOR) {

      @Override
      public boolean execute(Player player, String key, String input) throws Exception {
        player.getPacketSender()
            .sendMessage("Retype the command to renable/disable the yell channel.");
        World.setGlobalYell(!World.isGlobalYell());
        World.sendMessage("<img=4> @blu@The yell channel has been @dre@" + (World.isGlobalYell()
            ? "@dre@enabled@blu@ again!" : "@dre@disabled@blu@ temporarily!"));
        return true;
      }

    });
    CommandHandler.submit(new Command("host", PlayerRights.ADMINISTRATOR) {

      @Override
      public boolean execute(Player player, String key, String input) throws Exception {
        String playerName = input;
        Player player2 = World.getPlayerByName(playerName);
        if (player2 != null) {
          player.getPacketSender().sendMessage("" + player2.getUsername() + " host IP: "
              + player2.getHostAddress() + ", serial number: " + player2.getSerialNumber());
        } else
          player.getPacketSender().sendMessage("Could not find player: " + playerName);
        return true;
      }

    });
    CommandHandler.submit(new Command("gold", PlayerRights.ADMINISTRATOR) {

      @Override
      public boolean execute(Player player, String key, String input) throws Exception {
        Player p = World.getPlayerByName(input);
        if (p != null) {
          long gold = 0;
          for (Item item : p.getInventory().getItems()) {
            if (item != null && item.getId() > 0 && item.tradeable())
              gold += item.getDefinition().getValue();
          }
          for (Item item : p.getEquipment().getItems()) {
            if (item != null && item.getId() > 0 && item.tradeable())
              gold += item.getDefinition().getValue();
          }
          for (int i = 0; i < 9; i++) {
            for (Item item : p.getBank(i).getItems()) {
              if (item != null && item.getId() > 0 && item.tradeable())
                gold += item.getDefinition().getValue();
            }
          }
          gold += p.getMoneyInPouch();
          player.getPacketSender().sendMessage(p.getUsername() + " has "
              + Misc.insertCommasToNumber(String.valueOf(gold)) + " coins.");
        } else {
          player.getPacketSender().sendMessage("Can not find player online.");
          return false;
        }
        return true;
      }

    });
  }

}
