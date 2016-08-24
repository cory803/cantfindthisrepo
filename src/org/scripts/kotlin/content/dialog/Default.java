package org.scripts.kotlin.content.dialog;

import com.chaos.model.options.fiveoption.FiveOption;
import com.chaos.model.options.fouroption.FourOption;
import com.chaos.model.options.threeoption.ThreeOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.GameMode;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.content.BankPin;
import com.chaos.world.content.PlayerPanel;
import com.chaos.world.entity.impl.player.Player;

public class Default extends Dialog {

    public Dialog dialog = this;

    public GameMode gameMode = null;

    public Default(Player player) {
        super(player);
        setEndState(27);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Welcome to @red@Chaos@bla@ adventurer! Can I help you in any way?");
            case 1:
                return Dialog.createPlayer(DialogHandler.CALM, "Yes, please! How can I get started?");
            case 2:
                return Dialog.createNpc(DialogHandler.CALM, "Im glad you asked! The first thing we need to do is get you setup on a game mode.");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM, "Here at Chaos we have several game modes for you to choose from. We have Sir, Lord, Legend, Extreme, Realism, and Ironman");
            case 4:
                return Dialog.createPlayer(DialogHandler.CALM, "That is a lot to choose from! Where do I start?");
            case 5:
                return Dialog.createNpc(DialogHandler.CALM, "That a wonderful question let me help you out.  What mode would you like to learn about?");
            case 6:
                return Dialog.createOption(new FiveOption(
                        "Learn about Realism mode",
                        "Learn about Extreme mode",
                        "Learn about Legend mode",
                        "Learn about Lord mode",
                        "Next Page:") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_5:
                                setState(8);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_2_OF_5:
                                setState(9);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_3_OF_5:
                                setState(10);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_4_OF_5:
                                setState(11);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_5_OF_5:
                                setState(7);
                                player.getDialog().sendDialog(dialog);
                                break;
                        }
                    }
                });
            case 7:
                return Dialog.createOption(new FourOption(
                        "Learn about Sir mode",
                        "Learn about Ironman mode",
                        "I have choose a game mode!",
                        "<- Back") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_4:
                                setState(12);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_2_OF_4:
                                setState(13);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_3_OF_4:
                                setState(14);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_4_OF_4:
                                setState(6);
                                player.getDialog().sendDialog(dialog);
                                break;
                        }
                    }
                });
            case 8:
                setState(5);
                return Dialog.createNpc(DialogHandler.CALM, "Realism is the most prestigious game mode, on Realism you will be playing on 5x exp rates, you also get 15x drop rate, 40% prayer drain rates, and recover spec every 5 seconds!");
            case 9:
                setState(5);
                return Dialog.createNpc(DialogHandler.CALM, "Extreme mode is the second hardest mode here at Chaos. You will be playing on 15x exp rates. You also get 12x drop rate, 50% prayer drain rates, and recover spec every 8 seconds!");
            case 10:
                setState(5);
                return Dialog.createNpc(DialogHandler.CALM, "On Legend mode you will be playing on 35x exp rates. You also get 10x drop rate, 60% prayer drain rates, and recover spec every 10 seconds!");
            case 11:
                setState(5);
                return Dialog.createNpc(DialogHandler.CALM, "On Lord mode you will be playing on 80x exp rates. You also get 8x drop rate, 75% prayer drain rates, and recover spec every 15 seconds!");
            case 12:
                setState(6);
                return Dialog.createNpc(DialogHandler.CALM, "On Sir mode you will be playing on 125x exp rates. You also get 5x drop rate, and recover spec every 20 seconds!");
            case 13:
                setState(6);
                return Dialog.createNpc(DialogHandler.CALM, "On Ironman you will not be able to use shops or trade with other players. You be on 25x exp rates, 12x drops, 55% prayer drain, and recover spec every 8 seconds!");
            case 14:
                return Dialog.createNpc(DialogHandler.CALM, "So you think you are ready? Okay then what game mode would you like to play on?");
            case 15:
                return Dialog.createOption(new FiveOption(
                        "Realism Mode",
                        "Extreme Mode",
                        "Legend Mode",
                        "Lord Mode",
                        "Next Page:") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_5: {
                                gameMode = GameMode.REALISM;
                                setState(17);
                                player.getDialog().sendDialog(dialog);
                            }
                            case OPTION_2_OF_5: {
                                gameMode = GameMode.EXTREME;
                                setState(17);
                                player.getDialog().sendDialog(dialog);
                            }
                            case OPTION_3_OF_5: {
                                gameMode = GameMode.LEGEND;
                                setState(17);
                                player.getDialog().sendDialog(dialog);
                            }
                            case OPTION_4_OF_5: {
                                gameMode = GameMode.LORD;
                                setState(17);
                                player.getDialog().sendDialog(dialog);
                            }
                            case OPTION_5_OF_5: {
                                setState(16);
                                player.getDialog().sendDialog(dialog);
                            }
                        }
                    }
                });
            case 16:
                return Dialog.createOption(new ThreeOption(
                        "Sir Mode",
                        "Ironman mode",
                        "<- Back") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_3:
                                gameMode = GameMode.SIR;
                                setState(17);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_2_OF_3:
                                gameMode = GameMode._IRONMAN;
                                setState(17);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_3_OF_3:
                                setState(15);
                                player.getDialog().sendDialog(dialog);
                                break;
                        }
                    }
                });
            case 17:
                return Dialog.createPlayer(DialogHandler.CALM, "I want to choose " + gameMode.getModeName() + "!");
            case 18:
                return Dialog.createNpc(DialogHandler.CALM, gameMode.getModeName() + " is the perfect game mode! However I want to want to make sure that is what you want.");
            case 19:
                return Dialog.createOption(new TwoOption(
                        "Yes I would like to play on " + gameMode.getModeName(),
                        "No, I want to choose another game mode!") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_2:
                                setState(20);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_2_OF_2:
                                setState(15);
                                player.getDialog().sendDialog(dialog);
                                break;
                        }
                    }
                });
            case 20:
                getPlayer().getGameModeAssistant().setGameMode(gameMode);
                PlayerPanel.refreshPanel(getPlayer());
                getPlayer().getPacketSender().sendRights();
                return Dialog.createNpc(DialogHandler.CALM, "Perfect, you are now a " + gameMode.getModeName() + "! Would you like me to show you around Chaos?");
            case 21:
                return Dialog.createOption(new TwoOption("Yes, can you please show me around Chaos?", "I think that I can manage from here, thanks!") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_2:
                                setState(24);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_2_OF_2:
                                setState(22);
                                player.getDialog().sendDialog(dialog);
                                break;
                        }
                    }
                });
            case 22:
                return Dialog.createPlayer(DialogHandler.CALM, "I think that I can manage from here, thanks!");
            case 23:
                getPlayer().setContinueSkipTutorial(true);
                setState(26);
                return Dialog.createNpc(DialogHandler.CALM, "Alright, sounds good, however we must first set you up with a bank pin to make sure your account is safe from mischievous players.");
            case 24:
                return Dialog.createPlayer(DialogHandler.CALM, "Yes, can you please show me around Chaos?");
            case 25:
                getPlayer().setContinueTutorial(true);
                return Dialog.createNpc(DialogHandler.CALM, "Of course I can! Lets make sure you are all setup to proceed on your new journey!");
            case 26:
                return Dialog.createNpc(DialogHandler.CALM, "First thing is we are going to set you up with a bank pin to make sure your account is safe from mischievous players.");
            case 27: {
                BankPin.init(getPlayer(), false);
                return null;
            }
        }
        return null;
    }
}
