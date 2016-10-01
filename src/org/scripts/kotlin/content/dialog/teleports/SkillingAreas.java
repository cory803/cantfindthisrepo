package org.scripts.kotlin.content.dialog.teleports;

import com.chaos.model.Position;
import com.chaos.model.options.fiveoption.FiveOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.transportation.TeleportHandler;
import com.chaos.world.entity.impl.player.Player;

public class SkillingAreas extends Dialog {

    public Dialog dialog = this;

    public SkillingAreas(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new FiveOption(
                        "Agility Courses",
                        "Catherby",
                        "Training Grounds",
                        "Neitiznot",
                        "Resource Area (@red@Wild@bla@)") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_5:
                                setState(1);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_2_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2809, 3435, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_3_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2517, 3360, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_4_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2332, 3802, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_5_OF_5:
                                player.getDialog().sendDialog(new ResourceArea(player));
                                break;
                        }
                    }
                });
            case 1:
                return Dialog.createOption(new TwoOption(
                        "Barbarian Course",
                        "Gnome Agility Course") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_2:
                                TeleportHandler.teleportPlayer(player, new Position(2552, 3556, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_2_OF_2:
                                TeleportHandler.teleportPlayer(player, new Position(2474, 3438, 0), player.getSpellbook().getTeleportType());
                        }
                    }
                });
            }
        return null;
    }
}
