package org.scripts.kotlin.content.dialog.Well;

import com.runelive.model.input.impl.DonateToWell;
import com.runelive.model.options.threeoption.ThreeOption;
import com.runelive.model.player.dialog.Dialog;
import com.runelive.model.player.dialog.DialogMessage;
import com.runelive.world.entity.impl.player.Player;

public class DonateToWellDial extends Dialog {

    public DonateToWellDial(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new ThreeOption(
                        "Fill Bonus Xp Well", //exp
                        "Fill Well of Wealth", //drops
                        "Fill Well of Execution") { //pkp
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_3:
                                player.setInputHandling(new DonateToWell("exp"));
                                player.getPacketSender().sendInterfaceRemoval()
                                        .sendEnterAmountPrompt("How much money would you like to contribute with?");
                                break;
                            case OPTION_2_OF_3:
                                player.setInputHandling(new DonateToWell("drops"));
                                player.getPacketSender().sendInterfaceRemoval()
                                        .sendEnterAmountPrompt("How much money would you like to contribute with?");
                                break;
                            case OPTION_3_OF_3:
                                player.setInputHandling(new DonateToWell("pkp"));
                                player.getPacketSender().sendInterfaceRemoval()
                                        .sendEnterAmountPrompt("How much money would you like to contribute with?");
                                break;
                        }
                    }
                });
        }
        return null;
    }
}
