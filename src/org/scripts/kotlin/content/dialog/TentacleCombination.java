package org.scripts.kotlin.content.dialog;

import com.chaos.model.Item;
import com.chaos.model.options.threeoption.ThreeOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.Degrading;
import com.chaos.world.entity.impl.player.Player;

public class TentacleCombination extends Dialog {

    public Dialog dialog = this;

    public TentacleCombination(Player player) {
        super(player);
        setEndState(2);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "You won't be able to get the whip out again. The combined item is not tradeable.");
            case 1:
                return Dialog.createOption(new TwoOption(
                        "Combine Kraken tentacle & Abyssal whip",
                        "No, don't combine them") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                if(player.getInventory().contains(
                                    new Item[] {
                                        new Item(12004, 1),
                                        new Item(4151, 1),
                                    })) {
                                    player.getInventory().delete(12004, 1);
                                    player.getInventory().delete(4151, 1);
                                    player.getInventory().add(12006, 1);
                                    player.getDegrading().maxCharges(Degrading.DegradingItems.ABYSSAL_TENTACLE);
                                    setState(2);
                                    player.getDialog().sendDialog(dialog);
                                }
                                break;
                            case OPTION_2_OF_2:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
            case 2:
                return Dialog.createStatement(DialogHandler.CALM, "You combine the kraken tentacle with", "your abyssal whip and create an", "abyssal tentacle with 10,000 charges!");
        }
        return null;
    }
}
