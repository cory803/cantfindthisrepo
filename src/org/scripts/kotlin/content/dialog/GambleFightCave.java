package org.scripts.kotlin.content.dialog;

import com.chaos.model.Item;
import com.chaos.model.container.impl.Shop;
import com.chaos.model.definitions.ItemDefinition;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.util.Misc;
import com.chaos.world.entity.impl.player.Player;

public class GambleFightCave extends Dialog {

    int itemId;

    public GambleFightCave(Player player, int state, int itemId) {
        super(player);
        this.itemId = itemId;
        setState(state);
        setEndState(3);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hello! Would you like a chance at capturing me?");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "If you sacrifice your Fire cape you have a 1/30 chance, or a TokHaar-Kal (1/15 chance).");
            case 2:
                return Dialog.createOption(new TwoOption(
                        "Yes, sacrifice my "+ ItemDefinition.forId(itemId).getName(),
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                player.getInventory().delete(itemId, 1);
                                boolean gotIt = false;
                                if(itemId == 6570) {
                                    gotIt = Misc.inclusiveRandom(0, 30) == 5;
                                } else if(itemId == 19111) {
                                    gotIt = Misc.inclusiveRandom(0, 15) == 5;
                                }

                                if(gotIt) {
                                    player.getInventory().add(new Item(13225, 1));
                                    player.getDialog().sendDialog(new GambleFightCave(player, 3, -1));
                                } else {
                                    player.getPacketSender().sendInterfaceRemoval();
                                }
                                break;
                            case OPTION_2_OF_2:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
            case 3:
                setEndState(3);
                return Dialog.createNpc(DialogHandler.CALM, "Woohooooo CONGRATULATIONS! My dad would be proud.");
        }
        return null;
    }
}
