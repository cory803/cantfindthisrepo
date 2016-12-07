package org.scripts.kotlin.content.dialog;

import com.chaos.model.options.fouroption.FourOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.GameMode;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.world.entity.impl.player.Player;

public class ChangeGameMode extends Dialog {

    public Dialog next = this;

    private boolean resetStats = getPlayer().getGameModeAssistant().getGameMode() == GameMode.KNIGHT;
    private boolean resetItems = getPlayer().getGameModeAssistant().getGameMode() == GameMode.IRONMAN;

    private String newMode = "";
    private String currentMode = getPlayer().getGameModeAssistant().getModeName();

    private boolean sameMode(Player player, GameMode mode) {
        player.getPacketSender().sendInterfaceRemoval();
        if (player.getGameModeAssistant().getGameMode() == mode) {
            player.getPacketSender().sendMessage("You cannot change your game mode to the mode you're already on.");
            setEndState(1);
            return true;
        }
        return false;
    }


    public ChangeGameMode(Player player) {
        super(player);
        setEndState(3);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.PLAIN_EVIL, "Hello, would you like to change your game mode?");
            case 1:
                setEndState(1);
                return Dialog.createOption(new FourOption(
                        "I want to be a Knight",
                        "I want to be Realism",
                        "I want to be an Iron Man",
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_4:
                                if (!sameMode(getPlayer(), GameMode.KNIGHT)) {
                                    newMode = "Knight";
                                    setState(2);
                                    getPlayer().getDialog().sendDialog(next);
                                }
                                break;
                            case OPTION_2_OF_4:
                                if (!sameMode(getPlayer(), GameMode.REALISM)) {
                                    newMode = "Realism";
                                    setState(2);
                                    getPlayer().getDialog().sendDialog(next);
                                }
                                break;
                            case OPTION_3_OF_4:
                                if (!sameMode(getPlayer(), GameMode.IRONMAN)) {
                                    newMode = "Iron Man";
                                    setState(2);
                                    getPlayer().getDialog().sendDialog(next);
                                }
                                break;
                            case OPTION_4_OF_4:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
            case 2:
                if (getPlayer().getGameModeAssistant().isIronMan()) {
                    return Dialog.createNpc(DialogHandler.PLAIN_EVIL, "When changing from a @blu@" + currentMode + "@bla@ to a @blu@" + newMode + "@bla@ I can allow you to keep your items and stats. This @red@cannot be undone@bla@.");
                } else if (getPlayer().getGameModeAssistant().getGameMode() == GameMode.REALISM && newMode != "Iron Man") {
                    return Dialog.createNpc(DialogHandler.PLAIN_EVIL, "When changing from a @blu@" + currentMode + "@bla@ to a @blu@" + newMode + "@bla@ I can allow you to keep your items and stats. This @red@cannot be undone@bla@");
                } else if (getPlayer().getGameModeAssistant().getGameMode() == GameMode.KNIGHT && newMode == "Iron Man") {
                    return Dialog.createNpc(DialogHandler.PLAIN_EVIL, "When changing from a @blu@" + currentMode + "@bla@ to a @blu@" + newMode + "@bla@ I have to take all your items, stats and achievements. This @red@cannot be undone@bla@");
                } else if (getPlayer().getGameModeAssistant().getGameMode() == GameMode.KNIGHT && newMode == "Realism") {
                    return Dialog.createNpc(DialogHandler.PLAIN_EVIL, "When changing from a @blu@" + currentMode + "@bla@ to a @blu@" + newMode + "@bla@ I can allow you to keep your items and but NOT your stats. This @red@cannot be undone@bla@.");
                } else {
                }
            case 3:
                return Dialog.createOption(new TwoOption(
                        "Yes change my mode from " + currentMode + " to " + newMode,
                        "No I want to stay as a(n) " + currentMode) {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                    if (currentMode == "Knight") {
                                        switch (newMode) {
                                            case "Realism":
                                                player.getPacketSender().sendInterfaceRemoval();
                                                player.getPacketSender().sendMessage("You have just changed your game mode from " + currentMode + " to " + newMode + ".");
                                                player.getGameModeAssistant().setGameMode(GameMode.REALISM);
                                                player.getGameModeAssistant().resetStats(player, false);
                                                break;
                                            case "Iron Man":
                                                player.getPacketSender().sendInterfaceRemoval();
                                                player.getPacketSender().sendMessage("You have just changed your game mode from " + currentMode + " to " + newMode + ".");
                                                player.getGameModeAssistant().setGameMode(GameMode.IRONMAN);
                                                player.getBank(0).resetBank(player);
                                                player.getGameModeAssistant().resetStats(player, true);
                                                break;
                                            default:
                                                player.getPacketSender().sendMessage("Error changing game mode....");
                                        }
                                    } else if (currentMode == "Realism") {
                                        switch (currentMode) {
                                            case "Knight":
                                                player.getPacketSender().sendInterfaceRemoval();
                                                player.getPacketSender().sendMessage("You have just changed your game mode from " + currentMode + " to " + newMode + ".");
                                                player.getGameModeAssistant().setGameMode(GameMode.KNIGHT);
                                                player.save();
                                                break;
                                            case "Iron Man":
                                                player.getPacketSender().sendInterfaceRemoval();
                                                player.getPacketSender().sendMessage("You have just changed your game mode from " + currentMode + " to " + newMode + ".");
                                                player.getGameModeAssistant().setGameMode(GameMode.IRONMAN);
                                                player.getBank(0).resetBank(player);
                                                player.getGameModeAssistant().resetStats(player, true);
                                                break;
                                            default:
                                                player.getPacketSender().sendMessage("Error changing game mode....");
                                        }

                                    } else if (currentMode == "Iron Man") {
                                        switch (currentMode) {
                                            case "Knight":
                                                player.getPacketSender().sendInterfaceRemoval();
                                                player.getPacketSender().sendMessage("You have just changed your game mode from " + currentMode + " to " + newMode + ".");
                                                player.getGameModeAssistant().setGameMode(GameMode.KNIGHT);
                                                player.save();
                                                break;
                                            case "Realism":
                                                player.getPacketSender().sendInterfaceRemoval();
                                                player.getPacketSender().sendMessage("You have just changed your game mode from " + currentMode + " to " + newMode + ".");
                                                player.getGameModeAssistant().setGameMode(GameMode.REALISM);
                                                player.save();
                                                break;
                                            default:
                                                player.getPacketSender().sendMessage("Error changing game mode....");
                                        }
                                    }
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
}
