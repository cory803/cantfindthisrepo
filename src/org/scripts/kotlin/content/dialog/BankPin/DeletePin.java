package org.scripts.kotlin.content.dialog.BankPin;

import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.BankPin;
import com.chaos.world.entity.impl.player.Player;

public class DeletePin extends Dialog {

    public Dialog dialog = this;

    public DeletePin(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "You do not have a bank PIN set. Would you like to set one?");
            case 1:
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
