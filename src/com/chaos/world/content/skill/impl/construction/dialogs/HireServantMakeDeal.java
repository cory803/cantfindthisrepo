package com.chaos.world.content.skill.impl.construction.dialogs;

import com.chaos.model.Skill;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.skill.impl.construction.ConstructionData;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;

public class HireServantMakeDeal extends Dialog {

    int npc;

    public HireServantMakeDeal(Player player, int npc) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Alright, how does " + ConstructionData.Butlers.forId(npc).getLoanCost() + "gp/8 services sound?.");
            case 1:
                return Dialog.createOption(new TwoOption(
                        "You're hired!",
                        "Never mind.") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                player.setServantItemFetch(((NPC)player.getInteractingEntity()).getId());
                                player.getDialog().sendDialog(new FinalServantDeal(player, true));
                                break;
                            case OPTION_2_OF_2:
                                player.getDialog().sendDialog(new FinalServantDeal(player, false));
                                break;
                        }
                    }
                });
        }
        return null;
    }
}