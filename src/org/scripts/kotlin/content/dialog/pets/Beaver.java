package org.scripts.kotlin.content.dialog.pets;

import com.runelive.model.player.dialog.Dialog;
import com.runelive.model.player.dialog.DialogHandler;
import com.runelive.model.player.dialog.DialogMessage;
import com.runelive.world.entity.impl.player.Player;

public class Beaver extends Dialog {

    public Dialog dialog = this;

    public Beaver(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "How much wood could a woodchuck chuck if a woodchuck could chuck wood?");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "Approximately 32,768 depending on his Woodcutting level.");
        }
        return null;
    }
}
