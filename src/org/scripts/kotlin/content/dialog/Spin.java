package org.scripts.kotlin.content.dialog;

import com.chaos.model.options.threeoption.ThreeOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.PlayerPanel;
import com.chaos.world.content.skill.impl.crafting.Flax;
import com.chaos.world.entity.impl.player.Player;

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
