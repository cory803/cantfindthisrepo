package org.scripts.kotlin.content.dialog;

import com.runelive.model.Item;
import com.runelive.model.options.twooption.TwoOption;
import com.runelive.model.player.dialog.Dialog;
import com.runelive.model.player.dialog.DialogMessage;
import com.runelive.world.entity.impl.player.Player;

public class EmptyDialog extends Dialog {

    public Dialog dialog = this;

    public EmptyDialog(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(0, "Are you sure you want to delete all the items in your inventory? They will not be dropped to the ground, but permanently deleted.");
            case 1:
                return Dialog.createOption(new TwoOption(
                        "Empty my inventory",
                        "No") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                for (Item i : player.getInventory().getItems()) {
                                    player.getInventory().delete(i);
                                }
                                player.getPacketSender().sendMessage("You have emptied all of the items in your inventory.");
                                player.getPacketSender().sendInterfaceRemoval();
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
