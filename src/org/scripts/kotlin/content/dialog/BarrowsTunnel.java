package org.scripts.kotlin.content.dialog;

import com.chaos.model.Position;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.entity.impl.player.Player;

public class BarrowsTunnel extends Dialog {

    public Dialog dialog = this;

    public BarrowsTunnel(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.SCARED, "You've found a hidden tunnel! Would you like to enter?");
            case 1:
                return Dialog.createOption(new TwoOption(
                        "Enter",
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                player.getPacketSender().sendInterfaceRemoval();
                                if (player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount() < 5) {
                                    player.getPacketSender()
                                            .sendMessage("You must have a killcount of at least 5 to enter the tunnel.");
                                    return;
                                }
                                player.moveTo(new Position(3552, 9692));
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
