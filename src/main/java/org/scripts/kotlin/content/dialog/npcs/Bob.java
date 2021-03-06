package org.scripts.kotlin.content.dialog.npcs;

import com.runelive.model.player.dialog.Dialog;
import com.runelive.model.player.dialog.DialogHandler;
import com.runelive.model.player.dialog.DialogMessage;
import com.runelive.world.entity.impl.player.Player;

public class Bob extends Dialog {

    public Dialog dialog = this;

    public Bob(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hello! You can use unnoted degraded barrows items on me and I will repair them for a price.");
        }
        return null;
    }
}
