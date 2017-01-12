package org.scripts.kotlin.content.dialog.npcs;

import com.runelive.model.container.impl.Shop;
import com.runelive.model.options.threeoption.ThreeOption;
import com.runelive.model.player.dialog.Dialog;
import com.runelive.model.player.dialog.DialogHandler;
import com.runelive.model.player.dialog.DialogMessage;
import com.runelive.world.entity.impl.player.Player;

public class Pikkupstix extends Dialog {

    public Dialog dialog = this;

    public Pikkupstix(Player player, int state) {
        super(player);
        setState(state);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hello, do you need to see on my shops?");
            case 1:
                return Dialog.createOption(new ThreeOption(
                        "Shop I",
                        "Shop II",
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_3:
                                Shop.ShopManager.getShops().get(34).open(player);
                                break;
                            case OPTION_2_OF_3:
                                Shop.ShopManager.getShops().get(35).open(player);
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
