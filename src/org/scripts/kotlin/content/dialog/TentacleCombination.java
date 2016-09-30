package org.scripts.kotlin.content.dialog;

import com.chaos.model.options.threeoption.ThreeOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.entity.impl.player.Player;

public class TentacleCombination extends Dialog {

    public Dialog dialog = this;
    final int krakWhip = 13;
    final int abbyWhip = 4151;
    public TentacleCombination(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "When using the items on each other there will be a warning message that states The tentacle will gradually consume your whip and destroy it. You won't be able to get the whip out again. The combined item is not tradeable.");
            case 1:
                return Dialog.createOption(new TwoOption(
                        "Combine Kraken tentacle & Abyssal whip",
                        "No, don't combine them") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:

                                    player.getInventory().delete(krakWhip, 1);
                                    player.getInventory().delete(abbyWhip, 1);
                                    player.getInventory().add(21369, 1);
                                    player.getDialog().sendDialog(new TentacleStatement(player));
                                break;
                            case OPTION_2_OF_2:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
        }
        return null;
    }
}
