package org.scripts.kotlin.content.dialog.npcs;

import com.chaos.model.Position;
import com.chaos.model.Skill;
import com.chaos.model.container.impl.Shop;
import com.chaos.model.options.threeoption.ThreeOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.transportation.TeleportHandler;
import com.chaos.world.entity.impl.player.Player;

public class Sailor extends Dialog {

    public Dialog dialog = this;

    public Sailor(Player player, int state) {
        super(player);
        setState(state);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Arrrg the captain is ready to go yee timbers! Where do you want to go?");
            case 1:
                return Dialog.createOption(new ThreeOption(
                        "Chaos Altar",
                        "Desert Pyramid",
                        "Lunar Isle") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_3:
                                TeleportHandler.teleportPlayer(player, new Position(2940, 3512, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_2_OF_3:
                                TeleportHandler.teleportPlayer(player, new Position(3233, 2919, 0), player.getSpellbook().getTeleportType());
                                player.getPacketSender().sendMessage("<col=ff0000>To change your spellbook, travel onto the south part of the pyramid.");
                                break;
                            case OPTION_3_OF_3:
                                TeleportHandler.teleportPlayer(player, new Position(2100, 3914, 0), player.getSpellbook().getTeleportType());
                                break;
                        }
                    }
                });
        }
        return null;
    }
}
