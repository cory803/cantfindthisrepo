package com.chaos.world.content.skill.impl.construction.dialogs;

import com.chaos.model.Skill;
import com.chaos.model.options.threeoption.ThreeOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.skill.impl.construction.Construction;
import com.chaos.world.content.skill.impl.construction.ConstructionData;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;

public class Servant extends Dialog {

    public Servant(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "How can I be of service?");
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
                                    player.getDialog().sendDialog(new HireServantDecline(player, b.getNpcId(), "lvlreq"));
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
                                    player.getDialog().sendDialog(new HireServantDecline(player, b.getNpcId(), "room"));
                                    return;
                                }
                                player.getDialog().sendDialog(new HireServantMakeDeal(player, b.getNpcId()));
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