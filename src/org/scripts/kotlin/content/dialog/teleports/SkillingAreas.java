package org.scripts.kotlin.content.dialog.teleports;

import com.chaos.model.Position;
import com.chaos.model.Skill;
import com.chaos.model.options.fiveoption.FiveOption;
import com.chaos.model.options.fouroption.FourOption;
import com.chaos.model.options.threeoption.ThreeOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.transportation.TeleportHandler;
import com.chaos.world.content.transportation.TeleportType;
import com.chaos.world.entity.impl.player.Player;

public class SkillingAreas extends Dialog {

    public Dialog dialog = this;

    public SkillingAreas(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new FiveOption(
                        "Catherby",
                        "Training Grounds",
                        "Neitiznot",
                        "Resource Area @bla@(@red@Wild@bla@)",
                        "Next Page") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2809, 3435, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_2_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2517, 3360, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_3_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2336, 3803, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_4_OF_5:
                                player.getDialog().sendDialog(new ResourceArea(player));
                                break;
                            case OPTION_5_OF_5:
                                setState(1);
                                player.getDialog().sendDialog(dialog);
                                break;
                        }
                    }
                });
            case 1:
                return Dialog.createOption(new ThreeOption(
                        "Agility Courses",
                        "Hunter",
                        "Previous Page") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_3:
                                setState(2);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_2_OF_3:
                                setState(3);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_3_OF_3:
                                setState(0);
                                player.getDialog().sendDialog(dialog);
                                break;
                        }
                    }
                });
            case 2:
                return Dialog.createOption(new FourOption(
                        "Gnome Agility Course",
                        "Barbarian Course",
                        "Wilderness Course (@red@Wild@bla@)",
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_4:
                                TeleportHandler.teleportPlayer(player, new Position(2474, 3438, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_2_OF_4:
                                if(player.getSkillManager().getMaxLevel(Skill.AGILITY) < 35) {
                                    player.getPacketSender().sendInterfaceRemoval();
                                    player.getPacketSender().sendMessage("You need an Agility level of at least level 35 to use this course.");
                                    return;
                                }
                                TeleportHandler.teleportPlayer(player, new Position(2552, 3556, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_3_OF_4:
                                if(player.getSkillManager().getMaxLevel(Skill.AGILITY) < 55) {
                                    player.getPacketSender().sendInterfaceRemoval();
                                    player.getPacketSender().sendMessage("You need an Agility level of at least level 55 to use this course.");
                                    return;
                                }
                                TeleportHandler.teleportPlayer(player, new Position(2998, 3914), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_4_OF_4:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
            case 3:
                return Dialog.createOption(new ThreeOption(
                        "Puro Puro",
                        "Karamja",
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_3:
                                TeleportHandler.teleportPlayer(player, new Position(2589, 4319), TeleportType.PURO_PURO);
                                break;
                            case OPTION_2_OF_3:
                                player.getPacketSender().sendInterfaceRemoval();
                                if(player.getSkillManager().getCurrentLevel(Skill.HUNTER) < 23) {
                                    player.getPacketSender().sendMessage("You need a Hunter level of at least 23 to visit the hunting area.");
                                    return;
                                }
                                TeleportHandler.teleportPlayer(player, new Position(2922, 2885), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_3_OF_3:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
            }
        return null;
    }
}
