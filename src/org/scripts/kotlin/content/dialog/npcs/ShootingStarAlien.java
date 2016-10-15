package org.scripts.kotlin.content.dialog.npcs;

import com.chaos.model.Item;
import com.chaos.model.container.impl.Shop;
import com.chaos.model.options.fouroption.FourOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.GameMode;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.diversions.hourly.ShootingStar;
import com.chaos.world.entity.impl.player.Player;

import java.util.List;

public class ShootingStarAlien extends Dialog {

    public Dialog dialog = this;

    public ShootingStarAlien(Player player) {
        super(player);
        setEndState(3);
    }

    @Override
    public DialogMessage getMessage() {
        final List<Item> rewards = ShootingStar.getRewards(getPlayer());
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.JOYOUS_TALK, "Is that stardust? Will you be willing to exchange it for some rewards I have collected?");
            case 1:
            return Dialog.createOption(new TwoOption(
                    "Yes",
                    "No") {
                @Override
                public void execute(Player player, OptionType option) {
                    switch(option) {
                        case OPTION_1_OF_2:
                            int amount = player.getInventory().getAmount(ShootingStar.STARDUST);
                            if (amount >= 20) {
                                player.getInventory().delete(new Item(ShootingStar.STARDUST, amount));
                                for (Item item : rewards) {
                                    player.getInventory().add(item);
                                }
                                setState(3);
                                setEndState(3);
                                player.getDialog().sendDialog(dialog);
                            } else {
                                player.getPacketSender().sendMessage("You need at least 20 stardusts to claim a reward.");
                                player.getPacketSender().sendInterfaceRemoval();
                            }
                            break;
                        case OPTION_2_OF_2:
                            setState(2);
                            setEndState(2);
                            player.getDialog().sendDialog(dialog);
                            break;
                    }
                }
            });
            case 2:
                return Dialog.createNpc(DialogHandler.GLANCE_DOWN, "I could always use stardust; come back if you change your mind human.");
            case 3:
                return Dialog.createNpc(DialogHandler.HAPPY_AND_SURPRISED_TALK, "I misjudged you humans. You're not that bad!");
        }
        return null;
    }
}
