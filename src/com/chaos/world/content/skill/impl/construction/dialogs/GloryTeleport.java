package com.chaos.world.content.skill.impl.construction.dialogs;

import com.chaos.model.options.fouroption.FourOption;
import com.chaos.model.options.threeoption.ThreeOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.skill.impl.construction.Construction;
import com.chaos.world.entity.impl.player.Player;

public class GloryTeleport extends Dialog {

    public GloryTeleport(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new FourOption(
                        "Edgeville",
                        "Karamja",
                        "Draynor village",
                        "Al-kharid") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_4:

                                break;
                            case OPTION_2_OF_4:

                                break;
                            case OPTION_3_OF_4:

                                break;
                            case OPTION_4_OF_4:

                                break;
                        }
                    }
                });
        }
        return null;
    }
}