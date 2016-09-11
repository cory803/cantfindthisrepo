package org.scripts.kotlin.content.dialog.npcs;

import com.chaos.model.container.impl.Shop;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.entity.impl.player.Player;

public class Oziach extends Dialog {

    public Dialog dialog = this;

    public Oziach(Player player) {
        super(player);
        setEndState(3);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.PLAIN_EVIL, "Hello Traveler! What would you like me to do for you?");
            case 1:
                return Dialog.createOption(new TwoOption(
                        "Information on PvP Armour",
                        "Open the PvP Stores") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                setState(2);
//                                setEndState(2);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_2_OF_2:
                                setState(4);
                                setEndState(4);
                                player.getDialog().sendDialog(dialog);
                                break;
                        }
                    }
                });
            case 2:
                return Dialog.createNpc(DialogHandler.PLAIN_EVIL, "In the Pk Exchange store you can buy corrupt pvp armours. They do NOT degrade and stay how is. On the other hand their is");
            case 3:
                return Dialog.createNpc(DialogHandler.PLAIN_EVIL, "non corrupt pvp armour which is dropped from wild revenants and WILL degrade over time.");
            case 4:
            return Dialog.createOption(new TwoOption(
                    "PvP Armours",
                    "Weapons & Misc Supplies") {
                @Override
                public void execute(Player player, OptionType option) {
                    switch(option) {
                        case OPTION_1_OF_2:
                            Shop.ShopManager.getShops().get(22).open(player);
                            break;
                        case OPTION_2_OF_2:
                            Shop.ShopManager.getShops().get(21).open(player);
                            break;
                    }
                }
            });
            }
        return null;
    }
}
