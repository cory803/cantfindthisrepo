package org.scripts.kotlin.content.dialog.BankPin;

import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.BankPin;
import com.chaos.world.entity.impl.player.Player;

public class BankPinTut extends Dialog {

    public Dialog dialog = this;

    public BankPinTut(Player player) {
        super(player);
        setEndState(2);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "To keep your account safe we require everyone to set a pin for their account.");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "We will now have you enter a 4 digit pin! Keep these pins safe and secure in case your password ever gets stolen.");
            case 2:
                return Dialog.createOption(new TwoOption(
                        "Setup Bank Pin",
                        " ") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_2:
                                BankPin.init(player, false);
                                break;
                            case OPTION_2_OF_2:
                                BankPin.init(player, false);
                                break;
                        }
                    }
                });
        }
        return null;
    }
}
