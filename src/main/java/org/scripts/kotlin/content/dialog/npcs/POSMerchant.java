package org.scripts.kotlin.content.dialog.npcs;

import com.runelive.GameSettings;
import com.runelive.model.input.impl.PosSearchShop;
import com.runelive.model.input.impl.SetPOSCaption;
import com.runelive.model.options.fouroption.FourOption;
import com.runelive.model.player.dialog.Dialog;
import com.runelive.model.player.dialog.DialogHandler;
import com.runelive.model.player.dialog.DialogMessage;
import com.runelive.world.content.pos.PlayerOwnedShops;
import com.runelive.world.entity.impl.player.Player;

public class POSMerchant extends Dialog {

    public POSMerchant(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hello "+getPlayer().getUsername()+", welcome to the RuneLive market!");
            case 1:
            return Dialog.createOption(new FourOption(
                    "Set my store caption",
                    "Search through stores",
                    "Open my shop",
                    "Open someone else's shop") {
                @Override
                public void execute(Player player, OptionType option) {
                    switch(option) {
                        case OPTION_1_OF_4:
                            player.getPacketSender().sendInterfaceRemoval();
                            player.getPacketSender().sendEnterInputPrompt("Enter a caption for your shop:");
                            player.setInputHandling(new SetPOSCaption());
                            break;
                        case OPTION_2_OF_4:
                            player.getPacketSender().sendInterfaceRemoval();
                            if (!GameSettings.POS_ENABLED) {
                                player.getPacketSender().sendMessage("Player owned shops have been disabled.");
                                return;
                            }
                            if (player.getGameModeAssistant().isIronMan()) {
                                player.getPacketSender().sendMessage("Ironmen can't use the player owned shops!");
                                return;
                            }
                            PlayerOwnedShops.openItemSearch(player, true);
                            //player.setPlayerOwnedShopping(true);
                            break;
                        case OPTION_3_OF_4:
                            player.getPacketSender().sendInterfaceRemoval();
                            if (!GameSettings.POS_ENABLED) {
                                player.getPacketSender().sendMessage("Player owned shops have been disabled.");
                                return;
                            }
                            if (player.getGameModeAssistant().isIronMan()) {
                                player.getPacketSender().sendMessage("Ironmen can't use the player owned shops!");
                                return;
                            }
                            player.getPacketSender().sendString(41900, "");
                            PlayerOwnedShops.openShop(player.getUsername(), player);
                            player.setPlayerOwnedShopping(true);
                            break;
                        case OPTION_4_OF_4:
                            player.getPacketSender().sendInterfaceRemoval();
                            if (!GameSettings.POS_ENABLED) {
                                player.getPacketSender().sendMessage("Player owned shops have been disabled.");
                                return;
                            }
                            player.getPacketSender().sendEnterInputPrompt("Enter the name of a player's shop:");
                            player.setInputHandling(new PosSearchShop());
                            //player.setPlayerOwnedShopping(true);
                            break;
                    }
                }
            });
            }
        return null;
    }
}
