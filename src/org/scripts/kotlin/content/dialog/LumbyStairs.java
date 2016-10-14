package org.scripts.kotlin.content.dialog;

import com.chaos.model.Position;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.entity.impl.player.Player;

public class LumbyStairs extends Dialog {

    public Dialog dialog = this;

    public LumbyStairs(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new TwoOption(
                        "Climb Up",
                        "Climb Down") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                player.moveTo(new Position(player.getPosition().getX(),
                                        player.getPosition().getY(), player.getPosition().getZ() + 1));
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                            case OPTION_2_OF_2:
                                player.moveTo(new Position(player.getPosition().getX(),
                                        player.getPosition().getY(), player.getPosition().getZ() - 1));
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
        }
        return null;
    }
}
