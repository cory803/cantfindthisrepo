package org.scripts.kotlin.content.dialog.teleports;

import com.chaos.model.Position;
import com.chaos.model.Skill;
import com.chaos.model.options.fiveoption.FiveOption;
import com.chaos.model.options.fouroption.FourOption;
import com.chaos.model.options.threeoption.ThreeOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.transportation.TeleportHandler;
import com.chaos.world.content.transportation.TeleportType;
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
                return Dialog.createOption(new ThreeOption(
                        "Catherby",
                        "Skilling Zone",
                        //"Skilling Zone (High)",
                        "Resource Area @bla@(@red@Wild@bla@)") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_3:
                                TeleportHandler.teleportPlayer(player, new Position(2809, 3435, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_2_OF_3:
                                TeleportHandler.teleportPlayer(player, new Position(2802, 2785, 0), player.getSpellbook().getTeleportType());
                                break;
                            //case OPTION_3_OF_4:
                                //TeleportHandler.teleportPlayer(player, new Position(2852, 2960, 0), player.getSpellbook().getTeleportType());
                                //break;
                            case OPTION_3_OF_3:
                                player.setNpcClickId(747);
                                player.getDialog().sendDialog(new ResourceArea(player));
                                break;
                        }
                    }
                });
        }
        return null;
    }
}
