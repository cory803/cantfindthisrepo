package org.scripts.kotlin.content.dialog.npcs;

import com.runelive.model.container.impl.Shop;
import com.runelive.model.options.threeoption.ThreeOption;
import com.runelive.model.player.dialog.Dialog;
import com.runelive.model.player.dialog.DialogHandler;
import com.runelive.model.player.dialog.DialogMessage;
import com.runelive.world.entity.impl.player.Player;

public class Frumscone extends Dialog {

    public Frumscone(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hey Traveler, would you like to see my supplies?");
            case 1:
            return Dialog.createOption(new ThreeOption(
                    "Let me see your talisman collection",
                    "I would like to exchange my energy fragments",
                    "No Thanks I don't need anything") {
                @Override
                public void execute(Player player, OptionType option) {
                    switch(option) {
                        case OPTION_1_OF_3:
                            Shop.ShopManager.getShops().get(26).open(player);
                            break;
                        case OPTION_2_OF_3:
                            Shop.ShopManager.getShops().get(27).open(player);
                            break;
                        case OPTION_3_OF_3:
                            player.getPacketSender().sendInterfaceRemoval();
                            break;
                    }
                }
            });
            }
        return null;
    }
}
