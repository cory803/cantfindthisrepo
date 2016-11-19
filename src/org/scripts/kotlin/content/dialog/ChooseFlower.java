package org.scripts.kotlin.content.dialog;

import com.chaos.model.Position;
import com.chaos.model.options.fiveoption.FiveOption;
import com.chaos.model.options.threeoption.ThreeOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.Gambling;
import com.chaos.world.content.transportation.TeleportHandler;
import com.chaos.world.entity.impl.player.Player;

public class ChooseFlower extends Dialog {

    public Dialog dialog = this;

    public ChooseFlower(Player player, int state) {
        super(player);
        setEndState(1);
        setState(state);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new FiveOption(
                        "Pastel",
                        "Red",
                        "Blue",
                        "Yellow",
                        "Next page ->") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_5:
                                player.getPacketSender().sendInterfaceRemoval();
                                Gambling.plantSeed(player, Gambling.FlowersData.PASTEL_FLOWERS);
                                break;
                            case OPTION_2_OF_5:
                                player.getPacketSender().sendInterfaceRemoval();
                                Gambling.plantSeed(player, Gambling.FlowersData.RED_FLOWERS);
                                break;
                            case OPTION_3_OF_5:
                                player.getPacketSender().sendInterfaceRemoval();
                                Gambling.plantSeed(player, Gambling.FlowersData.BLUE_FLOWERS);
                                break;
                            case OPTION_4_OF_5:
                                player.getPacketSender().sendInterfaceRemoval();
                                Gambling.plantSeed(player, Gambling.FlowersData.YELLOW_FLOWERS);
                                break;
                            case OPTION_5_OF_5:
                                player.getDialog().sendDialog(new ChooseFlower(player, 1));
                                break;
                        }
                    }
                });
            case 1:
                return Dialog.createOption(new ThreeOption(
                        "Purple",
                        "Orange",
                        "<- Previous") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_3:
                                player.getPacketSender().sendInterfaceRemoval();
                                Gambling.plantSeed(player, Gambling.FlowersData.PURPLE_FLOWERS);
                                break;
                            case OPTION_2_OF_3:
                                player.getPacketSender().sendInterfaceRemoval();
                                Gambling.plantSeed(player, Gambling.FlowersData.ORANGE_FLOWERS);
                                break;
                            case OPTION_3_OF_3:
                                player.getDialog().sendDialog(new ChooseFlower(player, 0));
                                break;
                        }
                    }
                });
            }
        return null;
    }
}
