package org.scripts.kotlin.content.dialog;

import com.chaos.model.CombatIcon;
import com.chaos.model.Hit;
import com.chaos.model.Hitmask;
import com.chaos.model.Skill;
import com.chaos.model.options.fiveoption.FiveOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.entity.impl.player.Player;

public class RockCake extends Dialog {

    public Dialog dialog = this;

    public RockCake(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                getPlayer().dealDamage(null, new Hit(500, Hitmask.RED, CombatIcon.NONE));
                return Dialog.createPlayer(DialogHandler.CALM, "Ow! I nearly broke a tooth!");
        }
        return null;
    }
}
