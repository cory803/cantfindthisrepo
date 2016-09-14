package org.scripts.kotlin.content.dialog.Well;

import com.chaos.model.options.fouroption.FourOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.WellOfGoodwill;
import com.chaos.world.entity.impl.player.Player;

public class Well extends Dialog {

    public Dialog dialog = this;

    public Well(Player player) {
        super(player);
        setEndState(4);
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
                                setState(2);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_4_OF_4:

                                setState(1);
                                player.getDialog().sendDialog(dialog);
                                break;
                        }
                    }
                });
            case 2:
                return Dialog.createPlayer(DialogHandler.HAPPY, "I would like to play as a "+getPlayer().getGameModeAssistant().getGameMode().getModeName()+".");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM_CONTINUED, "A very interesting choice! Would you like to go through the Chaos tutorial to help you learn how to make money and other sorts of stuff?");
            case 4:
                return Dialog.createOption(new TwoOption(
                        "Yes, take the tutorial.",
                        "No, skip the tutorial.") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                player.setNewPlayer(false);
                                player.setPlayerLocked(false);
                                player.getPacketSender().sendInterfaceRemoval();
                                //TODO: Start the tutorial
                                break;
                            case OPTION_2_OF_2:
                                player.setNewPlayer(false);
                                player.setPlayerLocked(false);
                                player.getPacketSender().sendInterfaceRemoval();
                                //TODO: Continue to setting an account pin
                                break;
                        }
                    }
                });

        }
        return null;
    }
}
