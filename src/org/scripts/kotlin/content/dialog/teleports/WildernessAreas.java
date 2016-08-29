package org.scripts.kotlin.content.dialog.teleports;

import com.chaos.model.Position;
import com.chaos.model.options.fiveoption.FiveOption;
import com.chaos.model.options.threeoption.ThreeOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.transportation.TeleportHandler;
import com.chaos.world.entity.impl.player.Player;

public class WildernessAreas extends Dialog {

    public Dialog dialog = this;

    public WildernessAreas(Player player) {
        super(player);
        setEndState(2);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new FiveOption(
                        "Varrock Wilderness",
                        "West Wilderness",
                        "Chaos Temple",
                        "East Wilderness",
                        "Mage Bank") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(3244, 3512, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_2_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2979, 3600, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_3_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(3236, 3635, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_4_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(3369, 3695, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_5_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2539, 4715, 0), player.getSpellbook().getTeleportType());
                                break;
                        }
                    }
                });
            }
        return null;
    }
}
