package org.scripts.kotlin.content.dialog.Well;

import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.util.Misc;
import com.chaos.world.content.WellOfGoodwill;
import com.chaos.world.entity.impl.player.Player;

public class LookDownWell extends Dialog {

    public Dialog dialog = this;
    Player player;
    public LookDownWell(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "It looks like the well could hold another " + Misc.insertCommasToNumber("" + WellOfGoodwill.getMissingAmount() + "") + " coins.");
//            case 1:
//                return player.getDialog().sendDialog(new WellStatement(player));
        }
        return null;
    }
}
