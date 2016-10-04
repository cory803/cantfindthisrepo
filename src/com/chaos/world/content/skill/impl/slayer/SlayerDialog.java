package com.chaos.world.content.skill.impl.slayer;

import com.chaos.model.definitions.ItemDefinition;
import com.chaos.model.definitions.NpcDefinition;
import com.chaos.model.options.fouroption.FourOption;
import com.chaos.model.options.threeoption.ThreeOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.skill.impl.slayer.SlayerMasters;
import com.chaos.world.entity.impl.player.Player;

public class SlayerDialog extends Dialog {

    public Dialog dialog = this;
    public SlayerMasters slayerMaster;

    public SlayerDialog(Player player, int state) {
        super(player);
        setState(state);
        setEndState(20);
    }

    @Override
    public DialogMessage getMessage() {
        slayerMaster = SlayerMasters.forNpcId(getPlayer().getNpcClickId());
        switch (getState()) {
            case 0:
                if(getPlayer().getSlayer().hasMasterRequirements(slayerMaster)) {
                    return Dialog.createNpc(DialogHandler.CALM, "Hello! How may I assist you today?");
                } else {
                    return Dialog.createNpc(DialogHandler.CALM, "Hmm a creature of a interesting kind... Come back when you have atleast "+slayerMaster.getCombatLevelRequirement()+" combat!");
                }
            case 1:
                if(getPlayer().getSlayer().getSlayerTask() != null) {
                    return Dialog.createOption(new FourOption(
                            "I need a slayer assignment.",
                            "How many slayer kills do I have left?",
                            "Do you have any advice for my current task?",
                            "Can you give me an easier task please.") {
                        @Override
                        public void execute(Player player, OptionType option) {
                            switch (option) {
                                case OPTION_1_OF_4:
                                    player.getSlayer().assignSlayerTask(slayerMaster, false);
                                    break;
                                case OPTION_2_OF_4:
                                    player.getSlayer().checkAmountLeft();
                                    break;
                                case OPTION_3_OF_4:
                                    player.getSlayer().checkRecommendations();
                                    break;
                                case OPTION_4_OF_4:
                                    player.getSlayer().giveEasierTask(slayerMaster);
                                    break;
                            }
                        }
                    });
                } else {
                    return Dialog.createOption(new TwoOption (
                            "I need a slayer assignment.",
                            "I don't need anything.") {
                        @Override
                        public void execute(Player player, OptionType option) {
                            switch (option) {
                                case OPTION_1_OF_2:
                                    player.getSlayer().assignSlayerTask(slayerMaster, false);
                                    break;
                                case OPTION_2_OF_2:
                                    player.getPacketSender().sendInterfaceRemoval();
                                    break;
                            }
                        }
                    });
                }
            case 2:
                setEndState(2);
                return Dialog.createNpc(DialogHandler.CALM, "You are already assigned to kill "+getPlayer().getSlayer().getAmountLeft()+" "+getPlayer().getSlayer().getTaskName()+"s! Come back when you are done for a new assignment.");
            case 3:
                setEndState(4);
                return Dialog.createNpc(DialogHandler.CALM, "You have been assigned to kill "+getPlayer().getSlayer().getAmountLeft()+" "+getPlayer().getSlayer().getTaskName()+"s.");
            case 4:
                return Dialog.createPlayer(DialogHandler.CALM, "Awesome I will get started!");
            case 5:
                setEndState(5);
                return Dialog.createNpc(DialogHandler.CALM, "You have "+getPlayer().getSlayer().getAmountLeft()+" "+getPlayer().getSlayer().getTaskName()+"s left to kill on your current slayer task.");
            case 6:
                setEndState(6);
                if(getPlayer().getSlayer().getSlayerTask().getEquipmentId() == -1) {
                    return Dialog.createNpc(DialogHandler.CALM, "I don't have any advice, good luck!");
                } else {
                    return Dialog.createNpc(DialogHandler.CALM, "Hmm, a wise one once told me something about "+ ItemDefinition.forId(getPlayer().getSlayer().getSlayerTask().getEquipmentId()).getName()+"...");
                }
            case 7:
                 setEndState(7);
                 return Dialog.createNpc(DialogHandler.CALM, "I can't offer you a easier slayer task. Sorry!");
            case 8:
                 setEndState(8);
                 getPlayer().getPacketSender().sendMessage("Congratulations! You have completed "+getPlayer().getSlayer().getSlayerStreak()+" tasks in a row and gained "+getPlayer().getSlayer().getPointsToGive()+" Slayer Points.");
                 return Dialog.createNpc(DialogHandler.CALM, "Congratulations! You have completed "+getPlayer().getSlayer().getSlayerStreak()+" tasks in a row and gained "+getPlayer().getSlayer().getPointsToGive()+" Slayer Points.");
        }
        return null;
    }
}
