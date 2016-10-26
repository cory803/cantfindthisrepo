package org.scripts.kotlin.content.dialog.npcs;

import com.chaos.model.container.impl.Shop;
import com.chaos.model.options.fouroption.FourOption;
import com.chaos.model.options.threeoption.ThreeOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.util.Misc;
import com.chaos.world.content.diversions.hourly.ShootingStar;
import com.chaos.world.entity.impl.player.Player;

public class Donovan extends Dialog {

    public Donovan(Player player, int state) {
        super(player);
        setState(state);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new FourOption(
                        "Can I view your rares store?",
                        "Can I view your armour/weapons store?",
                        "I want to buy scrolls from the website.",
                        "How many donator points do I have?") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            /**
                             * Weapons & armour
                             */
                            case OPTION_1_OF_4:
                                Shop.ShopManager.getShops().get(43).open(player);
                                break;

                            /**
                             * Rares
                             */
                            case OPTION_2_OF_4:
                                Shop.ShopManager.getShops().get(42).open(player);
                                break;

                            /**
                             * Buy scrolls @ website
                             */
                            case OPTION_3_OF_4:
                                player.getPacketSender().sendString(1, "www.chaosps.com/store/");
                                setEndState(1);
                                player.getDialog().sendDialog(new Donovan(player, 1));
                                break;

                            /**
                             * Check how many points you have
                             */
                            case OPTION_4_OF_4:
                                setEndState(2);
                                player.getDialog().sendDialog(new Donovan(player, 2));
                                break;
                        }
                    }
                });
            case 1:
                setEndState(1);
                return Dialog.createStatement(DialogHandler.JOYOUS_TALK, "Opening website...", "~ www.chaosps.com/store ~");
            case 2:
                setEndState(2);
                return Dialog.createStatement(DialogHandler.JOYOUS_TALK, "You currently have "+ Misc.format(getPlayer().getPoints())+" donator points.");
        }
        return null;
    }
}
