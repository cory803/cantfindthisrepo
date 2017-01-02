package org.scripts.kotlin.content.dialog;

import com.chaos.model.Position;
import com.chaos.model.options.fiveoption.FiveOption;
import com.chaos.model.options.fouroption.FourOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.lottery.Lottery;
import com.chaos.world.content.lottery.LotterySaving;
import com.chaos.world.content.transportation.TeleportHandler;
import com.chaos.world.content.transportation.TeleportType;
import com.chaos.world.entity.impl.player.Player;
import org.scripts.kotlin.content.dialog.npcs.Gambler;

public class UseLottery extends Dialog {

    public Dialog dialog = this;

    public UseLottery(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new TwoOption(
                        "Enter Lottery",
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                if(!LotterySaving.LOTTERY_ON) {
                                    player.getDialog().sendDialog(new Gambler(player, 6, 6));
                                    break;
                                }
                                if(player.getInventory().contains(13664)) {
                                    LotterySaving.lottery1.addOffer(new Lottery(player.getUsername()));
                                    player.getInventory().delete(13664, 1);
                                    player.getDialog().sendDialog(new Gambler(player, 4, 4));
                                    LotterySaving.log("enter", player.getUsername());
                                } else {
                                    player.getDialog().sendDialog(new Gambler(player, 3, 3));
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
