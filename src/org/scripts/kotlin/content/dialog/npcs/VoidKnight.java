package org.scripts.kotlin.content.dialog.npcs;

import com.chaos.model.container.impl.Shop;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.entity.impl.player.Player;

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
                return Dialog.createNpc(DialogHandler.CALM, "Hello warrior, I can help you exchange your commedations for all you've done.");
            case 1:
            return Dialog.createOption(new TwoOption(
                    "Yes, show me what your selling",
                    "No, I'm ok right now") {
                @Override
                public void execute(Player player, OptionType option) {
                    switch(option) {
                        case OPTION_1_OF_2:
                            player.getVoidShop();
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
