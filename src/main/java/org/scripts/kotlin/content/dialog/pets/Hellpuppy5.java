package org.scripts.kotlin.content.dialog.pets;

import com.runelive.model.player.dialog.Dialog;
import com.runelive.model.player.dialog.DialogHandler;
import com.runelive.model.player.dialog.DialogMessage;
import com.runelive.world.entity.impl.player.Player;

public class Hellpuppy5 extends Dialog {

    public Dialog dialog = this;

    public Hellpuppy5(Player player) {
        super(player);
        setEndState(3);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "What a cute puppy, how nice to meet you.");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "It'd be nice to meat you too...");
            case 2:
                return Dialog.createPlayer(DialogHandler.CALM, "Urk... nice doggy.");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM, "Grrr....");
        }
        return null;
    }
}
