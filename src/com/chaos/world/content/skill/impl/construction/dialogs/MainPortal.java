package com.chaos.world.content.skill.impl.construction.dialogs;

import com.chaos.model.container.impl.Shop;
import com.chaos.model.options.threeoption.ThreeOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.skill.impl.construction.Construction;
import com.chaos.world.entity.impl.player.Player;

public class MainPortal extends Dialog {

    public MainPortal(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new ThreeOption(
                        "Enter your house",
                        "Enter your house (building mode)",
                        "Enter friend's house") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_3:
                                player.getPacketSender().sendInterfaceRemoval();
                                if (player.getHouseRooms()[0][0][0] == null) {
                                    player.getPacketSender().sendMessage("You don't own a house. Talk to the House Agent to buy one.");
                                    return;
                                }
                                player.setIsBuildingMode(false);
                                Construction.enterHouse(player, player, false, true);
                                break;
                            case OPTION_2_OF_3:
                                player.getPacketSender().sendInterfaceRemoval();
                                if (player.getHouseRooms()[0][0][0] == null) {
                                    player.getPacketSender().sendMessage("You don't own a house. Talk to the House Agent to buy one.");
                                    return;
                                }
                                player.setIsBuildingMode(true);
                                Construction.enterHouse(player, player, true, true);
                                break;
                            case OPTION_3_OF_3:
                                player.getPacketSender().commandFrame(2);
                                break;
                        }
                    }
                });
        }
        return null;
    }
}