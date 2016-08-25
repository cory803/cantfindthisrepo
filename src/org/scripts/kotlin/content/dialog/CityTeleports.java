package org.scripts.kotlin.content.dialog;

import com.chaos.model.Position;
import com.chaos.model.Skill;
import com.chaos.model.options.fiveoption.FiveOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.transportation.TeleportHandler;
import com.chaos.world.content.transportation.TeleportType;
import com.chaos.world.entity.impl.player.Player;

public class CityTeleports extends Dialog {

    public Dialog dialog = this;

    public CityTeleports(Player player) {
        super(player);
        setEndState(2);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new FiveOption(
                        "Varrock",
                        "Falador",
                        "Lumbridge",
                        "Draynor",
                        "Next >>") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(3213, 3430, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_2_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2965, 3378, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_3_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2757, 3477, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_4_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(3104, 3249, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_5_OF_5:
                                setState(1);
                                player.getDialog().sendDialog(dialog);
                                break;
                        }
                    }
                });
            case 1:
                return Dialog.createOption(new FiveOption(
                        "Ardougne",
                        "Camelot",
                        "Yanille",
                        "Catherby",
                        "Next >>") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2662, 3306, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_2_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2757, 3477, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_3_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2605, 3097, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_4_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2813, 3447, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_5_OF_5:
                                setState(2);
                                player.getDialog().sendDialog(dialog);
                                break;
                        }
                    }
                });
            case 2:
                return Dialog.createOption(new FiveOption(
                        "Al Kharid",
                        "Burthorpe",
                        "Canifis",
                        "Entrana",
                        "Shilo Village") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(3276, 3166, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_2_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2899, 3546, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_3_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(3494, 3483, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_4_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2827, 3344, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_5_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2852, 2960, 0), TeleportType.NORMAL);
                                break;
                        }
                    }
                });
            }
        return null;
    }
}
