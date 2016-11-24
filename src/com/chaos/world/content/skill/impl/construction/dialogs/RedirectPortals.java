package com.chaos.world.content.skill.impl.construction.dialogs;

import com.chaos.model.options.threeoption.ThreeOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.skill.impl.construction.Construction;
import com.chaos.world.entity.impl.player.Player;

public class RedirectPortals extends Dialog {

    public RedirectPortals(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new ThreeOption(
                        "Right portal",
                        "Middle portal",
                        "Left portal") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_3:
                                player.setPortalSelected(2);
                                player.getPacketSender().sendInterfaceRemoval();
                                //TODO: Maybe something more in these cases
                                break;
                            case OPTION_2_OF_3:
                                player.setPortalSelected(1);
                                player.getPacketSender().sendInterfaceRemoval();
                                //TODO: Maybe something more in these cases
                                break;
                            case OPTION_3_OF_3:
                                player.setPortalSelected(0);
                                player.getPacketSender().sendInterfaceRemoval();
                                //TODO: Maybe something more in these cases
                                break;
                        }
                    }
                });
        }
        return null;
    }
}