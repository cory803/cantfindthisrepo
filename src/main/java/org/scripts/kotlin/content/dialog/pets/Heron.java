package org.scripts.kotlin.content.dialog.pets;

import com.runelive.model.player.dialog.Dialog;
import com.runelive.model.player.dialog.DialogHandler;
import com.runelive.model.player.dialog.DialogMessage;
import com.runelive.world.entity.impl.player.Player;

public class Heron extends Dialog {

    public Dialog dialog = this;

    public Heron(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "Hop inside my mouth if you want to live!");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "I'm not falling for that... I'm not a fish! I've got more foresight than that.");
        }
        return null;
    }
}
