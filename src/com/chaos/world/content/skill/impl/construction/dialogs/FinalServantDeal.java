package com.chaos.world.content.skill.impl.construction.dialogs;

import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.skill.impl.construction.ConstructionData;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;

public class FinalServantDeal extends Dialog {

    boolean accepted = false;

    public FinalServantDeal(Player player, boolean accepted) {
        super(player);
        setEndState(0);
        this.accepted = accepted;
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, accepted ? "You're hired!" : "Never mind." );
        }
        return null;
    }
}