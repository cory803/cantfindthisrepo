package org.scripts.kotlin.content.dialog;

import com.runelive.model.CombatIcon;
import com.runelive.model.Hit;
import com.runelive.model.Hitmask;
import com.runelive.model.player.dialog.Dialog;
import com.runelive.model.player.dialog.DialogHandler;
import com.runelive.model.player.dialog.DialogMessage;
import com.runelive.world.entity.impl.player.Player;

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
                getPlayer().getInventory().delete(2379, 1);
                return Dialog.createPlayer(DialogHandler.CALM, "Ow! I nearly broke a tooth!");
        }
        return null;
    }
}
