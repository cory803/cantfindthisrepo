package org.scripts.kotlin.content.dialog;

import com.chaos.model.options.fouroption.FourOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.clan.ClanChatManager;
import com.chaos.world.entity.impl.player.Player;

public class ClanChatDialogue extends Dialog {

    public Dialog dialog = this;

    public ClanChatDialogue(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new FourOption(
                        "Create Clan Chat",
                        "Edit Clan Chat",
                        "Delete Clan Chat",
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_4:
                                ClanChatManager.createClan(player);
                                break;
                            case OPTION_2_OF_4:
                                ClanChatManager.clanChatSetupInterface(player, true);
                                break;
                            case OPTION_3_OF_4:
                                ClanChatManager.deleteClan(player);
                                break;
                            case OPTION_4_OF_4:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
        }
        return null;
    }
}
