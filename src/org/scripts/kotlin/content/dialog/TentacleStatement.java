package org.scripts.kotlin.content.dialog;

import com.chaos.model.Position;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.entity.impl.player.Player;

public class TentacleStatement extends Dialog {

    public Dialog dialog = this;

    public TentacleStatement(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.SCARED, "You have combined your items into a tenetacle whip.");
        }
        return null;
    }
}
