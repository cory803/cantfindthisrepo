package org.scripts.kotlin.content.dialog;

import com.runelive.model.options.threeoption.ThreeOption;
import com.runelive.model.player.dialog.Dialog;
import com.runelive.model.player.dialog.DialogMessage;
import com.runelive.world.content.skill.impl.crafting.Flax;
import com.runelive.world.entity.impl.player.Player;

public class Spin extends Dialog {

    public Dialog dialog = this;

    public Spin(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new ThreeOption(
                        "Spin Flax",
                        "Spin Wool",
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_3:
                                Flax.showSpinInterface(player, true);
                                break;
                            case OPTION_2_OF_3:
                                Flax.showSpinInterface(player, false);
                                break;
                            case OPTION_3_OF_3:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
        }
        return null;
    }
}
