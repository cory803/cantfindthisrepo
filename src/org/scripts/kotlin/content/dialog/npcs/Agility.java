package org.scripts.kotlin.content.dialog.npcs;

import com.chaos.model.container.impl.Shop;
import com.chaos.model.options.threeoption.threeOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.entity.impl.player.Player;

public class Agility extends Dialog {

    public Dialog dialog = this;

    public Agility(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hello! What can I do for you?");
            case 1:
            return Dialog.createOption(new ThreeOption(
                    "I want to buy experience",
                    "I want to buy equipment",
                    "Cancel") {
                @Override
                public void execute(Player player, OptionType option) {
                    switch(option) {
                        case OPTION_1_OF_3:
                            DialogueManager.start(player, AgilityTicketExchange.getDialogue(player));
                            break;
                        case OPTION_2_OF_3:
                            Shop.ShopManager.getShops().get(28).open(player);
                            break;
                        case OPTION_3_OF_3:
                            player.getPacketSender().sendInterfaceRemoval();
                            break;
                    }
                }
            });
            }
        return null;
    }
}
