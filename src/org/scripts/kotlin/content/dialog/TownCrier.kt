package org.scripts.kotlin.content.dialog

import com.runelive.model.input.impl.ChangePassword
import com.runelive.model.input.impl.SetEmail
import com.runelive.model.options.fiveoption.FiveOption
import com.runelive.model.options.twooption.TwoOption
import com.runelive.model.player.GameMode
import com.runelive.model.player.dialog.Dialog
import com.runelive.model.player.dialog.DialogMessage
import com.runelive.world.content.BankPin
import com.runelive.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/17/2016.

 * @author Seba
 */
class TownCrier(player: Player) : Dialog(player) {

    internal var dialog: Dialog = this

    internal var gameMode: GameMode? = null

    init {
        setEndState(14)
    }

    override fun getMessage(): DialogMessage? {
        when (state) {
            0 -> return createNpc("Hello there, " + player.username + ", I have the ability to help you with your account settings. What would you like to do?")
            1 -> return createOption(object : FiveOption(
                    "Set email-address (used when recovering your password)",
                    if (player.bankPinAttributes.hasBankPin()) "Delete your account pin." else "Set account-pin (used to protect against hackers)",
                    "Change Gamemode",
                    "Change Password",
                    "Cancel") {
                override fun execute(player: Player, option: OptionType) {
                    when (option) {
                        OptionType.OPTION_1_OF_5 -> {
                            setEndState(state)
                            player.inputHandling = SetEmail()
                            player.packetSender.sendEnterInputPrompt("Enter your email address:")
                        }
                        OptionType.OPTION_2_OF_5 -> {
                            setEndState(state)
                            if (player.bankPinAttributes.hasBankPin()) {
                                player.optionContainer.display(object : TwoOption("Delete bank pin", "Cancel") {
                                    override fun execute(player: Player, option: OptionType) {
                                        when (option) {
                                            OptionType.OPTION_1_OF_2 -> BankPin.deletePin(player)
                                            OptionType.OPTION_2_OF_2 -> player.packetSender.sendInterfaceRemoval()
                                        }
                                    }
                                })
                            } else {
                                BankPin.init(player, false)
                            }
                        }
                        OptionType.OPTION_3_OF_5 -> {
                            state = 2
                            player.dialog.sendDialog(dialog)
                        }
                        OptionType.OPTION_4_OF_5 -> {
                            setEndState(state)
                            player.inputHandling = ChangePassword()
                            player.packetSender.sendEnterInputPrompt("Enter a new password:")
                        }
                        OptionType.OPTION_5_OF_5 -> player.packetSender.sendInterfaceRemoval()
                    }
                }
            })
            2 -> {
                if (!player.equipment.isNaked) {
                    setEndState(state)
                    return createNpc("You need to have all equipment removed to change game modes")
                }
                return createNpc("Awh, so I see you want to change game modes? Let me first explain how this process works.")
            }
            3 -> return createNpc("When you are moving from a higher game mode to a lower one, all your stats and achievements will be reset to ensure " + "that it is fair for everyone.")
            4 -> return createNpc("However, if you are moving from a lower game mode to a higher one, your skills will be adjust to that of the game mode you choose.")
            5 -> return createNpc("After hearing about how game mode switching works would you still like to proceed?")
            6 -> return createOption(object : TwoOption("Yes, I still want to change game modes.", "No thanks, I will stick with my current game mode.") {
                override fun execute(player: Player, option: OptionType) {
                    when (option) {
                        OptionType.OPTION_1_OF_2 -> {
                            state = 7
                            player.dialog.sendDialog(dialog)
                        }
                        OptionType.OPTION_2_OF_2 -> player.packetSender.sendInterfaceRemoval()
                    }
                }
            })
            7 -> return createPlayer("Yes, I still want to change game modes.")
            8 -> return createNpc("Perfect, What game mode would you then like to change to?")
            9 -> {
                if (player.gameModeAssistant.gameMode == GameMode.IRONMAN) {
                    return createOption(object : FiveOption(
                            "Play in @blu@Realism@bla@ mode (@blu@5x@bla@ XP)",
                            "Play in @blu@Extreme@bla@ mode (@blu@15x@bla@ XP)",
                            "Play in @blu@Legend@bla@ mode (@blu@35x@bla@ XP)",
                            "Play in @blu@Lord@bla@ mode (@blu@80x@bla@ XP)",
                            "Play in @blu@Ironman@bla@ mode (@blu@25x@bla@ XP)") {
                        override fun execute(player: Player, option: OptionType) {
                            when (option) {
                                OptionType.OPTION_1_OF_5 -> gameMode = GameMode.REALISM
                                OptionType.OPTION_2_OF_5 -> gameMode = GameMode.EXTREME
                                OptionType.OPTION_3_OF_5 -> gameMode = GameMode.LEGEND
                                OptionType.OPTION_4_OF_5 -> gameMode = GameMode.LORD
                                OptionType.OPTION_5_OF_5 -> gameMode = GameMode._IRONMAN
                            }
                            state = 10
                            player.dialog.sendDialog(dialog)
                        }
                    })
                } else {
                    return createOption(object : FiveOption(
                            "Play in @blu@Realism@bla@ mode (@blu@5x@bla@ XP)",
                            "Play in @blu@Extreme@bla@ mode (@blu@15x@bla@ XP)",
                            "Play in @blu@Legend@bla@ mode (@blu@35x@bla@ XP)",
                            "Play in @blu@Lord@bla@ mode (@blu@80x@bla@ XP)",
                            "Play in @blu@Sir@bla@ mode (@blu@125x@bla@ XP)") {
                        override fun execute(player: Player, option: OptionType) {
                            when (option) {
                                OptionType.OPTION_1_OF_5 -> gameMode = GameMode.REALISM
                                OptionType.OPTION_2_OF_5 -> gameMode = GameMode.EXTREME
                                OptionType.OPTION_3_OF_5 -> gameMode = GameMode.LEGEND
                                OptionType.OPTION_4_OF_5 -> gameMode = GameMode.LORD
                                OptionType.OPTION_5_OF_5 -> gameMode = GameMode.SIR
                            }
                            state = 10
                            player.dialog.sendDialog(dialog)
                        }
                    })
                }
            }
            10 -> {
                if (player.gameModeAssistant.gameMode == gameMode) {
                    setEndState(state)
                    return createNpc("You are already a " + player.gameModeAssistant.modeName + " and therefore cannot change to the game game mode.")
                } else {
                    return createNpc("So you would like to change to " + gameMode!!.modeName + "?")
                }
            }
            11 -> return createOption(object : TwoOption("Yes, I would like to change to " + gameMode!!.modeName, "No, I changed my mind.") {
                override fun execute(player: Player, option: OptionType) {
                    when (option) {
                        OptionType.OPTION_1_OF_2 -> {
                            state = 12
                            player.dialog.sendDialog(dialog)
                        }
                        OptionType.OPTION_2_OF_2 -> player.packetSender.sendInterfaceRemoval()
                    }
                }
            })
            12 -> return createNpc("I will ask one more time, once you switch to " + gameMode!!.modeName + " it is permanent and cannot be reversed. Are you sure you want to switch modes?")
            13 -> return createOption(object : TwoOption("Yes, I would like to change to " + gameMode!!.modeName, "No, I changed my mind.") {
                override fun execute(player: Player, option: OptionType) {
                    when (option) {
                        OptionType.OPTION_1_OF_2 -> {
                            player.gameModeAssistant.setNewGamemode(gameMode)
                            state = 14
                            player.dialog.sendDialog(dialog)
                        }
                        OptionType.OPTION_2_OF_2 -> player.packetSender.sendInterfaceRemoval()
                    }
                }
            })
            14 -> return createNpc("Congratulations, you are now playing in " + gameMode!!.modeName)
        }
        return null
    }
}
