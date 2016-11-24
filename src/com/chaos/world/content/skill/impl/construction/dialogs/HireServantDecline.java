package com.chaos.world.content.skill.impl.construction.dialogs;

import com.chaos.model.Skill;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.skill.impl.construction.ConstructionData;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;

public class HireServantDecline extends Dialog {

    String errorReq;
    int npc;

    public HireServantDecline(Player player, int npc, String errorReq) {
        super(player);
        setEndState(1);
        this.errorReq = errorReq;
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                if (errorReq.equalsIgnoreCase("room")) {
                    return Dialog.createNpc(DialogHandler.CALM, "You don't expect me to sleep on the floor do you? Come back when you have built at least 2 bedrooms in your house.");
                } else {
                    return Dialog.createNpc(DialogHandler.CALM, "Your house is not worth serving! Come back when you have a Construction level of at least " + ConstructionData.Butlers.forId(npc).getConsLevel() + ".");
                }
            case 1:
                return Dialog.createOption(new TwoOption(
                        "I'd like to hire you!",
                        "Never mind.") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                ConstructionData.Butlers b = ConstructionData.Butlers.forId(((NPC) player.getInteractingEntity()).getId());
                                if (player.getSkillManager().getCurrentLevel(Skill.CONSTRUCTION) < b.getConsLevel()) {
                                    //DialogueManager.start(p, ConstructionDialogues.hireServantDeclineDialogue(p, b.getNpcId(), "lvlreq"));
                                    return;
                                }
                                int roomCount = 0;
                                for (int z = 0; z < player.getHouseRooms().length; z++) {
                                    for (int x = 0; x < player.getHouseRooms()[z].length; x++) {
                                        for (int y = 0; y < player.getHouseRooms()[z][x].length; y++) {
                                            if (player.getHouseRooms()[z][x][y] == null) {
                                                continue;
                                            }
                                            if (player.getHouseRooms()[z][x][y].getType() == ConstructionData.BEDROOM)
                                                roomCount++;
                                            if (roomCount > 1)
                                                break;
                                        }
                                    }
                                }
                                if (roomCount < 2) {
                                    //DialogueManager.start(p, ConstructionDialogues.hireServantDeclineDialogue(p, b.getNpcId(), "room"));
                                    return;
                                }
                                //DialogueManager.start(p, ConstructionDialogues.hireServantMakeDealDialogue(p, b.getNpcId()));
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