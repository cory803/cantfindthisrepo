package org.scripts.kotlin.content.dialog.npcs;

import com.chaos.model.options.fouroption.FourOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.util.Misc;
import com.chaos.world.content.Achievements;
import com.chaos.world.entity.impl.player.Player;

public class Max extends Dialog {

    public Dialog dialog = this;

    public Max(Player player) {
        super(player);
        setEndState(10);
    }

    /**
     * Obtain the price of a max cape depending
     * on your game mode.
     * @param player
     * @return
     */
    public int getMaxCapePrice(Player player) {
        switch(player.getGameModeAssistant().getGameMode()) {
            case KNIGHT:
                return 5_000_000;
            case REALISM:
                return 1_000_000;
            case IRONMAN:
                return 500_000;
        }
        return 5_000_000;
    }

    /**
     * Obtain the price of a completionist cape depending
     * on your game mode.
     * @param player
     * @return
     */
    public int getCompletionistCapePrice(Player player) {
        switch(player.getGameModeAssistant().getGameMode()) {
            case KNIGHT:
                return 10_000_000;
            case REALISM:
                return 2_000_000;
            case IRONMAN:
                return 1_000;
        }
        return 10_000_000;
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hello! I distribute all well known capes that prove achievements, accomplishments, and status among Chaos. I only give them to those that are worthy.");
            case 1:
                return Dialog.createOption(new FourOption(
                        "I'd like to buy a Max Cape ("+Misc.formatAmount(getMaxCapePrice(getPlayer()))+")",
                        "I'd like to buy a Completionist Cape ("+Misc.formatAmount(getCompletionistCapePrice(getPlayer()))+")",
                        "I'd like to buy a Veteran Cape (2M)",
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_4:
                                player.getPacketSender().sendInterfaceRemoval();
                                if (!player.getSkillManager().maxStats()) {
                                    player.getPacketSender().sendMessage("You must have 99's in every skill in order to purchase the max cape!");
                                    return;
                                }
                                boolean usePouch = player.getMoneyInPouch() >= getMaxCapePrice(getPlayer());
                                if (!usePouch && player.getInventory().getAmount(995) < getMaxCapePrice(getPlayer())) {
                                    player.getPacketSender().sendMessage("You do not have enough coins.");
                                    return;
                                }
                                if (usePouch) {
                                    player.setMoneyInPouch(player.getMoneyInPouch() - getMaxCapePrice(getPlayer()));
                                    player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
                                } else
                                    player.getInventory().delete(995, getMaxCapePrice(getPlayer()));
                                player.getInventory().add(14019, 1);
                                player.getPacketSender().sendMessage("You've purchased a Max cape.");
                                break;
                            case OPTION_2_OF_4:
                                player.getPacketSender().sendInterfaceRemoval();
                                for (Achievements.AchievementData d : Achievements.AchievementData.values()) {
                                    if (!player.getAchievementAttributes().getCompletion()[d.ordinal()]) {
                                        player.getPacketSender().sendMessage("You must have completed all achievements in order to buy this cape.");
                                        return;
                                    }
                                }
                                boolean usePouch3 = player.getMoneyInPouch() >= getCompletionistCapePrice(getPlayer());
                                if (!usePouch3 && player.getInventory().getAmount(995) < getCompletionistCapePrice(getPlayer())) {
                                    player.getPacketSender().sendMessage("You do not have enough coins.");
                                    return;
                                }
                                if (usePouch3) {
                                    player.setMoneyInPouch(player.getMoneyInPouch() - getCompletionistCapePrice(getPlayer()));
                                    player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
                                } else
                                    player.getInventory().delete(995, getCompletionistCapePrice(getPlayer()));
                                player.getInventory().add(14022, 1);
                                player.getPacketSender().sendMessage("You've purchased a Completionist cape.");
                                break;
                            case OPTION_3_OF_4:
                                if (Misc.getHoursPlayedNumeric(player.getTotalPlayTime()) >= 2000) {
                                    player.getPacketSender().sendInterfaceRemoval();
                                    boolean usePouch2 = player.getMoneyInPouch() >= 2000000;
                                    if (!usePouch2 && player.getInventory().getAmount(995) < 2000000) {
                                        player.getPacketSender().sendMessage("You do not have enough coins.");
                                        return;
                                    }
                                    if (usePouch2) {
                                        player.setMoneyInPouch(player.getMoneyInPouch() - 2000000);
                                        player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
                                    } else
                                        player.getInventory().delete(995, 2000000);
                                    player.getInventory().add(14021, 1);
                                    player.getPacketSender().sendMessage("You've purchased a Veteran cape.");
                                    setState(2);
                                    setEndState(3);
                                    //DialogueManager.start(player, 122);
                                    // player.setDialogueActionId(76);
                                } else {
                                    player.getPacketSender().sendMessage("You've played " + Misc.getHoursPlayedNumeric(player.getTotalPlayTime()) + "/2000 Hours.");
                                    player.getPacketSender().sendInterfaceRemoval();
                                }
                                break;
                            case OPTION_4_OF_4:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
            case 2:
                return Dialog.createNpc(DialogHandler.CALM, "Now that you're a veteran would you like the Veteran rank? It will change your current rank.");
            case 3:
                return Dialog.createOption(new TwoOption(
                        "Yes I want Veteran rank",
                        "No I want to keep my current rank") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_2:
                                //TODO Give Veteran Rank
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
