package org.scripts.kotlin.content.dialog.npcs;

import com.runelive.model.Position;
import com.runelive.model.container.impl.Shop;
import com.runelive.model.options.twooption.TwoOption;
import com.runelive.model.player.dialog.Dialog;
import com.runelive.model.player.dialog.DialogHandler;
import com.runelive.model.player.dialog.DialogMessage;
import com.runelive.world.content.transportation.TeleportHandler;
import com.runelive.world.entity.impl.player.Player;

public class Aubury extends Dialog {

    public Dialog dialog = this;

    public Aubury(Player player) {
        super(player);
        setEndState(3);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "A magical sense of an imagination can be a great discovery for an archaeologist.");
            case 1:
                return Dialog.createPlayer(DialogHandler.CALM, "What?");
            case 2:
                return Dialog.createNpc(DialogHandler.CALM, "How may I assist you today?");
            case 3:
                return Dialog.createOption(new TwoOption(
                        "Show me what you have for sale.",
                        "Bring me to the essence mine.") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                Shop.ShopManager.getShops().get(29).open(player);
                                break;
                            case OPTION_2_OF_2:
                                TeleportHandler.teleportPlayer(player, new Position(2911, 4832, 0), player.getSpellbook().getTeleportType());
                                break;
                        }
                    }
                });
            }
        return null;
    }
}
