package org.scripts.kotlin.content.dialog.npcs;

import com.chaos.model.container.impl.Shop;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.entity.impl.player.Player;

public class Horvik extends Dialog {

    public Horvik(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hello! Would you like to see what armor I have for sale?");
            case 1:
                return Dialog.createOption(new TwoOption(
                        "Yes, show me what your selling.",
                        "No, I don't want to see.") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_2:
                                Shop.ShopManager.getShops().get(1).open(player);
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
