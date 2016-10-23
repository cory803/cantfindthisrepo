package org.scripts.kotlin.content.dialog.npcs;

import com.chaos.model.Position;
import com.chaos.model.container.impl.Shop;
import com.chaos.model.options.threeoption.ThreeOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.skill.impl.crafting.Tanning;
import com.chaos.world.content.transportation.TeleportHandler;
import com.chaos.world.content.transportation.TeleportType;
import com.chaos.world.entity.impl.player.Player;

public class MasterCrafter extends Dialog {

    public MasterCrafter(Player player, int state) {
        super(player);
        setState(2);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "A true crafter is one with a humble mind...");
            case 1:
                return Dialog.createPlayer(DialogHandler.CALM, "That's deep.");
            case 2:
                return Dialog.createNpc(DialogHandler.CALM, "What can I help you with today?");
            case 3:
                return Dialog.createOption(new TwoOption(
                        "I need to tan hides.",
                        "Nevermind, I don't need anything.") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                Tanning.selectionInterface(player);
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
