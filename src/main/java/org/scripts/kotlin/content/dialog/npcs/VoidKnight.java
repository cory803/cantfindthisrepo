package org.scripts.kotlin.content.dialog.npcs;

import com.runelive.model.container.impl.Shop;
import com.runelive.model.options.twooption.TwoOption;
import com.runelive.model.player.dialog.Dialog;
import com.runelive.model.player.dialog.DialogHandler;
import com.runelive.model.player.dialog.DialogMessage;
import com.runelive.world.entity.impl.player.Player;

public class VoidKnight extends Dialog {

    public Dialog dialog = this;

    public VoidKnight(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hello warrior, I can help you exchange your commendations for all you've done.");
            case 1:
            return Dialog.createOption(new TwoOption(
                    "Yes, show me what your selling",
                    "No, I'm ok right now") {
                @Override
                public void execute(Player player, OptionType option) {
                    switch(option) {
                        case OPTION_1_OF_2:
                            Shop.ShopManager.getShops().get(47).open(player);
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
