package org.scripts.kotlin.content.dialog;

import com.runelive.model.options.threeoption.ThreeOption;
import com.runelive.model.player.dialog.Dialog;
import com.runelive.model.player.dialog.DialogMessage;
import com.runelive.world.entity.impl.player.Player;

public class Report extends Dialog {

    public Dialog dialog = this;

    public Report(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new ThreeOption(
                        "Report a Player",
                        "Report a Staff Member",
                        "Report a Bug") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_3:
                                player.getPacketSender().sendString(1, "www.rune.live/forums/index.php?/support/");
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                            case OPTION_2_OF_3:
                                player.getPacketSender().sendString(1, "www.rune.live/forums/index.php?/support/");
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                            case OPTION_3_OF_3:
                                player.getPacketSender().sendString(1, "www.rune.live/forums/index.php?/support/");
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
        }
        return null;
    }
}
