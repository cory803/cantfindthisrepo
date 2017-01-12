package org.scripts.kotlin.content.dialog.npcs;

import com.runelive.model.container.impl.Shop;
import com.runelive.model.options.fiveoption.FiveOption;
import com.runelive.model.player.dialog.Dialog;
import com.runelive.model.player.dialog.DialogHandler;
import com.runelive.model.player.dialog.DialogMessage;
import com.runelive.world.entity.impl.player.Player;

public class WiseOldMan extends Dialog {

    public WiseOldMan(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hello! Would you like to see what skill capes I have for sale?");
            case 1:
            return Dialog.createOption(new FiveOption(
                    "Yes, I want to buy skillcapes.",
                    "Yes, I want to buy trimmed skillcapes.",
                    "Yes, I want to buy master skillcapes.",
                    "Yes, I want to buy skillcape hoods",
                    "No, I don't want to buy anything."
                    ) {
                @Override
                public void execute(Player player, OptionType option) {
                    switch(option) {
                        case OPTION_1_OF_5:
                            Shop.ShopManager.getShops().get(9).open(player);
                            break;
                        case OPTION_2_OF_5:
                            Shop.ShopManager.getShops().get(10).open(player);
                            break;
                        case OPTION_3_OF_5:
                            Shop.ShopManager.getShops().get(11).open(player);
                            break;
                        case OPTION_4_OF_5:
                            Shop.ShopManager.getShops().get(8).open(player);
                            break;
                        case OPTION_5_OF_5:
                            player.getPacketSender().sendInterfaceRemoval();
                            break;
                    }
                }
            });
            }
        return null;
    }
}
