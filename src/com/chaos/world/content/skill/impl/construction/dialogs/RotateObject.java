package com.chaos.world.content.skill.impl.construction.dialogs;

import com.chaos.model.options.fouroption.FourOption;
import com.chaos.model.options.threeoption.ThreeOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.skill.impl.construction.Construction;
import com.chaos.world.entity.impl.player.Player;

public class RotateObject extends Dialog {

    public RotateObject(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new ThreeOption(
                        "Rotate clockwise",
                        "Rotate counter-clockwise",
                        "Remove") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_3:
                                Construction.rotateRoom(0, player);
                                break;
                            case OPTION_2_OF_3:
                                Construction.rotateRoom(1, player);
                                break;
                            case OPTION_3_OF_3:
                                if (player.getPosition().getZ() == 0 && !player.inConstructionDungeon())
                                    Construction.deleteRoom(player, 0);
                                if (player.inConstructionDungeon())
                                    Construction.deleteRoom(player, 4);
                                if (player.getPosition().getZ() == 1) {
                                    Construction.deleteRoom(player, 1);
                                }
                                break;
                        }
                    }
                });
        }
        return null;
    }
}