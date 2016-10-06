package com.chaos.world.content.skill.impl.slayer;

import com.chaos.model.Direction;
import com.chaos.model.Position;
import com.chaos.model.options.fiveoption.FiveOption;
import com.chaos.model.options.threeoption.ThreeOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.transportation.TeleportHandler;
import com.chaos.world.content.transportation.TeleportType;
import com.chaos.world.entity.impl.player.Player;

public class SlayerTeleports extends Dialog {

    public Dialog dialog = this;

    public SlayerTeleports(Player player) {
        super(player);
        setEndState(2);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                    return Dialog.createOption(new FiveOption(
                            "Turadel (Level 3 Minimum)",
                            "Mazchna (Level 20 Minimum)",
                            "Vannaka (Level 40 Minimum)",
                            "Chaeldar (Level 70 Minimum)",
                            "Next Page") {
                        @Override
                        public void execute(Player player, OptionType option) {
                            switch (option) {
                                case OPTION_1_OF_5:
                                    TeleportHandler.teleportPlayer(player, new Position(2929, 3536, 0), TeleportType.NORMAL);
                                    break;
                                case OPTION_2_OF_5:
                                    TeleportHandler.teleportPlayer(player, new Position(3505, 3501, 0), TeleportType.NORMAL);
                                    break;
                                case OPTION_3_OF_5:
                                    TeleportHandler.teleportPlayer(player, new Position(3146, 9914, 0), TeleportType.NORMAL);
                                    break;
                                case OPTION_4_OF_5:
                                    TeleportHandler.teleportPlayer(player, new Position(2446, 4429, 0), TeleportType.NORMAL);
                                    player.setDirection(Direction.NORTH);
                                    break;
                                case OPTION_5_OF_5:
                                    setState(1);
                                    player.getDialog().sendDialog(dialog);
                                    break;
                            }
                        }
                    });
                    case 1:
                        return Dialog.createOption(new ThreeOption(
                                "Nieve (Level 85 Minimum)",
                                "Duradel (Level 123 Minimum)",
                                "Previous Page") {
                            @Override
                            public void execute(Player player, OptionType option) {
                                switch (option) {
                                    case OPTION_1_OF_3:
                                        TeleportHandler.teleportPlayer(player, new Position(2436, 3421, 0), TeleportType.NORMAL);
                                        break;
                                    case OPTION_2_OF_3:
                                        TeleportHandler.teleportPlayer(player, new Position(2869, 2982, 0), TeleportType.NORMAL);
                                        player.setDirection(Direction.SOUTH);
                                        break;
                                    case OPTION_3_OF_3:
                                        setState(0);
                                        player.getDialog().sendDialog(dialog);
                                        break;
                                }
                            }
                });
        }
        return null;
    }
}
