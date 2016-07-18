package org.scripts.kotlin.content.dialog;

import com.runelive.model.options.Option;
import com.runelive.model.options.fiveoption.FiveOption;
import com.runelive.model.options.fouroption.FourOption;
import com.runelive.model.options.threeoption.ThreeOption;
import com.runelive.model.options.twooption.TwoOption;
import com.runelive.model.player.GameMode;
import com.runelive.model.player.dialog.Dialog;
import com.runelive.model.player.dialog.DialogMessage;
import com.runelive.world.entity.impl.player.Player;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/18/2016.
 *
 * @author Seba
 */
public class Tutorial extends Dialog {

    STAGE stage = STAGE.START;

    GameMode gameMode;

    private enum STAGE {
        START, TUTORIAL
    }

    public Tutorial(Player player) {
        super(player);
        player.setNpcClickId(945);
    }

    @Override
    public DialogMessage getMessage() {
        return getMessage(stage);
    }

    public DialogMessage getMessage(STAGE stage) {
        switch (stage) {
            case START:
                setEndState(1);
                switch (getState()) {
                    case 0:
                        return createNpc("Welcome to @red@RuneLive@bla@ adventurer! Can I help you in any way?");
                    case 1:
                        return createPlayer("Yes, please! How can I get started?");
                    case 2:
                        return createNpc("Im glad you asked! The first thing we need to do is get you setup on a game mode.");
                    case 3:
                        return createNpc("Here at RuneLive we have several game modes for you to choose from. We have Sir, Lord, Legend, Extreme, Realism, and Ironman");
                    case 4:
                        return createPlayer("That is a lot to choose from! Where do I start?");
                    case 5:
                        return createNpc("That a wonderful question let me help you out.  What mode would you like to learn about?");
                    case 6:
                        return createOption(new FiveOption(
                                "Learn about Realism mode",
                                "Learn about Extreme mode",
                                "Learn about Legend mode",
                                "Learn about Lord mode",
                                "Next Page ->"
                        ) {
                            @Override
                            public void execute(Player player, OptionType option) {

                            }
                        });
                    case 7:
                        return createOption(new FourOption(
                                "Learn about Sir mode",
                                "Learn about Ironman mode",
                                "I have choose a game mode!",
                                "<- Back"
                        ) {
                            @Override
                            public void execute(Player player, OptionType option) {

                            }
                        });
                    case 8:
                        setState(6);
                        return createNpc("Awh, Realism is the most prestigious game mode here at RuneLive. On Realism you will be playing on 5x exp rates. You also get 15x drop rate, 40% prayer drain rates, and recover spec every 5 seconds!");
                    case 9:
                        setState(6);
                        return createNpc("Extreme mode is the second hardest mode here at RuneLive. You will be playing on 15x exp rates. You also get 12x drop rate, 50% prayer drain rates, and recover spec every 8 seconds!");
                    case 10:
                        setState(6);
                        return createNpc("On Legend mode you will be playing on 35x exp rates. You also get 10x drop rate, 60% prayer drain rates, and recover spec every 10 seconds!");
                    case 11:
                        setState(6);
                        return createNpc("On Lord mode you will be playing on 80x exp rates. You also get 8x drop rate, 75% prayer drain rates, and recover spec every 15 seconds!");
                    case 12:
                        setState(7);
                        return createNpc("On Sir mode you will be playing on 125x exp rates. You also get 5x drop rate, and recover spec every 20 seconds!");
                    case 13:
                        setState(7);
                        return createNpc("Ironman is one of the toughest modes here. You will not be able to use shops or trade with other players. You be on 25x exp rates, 12x drops, 55% prayer drain, and recover spec every 8 seconds!");
                    case 14:
                        return createNpc("So you think you are ready? Okay then what game mode would you like to play on?");
                    case 15:
                        return createOption(new FiveOption(
                                "Realism Mode",
                                "Extreme Mode",
                                "Legend Mode",
                                "Lord Mode",
                                "Next Page ->"
                        ) {
                            @Override
                            public void execute(Player player, OptionType option) {

                            }
                        });
                    case 16:
                        return createOption(new ThreeOption(
                                "Sir Mode",
                                "Ironman mode",
                                "<- Back"
                        ) {
                            @Override
                            public void execute(Player player, OptionType option) {

                            }
                        });
                    case 17:
                        return createPlayer("I want to choose " + gameMode.getModeName() + "!");
                    case 18:
                        return createNpc(gameMode.getModeName() + " is the perfect game mode! However I want to want to make sure that is what you want.");
                    case 19:
                        return createOption(new TwoOption(
                                "Yes I would like to play on " + gameMode.getModeName(),
                                "No, I want to choose another game mode!"
                        ) {
                            @Override
                            public void execute(Player player, OptionType option) {

                            }
                        });
                    case 20:
                        return createNpc("Perfect, you are now a " + gameMode.getModeName() + "! Now we need to set you up with a bank pin, this will protect your account from mischievous people!");
                }
        }

        return null;
    }
}
