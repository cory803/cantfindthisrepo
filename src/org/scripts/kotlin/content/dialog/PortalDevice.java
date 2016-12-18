package org.scripts.kotlin.content.dialog;

import com.chaos.model.Position;
import com.chaos.model.Skill;
import com.chaos.model.options.fiveoption.FiveOption;
import com.chaos.model.options.threeoption.ThreeOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.transportation.TeleportHandler;
import com.chaos.world.content.transportation.TeleportType;
import com.chaos.world.entity.impl.player.Player;

public class PortalDevice extends Dialog {

    public Dialog dialog = this;

    public PortalDevice(Player player, int state) {
        super(player);
        setEndState(0);
        setState(state);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new FiveOption(
                        "Frost Dragons",
                        "Scorpia @bla@(@red@Wild@bla@)",
                        "Vet'ion @bla@(@red@Wild@bla@)",
                        "Callisto @bla@(@red@Wild@bla@)",
                        "More") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_5:
                                player.getPacketSender().sendInterfaceRemoval();
                                TeleportHandler.teleportPlayer(player, new Position(2793, 3794, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_2_OF_5:
                                player.getPacketSender().sendInterfaceRemoval();
                                TeleportHandler.teleportPlayer(player, new Position(3136, 3715, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_3_OF_5:
                                player.getPacketSender().sendInterfaceRemoval();
                                TeleportHandler.teleportPlayer(player, new Position(3242, 3790, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_4_OF_5:
                                player.getPacketSender().sendInterfaceRemoval();
                                TeleportHandler.teleportPlayer(player, new Position(3184, 3668, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_5_OF_5:
                                player.getDialog().sendDialog(new PortalDevice(player, 1));
                                break;
                        }
                    }
                });
            case 1:
                setEndState(1);
                return Dialog.createOption(new ThreeOption(
                        "Chaos Fanatic @bla@(@red@Wild@bla@)",
                        "Crazy Archaeologist @bla@(@red@Wild@bla@)",
                        "Venenatis @bla@(@red@Wild@bla@)") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_3:
                                player.getPacketSender().sendInterfaceRemoval();
                                TeleportHandler.teleportPlayer(player, new Position(2981, 3854, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_2_OF_3:
                                player.getPacketSender().sendInterfaceRemoval();
                                TeleportHandler.teleportPlayer(player, new Position(2978, 3702, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_3_OF_3:
                                player.getPacketSender().sendInterfaceRemoval();
                                TeleportHandler.teleportPlayer(player, new Position(3146, 3800, 0), TeleportType.NORMAL);
                        }
                    }
                });
        }
        return null;
    }
}
