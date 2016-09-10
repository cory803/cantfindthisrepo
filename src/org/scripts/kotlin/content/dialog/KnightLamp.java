package org.scripts.kotlin.content.dialog;

import com.chaos.model.Skill;
import com.chaos.model.options.fiveoption.FiveOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.entity.impl.player.Player;

public class KnightLamp extends Dialog {
    String msg = "";
    private String bonusLevel(Player player, Skill skill) {
        if (player.getInventory().contains(10586)) {
            player.getInventory().delete(10586, 1);
            player.getSkillManager().setMaxLevel(skill, 99);
            player.getSkillManager().setCurrentLevel(skill, 99);
            player.getSkillManager().setExperience(skill, 99, true);
            player.getPacketSender().sendInterfaceRemoval();
            return "You have boosted your " + skill.getName() + " level to 99.";
        }
        return "You do not have a lamp in your inventory.";
    }

    public Dialog dialog = this;

    public KnightLamp(Player player) {
        super(player);
        setEndState(2);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createStatement(DialogHandler.CALM, "As a Knight you're given an xp lamp that will grant you a 99",  "in a certain combat stats. Please choose which " , "stat you want to make 99.");
            case 1:
                return Dialog.createOption(new FiveOption(
                        "Attack",
                        "Defence",
                        "Strength",
                        "Range",
                        "Magic") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_5:
                                msg = bonusLevel(player, Skill.ATTACK);
                                break;
                            case OPTION_2_OF_5:
                                msg = bonusLevel(player, Skill.DEFENCE);
                                break;
                            case OPTION_3_OF_5:
                                msg = bonusLevel(player, Skill.STRENGTH);
                                break;
                            case OPTION_4_OF_5:
                                msg = bonusLevel(player, Skill.RANGED);
                                break;
                            case OPTION_5_OF_5:
                                msg = bonusLevel(player, Skill.MAGIC);
                                break;
                        }
                    }
                });
            case 2:
                return Dialog.createStatement(DialogHandler.CALM, msg);
        }
        return null;
    }
}
