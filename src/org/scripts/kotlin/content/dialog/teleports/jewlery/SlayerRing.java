package org.scripts.kotlin.content.dialog.teleports.jewlery;

import com.chaos.model.Position;
import com.chaos.model.options.threeoption.ThreeOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.transportation.TeleportHandler;
import com.chaos.world.entity.impl.player.Player;

public class SlayerRing extends Dialog {

    public Dialog dialog = this;
    private Position pos;

    public SlayerRing(Player player, Position pos) {
        super(player);
        this.pos = pos;
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                getPlayer().setNpcClickId(getPlayer().getSlayer().getSlayerMaster().getNpcId());
                return Dialog.createNpc(DialogHandler.CALM, "Your task is somewhere inside of the @red@wilderness@bla@, would you still like me to teleport you?");
            case 1:
                return Dialog.createOption(new TwoOption(
                        "Teleport me to my task",
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_2:
                                TeleportHandler.teleportPlayer(player, pos, player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_2_OF_2:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
        }
        return null;
    }
}