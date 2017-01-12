package org.scripts.kotlin.content.dialog.BankPin;

import com.runelive.model.options.twooption.TwoOption;
import com.runelive.model.player.dialog.Dialog;
import com.runelive.model.player.dialog.DialogMessage;
import com.runelive.world.content.BankPin;
import com.runelive.world.entity.impl.player.Player;

public class DeletePin extends Dialog {

    public Dialog dialog = this;

    public DeletePin(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new TwoOption(
                        "Delete my Pin : Make my account Vulnerable",
                        "Keep my Pin : I want my account Safe") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_2:
                                if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
                                        && player.getBankPinAttributes().onDifferent(player)) {
                                    BankPin.init(player, true);
                                    return;
                                }
                                BankPin.deletePin(player);
                                break;
                            case OPTION_2_OF_2:
                                player.getPacketSender().sendInterfaceRemoval();
                                player.getBank(player.getCurrentBankTab()).open();
                                break;
                        }
                    }
                });
        }
        return null;
    }
}
