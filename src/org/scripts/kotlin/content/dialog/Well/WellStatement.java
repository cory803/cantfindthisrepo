package org.scripts.kotlin.content.dialog.Well;

import com.chaos.model.options.fouroption.FourOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.clan.ClanChatManager;
import com.chaos.world.entity.impl.player.Player;

public class WellStatement extends Dialog {

    public Dialog dialog = this;

    public WellStatement(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "The Well of Goodwill, brought to us by Saradomin. It is said that the well can hold a hundred million coins, and that if it is filled, Players will recieve 30% bonus experience");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "and Donators will receive 50% bonus experience for 2 hours. It is also said that those who contributed will be blessed even further, and receive bonus Loyalty points too, during these 2 hours.");
        }
        return null;
    }
}
