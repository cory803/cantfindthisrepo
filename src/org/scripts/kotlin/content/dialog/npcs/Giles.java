package org.scripts.kotlin.content.dialog.npcs;

import com.chaos.model.Skill;
import com.chaos.model.container.impl.Shop;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.entity.impl.player.Player;

public class Giles extends Dialog {

    public Dialog dialog = this;

    public Giles(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hello! Would you like to see what supplies I have for sale?");
            case 1:
            return Dialog.createOption(new TwoOption(
                    "Yes, show me what your selling.",
                    "No, I don't want to see.") {
                @Override
                public void execute(Player player, OptionType option) {
                    switch(option) {
                        case OPTION_1_OF_2:
                            Shop.ShopManager.getShops().get(3).open(player);
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

    public static class HealersQuickOption extends Dialog {

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
}
