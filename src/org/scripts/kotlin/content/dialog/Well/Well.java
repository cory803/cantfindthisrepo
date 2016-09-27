package org.scripts.kotlin.content.dialog.Well;

import com.chaos.model.options.fouroption.FourOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.Well.WellOfGoodwill;
import com.chaos.world.entity.impl.player.Player;

public class Well extends Dialog {

    public Dialog dialog = this;

    public Well(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new FourOption(
                        "What is the Well",
                        "Look down Well",
                        "Contribute to Well",
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_4:
                                player.getDialog().sendDialog(new WellStatement(player));
                                break;
                            case OPTION_2_OF_4:
                                WellOfGoodwill.lookDownWell(player);
                                break;
                            case OPTION_3_OF_4:
                                player.getDialog().sendDialog(new DonateToWellDial(player));
                                break;
                            case OPTION_4_OF_4:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
        }
        return null;
    }
}
