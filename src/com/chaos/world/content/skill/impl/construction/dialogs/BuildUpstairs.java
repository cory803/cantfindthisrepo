package com.chaos.world.content.skill.impl.construction.dialogs;

import com.chaos.model.options.fouroption.FourOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.skill.impl.construction.Construction;
import com.chaos.world.content.skill.impl.construction.ConstructionData;
import com.chaos.world.content.skill.impl.construction.Room;
import com.chaos.world.entity.impl.player.Player;

public class BuildUpstairs extends Dialog {

    public BuildUpstairs(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createStatement(DialogHandler.CALM,
                        "These stairs lead nowhere.",
                        "Would you like to build a room above this room?");
            case 1:
                return Dialog.createOption(new TwoOption(
                        "Yes",
                        "No") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                int myTiles[] = Construction.getMyChunk(player);
                                Room room = player.getRegionInstance().getOwner().getHouseRooms()[1][myTiles[0] - 1][myTiles[1] - 1];
                                Room room_1 = player.getRegionInstance().getOwner().getHouseRooms()[0][myTiles[0] - 1][myTiles[1] - 1];
                                if (room != null) {
                                    player.getPacketSender().sendMessage("You did something retarded and now there is a room above you for some reason.");
                                    player.getPacketSender().sendInterfaceRemoval();
                                } else {
                                    Construction.createRoom(room_1.getType() == ConstructionData.SKILL_ROOM ? ConstructionData.SKILL_HALL_DOWN : ConstructionData.QUEST_HALL_DOWN, player, 100);
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