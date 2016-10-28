package org.scripts.kotlin.content.dialog.npcs;

import com.chaos.model.definitions.ItemDefinition;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.util.Misc;
import com.chaos.world.entity.impl.player.Player;

public class PumpkinPete extends Dialog {

    public int[] WITCH_DOCTOR_ITEMS = {
        20046,
        20045,
        20044
    };

    public int getRewardItem() {
        for(int i = 0; i < WITCH_DOCTOR_ITEMS.length; i++) {
            if (!getPlayer().hasItem(WITCH_DOCTOR_ITEMS[i])) {
                return WITCH_DOCTOR_ITEMS[i];
            }
        }
        return WITCH_DOCTOR_ITEMS[Misc.inclusiveRandom(WITCH_DOCTOR_ITEMS.length - 1)];
    }


    public Dialog dialog = this;

    public PumpkinPete(Player player, int state) {
        super(player);
        setState(state);
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
                setEndState(3);
                return Dialog.createPlayer(DialogHandler.CALM, "OK!");
            case 4:
                setEndState(4);
                if(!getPlayer().getInventory().contains(1959)) {
                    return Dialog.createNpc(DialogHandler.ANGRY_1, "I need pumpkins!");
                } else {
                    int random = Misc.inclusiveRandom(0, 4);
                    int randomItem = WITCH_DOCTOR_ITEMS[Misc.inclusiveRandom(WITCH_DOCTOR_ITEMS.length - 1)];
                    if (random == 4) {
                        if (getPlayer().hasItem(randomItem)) {
                            randomItem = getRewardItem();
                        }
                        getPlayer().getInventory().delete(1959, 1);
                        getPlayer().getInventory().add(randomItem, 1);
                        return Dialog.createNpc(DialogHandler.CALM, "Oh boy that tastes good! Enjoy a " + ItemDefinition.forId(randomItem).getName() + " as a reward!");
                    } else {
                        int amount = Misc.inclusiveRandom(200_000, 300_000);
                        getPlayer().getInventory().delete(1959, 1);
                        getPlayer().getInventory().add(995, amount);
                        return Dialog.createNpc(DialogHandler.CALM, "Oh boy that tastes good! Enjoy " + Misc.format(amount) + " coins as a reward!");
                    }
                }
            }
        return null;
    }
}
