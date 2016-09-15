package org.scripts.kotlin.content.dialog.teleports;

import com.chaos.model.Position;
import com.chaos.model.Skill;
import com.chaos.model.options.fiveoption.FiveOption;
import com.chaos.model.options.threeoption.ThreeOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.transportation.TeleportHandler;
import com.chaos.world.entity.impl.player.Player;

public class Minigames extends Dialog {

    public Dialog dialog = this;

    public Minigames(Player player) {
        super(player);
        setEndState(2);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new FiveOption(
                        "Warriors Guild",
                        "Pest Control",
                        "Duel Arena",
                        "Barrows",
                        "Next Page") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_5:
                                if(player.getSkillManager().getCurrentLevel(Skill.ATTACK) + player.getSkillManager().getCurrentLevel(Skill.STRENGTH) < 130) {
                                    player.getPacketSender().sendMessage("A true warrior requires a total of 130 Strength & Attack.");
                                    player.getPacketSender().sendInterfaceRemoval();
                                    return;
                                }
                                TeleportHandler.teleportPlayer(player, new Position(2873, 3546, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_2_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2658, 2659, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_3_OF_5:
                                setState(2);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_4_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(3565, 3313, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_5_OF_5:
                                setState(1);
                                player.getDialog().sendDialog(dialog);
                                break;
                        }
                    }
                });
            case 1:
                return Dialog.createOption(new TwoOption(
                        "TzHaar Fight Cave",
                        "TzHaar Fight Pit") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_2:
                                TeleportHandler.teleportPlayer(player, new Position(2480, 5166, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_2_OF_2:
                                TeleportHandler.teleportPlayer(player, new Position(2399, 5177, 0), player.getSpellbook().getTeleportType());
                                break;
                        }
                    }
                });
            case 2:
                return Dialog.createOption(new ThreeOption(
                        "Entrance",
                        "Challenge Arena",
                        "Watch zone") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_3:
                                TeleportHandler.teleportPlayer(player, new Position(3316, 3234, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_2_OF_3:
                                TeleportHandler.teleportPlayer(player, new Position(3367, 3266, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_3_OF_3:
                                TeleportHandler.teleportPlayer(player, new Position(3345, 3260, 0), player.getSpellbook().getTeleportType());
                                break;
                        }
                    }
                });
            }
        return null;
    }
}
