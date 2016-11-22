package org.scripts.kotlin.content.dialog.npcs;

import com.chaos.GameSettings;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.pos.PosFeaturedShops;
import com.chaos.world.entity.impl.player.Player;

public class POSMerchant2 extends Dialog {
    int x = 0;
    public POSMerchant2(Player player, int x) {
        super(player);
        this.x = x;
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hello "+getPlayer().getUsername()+", would you like to purchase a shop for 10m?");
            case 1:
            return Dialog.createOption(new TwoOption(
                    "Yes, purchase this slot for 10m",
                    "No, thank you") {
                @Override
                public void execute(Player player, OptionType option) {
                    switch(option) {
                        case OPTION_1_OF_2:
                            player.getPacketSender().sendInterfaceRemoval();
                            if (!GameSettings.POS_ENABLED) {
                                player.getPacketSender().sendMessage("Player owned shops have been disabled.");
                                return;
                            }
                            if (player.getGameModeAssistant().isIronMan()) {
                                player.getPacketSender().sendMessage("Ironmen can't use the player owned shops!");
                                return;
                            }

                            if (player.getMoneyInPouch() >= 10000000) {
                                player.setMoneyInPouch(player.getMoneyInPouch() - 10000000);
                                player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
                                PosFeaturedShops.isEmpty[x] = true;
                                PosFeaturedShops.timeRemaining[x] = 120000; //120k ticks = 2hours
                                PosFeaturedShops.shopOwner[x] = player.getUsername();
                                player.getPacketSender().sendMessage("You have just bought the " + x + " slot for your featured shop.");
                            } else {
                                player.getPacketSender().sendMessage("You do not have 10m coins in your money pouch.");
                            }
                            break;
                        case OPTION_2_OF_2:
                            player.getPacketSender().sendInterfaceRemoval();
                            break;
                    }
                }
            });
            }
        return null;
    }
}
