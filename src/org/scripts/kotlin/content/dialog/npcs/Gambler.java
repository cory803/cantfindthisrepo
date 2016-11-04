package org.scripts.kotlin.content.dialog.npcs;

import com.chaos.model.container.impl.Shop;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.entity.impl.player.Player;

public class Gambler extends Dialog {

    public Gambler(Player player) {
        super(player);
        setEndState(2);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hmmm, be careful with these sneaky gamblers...");
            case 1:
                return Dialog.createPlayer(DialogHandler.CALM, "Yeah for sure *wink* *wink*");
            case 2:
                return Dialog.createStatement(DialogHandler.CALM, "The gambler stares at you...");
            }
        return null;
    }
}
