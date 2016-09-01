package org.scripts.kotlin.content.dialog.teleports;

import com.chaos.model.Position;
import com.chaos.model.options.fiveoption.FiveOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.transportation.TeleportHandler;
import com.chaos.world.entity.impl.player.Player;

public class Dungeons extends Dialog {

    public Dialog dialog = this;

    public Dungeons(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new FiveOption(
                        "Edgeville Dungeon",
                        "Taverly Dungeon",
                        "Brimhaven Dungeon",
                        "Ancient Cavern",
                        "Strykewyrm Cavern") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(3097, 9882, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_2_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2884, 9799, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_3_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2697, 9564, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_4_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(1748, 5325, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_5_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2731, 5095, 0), player.getSpellbook().getTeleportType());
                                break;
                        }
                    }
                });
            }
        return null;
    }
}