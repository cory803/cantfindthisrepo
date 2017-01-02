package org.scripts.kotlin.content.dialog.npcs;

import com.chaos.model.Position;
import com.chaos.model.container.impl.Shop;
import com.chaos.model.options.fouroption.FourOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.transportation.TeleportHandler;
import com.chaos.world.entity.impl.player.Player;

public class Gambler extends Dialog {

    public Gambler(Player player, int state) {
        super(player);
        setState(state);
        setEndState(2);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hmmm, be careful with these sneaky gamblers...");
            case 1:
                return Dialog.createOption(new FourOption(
                        "What is the Lottery?",
                        "I want to enter the $50 Lottery.",
                        "How many tickets do I have in the Lottery?",
                        "How many tickets are in total are in the Lottery?") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_4:
                                player.getDialog().sendDialog(new Gambler(player, 2));
                                break;
                            case OPTION_2_OF_4:
                                TeleportHandler.teleportPlayer(player, new Position(2911, 4832, 0), player.getSpellbook().getTeleportType());
                                break;
                        }
                    }
                });
            case 2:
                setEndState(3);
                return Dialog.createNpc(DialogHandler.CALM, "The Lottery is a pot of $50 that is drawn every 7 days.");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM, "You can get lottery tickets by killing monsters, or other various things in Chaos.");
            }
        return null;
    }
}
