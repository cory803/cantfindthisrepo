package org.scripts.kotlin.content.dialog.npcs;

import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.entity.impl.player.Player;

public class PumpkinPete extends Dialog {

    public Dialog dialog = this;

    public PumpkinPete(Player player) {
        super(player);
        setEndState(3);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Dancing for Pumpkins!");
            case 1:
                return Dialog.createPlayer(DialogHandler.CALM, "What?");
            case 2:
                return Dialog.createNpc(DialogHandler.CALM, "Use a pumpkin on me and I will give you a suprise!");
            case 3:
                return Dialog.createPlayer(DialogHandler.CALM, "OK!");
            }
        return null;
    }
}
