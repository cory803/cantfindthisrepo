package org.scripts.kotlin.content.dialog.npcs;

import com.runelive.model.container.impl.Shop;
import com.runelive.model.options.twooption.TwoOption;
import com.runelive.model.player.GameMode;
import com.runelive.model.player.dialog.Dialog;
import com.runelive.model.player.dialog.DialogHandler;
import com.runelive.model.player.dialog.DialogMessage;
import com.runelive.world.entity.impl.player.Player;

public class David extends Dialog {

    public David(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hello! Would you like to see what skilling items I have for sale?");
            case 1:
            return Dialog.createOption(new TwoOption(
                    "Yes, show me what you have to offer!",
                    "No, I don't want to see.") {
                @Override
                public void execute(Player player, OptionType option) {
                    switch(option) {
                        case OPTION_1_OF_2:
                            if (player.getGameModeAssistant().getGameMode() == GameMode.IRONMAN) {
                                Shop.ShopManager.getShops().get(40).open(player);
                            } else {
                                Shop.ShopManager.getShops().get(30).open(player);
                            }
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
