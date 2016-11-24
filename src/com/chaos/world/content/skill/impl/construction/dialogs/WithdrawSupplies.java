package com.chaos.world.content.skill.impl.construction.dialogs;

import com.chaos.model.options.fiveoption.FiveOption;
import com.chaos.model.options.fouroption.FourOption;
import com.chaos.model.options.threeoption.ThreeOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.entity.impl.player.Player;

public class WithdrawSupplies extends Dialog {

    int type;

    public WithdrawSupplies(Player player, int type) {
        super(player);
        setEndState(0);
        this.type = type;
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                switch (type) {
                //423 == 1
                    case 1:
                        return Dialog.createOption(new ThreeOption(
                                "Kettle",
                                "Teapot",
                                "Clay cup") {
                            @Override
                            public void execute(Player player, OptionType option) {
                                switch(option) {
                                    case OPTION_1_OF_3:
                                        player.getInventory().add(7688, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_2_OF_3:
                                        player.getInventory().add(7702, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_3_OF_3:
                                        player.getInventory().add(7728, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                }
                            }
                        });
                    //424 == 2
                    case 2:
                        return Dialog.createOption(new FourOption(
                                "Kettle",
                                "Teapot",
                                "Clay cup",
                                "Beer glass") {
                            @Override
                            public void execute(Player player, OptionType option) {
                                switch(option) {
                                    case OPTION_1_OF_4:
                                        player.getInventory().add(7688, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_2_OF_4:
                                        player.getInventory().add(7702, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_3_OF_4:
                                        player.getInventory().add(7728, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_4_OF_4:
                                        player.getInventory().add(1919, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                }
                            }
                        });
                    //425 == 3
                    case 3:
                        return Dialog.createOption(new FiveOption(
                                "Kettle",
                                "Teapot",
                                "Porcelain cup",
                                "Beer glass",
                                "Cake tin") {
                            @Override
                            public void execute(Player player, OptionType option) {
                                switch(option) {
                                    case OPTION_1_OF_5:
                                        player.getInventory().add(7688, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_2_OF_5:
                                        player.getInventory().add(7702, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_3_OF_5:
                                        player.getInventory().add(7732, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_4_OF_5:
                                        player.getInventory().add(1919, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_5_OF_5:
                                        player.getInventory().add(1887, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                }
                            }
                        });
                    //426 == 4
                    case 4:
                        return Dialog.createOption(new FiveOption(
                                "Kettle",
                                "Teapot",
                                "Clay cup",
                                "Beer glass",
                                "Bowl") {
                            @Override
                            public void execute(Player player, OptionType option) {
                                switch(option) {
                                    case OPTION_1_OF_5:
                                        player.getInventory().add(7688, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_2_OF_5:
                                        player.getInventory().add(7714, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_3_OF_5:
                                        player.getInventory().add(7732, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_4_OF_5:
                                        player.getInventory().add(1919, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_5_OF_5:
                                        player.getInventory().add(1923, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                }
                            }
                        });
                    //427 == 5
                    case 5:
                        return Dialog.createOption(new FiveOption(
                                "Kettle",
                                "Teapot",
                                "Porcelain cup",
                                "Beer glass",
                                "Next") {
                            @Override
                            public void execute(Player player, OptionType option) {
                                switch(option) {
                                    case OPTION_1_OF_5:
                                        player.getInventory().add(7688, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_2_OF_5:
                                        player.getInventory().add(7714, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_3_OF_5:
                                        player.getInventory().add(7732, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_4_OF_5:
                                        player.getInventory().add(1919, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_5_OF_5:
                                        player.getDialog().sendDialog(new WithdrawSupplies(player, 6));
                                        break;
                                }
                            }
                        });
                    //428 == 6
                    case 6:
                        return Dialog.createOption(new ThreeOption(
                                "Bowl",
                                "Cake tin",
                                "Back") {
                            @Override
                            public void execute(Player player, OptionType option) {
                                switch(option) {
                                    case OPTION_1_OF_3:
                                        player.getInventory().add(1923, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_2_OF_3:
                                        player.getInventory().add(1887, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_3_OF_3:
                                        player.getDialog().sendDialog(new WithdrawSupplies(player, 5));
                                        break;
                                }
                            }
                        });
                    //429 == 7
                    case 7:
                        return Dialog.createOption(new FiveOption(
                                "Kettle",
                                "Teapot",
                                "Porcelain cup",
                                "Beer glass",
                                "Next") {
                            @Override
                            public void execute(Player player, OptionType option) {
                                switch(option) {
                                    case OPTION_1_OF_5:
                                        player.getInventory().add(7688, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_2_OF_5:
                                        player.getInventory().add(7726, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_3_OF_5:
                                        player.getInventory().add(7732, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_4_OF_5:
                                        player.getInventory().add(1919, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_5_OF_5:
                                        player.getDialog().sendDialog(new WithdrawSupplies(player, 8));
                                        break;
                                }
                            }
                        });
                    //430 == 8
                    case 8:
                        return Dialog.createOption(new FourOption(
                                "Bowl",
                                "Pie dish",
                                "Empty pot",
                                "Back") {
                            @Override
                            public void execute(Player player, OptionType option) {
                                switch(option) {
                                    case OPTION_1_OF_4:
                                        player.getInventory().add(1923, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_2_OF_4:
                                        player.getInventory().add(2313, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_3_OF_4:
                                        player.getInventory().add(1931, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_4_OF_4:
                                        player.getDialog().sendDialog(new WithdrawSupplies(player, 7));
                                        break;
                                }
                            }
                        });
                    //431 == 9
                    case 9:
                        return Dialog.createOption(new FiveOption(
                                "Kettle",
                                "Teapot",
                                "Porcelain cup",
                                "Beer glass",
                                "Next") {
                            @Override
                            public void execute(Player player, OptionType option) {
                                switch(option) {
                                    case OPTION_1_OF_5:
                                        player.getInventory().add(7688, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_2_OF_5:
                                        player.getInventory().add(7726, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_3_OF_5:
                                        player.getInventory().add(7732, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_4_OF_5:
                                        player.getInventory().add(1919, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_5_OF_5:
                                        player.getDialog().sendDialog(new WithdrawSupplies(player, 10));
                                        break;
                                }
                            }
                        });
                    //432 == 10
                    case 10:
                        return Dialog.createOption(new FiveOption(
                                "Bowl",
                                "Pie dish",
                                "Empty pot",
                                "Chef's hat",
                                "Back") {
                            @Override
                            public void execute(Player player, OptionType option) {
                                switch(option) {
                                    case OPTION_1_OF_5:
                                        player.getInventory().add(1923, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_2_OF_5:
                                        player.getInventory().add(2313, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_3_OF_5:
                                        player.getInventory().add(1931, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_4_OF_5:
                                        player.getInventory().add(1949, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_5_OF_5:
                                        player.getDialog().sendDialog(new WithdrawSupplies(player, 9));
                                        break;
                                }
                            }
                        });
                    //433 == 11
                    case 11:
                        return Dialog.createOption(new TwoOption(
                                "Tea leaves",
                                "Bucket of milk") {
                            @Override
                            public void execute(Player player, OptionType option) {
                                switch(option) {
                                    case OPTION_1_OF_2:
                                        player.getInventory().add(7738, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_2_OF_2:
                                        player.getInventory().add(1927, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                }
                            }
                        });
                    //434 == 12
                    case 12:
                        return Dialog.createOption(new FourOption(
                                "Tea leaves",
                                "Bucket of milk",
                                "Egg",
                                "Pot of flour") {
                            @Override
                            public void execute(Player player, OptionType option) {
                                switch(option) {
                                    case OPTION_1_OF_4:
                                        player.getInventory().add(7738, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_2_OF_4:
                                        player.getInventory().add(1927, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_3_OF_4:
                                        player.getInventory().add(1944, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_4_OF_4:
                                        player.getInventory().add(1933, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                }
                            }
                        });
                    //435 == 13
                    case 13:
                        return Dialog.createOption(new FiveOption(
                                "Tea leaves",
                                "Bucket of milk",
                                "Egg",
                                "Pot of flour",
                                "Next") {
                            @Override
                            public void execute(Player player, OptionType option) {
                                switch(option) {
                                    case OPTION_1_OF_5:
                                        player.getInventory().add(7738, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_2_OF_5:
                                        player.getInventory().add(1927, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_3_OF_5:
                                        player.getInventory().add(1944, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_4_OF_5:
                                        player.getInventory().add(1933, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_5_OF_5:
                                        player.getDialog().sendDialog(new WithdrawSupplies(player, 14));
                                        break;
                                }
                            }
                        });
                    //436 == 14
                    case 14:
                        return Dialog.createOption(new FiveOption(
                                "Potato",
                                "Garlic",
                                "Onion",
                                "Cheese",
                                "Back") {
                            @Override
                            public void execute(Player player, OptionType option) {
                                switch(option) {
                                    case OPTION_1_OF_5:
                                        player.getInventory().add(1942, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_2_OF_5:
                                        player.getInventory().add(1550, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_3_OF_5:
                                        player.getInventory().add(1957, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_4_OF_5:
                                        player.getInventory().add(1985, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                        break;
                                    case OPTION_5_OF_5:
                                        player.getDialog().sendDialog(new WithdrawSupplies(player, 13));
                                        break;
                                }
                            }
                        });
                }
        }
        return null;
    }
}