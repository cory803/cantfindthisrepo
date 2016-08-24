package org.scripts.kotlin.content.dialog;

import com.chaos.model.Skill;
import com.chaos.model.player.GameMode;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.entity.impl.player.Player;

public class HealersQuickOption extends Dialog {

    public Dialog dialog = this;

    public HealersQuickOption(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                getPlayer().getSkillManager().setCurrentLevel(Skill.CONSTITUTION, (getPlayer().getSkillManager().getMaxLevel(Skill.CONSTITUTION)));
                if(getPlayer().getDonatorRights().isDonator()) {
                    if (getPlayer().getDonatorRights().ordinal() >= 2) {
                        getPlayer().getSkillManager().setCurrentLevel(Skill.PRAYER, (getPlayer().getSkillManager().getMaxLevel(Skill.PRAYER)));
                    }
                    if (getPlayer().getDonatorRights().ordinal() >= 3) {
                        for (Skill skill : Skill.values()) {
                            if (getPlayer().getSkillManager().getCurrentLevel(skill) < getPlayer().getSkillManager().getMaxLevel(skill)) {
                                getPlayer().getSkillManager().setCurrentLevel(skill, getPlayer().getSkillManager().getMaxLevel(skill));
                            }
                        }
                        getPlayer().getSkillManager().setCurrentLevel(Skill.PRAYER, (getPlayer().getSkillManager().getMaxLevel(Skill.PRAYER)));
                    }
                    if (getPlayer().getDonatorRights().ordinal() >= 4) {
                        if (getPlayer().getRunEnergy() < 100) {
                            getPlayer().setRunEnergy(100);
                        }
                    }
                    if (getPlayer().getDonatorRights().ordinal() >= 5) {
                        if(getPlayer().getSpecialPercentage() < 100) {
                            getPlayer().setSpecialPercentage(100);
                        }
                    }
                }
                return Dialog.createNpc(DialogHandler.CALM, "You have been healed!");
            }
        return null;
    }
}
